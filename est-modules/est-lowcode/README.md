# EST Low Code Platform - 低代码平台基础框架

**版本**: 2.4.0-SNAPSHOT  
**状态**: 架构设计完成，待实现

---

## 📋 概述

EST Low Code Platform 是 EST Framework 的低代码平台基础框架，提供可视化流程设计器、表单设计器、拖拽式界面构建和报表设计器等功能，帮助开发者快速构建企业级应用。

### 核心特性

- **可视化流程设计器** - 拖拽式流程设计，支持多种节点类型
- **表单设计器** - 丰富的表单组件库，支持数据绑定和验证
- **拖拽式界面构建** - UI 组件库，页面布局设计
- **报表设计器** - 报表模板设计，数据可视化
- **API 与实现分离** - 清晰的接口定义和实现分离
- **模块化设计** - 独立的低代码模块，按需引入

---

## 🏗️ 模块结构

```
est-lowcode/
├── est-lowcode-api/              # API 接口定义
│   ├── est-lowcode-flow-api/     # 流程设计器 API
│   ├── est-lowcode-form-api/     # 表单设计器 API
│   ├── est-lowcode-ui-api/       # 界面构建器 API
│   └── est-lowcode-report-api/   # 报表设计器 API
├── est-lowcode-impl/             # 实现模块
│   ├── est-lowcode-flow-impl/    # 流程设计器实现
│   ├── est-lowcode-form-impl/    # 表单设计器实现
│   ├── est-lowcode-ui-impl/      # 界面构建器实现
│   └── est-lowcode-report-impl/  # 报表设计器实现
├── README.md
└── pom.xml
```

---

## 🎯 核心组件

### 1. 可视化流程设计器（est-lowcode-flow）

**功能特性**:
- 流程节点拖拽
- 连线编辑器
- 属性面板
- 流程预览
- 流程版本管理
- 流程模拟运行

**节点类型**:
- 开始节点
- 结束节点
- 任务节点（人工任务、自动任务）
- 网关节点（排他网关、并行网关、包容网关）
- 子流程节点

### 2. 表单设计器（est-lowcode-form）

**功能特性**:
- 表单组件库
- 表单拖拽设计
- 表单验证配置
- 表单数据绑定
- 表单版本管理
- 表单预览

**表单组件**:
- 基础组件（文本框、文本域、数字框、日期选择器）
- 选择组件（下拉框、单选框、复选框）
- 高级组件（文件上传、图片上传、富文本编辑器）
- 布局组件（栅格布局、标签页、折叠面板）

### 3. 拖拽式界面构建（est-lowcode-ui）

**功能特性**:
- UI 组件库
- 页面布局设计器
- 样式配置面板
- 预览和导出
- 页面模板管理

**UI 组件**:
- 基础组件（按钮、图标、标签、徽章）
- 数据组件（表格、卡片、列表、树形）
- 导航组件（菜单、面包屑、分页）
- 反馈组件（对话框、消息提示、加载状态）

### 4. 报表设计器（est-lowcode-report）

**功能特性**:
- 报表模板设计
- 数据源配置
- 图表组件库
- 报表预览
- 报表导出（PDF、Excel）
- 报表版本管理

**图表类型**:
- 柱状图
- 折线图
- 饼图
- 散点图
- 雷达图
- 热力图

---

## 📝 开发计划

### 阶段一：API 接口定义（已完成）
- ✅ 模块结构设计
- ✅ pom.xml 配置
- ✅ 架构设计文档

### 阶段二：流程设计器实现（待开始）
- [ ] 流程节点定义
- [ ] 流程编辑器核心
- [ ] 流程版本管理
- [ ] 流程模拟运行

### 阶段三：表单设计器实现（待开始）
- [ ] 表单组件库
- [ ] 表单编辑器核心
- [ ] 表单数据绑定
- [ ] 表单验证

### 阶段四：界面构建器实现（待开始）
- [ ] UI 组件库
- [ ] 页面布局设计器
- [ ] 样式配置
- [ ] 页面导出

### 阶段五：报表设计器实现（待开始）
- [ ] 报表模板设计
- [ ] 数据源配置
- [ ] 图表组件库
- [ ] 报表导出

---

## 🚀 快速开始

### Maven 依赖

```xml
<!-- 流程设计器 API -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-lowcode-flow-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<!-- 表单设计器 API -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-lowcode-form-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<!-- 界面构建器 API -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-lowcode-ui-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>

<!-- 报表设计器 API -->
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-lowcode-report-api</artifactId>
    <version>2.4.0-SNAPSHOT</version>
</dependency>
```

### 使用示例（待实现）

```java
// 流程设计器使用示例（待实现）
// 表单设计器使用示例（待实现）
// 界面构建器使用示例（待实现）
// 报表设计器使用示例（待实现）
```

---

## 📚 相关文档

| 文档 | 说明 |
|------|------|
| [路线图](../../dev-docs/roadmap.md) | 项目整体路线图 |
| [中期计划实施总结](../../dev-docs/mid-term-implementation-summary.md) | 中期计划完成情况 |
| [工作流引擎](../est-data-group/est-workflow/README.md) | 工作流引擎文档 |
| [EST 架构文档](../../docs/architecture/README.md) | EST 框架架构 |

---

## 🤝 贡献指南

欢迎参与 EST Low Code Platform 的开发！

### 如何贡献

1. 选择感兴趣的组件（流程设计器、表单设计器等）
2. 创建 Issue 说明您计划如何实现
3. Fork 仓库，创建开发分支
4. 提交 PR，遵循贡献规范

### 建议的贡献方向

- 新手友好：文档完善、示例代码
- 中级：功能增强、API 设计
- 高级：核心实现、架构设计

---

## 📞 联系方式

- **项目地址**: https://github.com/idcu/est
- **问题反馈**: https://github.com/idcu/est/issues
- **讨论区**: https://github.com/idcu/est/discussions

---

## 📄 许可证

Apache License 2.0

---

**最后更新**: 2026-03-10  
**EST Framework Team**
