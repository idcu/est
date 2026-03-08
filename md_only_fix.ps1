
$ErrorActionPreference = "Continue"

$rootDir = $PWD.Path
Write-Host "Root directory: $rootDir"

$extensions = @(".md")

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext -and $_.FullName -notlike "*\.git*" -and $_.Name -ne "README.md"
}

Write-Host "Found $($files.Count) markdown files to process"

$count = 0

foreach ($file in $files) {
    $count++
    Write-Host "[$count/$($files.Count)] Processing: $($file.FullName)"
    
    try {
        $rawBytes = [System.IO.File]::ReadAllBytes($file.FullName)
        $bestContent = $null
        $bestScore = 0
        
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
                    Write-Host "  Candidate: $encName, score: $score"
                }
            } catch {
            }
        }
        
        if ($bestContent) {
            [System.IO.File]::WriteAllText($file.FullName, $bestContent, [System.Text.Encoding]::UTF8)
            Write-Host "  Saved as UTF-8"
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
