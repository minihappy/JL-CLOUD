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
            <el-button @click="getRegisterList">搜索</el-button>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="dialogVisible = true">新增</el-button>
        </el-form-item>
    </el-form>


    <el-table
            ref="multipleTable"
            :data="tableData"
            tooltip-effect="dark"
            style="width: 100%"
            border
            stripe
            @selection-change="handleSelectionChange"
    >

        <el-table-column
                type="selection"
                width="55">
        </el-table-column>

        <el-table-column
                prop="name"
                label="报名主题"
                width="120">
        </el-table-column>
        <!--        <el-table-column-->
        <!--                prop="user"-->
        <!--                label="负责人员"-->
        <!--                show-overflow-tooltip>-->
        <!--        </el-table-column>-->
        <el-table-column
                :formatter="formatDate"
                prop="startTimeAt"
                label="开始时间"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                :formatter="formatDate"
                prop="endTimeAt"
                label="结束时间"
                show-overflow-tooltip>
        </el-table-column>
        <el-table-column
                prop="status"
                label="状态">
            <template v-slot:default="scope">
                <el-tag size="small" v-if="scope.row.status === 1" type="success">启用</el-tag>
                <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">禁用</el-tag>
            </template>

        </el-table-column>
        <el-table-column
                :formatter="formatDate"
                prop="updateAt"
                label="最后更改时间"
                show-overflow-tooltip>
        </el-table-column>

        <el-table-column
                prop="icon"
                label="操作">

            <template v-slot:default="scope">
                <el-button type="text" @click="permHandle(scope.row.id)">负责人</el-button>
                <el-divider direction="vertical"></el-divider>

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
    <!--新增报名主题对话框-->
    <el-dialog
            :title="editForm.id>1?'更改主题':'新增主题'"
            v-model="dialogVisible"
            width="600px"
            :before-close="handleClose">

        <el-form :model="editForm" :rules="addFormRules" ref="editForm">
            <el-form-item label="报名主题" prop="name" label-width="100px">
                <el-input v-model="editForm.name" autocomplete="off"></el-input>
            </el-form-item>

            <!--            <el-form-item label="负责人员" prop="email" label-width="100px">-->
            <!--                <template>-->
            <!--                    <el-checkbox-group v-model="editForm.users">-->
            <!--                        <template v-for="user in editForm.users">-->
            <!--                            <el-checkbox :label="user.username"/>-->
            <!--                        </template>-->
            <!--                    </el-checkbox-group>-->
            <!--                </template>-->
            <!--            </el-form-item>-->
            <el-form-item label="开始时间" prop="startTimeAt" label-width="100px">
                <el-date-picker
                        v-model="editForm.startTimeAt"
                        type="datetime"
                        placeholder="选择开始时间"
                        :default-time="defaultStartTime"
                />
                <!--                <el-input v-model="editForm.startTimeAt" autocomplete="off"></el-input>-->
            </el-form-item>
            <el-form-item label="结束时间" prop="endTimeAt" label-width="100px">
                <el-date-picker
                        v-model="editForm.endTimeAt"
                        type="datetime"
                        placeholder="选择结束时间"
                        :default-time="defaultEndTime"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status" label-width="100px">
                <el-radio-group v-model="editForm.status">
                    <el-radio :label="0">禁用</el-radio>
                    <el-radio :label="1">正常</el-radio>
                </el-radio-group>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="resetForm('editForm')">取 消</el-button>
            <el-button type="primary" @click="submitForm('editForm')">确 定</el-button>
        </div>
    </el-dialog>
</template>

<script>
    export default {
        name: "register",
        watch: {//监听改变
            // value(newValue, oldValue) {}
        },
        methods: {
            // openUpDiaLog() {//如果是新增操作，需要手动拉取报名负责人员信息
            //     if (!this.editForm.id) {
            //         this.$axios.get('/sys-comm/sys/register/info/' + 0).then(res => {
            //             this.editForm = res.data.data
            //
            //             this.dialogVisible = true
            //         })
            //     }
            // },
            handleSelectionChange() {//table选中调用

            },
            handleSizeChange(val) {//页数变换
                this.size = val
                this.getRegisterList()
            },
            handleCurrentChange(val) {//当前页变化
                this.current = val
                this.getRegisterList()
            },
            getRegisterList() {
                this.$axios.get("/sys-comm/sys/register/list", {
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

            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.dialogVisible = false
                this.editForm = {}
            },
            handleClose() {
                this.resetForm('editForm')
            }, submitForm(formName) {//提交新增报名主题
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys-comm/sys/register/' + (this.editForm.id ? 'update' : 'save'), this.editForm, {
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
                                        this.getRegisterList()
                                    }
                                });
                                this.dialogVisible = false
                            })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }, editHandle(id) {//编辑报名主题，传报名主题的id，覆盖表单数据
                this.$axios.get('/sys-comm/sys/register/info/' + id).then(res => {
                    this.editForm = res.data.data
                    this.dialogVisible = true
                })
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
            },
            delHandle(id) {
                this.$axios.get("/sys-comm/sys/register/delete/" + id).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getRegisterList()
                        }
                    });

                })
            },


        },
        components() {//计算
        },
        data() {
            return {
                defaultStartTime: new Date(2000, 1, 1, 0, 0, 0),
                defaultEndTime: new Date(2000, 1, 1, 0, 0, 0),
                searchForm: {},//
                total: 0,
                size: 10,
                current: 1,
                tableData: [],
                editForm: {},
                dialogVisible: false,//新增窗口打开关闭
                addFormRules: {
                    name: [
                        {required: true, message: '请输入报名主题', trigger: 'blur'}
                    ],
                    // users: [
                    //     {required: true, message: '请选择报名负责人', trigger: 'blur'}
                    // ],
                    status: [
                        {required: true, message: '请选择状态', trigger: 'blur'}
                    ],
                    startTimeAt: [
                        {required: true, message: '请选择开始时间', trigger: 'blur'}
                    ],
                    endTimeAt: [
                        {required: true, message: '请选择结束时间', trigger: 'blur'}
                    ]
                },
            }
        }, created() {
            this.getRegisterList();
        },

    }
</script>

<style scoped>
    .el-pagination {
        float: right;
        margin-top: 22px;
    }
</style>
