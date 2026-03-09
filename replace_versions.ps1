$baseDir = "d:\os\proj\java\est2.0"
$targetVersion = "2.1.0"

# Function to replace version in a file
function Replace-VersionsInFile {
    param($filePath)
    $content = Get-Content $filePath -Raw -Encoding UTF8
    $original = $content
    
    # Replace <version>2</version>
    $content = $content -replace '(<version>)2(</version>)', "`${1}$targetVersion`$2"
    
    # Replace <version>2.0.0</version>
    $content = $content -replace '(<version>)2\.0\.0(</version>)', "`${1}$targetVersion`$2"
    
    if ($content -ne $original) {
        Set-Content -Path $filePath -Value $content -Encoding UTF8 -NoNewline
        return $true
    }
    return $false
}

Write-Host "Finding pom.xml files..."
$pomFiles = Get-ChildItem -Path $baseDir -Filter "pom.xml" -Recurse

$count = 0
foreach ($file in $pomFiles) {
    $relativePath = $file.FullName.Substring($baseDir.Length + 1)
    if (Replace-VersionsInFile $file.FullName) {
        Write-Host "Updated: $relativePath"
        $count++
    } else {
        Write-Host "No change: $relativePath"
    }
}

Write-Host "`nTotal updated: $count"
Write-Host "Done!"
