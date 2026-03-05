# 环境准备与安装指南

本文档详细介绍 EST 框架的开发环境搭建、项目构建以及运行环境部署的完整流程，适合从零基础小白到有经验的开发者。

---

## 目录

- [准备工作概述](#准备工作概述)
- [第一步：安装 JDK 21](#第一步安装-jdk-21)
- [第二步：安装 Maven](#第二步安装-maven)
- [第三步：安装 Git（可选但推荐）](#第三步安装-git可选但推荐)
- [第四步：安装 IDE（推荐 IntelliJ IDEA）](#第四步安装-ide推荐-intellij-idea)
- [第五步：获取并构建 EST 框架](#第五步获取并构建-est-框架)
- [环境验证清单](#环境验证清单)
- [快速开始使用 EST 框架](#快速开始使用-est-框架)
- [常见问题 FAQ](#常见问题-faq)
- [获取帮助](#获取帮助)

---

## 准备工作概述

在开始使用 EST 框架之前，你需要准备以下环境：

| 软件 | 是否必须 | 版本要求 | 用途 |
|------|----------|----------|------|
| **JDK** | ✅ 必须 | 21 或更高 | Java 开发工具包，核心必需 |
| **Maven** | ✅ 必须 | 3.6 或更高 | 项目构建和依赖管理 |
| **Git** | ⭐ 推荐 | 任意 | 代码版本控制和克隆项目 |
| **IDE** | ⭐ 推荐 | IntelliJ IDEA / Eclipse / VS Code | 代码编辑器 |

### 硬件要求检查

在开始安装之前，请确保你的电脑满足以下最低要求：

| 硬件 | 最低配置 | 推荐配置 |
|------|----------|----------|
| **CPU** | 双核处理器 | 四核及以上 |
| **内存** | 4GB RAM | 8GB+ RAM |
| **磁盘空间** | 5GB 可用空间 | 10GB+ 可用空间 |
| **操作系统** | Windows 10+ / macOS 10.15+ / Linux | 最新版本 |

**如何查看电脑配置？**

**Windows：**
1. 右键点击"此电脑" → "属性"
2. 查看处理器和内存信息

**macOS：**
1. 点击左上角苹果图标 → "关于本机"
2. 查看处理器和内存信息

**Linux：**
```bash
# 查看 CPU 信息
cat /proc/cpuinfo

# 查看内存信息
free -h
```

**预计时间**：30-60 分钟（取决于网络速度）

---

## 第一步：安装 JDK 21

JDK（Java Development Kit）是 Java 开发的核心，EST 框架需要 JDK 21 或更高版本。

### 1.1 下载 JDK

我们推荐使用 **Eclipse Adoptium**（免费、开源、稳定）：

👉 **下载地址**：https://adoptium.net/

**下载步骤：**
1. 打开上面的网址
2. 选择 "Temurin 21 (LTS)" 版本
3. 选择你的操作系统（Windows / macOS / Linux）
4. 选择架构（x64 或 aarch64，根据你的电脑选择）
5. 点击下载安装包

### 1.2 安装 JDK

#### Windows 系统

1. 双击下载的 `.msi` 安装文件
2. 点击 "Next" 继续
3. 选择安装路径（建议使用默认路径）
4. 点击 "Next" → "Install" 开始安装
5. 等待安装完成，点击 "Finish"

#### macOS 系统

1. 双击下载的 `.pkg` 安装文件
2. 点击 "继续" → "安装"
3. 输入管理员密码确认
4. 等待安装完成，点击 "关闭"

或使用 Homebrew（推荐）：
```bash
# 使用 Homebrew 安装
brew install openjdk@21
```

#### Linux 系统（Ubuntu/Debian）

```bash
# 更新软件包列表
sudo apt update

# 安装 JDK 21
sudo apt install -y openjdk-21-jdk
```

### 1.3 配置环境变量（重要！）

#### Windows 系统

1. 右键点击 "此电脑" → "属性"
2. 点击 "高级系统设置"
3. 点击 "环境变量" 按钮
4. 在 "系统变量" 区域：
   - 点击 "新建"
   - 变量名：`JAVA_HOME`
   - 变量值：JDK 安装路径（例如：`C:\Program Files\Eclipse Adoptium\jdk-21.0.1-hotspot`）
   - 点击 "确定"
5. 编辑 "Path" 变量：
   - 在 "系统变量" 中找到 "Path"，点击 "编辑"
   - 点击 "新建"，输入：`%JAVA_HOME%\bin`
   - 点击 "确定" 保存所有设置

#### macOS 系统

打开终端（Terminal），执行以下命令：

```bash
# 编辑 zsh 配置文件（macOS Catalina 及以上）
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc

# 使配置生效
source ~/.zshrc
```

#### Linux 系统

```bash
# 编辑 bash 配置文件
echo 'export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc

# 使配置生效
source ~/.bashrc
```

### 1.4 验证 JDK 安装

打开新的终端窗口（重要！必须是新窗口），执行以下命令：

```bash
java -version
```

**预期输出示例：**
```
openjdk version "21.0.1" 2023-10-17 LTS
OpenJDK Runtime Environment Temurin-21.0.1+12 (build 21.0.1+12-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.1+12 (build 21.0.1+12-LTS, mixed mode, sharing)
```

如果看到类似上面的输出，说明 JDK 安装成功！🎉

---

## 第二步：安装 Maven

Maven 是 Java 项目的构建工具，负责管理依赖、编译代码、打包项目等。

### 2.1 下载 Maven

👉 **下载地址**：https://maven.apache.org/download.cgi

**下载步骤：**
1. 打开上面的网址
2. 找到 "Files" 部分
3. 下载 `apache-maven-3.9.x-bin.zip`（Windows）或 `apache-maven-3.9.x-bin.tar.gz`（macOS/Linux）

### 2.2 安装 Maven

#### Windows 系统

1. 将下载的 zip 文件解压到你想安装的位置（建议：`C:\Program Files\Apache\maven`）
2. 解压后会看到 `apache-maven-3.9.x` 文件夹

#### macOS 系统（使用 Homebrew，推荐）

```bash
# 如果没有安装 Homebrew，先安装
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 使用 Homebrew 安装 Maven
brew install maven
```

#### Linux 系统（Ubuntu/Debian）

```bash
sudo apt install -y maven
```

### 2.3 配置环境变量（仅手动安装需要）

#### Windows 系统

1. 右键点击 "此电脑" → "属性" → "高级系统设置" → "环境变量"
2. 在 "系统变量" 区域：
   - 点击 "新建"
   - 变量名：`MAVEN_HOME`
   - 变量值：Maven 解压路径（例如：`C:\Program Files\Apache\maven\apache-maven-3.9.5`）
   - 点击 "确定"
3. 编辑 "Path" 变量：
   - 在 "系统变量" 中找到 "Path"，点击 "编辑"
   - 点击 "新建"，输入：`%MAVEN_HOME%\bin`
   - 点击 "确定" 保存所有设置

### 2.4 验证 Maven 安装

打开新的终端窗口，执行以下命令：

```bash
mvn -version
```

**预期输出示例：**
```
Apache Maven 3.9.5 (...)
Maven home: C:\Program Files\Apache\maven\apache-maven-3.9.5
Java version: 21.0.1, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.1-hotspot
Default locale: zh_CN, platform encoding: UTF-8
OS name: "Windows 11", version: "10.0", arch: "amd64"
```

如果看到类似输出，说明 Maven 安装成功！🎉

---

## 第三步：安装 Git（可选但推荐）

Git 是版本控制工具，用于下载和管理 EST 框架的源代码。

### 3.1 下载 Git

👉 **下载地址**：https://git-scm.com/downloads

### 3.2 安装 Git

#### Windows 系统

1. 双击下载的安装文件
2. 一直点击 "Next" 使用默认设置即可
3. 最后点击 "Install" 安装

#### macOS 系统

```bash
# 使用 Homebrew 安装
brew install git
```

#### Linux 系统

```bash
sudo apt install -y git
```

### 3.3 验证 Git 安装

```bash
git --version
```

**预期输出：**
```
git version 2.42.0
```

---

## 第四步：安装 IDE（推荐 IntelliJ IDEA）

IDE（集成开发环境）可以让你更方便地编写和调试代码。我们推荐 IntelliJ IDEA。

### 4.1 下载 IntelliJ IDEA

👉 **下载地址**：https://www.jetbrains.com/idea/download/

- **Community Edition**：免费，功能足够使用
- **Ultimate Edition**：付费，功能更强大（学生可以免费申请）

### 4.2 安装 IntelliJ IDEA

1. 下载后运行安装程序
2. 按照提示完成安装
3. 首次启动时，选择主题、插件等（使用默认即可）

### 4.3 配置 IntelliJ IDEA

#### 配置 JDK

1. 打开 IntelliJ IDEA
2. 点击 `File` → `Project Structure`（或按 `Ctrl+Alt+Shift+S`）
3. 在左侧选择 `Project`
4. 设置 `SDK`：
   - 如果下拉框中没有 JDK 21，点击 `Add SDK` → `Download JDK`
   - 选择版本 21，点击 `Download`
5. 设置 `Language level` 为 `21 - Pattern matching for switch`
6. 点击 `OK` 保存

#### 配置 Maven

1. 点击 `File` → `Settings`（或按 `Ctrl+Alt+S`）
2. 在左侧导航到 `Build, Execution, Deployment` → `Build Tools` → `Maven`
3. 确认 `Maven home path` 配置正确
4. 点击 `OK` 保存

---

## 第五步：获取并构建 EST 框架

### 方法一：从 GitHub 克隆（推荐）

```bash
# 克隆项目
git clone https://github.com/idcu/est.git
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

# 只构建指定模块（比如 web 模块）
mvn clean install -pl est-web

# 构建 web 模块及其依赖
mvn clean install -pl est-web -am
```

这个过程可能需要几分钟，请耐心等待。当你看到 `BUILD SUCCESS` 时，说明构建成功了！

---

## 环境验证清单

完成以上步骤后，请逐项检查：

- [ ] 硬件要求满足
- [ ] JDK 21 已安装并配置
- [ ] `java -version` 输出显示版本 21+
- [ ] Maven 已安装并配置
- [ ] `mvn -version` 输出版本信息
- [ ] Git 已安装（可选）
- [ ] IntelliJ IDEA 已安装（可选）
- [ ] EST 框架已构建成功

**恭喜你！环境准备工作已全部完成！** 🎊

---

## 快速开始使用 EST 框架

### 方法一：在 IDE 中打开项目

1. 打开 IntelliJ IDEA
2. 选择 `File` → `Open`
3. 选择克隆的 est 项目根目录
4. 等待 IDE 导入项目（右下角可以看到进度）
5. 找到示例文件：`est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java`
6. 右键点击类名，选择 `Run 'BasicWebAppExample.main()'`

### 方法二：使用 Maven 运行

```bash
# 1. 进入示例模块目录
cd est-examples/est-examples-web

# 2. 编译并运行
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
```

### 验证运行成功

打开浏览器访问：http://localhost:8080

你应该能看到 "Hello, World!" 页面！

---

## 常见问题 FAQ

### Q1: 执行 `java -version` 报错"不是内部或外部命令"

**A:** 这说明环境变量没有配置正确，请检查：
1. 是否打开了新的终端窗口（旧窗口不会加载新环境变量）
2. `JAVA_HOME` 环境变量是否设置正确
3. `Path` 变量中是否包含 `%JAVA_HOME%\bin`

### Q2: Maven 下载依赖非常慢

**A:** 配置国内 Maven 镜像源，编辑 `~/.m2/settings.xml`（Windows 是 `C:\Users\你的用户名\.m2\settings.xml`）：

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
</settings>
```

### Q3: IntelliJ IDEA 中项目有红色波浪线

**A:** 这通常是因为依赖没有下载或索引未完成：
1. 等待右下角的进度条完成
2. 右键点击 `pom.xml` → `Maven` → `Reload Project`
3. 点击 `File` → `Invalidate Caches...` → `Invalidate and Restart`

### Q4: 运行示例时提示"端口 8080 已被占用"

**A:** 你可以：
1. 找到并关闭占用 8080 端口的程序
2. 或者修改示例代码中的端口号（例如改为 8081）

**Windows 查看端口占用：**
```bash
netstat -ano | findstr :8080
```

**macOS/Linux 查看端口占用：**
```bash
lsof -i :8080
```

### Q5: 编译时提示"找不到符号"或"类不存在"

**A:** 确保先执行了 Maven 构建：
```bash
mvn clean install -DskipTests
```

### Q6: 编译错误，提示不支持的类文件版本

**A:** 确保 `pom.xml` 中的这两个配置正确：
```xml
<maven.compiler.source>21</maven.compiler.source>
<maven.compiler.target>21</maven.compiler.target>
```

### Q7: 找不到 est-* 依赖

**A:** 确保你已经在 EST 项目根目录执行了 `mvn clean install`，把 EST 框架安装到了本地 Maven 仓库。

---

## 获取帮助

如果你在环境准备过程中遇到问题：

1. 先查看本文档的 [常见问题 FAQ](#常见问题-faq) 部分
2. 搜索项目 Issues：https://github.com/idcu/est/issues
3. 提交新的 Issue：https://github.com/idcu/est/issues/new

---

## 下一步

现在你的开发环境已经配置好了！接下来：

- 阅读 [快速开始指南](./getting-started.md) 来创建你的第一个应用
- 查看 [入门教程](../tutorials/beginner/) 深入学习核心概念
- 探索 [示例代码](../../est-examples/) 了解更多用法

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**适合人群**: 初学者、零基础小白、有经验的开发者
