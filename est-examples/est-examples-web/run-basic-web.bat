
@echo off
chcp 65001 &gt;nul
title EST Web Example - Basic

echo ========================================
echo   EST Web 示例 - 基础 Web 应用
echo ========================================
echo.

echo 正在启动基础 Web 示例...
echo.
echo 应用将在 http://localhost:8080 启动
echo 按 Ctrl+C 可以停止应用
echo.
echo ========================================
echo.

call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   启动失败！
    echo   请确保已先运行: mvn clean install
    echo ========================================
    echo.
    pause
)

