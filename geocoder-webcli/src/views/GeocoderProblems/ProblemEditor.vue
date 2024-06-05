<script setup lang="ts">

import { GeocoderPageLayout } from '../../layout'
import GeocoderProblemForm from './ProblemForm/GeocoderProblemForm.vue'
import { EditableProblem } from '../../api'
import { computed, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import { type AfterFetchContext, useFetch } from '@vueuse/core'

const props = defineProps<{
	mode: 'copy' | 'create' | 'update';
}>()

const { mode } = toRefs(props)

const route = useRoute()

const defaultProblem: EditableProblem = {
	id: -1,
	name: '',
	points: [],
}

const problemUrl = computed(() => `/api/problems/${route.params.id}`)
const persistUrl = computed(() =>
	mode.value !== 'create' ? `${problemUrl.value}/${mode.value}` : '/api/problems',
)

const { isFetching, error, data } = useFetch(problemUrl, {
	initialData: defaultProblem,
	immediate: mode.value !== 'create',
	afterFetch: afterProblemFetch,
})
	.get()
	.json<EditableProblem>()

function afterProblemFetch(ctx: AfterFetchContext<EditableProblem>) {
	ctx.data = {
		id: ctx.data?.id || defaultProblem.id,
		name: ctx.data?.name || defaultProblem.name,
		points: ctx.data?.points || defaultProblem.points,
	}
	return ctx
}
</script>

<template>
	<geocoder-page-layout v-slot="{ tabFooterHeight }" :is-fetching="isFetching" :error="error">
		<main>
			<geocoder-problem-form
				v-if="data"
				v-model:problem="data"
				:persist-url="persistUrl"
				:style="`height: calc(100vh - ${tabFooterHeight})`"
			/>
			<geocoder-problem-form
				v-else
				v-model:problem="defaultProblem"
				:persist-url="persistUrl"
				:style="`height: calc(100vh - ${tabFooterHeight})`"
			/>
		</main>
	</geocoder-page-layout>
</template>
