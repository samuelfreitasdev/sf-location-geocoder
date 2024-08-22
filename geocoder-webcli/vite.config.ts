import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
	server: {
		port: 3000,
		proxy: {
			'/api/': {
				target: 'http://localhost:8080/',
				changeOrigin: true,
				secure: false,
			},
			'/ws/': {
				target: 'ws://localhost:8080/',
				changeOrigin: true,
				secure: false,
				ws: true,
			},
		},
	},
	plugins: [vue(), vueJsx()],
	optimizeDeps: {
		exclude: ['oh-vue-icons/icons'],
	},
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url)),
		},
	},
})
