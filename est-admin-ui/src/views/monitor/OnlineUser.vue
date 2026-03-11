<template>
  <div class="online-user">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Online Users</span>
          <el-button type="primary" @click="loadUsers" :loading="loading">
            <el-icon><Refresh /></el-icon>
            Refresh
          </el-button>
        </div>
      </template>
      
      <div class="stats-bar">
        <el-statistic title="Online User Count" :value="userCount" />
      </div>
      
      <el-table :data="users" style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="Username" width="120" />
        <el-table-column prop="ip" label="IP Address" width="140" />
        <el-table-column prop="browser" label="Browser" min-width="200" show-overflow-tooltip />
        <el-table-column prop="loginTime" label="Login Time" width="180">
          <template #default="{ row }">
            {{ formatDate(row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastActivityTime" label="Last Activity" width="180">
          <template #default="{ row }">
            {{ formatDate(row.lastActivityTime) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="forceLogoutUser(row)">
              Force Logout
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { getOnlineUsers, forceLogout, type OnlineUser } from '@/api/monitor'

const users = ref<OnlineUser[]>([])
const userCount = ref(0)
const loading = ref(false)

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getOnlineUsers()
    if (res.data.success) {
      users.value = res.data.data.data
      userCount.value = res.data.data.count
    } else {
      ElMessage.error(res.data.message || 'Load failed')
    }
  } catch (error) {
    ElMessage.error('Load failed')
  } finally {
    loading.value = false
  }
}

const forceLogoutUser = async (user: OnlineUser) => {
  try {
    await ElMessageBox.confirm(`Are you sure you want to force logout user ${user.username}?`, 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    const res = await forceLogout(user.sessionId)
    if (res.data.success) {
      ElMessage.success('Force logout successful')
      loadUsers()
    } else {
      ElMessage.error(res.data.message || 'Operation failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Operation failed')
    }
  }
}

const formatDate = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleString()
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.online-user {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-bar {
  margin-bottom: 20px;
}
</style>
