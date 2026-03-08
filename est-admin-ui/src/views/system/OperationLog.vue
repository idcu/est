<template>
  <div class="operation-log">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>操作日志</span>
          <div class="header-actions">
            <el-button type="primary" @click="loadLogs" :loading="loading">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
            <el-button type="danger" @click="clearOldLogs">
              <el-icon><Delete /></el-icon>
              清空旧日�?            </el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="logs" style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="用户�? width="120" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="operation" label="操作" width="150" />
        <el-table-column prop="method" label="方法" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="time" label="耗时(ms)" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.time && row.time > 1000 ? '#f56c6c' : '' }">
              {{ row.time }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状�? width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">
              详情
            </el-button>
            <el-button type="danger" link size="small" @click="deleteLog(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog v-model="detailDialogVisible" title="日志详情" width="70%">
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="用户�?>{{ currentLog.username }}</el-descriptions-item>
        <el-descriptions-item label="模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作">{{ currentLog.operation }}</el-descriptions-item>
        <el-descriptions-item label="方法">{{ currentLog.method }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog.time }}ms</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ip }}</el-descriptions-item>
        <el-descriptions-item label="浏览�?>
          <el-tooltip :content="currentLog.userAgent" placement="top">
            <span>{{ currentLog.userAgent?.substring(0, 50) }}...</span>
          </el-tooltip>
        </el-descriptions-item>
        <el-descriptions-item label="状�?>
          <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'">
            {{ currentLog.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">
          {{ formatDate(currentLog.createdAt) }}
        </el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre style="white-space: pre-wrap; word-break: break-all; margin: 0; padding: 8px; background: #f5f7fa; border-radius: 4px;">
{{ currentLog.params }}
          </pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMsg" label="错误信息" :span="2">
          <pre style="white-space: pre-wrap; word-break: break-all; margin: 0; padding: 8px; background: #fef0f0; border-radius: 4px; color: #f56c6c;">
{{ currentLog.errorMsg }}
          </pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Delete } from '@element-plus/icons-vue'
import { getOperationLogs, deleteOperationLog, clearOldOperationLogs, type OperationLog } from '@/api/log'

const logs = ref<OperationLog[]>([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentLog = ref<OperationLog | null>(null)

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await getOperationLogs()
    if (res.data.success) {
      logs.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '加载失败')
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const viewDetail = (log: OperationLog) => {
  currentLog.value = log
  detailDialogVisible.value = true
}

const deleteLog = async (log: OperationLog) => {
  try {
    await ElMessageBox.confirm('确定要删除这条日志吗�?, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await deleteOperationLog(log.id)
    if (res.data.success) {
      ElMessage.success('删除成功')
      loadLogs()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const clearOldLogs = async () => {
  try {
    await ElMessageBox.confirm('确定要清�?0天前的旧日志吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await clearOldOperationLogs()
    if (res.data.success) {
      ElMessage.success('清空成功')
      loadLogs()
    } else {
      ElMessage.error(res.data.message || '清空失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空失败')
    }
  }
}

const formatDate = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.operation-log {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 10px;
}
</style>
