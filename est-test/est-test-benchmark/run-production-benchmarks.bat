@echo off
echo Starting EST Production Benchmarks
echo =================================
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Error: Maven is not installed or not in PATH
    exit /b 1
)

REM Build the project
echo Building project...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build failed!
    exit /b 1
)

echo.
echo Build successful!
echo.

REM Run benchmarks
echo Running production benchmarks...
echo.

REM Check if the jar file exists
if not exist "target\est-benchmarks.jar" (
    echo Error: est-benchmarks.jar not found in target directory
    exit /b 1
)

REM Run only production benchmarks
echo Running ProductionBenchmark only...
java -jar target\est-benchmarks.jar ltd.idcu.est.test.benchmark.ProductionBenchmark -rf csv -rff production-benchmarks-results.csv

echo.
echo Production benchmarks completed!
echo Results saved to production-benchmarks-results.csv
