
@echo off
chcp 65001 &gt;nul
title EST Basic Example

echo ========================================
echo   EST Basic Example
echo ========================================
echo.

echo Running basic example...
echo.
echo ========================================
echo.

call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   Run failed!
    echo   Please ensure you have run: mvn clean install
    echo ========================================
    echo.
    pause
)
