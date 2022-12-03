<template>
    <h1>handbook</h1>
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
            <el-button @click="getRegisterMan">搜索</el-button>
        </el-form-item>

        <el-form-item>
            <el-button type="primary" @click="dialogVisible = true">新增</el-button>
        </el-form-item>
        <el-form-item>
            <el-popconfirm title="确定通过审核吗？" @confirm="delHandle(null)">
                <template #reference>
                    <el-button type="success" slot="reference" :disabled="checkStatus">通过审核</el-button>
                </template>
            </el-popconfirm>
        </el-form-item>
        <el-form-item>
            <el-popconfirm title="确定拒绝审核吗？" @confirm="delHandle(null)">
                <template #reference>
                    <el-button type="danger" slot="reference" :disabled="checkStatus">拒绝通过</el-button>
                </template>
            </el-popconfirm>
        </el-form-item>
    </el-form>
</template>

<script>
    export default {
        name: "handbook"
    }
</script>

<style scoped>
    .el-pagination {
        float: right;
        margin-top: 22px;
    }
</style>
