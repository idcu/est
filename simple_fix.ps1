
$ErrorActionPreference = "Stop"

$rootDir = $PWD.Path
$extensions = @(".md", ".java", ".xml", ".properties")

Write-Host "Starting to fix file encoding in: $rootDir"

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext -and $_.FullName -notlike "*\.git*" -and $_.Name -ne "README.md"
}

Write-Host "Found $($files.Count) files to process"

foreach ($file in $files) {
    try {
        Write-Host "Processing: $($file.FullName)"
        
        $rawBytes = [System.IO.File]::ReadAllBytes($file.FullName)
        $content = $null
        
        # Try UTF-8 first
        try {
            $content = [System.Text.Encoding]::UTF8.GetString($rawBytes)
        } catch {
        }
        
        if ($content -ne $null -and ($content.Contains([char]0xFFFD) -or $content.Contains("锟"))) {
            Write-Host "  Garbled detected, trying to fix..."
            
            # Method 1: UTF-8 -> ISO-8859-1 -> UTF-8
            try {
                $testContent = [System.Text.Encoding]::UTF8.GetString([System.Text.Encoding]::GetEncoding("ISO-8859-1").GetBytes($content))
                $hasChinese = $false
                foreach ($c in $testContent.ToCharArray()) {
                    if ($c -ge [char]0x4E00 -and $c -le [char]0x9FFF) {
                        $hasChinese = $true
                        break
                    }
                }
                if ($hasChinese) {
                    $content = $testContent
                    Write-Host "  Fixed using method 1"
                }
            } catch {
            }
            
            # If method 1 didn't work, try method 2: GBK -> UTF-8
            if ($content.Contains([char]0xFFFD) -or $content.Contains("锟")) {
                try {
                    $testContent = [System.Text.Encoding]::GetEncoding("GBK").GetString($rawBytes)
                    $hasChinese = $false
                    $hasGarbled = $false
                    foreach ($c in $testContent.ToCharArray()) {
                        if ($c -ge [char]0x4E00 -and $c -le [char]0x9FFF) {
                            $hasChinese = $true
                        }
                        if ($c -eq [char]0xFFFD) {
                            $hasGarbled = $true
                        }
                    }
                    if ($hasChinese -and -not $hasGarbled) {
                        $content = $testContent
                        Write-Host "  Fixed using method 2"
                    }
                } catch {
                }
            }
        }
        
        if ($content -ne $null) {
            [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
            Write-Host "  Fixed: $($file.FullName)"
        }
        
    } catch {
        Write-Host "  Error processing $($file.FullName): $_" -ForegroundColor Red
    }
}

Write-Host "Done!"
