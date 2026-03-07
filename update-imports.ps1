# PowerShell script to update import statements from ltd.idcu.est.features. to ltd.idcu.est.

Get-ChildItem -Path .\est-examples -Filter "*.java" -Recurse | ForEach-Object {
    $filePath = $_.FullName
    $content = Get-Content $filePath -Raw
    $originalContent = $content
    
    # Replace all ltd.idcu.est.features. with ltd.idcu.est.
    $content = $content -replace "ltd\.idcu\.est\.features\.", "ltd.idcu.est."
    
    if ($content -ne $originalContent) {
        Set-Content -Path $filePath -Value $content -NoNewline
        Write-Host "Updated imports in: $filePath"
    }
}

Write-Host "`nImport update completed!"
