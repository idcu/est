# 安装与配置

本指南详细介绍如何安装和配置 EST 框架。

## 环境要求

### JDK 安装

EST 框架需要 JDK 21 或更高版本。

**检查 Java 版本：**
```bash
java -version
```

**安装 JDK 21：**
- Windows: 下载并安装 [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/) 或 [OpenJDK 21](https://adoptium.net/)
- macOS: `brew install openjdk@21`
- Linux: `sudo apt install openjdk-21-jdk`

### Maven 安装

**检查 Maven 版本：**
```bash
mvn -version
```

**安装 Maven：**
- 下载：https://maven.apache.org/download.cgi
- 解压并配置环境变量

## 获取 EST 框架

### 方法一：从源码构建

```bash
# 克隆仓库
git clone https://github.com/idcu/est.git
cd est

# 构建所有模块
mvn clean install

# 跳过测试（可选）
mvn clean install -DskipTests
```

### 方法二：使用 Maven 依赖

在你的项目 `pom.xml` 中添加：

```xml
<repositories>
    <repository>
        <id>est-repo</id>
        <url>https://repo.example.com/maven2</url>
    </repository>
</repositories>

<dependencies>
    <!-- 根据需要选择模块 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-core-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 模块选择

EST 采用递进式模块设计，你可以根据需要选择模块：

| 模块 | 说明 | 依赖 |
|------|------|------|
| est-core-api | 核心接口 | 无 |
| est-core-impl | 核心实现 | est-core-api |
| est-web-impl | Web 框架 | est-core-impl |
| est-features-cache-memory | 内存缓存 | est-core-impl |
| est-features-cache-file | 文件缓存 | est-core-impl |
| est-features-logging-console | 控制台日志 | est-core-impl |
| ... | ... | ... |

**最小配置（仅核心）：**
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-impl</artifactId>
    <version>1.3.0-SNAPSHOT</version>
</dependency>
```

**完整 Web 应用配置：**
```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-impl</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-logging-console</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

## 配置文件

### application.properties

创建 `src/main/resources/application.properties`：

```properties
# 应用配置
app.name=My EST App
app.version=1.0.0

# 服务器配置
server.port=8080
server.host=0.0.0.0

# 日志配置
logging.level=INFO
logging.file=logs/app.log

# 缓存配置
cache.maxSize=1000
cache.expireAfterWrite=3600
```

### 加载配置

```java
EstApplication app = DefaultEstApplication.create();
app.getConfiguration().load("application.properties");
```

## IDE 配置

### IntelliJ IDEA

1. 导入项目：File → Open → 选择 `pom.xml`
2. 确保 SDK 设置为 JDK 21：File → Project Structure → Project
3. 配置 Maven：File → Settings → Build, Execution, Deployment → Build Tools → Maven

### Eclipse

1. 导入项目：File → Import → Existing Maven Projects
2. 选择项目根目录
3. 配置 JRE：右键项目 → Properties → Java Build Path → Libraries

## 验证安装

运行以下命令验证安装：

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 生成 Javadoc
mvn javadoc:aggregate
```

## 常见问题

### Q: 提示找不到 JDK 21？
A: 确保 JAVA_HOME 环境变量指向 JDK 21 安装目录。

### Q: Maven 依赖下载失败？
A: 检查网络连接，或配置 Maven 镜像源。

### Q: 编译错误？
A: 确保使用 JDK 21 编译，检查 `maven.compiler.source` 和 `maven.compiler.target` 配置。

## 下一步

- [快速开始](./getting-started.md)
- [第一个 EST 应用](../tutorials/beginner/01-first-app.md)
