@echo off
echo ========================================
echo   EST Admin UI 启动脚本
echo ========================================
echo.

cd /d "%~dp0"

echo 正在检查 Node.js 和 npm...
node --version
if %errorlevel% neq 0 (
    echo.
    echo 错误：未找到 Node.js，请先安装 Node.js！
    pause
    exit /b 1
)

npm --version
echo.

if not exist "node_modules" (
    echo 正在安装依赖...
    call npm install
    if %errorlevel% neq 0 (
        echo.
        echo 依赖安装失败！
        pause
        exit /b 1
    )
    echo.
)

echo 正在启动开发服务器...
echo.
echo 前端访问地址：http://localhost:3000
echo 后端 API 地址：http://localhost:8080
echo.
echo 请确保后端服务已启动！
echo.

call npm run dev

pause
