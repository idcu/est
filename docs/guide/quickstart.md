# 快速开始指南：5分钟上手 EST

欢迎来到 EST 框架的快速开始指南！本指南将帮助你在 5 分钟内快速上手 EST 框架。

## 环境要求

在开始之前，请确保你的开发环境满足以下要求：

- **JDK**: 21 或更高版本
- **Maven**: 3.6 或更高版本
- **操作系统**: Windows、macOS 或 Linux
- **IDE**: IntelliJ IDEA、Eclipse 或 VS Code（推荐 IntelliJ IDEA）

### 检查环境

打开终端，运行以下命令检查环境：

```bash
# 检查 Java 版本
java -version

# 检查 Maven 版本
mvn -version
```

如果版本不符合要求，请先安装或升级。

## 第一步：创建项目（1分钟）

### 方式一：使用 EST 脚手架（推荐）

```bash
# 克隆 EST 项目
git clone https://github.com/idcu/est.git
cd est

# 或者直接使用示例项目
cd est-examples/est-examples-basic
```

### 方式二：手动创建 Maven 项目

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-est-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

cd my-est-app
```

## 第二步：配置依赖（1分钟）

编辑 `pom.xml`，添加 EST 框架依赖：

```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <est.version>2.0.0</est.version>
</properties>

<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web</artifactId>
        <version>${est.version}</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.1.0</version>
            <configuration>
                <mainClass>com.example.App</mainClass>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 第三步：编写代码（1分钟）

创建 `src/main/java/com/example/App.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class App {
    public static void main(String[] args) {
        WebApplication app = Web.create("我的第一个 EST 应用", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, EST!"));
        
        app.get("/api/greet/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.json(Map.of(
                "message", "Hello, " + name + "!",
                "timestamp", System.currentTimeMillis()
            ));
        });
        
        app.run(8080);
        System.out.println("应用已启动: http://localhost:8080");
    }
}
```

## 第四步：运行应用（1分钟）

### 编译项目

```bash
mvn clean compile
```

### 运行应用

```bash
mvn exec:java
```

你应该会看到类似以下的输出：

```
应用已启动: http://localhost:8080
```

## 第五步：测试应用（1分钟）

打开浏览器或使用 curl 访问以下 URL：

### 1. 测试首页

访问：http://localhost:8080

你应该会看到：
```
Hello, EST!
```

### 2. 测试 API

访问：http://localhost:8080/api/greet/张三

你应该会看到类似以下的 JSON 响应：
```json
{
  "message": "Hello, 张三!",
  "timestamp": 1709876543210
}
```

### 使用 curl 测试

```bash
# 测试首页
curl http://localhost:8080

# 测试 API
curl http://localhost:8080/api/greet/张三
```

## 恭喜！🎉

你已经成功创建并运行了你的第一个 EST 应用！

## 下一步

现在你已经掌握了基础，可以继续学习：

- 📖 **教程系列**: 查看 [教程](../tutorials/README.md) 深入学习
- 📚 **API 文档**: 查看 [API 文档](../api/README.md) 了解完整 API
- 💡 **最佳实践**: 查看 [最佳实践](../best-practices/README.md) 学习最佳实践
- 🔧 **示例代码**: 查看 [示例代码](../../est-examples/README.md) 学习更多用法

## 常见问题

### Q: 端口 8080 被占用怎么办？

A: 修改 `app.run(8080)` 中的端口号，例如改为 `app.run(8081)`。

### Q: 如何热重载代码？

A: 使用 IDE 的热重载功能，或者使用 `est-hotreload` 模块。

### Q: 如何添加数据库支持？

A: 添加 `est-data-jdbc` 依赖，参考 [数据库教程](../tutorials/data/jdbc.md)。

### Q: 如何添加 AI 功能？

A: 添加 `est-ai-assistant` 依赖，参考 [AI 教程](../tutorials/ai/assistant-quickstart.md)。

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
