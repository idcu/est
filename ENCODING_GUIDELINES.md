# 编码规范文档

## 1. 统一编码标准
- 所有项目文件必须使用 UTF-8 编码
- 避免使用其他编码格式（如 GBK、ISO-8859-1 等）

## 2. 构建工具配置
- Maven 项目：在 pom.xml 中设置编码为 UTF-8
- Gradle 项目：在 build.gradle 中设置编码为 UTF-8

## 3. Git 配置
- core.autocrlf = true
- i18n.commitencoding = utf-8
- i18n.logoutputencoding = utf-8
- gui.encoding = utf-8

## 4. IDE 设置
- IntelliJ IDEA：Settings → Editor → File Encodings → Global Encoding = UTF-8
- Eclipse：Preferences → General → Workspace → Text file encoding = UTF-8
- VS Code：Settings → Files: Encoding = utf8

## 5. 代码审查
- 代码审查过程中检查文件编码
- 确保所有新文件使用 UTF-8 编码

## 6. 自动化检测
- 使用脚本定期检查文件编码一致性
- 集成编码检查到 CI/CD 流程

## 7. 最佳实践
- 避免在不同编码环境间切换
- 当处理外部文件时，明确指定编码
- 定期备份项目文件
