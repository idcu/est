# Restore all .md files directly from Git to ensure correct encoding
Write-Host "Restoring all .md files from Git HEAD..."

# Get all .md files in Git
$mdFiles = git ls-files | Where-Object { $_ -like "*.md" }

Write-Host "Found $($mdFiles.Count) .md files in Git"

$restoredCount = 0

foreach ($file in $mdFiles) {
    try {
        # Get the content from Git and write it back directly
        $content = git show "HEAD:$file"
        [System.IO.File]::WriteAllText($file, $content, [System.Text.Encoding]::UTF8)
        Write-Host "Restored: $file"
        $restoredCount++
    }
    catch {
        Write-Host "Failed to restore: $file - Error: $_" -ForegroundColor Red
    }
}

Write-Host "`nRestore complete!"
Write-Host "Restored: $restoredCount files"
