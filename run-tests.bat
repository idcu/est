@echo off
echo ========================================
echo EST Framework Test Runner
echo ========================================
echo.

echo Compiling project first...
call mvn clean compile -DskipTests
if %errorlevel% neq 0 (
    echo Compilation failed!
    exit /b 1
)
echo.

echo ========================================
echo Compilation successful!
echo ========================================
echo.
echo Now you can run specific tests:
echo.
echo 1. To run core container tests:
echo    mvn exec:java -Dexec.mainClass="ltd.idcu.est.test.TestLauncher" -pl est-base/est-test/est-test-impl
echo.
echo 2. To run est-admin:
echo    cd est-app\est-admin
echo    call run-admin.bat
echo.
echo ========================================
