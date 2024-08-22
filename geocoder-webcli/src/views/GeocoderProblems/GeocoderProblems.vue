<script setup lang="ts">
import { GeocoderPageLayout } from '../../layout'
import { type GeocoderSummary, Page } from '../../api'

import { useFetch } from '@vueuse/core'
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { AlertMessage, DeleteDialog, InputSearch, PaginatedTable } from '../../components'

const route = useRoute()

const url = computed(
	() => `/api/problems?page=${route.query.page || 0}&size=${route.query.size || 10}&q=${route.query.q || ''}`
)

const {
	isFetching,
	error,
	data: page,
	execute: fetchProblems,
} = useFetch(url, { refetch: true }).get().json<Page<GeocoderSummary>>()

const selectedProblem = ref<GeocoderSummary | null>(null)

const openRemove = ref<boolean>(false)
const removeUrl = computed(() => `/api/problems/${selectedProblem.value?.id}`)
const removeError = ref(false)

const showDeleteModal = (instance: GeocoderSummary) => {
	selectedProblem.value = instance
	openRemove.value = true
}
</script>

<template>
	<geocoder-page-layout v-slot="{ tableFooterHeight }" :is-fetching="isFetching" :error="error">
		<main>
			<div class="mx-2 my-2 space-x-2">
				<alert-message
					v-if="removeError"
					:message="`Could not remove GeocoderProblem: ${selectedProblem?.name}`"
					variant="error"
				/>

				<delete-dialog
					v-model:url="removeUrl"
					v-model:open="openRemove"
					:message="`Are you sure you want to delete ${selectedProblem?.name} (id: ${selectedProblem?.id})?`"
					@success-remove="fetchProblems"
					@fail-remove="removeError = true"
				/>

				<h1 class="text-2xl">Problems</h1>
				<div class="flex w-full justify-between pr-2">
					<input-search :query="`${route.query.q || ''}`" />
					<router-link to="/problem/new" class="btn btn-circle">
						<v-icon name="md-add" />
					</router-link>
				</div>

				<paginated-table :page="page" :style="`height: calc(100vh - ${tableFooterHeight})`">
					<template #head>
						<th>Id</th>
						<th>Name</th>
						<th>Num Locations</th>
						<th>Num Solver Requests</th>
						<th>Solver Request Status</th>
						<th>Actions</th>
					</template>
					<template #show="{ row }">
						<td>{{ row.id }}</td>
						<td>{{ row.name }}</td>
						<td>{{ row.nlocations }}</td>
						<td>{{ row.numSolverRequests }}</td>
						<td class="space-x-2">
							<div
								v-if="row.numEnqueuedRequests > 0"
								class="badge badge-warning tooltip"
								data-tip="Enqueued"
							>
								E
							</div>
							<div
								v-else-if="row.numRunningRequests > 0"
								class="badge badge-success tooltip"
								data-tip="Running"
							>
								R
							</div>
							<div v-else class="badge badge-info tooltip" data-tip="Not Solving">N</div>
						</td>
						<td class="space-x-2">
							<div class="tooltip" data-tip="Solve it">
								<router-link :to="`/solve/${row.id}`" class="btn btn-sm btn-circle">
									<v-icon name="md-playarrow" />
								</router-link>
							</div>
							<div v-if="row.numSolverRequests === 0" class="tooltip" data-tip="Edit">
								<router-link :to="`/problem/${row.id}/edit`" class="btn btn-sm btn-circle">
									<v-icon name="md-edit-twotone" />
								</router-link>
							</div>
							<div v-else class="tooltip" data-tip="Copy">
								<router-link :to="`/problem/${row.id}/copy`" class="btn btn-sm btn-circle">
									<v-icon name="md-contentcopy" />
								</router-link>
							</div>
							<div class="tooltip" data-tip="Delete">
								<button
									:disabled="row.numSolverRequests > 0"
									class="btn btn-sm btn-circle"
									@click="showDeleteModal(row)"
								>
									<v-icon name="md-deleteoutline" />
								</button>
							</div>
						</td>
					</template>
				</paginated-table>
			</div>
		</main>
	</geocoder-page-layout>
</template>

<style scoped></style>
