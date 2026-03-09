$baseDir = "d:\os\proj\java\est2.0"
$targetVersion = "2.1.0"

Write-Host "Searching for pom.xml files..."
$pomFiles = Get-ChildItem -Path $baseDir -Filter "pom.xml" -Recurse

$count = 0
foreach ($file in $pomFiles) {
    $filePath = $file.FullName
    $relativePath = $file.FullName.Substring($baseDir.Length + 1)
    
    try {
        $content = Get-Content $filePath -Raw -Encoding UTF8
        $updated = $false
        
        # Update main pom.xml version
        if ($content -match '(<groupId>ltd\.idcu</groupId>\s*<artifactId>est</artifactId>\s*)<version>[^<]+</version>') {
            $content = $content -replace '(<groupId>ltd\.idcu</groupId>\s*<artifactId>est</artifactId>\s*)<version>[^<]+</version>', "`${1}<version>$targetVersion</version>"
            $updated = $true
        }
        
        # Update parent version
        if ($content -match '(<parent>\s*<groupId>ltd\.idcu</groupId>\s*<artifactId>[^<]+</artifactId>\s*)<version>[^<]+</version>') {
            $content = $content -replace '(<parent>\s*<groupId>ltd\.idcu</groupId>\s*<artifactId>[^<]+</artifactId>\s*)<version>[^<]+</version>', "`${1}<version>$targetVersion</version>"
            $updated = $true
        }
        
        if ($updated) {
            Set-Content -Path $filePath -Value $content -Encoding UTF8 -NoNewline
            Write-Host "Updated: $relativePath"
            $count++
        } else {
            Write-Host "No change: $relativePath"
        }
    } catch {
        Write-Host "Error processing $relativePath : $_" -ForegroundColor Red
    }
}

Write-Host "`nTotal updated: $count"
Write-Host "Done!"
