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
    // history: createWebHistory(process.env.BASE_URL),
    history: createWebHistory(import.meta.env.BASE_URL),//vite改动
    routes
})
router.beforeEach((to, from, next) => {//在路由操作前遍历调用
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
            newRoutes.forEach(newRoute => {
                router.addRoute(newRoute)
            }) //vue3写法
            hasRoute = true//获取得到权限后，设置已经存储了路由标识，后面操作就不会重新获取页面的路由了
            store.commit("changeRouteStatus", hasRoute)
        })
    }


    next()
})
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
    route.component = () => import(/* @vite-ignore */'../views/' + menu.component + '.vue')//动态添加依赖

    return route
}
export default router





