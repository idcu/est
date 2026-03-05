# 🛠️ 安装与配置指南

本指南将详细介绍如何安装和配置 EST 框架的开发环境。

---

## 📋 环境要求

在开始之前，我们需要准备以下软件：

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| JDK | 21 或更高 | Java 开发工具包，必须 |
| Maven | 3.6 或更高 | 项目构建工具，必须 |
| IDE（可选） | 任意 | 推荐 IntelliJ IDEA 或 Eclipse |

---

## ☕ 第一步：安装 JDK 21

JDK（Java Development Kit）是 Java 开发的核心工具包。

### 检查是否已安装 JDK

打开终端（Windows 用 PowerShell 或 CMD，Mac/Linux 用 Terminal），输入：

```bash
java -version
```

如果看到类似以下输出，说明已经安装了 JDK：

```
java version "21.0.1" 2023-10-17 LTS
Java(TM) SE Runtime Environment (build 21.0.1+12-LTS-29)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.1+12-LTS-29, mixed mode, sharing)
```

如果版本低于 21 或者没有安装，请继续下面的步骤。

### 下载并安装 JDK 21

#### Windows 用户

1. 访问 Oracle 官网下载：https://www.oracle.com/java/technologies/downloads/#java21
   或者使用 OpenJDK：https://adoptium.net/

2. 下载 Windows x64 版本的安装包（.msi 或 .exe 文件）

3. 双击安装包，按照提示完成安装

4. 配置环境变量：
   - 右键"此电脑" → "属性" → "高级系统设置" → "环境变量"
   - 在"系统变量"中点击"新建"：
     - 变量名：`JAVA_HOME`
     - 变量值：JDK 安装路径（例如：`C:\Program Files\Java\jdk-21`）
   - 编辑"Path"变量，添加：`%JAVA_HOME%\bin`

5. 重新打开终端，验证安装：
   ```bash
   java -version
   ```

#### macOS 用户

使用 Homebrew 安装最简单：

```bash
brew install openjdk@21
```

安装完成后，配置环境变量：

```bash
echo 'export JAVA_HOME=/opt/homebrew/opt/openjdk@21' >> ~/.zprofile
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zprofile
source ~/.zprofile
```

验证安装：

```bash
java -version
```

#### Linux 用户（Ubuntu/Debian）

```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

配置环境变量：

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

验证安装：

```bash
java -version
```

---

## 📦 第二步：安装 Maven

Maven 是一个项目构建和依赖管理工具。

### 检查是否已安装 Maven

在终端中输入：

```bash
mvn -version
```

如果看到类似以下输出，说明已经安装了 Maven：

```
Apache Maven 3.9.5 (57804ffe001d72f44f1e09410549296938991da0)
Maven home: /opt/maven
Java version: 21.0.1, vendor: Oracle Corporation
Default locale: zh_CN, platform encoding: UTF-8
```

如果版本低于 3.6 或者没有安装，请继续下面的步骤。

### 下载并安装 Maven

#### Windows 用户

1. 访问 Maven 官网：https://maven.apache.org/download.cgi

2. 下载 `apache-maven-3.9.x-bin.zip`（x 是最新版本号）

3. 解压到某个目录，例如：`C:\Program Files\Apache\maven`

4. 配置环境变量：
   - 右键"此电脑" → "属性" → "高级系统设置" → "环境变量"
   - 在"系统变量"中点击"新建"：
     - 变量名：`MAVEN_HOME`
     - 变量值：Maven 解压路径（例如：`C:\Program Files\Apache\maven`）
   - 编辑"Path"变量，添加：`%MAVEN_HOME%\bin`

5. 重新打开终端，验证安装：
   ```bash
   mvn -version
   ```

#### macOS 用户

使用 Homebrew 安装：

```bash
brew install maven
```

验证安装：

```bash
mvn -version
```

#### Linux 用户（Ubuntu/Debian）

```bash
sudo apt update
sudo apt install maven
```

验证安装：

```bash
mvn -version
```

---

## 🚀 第三步：获取并构建 EST 框架

### 方法一：从 GitHub 克隆（推荐）

```bash
# 克隆项目
git clone https://github.com/idcu/est.git

# 进入项目目录
cd est
```

### 方法二：下载 ZIP 包

1. 访问 https://github.com/idcu/est
2. 点击绿色的 "Code" 按钮
3. 选择 "Download ZIP"
4. 解压到你想放的目录

### 构建 EST 框架

在项目根目录下执行：

```bash
# 构建所有模块（包含测试）
mvn clean install

# 或者跳过测试（更快）
mvn clean install -DskipTests
```

这个过程可能需要几分钟，请耐心等待。当你看到 `BUILD SUCCESS` 时，说明构建成功了！

---

## 💻 第四步：配置 IDE（可选但推荐）

### IntelliJ IDEA 配置

IntelliJ IDEA 是最流行的 Java IDE 之一，对 EST 框架支持很好。

#### 1. 导入项目

1. 打开 IntelliJ IDEA
2. 选择 "File" → "Open"
3. 选择 EST 项目根目录下的 `pom.xml` 文件
4. 点击 "Open as Project"
5. 等待 IDEA 索引完成

#### 2. 配置 JDK

1. 打开 "File" → "Project Structure"（快捷键：Ctrl+Alt+Shift+S）
2. 在 "Project" 标签页中：
   - SDK：选择 JDK 21（如果没有，点击 "Add SDK" → "Download JDK"）
   - Language level：选择 "21 - Pattern matching for switch"
3. 点击 "OK"

#### 3. 配置 Maven

1. 打开 "File" → "Settings"（快捷键：Ctrl+Alt+S）
2. 导航到 "Build, Execution, Deployment" → "Build Tools" → "Maven"
3. 确认以下设置：
   - Maven home directory：你的 Maven 安装路径
   - User settings file：你的 Maven settings.xml 路径
   - Local repository：你的本地仓库路径
4. 点击 "OK"

### Eclipse 配置

#### 1. 导入项目

1. 打开 Eclipse
2. 选择 "File" → "Import"
3. 选择 "Existing Maven Projects"
4. 点击 "Next"
5. 选择 EST 项目根目录
6. 点击 "Finish"

#### 2. 配置 JRE

1. 右键点击项目 → "Properties"
2. 选择 "Java Build Path"
3. 选择 "Libraries" 标签页
4. 点击 "Add Library" → "JRE System Library"
5. 选择 "Workspace default JRE" 或选择 JDK 21
6. 点击 "Finish" 和 "OK"

---

## ✅ 第五步：验证安装

让我们创建一个简单的测试项目来验证一切是否正常工作。

### 1. 创建测试项目

创建一个新目录，然后在里面创建 `pom.xml`：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>test-est</artifactId>
    <version>1.0.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>est-web-impl</artifactId>
            <version>1.3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

### 2. 创建测试类

在 `src/main/java/com/example/` 目录下创建 `Test.java`：

```java
package com.example;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class Test {
    public static void main(String[] args) {
        WebApplication app = Web.create("测试应用", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("安装成功！EST 框架运行正常！");
        });
        
        System.out.println("测试服务器启动中...");
        app.run(8080);
    }
}
```

### 3. 运行测试

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.Test"
```

打开浏览器访问 http://localhost:8080，如果看到 "安装成功！EST 框架运行正常！"，恭喜你！环境配置完成了！

---

## 🔧 常见问题解答

### Q: 提示找不到 JDK 21？

**A:** 
1. 检查 JAVA_HOME 环境变量是否正确设置
2. 检查 Path 变量中是否包含 %JAVA_HOME%\bin
3. 重新打开终端让环境变量生效

### Q: Maven 依赖下载失败？

**A:**
1. 检查网络连接
2. 配置 Maven 镜像源（推荐使用阿里云镜像）
3. 在 `settings.xml` 中添加：
   ```xml
   <mirrors>
     <mirror>
       <id>aliyun</id>
       <mirrorOf>central</mirrorOf>
       <name>Aliyun Maven</name>
       <url>https://maven.aliyun.com/repository/public</url>
     </mirror>
   </mirrors>
   ```

### Q: 编译错误，提示不支持的类文件版本？

**A:**
确保 `pom.xml` 中的这两个配置正确：
```xml
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

### Q: 找不到 est-* 依赖？

**A:**
确保你已经在 EST 项目根目录执行了 `mvn clean install`，把 EST 框架安装到了本地 Maven 仓库。

---

## 📚 下一步

现在你的开发环境已经配置好了！接下来：

- 阅读 [快速开始](./getting-started.md) 来创建你的第一个应用
- 查看 [入门教程](../tutorials/beginner/) 深入学习核心概念
- 探索 [示例代码](../../est-examples/) 了解更多用法

---

**祝你使用 EST 框架愉快！** 🎉
