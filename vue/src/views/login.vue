<template>
    <el-row type="flex" class="row-bg" justify="center">
        <el-col :xl="6" :lg="7">
            <h2>欢迎来到VueAdmin管理系统</h2>
<!--            <el-image :src="require('@/assets/MarkerHub.jpg')" style="height: 180px; width: 180px;"></el-image>-->

<!--            <p>公众号 MarkerHub</p>-->
<!--            <p>扫码二维码，回复【 VueAdmin 】获取登录密码</p>-->

        </el-col>

        <el-col :span="1">
            <el-divider direction="vertical"></el-divider>
        </el-col>
        <el-col :xl="6" :lg="7">
            <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="80px">
                <el-form-item label="用户名" prop="username" style="width: 380px;">
                    <el-input v-model="loginForm.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" style="width: 380px;">
                    <el-input v-model="loginForm.password" type="password"></el-input>
                </el-form-item>
                <el-form-item label="验证码" prop="code" style="width: 380px;">
                    <el-input v-model="loginForm.code" style="width: 172px; float: left" maxlength="5"></el-input>
                    <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha"></el-image>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm('loginForm')">立即创建</el-button>
                    <el-button @click="resetForm('loginForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>

</template>

<script>
    import {getCurrentInstance} from 'vue'
    import qs from 'qs'

    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    // username: 'admin',
                    // password: '111111',
                    // code: '11111',
                    // token: ''
                    username: '',
                    password: '',
                    code: '',
                    token: ''
                },
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入验证码', trigger: 'blur'},
                        {min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur'}
                    ],
                },
                captchaImg: null
            };
        },
        methods: {
            submitForm(formName) {
                const {proxy} = getCurrentInstance()
                proxy.$refs[formName].validate((valid) => {
                    if (valid) {
                        proxy.$axios.post('/user/login?' + qs.stringify(proxy.loginForm)).then(res => {

                            console.log(res)

                            const jwt = res.headers['authorization']

                            proxy.$store.commit('SET_TOKEN', jwt)
                            proxy.$router.push("/index")
                        })

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                const {proxy} = getCurrentInstance()
                proxy.$refs[formName].resetFields();
            },
            getCaptcha() {
                const {proxy} = getCurrentInstance()
                proxy.$axios.get('/captcha').then(res => {
                    debugger
                    console.log("/captcha")
                    console.log(res)

                    proxy.loginForm.token = res.data.data.token
                    proxy.captchaImg = res.data.data
                    proxy.loginForm.code = ''
                })
            }
        },
        created() {
            const {proxy} = getCurrentInstance()
            proxy.getCaptcha()
        }
    }
</script>

<style scoped>

    .el-row {
        background-color: #fafafa;
        height: 100%;
        display: flex;
        align-items: center;
        text-align: center;
        justify-content: center;
    }

    .el-divider {
        height: 200px;
    }

    .captchaImg {
        float: left;
        margin-left: 8px;
        border-radius: 4px;
    }

</style>