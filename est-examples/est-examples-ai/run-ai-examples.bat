@echo off
chcp 65001 >nul
echo ============================================================
echo EST AI 示例 - 运行脚本
echo ============================================================
echo.
echo 请选择要运行的示例：
echo.
echo [1] Main                    - 查看所有示例（推荐）
echo [2] AiQuickStartExample     - AI 快速开始
echo [3] ComprehensiveAiExample  - 综合 AI 示例
echo [4] StorageExample          - 存储系统示例
echo [5] ConfigExample           - 配置管理示例
echo [6] CodeGeneratorExample    - 代码生成器示例
echo [7] PromptTemplateExample   - 提示词模板示例
echo [8] LlmIntegrationExample   - LLM 集成示例
echo [9] AdvancedAiExample       - 高级 AI 示例
echo [10] MidTermFeaturesExample - 中期功能示例
echo [11] LongTermFeaturesExample - 长期功能示例
echo [12] AiAssistantWebExample  - AI 助手 Web 应用
echo [0] 退出
echo.
set /p choice=请输入选项 [0-12]:

if "%choice%"=="1" goto main
if "%choice%"=="2" goto quickstart
if "%choice%"=="3" goto comprehensive
if "%choice%"=="4" goto storage
if "%choice%"=="5" goto config
if "%choice%"=="6" goto codegen
if "%choice%"=="7" goto template
if "%choice%"=="8" goto llm
if "%choice%"=="9" goto advanced
if "%choice%"=="10" goto midterm
if "%choice%"=="11" goto longterm
if "%choice%"=="12" goto web
if "%choice%"=="0" goto end

echo 无效选项，请重新运行脚本
goto end

:main
echo.
echo 运行 Main...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.Main"
goto end

:quickstart
echo.
echo 运行 AiQuickStartExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
goto end

:comprehensive
echo.
echo 运行 ComprehensiveAiExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ComprehensiveAiExample"
goto end

:storage
echo.
echo 运行 StorageExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.StorageExample"
goto end

:config
echo.
echo 运行 ConfigExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ConfigExample"
goto end

:codegen
echo.
echo 运行 CodeGeneratorExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.CodeGeneratorExample"
goto end

:template
echo.
echo 运行 PromptTemplateExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.PromptTemplateExample"
goto end

:llm
echo.
echo 运行 LlmIntegrationExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LlmIntegrationExample"
goto end

:advanced
echo.
echo 运行 AdvancedAiExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AdvancedAiExample"
goto end

:midterm
echo.
echo 运行 MidTermFeaturesExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.MidTermFeaturesExample"
goto end

:longterm
echo.
echo 运行 LongTermFeaturesExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LongTermFeaturesExample"
goto end

:web
echo.
echo 运行 AiAssistantWebExample...
echo.
echo Web 应用将在 http://localhost:8080 启动
echo 按 Ctrl+C 停止服务器
echo.
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiAssistantWebExample"
goto end

:end
echo.
echo ============================================================
echo 示例运行完成
echo ============================================================
pause
