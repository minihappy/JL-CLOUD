// import store from './store'
// export function hasAuth(perm) {
//     // 被组合式函数封装和管理的状态
//     const authority= store.state.menus.permList
//     // 通过返回值暴露所管理的状态
//     return authority.indexOf(perm) > -1
// }
// Vue.mixin({
// 	methods: {
// 		hasAuth(perm) {
// 			var authority = this.$store.state.menus.permList
//
// 			return authority.indexOf(perm) > -1
// 		}
// 	}
// })
// 为了vue3兼容到main中
//不推荐
//
// Mixins 在 Vue 3 支持主要是为了向后兼容，因为生态中有许多库使用到。在新的应用中应尽量避免使用 mixin，特别是全局 mixin。
//
// 若要进行逻辑复用，推荐用组合式函数来替代。
//https://cn.vuejs.org/api/application.html#app-use
