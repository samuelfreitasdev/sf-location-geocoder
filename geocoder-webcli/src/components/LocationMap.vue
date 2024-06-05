<script lang="ts" setup>
import 'leaflet/dist/leaflet.css'
import * as L from 'leaflet'
import { LatLng } from 'leaflet'
import { LMap, LMarker, LTileLayer } from '@vue-leaflet/vue-leaflet'
import { ref } from 'vue'


const poi = [-4.9660368, -39.0048207]

const points = [
	poi,
	[-4.9660368, -39.0048207],
	[-4.96603, -39.0047856],
	[-4.96600, -39.0047856],
	[-4.96603, -39.0047846],
	[-4.96603, -39.0043841],
	[-4.96613, -39.0043841],
	[-4.96619, -39.0043841],
	[-4.96603, -39.0047859],
	[-4.9661943, -39.004726],
	[-4.9661940, -39.004720],
]
	.map(([lat, lng]) => new LatLng(lat, lng))

const routerMap = ref<typeof LMap | null>(null)
const center = ref<L.PointExpression>([-4.9660368, -39.0048207])
const zoom = ref(20)

</script>

<template>
	<div style="height:600px; width:800px">
		<l-map ref="routerMap"
			   v-model:zoom="zoom"
			   :use-global-leaflet="false"
			   :center="center"
		>
			<l-marker
				v-for="point in points || []"
				:lat-lng="point"
			></l-marker>

			<!--			<l-polygon-->
			<!--				:lat-lngs="points"-->
			<!--				:color="'red'"-->
			<!--				:fill-opacity="0.5"-->
			<!--			></l-polygon>-->

			<l-tile-layer
				url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
				layer-type="base"
				name="OpenStreetMap"
			></l-tile-layer>
		</l-map>
	</div>
</template>
