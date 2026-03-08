
$ErrorActionPreference = "Continue"

$filesToFix = @(
    "d:\os\proj\java\est2.0\docs\README.md",
    "d:\os\proj\java\est2.0\est-core\README.md",
    "d:\os\proj\java\est2.0\est-demo\README.md",
    "d:\os\proj\java\est2.0\est-base\README.md",
    "d:\os\proj\java\est2.0\est-modules\README.md",
    "d:\os\proj\java\est2.0\est-app\README.md",
    "d:\os\proj\java\est2.0\est-tools\README.md",
    "d:\os\proj\java\est2.0\est-examples\README.md",
    "d:\os\proj\java\est2.0\deploy\README.md"
)

Write-Host "Processing $($filesToFix.Count) selected files"

$count = 0

foreach ($filePath in $filesToFix) {
    $count++
    if (-not (Test-Path $filePath)) {
        Write-Host "[$count] Skipping (not found): $filePath"
        continue
    }
    
    Write-Host "[$count] Processing: $filePath"
    
    try {
        $rawBytes = [System.IO.File]::ReadAllBytes($filePath)
        $bestContent = $null
        $bestScore = -99999
        $bestEnc = $null
        
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
                    $bestEnc = $encName
                }
            } catch {
            }
        }
        
        if ($bestContent) {
            [System.IO.File]::WriteAllText($filePath, $bestContent, [System.Text.Encoding]::UTF8)
            Write-Host "  Saved successfully, encoding: $bestEnc, score: $bestScore"
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
