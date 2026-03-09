# TypeScript SDK 发布指南

本文档介绍如何将 EST Framework TypeScript SDK 发布到 npm。

## 前置条件

在发布之前，请确保：

1. **npm 账号**
   - 访问 https://www.npmjs.com/ 注册账号
   - 验证邮箱
   - 申请 `est-framework-sdk` 包名（如果尚未拥有）

2. **开发环境**
   - Node.js 16+
   - npm 或 yarn 或 pnpm

3. **配置文件（可选）**
   - 登录 npm：`npm login`
   - 或者使用 npm token 进行认证

## 发布步骤

### 1. 准备发布

确保所有代码已经提交，并且版本号已更新：

- 检查 `package.json` 中的 `version` 字段
- 更新 README.md 和文档
- 运行所有测试确保通过

```bash
# 安装依赖
npm install

# 运行测试
npm test

# 运行测试覆盖率
npm run test:coverage

# 构建
npm run build
```

### 2. 检查 package.json

确保 `package.json` 配置正确：

```json
{
  "name": "est-framework-sdk",
  "version": "2.4.0",
  "description": "EST Framework TypeScript/JavaScript SDK",
  "main": "dist/index.js",
  "types": "dist/index.d.ts",
  "files": [
    "dist",
    "README.md",
    "LICENSE"
  ],
  "keywords": [
    "est",
    "framework",
    "sdk",
    "typescript",
    "javascript"
  ],
  "author": "EST Team",
  "license": "Apache-2.0",
  "repository": {
    "type": "git",
    "url": "https://github.com/idcu/est.git"
  },
  "homepage": "https://github.com/idcu/est#readme",
  "bugs": {
    "url": "https://github.com/idcu/est/issues"
  }
}
```

### 3. 构建包

```bash
# 清理旧的构建文件
rm -rf dist

# 构建
npm run build
```

### 4. 测试包

在发布之前，先在本地测试：

```bash
# 创建一个临时目录
mkdir -p /tmp/test-est-sdk
cd /tmp/test-est-sdk

# 初始化 npm 项目
npm init -y

# 安装本地包
npm install /path/to/est-typescript-sdk

# 创建测试文件 test.js
cat > test.js << 'EOF'
const { EstClient } = require('est-framework-sdk');

const client = new EstClient({ baseUrl: 'http://localhost:8080' });
console.log('Client created successfully!');
EOF

# 运行测试
node test.js
```

### 5. 登录 npm

```bash
# 登录 npm（如果尚未登录）
npm login

# 或者使用 npm token
npm config set //registry.npmjs.org/:_authToken YOUR_NPM_TOKEN
```

### 6. 发布到 npm

```bash
# 发布到正式 npm
npm publish

# 如果是预发布版本
npm publish --tag beta

# 如果是私有包（需要付费账号）
npm publish --access private
```

### 7. 验证发布

发布完成后，验证：

1. **访问 npm 页面**
   - 访问 https://www.npmjs.com/package/est-framework-sdk

2. **从 npm 安装测试**
   ```bash
   # 创建新目录
   mkdir -p /tmp/test-install
   cd /tmp/test-install
   npm init -y
   
   # 安装
   npm install est-framework-sdk
   
   # 测试
   node -e "const { EstClient } = require('est-framework-sdk'); console.log('Import successful!');"
   ```

3. **运行示例代码测试功能是否正常**

## 版本管理

### 版本号规范

遵循语义化版本规范（Semantic Versioning）：
- `MAJOR.MINOR.PATCH`
  - MAJOR：不兼容的 API 修改
  - MINOR：向下兼容的功能性新增
  - PATCH：向下兼容的问题修正

### 更新版本号

```bash
# 使用 npm version 命令更新版本
npm version patch  # 1.0.0 -> 1.0.1
npm version minor  # 1.0.0 -> 1.1.0
npm version major  # 1.0.0 -> 2.0.0

# 或者手动修改 package.json 中的 version 字段
```

### 预发布版本

对于预发布版本：

```bash
# Alpha 版本
npm version 1.0.0-alpha.1

# Beta 版本
npm version 1.0.0-beta.1

# RC 版本
npm version 1.0.0-rc.1

# 发布预发布版本（使用 --tag）
npm publish --tag beta
```

### 标签管理

```bash
# 安装特定标签的版本
npm install est-framework-sdk@beta
npm install est-framework-sdk@latest
npm install est-framework-sdk@1.0.0
```

## 常见问题

### 问题：包名已被占用

如果 `est-framework-sdk` 包名已被占用，可以：
1. 使用不同的包名，如 `@your-username/est-framework-sdk`（作用域包）
2. 联系包所有者请求转让
3. 使用组织账号发布

### 问题：发布失败

检查：
1. 网络连接
2. npm 账号权限
3. 是否已登录 npm
4. 包名是否可用
5. 版本号是否已存在（不能重复发布相同版本）

### 问题：安装后导入失败

检查：
1. `package.json` 中的 `main` 和 `types` 字段是否正确
2. 是否正确运行了 `npm run build`
3. `dist` 目录是否包含在 `files` 数组中
4. TypeScript 配置是否正确

### 问题：TypeScript 类型不工作

检查：
1. `package.json` 中的 `types` 字段是否指向正确的 `.d.ts` 文件
2. `tsconfig.json` 中的 `declaration` 是否设置为 `true`
3. 构建是否生成了类型声明文件

## 自动化发布（可选）

可以使用 GitHub Actions 自动化发布流程：

1. 在 GitHub Secrets 中添加 npm token
2. 创建 `.github/workflows/publish.yml` 工作流
3. 推送到 main 分支或打标签时自动发布

示例工作流：
```yaml
name: Publish TypeScript Package

on:
  release:
    types: [created]

jobs:
  build-and-publish:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Node.js
      uses: actions/setup-node@v2
      with:
        node-version: '18'
        registry-url: 'https://registry.npmjs.org'
    - name: Install dependencies
      run: npm ci
      working-directory: est-modules/est-kotlin-support/est-typescript-sdk
    - name: Run tests
      run: npm test
      working-directory: est-modules/est-kotlin-support/est-typescript-sdk
    - name: Build
      run: npm run build
      working-directory: est-modules/est-kotlin-support/est-typescript-sdk
    - name: Publish
      run: npm publish
      working-directory: est-modules/est-kotlin-support/est-typescript-sdk
      env:
        NODE_AUTH_TOKEN: ${{ secrets.NPM_TOKEN }}
```

## 相关资源

- [npm 官方文档](https://docs.npmjs.com/)
- [TypeScript 官方文档](https://www.typescriptlang.org/docs/)
- [语义化版本规范](https://semver.org/lang/zh-CN/)
- [npm 包发布指南](https://docs.npmjs.com/creating-and-publishing-unscoped-public-packages)

---

**最后更新：2026-03-10**
