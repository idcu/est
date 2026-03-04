@echo off
echo === Running EST Core Tests ===
echo.

set CLASSPATH=..\est-core-api\target\classes
set CLASSPATH=%CLASSPATH%;.\target\classes
set CLASSPATH=%CLASSPATH%;.\target\test-classes
set CLASSPATH=%CLASSPATH%;..\..\est-test\est-test-api\target\classes
set CLASSPATH=%CLASSPATH%;..\..\est-test\est-test-impl\target\classes

java -cp %CLASSPATH% ltd.idcu.est.core.impl.CoreTestsRunner

echo.
echo === Test Run Complete ===
