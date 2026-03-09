<template>
  <div class="report-dashboard-page">
    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            style="width: 350px;"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRefresh">刷新</el-button>
          <el-button @click="handleReset">重置</el-button>
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
              <span>用户增长趋势</span>
            </div>
          </template>
          <div ref="userChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>角色分布</span>
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
              <span>部门人员统计</span>
            </div>
          </template>
          <div ref="deptChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>操作日志趋势</span>
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
              <span>登录日志统计</span>
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
    label: '总用户数',
    value: '1,256',
    icon: User,
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
    trend: 12.5
  },
  {
    label: '活跃用户',
    value: '892',
    icon: UserFilled,
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
    trend: 8.2
  },
  {
    label: '今日操作',
    value: '3,456',
    icon: Operation,
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
    trend: -3.1
  },
  {
    label: '工作流实例',
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
      data: ['新增用户', '活跃用户']
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
      data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '新增用户',
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
        name: '活跃用户',
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
        name: '角色分布',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 1048, name: '超级管理员' },
          { value: 735, name: '系统管理员' },
          { value: 580, name: '普通用户' },
          { value: 484, name: '访客' }
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
      data: ['技术部', '产品部', '运营部', '市场部', '财务部', '人事部']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '人数',
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
      data: ['操作次数']
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
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '操作次数',
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
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
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
        name: '登录次数',
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
