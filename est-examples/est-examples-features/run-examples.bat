@echo off
setlocal enabledelayedexpansion

set CP=target\classes
for /r "E:\repos\maven\ltd\idcu" %%f in (*.jar) do (
    set CP=!CP!;%%f
)

echo Running CacheExample...
java -cp "%CP%" ltd.idcu.est.examples.features.CacheExample

echo.
echo Running LoggingExample...
java -cp "%CP%" ltd.idcu.est.examples.features.LoggingExample

echo.
echo Running EventExample...
java -cp "%CP%" ltd.idcu.est.examples.features.EventExample

echo.
echo Running SecurityExample...
java -cp "%CP%" ltd.idcu.est.examples.features.SecurityExample

echo.
echo Running MonitorExample...
java -cp "%CP%" ltd.idcu.est.examples.features.MonitorExample

echo.
echo Running SchedulerExample...
java -cp "%CP%" ltd.idcu.est.examples.features.SchedulerExample

endlocal
