import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/components/layout/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '仪表�?, requiresAuth: true }
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { title: '系统管理', requiresAuth: true },
        children: [
          {
            path: 'user',
            name: 'User',
            component: () => import('@/views/system/User.vue'),
            meta: { title: '用户管理', requiresAuth: true }
          },
          {
            path: 'role',
            name: 'Role',
            component: () => import('@/views/system/Role.vue'),
            meta: { title: '角色管理', requiresAuth: true }
          },
          {
            path: 'menu',
            name: 'Menu',
            component: () => import('@/views/system/Menu.vue'),
            meta: { title: '菜单管理', requiresAuth: true }
          },
          {
            path: 'department',
            name: 'Department',
            component: () => import('@/views/system/Department.vue'),
            meta: { title: '部门管理', requiresAuth: true }
          },
          {
            path: 'tenant',
            name: 'Tenant',
            component: () => import('@/views/system/Tenant.vue'),
            meta: { title: '租户管理', requiresAuth: true }
          },
          {
            path: 'operation-log',
            name: 'OperationLog',
            component: () => import('@/views/system/OperationLog.vue'),
            meta: { title: '操作日志', requiresAuth: true }
          },
          {
            path: 'login-log',
            name: 'LoginLog',
            component: () => import('@/views/system/LoginLog.vue'),
            meta: { title: '登录日志', requiresAuth: true }
          }
        ]
      },
      {
        path: 'monitor',
        name: 'Monitor',
        redirect: '/monitor/service',
        meta: { title: '系统监控', requiresAuth: true },
        children: [
          {
            path: 'service',
            name: 'ServiceMonitor',
            component: () => import('@/views/monitor/ServiceMonitor.vue'),
            meta: { title: '服务监控', requiresAuth: true }
          },
          {
            path: 'online-user',
            name: 'OnlineUser',
            component: () => import('@/views/monitor/OnlineUser.vue'),
            meta: { title: '在线用户', requiresAuth: true }
          },
          {
            path: 'cache',
            name: 'CacheMonitor',
            component: () => import('@/views/monitor/CacheMonitor.vue'),
            meta: { title: '缓存监控', requiresAuth: true }
          }
        ]
      },
      {
        path: 'integration',
        name: 'Integration',
        redirect: '/integration/email',
        meta: { title: '第三方集�?, requiresAuth: true },
        children: [
          {
            path: 'email',
            name: 'Email',
            component: () => import('@/views/integration/Email.vue'),
            meta: { title: '邮件服务', requiresAuth: true }
          },
          {
            path: 'sms',
            name: 'Sms',
            component: () => import('@/views/integration/Sms.vue'),
            meta: { title: '短信服务', requiresAuth: true }
          },
          {
            path: 'oss',
            name: 'Oss',
            component: () => import('@/views/integration/Oss.vue'),
            meta: { title: '对象存储', requiresAuth: true }
          }
        ]
      },
      {
        path: 'ai',
        name: 'Ai',
        redirect: '/ai/chat',
        meta: { title: 'AI 助手', requiresAuth: true },
        children: [
          {
            path: 'chat',
            name: 'AiChat',
            component: () => import('@/views/ai/Chat.vue'),
            meta: { title: 'AI 对话', requiresAuth: true }
          },
          {
            path: 'code',
            name: 'AiCode',
            component: () => import('@/views/ai/Code.vue'),
            meta: { title: '代码生成', requiresAuth: true }
          },
          {
            path: 'reference',
            name: 'AiReference',
            component: () => import('@/views/ai/Reference.vue'),
            meta: { title: '开发参�?, requiresAuth: true }
          },
          {
            path: 'template',
            name: 'AiTemplate',
            component: () => import('@/views/ai/Template.vue'),
            meta: { title: '提示模板', requiresAuth: true }
          }
        ]
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || 'EST Admin'} - EST Admin Console`
  
  const userStore = useUserStore()
  const requiresAuth = to.meta.requiresAuth !== false
  
  if (requiresAuth && !userStore.isLoggedIn()) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn()) {
    next('/')
  } else {
    next()
  }
})

export default router
