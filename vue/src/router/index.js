import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/HomeView.vue'
// import Vue from 'vue'
// import VueRouter from 'vue-router'
// import HomeView from '../views/HomeView.vue'
import axios from "../axios"
import store from "../store"
// Vue.use(VueRouter)
const routes = [
    {
        path: '/home',
        name: 'home',
        component: HomeView,
        meta: {
            title: "home"
        }
        ,
        children: [
            {
                path: '/index',
                name: 'index',
                meta: {
                    title: "首页"
                },
                // route level code-splitting
                // this generates a separate chunk (about.[hash].js) for this route
                // which is lazy-loaded when the route is visited.
                component: () => import(/* webpackChunkName: "about" */ '../views/index.vue')
            },
            {
                path: '/userCenter',
                name: 'UserCenter',
                meta: {
                    title: "个人中心"
                },
                component: () => import('../views/UserCenter.vue')
            },
        ],
    },
    {
        path: '/about',
        name: 'about',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    },

    {
        path: '/',
        name: 'login',
        title: "登录",
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/login.vue')
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})
let registerRouteFresh = true
router.beforeEach((to, from, next) => {//在路由操作前遍历调用
    const jwt = to.query.accessToken
    if (typeof (jwt) == "string" && to.path == "/") {
        store.commit('SET_TOKEN', jwt)
        next({path: '/index'})
    }
    let hasRoute = store.state.menus.hasRoutes
    let token = localStorage.getItem("token")//获取token
    if (to.path == '/') {//判断路由的访问页面
        next()

    } else if (!token) {
        next({path: '/'})

    } else if (token && !hasRoute) {
        axios.get("/sys-comm/sys/menu/nav").then(res => {
            // 拿到menuList
            store.commit("setMenuList", res.data.data.nav)//获取相对应的父节点和子节点，以及相关的权限

            // 拿到用户权限

            store.commit("setPermList", res.data.data.authorities)//获取用户权限

            // 动态绑定路由
            let newRoutes = router.options.routes

            res.data.data.nav.forEach(menu => {
                if (menu.children) {//如果当前的父导航有孩子的话，则遍历
                    menu.children.forEach(e => {
                        // 转成路由
                        let route = menuToRoute(e)

                        // 吧路由添加到路由管理中
                        if (route) {
                            newRoutes[0].children.push(route)
                        }

                    })
                }
            })
            // router.addRoutes(newRoutes)//vue2写法
            // newRoutes.forEach(newRoute => {
            //     router.addRoute(newRoute)
            // }) //vue3写法
            if (registerRouteFresh) {
                newRoutes.forEach((val) => {
                    router.addRoute(val)
                })
                next({...to, replace: true})
                registerRouteFresh = false
            } else {
                next()
            }
            hasRoute = true//获取得到权限后，设置已经存储了路由标识，后面操作就不会重新获取页面的路由了
            store.commit("changeRouteStatus", hasRoute)
        })
    }
    if (hasRoute)//当获取过权限的，遍历过一次再执行
        next()
})
// 如果首次或者刷新界面，next(...to, replace: true)会循环遍历路由，如果to找不到对应的路由那么他会再执行一次beforeEach((to, from, next))直到找到对应的路由，我们的问题在于页面刷新以后异步获取数据，直接执行next()感觉路由添加了但是在next()之后执行的，所以我们没法导航到相应的界面。这里使用变量registerRouteFresh变量做记录，直到找到相应的路由以后，把值设置为false然后走else执行next(),整个流程就走完了，路由也就添加完了。
// 导航转成路由
const menuToRoute = (menu) => {

    if (!menu.component) {//如果不是组件,（组件为空），返回空
        return null
    }

    let route = {//定义一个route
        name: menu.name,
        path: menu.path,
        meta: {
            icon: menu.icon,
            title: menu.title
        }
    }
    route.component = () => import('@/views/' + menu.component + '.vue')//动态添加依赖

    return route
}
export default router





