@echo off
REM EST Framework Kubernetes Undeployment Script for Windows
REM Usage: undeploy-k8s.bat [namespace]

set K8S_DIR=%~dp0..\k8s
set DEFAULT_NAMESPACE=est

if "%1"=="" (
    set NAMESPACE=%DEFAULT_NAMESPACE%
) else (
    set NAMESPACE=%1
)

echo ========================================
echo Undeploying EST Demo from Kubernetes
echo ========================================
echo Namespace: %NAMESPACE%
echo.

REM Confirmation
echo WARNING: This will delete all EST Demo resources from namespace %NAMESPACE%
set /p CONFIRM="Are you sure? (y/N): "

if /i not "%CONFIRM%"=="y" (
    echo Operation cancelled.
    exit /b 0
)

REM Undeploy using Kustomize
echo Undeploying with Kustomize...
kubectl delete -k %K8S_DIR%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Undeployment successful!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Undeployment may have completed with some resources already deleted.
    echo ========================================
)
