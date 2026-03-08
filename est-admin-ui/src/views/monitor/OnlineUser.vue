<template>
  <div class="online-user">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>在线用户</span>
          <el-button type="primary" @click="loadUsers" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <div class="stats-bar">
        <el-statistic title="在线用户数" :value="userCount" />
      </div>
      
      <el-table :data="users" style="width: 100%" v-loading="loading">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="ip" label="IP地址" width="140" />
        <el-table-column prop="browser" label="浏览器" min-width="200" show-overflow-tooltip />
        <el-table-column prop="loginTime" label="登录时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastActivityTime" label="最后活动" width="180">
          <template #default="{ row }">
            {{ formatDate(row.lastActivityTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="forceLogoutUser(row)">
              强制下线
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
      ElMessage.error(res.data.message || '加载失败')
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const forceLogoutUser = async (user: OnlineUser) => {
  try {
    await ElMessageBox.confirm(`确定要强制下线用户 ${user.username} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await forceLogout(user.sessionId)
    if (res.data.success) {
      ElMessage.success('强制下线成功')
      loadUsers()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const formatDate = (timestamp: number) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
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
