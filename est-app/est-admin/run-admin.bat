@echo off
echo ========================================
echo   EST Admin Console 启动脚本
echo ========================================
echo.

cd /d "%~dp0"

echo 正在编译项目...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo 编译失败！请检查错误信息。
    pause
    exit /b 1
)

echo.
echo 编译成功！
echo.
echo 正在启动 EST Admin Console...
echo.

cd est-admin-impl
java -jar target\est-admin-impl-2.0.0.jar

pause
