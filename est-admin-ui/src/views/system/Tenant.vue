<template>
  <div class="tenant-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Tenant Name">
          <el-input v-model="searchForm.name" placeholder="Please enter tenant name" clearable />
        </el-form-item>
        <el-form-item label="Tenant Code">
          <el-input v-model="searchForm.code" placeholder="Please enter tenant code" clearable />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="Please select status" clearable>
            <el-option label="Enabled" :value="1" />
            <el-option label="Disabled" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>Tenant List</span>
          <el-button type="primary" @click="handleAdd">Add Tenant</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Tenant Name" width="200" />
        <el-table-column prop="code" label="Tenant Code" width="150" />
        <el-table-column prop="type" label="Tenant Type" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'primary' : row.type === 2 ? 'success' : 'info'">
              {{ row.type === 1 ? 'COLUMN' : row.type === 2 ? 'SCHEMA' : 'DATABASE' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contactName" label="Contact" width="120" />
        <el-table-column prop="contactPhone" label="Phone" width="130" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expireTime" label="Expire Time" width="180" />
        <el-table-column prop="createTime" label="Created At" width="180" />
        <el-table-column label="Actions" width="250" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="warning" size="small" @click="handleStatus(row)">
              {{ row.status === 1 ? 'Disable' : 'Enable' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">Delete</el-button>
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
        <el-form-item label="Tenant Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter tenant name" />
        </el-form-item>
        <el-form-item label="Tenant Code" prop="code">
          <el-input v-model="formData.code" placeholder="Please enter tenant code" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="Tenant Type" prop="type">
          <el-select v-model="formData.type" placeholder="Please select tenant type">
            <el-option label="COLUMN" :value="1" />
            <el-option label="SCHEMA" :value="2" />
            <el-option label="DATABASE" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="Contact" prop="contactName">
          <el-input v-model="formData.contactName" placeholder="Please enter contact" />
        </el-form-item>
        <el-form-item label="Phone" prop="contactPhone">
          <el-input v-model="formData.contactPhone" placeholder="Please enter phone" />
        </el-form-item>
        <el-form-item label="Email" prop="contactEmail">
          <el-input v-model="formData.contactEmail" placeholder="Please enter email" />
        </el-form-item>
        <el-form-item label="Expire Time" prop="expireTime">
          <el-date-picker
            v-model="formData.expireTime"
            type="datetime"
            placeholder="Select expire time"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">Enabled</el-radio>
            <el-radio :label="0">Disabled</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="Remark" prop="remark">
          <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="Please enter remark" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
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
    { required: true, message: 'Please enter tenant name', trigger: 'blur' }
  ],
  code: [
    { required: true, message: 'Please enter tenant code', trigger: 'blur' }
  ],
  type: [
    { required: true, message: 'Please select tenant type', trigger: 'change' }
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
    console.error('Failed to load tenant list', error)
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
  dialogTitle.value = 'Add Tenant'
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
  dialogTitle.value = 'Edit Tenant'
  isEdit.value = true
  try {
    const res = await getTenant(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('Failed to get tenant info', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateTenant(formData)
          ElMessage.success('Updated successfully')
        } else {
          await createTenant(formData)
          ElMessage.success('Created successfully')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Submit failed', error)
      }
    }
  })
}

const handleStatus = async (row: any) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await updateTenantStatus(row.id, newStatus)
    ElMessage.success(newStatus === 1 ? 'Enabled successfully' : 'Disabled successfully')
    loadData()
  } catch (error) {
    console.error('Status update failed', error)
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('Are you sure you want to delete this tenant?', 'Confirm', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteTenant(row.id)
      ElMessage.success('Deleted successfully')
      loadData()
    } catch (error) {
      console.error('Delete failed', error)
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
