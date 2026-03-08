# EST App层模块重构计划

## 📋 概述

本次重构将把app层中的可复用功能抽取到est-modules层，实现更清晰的架构分层。

## 🎯 重构目标

1. **职责清晰**：app层专注于组装，modules层提供可复用功能
2. **提高复用性**：抽取的模块可以在多个项目中复用
3. **易于维护**：模块边界清晰，修改互不影响
4. **便于测试**：每个模块可以独立测试

## 📦 模块抽取清单

### 1. est-rbac - RBAC权限管理模块 ⏳ 进行中

**包含内容：**
- ✅ API接口（User, Role, Menu, Department, Tenant等）
- ✅ Service接口（UserService, RoleService等）
- ⏳ 实现类（DefaultUser, DefaultRole等）
- ⏳ 切面（PermissionAspect）
- ⏳ 适配器（SecurityUserAdapter）

**进度：** 60%

---

### 2. est-audit - 审计日志模块 📋 待开始

**包含内容：**
- OperationLog, LoginLog实体
- OperationLogService, LoginLogService接口
- DefaultOperationLogService, DefaultLoginLogService实现
- OperationLogAspect切面
- OperationLogAnnotation注解

---

### 3. est-integration - 集成服务模块 📋 待开始

**包含内容：**
- EmailService接口及实现
- SmsService接口及实现
- OssService接口及实现
- 相关模型类（EmailTemplate, SmsTemplate, OssFile等）

---

### 4. est-web模块拆分 📋 待规划

**拟拆分为：**
- est-web-server - HTTP服务器核心
- est-web-router - 路由系统
- est-web-middleware - 中间件集合
- est-web-session - 会话管理
- est-web-template - 模板引擎

---

## 🏗️ 新的架构图

```
est-app/
├── est-admin/          ← 精简为组装层
│   ├── est-admin-api/  ← 仅保留admin特有的接口
│   └── est-admin-impl/ ← 仅保留组装逻辑和Controller
├── est-web/            ← 精简为Web框架组装
└── est-console/

est-modules/
├── est-rbac/           ← 新增：权限管理
├── est-audit/          ← 新增：审计日志
├── est-integration/    ← 新增：集成服务
├── est-web-server/     ← 新增：Web服务器
├── est-web-router/     ← 新增：路由系统
├── est-web-middleware/ ← 新增：中间件
├── est-web-session/    ← 新增：会话管理
├── est-web-template/   ← 新增：模板引擎
└── (现有模块...)
```

## 📝 实施步骤

### 阶段一：est-rbac模块（当前）
1. ✅ 创建模块目录结构和POM文件
2. ✅ 抽取API接口到est-rbac-api
3. ⏳ 抽取实现类到est-rbac-impl
4. ⏳ 更新est-admin依赖
5. ⏳ 更新根pom.xml

### 阶段二：est-audit模块
1. 创建模块目录结构和POM文件
2. 抽取API接口
3. 抽取实现类
4. 更新est-admin依赖

### 阶段三：est-integration模块
1. 创建模块目录结构和POM文件
2. 抽取API接口
3. 抽取实现类
4. 更新est-admin依赖

### 阶段四：est-web模块拆分
1. 分析依赖关系
2. 创建各个子模块
3. 迁移代码
4. 更新依赖

## 🔧 注意事项

1. **向后兼容**：保持原有的est-admin模块作为兼容层
2. **逐步迁移**：先创建新模块，再逐步更新引用
3. **测试验证**：每个阶段完成后进行编译和测试验证
4. **文档更新**：同步更新相关文档

---

**创建日期**: 2026-03-08  
**维护者**: EST架构团队
