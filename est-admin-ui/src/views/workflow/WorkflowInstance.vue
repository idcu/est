<template>
  <div class="workflow-instance-page">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="工作流定义">
          <el-select v-model="searchForm.definitionId" placeholder="请选择工作流定义" clearable filterable>
            <el-option
              v-for="item in definitionList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="运行中" value="RUNNING" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
            <el-option label="异常" value="ERROR" />
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
          <span>工作流实例列表</span>
          <el-button type="primary" @click="handleStart">启动工作流</el-button>
        </div>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="definitionName" label="工作流名称" width="200" />
        <el-table-column prop="businessKey" label="业务键" width="150" />
        <el-table-column prop="currentNode" label="当前节点" width="150" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" />
        <el-table-column prop="endTime" label="结束时间" width="180" />
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.status === 'RUNNING'"
              link
              type="warning"
              size="small"
              @click="handlePause(row)"
            >
              暂停
            </el-button>
            <el-button
              v-if="row.status === 'PAUSED'"
              link
              type="success"
              size="small"
              @click="handleResume(row)"
            >
              恢复
            </el-button>
            <el-button
              v-if="row.status === 'ERROR'"
              link
              type="info"
              size="small"
              @click="handleRetry(row)"
            >
              重试
            </el-button>
            <el-button
              v-if="['RUNNING', 'PAUSED'].includes(row.status)"
              link
              type="danger"
              size="small"
              @click="handleCancel(row)"
            >
              取消
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
      title="启动工作流"
      width="600px"
    >
      <el-form ref="startFormRef" :model="startFormData" :rules="startFormRules" label-width="120px">
        <el-form-item label="工作流定义" prop="definitionId">
          <el-select v-model="startFormData.definitionId" placeholder="请选择工作流定义" filterable style="width: 100%">
            <el-option
              v-for="item in definitionList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="业务键" prop="businessKey">
          <el-input v-model="startFormData.businessKey" placeholder="请输入业务键" />
        </el-form-item>
        <el-form-item label="变量" prop="variables">
          <el-input
            v-model="startFormData.variables"
            type="textarea"
            :rows="5"
            placeholder="请输入JSON格式的变量"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="startDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleStartSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="viewDialogVisible"
      title="查看工作流实例"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID">{{ viewData.id }}</el-descriptions-item>
        <el-descriptions-item label="工作流定义ID">{{ viewData.definitionId }}</el-descriptions-item>
        <el-descriptions-item label="工作流名称">{{ viewData.definitionName }}</el-descriptions-item>
        <el-descriptions-item label="业务键">{{ viewData.businessKey || '-' }}</el-descriptions-item>
        <el-descriptions-item label="当前节点">{{ viewData.currentNode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewData.status)">
            {{ getStatusText(viewData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ viewData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ viewData.startTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ viewData.endTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">变量</el-divider>
      <pre style="max-height: 200px; overflow-y: auto; background: #f5f7fa; padding: 12px; border-radius: 4px;">{{ JSON.stringify(viewData.variables, null, 2) }}</pre>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
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
  definitionId: [{ required: true, message: '请选择工作流定义', trigger: 'change' }]
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
    RUNNING: '运行中',
    PAUSED: '已暂停',
    COMPLETED: '已完成',
    CANCELLED: '已取消',
    ERROR: '异常'
  }
  return textMap[status] || status
}

const loadDefinitions = async () => {
  try {
    const res = await listWorkflowDefinitions({ page: 1, size: 1000 })
    definitionList.value = res.data.list || []
  } catch (error) {
    console.error('加载工作流定义失败', error)
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
    console.error('加载数据失败', error)
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
      ElMessage.error('变量JSON格式错误')
      return
    }
    await startWorkflowInstance({
      definitionId: startFormData.definitionId,
      businessKey: startFormData.businessKey || undefined,
      variables
    })
    ElMessage.success('启动成功')
    startDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('启动失败', error)
  }
}

const handleView = async (row: WorkflowInstance) => {
  try {
    const res = await getWorkflowInstance(row.id)
    viewData.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取详情失败', error)
  }
}

const handlePause = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('确定要暂停该工作流实例吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await pauseWorkflowInstance(row.id)
    ElMessage.success('暂停成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('暂停失败', error)
    }
  }
}

const handleResume = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('确定要恢复该工作流实例吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await resumeWorkflowInstance(row.id)
    ElMessage.success('恢复成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('恢复失败', error)
    }
  }
}

const handleRetry = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('确定要重试该工作流实例吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await retryWorkflowInstance(row.id)
    ElMessage.success('重试成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重试失败', error)
    }
  }
}

const handleCancel = async (row: WorkflowInstance) => {
  try {
    await ElMessageBox.confirm('确定要取消该工作流实例吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await cancelWorkflowInstance(row.id)
    ElMessage.success('取消成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败', error)
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
