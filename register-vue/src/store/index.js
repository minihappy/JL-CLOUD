import {createStore} from 'vuex'
// import menus from "./modules/menus";

export default createStore({
    state: {
        token: ''
    },
    getters: {},
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
            localStorage.setItem("token", token)
        },
    },
    actions: {},
    modules: {
        // menus
    }
})
