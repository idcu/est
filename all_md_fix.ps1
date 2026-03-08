
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
        $bestContent = $null
        $bestScore = -99999
        
        $encodings = @("UTF-8", "GBK", "GB2312", "Big5")
        
        foreach ($encName in $encodings) {
            try {
                $enc = [System.Text.Encoding]::GetEncoding($encName)
                $content = $enc.GetString($rawBytes)
                
                $score = 0
                $chineseCount = 0
                $garbledCount = 0
                
                foreach ($c in $content.ToCharArray()) {
                    $uc = [int]$c
                    if ($uc -ge 0x4E00 -and $uc -le 0x9FFF) {
                        $chineseCount++
                    }
                    if ($uc -eq 0xFFFD) {
                        $garbledCount++
                    }
                }
                
                $score = $chineseCount * 10 - $garbledCount * 100
                
                if ($score -gt $bestScore) {
                    $bestScore = $score
                    $bestContent = $content
                }
            } catch {
            }
        }
        
        if ($bestContent) {
            [System.IO.File]::WriteAllText($file.FullName, $bestContent, [System.Text.Encoding]::UTF8)
            Write-Host "  Saved successfully, best score: $bestScore"
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
