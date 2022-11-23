import Vue, {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import installElementPlus from './plugins/element'
import axios from './axios'

const app = createApp(App)
app.mixin({
    methods: {
        hasAuth(perm) {
            var authority = this.$store.state.menus.permList
            return authority.indexOf(perm) > -1
        }
    }
})
app.config.globalProperties.$axios = axios
installElementPlus(app)
app.use(store).use(router).mount('#app')
