<script setup lang="ts">
import { GeocoderPageLayout, GeocoderSolverPanelLayout } from '../../layout'
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import SolverPanel from './SolverPanel.vue'
import { type GeocoderProblem, GeocoderSolution, type GeocoderSolutionRequest } from '../../api'
import { type AfterFetchContext, useFetch, useWebSocket, watchOnce } from '@vueuse/core'
import SolverMap from '../../views/GeocoderSolver/SolverMap.vue'

const route = useRoute()

const solverStatus = ref<string | null>(null)
const selectedSolver = ref<string>('')

const webSocketUrl = computed(() => `ws://${location.host}/ws/solution-state/${route.params.id}`)
const solutionPanelUrl = computed(() => `/api/solver/${route.params.id}/solution`)
const refetchSolution = ref(true)
const solverNamesUrl = ref('/api/solver/solver-names')

const problemUrl = computed(() => `/api/problems/${route.params.id}`)
const solveUrl = computed(() => `/api/solver/${route.params.id}/solve/${selectedSolver.value}`)
const terminateUrl = computed(() => `/api/solver/${route.params.id}/terminate`)
const cleanUrl = computed(() => `/api/solver/${route.params.id}/clean`)

const { status, data: wsData, open: wsOpen } = useWebSocket<string>(webSocketUrl, { immediate: true })

const { data: problem } = useFetch(problemUrl, { immediate: true }).get().json<GeocoderProblem>()

const { execute: fetchProblem, data: solution } = useFetch(solutionPanelUrl, {
	refetch: refetchSolution,
	afterFetch: afterFetchSolution,
	immediate: true,
	timeout: 5000,
	updateDataOnError: true,
})
	.get()
	.json<GeocoderSolutionRequest>()

const { isFetching, data: solvers } = useFetch(solverNamesUrl, {
	initialData: [],
	immediate: true,
})
	.get()
	.json<string[]>()

const { data: solveStatus, execute: solve } = useFetch(solveUrl, {
	immediate: false,
	afterFetch: afterSolutionFetch,
}).post().json<string>()

const { execute: terminate } = useFetch(terminateUrl, { immediate: false }).post().json<string>()
const { execute: clean } = useFetch(cleanUrl, { immediate: false }).post().json<string>()

function afterFetchSolution(ctx: AfterFetchContext<GeocoderSolutionRequest>) {
	refetchSolution.value = ctx.data?.status === 'ENQUEUED' || ctx.data?.status === 'RUNNING'
	solverStatus.value = ctx.data?.status || 'NOT_SOLVED'

	if (refetchSolution.value) {
		setTimeout(() => fetchProblem(), 5000)
	}

	return ctx
}

async function solveAction() {
	if (problem.value) {
		await solve()
		solverStatus.value = solveStatus.value || 'NOT_SOLVED'
		await fetchProblem()
	}
}

async function terminateAction() {
	if (problem.value) {
		await terminate()
		solverStatus.value = 'TERMINATED'
	}
}

async function cleanAction() {
	if (problem.value) {
		await clean()
		solverStatus.value = 'NOT_SOLVED'
	}
}

watchOnce(solvers, () => {
	selectedSolver.value = (solvers.value && solvers.value[0]) || ''
})

watch(wsData, () => {
	if (wsData.value) {
		const payload = JSON.parse(wsData.value)
		solution.value = payload.solution
		solverStatus.value = payload.status
	}
})

function afterSolutionFetch(ctx: AfterFetchContext) {
	wsOpen()
	return ctx
}

</script>

<template>
	<geocoder-page-layout v-slot="{ mapFooterHeight }" :is-fetching="isFetching" :error="null">
		<geocoder-solver-panel-layout>
			<template #menu>
				<solver-panel
					v-model:selected-solver="selectedSolver"
					:solution="solution?.solution as GeocoderSolution"
					:problem="problem"
					:solvers="solvers || []"
					:solver-status="solverStatus"
					:ws-status="status"
					:style="`height: calc(100vh - ${mapFooterHeight})`"
					@on-solve="solveAction"
					@on-terminate="terminateAction"
					@on-clear="cleanAction"
				/>
			</template>
			<template #main>
				<solver-map
					v-if="(problem?.points || []).length > 0"
					:solution="solution?.solution as GeocoderSolution"
					:problem="problem"
				/>
			</template>
		</geocoder-solver-panel-layout>
	</geocoder-page-layout>
</template>
