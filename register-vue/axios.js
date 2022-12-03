import axios from "axios";
import router from "./src/router";
import {ElMessage} from "element-plus"
import qs from 'qs'

const request = axios.create({
    baseURL: "http://localhost:8000",
    timeout: 5000,
    headers: {
        'Content-Type': "application/x-www-form-urlencoded; charset=utf-8"
    }
})
// components:{
// 	ElMessage
// }
// request.interceptors.request.use(config => {
//     config.headers['Authorization'] = localStorage.getItem("token")
//     return config
// })
request.interceptors.request.use(config => {
    // let token = localStorage.getItem("token")
    // token = typeof (token) == "undefined" || typeof (token) == undefined || token == "undefined" ? null : token
    // if (token != null) {//token为空不能添加到headers中否则jwt会捕获空的token
    //     config.headers['token'] = token
    //     config.headers['Authorization'] = "Bearer " + token
    // }
    // config.data = qs.stringify(config.data)
    // config.headers['Cache-Control'] = 'no-cache'
    return config
})
request.interceptors.response.use(
    response => {

        let res = response.data

        if (res.code === 200) {
            return response
        } else if (res.code === 401) {
        }
        ElMessage.error(!res.message ? '系统异常' : res.message)
        // ElMessage.Message.error(!res.message ? '系统异常' : res.message)
        return Promise.reject(response.data.message)

    },
    error => {
        if (error.response.data) {
            error.message = error.response.data.message
        }

        if (error.response.status === 401) {
            router.push("/login")
        }

        ElMessage.error(error.message, {duration: 3000})
        return Promise.reject(error)
    }
)

export default request
