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

---

## 第一步：安装 JDK 21

JDK（Java Development Kit）是 Java 开发的核心，EST 框架需要 JDK 21 或更高版本。

### 1.1 下载 JDK

我们推荐使用 **Eclipse Adoptium**（免费、开源、稳定）：

👉 **下载地址**：https://adoptium.net/

### 1.2 安装 JDK

#### Windows 系统

1. 双击下载的 `.msi` 安装文件
2. 点击 "Next" 继续
3. 选择安装路径（建议使用默认路径）
4. 点击 "Next" → "Install" 开始安装
5. 等待安装完成，点击 "Finish"

---

## 快速开始使用 EST 框架

### 方法一：在 IDE 中打开项目

1. 打开 IntelliJ IDEA
2. 选择 `File` → `Open`
3. 选择克隆的 est 项目根目录
4. 等待 IDE 导入项目（右下角可以看到进度）
5. 找到示例文件：`est-examples/est-examples-web/src/main/java/ltd/idcu/est/examples/web/BasicWebAppExample.java`
6. 右键点击类名，选择 `Run 'BasicWebAppExample.main()'`

---

**文档版本**: 2.0  
**最后更新**: 2026-03-06  
**适合人群**: 初学者、零基础小白、有经验的开发者

