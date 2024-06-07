<script setup lang="ts">

import type { Coordinate, GeocoderSolution } from '../../api'
import { computed, ref, toRefs } from 'vue'
import { LMap, LMarker, LTileLayer } from '@vue-leaflet/vue-leaflet'
import * as L from 'leaflet'

const props = defineProps<{
	solution: GeocoderSolution | null;
}>()

const { solution } = toRefs(props)

const problem = computed(() => solution.value?.problem)

const points = computed<(Coordinate)[]>(() => {
	return (problem.value?.points || [])
})

const result = computed<(Coordinate)>(() => {
	return solution.value?.suggestedCoordinate || { lat: 0, lng: 0 }
})

// MAP OPTIONS

const center = ref<L.PointExpression>([0, 0])
const zoom = ref(3)
const minZoom = 2
const maxZoom = 18
const mapOptions = { attributionControl: false }

const layerUrl = 'https://{s}.tile.osm.org/{z}/{x}/{y}.png'
const layerOptions = { subdomains: ['a', 'b', 'c'] }

function pointKey(point: Coordinate) {
	return `${point.lat.toFixed(4)}-${point.lng.toFixed(4)}`
}

function getPointIcon() {
	return L.icon({
		iconUrl: '/point.svg',
		iconSize: [48, 48],
		className: '',
	})
}

function getResultIcon() {
	return L.icon({
		iconUrl: '/result.svg',
		iconSize: [48, 48],
		className: '',
	})
}
</script>

<template>
	<div style="height:100%; min-height: 600px">
		<l-map
			ref="routerMap"
			v-model:zoom="zoom"
			:min-zoom="minZoom"
			:max-zoom="maxZoom"
			:center="center"
			:use-global-leaflet="false"
			:options="mapOptions"
		>
			<l-tile-layer :url="layerUrl" :options="layerOptions" />
			<l-marker
				v-for="point in points || []"
				:key="pointKey(point)"
				:name="pointKey(point)"
				:lat-lng="point"
				:attribution="`{ &quot;locationId&quot;: ${pointKey(point)} }`"
				:icon="getPointIcon()"
			>
				<!--				<l-popup-->
				<!--					v-if="!isHighlighted(point)"-->
				<!--					:key="pointKey(point)"-->
				<!--				>-->
				<!--					<div>-->
				<!--						<span>{{ pointKey(point) }}</span>-->
				<!--					</div>-->
				<!--				</l-popup>-->
			</l-marker>

			<l-marker
				:key="pointKey(result)"
				:name="pointKey(result)"
				:lat-lng="result"
				:attribution="`{ &quot;locationId&quot;: ${pointKey(result)} }`"
				:icon="getResultIcon"
			></l-marker>
		</l-map>
	</div>
</template>
