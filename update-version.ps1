# 更新所有 pom.xml 文件中的版本号
Get-ChildItem -Path . -Filter pom.xml -Recurse | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    $content = $content -replace '1\.3\.0-SNAPSHOT', '2.0.0'
    Set-Content -Path $_.FullName -Value $content -NoNewline
    Write-Host "Updated: $($_.FullName)"
}
