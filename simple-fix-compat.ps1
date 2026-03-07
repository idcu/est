# Simple fix for compatibility modules
Write-Host "=== Fixing Compatibility Modules ===" -ForegroundColor Green

# Get all pom files in est-features
$pomFiles = Get-ChildItem -Path "est-features" -Recurse -Filter "pom.xml"

foreach ($pomFile in $pomFiles) {
    Write-Host "Processing: $($pomFile.FullName)"
    
    # Read file content
    $content = Get-Content $pomFile.FullName -Raw
    
    # Fix the version issue
    $content = $content -replace '<version>\\</version>', '<version>${project.version}</version>'
    $content = $content -replace '<version>\\</version>', '<version>${project.version}</version>'
    
    # Fix name and description issues
    $content = $content -replace '\(Deprecated\)', '(Deprecated)'
    
    # Write back the fixed content
    Set-Content $pomFile.FullName -Value $content -NoNewline -Encoding UTF8
}

Write-Host "`n✓ All compatibility modules fixed!" -ForegroundColor Green
