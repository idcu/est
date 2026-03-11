<template>
  <div class="workflow-definition-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Name">
          <el-input v-model="searchForm.name" placeholder="Please enter workflow name" clearable />
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
          <span>Workflow Definition List</span>
          <el-button type="primary" @click="handleAdd">Add Workflow</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Name" width="200" />
        <el-table-column prop="description" label="Description" show-overflow-tooltip />
        <el-table-column prop="version" label="Version" width="80" />
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Create Time" width="180" />
        <el-table-column prop="updateTime" label="Update Time" width="180" />
        <el-table-column label="Actions" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">Edit</el-button>
            <el-button link type="success" size="small" @click="handleView(row)">View</el-button>
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
      width="800px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="formData.name" placeholder="Please enter workflow name" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="Please enter description" />
        </el-form-item>
        <el-form-item label="Workflow Definition" prop="definition">
          <el-input
            v-model="formData.definition"
            type="textarea"
            :rows="10"
            placeholder="Please enter JSON format workflow definition"
          />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">Enabled</el-radio>
            <el-radio :label="0">Disabled</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit">Confirm</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="viewDialogVisible"
      title="View Workflow Definition"
      width="800px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="Name">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="Description">{{ viewData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Version">{{ viewData.version }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="viewData.status === 1 ? 'success' : 'danger'">
            {{ viewData.status === 1 ? 'Enabled' : 'Disabled' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Create Time">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="Update Time">{{ viewData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="Workflow Definition">
          <pre style="max-height: 300px; overflow-y: auto; background: #f5f7fa; padding: 12px; border-radius: 4px;">{{ viewData.definition }}</pre>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">Close</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import {
  listWorkflowDefinitions,
  getWorkflowDefinition,
  createWorkflowDefinition,
  updateWorkflowDefinition,
  deleteWorkflowDefinition,
  type WorkflowDefinition
} from '@/api/workflow'

const searchForm = reactive({
  name: '',
  status: undefined as number | undefined
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref<WorkflowDefinition[]>([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('')
const formRef = ref<FormInstance>()
const viewData = ref<WorkflowDefinition>({} as WorkflowDefinition)

const formData = reactive<Partial<WorkflowDefinition>>({
  name: '',
  description: '',
  definition: '',
  status: 1
})

const formRules: FormRules = {
  name: [{ required: true, message: 'Please enter workflow name', trigger: 'blur' }],
  definition: [{ required: true, message: 'Please enter workflow definition', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await listWorkflowDefinitions({
      ...searchForm,
      page: pagination.page,
      size: pagination.size
    })
    tableData.value = res.data.list || []
    pagination.total = res.data.total || 0
  } catch (error) {
    console.error('Load data failed', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.status = undefined
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = 'Add Workflow'
  Object.assign(formData, {
    name: '',
    description: '',
    definition: '',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: WorkflowDefinition) => {
  isEdit.value = true
  dialogTitle.value = 'Edit Workflow'
  Object.assign(formData, { ...row })
  dialogVisible.value = true
}

const handleView = async (row: WorkflowDefinition) => {
  try {
    const res = await getWorkflowDefinition(row.id)
    viewData.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('Get details failed', error)
  }
}

const handleStatus = async (row: WorkflowDefinition) => {
  try {
    await ElMessageBox.confirm(
      `Are you sure you want to ${row.status === 1 ? 'disable' : 'enable'} this workflow?`,
      'Warning',
      { confirmButtonText: 'Confirm', cancelButtonText: 'Cancel', type: 'warning' }
    )
    await updateWorkflowDefinition({
      id: row.id,
      status: row.status === 1 ? 0 : 1
    })
    ElMessage.success('Operation successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Operation failed', error)
    }
  }
}

const handleDelete = async (row: WorkflowDefinition) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this workflow?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await deleteWorkflowDefinition(row.id)
    ElMessage.success('Deleted successfully')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete failed', error)
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value?.validate()
    if (isEdit.value) {
      await updateWorkflowDefinition(formData)
      ElMessage.success('Updated successfully')
    } else {
      await createWorkflowDefinition(formData)
      ElMessage.success('Created successfully')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Submit failed', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.workflow-definition-page {
  padding: 20px;

  .search-card {
    margin-bottom: 20px;
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
