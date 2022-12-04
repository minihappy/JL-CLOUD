<template>
    <div id="app">
        <router-view/>
    </div>
</template>
<style>   html, body, #app {
    font-family: 'Helvetica Neue', 'Hiragino Sans GB', 'WenQuanYi Micro Hei', 'Microsoft Yahei', sans-serif;
    height: 100%;
    padding: 0;
    margin: 0;
    font-size: 15px;
}</style>
<script>
    import HelloWorld from './components/HelloWorld.vue'

    export default {
        name: "App",
        watch: {
            $route(to, from) {//监听路由的变换，将路由推送至store中
                if (to.path != '/login' && to.path != '/') {
                    let obj = {
                        name: to.name,
                        title: to.meta.title
                    }
                    this.$store.commit("addTab", obj)
                }

            }
        },
        // provide() {//将当前的reload作为组件提供去内部页面，inject:['reload']
        //     reload:this.reload()
        // },
        // data() {
        //     return {
        //         isRouterAlive: true
        //     }
        // },
        // methods: {
        //     reload() {
        //         this.isRouterAlive = false
        //         this.$nextTick(function () {
        //             this.isRouterAlive = true
        //         })
        //     }
        // }
    }
</script>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        /*margin-top: 60px;*/
    }
</style>
