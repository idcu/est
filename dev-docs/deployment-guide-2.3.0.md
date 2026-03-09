# EST Framework 2.3.0 发布部署指南

**版本**: 2.3.0  
**部署日期**: 2026-06-30  
**状态**: 准备中

---

## 📋 目录
1. [部署前检查](#部署前检查)
2. [版本管理](#版本管理)
3. [构建发布包](#构建发布包)
4. [Maven中央仓库部署](#maven中央仓库部署)
5. [GitHub Release](#github-release)
6. [发布公告](#发布公告)
7. [发布后验证](#发布后验证)

---

## ✅ 部署前检查

### 检查清单
- [x] 所有代码已提交到主分支
- [x] 所有单元测试编写完成（38个测试用例）
- [x] 变更日志已更新（changelog.md）
- [x] 发布说明已准备（release-notes-2.3.0.md）
- [x] 开发路线图已更新（roadmap.md）
- [x] 质量验证报告已创建（quality-verification-2.3.0.md）
- [ ] 完整构建测试通过
- [ ] 所有单元测试运行通过
- [ ] 性能基准测试完成

### 环境要求
- JDK 21+
- Maven 3.8+
- GPG签名密钥（用于Maven中央仓库部署）
- GitHub访问权限
- Maven中央仓库账号（Sonatype OSSRH）

---

## 🔄 版本管理

### 当前版本
- **开发版本**: 2.3.0-SNAPSHOT → 2.3.0
- **下一版本**: 2.4.0-SNAPSHOT

### 版本号更新状态
- [x] 根pom.xml版本已更新为2.3.0
- [ ] 子模块pom.xml版本检查
- [ ] Git标签创建

### Git操作
```bash
# 创建发布标签
git tag -a v2.3.0 -m "Release version 2.3.0"

# 推送标签到远程仓库
git push origin v2.3.0

# 创建发布分支（可选）
git checkout -b release/2.3.0
git push origin release/2.3.0
```

---

## 📦 构建发布包

### Maven构建命令
```bash
# 清理并构建
mvn clean install

# 跳过测试快速构建
mvn clean install -DskipTests

# 构建源文件和Javadoc
mvn clean install source:jar javadoc:jar
```

### 构建验证
- [ ] 所有模块编译成功
- [ ] 无编译警告
- [ ] 测试通过（如果运行）
- [ ] Javadoc生成成功
- [ ] 源文件打包成功

### 发布包内容
- `est-2.3.0.pom` - 根POM文件
- `est-core-2.3.0.jar` - 核心模块
- `est-base-2.3.0.pom` - 基础模块POM
- `est-util-common-2.3.0.jar` - 通用工具模块
- 各模块的源文件jar包
- 各模块的Javadoc jar包

---

## 🚀 Maven中央仓库部署

### 前置条件
1. 已注册Sonatype OSSRH账号
2. 已配置GPG签名密钥
3. 已在Maven settings.xml中配置服务器信息

### settings.xml配置
```xml
<servers>
    <server>
        <id>ossrh</id>
        <username>your-username</username>
        <password>your-password</password>
    </server>
</servers>

<profiles>
    <profile>
        <id>ossrh</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <gpg.executable>gpg</gpg.executable>
            <gpg.passphrase>your-passphrase</gpg.passphrase>
        </properties>
    </profile>
</profiles>
```

### 部署命令
```bash
# 部署到Maven中央仓库
mvn clean deploy -P release

# 如果需要跳过GPG签名（仅用于测试）
mvn clean deploy -Dgpg.skip=true
```

### 部署验证
- [ ] 部署到Staging仓库成功
- [ ] Staging仓库验证通过
- [ ] Release到Maven中央仓库
- [ ] 在Maven中央仓库可搜索到

---

## 🌟 GitHub Release

### 创建GitHub Release步骤
1. 访问GitHub仓库Release页面
2. 点击 "Draft a new release"
3. 选择标签：`v2.3.0`
4. 填写发布标题：`EST Framework 2.3.0`
5. 编写发布说明（使用release-notes-2.3.0.md内容）
6. 上传构建的jar包（可选）
7. 点击 "Publish release"

### Release内容
- 发布标题
- 完整的发布说明
- 变更日志链接
- 升级指南
- 已知问题（如有）
- 贡献者感谢

### 发布检查清单
- [ ] Release已创建
- [ ] 标签正确关联
- [ ] 发布说明完整
- [ ] 附件上传完成（如有）
- [ ] Release已发布

---

## 📢 发布公告

### 发布渠道
1. GitHub Discussions
2. 项目README
3. 社交媒体（可选）
4. 邮件列表（可选）

### 公告内容模板
```markdown
# 🎉 EST Framework 2.3.0 正式发布！

我们很高兴地宣布 EST Framework 2.3.0 版本正式发布！

## 主要亮点
- ✨ AI功能全面增强
- 🚀 性能优化工具套件
- 📊 完整的性能基准测试
- ✅ 高测试覆盖率
- 📚 丰富的示例代码

## 快速开始
```xml
<dependency>
    <groupId>ltd.idcu</groupId>
    <artifactId>est-core</artifactId>
    <version>2.3.0</version>
</dependency>
```

## 详细信息
- [发布说明](dev-docs/release-notes-2.3.0.md)
- [变更日志](dev-docs/changelog.md)
- [开发路线图](dev-docs/roadmap.md)

感谢所有贡献者！💖

#ESTFramework #Java #Release
```

### 公告发布检查清单
- [ ] GitHub Discussions公告已发布
- [ ] README已更新
- [ ] 社交媒体已发布（如有）
- [ ] 邮件列表已发送（如有）

---

## 🔍 发布后验证

### 验证清单
- [ ] Maven中央仓库可下载
- [ ] GitHub Release可访问
- [ ] 发布说明链接正确
- [ ] 文档链接可访问
- [ ] 示例代码可运行

### 快速验证测试
```bash
# 创建测试项目
mvn archetype:generate -DgroupId=test -DartifactId=est-test -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

# 进入项目目录
cd est-test

# 添加EST依赖到pom.xml
# 编辑pom.xml添加est依赖

# 编译测试
mvn clean compile
```

---

## 📝 发布总结

### 已完成
- ✅ 版本准备完成
- ✅ 质量验证完成
- ✅ 文档准备完成
- ✅ 发布计划制定
- ✅ 部署指南编写

### 待完成
- [ ] 完整构建测试
- [ ] Git标签创建
- [ ] Maven中央仓库部署
- [ ] GitHub Release创建
- [ ] 发布公告发布
- [ ] 发布后验证

### 下一步
1. 执行完整构建和测试
2. 创建Git标签
3. 部署到Maven中央仓库
4. 创建GitHub Release
5. 发布公告
6. 验证发布结果

---

## 📞 支持

如有问题，请通过以下方式联系：
- GitHub Issues: https://github.com/idcu/est/issues
- GitHub Discussions: https://github.com/idcu/est/discussions

---

**部署负责人**: EST Team  
**最后更新**: 2026-03-09
