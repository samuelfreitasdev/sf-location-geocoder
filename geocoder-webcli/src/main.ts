import { createApp, defineAsyncComponent } from 'vue'
import { addIcons, OhVueIcon } from 'oh-vue-icons'
import {
	MdAdd,
	MdAddcircle,
	MdCancelOutlined,
	MdChangecircle,
	MdCheck,
	MdCheckcircleOutlined,
	MdClose,
	MdContentcopy,
	MdDeleteoutline,
	MdEditTwotone,
	MdInfoOutlined,
	MdPlayarrow,
	MdRemovecircle,
	MdSearch,
	MdWarningamberRound,
} from 'oh-vue-icons/icons/md'
import * as FaIcons from 'oh-vue-icons/icons/fa'
import { FaGithub, FaSlackHash } from 'oh-vue-icons/icons/fa'
import { OiGear } from 'oh-vue-icons/icons/oi'

import './app.css'
import App from './App.vue'
import router from './router'

const VueApexCharts = defineAsyncComponent(() => import('vue3-apexcharts'))

const Fa = Object.values({ ...FaIcons })
addIcons(...Fa)

addIcons(
	FaGithub,
	FaSlackHash,
	MdAdd,
	MdAddcircle,
	MdCancelOutlined,
	MdChangecircle,
	MdCheck,
	MdCheckcircleOutlined,
	MdClose,
	MdContentcopy,
	MdDeleteoutline,
	MdInfoOutlined,
	MdEditTwotone,
	MdSearch,
	MdWarningamberRound,
	OiGear,
	MdPlayarrow,
	MdRemovecircle,
)

createApp(App)
	.use(router)
	.component('apexchart', VueApexCharts)
	.component('v-icon', OhVueIcon)
	.mount('#app')
