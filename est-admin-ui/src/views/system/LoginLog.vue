<template>
  <div class="login-log">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Login Logs</span>
          <div class="header-actions">
            <el-button type="primary" @click="loadLogs" :loading="loading">
              <el-icon><Refresh /></el-icon>
              Refresh
            </el-button>
            <el-button type="danger" @click="clearOldLogs">
              <el-icon><Delete /></el-icon>
              Clear Old Logs
            </el-button>
          </div>
        </div>
      </template>
      
      <el-table :data="logs" style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="Username" width="120" />
        <el-table-column prop="ip" label="IP Address" width="140" />
        <el-table-column prop="userAgent" label="Browser" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="Status" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? 'Success' : 'Failed' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="Login Time" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="viewDetail(row)">
              Detail
            </el-button>
            <el-button type="danger" link size="small" @click="deleteLog(row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog v-model="detailDialogVisible" title="Login Details" width="60%">
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="Username">{{ currentLog.username }}</el-descriptions-item>
        <el-descriptions-item label="IP Address">{{ currentLog.ip }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="currentLog.status === 1 ? 'success' : 'danger'">
            {{ currentLog.status === 1 ? 'Success' : 'Failed' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Login Time" :span="2">
          {{ formatDate(currentLog.createdAt) }}
        </el-descriptions-item>
        <el-descriptions-item label="Browser Info" :span="2">
          <pre style="white-space: pre-wrap; word-break: break-all; margin: 0; padding: 8px; background: #f5f7fa; border-radius: 4px;">
{{ currentLog.userAgent }}
          </pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMsg" label="Error Message" :span="2">
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
import { getLoginLogs, deleteLoginLog, clearOldLoginLogs, type LoginLog } from '@/api/log'

const logs = ref<LoginLog[]>([])
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentLog = ref<LoginLog | null>(null)

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await getLoginLogs()
    if (res.data.success) {
      logs.value = res.data.data
    } else {
      ElMessage.error(res.data.message || 'Load failed')
    }
  } catch (error) {
    ElMessage.error('Load failed')
  } finally {
    loading.value = false
  }
}

const viewDetail = (log: LoginLog) => {
  currentLog.value = log
  detailDialogVisible.value = true
}

const deleteLog = async (log: LoginLog) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this log?', 'Confirm', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    const res = await deleteLoginLog(log.id)
    if (res.data.success) {
      ElMessage.success('Deleted successfully')
      loadLogs()
    } else {
      ElMessage.error(res.data.message || 'Delete failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Delete failed')
    }
  }
}

const clearOldLogs = async () => {
  try {
    await ElMessageBox.confirm('Are you sure you want to clear logs older than 30 days?', 'Confirm', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    const res = await clearOldLoginLogs()
    if (res.data.success) {
      ElMessage.success('Cleared successfully')
      loadLogs()
    } else {
      ElMessage.error(res.data.message || 'Clear failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Clear failed')
    }
  }
}

const formatDate = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleString()
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.login-log {
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
