
$rootDir = $PWD.Path
$allFiles = Get-ChildItem -Path $rootDir -Recurse -File
Write-Host "Total files: $($allFiles.Count)"

$mdFiles = $allFiles | Where-Object { $_.Extension -eq ".md" }
Write-Host "Total .md files: $($mdFiles.Count)"
Write-Host ""
Write-Host "List of .md files:"
foreach ($file in $mdFiles) {
    Write-Host "  $($file.FullName)"
}
