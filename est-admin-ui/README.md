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
│   │   ├── tenant.ts      # 租户管理 API
│   │   ├── log.ts         # 日志管理 API
│   │   ├── monitor.ts     # 监控管理 API
│   │   ├── integration.ts # 第三方集成 API
│   │   └── ai.ts          # AI 助手 API
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   │   └── layout/       # 布局组件
│   │       └── Layout.vue
│   ├── directives/        # 自定义指令
│   │   └── permission.ts  # 权限指令
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
│   │   ├── system/        # 系统管理
│   │   │   ├── User.vue
│   │   │   ├── Role.vue
│   │   │   ├── Menu.vue
│   │   │   ├── Department.vue
│   │   │   ├── Tenant.vue
│   │   │   ├── OperationLog.vue
│   │   │   └── LoginLog.vue
│   │   ├── monitor/       # 系统监控
│   │   │   ├── ServiceMonitor.vue
│   │   │   ├── OnlineUser.vue
│   │   │   └── CacheMonitor.vue
│   │   ├── integration/   # 第三方集成
│   │   │   ├── Email.vue
│   │   │   ├── Sms.vue
│   │   │   └── Oss.vue
│   │   └── ai/            # AI 助手
│   │       ├── Chat.vue
│   │       ├── Code.vue
│   │       ├── Reference.vue
│   │       └── Template.vue
│   ├── App.vue            # 根组件
│   └── main.ts            # 应用入口
├── .env.development       # 开发环境变量
├── .env.production        # 生产环境变量
├── index.html
├── package.json
├── tsconfig.json
├── tsconfig.node.json
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
- 操作日志管理
- 登录日志管理

### 系统监控
- 服务监控 (JVM、系统指标)
- 在线用户管理
- 缓存监控

### 第三方集成
- 邮件服务配置
- 短信服务配置
- 对象存储 (OSS) 配置

### AI 助手
- AI 对话聊天
- 代码生成
- 代码解释
- 代码优化
- 开发参考查询
- 最佳实践查询
- 教程查询
- 提示模板管理

### 其他特性
- 响应式布局
- 状态管理
- API 请求封装
- 统一错误处理
- 权限指令
- 权限检查

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

### 认证接口
- `POST /admin/api/auth/login` - 登录
- `POST /admin/api/auth/logout` - 登出
- `GET /admin/api/auth/current` - 获取当前用户
- `POST /admin/api/auth/refresh-token` - 刷新 Token

### 系统管理接口
- `GET /admin/api/users` - 用户列表
- `POST /admin/api/users` - 创建用户
- `PUT /admin/api/users/:id` - 更新用户
- `DELETE /admin/api/users/:id` - 删除用户
- `GET /admin/api/roles` - 角色列表
- `POST /admin/api/roles` - 创建角色
- `PUT /admin/api/roles/:id` - 更新角色
- `DELETE /admin/api/roles/:id` - 删除角色
- `GET /admin/api/menus` - 菜单列表
- `POST /admin/api/menus` - 创建菜单
- `PUT /admin/api/menus/:id` - 更新菜单
- `DELETE /admin/api/menus/:id` - 删除菜单
- `GET /admin/api/departments` - 部门列表
- `POST /admin/api/departments` - 创建部门
- `PUT /admin/api/departments/:id` - 更新部门
- `DELETE /admin/api/departments/:id` - 删除部门
- `GET /admin/api/tenants` - 租户列表
- `POST /admin/api/tenants` - 创建租户
- `PUT /admin/api/tenants/:id` - 更新租户
- `DELETE /admin/api/tenants/:id` - 删除租户

### 日志管理接口
- `GET /admin/api/operation-logs` - 操作日志列表
- `GET /admin/api/operation-logs/:id` - 获取操作日志详情
- `DELETE /admin/api/operation-logs/:id` - 删除操作日志
- `DELETE /admin/api/operation-logs` - 清空操作日志
- `GET /admin/api/login-logs` - 登录日志列表
- `GET /admin/api/login-logs/:id` - 获取登录日志详情
- `DELETE /admin/api/login-logs/:id` - 删除登录日志
- `DELETE /admin/api/login-logs` - 清空登录日志

### 监控接口
- `GET /admin/api/monitor/jvm` - JVM 监控指标
- `GET /admin/api/monitor/system` - 系统监控指标
- `GET /admin/api/monitor/health` - 健康检查
- `GET /admin/api/monitor/metrics` - 所有监控指标
- `GET /admin/api/online-users` - 在线用户列表
- `GET /admin/api/online-users/count` - 在线用户数
- `GET /admin/api/cache/statistics` - 缓存统计
- `GET /admin/api/cache/keys` - 缓存键列表

### 第三方集成接口
- `GET /admin/api/integration/email` - 获取邮件配置
- `PUT /admin/api/integration/email` - 更新邮件配置
- `GET /admin/api/integration/sms` - 获取短信配置
- `PUT /admin/api/integration/sms` - 更新短信配置
- `GET /admin/api/integration/oss` - 获取 OSS 配置
- `PUT /admin/api/integration/oss` - 更新 OSS 配置

### AI 助手接口
- `POST /admin/api/ai/chat` - AI 对话
- `POST /admin/api/ai/code/generate` - 代码生成
- `POST /admin/api/ai/code/suggest` - 代码建议
- `POST /admin/api/ai/code/explain` - 代码解释
- `POST /admin/api/ai/code/optimize` - 代码优化
- `GET /admin/api/ai/reference` - 开发参考
- `GET /admin/api/ai/bestpractice` - 最佳实践
- `GET /admin/api/ai/tutorial` - 教程
- `GET /admin/api/ai/templates` - 提示模板列表
- `POST /admin/api/ai/templates/generate` - 生成提示

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
