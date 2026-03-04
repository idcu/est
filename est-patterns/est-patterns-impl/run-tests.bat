@echo off
echo Building and running est-patterns tests...
cd /d "%~dp0"

if not exist "target\test-classes" mkdir target\test-classes

echo Compiling test classes...
javac -encoding UTF-8 -d target\test-classes -cp "target\classes;..\..\est-test\est-test-api\target\classes;..\..\est-test\est-test-impl\target\classes;..\est-patterns-api\target\classes;..\..\est-core\est-core-api\target\classes" src\test\java\ltd\idcu\est\patterns\impl\*.java src\test\java\ltd\idcu\est\patterns\impl\creational\*.java src\test\java\ltd\idcu\est\patterns\impl\behavioral\*.java src\test\java\ltd\idcu\est\patterns\impl\structural\*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    exit /b %errorlevel%
)

echo Running tests...
java -cp "target\classes;target\test-classes;..\..\est-test\est-test-api\target\classes;..\..\est-test\est-test-impl\target\classes;..\est-patterns-api\target\classes;..\..\est-core\est-core-api\target\classes" ltd.idcu.est.patterns.impl.PatternsTestsRunner
