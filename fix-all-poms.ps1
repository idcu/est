# Fix all pom files in est-features
Write-Host "=== Fixing All POM Files in est-features ===" -ForegroundColor Green

# Get all pom files in est-features
$pomFiles = Get-ChildItem -Path "est-features" -Recurse -Filter "pom.xml"

foreach ($pomFile in $pomFiles) {
    Write-Host "Processing: $($pomFile.FullName)"
    
    # Read file content
    $content = Get-Content $pomFile.FullName -Raw -Encoding UTF8
    
    # Fix the version issue - replace <version>\</version> with <version>${project.version}</version>
    # We need to handle the backslash carefully
    $content = $content -replace '<version>\\</version>', '<version>${project.version}</version>'
    
    # Also fix any other escape character issues
    $content = $content -replace '\\\${', '${'
    
    # Write back the fixed content
    [System.IO.File]::WriteAllText($pomFile.FullName, $content, [System.Text.UTF8Encoding]::new($false))
}

Write-Host "`n✓ All POM files fixed!" -ForegroundColor Green
