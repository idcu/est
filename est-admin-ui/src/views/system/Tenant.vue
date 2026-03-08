<template>
  <div class="tenant-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="租户名称">
          <el-input v-model="searchForm.name" placeholder="请输入租户名�? clearable />
        </el-form-item>
        <el-form-item label="租户编码">
          <el-input v-model="searchForm.code" placeholder="请输入租户编�? clearable />
        </el-form-item>
        <el-form-item label="状�?>
          <el-select v-model="searchForm.status" placeholder="请选择状�? clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>租户列表</span>
          <el-button type="primary" @click="handleAdd">新增租户</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="租户名称" width="200" />
        <el-table-column prop="code" label="租户编码" width="150" />
        <el-table-column prop="type" label="租户类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'primary' : row.type === 2 ? 'success' : 'info'">
              {{ row.type === 1 ? 'COLUMN' : row.type === 2 ? 'SCHEMA' : 'DATABASE' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="联系�? width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状�? width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" label="过期时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSearch"
        @current-change="handleSearch"
        style="margin-top: 16px; justify-content: flex-end"
      />
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px">
        <el-form-item label="租户名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入租户名�? />
        </el-form-item>
        <el-form-item label="租户编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入租户编�? :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="租户类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择租户类型">
            <el-option label="COLUMN (字段�?" :value="1" />
            <el-option label="SCHEMA (模式�?" :value="2" />
            <el-option label="DATABASE (数据库级)" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="联系�? prop="contactName">
          <el-input v-model="formData.contactName" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="请输入联系电�? />
        </el-form-item>
        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="formData.contactEmail" placeholder="请输入联系邮�? />
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker
            v-model="formData.expireTime"
            type="datetime"
            placeholder="选择过期时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状�? prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备�? />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { listTenants, getTenant, createTenant, updateTenant, deleteTenant, updateTenantStatus } from '@/api/tenant'

const searchForm = reactive({
  name: '',
  code: '',
  status: undefined as number | undefined
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const formData = reactive({
  id: undefined as number | undefined,
  name: '',
  code: '',
  type: 1,
  contactName: '',
  contactPhone: '',
  contactEmail: '',
  expireTime: '',
  status: 1,
  remark: ''
})

const formRules: FormRules = {
  name: [
    { required: true, message: '请输入租户名�?, trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入租户编�?, trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择租户类型', trigger: 'change' }
  ]
}

const loadData = async () => {
  try {
    const res = await listTenants({
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('加载租户列表失败', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.code = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增租户'
  isEdit.value = false
  Object.assign(formData, {
    id: undefined,
    name: '',
    code: '',
    type: 1,
    contactName: '',
    contactPhone: '',
    contactEmail: '',
    expireTime: '',
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = async (row: any) => {
  dialogTitle.value = '编辑租户'
  isEdit.value = true
  try {
    const res = await getTenant(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取租户信息失败', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateTenant(formData)
          ElMessage.success('更新成功')
        } else {
          await createTenant(formData)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('提交失败', error)
      }
    }
  })
}

const handleStatus = async (row: any) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await updateTenantStatus(row.id, newStatus)
    ElMessage.success(newStatus === 1 ? '启用成功' : '禁用成功')
    loadData()
  } catch (error) {
    console.error('状态更新失�?, error)
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除该租户吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTenant(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.tenant-page {
  .search-card {
    margin-bottom: 16px;
    
    .search-form {
      margin-bottom: 0;
    }
  }
  
  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style>
