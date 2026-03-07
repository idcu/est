# PowerShell script to update dependencies from est-features-* to est-*

$excludePaths = @(
    "est-features\",
    "est-modules\"
)

Get-ChildItem -Path . -Filter "pom.xml" -Recurse | ForEach-Object {
    $filePath = $_.FullName
    $shouldExclude = $false
    
    foreach ($excludePath in $excludePaths) {
        if ($filePath -like "*$excludePath*") {
            $shouldExclude = $true
            break
        }
    }
    
    if (-not $shouldExclude) {
        $content = Get-Content $filePath -Raw
        $originalContent = $content
        
        # Replace all est-features-* with est-*
        $content = $content -replace "est-features-", "est-"
        # Replace est-microservices-gateway with est-gateway
        $content = $content -replace "est-microservices-gateway", "est-gateway"
        
        if ($content -ne $originalContent) {
            Set-Content -Path $filePath -Value $content -NoNewline
            Write-Host "Updated: $filePath"
        }
    }
}

Write-Host "`nDependency update completed!"
