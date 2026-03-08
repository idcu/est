<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon user-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon role-icon">
              <el-icon><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.roleCount }}</div>
              <div class="stat-label">角色数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon menu-icon">
              <el-icon><Menu /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.menuCount }}</div>
              <div class="stat-label">菜单数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon tenant-icon">
              <el-icon><House /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.tenantCount }}</div>
              <div class="stat-label">租户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>欢迎使用 EST Admin Console</span>
            </div>
          </template>
          <div class="welcome-content">
            <h3>🎉 欢迎，Admin</h3>
            <p>这是一个现代化的企业级后台管理系统</p>
            <el-divider />
            <h4>快速导航</h4>
            <div class="quick-nav">
              <el-button type="primary" @click="$router.push('/system/user')">
                <el-icon><User /></el-icon>
                用户管理
              </el-button>
              <el-button type="success" @click="$router.push('/system/role')">
                <el-icon><UserFilled /></el-icon>
                角色管理
              </el-button>
              <el-button type="warning" @click="$router.push('/system/menu')">
                <el-icon><Menu /></el-icon>
                菜单管理
              </el-button>
              <el-button type="info" @click="$router.push('/system/department')">
                <el-icon><OfficeBuilding /></el-icon>
                部门管理
              </el-button>
              <el-button type="danger" @click="$router.push('/system/tenant')">
                <el-icon><House /></el-icon>
                租户管理
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统信息</span>
            </div>
          </template>
          <div class="system-info">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="框架版本">EST 2.0.0</el-descriptions-item>
              <el-descriptions-item label="Vue 版本">3.4.x</el-descriptions-item>
              <el-descriptions-item label="Element Plus">2.4.x</el-descriptions-item>
              <el-descriptions-item label="构建工具">Vite 5.x</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import {
  User,
  UserFilled,
  Menu,
  House,
  OfficeBuilding
} from '@element-plus/icons-vue'

const stats = ref({
  userCount: 128,
  roleCount: 16,
  menuCount: 64,
  tenantCount: 8
})
</script>

<style scoped lang="scss">
.dashboard {
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: 20px;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 8px;
        display: flex;
        justify-content: center;
        align-items: center;
        font-size: 30px;
        color: white;

        &.user-icon {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        &.role-icon {
          background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        &.menu-icon {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        &.tenant-icon {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
      }

      .stat-info {
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #333;
        }
        .stat-label {
          color: #999;
          font-size: 14px;
        }
      }
    }
  }

  .card-header {
    font-weight: bold;
  }

  .welcome-content {
    h3 {
      margin-bottom: 10px;
      color: #333;
    }
    p {
      color: #666;
    }
    h4 {
      margin: 20px 0 15px;
      color: #333;
    }
    .quick-nav {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
    }
  }

  .system-info {
    :deep(.el-descriptions__label) {
      width: 100px;
    }
  }
}
</style>
