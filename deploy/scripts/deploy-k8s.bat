@echo off
REM EST Framework Kubernetes Deployment Script for Windows
REM Usage: deploy-k8s.bat [namespace]

set K8S_DIR=%~dp0..\k8s
set DEFAULT_NAMESPACE=est

if "%1"=="" (
    set NAMESPACE=%DEFAULT_NAMESPACE%
) else (
    set NAMESPACE=%1
)

echo ========================================
echo Deploying EST Demo to Kubernetes
echo ========================================
echo Namespace: %NAMESPACE%
echo K8s Config Dir: %K8S_DIR%
echo.

REM Check if kubectl is available
kubectl version --client >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: kubectl not found or not configured
    exit /b 1
)

REM Deploy using Kustomize
echo Deploying with Kustomize...
kubectl apply -k %K8S_DIR%

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Deployment successful!
    echo ========================================
    echo.
    echo Checking deployment status...
    kubectl get pods -n %NAMESPACE%
    echo.
    echo To check logs:
    echo kubectl logs -f deployment/est-demo -n %NAMESPACE%
    echo.
    echo To port-forward:
    echo kubectl port-forward svc/est-demo-service 8080:80 -n %NAMESPACE%
    echo.
) else (
    echo.
    echo ========================================
    echo Deployment failed!
    echo ========================================
    exit /b 1
)
