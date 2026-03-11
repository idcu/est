<template>
  <div class="role-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Role Name">
          <el-input v-model="searchForm.name" placeholder="Please enter role name" clearable />
        </el-form-item>
        <el-form-item label="Role Code">
          <el-input v-model="searchForm.code" placeholder="Please enter role code" clearable />
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
          <span>Role List</span>
          <el-button type="primary" @click="handleAdd">Add Role</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Role Name" width="150" />
        <el-table-column prop="code" label="Role Code" width="150" />
        <el-table-column prop="sort" label="Sort" width="100" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="Remark" />
        <el-table-column prop="createTime" label="Create Time" width="180" />
        <el-table-column label="Actions" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="info" size="small" @click="handlePermission(row)">Permission</el-button>
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
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="Role Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter role name" />
        </el-form-item>
        <el-form-item label="Role Code" prop="code">
          <el-input v-model="formData.code" placeholder="Please enter role code" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="Sort" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
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

    <el-dialog
      v-model="permissionDialogVisible"
      title="Assign Permission"
      width="500px"
      destroy-on-close
    >
      <el-tree
        ref="treeRef"
        :data="menuTreeData"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedMenuIds"
        :props="treeProps"
      />
      <template #footer>
        <el-button @click="permissionDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handlePermissionSubmit">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { listRoles, getRole, createRole, updateRole, deleteRole, updateRoleStatus } from '@/api/role'
import { listMenus } from '@/api/menu'

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
const menuTreeData = ref<any[]>([])
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const treeRef = ref<any>()
const currentRoleId = ref<number>()
const checkedMenuIds = ref<number[]>([])

const treeProps = {
  children: 'children',
  label: 'name'
}

const formData = reactive({
  id: undefined as number | undefined,
  name: '',
  code: '',
  sort: 0,
  status: 1,
  remark: ''
})

const formRules: FormRules = {
  name: [
    { required: true, message: 'Please enter role name', trigger: 'blur' }
  ],
  code: [
    { required: true, message: 'Please enter role code', trigger: 'blur' }
  ]
}

const loadData = async () => {
  try {
    const res = await listRoles({
      page: pagination.page,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('Failed to load role list', error)
  }
}

const loadMenuTree = async () => {
  try {
    const res = await listMenus()
    menuTreeData.value = res.data || []
  } catch (error) {
    console.error('Failed to load menu tree', error)
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
  dialogTitle.value = 'Add Role'
  isEdit.value = false
  Object.assign(formData, {
    id: undefined,
    name: '',
    code: '',
    sort: 0,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = async (row: any) => {
  dialogTitle.value = 'Edit Role'
  isEdit.value = true
  try {
    const res = await getRole(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('Failed to get role info', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateRole(formData)
          ElMessage.success('Updated successfully')
        } else {
          await createRole(formData)
          ElMessage.success('Created successfully')
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Failed to submit', error)
      }
    }
  })
}

const handlePermission = async (row: any) => {
  currentRoleId.value = row.id
  checkedMenuIds.value = row.menuIds || []
  await loadMenuTree()
  permissionDialogVisible.value = true
}

const handlePermissionSubmit = () => {
  ElMessage.success('Permission assigned successfully')
  permissionDialogVisible.value = false
}

const handleStatus = async (row: any) => {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await updateRoleStatus(row.id, newStatus)
    ElMessage.success(newStatus === 1 ? 'Enabled successfully' : 'Disabled successfully')
    loadData()
  } catch (error) {
    console.error('Failed to update status', error)
  }
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm('Are you sure you want to delete this role?', 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('Deleted successfully')
      loadData()
    } catch (error) {
      console.error('Failed to delete', error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.role-page {
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
