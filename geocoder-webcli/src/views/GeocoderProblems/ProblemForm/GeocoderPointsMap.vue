<script setup lang="ts">
import 'leaflet/dist/leaflet.css'
import * as L from 'leaflet'
import { LMap, LMarker, LPolygon, LPopup, LTileLayer } from '@vue-leaflet/vue-leaflet'
import { computed, ref, toRef, watchEffect } from 'vue'
import type { Coordinate } from '../../../api'

const props = defineProps<{
	points: Coordinate[]
}>()

const emit = defineEmits<{
	(e: 'addPoint', val: Coordinate): void
	(e: 'removePoint', val: Coordinate): void

	(e: 'update:selectedPoint', val: Coordinate | null | undefined): void
	(e: 'markerClick', val: L.LeafletMouseEvent): void
}>()

const points = computed(() => props.points)

const selectedPoint = toRef<Coordinate | null | undefined>(points.value?.length > 0 ? points.value[0] : null)
const componentPoint = computed<Coordinate | null | undefined>({
	get: () => selectedPoint.value,
	set: (val) => emit('update:selectedPoint', val),
})

const routerMap = ref<typeof LMap | null>(null)

const center = ref<L.PointExpression>([0, 0])
const zoom = ref(3)
const minZoom = 2
const maxZoom = 18
const mapOptions = { attributionControl: false }

const layerUrl = 'https://{s}.tile.osm.org/{z}/{x}/{y}.png'
const layerOptions = { subdomains: ['a', 'b', 'c'] }

function addPoint($event: L.LeafletMouseEvent) {
	const point: Coordinate = { lat: $event.latlng.lat, lng: $event.latlng.lng }
	emit('addPoint', point)
}

function removePoint(point: Coordinate) {
	emit('removePoint', point)
}

function markerClickHandler(e: L.LeafletMouseEvent) {
	if (!componentPoint.value) {
		const attribution = JSON.parse(e.target.options.attribution)
		componentPoint.value = points.value?.find(
			(p) => p.lat.toFixed(4) === attribution.lat.toFixed(4) && p.lng.toFixed(4) === attribution.lng.toFixed(4)
		)
		emit('markerClick', e)
	}
}

function isHighlighted(point: Coordinate) {
	return point === componentPoint.value
}

function pointKey(point: Coordinate): string {
	return `(${point.lat.toFixed(4)}, ${point.lng.toFixed(4)})`
}

watchEffect(() => {
	const _points = points.value.concat(componentPoint?.value ? [componentPoint?.value] : [])
	const bounds: L.LatLngBounds = L.featureGroup(_points.map((e) => new L.Marker([e.lat, e.lng]))).getBounds()

	if (bounds.isValid()) {
		const tmp = [
			[bounds.getSouthWest().lat, bounds.getSouthWest().lng],
			[bounds.getNorthEast().lat, bounds.getNorthEast().lng],
		]
		routerMap.value?.leafletObject?.fitBounds(tmp)
		if (componentPoint?.value) {
			center.value = [componentPoint.value.lat, componentPoint.value.lng]
		}
	}
})
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
			@click="addPoint"
		>
			<l-tile-layer :url="layerUrl" :options="layerOptions" />
			<l-marker
				v-for="point in points || []"
				:key="pointKey(point)"
				:name="pointKey(point)"
				:lat-lng="point"
				:draggable="false"
				:attribution="`{ &quot;locationId&quot;: ${pointKey(point)} }`"
				@click="markerClickHandler"
			>
				<l-popup v-if="!isHighlighted(point)" :key="pointKey(point)">
					<div>
						<span>{{ pointKey(point) }}</span>
						<button class="align-middle ml-1 btn btn-xs btn-circle" @click="removePoint(point)">
							<v-icon name="md-deleteoutline" />
						</button>
					</div>
				</l-popup>
			</l-marker>
			<l-polygon :lat-lngs="points"></l-polygon>
		</l-map>
	</div>
</template>
