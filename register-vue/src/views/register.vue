<template>
    <div class="common-layout" style="height: 100%">
        <el-container class="layout-container-demo" style="height: 100%">
            <el-header class="el-header">
                <div style="height: 100%;width: 100%;background-color: #fafafa ;display: flex;align-items: center;justify-content: start">
                    <img src="/vite.svg">
                </div>
            </el-header>
            <el-container>
                <el-main class="el-main" style="display:flex;height: 100%;align-items: center;justify-content: center">
                    <el-form :model="inRegisterForm" :rules="rules" ref="registerForm" label-width="80px">
                        <el-form-item label="报名主题" prop="registerName" style="width: 380px;">
                            <el-input v-model="inRegisterForm.registerName"></el-input>
                        </el-form-item>
                        <el-form-item label="用户名" prop="name" style="width: 380px;">
                            <el-input v-model="inRegisterForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="电子邮箱" prop="email" style="width: 380px;">
                            <el-input v-model="inRegisterForm.email"></el-input>
                        </el-form-item>
                        <el-form-item label="身份证" prop="idCard" style="width: 380px;">
                            <el-input v-model="inRegisterForm.idCard"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号" prop="phone" style="width: 380px;">
                            <el-input v-model="inRegisterForm.phone"></el-input>
                        </el-form-item>
                        <el-button type="primary" @click="submitForm('registerForm')">立即报名</el-button>
                    </el-form>
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
        name: "register",
        watch: {//监听改变
            // value(newValue, oldValue) {}
        },
        methods: {//方法区
            submitForm(formName) {
                // const {proxy} = getCurrentInstance()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/register/registerPersonnel/save', this.inRegisterForm, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }).then(res => {
                            this.$router.push({name: "progress", query: {msg: res.data.msg}})
                        })
                    } else {
                        return false;
                    }
                });
            },
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
                    phone: [
                        {required: true, message: '请输入手机号', trigger: 'blur'},
                        // {min: 5, max: 5, message: '长度为 5 个字符', trigger: 'blur'}
                    ],
                    idCard: [
                        {required: true, message: '请输入身份证号', trigger: 'blur'},
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
            if (!(JSON.stringify(this.$route.query) === "{}")) {
                this.inRegisterForm = this.$route.query
            } else {
                this.$router.push("/")
            }
        },

    }
</script>

<style scoped>

</style>
