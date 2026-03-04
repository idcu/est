$cp = "target\classes"
Get-ChildItem -Path "E:\repos\maven\ltd\idcu" -Filter "*.jar" -Recurse | ForEach-Object { 
    $cp += ";$($_.FullName)" 
}

Write-Host "Running CacheExample..."
java -cp $cp ltd.idcu.est.examples.features.CacheExample

Write-Host "`nRunning LoggingExample..."
java -cp $cp ltd.idcu.est.examples.features.LoggingExample

Write-Host "`nRunning EventExample..."
java -cp $cp ltd.idcu.est.examples.features.EventExample

Write-Host "`nRunning SecurityExample..."
java -cp $cp ltd.idcu.est.examples.features.SecurityExample

Write-Host "`nRunning MonitorExample..."
java -cp $cp ltd.idcu.est.examples.features.MonitorExample

Write-Host "`nRunning SchedulerExample..."
java -cp $cp ltd.idcu.est.examples.features.SchedulerExample
