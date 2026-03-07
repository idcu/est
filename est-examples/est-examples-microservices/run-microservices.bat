
@echo off
chcp 65001 &gt;nul
title EST Microservices Example

echo ========================================
echo   EST 微服务示例
echo ========================================
echo.
echo 请选择要启动的服务：
echo.
echo [1] 用户服务 (端口 8081)
echo [2] 订单服务 (端口 8082)
echo [3] API网关 (端口 8080)
echo [4] 启动所有服务（需要3个终端窗口）
echo [0] 返回
echo.
set /p choice=请输入选项 [0-4]:

if "%choice%"=="1" goto user_service
if "%choice%"=="2" goto order_service
if "%choice%"=="3" goto gateway
if "%choice%"=="4" goto all_services
if "%choice%"=="0" goto end
goto menu

:user_service
echo.
echo 正在启动用户服务 (端口 8081)...
echo.
cd est-examples-microservices-user
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
cd ..
goto end

:order_service
echo.
echo 正在启动订单服务 (端口 8082)...
echo.
cd est-examples-microservices-order
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
cd ..
goto end

:gateway
echo.
echo 正在启动 API网关 (端口 8080)...
echo.
cd est-examples-microservices-gateway
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
cd ..
goto end

:all_services
echo.
echo ========================================
echo   启动所有微服务
echo ========================================
echo.
echo 请按顺序在3个不同的终端窗口中运行：
echo.
echo 1. 终端1 - 用户服务:
echo    cd est-examples-microservices-user
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
echo.
echo 2. 终端2 - 订单服务:
echo    cd est-examples-microservices-order
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
echo.
echo 3. 终端3 - API网关:
echo    cd est-examples-microservices-gateway
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
echo.
echo ========================================
echo.
pause
goto end

:end
echo.
echo 感谢使用 EST 微服务示例！
echo.

