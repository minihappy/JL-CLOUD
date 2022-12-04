<template>
    <!--    <el-menu-->
    <!--            :default-active="this.$store.state.menus.editableTabsValue"-->
    <!--            class="el-menu-vertical-demo"-->
    <!--            background-color="#545c64"-->
    <!--            text-color="#fff"-->
    <!--            active-text-color="#ffd04b">-->

    <!--        <router-link to="/index">-->
    <!--            <el-menu-item index="Index" @click="selectMenu({name: 'index', title: '首页'})">-->
    <!--                <template slot="title">-->
    <!--                    <i class="el-icon-s-home"></i>-->
    <!--                    <span slot="title">首页</span>-->
    <!--                </template>-->
    <!--            </el-menu-item>-->
    <!--        </router-link>-->
    <!--        &lt;!&ndash;        <h1>{{menuList}}</h1>&ndash;&gt;-->
    <!--        <el-submenu :index="menu.name" v-for="menu in menuList">-->
    <!--            <template slot="title">-->
    <!--                <i :class="menu.icon"></i>-->
    <!--                <span>{{menu.title}}</span>-->
    <!--            </template>-->

    <!--            <router-link :to="item.path" v-for="item in menu.children">-->
    <!--                <el-menu-item :index="item.name" @click="selectMenu(item)">-->
    <!--                    <template slot="title">-->
    <!--                        <i :class="item.icon"></i>-->
    <!--                        <span slot="title">{{item.title}}</span>-->
    <!--                    </template>-->
    <!--                </el-menu-item>-->
    <!--            </router-link>-->

    <!--        </el-submenu>-->

    <!--    </el-menu>-->
    <el-menu
            active-text-color="#ffd04b"
            background-color="#545c64"
            class="el-menu-vertical-demo"
            default-active="2"
            text-color="#fff"
            :default-active="this.$store.state.menus.editableTabsValue"
    >
        <router-link to="/index">
            <el-menu-item index="Index" @click="selectMenu({name: 'index', title: '首页'})">
                <el-icon>
                    <HomeFilled/>
                </el-icon>
                <span slot="title">首页</span>
            </el-menu-item>
        </router-link>
        <el-sub-menu :index="menu.name" v-for="menu in menuList">
            <template #title>
                <el-icon>
                    <Menu/>
                </el-icon>
                <!--                <i :class="menu.icon"></i>-->
                <span>{{menu.title}}</span>
            </template>
            <router-link :to="item.path" v-for="item in menu.children">
                <template #title>{{item.title}}</template>
                <el-menu-item :index="item.name" @click="selectMenu(item)">
                    <el-icon>
                        <component :is="item.icon"></component>
                    </el-icon>
                    <!--                    <i :class="item.icon"></i>-->
                    <span slot="title">{{item.title}}</span>
                </el-menu-item>
            </router-link>
        </el-sub-menu>
    </el-menu>
</template>
<script>
    export default {
        name: "SideMenu",
        data() {
            return {}
        },
        computed: {
            menuList: {
                get() {
                    return this.$store.state.menus.menuList
                }
            }
        },
        methods: {
            selectMenu(item) {
                this.$store.commit("addTab", item)
            }
        }
    }
</script>
<style scoped>
    .el-menu-vertical-demo {
        height: 100%;
    }

    a {
        text-decoration: none;
    }</style>
