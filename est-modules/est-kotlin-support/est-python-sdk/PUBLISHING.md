# Python SDK 发布指南

本文档介绍如何将 EST Framework Python SDK 发布到 PyPI。

## 前置条件

在发布之前，请确保：

1. **PyPI 账号
   - 访问 https://pypi.org/ 注册账号
   - 验证邮箱
   - 申请 `est-framework` 包名（如果尚未拥有）

2. **测试环境
   - Python 3.8+
   - pip
   - 安装构建工具：`pip install --upgrade setuptools wheel twine`

3. **配置文件（可选）
   - 创建 `~/.pypirc` 文件：
     ```ini
     [pypi]
     username = __token__
     password = pypi-xxx...
     ```

## 发布步骤

### 1. 准备发布

确保所有代码已经提交，并且版本号已更新：

- 检查 `setup.py` 中的 `version` 字段
- 更新 CHANGELOG（如果有）
- 运行所有测试确保通过

### 2. 构建包

**Windows:**
```bash
build.bat
```

**Linux/macOS:**
```bash
chmod +x build.sh
./build.sh
```

或者手动执行：
```bash
# 清理旧的构建文件
rm -rf dist build est_framework.egg-info

# 构建源分发包和wheel包
python setup.py sdist bdist_wheel
```

### 3. 测试包

在发布之前，先在本地测试安装：

```bash
# 安装本地构建的包
pip install dist/est_framework-2.4.0-py3-none-any.whl

# 测试导入
python -c "from est_sdk import EstClient; print('Import successful!')"
```

### 4. 上传到 PyPI

**Windows:**
```bash
publish.bat
```

**Linux/macOS:**
```bash
chmod +x publish.sh
./publish.sh
```

或者手动使用 twine 上传：
```bash
# 上传到测试PyPI（可选，用于测试）
twine upload --repository testpypi dist/*

# 上传到正式PyPI
twine upload dist/*
```

上传时会提示输入 PyPI 用户名和密码。

## 验证发布

发布完成后，验证：

1. 访问 PyPI 页面：https://pypi.org/project/est-framework/

2. 从 PyPI 安装测试：
```bash
pip install est-framework
```

3. 运行示例代码测试功能是否正常

## 版本管理

### 版本号规范

遵循语义化版本规范（Semantic Versioning）：
- `MAJOR.MINOR.PATCH`
  - MAJOR：不兼容的 API 修改
  - MINOR：向下兼容的功能性新增
  - PATCH：向下兼容的问题修正

### 更新版本号

1. 修改 `setup.py` 中的 `version` 字段
2. 更新 README.md 和文档
3. 添加 CHANGELOG（如果有）
4. 提交版本更改
5. 打标签：`git tag v2.4.0`
6. 推送标签：`git push origin v2.4.0`

## 常见问题

### 问题：包名已被占用

如果 `est-framework` 包名已被占用，可以：
1. 使用不同的包名，如 `est-framework-sdk`
2. 联系包所有者请求转让
3. 使用组织账号发布

### 问题：上传失败

检查：
1. 网络连接
2. PyPI 账号权限
3. 用户名和密码是否正确
4. 是否使用 API token（推荐使用 token 而不是密码）

### 问题：安装后导入失败

检查：
1. 包名是否正确（注意是 `est-framework`，导入是 `import est_sdk`）
2. Python 版本是否满足要求（>=3.8）
3. 依赖是否正确安装

## 自动化发布（可选）

可以使用 GitHub Actions 自动化发布流程：

1. 在 GitHub Secrets 中添加 PyPI API Token
2. 创建 `.github/workflows/publish.yml` 工作流
3. 推送到 main 分支或打标签时自动发布

示例工作流：
```yaml
name: Publish Python Package

on:
  release:
    types: [created]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Python
      uses: actions/setup-python@v2
      with:
        python-version: '3.8'
    - name: Install dependencies
      run: |
        python -m pip install --upgrade pip
        pip install setuptools wheel twine
    - name: Build and publish
      env:
        TWINE_USERNAME: __token__
        TWINE_PASSWORD: ${{ secrets.PYPI_API_TOKEN }}
      run: |
        cd est-modules/est-kotlin-support/est-python-sdk
        python setup.py sdist bdist_wheel
        twine upload dist/*
```

## 相关资源

- [PyPI 官方文档](https://packaging.python.org/)
- [setuptools 文档](https://setuptools.pypa.io/)
- [twine 文档](https://twine.readthedocs.io/)
- [语义化版本规范](https://semver.org/lang/zh-CN/)

---

**最后更新：2026-03-10
