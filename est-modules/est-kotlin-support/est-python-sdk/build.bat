@echo off
echo ========================================
echo EST Framework Python SDK - 构建脚本
echo ========================================
echo.

echo [1/3] 清理旧的构建文件...
if exist dist rmdir /s /q dist
if exist build rmdir /s /q build
if exist est_framework.egg-info rmdir /s /q est_framework.egg-info
echo 清理完成！
echo.

echo [2/3] 安装构建工具...
pip install --upgrade setuptools wheel twine
echo 构建工具安装完成！
echo.

echo [3/3] 构建包...
python setup.py sdist bdist_wheel
echo.

echo ========================================
echo 构建完成！
echo 输出文件位于 dist/ 目录
echo ========================================
echo.
echo 下一步：
echo 1. 测试包：pip install dist\est_framework-2.4.0-py3-none-any.whl
echo 2. 上传到PyPI：twine upload dist/*
echo.
pause
