<template>
    <el-form style="text-align: left" :inline="true">
        <el-form-item>
            <el-input
                    v-model="searchForm.name"
                    placeholder="名称"
                    clearable
            >
            </el-input>
        </el-form-item>

        <el-form-item>
            <el-button @click="getPersonnelList">搜索</el-button>
        </el-form-item>


        <el-form-item>
            <el-button type="primary" @click="dialogVisible = true">新增</el-button>
        </el-form-item>
        <el-form-item>
            <el-popconfirm title="确定通过审核吗？" @confirm="Pass(null)">
                <template #reference>
                    <el-button type="success" slot="reference" :disabled="checkStatus">通过审核</el-button>
                </template>
            </el-popconfirm>
        </el-form-item>
        <el-form-item>
            <el-popconfirm title="确定拒绝审核吗？" @confirm="unPass(null)">
                <template #reference>
                    <el-button type="info" slot="reference" :disabled="checkStatus">拒绝通过</el-button>
                </template>
            </el-popconfirm>
        </el-form-item>
        <el-form-item>
            <el-popconfirm title="确定批量删除吗？" @confirm="delHandle(null)">
                <template #reference>
                    <el-button type="danger" slot="reference" :disabled="checkStatus">批量删除</el-button>
                </template>
            </el-popconfirm>
        </el-form-item>
    </el-form>


    <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            border
            stripe
            @selection-change="handleSelectionChange">

        <el-table-column
                type="selection"
                width="55">
        </el-table-column>
        <el-table-column
                prop="photo"
                label="照片"
                width="120">
        </el-table-column>
        <el-table-column
                prop="name"
                label="名称"
                width="120">
        </el-table-column>
        <el-table-column
                prop="phone"
                label="手机号"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="id_card"
                label="身份证"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="email"
                label="电子邮箱"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="check_man"
                label="审核员"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="created_at"
                label="报名时间"
                :formatter="formatDate"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="check_at"
                label="审核时间"
                :formatter="formatDate"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="update_at"
                label="修改时间"
                :formatter="formatDate"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="status"
                label="状态">
            <template v-slot:default="scope">
                <el-tag size="small" v-if="scope.row.status === 1" type="success">通过</el-tag>
                <el-tag size="small" v-else-if="scope.row.status === 0" type="info">未审核</el-tag>
                <el-tag size="small" v-else-if="scope.row.status === 2" type="danger">拒绝</el-tag>
            </template>

        </el-table-column>

        <el-table-column
                prop="icon"
                label="操作">

            <template v-slot:default="scope">

                <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
                <el-divider direction="vertical"></el-divider>


                <el-popconfirm title="这是一段内容确定删除吗？" @confirm="delHandle(scope.row.id)">
                    <template #reference>
                        <el-button type="text" slot="reference">删除</el-button>
                    </template>
                </el-popconfirm>


            </template>
        </el-table-column>

    </el-table>


    <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[10, 20, 50, 100]"
            :current-page="current"
            :page-size="size"
            :total="total">
    </el-pagination>
    <!--新增报名对话框-->
    <el-dialog
            title="提示"
            v-model="dialogVisible"
            width="600px"
            @open="getRegister"
            :before-close="handleClose">
        <el-form :model="editForm" :rules="editFormRules" ref="editForm">
            <el-form-item label="报名主题" prop="register_id" label-width="100px">
                <el-select v-model="editForm.register_id" class="m-2" placeholder="选择报名主题">
                    <el-option
                            v-for="item in register"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="名称" prop="name" label-width="100px">
                <el-input v-model="editForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone" label-width="100px">
                <el-input v-model="editForm.phone" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="身份证" prop="id_card" label-width="100px">
                <el-input v-model="editForm.id_card" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="电子邮箱" prop="email" label-width="100px">
                <el-input v-model="editForm.email" autocomplete="off"></el-input>
            </el-form-item>
            <!--            <el-form-item label="状态" prop="status" label-width="100px">-->
            <!--                <el-radio-group v-model="editForm.status">-->
            <!--                    <el-radio :label="0">拒绝</el-radio>-->
            <!--                    <el-radio :label="1">通过</el-radio>-->
            <!--                </el-radio-group>-->
            <!--            </el-form-item>-->
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetForm('editForm')">取 消</el-button>
            <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
    export default {
        name: "personnel",
        watch: {//监听改变
            // value(newValue, oldValue) {}
        },
        methods: {
            Pass() {//搜索报名人员
                let ids = []
                this.multipleSelection.forEach(row => {
                    ids.push(row.id)
                })
                this.$axios.get("/sys-comm/sys/personnel/pass/" + ids, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getPersonnelList()
                        }
                    });
                })
            },
            unPass() {
                let ids = []
                this.multipleSelection.forEach(row => {
                    ids.push(row.id)
                })
                this.$axios.get("/sys-comm/sys/personnel/unPass/" + ids, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getPersonnelList()
                        }
                    });
                })
            }
            , handleSizeChange(val) {//页数变换
                this.size = val
                this.getPersonnelList()
            },
            handleCurrentChange(val) {//当前页变化
                this.current = val
                this.getPersonnelList()
            },
            getPersonnelList() {
                this.$axios.get("/sys-comm/sys/personnel/list", {
                    params: {
                        name: this.searchForm.name,
                        current: this.current,
                        size: this.size
                    }
                }).then(res => {
                    this.tableData = res.data.data.content
                    this.size = res.data.data.pageable.pageSize
                    this.current = res.data.data.pageable.pageNumber + 1
                    this.total = res.data.data.totalElements
                })
            },
            handleClose() {
                this.resetForm('editForm')
            }, resetForm(formName) {
                this.$refs[formName].resetFields();
                this.dialogVisible = false
                this.editForm = {}
            }, submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys-comm/sys/personnel/' + (this.editForm.id ? 'update/' : 'save/') + this.editForm.register_id, this.editForm, {
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        })
                            .then(res => {

                                this.$message({
                                    showClose: true,
                                    message: '恭喜你，操作成功',
                                    type: 'success',
                                    onClose: () => {
                                        this.getPersonnelList()
                                    }
                                });
                                this.dialogVisible = false
                            })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            delHandle(id) {
                var ids = []

                if (id) {
                    ids.push(id)
                } else {
                    this.multipleSelection.forEach(row => {
                        ids.push(row.id)
                    })
                }
                this.$axios.get("/sys-comm/sys/personnel/delete/" + ids, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getPersonnelList()
                        }
                    });

                })
            }, handleSelectionChange(val) {
                this.multipleSelection = val;
                this.checkStatus = val.length == 0
            }, formatDate(row, column) {//格式化数据类型
                // 获取单元格数据
                let data = row[column.property]
                if (data == null) {
                    return null
                }
                let dt = new Date(data)
                return dt.getFullYear() + '-' +
                    ((dt.getMonth() + 1) < 10 ? ('0' + (dt.getMonth() + 1)) : (dt.getMonth() + 1)) + '-' +
                    (dt.getDate() < 10 ? ('0' + dt.getDate()) : dt.getDate()) + ' ' +
                    (dt.getHours() < 10 ? ('0' + dt.getHours()) : dt.getHours()) + ':' +
                    (dt.getMinutes() < 10 ? ('0' + dt.getMinutes()) : dt.getMinutes()) + ':' +
                    (dt.getSeconds() < 10 ? ('0' + dt.getSeconds()) : dt.getSeconds())
            }, editHandle(id) {
                this.$axios.get('/sys-comm/sys/personnel/info/' + id).then(res => {
                    this.editForm = res.data.data
                    this.editForm.register_id = res.data.data.register.id
                    this.dialogVisible = true
                })
            }, getRegister() {
                this.$axios.get('/sys-comm/sys/register/doingRegister').then(res => {
                    this.register = res.data.data
                })
            }
        },
        components() {//计算
        },
        data() {
            return {
                register: [],
                multipleSelection: [],
                defaultStartTime: new Date(2000, 1, 1, 0, 0, 0),
                defaultEndTime: new Date(2000, 1, 1, 0, 0, 0),
                checkStatus: true,//
                searchForm: {},//
                total: 0,
                size: 10,
                current: 1,
                tableData: [],
                editForm: {},
                dialogVisible: false,//新增窗口打开关闭
                editFormRules: {
                    register_id: [
                        {required: true, message: '请填写报名主题', trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请填写报名人名字', trigger: 'blur'}
                    ],
                    // users: [
                    //     {required: true, message: '请选择报名负责人', trigger: 'blur'}
                    // ],
                    phone: [
                        {required: true, message: '请填写手机号', trigger: 'blur'}
                    ],
                    id_card: [
                        {required: true, message: '请填写身份证', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '请填写电子邮件', trigger: 'blur'}
                    ],
                    status: [
                        {required: true, message: '状态为必填', trigger: 'blur'}
                    ]
                },
            }
        },

        created() {
            this.getPersonnelList()
        },

    }
</script>

<style scoped>
    .el-pagination {
        float: right;
        margin-top: 22px;
    }
</style>
