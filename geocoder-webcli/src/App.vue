<script lang="ts" setup>
import { useColorMode, watchOnce } from '@vueuse/core'
import { localThemes, modes } from './themes'

const mode = useColorMode({
	attribute: 'data-theme',
	modes: modes,
})

watchOnce(mode, () => {
	mode.value = 'dark'
})
</script>

<template>
	<header class="navbar bg-base-300">
		<div class="flex-1">
			<a class="btn btn-ghost normal-case text-xl">Geocoder</a>
			<router-link to="/" class="btn btn-ghost normal-case text-l">Home</router-link>
		</div>
		<div class="justify-end space-x-4">
			<a href="https://github.com/samuelfreitasdev/smart-geocoder">
				<v-icon name="fa-github" scale="2" />
			</a>
			<select v-model="mode" class="select select-sm w-full max-w-xs" data-choose-theme>
				<option v-for="theme in localThemes" :key="theme" :value="theme">{{ theme }}</option>
			</select>
		</div>
	</header>

	<router-view></router-view>

	<footer class="footer items-center p-4 bg-neutral text-neutral-content fixed bottom-0">
		<div class="items-center grid-flow-col">
			<v-icon name="fa-slack-hash" scale="2" />
			<p>Copyright © 2023 - All right reserved</p>
		</div>
	</footer>
</template>
