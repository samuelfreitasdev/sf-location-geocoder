<script setup lang="ts">

import { computed, ref, type StyleValue, toRefs } from 'vue'
import { type GeocoderProblem, type GeocoderSolution } from '../../api'
import { until } from '@vueuse/core'

const props = defineProps<{
	solution: GeocoderSolution | null | undefined;
	problem: GeocoderProblem | null;
	solverStatus: string | null;
	selectedSolver: string;
	solvers: string[];
	style?: StyleValue;
}>()

const emit = defineEmits<{
	(e: 'onSolve'): void;
	(e: 'onTerminate'): void;
	(e: 'onClear'): void;
	(e: 'update:selectedSolver', val: string): void;
}>()

const {
	problem,
	solverStatus,
	selectedSolver,
	solvers,
	style,
} = toRefs(props)

const editorSelectedSolver = computed({
	get: () => selectedSolver.value,
	set: (val) => emit('update:selectedSolver', val),
})

const isRunning = computed(() => ['ENQUEUED', 'RUNNING'].includes(solverStatus.value || ''))
// const isWsConnected = computed(() => wsStatus.value === "OPEN");

const waitingTermination = ref(false)
const waitingClear = ref(false)

async function wrapperTermination() {
	waitingTermination.value = true
	emit('onTerminate')
	await until(solverStatus).toMatch((v) => v === 'TERMINATED')
	waitingTermination.value = false
}

async function wrapperClear() {
	waitingClear.value = true
	emit('onClear')
	await until(solverStatus).toMatch((v) => v === 'NOT_SOLVED')
	waitingClear.value = false
}
</script>

<template>
	<div class="form-control flex flex-col overflow-y-auto space-y-2" :style="style">
		<div class="flex space-x-2">
			<div class="basis-1/2">
				<h1 class="text-lg">Solver for {{ problem?.name }}</h1>
			</div>
			<div class="basis-1/2 flex flex-row-reverse">
				<!--				<router-link :to="`/solver-history/${solution?.problem.id}`" class="link link-primary">-->
				<!--					Solver History »-->
				<!--				</router-link>-->
			</div>
		</div>
		<div class="flex flex-row-reverse space-x-2">
			<div class="flex justify-end space-x-2">
				<div>
					<span v-if="solverStatus" class="badge badge-outline">{{ solverStatus }}</span>
				</div>
				<!--				<div class="tooltip tooltip-left" :data-tip="`Web Socket ${isWsConnected ? 'connected' : 'disconnected'}`">-->
				<!--					<span :class="`badge ${isWsConnected ? 'badge-success' : 'badge-error'}`">WS</span>-->
				<!--				</div>-->
			</div>
		</div>
		<div class="flex space-x-2">
			<label class="relative inline-flex items-center mb-4 cursor-pointer">
				<span class="mr-3 text-sm font-medium">Solver</span>
				<select
					v-model="editorSelectedSolver"
					class="select select-bordered select-xs w-full max-w-xs"
				>
					<option
						v-for="(solver, index) in solvers"
						:key="solver"
						:value="solver"
						:selected="index == 0"
					>
						{{ solver }}
					</option>
				</select>
			</label>
		</div>
		<div class="flex justify-between">
			<div class="card-actions">
				<button
					class="btn btn-sm btn-success"
					:disabled="isRunning"
					@click="$emit('onSolve')"
				>
					Solve
					<span v-if="isRunning" class="loading loading-bars loading-xs"></span>
				</button>
				<button
					:disabled="!isRunning || waitingTermination"
					class="btn btn-sm btn-warning"
					@click="wrapperTermination">
					Terminate
					<span v-if="waitingTermination" class="loading loading-bars loading-xs"></span>
				</button>
				<button
					:disabled="isRunning || waitingClear"
					class="btn btn-sm btn-error"
					@click="wrapperClear">
					Clear
					<span v-if="waitingClear" class="loading loading-bars loading-xs"></span>
				</button>
			</div>
		</div>
		<div class="">
			<div>
				<p>Points:</p>
				<table class="table table-zebra w-full">
					<thead>
					<tr>
						<th>Index</th>
						<th>Latitude</th>
						<th>Longitude</th>
					</tr>
					</thead>
					<tbody>
					<tr v-for="(point, index) in problem?.points || []">
						<td>{{ index }}</td>
						<td>{{ point.lat.toFixed(4) }}</td>
						<td>{{ point.lng.toFixed(4) }}</td>
					</tr>
					</tbody>
				</table>

				<div v-if="solution" class="mt-3.5">
					<p>Result:</p>

					<table class="table table-zebra w-full">
						<thead>
						<tr>
							<th>Latitude</th>
							<th>Longitude</th>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td>{{ solution.suggestedCoordinate.lat.toFixed(4) }}</td>
							<td>{{ solution.suggestedCoordinate.lng.toFixed(4) }}</td>
						</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

</template>
