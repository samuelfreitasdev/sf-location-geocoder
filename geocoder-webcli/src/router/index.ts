import { createRouter, createWebHistory } from 'vue-router'
import GeocoderProblems from '../views/GeocoderProblems/GeocoderProblems.vue'
import ProblemEditor from '../views/GeocoderProblems/ProblemEditor.vue'
import GeocoderSolver from '../views/GeocoderSolver/GeocoderSolver.vue'


const router = createRouter({
	history: createWebHistory(),
	routes: [
		{ path: '/', component: GeocoderProblems },
		{ path: "/solve/:id", component: GeocoderSolver },
		{ path: '/problem/new', component: ProblemEditor, props: { mode: 'create' } },
		{ path: '/problem/:id/edit', component: ProblemEditor, props: { mode: 'update' } },
		{ path: '/problem/:id/copy', component: ProblemEditor, props: { mode: 'copy' } },
	],
})

export default router
