
@echo off
chcp 65001 >nul
title EST Microservices Example

echo ========================================
echo   EST Microservices Example
echo ========================================
echo.
echo Please select the service to start:
echo.
echo [1] User Service (port 8081)
echo [2] Order Service (port 8082)
echo [3] API Gateway (port 8080)
echo [4] Start all services (requires 3 terminal windows)
echo [0] Return
echo.
set /p choice=Please enter option [0-4]:

if "%choice%"=="1" goto user_service
if "%choice%"=="2" goto order_service
if "%choice%"=="3" goto gateway
if "%choice%"=="4" goto all_services
if "%choice%"=="0" goto end
goto menu

:user_service
echo.
echo Starting user service (port 8081)...
echo.
cd est-examples-microservices-user
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
cd ..
goto end

:order_service
echo.
echo Starting order service (port 8082)...
echo.
cd est-examples-microservices-order
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
cd ..
goto end

:gateway
echo.
echo Starting API Gateway (port 8080)...
echo.
cd est-examples-microservices-gateway
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
cd ..
goto end

:all_services
echo.
echo ========================================
echo   Start All Microservices
echo ========================================
echo.
echo Please run in 3 different terminal windows in order:
echo.
echo 1. Terminal 1 - User Service:
echo    cd est-examples-microservices-user
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
echo.
echo 2. Terminal 2 - Order Service:
echo    cd est-examples-microservices-order
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
echo.
echo 3. Terminal 3 - API Gateway:
echo    cd est-examples-microservices-gateway
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
echo.
echo ========================================
echo.
pause
goto end

:end
echo.
echo Thank you for using EST Microservices Example!
echo.
