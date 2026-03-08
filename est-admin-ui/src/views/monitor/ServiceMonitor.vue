<template>
  <div class="service-monitor">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>服务监控</span>
          <el-button type="primary" @click="loadData" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="JVM 监控" name="jvm">
          <div v-if="jvmData" class="monitor-content">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-statistic title="JVM 信息" :value="jvmData.jvmInfo" />
              </el-col>
              <el-col :span="6">
                <el-statistic title="运行时间">
                  <template #default>
                    {{ formatUptime(jvmData.uptime) }}
                  </template>
                </el-statistic>
              </el-col>
              <el-col :span="6">
                <el-statistic title="线程�? :value="jvmData.metrics['jvm.thread.count']" />
              </el-col>
              <el-col :span="6">
                <el-statistic title="GC 次数" :value="jvmData.metrics['jvm.gc.count']" />
              </el-col>
            </el-row>
            
            <el-divider />
            
            <h4>内存使用情况</h4>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-card>
                  <template #header>堆内�?/template>
                  <el-progress 
                    :percentage="Math.round(jvmData.metrics['jvm.heap.usage'] * 100)" 
                    :status="jvmData.metrics['jvm.heap.usage'] > 0.8 ? 'warning' : 'success'"
                  />
                  <div class="memory-details">
                    <span>已用: {{ formatBytes(jvmData.metrics['jvm.heap.used']) }}</span>
                    <span>最�? {{ formatBytes(jvmData.metrics['jvm.heap.max']) }}</span>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <template #header>非堆内存</template>
                  <div class="memory-details">
                    <span>已用: {{ formatBytes(jvmData.metrics['jvm.nonheap.used']) }}</span>
                    <span>最�? {{ formatBytes(jvmData.metrics['jvm.nonheap.max']) }}</span>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            
            <el-divider />
            
            <h4>详细指标</h4>
            <el-table :data="getMetricList(jvmData.metrics)" style="width: 100%">
              <el-table-column prop="name" label="指标名称" width="250" />
              <el-table-column prop="value" label="�? />
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="系统监控" name="system">
          <div v-if="systemData" class="monitor-content">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-statistic title="操作系统" :value="systemData.osInfo" />
              </el-col>
            </el-row>
            
            <el-divider />
            
            <h4>详细指标</h4>
            <el-table :data="getMetricList(systemData.metrics)" style="width: 100%">
              <el-table-column prop="name" label="指标名称" width="250" />
              <el-table-column prop="value" label="�? />
            </el-table>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="健康检�? name="health">
          <div v-if="healthData" class="monitor-content">
            <h4>JVM 健康检�?/h4>
            <el-table :data="healthData.jvm" style="width: 100%">
              <el-table-column prop="name" label="检查项" width="200" />
              <el-table-column prop="status" label="状�? width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="message" label="消息" />
            </el-table>
            
            <el-divider />
            
            <h4>系统健康检�?/h4>
            <el-table :data="healthData.system" style="width: 100%">
              <el-table-column prop="name" label="检查项" width="200" />
              <el-table-column prop="status" label="状�? width="100">
                <template #default="{ row }">
                  <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="message" label="消息" />
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'
import { getJvmMetrics, getSystemMetrics, getHealthChecks, type JvmMetrics, type SystemMetrics, type HealthChecks } from '@/api/monitor'

const activeTab = ref('jvm')
const loading = ref(false)
const jvmData = ref<JvmMetrics | null>(null)
const systemData = ref<SystemMetrics | null>(null)
const healthData = ref<HealthChecks | null>(null)

const loadData = async () => {
  loading.value = true
  try {
    const [jvmRes, systemRes, healthRes] = await Promise.all([
      getJvmMetrics(),
      getSystemMetrics(),
      getHealthChecks()
    ])
    
    if (jvmRes.data.success) {
      jvmData.value = jvmRes.data.data
    }
    if (systemRes.data.success) {
      systemData.value = systemRes.data.data
    }
    if (healthRes.data.success) {
      healthData.value = healthRes.data.data
    }
  } catch (error) {
    ElMessage.error('加载监控数据失败')
  } finally {
    loading.value = false
  }
}

const getMetricList = (metrics: Record<string, any>) => {
  return Object.entries(metrics).map(([name, value]) => ({
    name,
    value: String(value)
  }))
}

const formatUptime = (ms: number) => {
  const seconds = Math.floor(ms / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  return `${days}�?${hours % 24}小时 ${minutes % 60}分钟`
}

const formatBytes = (bytes: number) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

const getStatusType = (status: string) => {
  switch (status?.toLowerCase()) {
    case 'healthy':
      return 'success'
    case 'degraded':
      return 'warning'
    case 'unhealthy':
      return 'danger'
    default:
      return 'info'
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.service-monitor {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.monitor-content {
  padding: 10px 0;
}

.memory-details {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
}

h4 {
  margin: 20px 0 10px 0;
  color: #303133;
}
</style>
