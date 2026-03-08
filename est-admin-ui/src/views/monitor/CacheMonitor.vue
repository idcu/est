<template>
  <div class="cache-monitor">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>缓存监控</span>
          <div class="header-actions">
            <el-button type="primary" @click="loadData" :loading="loading">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
            <el-button type="danger" @click="clearAllCachesConfirm">
              <el-icon><Delete /></el-icon>
              清空所有缓�?            </el-button>
          </div>
        </div>
      </template>
      
      <div v-if="cacheData" class="monitor-content">
        <el-row :gutter="20" class="stats-row">
          <el-col :span="6">
            <el-statistic title="总缓存键�? :value="cacheData.totalSize" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="总命中次�? :value="cacheData.totalHitCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="总未命中次数" :value="cacheData.totalMissCount" />
          </el-col>
          <el-col :span="6">
            <el-statistic title="总命中率">
              <template #default>
                {{ (cacheData.totalHitRate * 100).toFixed(2) }}%
              </template>
            </el-statistic>
          </el-col>
        </el-row>
        
        <el-divider />
        
        <h4>缓存详情</h4>
        <el-table :data="cacheData.caches" style="width: 100%">
          <el-table-column prop="name" label="缓存名称" width="150" />
          <el-table-column prop="size" label="键数�? width="100" />
          <el-table-column prop="hitCount" label="命中次数" width="120" />
          <el-table-column prop="missCount" label="未命中次�? width="120" />
          <el-table-column label="命中�? width="120">
            <template #default="{ row }">
              <el-progress 
                :percentage="Math.round(row.hitRate * 100)" 
                :status="row.hitRate > 0.8 ? 'success' : row.hitRate > 0.5 ? 'warning' : 'danger'"
                :stroke-width="10"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-button type="danger" link size="small" @click="clearCacheByName(row.name)">
                清空
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
      ElMessage.error(res.data.message || '加载失败')
    }
  } catch (error) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const clearCacheByName = async (cacheName: string) => {
  try {
    await ElMessageBox.confirm(`确定要清空缓�?${cacheName} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await clearCache(cacheName)
    if (res.data.success) {
      ElMessage.success('缓存清空成功')
      loadData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const clearAllCachesConfirm = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有缓存吗？此操作不可恢复�?, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const res = await clearAllCachesApi()
    if (res.data.success) {
      ElMessage.success('所有缓存清空成�?)
      loadData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
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
