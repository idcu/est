<template>
  <div class="report-dashboard-page">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="Time Range">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="to"
            start-placeholder="Start Date"
            end-placeholder="End Date"
            value-format="YYYY-MM-DD"
            style="width: 350px;"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRefresh">Refresh</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6" v-for="(stat, index) in statsData" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="30">
                <component :is="stat.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-label">{{ stat.label }}</div>
              <div class="stat-trend" :class="stat.trend > 0 ? 'up' : 'down'">
                <el-icon v-if="stat.trend > 0"><ArrowUp /></el-icon>
                <el-icon v-else><ArrowDown /></el-icon>
                {{ Math.abs(stat.trend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>User Growth Trend</span>
            </div>
          </template>
          <div ref="userChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>Role Distribution</span>
            </div>
          </template>
          <div ref="roleChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>Department Personnel Statistics</span>
            </div>
          </template>
          <div ref="deptChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>Operation Log Trend</span>
            </div>
          </template>
          <div ref="logChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :xs="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>Login Log Statistics</span>
            </div>
          </template>
          <div ref="loginChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'
import {
  User,
  ArrowUp,
  ArrowDown,
  TrendCharts,
  UserFilled,
  Operation,
  Document
} from '@element-plus/icons-vue'

const filterForm = reactive({})
const dateRange = ref<string[]>([])

const userChartRef = ref<HTMLElement>()
const roleChartRef = ref<HTMLElement>()
const deptChartRef = ref<HTMLElement>()
const logChartRef = ref<HTMLElement>()
const loginChartRef = ref<HTMLElement>()

let userChart: echarts.ECharts | null = null
let roleChart: echarts.ECharts | null = null
let deptChart: echarts.ECharts | null = null
let logChart: echarts.ECharts | null = null
let loginChart: echarts.ECharts | null = null

const statsData = ref([
  {
    label: 'Total Users',
    value: '1,256',
    icon: User,
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    trend: 12.5
  },
  {
    label: 'Active Users',
    value: '892',
    icon: UserFilled,
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    trend: 8.2
  },
  {
    label: 'Today Operations',
    value: '3,456',
    icon: Operation,
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    trend: -3.1
  },
  {
    label: 'Workflow Instances',
    value: '156',
    icon: Document,
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
    trend: 15.8
  }
])

const initUserChart = () => {
  if (!userChartRef.value) return
  
  userChart = echarts.init(userChartRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['New Users', 'Active Users']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'New Users',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230, 210],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(102, 126, 234, 0.5)' },
            { offset: 1, color: 'rgba(102, 126, 234, 0.1)' }
          ])
        }
      },
      {
        name: 'Active Users',
        type: 'line',
        smooth: true,
        data: [220, 182, 191, 234, 290, 330, 310],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(240, 147, 251, 0.5)' },
            { offset: 1, color: 'rgba(240, 147, 251, 0.1)' }
          ])
        }
      }
    ]
  }
  
  userChart.setOption(option)
}

const initRoleChart = () => {
  if (!roleChartRef.value) return
  
  roleChart = echarts.init(roleChartRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Role Distribution',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 1048, name: 'Super Admin' },
          { value: 735, name: 'System Admin' },
          { value: 580, name: 'Regular User' },
          { value: 484, name: 'Guest' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  roleChart.setOption(option)
}

const initDeptChart = () => {
  if (!deptChartRef.value) return
  
  deptChart = echarts.init(deptChartRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ['Tech', 'Product', 'Operations', 'Marketing', 'Finance', 'HR']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'Count',
        type: 'bar',
        data: [120, 80, 60, 50, 30, 20],
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#667eea' },
            { offset: 1, color: '#764ba2' }
          ])
        }
      }
    ]
  }
  
  deptChart.setOption(option)
}

const initLogChart = () => {
  if (!logChartRef.value) return
  
  logChart = echarts.init(logChartRef.value)
  
  const option: EChartsOption = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['Operation Count']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'Operation Count',
        type: 'line',
        smooth: true,
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(79, 172, 254, 0.5)' },
            { offset: 1, color: 'rgba(79, 172, 254, 0.1)' }
          ])
        }
      }
    ]
  }
  
  logChart.setOption(option)
}

const initLoginChart = () => {
  if (!loginChartRef.value) return
  
  loginChart = echarts.init(loginChartRef.value)
  
  const hours = Array.from({ length: 24 }, (_, i) => `${i}:00`)
  const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  const data = []
  
  for (let i = 0; i < 7; i++) {
    for (let j = 0; j < 24; j++) {
      data.push([j, i, Math.floor(Math.random() * 50)])
    }
  }
  
  const option: EChartsOption = {
    tooltip: {
      position: 'top'
    },
    grid: {
      height: '50%',
      top: '10%'
    },
    xAxis: {
      type: 'category',
      data: hours,
      splitArea: {
        show: true
      }
    },
    yAxis: {
      type: 'category',
      data: days,
      splitArea: {
        show: true
      }
    },
    visualMap: {
      min: 0,
      max: 50,
      calculable: true,
      orient: 'horizontal',
      left: 'center',
      bottom: '15%'
    },
    series: [
      {
        name: 'Login Count',
        type: 'heatmap',
        data: data,
        label: {
          show: false
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  loginChart.setOption(option)
}

const handleRefresh = () => {
  userChart?.resize()
  roleChart?.resize()
  deptChart?.resize()
  logChart?.resize()
  loginChart?.resize()
}

const handleReset = () => {
  dateRange.value = []
  handleRefresh()
}

const handleResize = () => {
  userChart?.resize()
  roleChart?.resize()
  deptChart?.resize()
  logChart?.resize()
  loginChart?.resize()
}

onMounted(() => {
  initUserChart()
  initRoleChart()
  initDeptChart()
  initLogChart()
  initLoginChart()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  userChart?.dispose()
  roleChart?.dispose()
  deptChart?.dispose()
  logChart?.dispose()
  loginChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.report-dashboard-page {
  padding: 20px;

  .filter-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      margin-bottom: 20px;

      .stat-content {
        display: flex;
        align-items: center;

        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 12px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          color: white;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 28px;
            font-weight: bold;
            color: #333;
            line-height: 1.2;
          }

          .stat-label {
            font-size: 14px;
            color: #999;
            margin-top: 4px;
          }

          .stat-trend {
            font-size: 12px;
            margin-top: 4px;
            display: flex;
            align-items: center;
            gap: 2px;

            &.up {
              color: #67c23a;
            }

            &.down {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .charts-row {
    margin-bottom: 20px;

    .chart-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .chart-container {
        height: 350px;
      }
    }
  }
}
</style>
