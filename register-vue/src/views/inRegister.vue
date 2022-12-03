<template>
    <div class="common-layout" style="height: 100%">
        <el-container class="layout-container-demo" style="height: 100%">
            <el-header class="el-header">
                <div style="height: 100%;width: 100%;background-color: #fafafa ;display: flex;align-items: center;justify-content: start">
                    <img src="/vite.svg">
                </div>
            </el-header>
            <el-container>
                <el-main class="el-main">
                    <el-row type="flex" class="row-bg" justify="center" style="height: 100%;align-items: center;">
                        <!--                        style="display: contents;"-->
                        <el-col :xl="5" :lg="6">
                            <el-form :model="inRegisterForm" :rules="rules" ref="loginForm" label-width="80px">
                                <el-form-item label="报名主题" prop="registerName" style="width: 380px;">
                                    <el-input v-model="inRegisterForm.registerName"></el-input>
                                </el-form-item>
                                <el-form-item label="用户名" prop="name" style="width: 380px;">
                                    <el-input v-model="inRegisterForm.name"></el-input>
                                </el-form-item>
                                <el-form-item label="电子邮箱" prop="email" style="width: 380px;">
                                    <el-input v-model="inRegisterForm.email"></el-input>
                                </el-form-item>
                                <el-form-item label="验证码" prop="code" style="width: 380px;">
                                    <el-input v-model="inRegisterForm.code" style="width: 172px; float: left"
                                              maxlength="5"></el-input>
                                    <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha"></el-image>
                                </el-form-item>

                                <el-form-item>
                                    <el-button type="primary" @click="submitForm('loginForm')">立即创建</el-button>
                                    <el-button @click="resetForm('loginForm')">重置</el-button>
                                </el-form-item>
                            </el-form>
                        </el-col>
                        <el-col :xl="3" :lg="4">
                            <el-button style="font-size: 20px;" type="danger"
                                       plain="true" text="true" @click="searchProgress">
                                <el-icon>
                                    <Search/>
                                </el-icon>
                                查询报名进度
                            </el-button>
                        </el-col>
                    </el-row>
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
        name: "inRegister.vue",
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
                        this.$router.push({name: 'register', query: this.inRegisterForm})
                    } else {
                        return false;
                    }
                });
            }, resetForm(formName) {
                // const {proxy} = getCurrentInstance()
                this.$refs[formName].resetFields();
            }, searchProgress() {
                this.$router.push("/progress");
            }
        },
        components() {//计算
        },
        data() {
            return {
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
                        {required: true, message: '请输入报名验证码', trigger: 'blur'},
                    ],
                },
                captchaImg: ""
            }
        },
        created() {
            this.getCaptcha()
        },

    }
</script>

<style scoped>
    .el-aside {
        overflow: auto;
        box-sizing: border-box;
        flex-shrink: 0;
        width: var(--el-aside-width, 300px)
    }

    .el-footer {
        --el-footer-padding: 0 0;
    }

    /*.layout-container-demo .el-header {*/
    /*    position: relative;*/
    /*    background-color: var(--el-color-primary-light-7);*/
    /*    color: var(--el-text-color-primary);*/
    /*}*/

    .layout-container-demo .el-aside {
        color: var(--el-text-color-primary);
        background: var(--el-color-primary-light-8);
    }

    .layout-container-demo .el-menu {
        border-right: none;
    }

    .layout-container-demo .el-main {
        padding: 0;
    }

    .layout-container-demo .toolbar {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        right: 20px;
    }

    .captchaImg {
        float: left;
        margin-left: 8px;
        border-radius: 4px;
    }
</style>
