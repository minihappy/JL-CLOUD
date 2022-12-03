<template>
    <div class="common-layout" style="height: 100%">
        <el-container class="layout-container-demo" style="height: 100%">
            <el-header class="el-header">
                <div style="height: 100%;width: 100%;background-color: #fafafa ;display: flex;align-items: center;justify-content: start">
                    <img src="/vite.svg">
                </div>
            </el-header>
            <el-container>
                <el-main class="el-main" style="display:flex;justify-content: center;align-items: center">
                    <div v-if="checkProgress">
                        <el-form :model="inRegisterForm" :rules="rules" ref="loginForm" label-width="80px">
                            <!--                            <el-form-item label="报名主题" prop="registerName" style="width: 380px;">-->
                            <!--                                <el-input v-model="inRegisterForm.registerName"></el-input>-->
                            <!--                            </el-form-item>-->
                            <!--                            <el-form-item label="用户名" prop="name" style="width: 380px;">-->
                            <!--                                <el-input v-model="inRegisterForm.name"></el-input>-->
                            <!--                            </el-form-item>-->
                            <el-form-item label="电子邮箱" prop="email" style="width: 380px;">
                                <el-input v-model="inRegisterForm.email"></el-input>
                            </el-form-item>
                            <el-form-item label="验证码" prop="code" style="width: 380px;">
                                <el-input v-model="inRegisterForm.code" style="width: 172px; float: left"
                                          maxlength="5"></el-input>
                                <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha"></el-image>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" @click="submitForm('loginForm')">查看进度</el-button>
                                <el-button @click="backRegister()">返回报名</el-button>
                            </el-form-item>
                        </el-form>
                    </div>
                    <div v-else>
                        <div class="spinner">
                            <div class="double-bounce0"></div>
                            <div class="double-bounce1"></div>
                            <div class="double-bounce2">
                                <el-icon style="color: rgb(255, 255, 255);top:45px;font-size: 4em;left:5px;">
                                    <Check/>
                                </el-icon>
                            </div>
                        </div>
                        <div>
                            <p class="text-center" style="font-size: 30px">
                                <Strong>{{msg}}</Strong>
                            </p>
                            <p class="text-center" style="font-size: 18px;color: #cacaca">
                                等待工作人员审核中
                                <el-button size="mini" loading="true" text="true" circle></el-button>
                            </p>
                        </div>
                    </div>
                </el-main>
                <!--                <el-aside class="el-aside">Aside</el-aside>-->
            </el-container>
            <el-footer class="el-footer">
                <div style="height: 100%;width: 100%;background-color: #fafafa ;display: flex;align-items: center;justify-content: center">
                    <p><span style="font-family:arial;">Copyright &copy; </span>网上报名</p>
                    <p style="">技术支持 <a href="http://papahub.top/" target="_blank">miniHappy</a></p>
                </div>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "progress",
        watch: {//监听改变
            // value(newValue, oldValue) {}
        },
        methods: {//方法区
            getCaptcha() {
                this.$axios.get('/captcha').then(res => {
                    this.captchaImg = res.data.data.captcha
                    this.inRegisterForm.code = ''
                })
            }, submitForm(formName) {
                // const {proxy} = getCurrentInstance()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.get('/register/registerPersonnel/getProcess/' + this.inRegisterForm.email,).then(res => {
                            this.inRegisterForm = false
                            this.$message({
                                showClose: true,
                                message: res.data.data,
                                type: 'success',
                            });
                        })
                    } else {
                        return false;
                    }
                });
            }, backRegister() {
                this.$router.push("/");
            }
        },
        components() {//计算
        },
        data() {
            return {
                msg: '',
                checkProgress: true,
                inRegisterForm: {},
                defaultStartTime: new Date(2000, 1, 1, 0, 0, 0),
                defaultEndTime: new Date(2000, 1, 1, 0, 0, 0),
                checkStatus: "",//
                searchForm: {},//
                total: 0,
                size: 10,
                current: 1,
                tableData: [],
                editForm: {},
                dialogVisible: false,//新增窗口打开关闭
                inRegisterForm: {
                    name: '',
                    registerName: '',
                    email: '',
                    code: '',
                },
                rules: {
                    name: [
                        {required: true, message: '请输入姓名', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '请输入电子邮箱', trigger: 'blur'},
                        // {min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur'}
                    ],
                    registerName: [
                        {required: true, message: '请输入报名主题', trigger: 'blur'},
                    ],
                    code: [
                        {required: true, message: '请输入验证码', trigger: 'blur'},
                    ],
                },
                captchaImg: ""
            }
        },
        created() {
            if (!(JSON.stringify(this.$route.query) === "{}")) {
                this.checkProgress = false
                this.msg = this.$route.query.msg
            }
            this.getCaptcha()
        },

    }
</script>

<style scoped>
    .double-bounce0 {
        width: 100%;
        height: 100%;
        border-radius: 50%;
        background-color: #f3fcf2;
        opacity: 0.6;
        position: absolute;
        top: 0;
        left: 0;
        -webkit-animation: bounce 2.0s infinite ease-in-out;
        animation: bounce 2.0s infinite ease-in-out;
    }

    .double-bounce1 {
        width: 80%;
        height: 80%;
        border-radius: 50%;
        background-color: #b6edb0;
        opacity: 0.6;
        position: absolute;
        top: 10%;
        left: 10%;
        -webkit-animation: bounce 2.0s infinite ease-in-out;
        animation: bounce 2.0s infinite ease-in-out;
    }

    .double-bounce2 {
        width: 60%;
        height: 60%;
        border-radius: 50%;
        background-color: #59c648;
        position: absolute;
        top: 20%;
        left: 20%;
        -webkit-animation: bounce 2.0s infinite ease-in-out;
        animation: bounce 2.0s infinite ease-in-out;
        -webkit-animation-delay: -1.0s;
        animation-delay: -1.0s;
    }

    .spinner {
        width: 250px;
        height: 250px;
        position: relative;
        margin: 0 auto;
        margin-bottom: 40px;
    }

    .captchaImg {
        float: left;
        margin-left: 8px;
        border-radius: 4px;
    }
</style>
