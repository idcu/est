
@echo off
chcp 65001 >nul
title EST Web Example - Basic

echo ========================================
echo   EST Web Example - Basic Web App
echo ========================================
echo.

echo Starting basic web example...
echo.
echo Application will start at http://localhost:8080
echo Press Ctrl+C to stop the application
echo.
echo ========================================
echo.

call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   Startup failed!
    echo   Please ensure you have run mvn clean install first
    echo ========================================
    echo.
    pause
)
