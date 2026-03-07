@echo off
echo ========================================
echo Running EST Web Integration Tests
echo ========================================
echo.

cd /d "%~dp0"

call mvn compile test-compile

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Compilation failed!
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================
echo Starting integration tests...
echo ========================================
echo.

java -cp "target/classes;target/test-classes;..\..\est-test\est-test-api\target\classes;..\..\est-test\est-test-impl\target\classes;..\..\est-core\est-core-api\target\classes;..\..\est-core\est-core-impl\target\classes;..\..\est-utils\est-utils-common\target\classes;..\..\est-utils\est-utils-io\target\classes;..\..\est-utils\est-utils-format\est-utils-format-json\target\classes;..\..\est-collection\est-collection-api\target\classes;..\..\est-collection\est-collection-impl\target\classes;..\est-web-api\target\classes" ltd.idcu.est.web.integration.IntegrationTestsRunner

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo All integration tests passed!
    echo ========================================
) else (
    echo.
    echo ========================================
    echo Some integration tests failed!
    echo ========================================
)

pause
