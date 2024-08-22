<script setup lang="ts">
import 'leaflet/dist/leaflet.css'
import * as L from 'leaflet'
import { LIcon, LMap, LMarker, LPolygon, LPopup, LTileLayer } from '@vue-leaflet/vue-leaflet'

import type { Coordinate, GeocoderProblem, GeocoderSolution } from '../../api'
import { computed, ref, watchEffect } from 'vue'
import { watchArray } from '@vueuse/core'

const props = defineProps<{
	solution: GeocoderSolution | null | undefined
	problem: GeocoderProblem | null
}>()

const points = computed(() => props.problem?.points || [])

const result = computed<Coordinate | null>(() => {
	return props.solution?.suggestedCoordinate || null
})

// MAP OPTIONS
const routerMap = ref<typeof LMap | null>(null)

const center = ref<L.PointExpression>([0, 0])
const zoom = ref(3)
const minZoom = 1
const maxZoom = 20
const mapOptions = { attributionControl: false }

const layerUrl = 'https://{s}.tile.osm.org/{z}/{x}/{y}.png'
const layerOptions = { subdomains: ['a', 'b', 'c'] }

function pointKey(point: Coordinate): string {
	return `${point.lat.toFixed(4)}, ${point.lng.toFixed(4)}`
}

watchArray(points, () => {
	fitMap()
})

watchEffect(() => {
	fitMap()
})

function fitMap() {
	const bounds: L.LatLngBounds = L.featureGroup(points.value.map((e) => new L.Marker([e.lat, e.lng]))).getBounds()

	if (bounds.isValid()) {
		const tmp = [
			[bounds.getSouthWest().lat, bounds.getSouthWest().lng],
			[bounds.getNorthEast().lat, bounds.getNorthEast().lng],
		]
		routerMap.value?.leafletObject?.fitBounds(tmp)
	}
}
</script>

<template>
	<div style="height: 100%; min-height: 600px">
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
				v-for="(point, index) in points || []"
				:key="pointKey(point)"
				:name="pointKey(point)"
				:lat-lng="point"
				:attribution="`{ &quot;locationIds&quot;: ${pointKey(point)} }`"
			>
				<l-icon icon-url="/point.svg" :icon-size="[40, 40]" :icon-anchor="[20, 38]" />
				<l-popup :key="pointKey(point)">
					<div>
						<span> {{ index }}: {{ pointKey(point) }} </span>
					</div>
				</l-popup>
			</l-marker>
			<l-polygon :lat-lngs="points"></l-polygon>
			<l-marker
				v-if="result"
				:key="pointKey(result)"
				:name="pointKey(result)"
				:lat-lng="result"
				:z-index-offset="1000"
				:attribution="`{ &quot;locationId&quot;: ${pointKey(result)} }`"
			>
				<l-icon icon-url="/result.svg" :icon-size="[40, 40]" :icon-anchor="[20, 40]" />
			</l-marker>
		</l-map>
	</div>
</template>
