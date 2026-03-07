# GitHub Maven 配置指南

## 1. GitHub Personal Access Token (PAT) 生成

1. 访问 GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. 点击 "Generate new token" → "Generate new token (classic)"
3. 设置 token 名称（例如：est-maven-deploy）
4. 选择以下权限范围：
   - `repo` (完全仓库访问权限)
   - `write:packages` (上传包权限)
   - `read:packages` (下载包权限)
5. 点击 "Generate token"
6. **重要**：复制生成的 token，只显示一次！

## 2. 配置 Maven settings.xml

### 方法一：全局配置（推荐）

将以下内容复制到 `~/.m2/settings.xml`（Windows 路径：`%USERPROFILE%\.m2\settings.xml`）：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  
  <servers>
    <server>
      <id>github</id>
      <username>您的GitHub用户名</username>
      <password>您的GitHub Personal Access Token</password>
    </server>
  </servers>
  
</settings>
```

### 方法二：项目本地配置

将项目中的 `settings.xml.template` 复制为 `settings.xml`，然后修改其中的用户名和 token。

## 3. 发布到 GitHub Packages

配置完成后，运行以下命令发布：

```bash
mvn clean deploy
```

或者使用本地 settings.xml：

```bash
mvn clean deploy -s settings.xml
```

## 4. 使用已发布的包

在其他项目的 pom.xml 中添加：

```xml
<repositories>
  <repository>
    <id>github</id>
    <name>GitHub EST Packages</name>
    <url>https://maven.pkg.github.com/idcu/est</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core-api</artifactId>
    <version>2.0.0</version>
  </dependency>
</dependencies>
```
