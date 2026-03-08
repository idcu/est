
$ErrorActionPreference = "Continue"

$rootDir = $PWD.Path
Write-Host "Root directory: $rootDir"

$allFiles = Get-ChildItem -Path $rootDir -Recurse -File -Filter "*.md"
Write-Host "Total .md files found: $($allFiles.Count)"

$files = $allFiles | Where-Object {
    $_.FullName -notlike "*\.git*" -and $_.Name -ne "README.md"
}

Write-Host "Files to process: $($files.Count)"

$count = 0

foreach ($file in $files) {
    $count++
    Write-Host "[$count/$($files.Count)] Processing: $($file.FullName)"
    
    try {
        $rawBytes = [System.IO.File]::ReadAllBytes($file.FullName)
        $gbk = [System.Text.Encoding]::GetEncoding("GBK")
        $content = $gbk.GetString($rawBytes)
        
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        Write-Host "  Converted from GBK to UTF-8"
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
