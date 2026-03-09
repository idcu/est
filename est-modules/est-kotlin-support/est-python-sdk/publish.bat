@echo off
echo ========================================
echo EST Framework Python SDK - 发布脚本
echo ========================================
echo.

echo 警告：此脚本将上传包到PyPI！
echo.
echo 请确认：
echo 1. 你已经运行了 build.bat 并测试了构建
echo 2. 你有PyPI账号且有权限发布 est-framework
echo 3. 你已经配置了 ~/.pypirc 文件或使用环境变量
echo.
set /p confirm="确认继续发布？(yes/no): "

if /i not "%confirm%"=="yes" (
    echo 发布已取消。
    pause
    exit /b
)

echo.
echo 开始上传到PyPI...
twine upload dist/*
echo.

if %errorlevel% equ 0 (
    echo ========================================
    echo 发布成功！
    echo ========================================
) else (
    echo ========================================
    echo 发布失败，请检查错误信息
    echo ========================================
)

echo.
pause
