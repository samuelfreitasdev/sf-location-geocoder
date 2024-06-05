<script setup lang="ts">
import { AlertMessage } from '../../../components'
import { computed, type StyleValue, toRefs } from 'vue'
import { sortBy } from 'lodash'
import type { Coordinate, EditableProblem } from '../../../api'
import { useFetch, useVModels } from '@vueuse/core'
import GeocoderPointsMap from './GeocoderPointsMap.vue'

const props = defineProps<{
	persistUrl: string;
	problem: EditableProblem;
	style?: StyleValue;
}>()

const emit = defineEmits<{
	(e: 'update:problem', val: EditableProblem): void;
}>()

const { persistUrl, style } = toRefs(props)

const { problem } = useVModels(props, emit)

const fetcher = useFetch(persistUrl, { immediate: false })
const creationMethod = computed(() => !persistUrl.value?.endsWith('update'))
const {
	isFetching: isUpdating,
	execute: persist,
	error: persistError,
	statusCode: updateCode,
} = creationMethod.value ? fetcher.post(problem) : fetcher.put(problem)
const successPersist = computed(() => (updateCode.value || 0) >= 200 && (updateCode.value || 0) < 300)

function errorClose() {
	persistError.value = null
}

function successClose() {
	updateCode.value = null
}

function addPoint(point: Coordinate) {
	const newPoints = problem.value.points.concat(point)
	problem.value = {
		...problem.value,
		...{ points: newPoints },
	}
}

function addEmptyPoint() {
	addPoint({ lat: 0, lng: 0 })
}

function removePoint(point: Coordinate) {
	const newPoints = problem.value.points.filter((p) => p !== point)
	problem.value = {
		...problem.value,
		...{ points: newPoints },
	}
}

function movePoint(prev: Coordinate, updated: Coordinate) {
	const idx = problem.value.points.findIndex((p) => p === prev)
	const copy = [...problem.value.points]
	copy.splice(idx, 1, updated)
	problem.value = {
		...problem.value,
		...{ points: sortBy(copy, it => it.lat) },
	}
}

function pointKey(point: Coordinate) {
	return `${point.lat.toFixed(4)}-${point.lng.toFixed(4)}}`
}
</script>

<template>
	<div class="flex my-2 mx-2 space-x-2 h-full">
		<div class="flex-initial flex-col w-7/12 p-3">
			<alert-message
				v-if="persistError"
				message="Could not save/update VrpProblem"
				variant="error"
				@close="errorClose"
			/>
			<alert-message
				v-if="successPersist"
				message="Succcessfully save/update Location"
				variant="success"
				@close="successClose"
			/>

			<div class="">
				<label for="name" class="block font-bold mb-2"> Name </label>
				<div>
					<input v-model="problem.name"
						   name="name"
						   class="input input-bordered w-full input-xs"
					/>
				</div>
			</div>

			<div class="mt-3 mb-3">
				<button class="btn" @click="addEmptyPoint">
					<v-icon name="md-addcircle" class="ui-menu-icon" />
					Add Point
				</button>
			</div>

			<div class="overflow-auto" style="max-height: 600px">
				<table class="table table-sm table-zebra w-full " >
					<thead>
					<tr>
						<th>Latitude</th>
						<th>Longitude</th>
						<th></th>
					</tr>
					</thead>
					<tbody>
					<tr v-for="point in sortBy(problem.points, it => it.lat)" :key="pointKey(point)">
						<td>
							<input v-model.number="point.lat"
								   class="input input-bordered input-xs" />
						</td>
						<td>
							<input v-model.number="point.lng"
								   class="input input-bordered input-xs" />
						</td>
						<td>
							<div class="tooltip" data-tip="Remove">
								<button class="primary btn btn-sm btn-circle bg-transparent/15"
										@click="removePoint(point)">
									<v-icon name="md-deleteoutline" />
								</button>
							</div>
						</td>
					</tr>
					</tbody>
				</table>
			</div>


			<div class="flex flex-row-reverse pt-4">
				<form class="space-x-2">
					<router-link to="/" class="btn">Cancel</router-link>
					<button class="btn btn-success" :disabled="isUpdating" @click="() => persist()">
						Save<span v-if="isUpdating" class="loading loading-bars loading-xs"></span>
					</button>
				</form>
			</div>
		</div>

		<div class="flex-auto">
			<geocoder-points-map
				:points="problem.points"
				@add-point="addPoint"
				@remove-point="removePoint"
				@move-point="movePoint"
			/>
		</div>
	</div>
</template>
