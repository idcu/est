
@echo off
chcp 65001 &gt;nul
title EST Framework - 快速开始

echo ========================================
echo   EST 2.0.0 框架 - 快速开始
echo ========================================
echo.

:menu
echo 请选择操作：
echo.
echo [1] 构建整个项目
echo [2] 运行 EST Demo 演示应用
echo [3] 运行 Web 基础示例
echo [4] 运行微服务示例 - 用户服务
echo [5] 运行微服务示例 - 订单服务
echo [6] 运行微服务示例 - API网关
echo [7] 清理构建
echo [0] 退出
echo.
set /p choice=请输入选项 [0-7]:

if "%choice%"=="1" goto build
if "%choice%"=="2" goto demo
if "%choice%"=="3" goto web_example
if "%choice%"=="4" goto user_service
if "%choice%"=="5" goto order_service
if "%choice%"=="6" goto gateway
if "%choice%"=="7" goto clean
if "%choice%"=="0" goto end
goto menu

:build
echo.
echo [1/1] 正在构建 EST 框架...
echo.
call mvn clean install -DskipTests
if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   构建成功！
    echo ========================================
) else (
    echo.
    echo ========================================
    echo   构建失败！请检查错误信息。
    echo ========================================
)
echo.
pause
goto menu

:demo
echo.
echo [2/2] 正在启动 EST Demo...
echo.
cd est-demo
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
cd ..
goto menu

:web_example
echo.
echo [3/3] 正在启动 Web 基础示例...
echo.
cd est-examples\est-examples-web
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.web.BasicWebAppExample"
cd ..\..
goto menu

:user_service
echo.
echo [4/4] 正在启动用户服务 (端口 8081)...
echo.
cd est-examples\est-examples-microservices\est-examples-microservices-user
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
cd ..\..\..
goto menu

:order_service
echo.
echo [5/5] 正在启动订单服务 (端口 8082)...
echo.
cd est-examples\est-examples-microservices\est-examples-microservices-order
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
cd ..\..\..
goto menu

:gateway
echo.
echo [6/6] 正在启动 API网关 (端口 8080)...
echo.
cd est-examples\est-examples-microservices\est-examples-microservices-gateway
call mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
cd ..\..\..
goto menu

:clean
echo.
echo [7/7] 正在清理构建...
echo.
call mvn clean
echo.
echo ========================================
echo   清理完成！
echo ========================================
echo.
pause
goto menu

:end
echo.
echo ========================================
echo   感谢使用 EST 框架！
echo ========================================
echo.
timeout /t 2 &gt;nul

