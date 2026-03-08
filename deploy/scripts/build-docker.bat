@echo off
REM EST Framework Docker Build Script for Windows
REM Usage: build-docker.bat [version]

set PROJECT_ROOT=%~dp0..\..
set DOCKERFILE_PATH=%~dp0..\docker\Dockerfile
set IMAGE_NAME=est-demo
set DEFAULT_VERSION=2.0.0

if "%1"=="" (
    set VERSION=%DEFAULT_VERSION%
) else (
    set VERSION=%1
)

echo ========================================
echo Building EST Demo Docker Image
echo ========================================
echo Image Name: %IMAGE_NAME%
echo Version: %VERSION%
echo Project Root: %PROJECT_ROOT%
echo.

REM Build the Docker image
docker build -f %DOCKERFILE_PATH% -t %IMAGE_NAME%:%VERSION% %PROJECT_ROOT%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo Image: %IMAGE_NAME%:%VERSION%
    echo.
    echo To run the container:
    echo docker run -d -p 8080:8080 --name est-demo %IMAGE_NAME%:%VERSION%
    echo.
) else (
    echo.
    echo ========================================
    echo Build failed!
    echo ========================================
    exit /b 1
)
