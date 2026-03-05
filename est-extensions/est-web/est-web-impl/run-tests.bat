@echo off
echo === Running EST Web Tests ===
echo.

set CLASSPATH=..\est-web-api\target\classes
set CLASSPATH=%CLASSPATH%;.\target\classes
set CLASSPATH=%CLASSPATH%;.\target\test-classes
set CLASSPATH=%CLASSPATH%;..\..\est-test\est-test-api\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-test\est-test-impl\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-core\est-core-api\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-utils\est-utils-io\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-utils\est-utils-format\est-utils-format-json\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-collection\est-collection-api\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-features\est-features-monitor\est-features-monitor-api\target\classes

echo Classpath:
echo %CLASSPATH%
echo.

java -cp %CLASSPATH% ltd.idcu.est.web.WebTestsRunner

echo.
echo === Test Run Complete ===
