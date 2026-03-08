
$ErrorActionPreference = "Continue"

$rootDir = $PWD.Path
Write-Host "Root directory: $rootDir"

$allFiles = Get-ChildItem -Path $rootDir -Recurse -File -Filter "*.md"
Write-Host "Total .md files found: $($allFiles.Count)"

$files = $allFiles | Where-Object {
    $_.FullName -notlike "*\.git*" -and $_.Name -ne "README.md"
}

Write-Host "Files to process (excluding .git and root README.md): $($files.Count)"

$count = 0

foreach ($file in $files) {
    $count++
    Write-Host "[$count/$($files.Count)] Processing: $($file.FullName)"
    
    try {
        $rawBytes = [System.IO.File]::ReadAllBytes($file.FullName)
        
        $hasChinese = $false
        $tempContent = $null
        try {
            $gbk = [System.Text.Encoding]::GetEncoding("GBK")
            $tempContent = $gbk.GetString($rawBytes)
            foreach ($c in $tempContent.ToCharArray()) {
                $uc = [int]$c
                if ($uc -ge 0x4E00 -and $uc -le 0x9FFF) {
                    $hasChinese = $true
                    break
                }
            }
        } catch {
        }
        
        if ($hasChinese) {
            [System.IO.File]::WriteAllText($file.FullName, $tempContent, [System.Text.Encoding]::UTF8)
            Write-Host "  Converted from GBK to UTF-8 (has Chinese)"
        } else {
            Write-Host "  Skipped (no Chinese characters)"
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
