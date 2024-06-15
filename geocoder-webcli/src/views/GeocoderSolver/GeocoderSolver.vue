<script setup lang="ts">

import { GeocoderPageLayout, GeocoderSolverPanelLayout } from '../../layout'
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import SolverPanel from './SolverPanel.vue'
import { type GeocoderProblem, GeocoderSolution } from '../../api'
import { useFetch } from '@vueuse/core'
import SolverMap from '../../views/GeocoderSolver/SolverMap.vue'

const route = useRoute()

const solverStatus = ref<string | null>(null)
const solution = ref<GeocoderSolution | null>(null)
// const problem = ref<GeocoderProblem | null>(null)
const selectedSolver = ref<string>('')

// const solutionPanelUrl = computed(() => `/api/solver/${route.params.id}/solution`)
const solverNamesUrl = ref('/api/solver/solver-names')

const problemUrl = computed(() => `/api/problems/${route.params.id}`)
const solveUrl = computed(() => `/api/solver/${route.params.id}/solve/${selectedSolver.value}`)
const terminateUrl = computed(() => `/api/solver/${route.params.id}/terminate`)
const cleanUrl = computed(() => `/api/solver/${route.params.id}/clean`)

const { data: problem } = useFetch(problemUrl, { immediate: true }).get().json<GeocoderProblem>()

// const {
// 	isFetching,
// 	error,
// 	data: solutionPanel,
// } = useFetch(solutionPanelUrl).get().json<GeocoderSolutionRequest>()

const {
	isFetching,
	error,
	data: solvers,
} = useFetch(solverNamesUrl, { initialData: [], immediate: true }).get().json<string[]>()

const { data: solveStatus, execute: solve } = useFetch(solveUrl, { immediate: false }).post().json<string>()
const { execute: terminate } = useFetch(terminateUrl, { immediate: false }).post().json<string>()
const { execute: clean } = useFetch(cleanUrl, { immediate: false }).post().json<string>()

async function solveAction() {
	if (problem.value) {
		await solve()
		solverStatus.value = solveStatus.value || null
	}
}

async function terminateAction() {
	if (problem.value) {
		await terminate()
	}
}

async function cleanAction() {
	if (problem.value) {
		await clean()
	}
}
</script>

<template>
	<geocoder-page-layout v-slot="{ mapFooterHeight }" :is-fetching="isFetching" :error="error">
		<geocoder-solver-panel-layout>
			<template #menu>
				<solver-panel
					v-model:selected-solver="selectedSolver"
					:solution="solution"
					:problem="problem"
					:solvers="solvers || []"
					:solver-status="solverStatus"
					:style="`height: calc(100vh - ${mapFooterHeight})`"
					@on-solve="solveAction"
					@on-terminate="terminateAction"
					@on-clear="cleanAction"
				/>
			</template>
			<template #main>
				<solver-map
					v-if="problem?.points.length > 0"
					:solution="solution"
					:problem="problem"
				/>
			</template>
		</geocoder-solver-panel-layout>
	</geocoder-page-layout>
</template>
