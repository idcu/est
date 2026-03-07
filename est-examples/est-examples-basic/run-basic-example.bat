
@echo off
chcp 65001 &gt;nul
title EST Basic Example

echo ========================================
echo   EST 基础示例
echo ========================================
echo.

echo 正在运行基础示例...
echo.
echo ========================================
echo.

call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   运行失败！
    echo   请确保已先运行: mvn clean install
    echo ========================================
    echo.
    pause
)

