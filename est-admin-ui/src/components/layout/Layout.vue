<template>
  <el-container class="layout-container">
    <el-aside :width="sidebarCollapsed ? '64px' : '200px'" class="sidebar">
      <div class="logo">
        <span v-if="!sidebarCollapsed">EST Admin</span>
        <span v-else>EST</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="sidebarCollapsed"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <template #title>仪表�?/template>
        </el-menu-item>
        <template v-for="menu in userStore.menus" :key="menu.id">
          <template v-if="menu.children && menu.children.length > 0">
            <el-sub-menu :index="menu.path">
              <template #title>
                <el-icon><component :is="getMenuIcon(menu.icon)" /></el-icon>
                <span>{{ menu.name }}</span>
              </template>
              <el-menu-item
                v-for="child in menu.children"
                :key="child.id"
                :index="child.path"
              >
                <el-icon><component :is="getMenuIcon(child.icon)" /></el-icon>
                <template #title>{{ child.name }}</template>
              </el-menu-item>
            </el-sub-menu>
          </template>
          <template v-else>
            <el-menu-item :index="menu.path">
              <el-icon><component :is="getMenuIcon(menu.icon)" /></el-icon>
              <template #title>{{ menu.name }}</template>
            </el-menu-item>
          </template>
        </template>
      </el-menu>
    </el-aside>

    <el-container class="main-container">
      <el-header class="header">
        <div class="header-left">
          <el-icon class="toggle-icon" @click="toggleSidebar">
            <Fold v-if="!sidebarCollapsed" />
            <Expand v-else />
          </el-icon>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-icon><Avatar /></el-icon>
              <span>{{ userStore.userInfo?.nickname || 'Admin' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登�?/el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, markRaw } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import {
  Fold,
  Expand,
  DataLine,
  Setting,
  User,
  UserFilled,
  Menu,
  OfficeBuilding,
  House,
  Avatar
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

const sidebarCollapsed = computed(() => appStore.sidebarCollapsed)
const activeMenu = computed(() => route.path)

const iconMap: Record<string, any> = {
  setting: markRaw(Setting),
  user: markRaw(User),
  team: markRaw(UserFilled),
  menu: markRaw(Menu),
  tree: markRaw(OfficeBuilding),
  office: markRaw(OfficeBuilding),
  house: markRaw(House),
  home: markRaw(House)
}

function getMenuIcon(icon: string) {
  return iconMap[icon] || markRaw(Setting)
}

function toggleSidebar() {
  appStore.toggleSidebar()
}

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;

  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    color: white;
    font-size: 18px;
    font-weight: bold;
    background-color: #2b3a4a;
  }
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: white;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;

  .header-left {
    .toggle-icon {
      font-size: 20px;
      cursor: pointer;
      &:hover {
        color: #409EFF;
      }
    }
  }

  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
    }
  }
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}
</style>
