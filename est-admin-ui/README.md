# EST Admin UI - 后台管理系统前端

基于 Vue 3 + Element Plus + Vite 的现代化后台管理系统前端。

## 技术栈

- **框架**: Vue 3.x (Composition API)
- **UI 组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4.x
- **HTTP 客户端**: Axios
- **构建工具**: Vite
- **语言**: TypeScript

## 项目结构

```
est-admin-ui/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口封装
│   │   ├── auth.ts        # 认证 API
│   │   ├── user.ts        # 用户管理 API
│   │   ├── role.ts        # 角色管理 API
│   │   ├── menu.ts        # 菜单管理 API
│   │   ├── department.ts  # 部门管理 API
│   │   └── tenant.ts      # 租户管理 API
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   │   └── layout/       # 布局组件
│   │       └── Layout.vue
│   ├── router/            # 路由配置
│   │   └── index.ts
│   ├── stores/            # Pinia 状态管理
│   │   ├── app.ts         # 应用状态
│   │   └── user.ts        # 用户状态
│   ├── utils/             # 工具函数
│   │   └── request.ts     # Axios 封装
│   ├── views/             # 页面组件
│   │   ├── login/         # 登录页面
│   │   ├── dashboard/     # 仪表板
│   │   └── system/        # 系统管理
│   │       ├── User.vue
│   │       ├── Role.vue
│   │       ├── Menu.vue
│   │       ├── Department.vue
│   │       └── Tenant.vue
│   ├── App.vue            # 根组件
│   └── main.ts            # 应用入口
├── .env.development       # 开发环境变量
├── .env.production        # 生产环境变量
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

## 快速开始

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

开发服务器将在 http://localhost:3000 启动

### 构建生产版本

```bash
npm run build
```

### 预览生产构建

```bash
npm run preview
```

## 默认账号

- 用户名: `admin`
- 密码: `admin123`

## 功能特性

### 认证授权
- JWT Token 认证
- 自动 Token 刷新
- 路由守卫
- 本地存储持久化

### 系统管理
- 用户管理 (CRUD)
- 角色管理 (CRUD + 权限分配)
- 菜单管理 (树形结构)
- 部门管理 (树形结构)
- 租户管理 (三种模式)

### 其他特性
- 响应式布局
- 状态管理
- API 请求封装
- 统一错误处理

## 环境变量

### .env.development
```env
VITE_API_BASE_URL=/admin/api
```

### .env.production
```env
VITE_API_BASE_URL=https://your-api-domain.com/admin/api
```

## 后端接口

后端需要运行在 8080 端口，提供以下 API:

- `POST /admin/api/auth/login` - 登录
- `POST /admin/api/auth/logout` - 登出
- `GET /admin/api/auth/current` - 获取当前用户
- `POST /admin/api/auth/refresh-token` - 刷新 Token
- `GET /admin/api/users` - 用户列表
- `GET /admin/api/roles` - 角色列表
- `GET /admin/api/menus` - 菜单列表
- `GET /admin/api/departments` - 部门列表
- `GET /admin/api/tenants` - 租户列表

## 开发指南

### 添加新页面

1. 在 `src/views/` 下创建页面组件
2. 在 `src/router/index.ts` 中添加路由
3. 如果需要 API，在 `src/api/` 下创建对应的 API 文件

### 添加新的 API 接口

```typescript
import request from '@/utils/request'

export interface YourData {
  id: string
  name: string
}

export function listYourData() {
  return request<YourData[]>({
    url: '/admin/api/your-data',
    method: 'get'
  })
}
```

### 使用 Store

```typescript
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 检查登录状态
if (userStore.isLoggedIn()) {
  // 已登录
}

// 检查权限
if (userStore.hasPermission('system:user:add')) {
  // 有权限
}
```

## 许可证

MIT License
