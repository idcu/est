
$ErrorActionPreference = "Continue"

$rootDir = $PWD.Path
Write-Host "Root directory: $rootDir"

$extensions = @(".md", ".java", ".xml", ".properties")

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext -and $_.FullName -notlike "*\.git*" -and $_.Name -ne "README.md"
}

Write-Host "Found $($files.Count) files to process"

$count = 0

foreach ($file in $files) {
    $count++
    Write-Host "[$count/$($files.Count)] Processing: $($file.FullName)"
    
    try {
        $rawBytes = [System.IO.File]::ReadAllBytes($file.FullName)
        $content = $null
        $fixed = $false
        
        try {
            $content = [System.Text.Encoding]::UTF8.GetString($rawBytes)
        } catch {
        }
        
        if ($content) {
            $needsFix = $false
            foreach ($c in $content.ToCharArray()) {
                if ([int]$c -eq 0xFFFD) {
                    $needsFix = $true
                    break
                }
            }
            
            if ($needsFix) {
                Write-Host "  Trying to fix..."
                
                try {
                    $isoBytes = [System.Text.Encoding]::GetEncoding("ISO-8859-1").GetBytes($content)
                    $testContent = [System.Text.Encoding]::UTF8.GetString($isoBytes)
                    
                    $hasChinese = $false
                    $hasGarbled = $false
                    foreach ($c in $testContent.ToCharArray()) {
                        $uc = [int]$c
                        if ($uc -ge 0x4E00 -and $uc -le 0x9FFF) {
                            $hasChinese = $true
                        }
                        if ($uc -eq 0xFFFD) {
                            $hasGarbled = $true
                        }
                    }
                    
                    if ($hasChinese -and -not $hasGarbled) {
                        $content = $testContent
                        $fixed = $true
                        Write-Host "  Fixed with method 1"
                    }
                } catch {
                }
                
                if (-not $fixed) {
                    try {
                        $gbk = [System.Text.Encoding]::GetEncoding("GBK")
                        $testContent = $gbk.GetString($rawBytes)
                        
                        $hasChinese = $false
                        $hasGarbled = $false
                        foreach ($c in $testContent.ToCharArray()) {
                            $uc = [int]$c
                            if ($uc -ge 0x4E00 -and $uc -le 0x9FFF) {
                                $hasChinese = $true
                            }
                            if ($uc -eq 0xFFFD) {
                                $hasGarbled = $true
                            }
                        }
                        
                        if ($hasChinese -and -not $hasGarbled) {
                            $content = $testContent
                            $fixed = $true
                            Write-Host "  Fixed with method 2"
                        }
                    } catch {
                    }
                }
            }
            
            [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
