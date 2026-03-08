# Convert all .md files from GBK to UTF-8 without BOM
$ErrorActionPreference = "Stop"

# Get all .md files
Write-Host "Finding all .md files..."
$mdFiles = Get-ChildItem -Path . -Filter "*.md" -Recurse | Where-Object { -not $_.PSIsContainer }
Write-Host "Found $($mdFiles.Count) .md files"

$convertedCount = 0
$failedCount = 0

foreach ($file in $mdFiles) {
    try {
        # Read file content with GBK encoding
        $content = [System.IO.File]::ReadAllText($file.FullName, [System.Text.Encoding]::GetEncoding("GBK"))
        
        # Write back with UTF-8 without BOM
        $utf8NoBom = New-Object System.Text.UTF8Encoding $false
        [System.IO.File]::WriteAllText($file.FullName, $content, $utf8NoBom)
        
        Write-Host "Converted: $($file.FullName)"
        $convertedCount++
    }
    catch {
        Write-Host "Failed to convert: $($file.FullName) - Error: $_" -ForegroundColor Red
        $failedCount++
    }
}

Write-Host "`nConversion complete!"
Write-Host "Converted: $convertedCount files"
Write-Host "Failed: $failedCount files"
