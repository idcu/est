# EST 产品线快速开始指南

欢迎使用 EST 产品线！本指南将帮助您快速开始使用 EST 框架的各种产品。

## 📋 产品线概览

EST 框架提供以下产品线：

| 产品 | 版本 | 状态 | 说明 |
|--------|------|------|------|
| **EST-Monomer** | 2.3.0 | ✅ 可用 | 单体应用启动器 - 开箱即用的单体应用模板 |
| **EST-Cloud** | 规划中 | 📝 待开发 | 微服务应用启动器 |
| **EST-Admin** | 2.3.0 | ✅ 可用 | 管理后台模块 |
| **EST-Admin-UI** | 1.0.0 | ✅ 可用 | 管理后台前端 (Vue3 + Element Plus) |

---

## 🚀 快速开始

### 第一步：环境准备

在开始之前，请确保您的系统已安装：

- **JDK 21+** - Java 开发工具包
- **Maven 3.6+** - Java 项目管理工具
- **Node.js 16+** - JavaScript 运行时（仅前端需要）
- **Git** - 版本控制工具（可选）

### 第二步：克隆项目

```bash
git clone https://github.com/idcu/est.git
cd est
```

### 第三步：构建项目

在项目根目录执行：

```bash
mvn clean install -DskipTests
```

这将编译整个 EST 框架并安装到本地 Maven 仓库。

---

## 🎯 使用 EST-Monomer（单体应用）

### 简介

EST-Monomer 是一个开箱即用的单体应用启动器，整合了 EST 框架的核心模块，适合快速开发中小型应用。

### 启动后端

#### Windows

```bash
cd est-starter-monomer
run.bat
```

#### Linux/Mac

```bash
cd est-starter-monomer
chmod +x run.sh
./run.sh
```

或者使用 Maven：

```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.starter.monomer.EstMonomerApplication"
```

### 访问应用

启动成功后，访问：

- **首页**: http://localhost:8080
- **健康检查**: http://localhost:8080/api/system/health
- **系统信息**: http://localhost:8080/api/system/info

### 更多信息

详细文档请参考：[est-starter-monomer/README.md](est-starter-monomer/README.md)

---

## 🎨 使用 EST-Admin-UI（管理后台前端）

### 简介

EST-Admin-UI 是基于 Vue3 + Element Plus 的现代化后台管理系统前端。

### 启动前端

#### Windows

```bash
cd est-admin-ui
run-ui.bat
```

#### Linux/Mac

```bash
cd est-admin-ui
chmod +x run-ui.sh
./run-ui.sh
```

或者使用 npm：

```bash
npm install
npm run dev
```

### 访问前端

启动成功后，访问：

- **前端页面**: http://localhost:3000

### 默认账号

- 用户名: `admin`
- 密码: `admin123`

### 更多信息

详细文档请参考：[est-admin-ui/README.md](est-admin-ui/README.md)

---

## 🔧 开发指南

### 添加新的后端模块

1. 在 `est-app/` 或 `est-modules/` 下创建新模块
2. 在父 `pom.xml` 中添加模块引用
3. 实现您的业务逻辑

### 自定义 EST-Monomer

编辑 `est-starter-monomer/src/main/java/ltd/idcu/est/starter/monomer/EstMonomerApplication.java`

### 自定义前端

编辑 `est-admin-ui/src/views/` 下的页面组件

---

## 📚 相关文档

- [EST 框架主文档](README.md)
- [EST-Monomer 文档](est-starter-monomer/README.md)
- [EST-Monomer 快速开始](est-starter-monomer/QUICKSTART.md)
- [EST-Admin 文档](est-app/est-admin/README.md)
- [EST-Admin-UI 文档](est-admin-ui/README.md)
- [开发路线图](dev-docs/roadmap.md)

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

## 📄 许可证

MIT License

---

## 祝您使用愉快！🚀
