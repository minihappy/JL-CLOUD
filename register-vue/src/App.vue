<script>
    import HelloWorld from './components/HelloWorld.vue'

    export default {
        name: "app",
        // watch: {
        //     $route(to, from) {//监听路由的变换，将路由推送至store中
        //         if (to.path != '/login' && to.path != '/') {
        //             let obj = {
        //                 name: to.name,
        //                 title: to.meta.title
        //             }
        //             // this.$store.commit("addTab", obj)
        //         }
        //
        //     }
        // }
    }
</script>

<template>
    <div id="app" style="height: 100vh">
        <router-view/>
    </div>
</template>

<style scoped>
    /*#app {*/
    /*    font-family: Avenir, Helvetica, Arial, sans-serif;*/
    /*    -webkit-font-smoothing: antialiased;*/
    /*    -moz-osx-font-smoothing: grayscale;*/
    /*    text-align: center;*/
    /*    color: #2c3e50;*/
    /*    margin-top: 60px;*/
    /*}*/
</style>
