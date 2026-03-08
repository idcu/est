# Check encoding and garbled characters script

$rootDir = $PWD.Path
$extensions = @('.java', '.xml', '.properties', '.md')

Write-Host "Starting encoding check, root directory: $rootDir"
Write-Host "File types to check: $($extensions -join ', ')"
Write-Host ""

$files = Get-ChildItem -Path $rootDir -Recurse -File | Where-Object {
    $ext = $_.Extension.ToLower()
    $extensions -contains $ext
}

Write-Host "Found $($files.Count) files to check"
Write-Host ""

$problemFiles = @()
$processedCount = 0

foreach ($file in $files) {
    $processedCount++
    if ($processedCount % 100 -eq 0) {
        Write-Host "Processed $processedCount / $($files.Count) files"
    }
    
    try {
        $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8
        
        # Check for common garbled patterns
        if ($content -match '锟\?|涓嶈兘|閿欒鏂囨。|濡傛灉|浣跨敤|鏈€鍚庤闂储寮曞潡|璁℃暟瀛楃椤垫暟璇█|楠岃璇█|淇℃伅|閫傜敤绱㈠紩|鎻愬彇鍏冩暟|浣跨敤绱㈠紩|鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鍦ㄥ師璧峰浣嶇疆瀛楀吀涓婁紶涓婁紶鍒版椂闂存埑澶勭悊涓嶈兘澶勭悊鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鍦ㄥ師璧峰浣嶇疆瀛楀吀涓婁紶涓婁紶鍒版椂闂存埑澶勭悊涓嶈兘澶勭悊鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鍦ㄥ師璧峰浣嶇疆瀛楀吀涓婁紶涓婁紶鍒版椂闂存埑澶勭悊涓嶈兘澶勭悊鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鍦ㄥ師璧峰浣嶇疆瀛楀吀涓婁紶涓婁紶鍒版椂闂存埑澶勭悊涓嶈兘澶勭悊鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡|鍦ㄥ師璧峰浣嶇疆瀛楀吀涓婁紶涓婁紶鍒版椂闂存埑澶勭悊涓嶈兘澶勭悊鏂囨。鍧楁ā鍨嬪瓨鍌ㄥ垎鍧楀悗鍧楃储寮曞潡') {
            $problemFiles += $file.FullName
            Write-Host "Found garbled characters: $($file.FullName)"
        }
    } catch {
        Write-Error "Failed to process file: $($file.FullName)"
        Write-Error $_.Exception.Message
    }
}

Write-Host ""
Write-Host "Check completed!"
Write-Host "Processed $processedCount files"
Write-Host "Found $($problemFiles.Count) files with garbled characters"

if ($problemFiles.Count -gt 0) {
    Write-Host ""
    Write-Host "Files with garbled characters:"
    $problemFiles | ForEach-Object {
        Write-Host "  - $_"
    }
} else {
    Write-Host ""
    Write-Host "All files are clean!"
}
