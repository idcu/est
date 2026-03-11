<template>
  <div class="department-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Dept Name">
          <el-input v-model="searchForm.name" placeholder="Please enter department name" clearable />
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
          <span>Department List</span>
          <el-button type="primary" @click="handleAdd">Add Department</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe row-key="id" default-expand-all style="width: 100%">
        <el-table-column prop="name" label="Dept Name" width="200" />
        <el-table-column prop="code" label="Dept Code" width="150" />
        <el-table-column prop="sort" label="Sort" width="100" />
        <el-table-column prop="leader" label="Leader" width="120" />
        <el-table-column prop="phone" label="Phone" width="130" />
        <el-table-column prop="email" label="Email" width="200" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Created At" width="180" />
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleAdd(row)">Add</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="Parent Dept" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="deptTreeData"
            :props="treeProps"
            check-strictly
            placeholder="Select parent department"
            clearable
          />
        </el-form-item>
        <el-form-item label="Dept Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter department name" />
        </el-form-item>
        <el-form-item label="Dept Code" prop="code">
          <el-input v-model="formData.code" placeholder="Please enter department code" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="Sort" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="Leader" prop="leader">
          <el-input v-model="formData.leader" placeholder="Please enter leader" />
        </el-form-item>
        <el-form-item label="Phone" prop="phone">
          <el-input v-model="formData.phone" placeholder="Please enter phone" />
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input v-model="formData.email" placeholder="Please enter email" />
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
import { listDepartments, getDepartment, createDepartment, updateDepartment, deleteDepartment } from '@/api/department'

const searchForm = reactive({
  name: '',
  status: undefined as number | undefined
})

const tableData = ref<any[]>([])
const deptTreeData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const treeProps = {
  children: 'children',
  label: 'name',
  value: 'id'
}

const formData = reactive({
  id: undefined as number | undefined,
  parentId: 0,
  name: '',
  code: '',
  sort: 0,
  leader: '',
  phone: '',
  email: '',
  status: 1,
  remark: ''
})

const formRules: FormRules = {
  name: [
    { required: true, message: 'Please enter department name', trigger: 'blur' }
  ],
  code: [
    { required: true, message: 'Please enter department code', trigger: 'blur' }
  ]
}

const loadData = async () => {
  try {
    const res = await listDepartments(searchForm)
    tableData.value = res.data || []
    deptTreeData.value = [
      { id: 0, name: 'Root', children: res.data || [] }
    ]
  } catch (error) {
    console.error('Failed to load department list', error)
  }
}

const handleSearch = () => {
  loadData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = (row?: any) => {
  dialogTitle.value = 'Add Department'
  isEdit.value = false
  Object.assign(formData, {
    id: undefined,
    parentId: row ? row.id : 0,
    name: '',
    code: '',
    sort: 0,
    leader: '',
    phone: '',
    email: '',
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

const handleEdit = async (row: any) => {
  dialogTitle.value = 'Edit Department'
  isEdit.value = true
  try {
    const res = await getDepartment(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('Failed to get department info', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (isEdit.value) {
          await updateDepartment(formData)
          ElMessage.success('Updated successfully')
        } else {
          await createDepartment(formData)
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

const handleDelete = (row: any) => {
  ElMessageBox.confirm('Are you sure you want to delete this department?', 'Confirm', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteDepartment(row.id)
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
.department-page {
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
