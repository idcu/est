# EST Monomer 快速开始指南

本指南将帮助您在 5 分钟内启动并运行 EST Monomer 单体应用。

## 前置条件

在开始之前，请确保您已经安装了：

- **JDK 21 或更高版本**
- **Maven 3.6 或更高版本**

## 第一步：构建项目

首先，在项目根目录执行 Maven 构建：

```bash
cd d:\os\proj\java\est2.0
mvn clean install -DskipTests
```

这将编译整个 EST 框架并安装到本地 Maven 仓库。

## 第二步：启动 EST Monomer

进入 est-starter-monomer 目录并运行：

### Windows

```bash
cd est-starter-monomer
run.bat
```

### Linux/Mac

```bash
cd est-starter-monomer
chmod +x run.sh
./run.sh
```

或者直接使用 Maven：

```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.starter.monomer.EstMonomerApplication"
```

## 第三步：访问应用

启动成功后，您将看到类似以下的输出：

```
========================================
  EST Monomer Application
  单体应用启动器
  Version: 2.3.0-SNAPSHOT
========================================

Server starting on port 8080...
Available endpoints:
  - GET  /                          - 首页
  - GET  /api/hello                 - 问候接口
  - GET  /api/system/health         - 健康检查
  - GET  /api/system/info           - 系统信息
  - GET  /api/monitor/jvm           - JVM指标
  - GET  /api/monitor/system        - 系统指标

Open your browser and visit: http://localhost:8080
Press Ctrl+C to stop the server.
```

现在，打开浏览器访问：

**http://localhost:8080**

您将看到 EST Monomer 的欢迎页面。

## 第四步：测试 API

让我们测试一些 API 端点：

### 1. 健康检查

```bash
curl http://localhost:8080/api/system/health
```

响应示例：
```json
{
  "status": "UP",
  "timestamp": 1234567890123,
  "version": "2.3.0-SNAPSHOT"
}
```

### 2. 系统信息

```bash
curl http://localhost:8080/api/system/info
```

### 3. 问候接口

```bash
curl http://localhost:8080/api/hello?name=EST
```

### 4. JVM 指标

```bash
curl http://localhost:8080/api/monitor/jvm
```

## 第五步：修改和扩展

现在您可以开始修改 `EstMonomerApplication.java` 来添加自己的功能。

### 添加新的路由

在 `app.routes()` 中添加新的路由：

```java
router.get("/api/my-endpoint", (req, res) -> {
    res.json(Map.of("message", "Hello from my endpoint!"));
});
```

### 使用中间件

添加全局中间件：

```java
app.use((req, res, next) -> {
    System.out.println("Request: " + req.getMethod() + " " + req.getPath());
    next.handle();
});
```

## 常见问题

### Q: 端口 8080 被占用怎么办？

A: 修改 `EstMonomerApplication.java` 中的端口号：

```java
app.run(8081); // 改为其他端口
```

### Q: 如何添加数据库支持？

A: 参考 est-data 模块，添加 JDBC 或 MongoDB 依赖，然后使用对应的 Repository 实现。

### Q: 如何集成前端？

A: 查看 [est-admin-ui](../est-admin-ui/README.md) 了解如何集成 Vue3 + Element Plus 前端。

### Q: 构建失败怎么办？

A: 确保：
1. JDK 版本是 21+
2. Maven 版本是 3.6+
3. 执行了 `mvn clean install` 在根目录

## 下一步

现在您已经成功启动了 EST Monomer，接下来可以：

- 📚 阅读 [README.md](README.md) 了解更多功能
- 🎨 集成 [est-admin-ui](../est-admin-ui/) 前端
- 🔧 探索 [EST 模块](../est-modules/) 扩展功能
- 📝 参考 [est-demo](../est-demo/) 查看更多示例

祝您使用愉快！🚀
