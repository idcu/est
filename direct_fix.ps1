
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
        $content = $null
        $fixed = $false
        
        try {
            $content = [System.Text.Encoding]::UTF8.GetString($rawBytes)
        } catch {
        }
        
        if ($content) {
            $hasGarbled = $false
            foreach ($c in $content.ToCharArray()) {
                $uc = [int]$c
                if ($uc -eq 0xFFFD) {
                    $hasGarbled = $true
                    break
                }
            }
            
            if ($hasGarbled) {
                Write-Host "  Garbled detected, trying all methods..."
                
                for ($method = 1; $method -le 4; $method++) {
                    if ($fixed) { break }
                    
                    try {
                        $testContent = $null
                        
                        switch ($method) {
                            1 {
                                $isoBytes = [System.Text.Encoding]::GetEncoding("ISO-8859-1").GetBytes($content)
                                $testContent = [System.Text.Encoding]::UTF8.GetString($isoBytes)
                            }
                            2 {
                                $gbk = [System.Text.Encoding]::GetEncoding("GBK")
                                $testContent = $gbk.GetString($rawBytes)
                            }
                            3 {
                                $gb2312 = [System.Text.Encoding]::GetEncoding("GB2312")
                                $testContent = $gb2312.GetString($rawBytes)
                            }
                            4 {
                                $win1252 = [System.Text.Encoding]::GetEncoding("Windows-1252")
                                $winBytes = $win1252.GetBytes($content)
                                $testContent = [System.Text.Encoding]::UTF8.GetString($winBytes)
                            }
                        }
                        
                        if ($testContent) {
                            $hasChinese = $false
                            $stillGarbled = $false
                            foreach ($c in $testContent.ToCharArray()) {
                                $uc = [int]$c
                                if ($uc -ge 0x4E00 -and $uc -le 0x9FFF) {
                                    $hasChinese = $true
                                }
                                if ($uc -eq 0xFFFD) {
                                    $stillGarbled = $true
                                    break
                                }
                            }
                            
                            if ($hasChinese -and -not $stillGarbled) {
                                $content = $testContent
                                $fixed = $true
                                Write-Host "  Fixed with method $method"
                            }
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
