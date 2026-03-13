@echo off
chcp 65001 >nul
echo ============================================================
echo EST AI Examples - Run Script
echo ============================================================
echo.
echo Please select the example to run:
echo.
echo [1] Main                    - View all examples (Recommended)
echo [2] AiQuickStartExample     - AI Quick Start
echo [3] ComprehensiveAiExample  - Comprehensive AI Example
echo [4] StorageExample          - Storage System Example
echo [5] ConfigExample           - Config Management Example
echo [6] CodeGeneratorExample    - Code Generator Example
echo [7] PromptTemplateExample   - Prompt Template Example
echo [8] LlmIntegrationExample   - LLM Integration Example
echo [9] AdvancedAiExample       - Advanced AI Example
echo [10] MidTermFeaturesExample - Mid-Term Features Example
echo [11] LongTermFeaturesExample - Long-Term Features Example
echo [12] AiAssistantWebExample  - AI Assistant Web App
echo [0] Exit
echo.
set /p choice=Please enter option [0-12]:

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

echo Invalid option, please re-run the script
goto end

:main
echo.
echo Running Main...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.Main"
goto end

:quickstart
echo.
echo Running AiQuickStartExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiQuickStartExample"
goto end

:comprehensive
echo.
echo Running ComprehensiveAiExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ComprehensiveAiExample"
goto end

:storage
echo.
echo Running StorageExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.StorageExample"
goto end

:config
echo.
echo Running ConfigExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.ConfigExample"
goto end

:codegen
echo.
echo Running CodeGeneratorExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.CodeGeneratorExample"
goto end

:template
echo.
echo Running PromptTemplateExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.PromptTemplateExample"
goto end

:llm
echo.
echo Running LlmIntegrationExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LlmIntegrationExample"
goto end

:advanced
echo.
echo Running AdvancedAiExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AdvancedAiExample"
goto end

:midterm
echo.
echo Running MidTermFeaturesExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.MidTermFeaturesExample"
goto end

:longterm
echo.
echo Running LongTermFeaturesExample...
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.LongTermFeaturesExample"
goto end

:web
echo.
echo Running AiAssistantWebExample...
echo.
echo Web app will start at http://localhost:8080
echo Press Ctrl+C to stop the service
echo.
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.ai.AiAssistantWebExample"
goto end

:end
echo.
echo ============================================================
echo Example run completed
echo ============================================================
pause
