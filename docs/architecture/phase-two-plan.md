# EST 框架阶段二实施计划

## 概述

阶段二的目标是建立EST框架的前端基础设施，包括前端框架集成、后台管理系统模板和系统管理模块的Web界面。这将使EST框架具备完整的企业级应用开发能力。

---

## 阶段二目标

### 核心目标
1. **前端框架集成**：集成Vue3 + Element Plus，建立现代前端开发环境
2. **后台管理系统模板**：提供完整的后台管理UI框架和布局
3. **系统管理Web界面**：将阶段一的RBAC系统通过Web界面可视化

### 技术栈
- **前端框架**：Vue 3.x（Composition API）
- **UI组件库**：Element Plus
- **状态管理**：Pinia
- **路由**：Vue Router 4.x
- **HTTP客户端**：Axios
- **构建工具**：Vite
- **包管理器**：npm / pnpm

---

## 高优先级任务

### 1. 前端架构设计 ✅

#### 1.1 项目结构设计
```
est-admin-ui/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口封装
│   │   ├── user.ts
│   │   ├── role.ts
│   │   ├── menu.ts
│   │   ├── department.ts
│   │   └── tenant.ts
│   ├── assets/            # 资源文件
│   │   ├── styles/
│   │   └── images/
│   ├── components/        # 公共组件
│   │   ├── layout/
│   │   │   ├── Header.vue
│   │   │   ├── Sidebar.vue
│   │   │   └── Main.vue
│   │   └── common/
│   ├── composables/       # 组合式函数
│   │   ├── useAuth.ts
│   │   └── usePermission.ts
│   ├── router/            # 路由配置
│   │   └── index.ts
│   ├── stores/            # Pinia状态管理
│   │   ├── user.ts
│   │   ├── app.ts
│   │   └── permission.ts
│   ├── utils/             # 工具函数
│   │   ├── request.ts
│   │   └── auth.ts
│   ├── views/             # 页面组件
│   │   ├── login/
│   │   │   └── Login.vue
│   │   ├── system/
│   │   │   ├── user/
│   │   │   │   ├── UserList.vue
│   │   │   │   └── UserForm.vue
│   │   │   ├── role/
│   │   │   │   ├── RoleList.vue
│   │   │   │   └── RoleForm.vue
│   │   │   ├── menu/
│   │   │   │   ├── MenuList.vue
│   │   │   │   └── MenuForm.vue
│   │   │   ├── department/
│   │   │   │   ├── DepartmentList.vue
│   │   │   │   └── DepartmentForm.vue
│   │   │   └── tenant/
│   │   │       ├── TenantList.vue
│   │   │       └── TenantForm.vue
│   │   └── dashboard/
│   │       └── Dashboard.vue
│   ├── App.vue
│   └── main.ts
├── .env.development
├── .env.production
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

#### 1.2 前后端通信协议
- **通信方式**：RESTful API
- **数据格式**：JSON
- **认证方式**：JWT Token
- **请求格式**：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {}
  }
  ```

---

### 2. 前端项目创建 ⏳

#### 2.1 初始化Vue3项目
使用Vite创建Vue3项目：
```bash
npm create vite@latest est-admin-ui -- --template vue-ts
cd est-admin-ui
```

#### 2.2 安装依赖
```bash
# 核心依赖
npm install vue@3 vue-router@4 pinia axios

# UI组件库
npm install element-plus @element-plus/icons-vue

# 工具库
npm install @vueuse/core dayjs nprogress

# 开发依赖
npm install -D @types/node sass
```

#### 2.3 Vite配置
```typescript
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
```

---

### 3. 后台管理系统模板 ⏳

#### 3.1 布局组件
**Header.vue** - 顶部导航栏
- 品牌Logo
- 用户信息下拉菜单
- 通知中心
- 主题切换
- 全屏切换

**Sidebar.vue** - 侧边栏
- 动态菜单渲染
- 菜单折叠/展开
- 路由高亮

**Main.vue** - 主内容区
- 面包屑导航
- 标签栏（多标签页）
- 内容渲染区

#### 3.2 登录页面
**Login.vue**
- 用户名/密码登录
- 租户选择（多租户模式）
- 记住密码
- 验证码

#### 3.3 仪表板页面
**Dashboard.vue**
- 欢迎卡片
- 统计数据（用户数、角色数、在线用户等）
- 快捷入口
- 系统信息

---

### 4. 系统管理Web界面 ⏳

#### 4.1 用户管理
**UserList.vue**
- 用户列表表格
- 搜索筛选（用户名、邮箱、状态）
- 新增用户按钮
- 编辑、删除操作
- 批量操作

**UserForm.vue**
- 基本信息表单
- 角色分配
- 权限分配
- 状态设置

#### 4.2 角色管理
**RoleList.vue**
- 角色列表表格
- 搜索筛选
- 新增角色按钮
- 编辑、删除操作

**RoleForm.vue**
- 角色基本信息
- 权限分配（树形选择）
- 菜单分配

#### 4.3 菜单管理
**MenuList.vue**
- 树形菜单展示
- 拖拽排序
- 新增、编辑、删除操作

**MenuForm.vue**
- 菜单类型（目录、菜单、按钮）
- 图标选择
- 路由配置
- 权限标识

#### 4.4 部门管理
**DepartmentList.vue**
- 树形部门展示
- 新增、编辑、删除操作

**DepartmentForm.vue**
- 部门基本信息
- 上级部门选择

#### 4.5 租户管理
**TenantList.vue**
- 租户列表表格
- 搜索筛选
- 新增、编辑、删除操作

**TenantForm.vue**
- 租户基本信息
- 租户模式选择（COLUMN/SCHEMA/DATABASE）
- 域名配置
- 过期时间设置

---

## 后端API增强

### RESTful API设计

#### 认证API
```
POST /api/auth/login          # 登录
POST /api/auth/logout         # 登出
GET  /api/auth/current        # 获取当前用户信息
POST /api/auth/refresh-token  # 刷新Token
```

#### 用户管理API
```
GET    /api/users              # 获取用户列表
GET    /api/users/:id          # 获取用户详情
POST   /api/users              # 创建用户
PUT    /api/users/:id          # 更新用户
DELETE /api/users/:id          # 删除用户
PUT    /api/users/:id/password # 修改密码
PUT    /api/users/:id/roles    # 分配角色
PUT    /api/users/:id/status   # 更新状态
```

#### 角色管理API
```
GET    /api/roles              # 获取角色列表
GET    /api/roles/:id          # 获取角色详情
POST   /api/roles              # 创建角色
PUT    /api/roles/:id          # 更新角色
DELETE /api/roles/:id          # 删除角色
PUT    /api/roles/:id/permissions # 分配权限
PUT    /api/roles/:id/menus     # 分配菜单
```

#### 菜单管理API
```
GET    /api/menus              # 获取菜单树
GET    /api/menus/:id          # 获取菜单详情
POST   /api/menus              # 创建菜单
PUT    /api/menus/:id          # 更新菜单
DELETE /api/menus/:id          # 删除菜单
```

#### 部门管理API
```
GET    /api/departments        # 获取部门树
GET    /api/departments/:id    # 获取部门详情
POST   /api/departments        # 创建部门
PUT    /api/departments/:id    # 更新部门
DELETE /api/departments/:id    # 删除部门
```

#### 租户管理API
```
GET    /api/tenants            # 获取租户列表
GET    /api/tenants/:id        # 获取租户详情
POST   /api/tenants            # 创建租户
PUT    /api/tenants/:id        # 更新租户
DELETE /api/tenants/:id        # 删除租户
PUT    /api/tenants/:id/status # 更新状态
GET    /api/tenants/current    # 获取当前租户
```

---

## 中优先级任务

### 5. 日志管理模块
- 操作日志：记录用户操作
- 登录日志：记录登录信息
- 日志查询和筛选
- 日志导出

### 6. 监控管理模块
- 服务监控：JVM、系统指标
- 在线用户：查看当前在线用户
- 缓存监控：Redis缓存统计
- 请求追踪：API调用追踪

### 7. 第三方集成
- 支付集成：支付宝、微信支付
- 短信集成：阿里云、腾讯云短信
- 邮件集成：SMTP邮件发送
- 对象存储：OSS、MinIO

---

## 低优先级任务

### 8. 核心业务模块
- CRM：客户关系管理
- ERP：企业资源规划
- 商城：电商平台

---

## 实施步骤

### 第一周：前端基础
1. 创建前端项目结构
2. 配置Vite和依赖
3. 实现基础布局组件
4. 实现登录页面
5. 实现路由和状态管理

### 第二周：系统管理UI
1. 实现用户管理界面
2. 实现角色管理界面
3. 实现菜单管理界面
4. 实现部门管理界面
5. 实现租户管理界面

### 第三周：完善和优化
1. 集成后端API
2. 完善权限控制
3. 添加测试
4. 性能优化
5. 文档完善

---

## 技术要点

### 1. 权限控制
- 路由级权限：根据权限动态加载路由
- 按钮级权限：权限指令控制按钮显示
- 接口级权限：后端验证Token和权限

### 2. 状态管理
- 用户状态：用户信息、Token
- 应用状态：主题、布局设置
- 权限状态：权限列表、菜单列表

### 3. 性能优化
- 路由懒加载
- 组件按需引入
- 请求缓存
- 虚拟滚动

### 4. 开发体验
- TypeScript类型支持
- 热模块替换
- 开发工具集成
- 代码规范

---

## 验收标准

### 功能验收
- [ ] 完整的登录/登出流程
- [ ] 用户管理CRUD功能
- [ ] 角色管理CRUD功能
- [ ] 菜单管理CRUD功能
- [ ] 部门管理CRUD功能
- [ ] 租户管理CRUD功能
- [ ] 动态菜单和权限控制

### 技术验收
- [ ] Vue3 + TypeScript代码规范
- [ ] Element Plus组件正确使用
- [ ] 前后端联调正常
- [ ] 页面加载速度 < 2s
- [ ] 移动端适配

### 质量验收
- [ ] 代码审查通过
- [ ] 单元测试覆盖率 > 70%
- [ ] E2E测试通过
- [ ] 文档完善

---

## 风险与应对

| 风险 | 影响 | 概率 | 应对措施 |
|------|------|------|----------|
| 前端技术栈复杂 | 高 | 中 | 提供完善的文档和示例 |
| 前后端联调困难 | 中 | 高 | 提前定义API规范 |
| 性能不达标 | 中 | 中 | 持续性能优化 |
| 浏览器兼容性 | 低 | 低 | 明确支持的浏览器范围 |

---

## 总结

阶段二的完成将使EST框架具备完整的企业级应用开发能力，包括：
- 现代化的前端开发环境
- 完整的后台管理系统模板
- 可视化的系统管理界面

这将为后续的业务模块开发提供坚实的基础，使EST框架真正成长为芋道级别的企业级框架。

---

**文档版本**: 1.0  
**创建日期**: 2026-03-08  
**维护者**: EST 架构团队
