<template>
    <div>
        <el-form
                style="text-align: left"
                class="demo-form-inline"
                :inline="true">
            <el-form-item>
                <el-input
                        v-model="searchForm.username"
                        placeholder="用户名"
                        clearable
                >
                </el-input>
            </el-form-item>

            <el-form-item>
                <el-button @click="getUserList">搜索</el-button>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="dialogVisible = true" v-if="hasAuth('sys:user:save')">新增</el-button>
            </el-form-item>
            <el-form-item>
                <el-popconfirm title="确定批量删除?"
                               @confirm="delHandle(null)">
                    <template #reference>
                        <el-button type="danger" slot="reference" :disabled="delBtlStatus"
                                   v-if="hasAuth('sys:user:delete')">批量删除
                        </el-button>
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
                    label="头像"
                    width="50">
                <template v-slot:default="scope">
                    <el-avatar size="small" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>

            <el-table-column
                    prop="username"
                    label="用户名"
                    width="120">
            </el-table-column>
            <el-table-column
                    prop="code"
                    label="角色名称">
                <template v-slot:default="scope">
                    <el-tag size="small" type="info" v-for="item in scope.row.role">{{item.name}}</el-tag>
                </template>

            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮箱">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="手机号">
            </el-table-column>

            <el-table-column
                    prop="status"
                    label="状态">
                <template v-slot:default="scope">
                    <el-tag size="small" v-if="scope.row.status === 1" type="success">正常</el-tag>
                    <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">禁用</el-tag>
                </template>

            </el-table-column>
            <el-table-column
                    prop="createTime"
                    width="200"
                    label="创建时间"
            >
            </el-table-column>
            <el-table-column
                    prop="icon"
                    width="260px"
                    label="操作">

                <template v-slot:default="scope">
                    <el-button type="text" @click="roleHandle(scope.row.id)">分配角色</el-button>
                    <el-divider direction="vertical"></el-divider>

                    <el-button type="text" @click="rePassHandle(scope.row.id, scope.row.username)">重置密码</el-button>
                    <el-divider direction="vertical"></el-divider>

                    <el-button type="text" @click="editHandle(scope.row.id)">编辑</el-button>
                    <el-divider direction="vertical"></el-divider>

                    <el-popconfirm title="确定删除吗?"
                                   @confirm="delHandle(scope.row.id)">
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


        <!--新增对话框-->
        <el-dialog
                title="提示"
                v-model="dialogVisible"
                width="600px"
                :before-close="handleClose">

            <el-form :model="editForm" :rules="editFormRules" ref="editForm">
                <el-form-item label="用户名" prop="username" label-width="100px">
                    <el-input v-model="editForm.username" autocomplete="off"></el-input>
                    <el-alert
                            title="初始密码为888888"
                            :closable="false"
                            type="info"
                            style="line-height: 12px;"
                    ></el-alert>
                </el-form-item>

                <el-form-item label="邮箱" prop="email" label-width="100px">
                    <el-input v-model="editForm.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone" label-width="100px">
                    <el-input v-model="editForm.phone" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="状态" prop="statu" label-width="100px">
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

        <!-- 分配权限对话框 -->
        <el-dialog title="分配角色" v-model="roleDialogFormVisible" width="1200px">
            <div style="display: inline-flex">
                <el-input
                        v-model="RoleSearchForm.name"
                        placeholder="用户名"
                        clearable
                >
                </el-input>
                <el-button @click="getRoleList">搜索</el-button>
            </div>
            <el-form :model="roleForm">
                <el-tree
                        :data="roleTreeData"
                        show-checkbox
                        ref="roleTree"
                        :check-strictly=checkStrictly
                        node-key="id"
                        :default-expand-all=true
                        :props="defaultProps">
                </el-tree>
                <el-pagination
                        @size-change="RoleHandleSizeChange"
                        @current-change="RoleHandleCurrentChange"
                        layout="total, sizes, prev, pager, next, jumper"
                        :page-sizes="[10, 20, 50, 100]"
                        :current-page="RoleCurrent"
                        :page-size="RoleSize"
                        :total="RoleTotal">
                </el-pagination>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button @click="roleDialogFormVisible=false">取 消</el-button>
                <el-button type="primary" @click="submitRoleHandle('roleForm')">确 定</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import qs from "qs";

    export default {
        name: "user",
        data() {
            return {
                searchForm: {},
                RoleSearchForm: {},
                delBtlStatus: true,

                total: 0,
                RoleTotal: 0,
                size: 10,
                current: 1,
                RoleSize: 10,
                RoleCurrent: 1,
                dialogVisible: false,
                editForm: {},

                tableData: [],

                editFormRules: {
                    username: [
                        {required: true, message: '请输入用户名称', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '请输入邮箱', trigger: 'blur'}
                    ],
                    status: [
                        {required: true, message: '请选择状态', trigger: 'blur'}
                    ]
                },

                multipleSelection: [],

                roleDialogFormVisible: false,
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                roleForm: {},
                roleTreeData: [],
                treeCheckedKeys: [],
                checkStrictly: true

            }
        },
        created() {
            this.getUserList()
            this.getRoleList()
        },
        methods: {
            getRoleList() {
                this.$axios.get("/sys-comm/sys/role/list", {
                    params: {
                        name: this.RoleSearchForm.name,
                        current: this.RoleCurrent,
                        size: this.RoleSize
                    }
                }).then(res => {
                    this.roleTreeData = res.data.data.content
                    this.RoleSize = res.data.data.size
                    this.RoleCurrent = res.data.data.number + 1
                    this.RoleTotal = res.data.data.totalElements
                })
            },
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                console.log("勾选")
                console.log(val)
                this.multipleSelection = val;

                this.delBtlStatus = val.length == 0
            },
            RoleHandleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.RoleSize = val
                this.getRoleList()
            },
            RoleHandleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.RoleCurrent = val
                this.getRoleList()
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.size = val
                this.getUserList()
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.current = val
                this.getUserList()
            },

            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.dialogVisible = false
                this.editForm = {}
            },
            handleClose() {
                this.resetForm('editForm')
            },

            getUserList() {
                this.$axios.get("/sys-comm/sys/user/list", {
                    params: {
                        username: this.searchForm.username,
                        current: this.current,
                        size: this.size
                    }
                }).then(res => {
                    this.tableData = res.data.data.content
                    this.size = res.data.data.size
                    this.current = res.data.data.number + 1
                    this.total = res.data.data.totalElements
                })
            },

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys-comm/sys/user/' + (this.editForm.id ? 'update' : 'save'), this.editForm, {
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
                                        this.getUserList()
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
            editHandle(id) {
                this.$axios.get('/sys-comm/sys/user/info/' + id).then(res => {
                    this.editForm = res.data.data

                    this.dialogVisible = true
                })
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

                this.$axios.post("/sys-comm/sys/user/delete", ids, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getUserList()
                        }
                    });
                })
            },

            roleHandle(id) {
                this.roleDialogFormVisible = true

                this.$axios.get('/sys-comm/sys/user/info/' + id).then(res => {
                    this.roleForm = res.data.data
                    let roleIds = []
                    res.data.data.role.forEach(row => {
                        roleIds.push(row.id)
                    })

                    this.$refs.roleTree.setCheckedKeys(roleIds)
                })
            },
            submitRoleHandle(formName) {
                var roleIds = this.$refs.roleTree.getCheckedKeys()
                this.$axios.post('/sys-comm/sys/user/role/' + this.roleForm.id, roleIds, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(res => {
                    this.$message({
                        showClose: true,
                        message: '恭喜你，操作成功',
                        type: 'success',
                        onClose: () => {
                            this.getUserList()
                        }
                    });

                    this.roleDialogFormVisible = false
                })
            },
            rePassHandle(id, username) {

                this.$confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.post("/sys-comm/sys/user/rePass", id).then(res => {
                        this.$message({
                            showClose: true,
                            message: '恭喜你，操作成功',
                            type: 'success',
                            onClose: () => {
                            }
                        });
                    })
                })
            }
        }
    }
</script>

<style scoped>

    .el-pagination {
        float: right;
        margin-top: 22px;
    }

</style>
