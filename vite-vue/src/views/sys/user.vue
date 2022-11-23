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
                :visible.sync="dialogVisible"
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
        <el-dialog title="分配角色" :visible.sync="roleDialogFormVisible" width="1200px">
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
        <template v-for="item in gg">
            <h1>{{item}}</h1>
        </template>
    </div>
</template>

<script type="ts" setup>
    import {ref, onMounted, reactive} from 'vue'
    import {ElTree} from 'element-plus'
    import axios from "axios";

    let name = "user"
    let searchForm = {}
    let RoleSearchForm = {}
    let delBtlStatus = true

    let total = ref(0)
    let RoleTotal = ref(0)
    let size = ref(10)
    let current = ref(1)
    let RoleSize = ref(10)
    let RoleCurrent = ref(1)
    let dialogVisible = false
    let editForm = {}

    let tableData = ref([])

    let editFormRules = {
        username: [
            {required: true, message: '请输入用户名称', trigger: 'blur'}
        ],
        email: [
            {required: true, message: '请输入邮箱', trigger: 'blur'}
        ],
        status: [
            {required: true, message: '请选择状态', trigger: 'blur'}
        ]
    }

    let multipleSelection = []

    let roleDialogFormVisible = false
    let defaultProps = {
        children: 'children',
        label: 'name'
    }
    let roleForm = ref({})
    let roleTreeData = ref([])
    let treeCheckedKeys = []
    let checkStrictly = true

    onMounted(() => {
        getUserList()
        getRoleList()
    })

    function getRoleList() {
        axios.get("/sys-comm/sys/role/list", {
            params: {
                name: RoleSearchForm.name,
                current: RoleCurrent.value,
                size: RoleSize.value
            }
        }).then(res => {
            roleTreeData.value = res.data.data.content
            RoleSize = res.data.data.size
            RoleCurrent = res.data.data.number + 1
            RoleTotal = res.data.data.totalElements
        })
    }

    function toggleSelection(rows) {
        if (rows) {
            rows.forEach(row => {
                $refs.multipleTable.toggleRowSelection(row);
            });
        } else {
            $refs.multipleTable.clearSelection();
        }
    }

    function handleSelectionChange(val) {
        console.log("勾选")
        console.log(val)
        multipleSelection = val;

        delBtlStatus = val.length == 0
    }

    function RoleHandleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        RoleSize = val
        getRoleList()
    }

    function RoleHandleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        RoleCurrent = val
        getRoleList()
    }

    function handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        size = val
        getUserList()
    }

    function handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        current = val
        getUserList()
    }


    function resetForm(formName) {
        $refs[formName].resetFields();
        dialogVisible = false
        editForm = {}
    }

    function handleClose() {
        resetForm('editForm')
    }


    function getUserList() {
        axios.get("/sys-comm/sys/user/list", {
            params: {
                username: searchForm.username,
                current: current.value,
                size: size.value
            }
        }).then(res => {
            tableData.value = res.data.data.content
            size = res.data.data.size
            current = res.data.data.number + 1
            total = res.data.data.totalElements
        })
    }


    function submitForm(formName) {
        $refs[formName].validate((valid) => {
            if (valid) {
                axios.post('/sys-comm/sys/user/' + (editForm.id ? 'update' : 'save'), editForm)
                    .then(res => {

                        $message({
                            showClose: true,
                            message: '恭喜你，操作成功',
                            type: 'success',
                            onClose: () => {
                                getUserList()
                            }
                        });

                        dialogVisible = false
                    })
            } else {
                console.log('error submit!!');
                return false;
            }
        });
    }

    function editHandle(id) {
        axios.get('/sys-comm/sys/user/info/' + id).then(res => {
            editForm = res.data.data

            dialogVisible = true
        })
    }

    function delHandle(id) {

        var ids = []

        if (id) {
            ids.push(id)
        } else {
            multipleSelection.forEach(row => {
                ids.push(row.id)
            })
        }

        axios.post("/sys-comm/sys/user/delete", ids).then(res => {
            $message({
                showClose: true,
                message: '恭喜你，操作成功',
                type: 'success',
                onClose: () => {
                    getUserList()
                }
            });
        })
    }

    function roleHandle(id) {
        roleDialogFormVisible = true

        axios.get('/sys-comm/sys/user/info/' + id).then(res => {
            roleForm = res.data.data
            let roleIds = []
            res.data.data.role.forEach(row => {
                roleIds.push(row.id)
            })

            $refs.roleTree.setCheckedKeys(roleIds)
        })
    }

    function submitRoleHandle(formName) {
        var roleIds = $refs.roleTree.getCheckedKeys()
        axios.post('/sys-comm/sys/user/role/' + roleForm.value.id, roleIds).then(res => {
            $message({
                showClose: true,
                message: '恭喜你，操作成功',
                type: 'success',
                onClose: () => {
                    getUserList()
                }
            });

            roleDialogFormVisible = false
        })
    }

    function rePassHandle(id, username) {
        $confirm('将重置用户【' + username + '】的密码, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            axios.post("/sys-comm/sys/user/rePass", id).then(res => {
                $message({
                    showClose: true,
                    message: '恭喜你，操作成功',
                    type: 'success',
                    onClose: () => {
                    }
                });
            })
        })
    }

</script>

<style scoped>

    .el-pagination {
        float: right;
        margin-top: 22px;
    }

</style>
