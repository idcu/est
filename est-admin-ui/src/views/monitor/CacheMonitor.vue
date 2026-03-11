<template>
  <div class="cache-monitor">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>Cache Monitor</span>
          <div class="header-actions">
            <el-button type="primary" @click="loadData" :loading="loading">
              <el-icon><Refresh /></el-icon>
              Refresh
            </el-button>
            <el-button type="danger" @click="clearAllCachesConfirm">
              <el-icon><Delete /></el-icon>
              Clear All Caches
            </el-button>
          </div>
        </div>
      </template>
      
      <div v-if="cacheData" class="monitor-content">
        <el-row :gutter="20" class="stats-row">
          <el-col :span="6">
            <el-statistic title="Total Cache Keys" :value="cacheData.totalSize" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="Total Hits" :value="cacheData.totalHitCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="Total Misses" :value="cacheData.totalMissCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="Total Hit Rate">
              <template #default>
                {{ (cacheData.totalHitRate * 100).toFixed(2) }}%
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider />
        
        <h4>Cache Details</h4>
        <el-table :data="cacheData.caches" style="width: 100%">
          <el-table-column prop="name" label="Cache Name" width="150" />
          <el-table-column prop="size" label="Keys" width="100" />
          <el-table-column prop="hitCount" label="Hits" width="120" />
          <el-table-column prop="missCount" label="Misses" width="120" />
          <el-table-column label="Hit Rate" width="120">
            <template #default="{ row }">
              <el-progress 
                :percentage="Math.round(row.hitRate * 100)" 
                :status="row.hitRate > 0.8 ? 'success' : row.hitRate > 0.5 ? 'warning' : 'danger'"
                :stroke-width="10"
              />
            </template>
          </el-table-column>
          <el-table-column label="Actions" width="120" fixed="right">
            <template #default="{ row }">
              <el-button type="danger" link size="small" @click="clearCacheByName(row.name)">
                Clear
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Delete } from '@element-plus/icons-vue'
import { getCacheStatistics, clearCache, clearAllCaches as clearAllCachesApi, type CacheStatistics } from '@/api/monitor'

const loading = ref(false)
const cacheData = ref<CacheStatistics | null>(null)

const loadData = async () => {
  loading.value = true
  try {
    const res = await getCacheStatistics()
    if (res.data.success) {
      cacheData.value = res.data.data
    } else {
      ElMessage.error(res.data.message || 'Load failed')
    }
  } catch (error) {
    ElMessage.error('Load failed')
  } finally {
    loading.value = false
  }
}

const clearCacheByName = async (cacheName: string) => {
  try {
    await ElMessageBox.confirm(`Are you sure you want to clear cache ${cacheName}?`, 'Confirm', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    const res = await clearCache(cacheName)
    if (res.data.success) {
      ElMessage.success('Cache cleared successfully')
      loadData()
    } else {
      ElMessage.error(res.data.message || 'Operation failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Operation failed')
    }
  }
}

const clearAllCachesConfirm = async () => {
  try {
    await ElMessageBox.confirm('Are you sure you want to clear all caches? This action cannot be undone.', 'Warning', {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    const res = await clearAllCachesApi()
    if (res.data.success) {
      ElMessage.success('All caches cleared successfully')
      loadData()
    } else {
      ElMessage.error(res.data.message || 'Operation failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Operation failed')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.cache-monitor {
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

.monitor-content {
  padding: 10px 0;
}

.stats-row {
  margin-bottom: 20px;
}

h4 {
  margin: 20px 0 10px 0;
  color: #303133;
}
</style>
