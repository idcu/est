<template>
  <div class="workflow-instance-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="Workflow Definition">
          <el-select v-model="searchForm.definitionId" placeholder="Please select workflow definition" clearable filterable>
            <el-option
              v-for="item in definitionList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="Please select status" clearable>
            <el-option label="Running" value="RUNNING" />
            <el-option label="Paused" value="PAUSED" />
            <el-option label="Completed" value="COMPLETED" />
            <el-option label="Cancelled" value="CANCELLED" />
            <el-option label="Error" value="ERROR" />
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
          <span>Workflow Instance List</span>
          <el-button type="primary" @click="handleStart">Start Workflow</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="definitionName" label="Workflow Name" width="200" />
        <el-table-column prop="businessKey" label="Business Key" width="150" />
        <el-table-column prop="currentNode" label="Current Node" width="150" />
        <el-table-column prop="status" label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="Start Time" width="180" />
        <el-table-column prop="endTime" label="End Time" width="180" />
        <el-table-column label="Actions" width="320" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">View</el-button>
            <el-button
              v-if="row.status === 'RUNNING'"
              link
              type="warning"
              size="small"
              @click="handlePause(row)"
            >
              Pause
            </el-button>
            <el-button
              v-if="row.status === 'PAUSED'"
              link
              type="success"
              size="small"
              @click="handleResume(row)"
            >
              Resume
            </el-button>
            <el-button
              v-if="row.status === 'ERROR'"
              link
              type="info"
              size="small"
              @click="handleRetry(row)"
            >
              Retry
            </el-button>
            <el-button
              v-if="['RUNNING', 'PAUSED'].includes(row.status)"
              link
              type="danger"
              size="small"
              @click="handleCancel(row)"
            >
              Cancel
            </el-button>
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
      v-model="startDialogVisible"
      title="Start Workflow"
      width="600px"
    >
      <el-form ref="startFormRef" :model="startFormData" :rules="startFormRules" label-width="120px">
        <el-form-item label="Workflow Definition" prop="definitionId">
          <el-select v-model="startFormData.definitionId" placeholder="Please select workflow definition" filterable style="width: 100%">
            <el-option
              v-for="item in definitionList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Business Key" prop="businessKey">
          <el-input v-model="startFormData.businessKey" placeholder="Please enter business key" />
        </el-form-item>
        <el-form-item label="Variables" prop="variables">
          <el-input
            v-model="startFormData.variables"
            type="textarea"
            :rows="5"
            placeholder="Please enter JSON format variables"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="startDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleStartSubmit">Confirm</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="viewDialogVisible"
      title="View Workflow Instance"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="Workflow Definition ID">{{ viewData.definitionId }}</el-descriptions-item>
        <el-descriptions-item label="Workflow Name">{{ viewData.definitionName }}</el-descriptions-item>
        <el-descriptions-item label="Business Key">{{ viewData.businessKey || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Current Node">{{ viewData.currentNode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="getStatusType(viewData.status)">
            {{ getStatusText(viewData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Create Time">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="Update Time">{{ viewData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="Start Time">{{ viewData.startTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="End Time">{{ viewData.endTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">Variables</el-divider>
      <pre style="max-height: 200px; overflow-y: auto; background: #f5f7fa; padding: 12px; border-radius: 4px;">{{ JSON.stringify(viewData.variables, null, 2) }}</pre>
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
  listWorkflowInstances,
  getWorkflowInstance,
  startWorkflowInstance,
  pauseWorkflowInstance,
  resumeWorkflowInstance,
  cancelWorkflowInstance,
  retryWorkflowInstance,
  type WorkflowDefinition,
  type WorkflowInstance
} from '@/api/workflow'

const searchForm = reactive({
  definitionId: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref<WorkflowInstance[]>([])
const definitionList = ref<WorkflowDefinition[]>([])
const startDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const startFormRef = ref<FormInstance>()
const viewData = ref<WorkflowInstance>({} as WorkflowInstance)

const startFormData = reactive({
  definitionId: '',
  businessKey: '',
  variables: '{}'
})

const startFormRules: FormRules = {
  definitionId: [{ required: true, message: 'Please select workflow definition', trigger: 'change' }]
}

const getStatusType = (status: string) => {
  const typeMap: Record<string, any> = {
    RUNNING: 'primary',
    PAUSED: 'warning',
    COMPLETED: 'success',
    CANCELLED: 'info',
    ERROR: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    RUNNING: 'Running',
    PAUSED: 'Paused',
    COMPLETED: 'Completed',
    CANCELLED: 'Cancelled',
    ERROR: 'Error'
  }
  return textMap[status] || status
}

const loadDefinitions = async () => {
  try {
    const res = await listWorkflowDefinitions({ page: 1, size: 1000 })
    definitionList.value = res.data.list || []
  } catch (error) {
    console.error('Load workflow definitions failed', error)
  }
}

const loadData = async () => {
  try {
    const res = await listWorkflowInstances({
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
  searchForm.definitionId = ''
  searchForm.status = ''
  handleSearch()
}

const handleStart = () => {
  Object.assign(startFormData, {
    definitionId: '',
    businessKey: '',
    variables: '{}'
  })
  startDialogVisible.value = true
}

const handleStartSubmit = async () => {
  try {
    await startFormRef.value?.validate()
    let variables: Record<string, any> = {}
    try {
      variables = JSON.parse(startFormData.variables)
    } catch {
      ElMessage.error('Variables JSON format error')
      return
    }
    await startWorkflowInstance({
      definitionId: startFormData.definitionId,
      businessKey: startFormData.businessKey || undefined,
      variables
    })
    ElMessage.success('Start successful')
    startDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Start failed', error)
  }
}

const handleView = async (row: WorkflowInstance) => {
  try {
    const res = await getWorkflowInstance(row.id)
    viewData.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('Get details failed', error)
  }
}

const handlePause = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to pause this workflow instance?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await pauseWorkflowInstance(row.id)
    ElMessage.success('Pause successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Pause failed', error)
    }
  }
}

const handleResume = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to resume this workflow instance?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await resumeWorkflowInstance(row.id)
    ElMessage.success('Resume successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Resume failed', error)
    }
  }
}

const handleRetry = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to retry this workflow instance?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await retryWorkflowInstance(row.id)
    ElMessage.success('Retry successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Retry failed', error)
    }
  }
}

const handleCancel = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to cancel this workflow instance?', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await cancelWorkflowInstance(row.id)
    ElMessage.success('Cancel successful')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Cancel failed', error)
    }
  }
}

onMounted(() => {
  loadDefinitions()
  loadData()
})
</script>

<style scoped lang="scss">
.workflow-instance-page {
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
