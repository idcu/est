# Simple encoding check script

$rootDir = $PWD.Path
$extensions = @('.java', '.xml', '.properties', '.md')

Write-Host "Starting simple encoding check..."
Write-Host "Root directory: $rootDir"
Write-Host "File types: $($extensions -join ', ')"
Write-Host ""

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext
}

Write-Host "Found $($files.Count) files"
Write-Host ""

$successCount = 0
$errorCount = 0

foreach ($file in $files) {
    try {
        $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8
        $successCount++
    } catch {
        $errorCount++
        Write-Host "Error reading file: $($file.FullName)"
    }
}

Write-Host ""
Write-Host "Check completed!"
Write-Host "Successfully read: $successCount files"
Write-Host "Failed to read: $errorCount files"

if ($errorCount -eq 0) {
    Write-Host ""
    Write-Host "All files can be read successfully!"
} else {
    Write-Host ""
    Write-Host "Some files had reading errors."
}
