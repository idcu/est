param(
    [Parameter(Mandatory=$true)]
    [string]$ModuleName,
    [string]$FeaturesName = $ModuleName
)

$ErrorActionPreference = "Stop"

$basePath = "d:\os\proj\java\est2.0\est-modules\est-$ModuleName"

Write-Host "`n========================================" -ForegroundColor Magenta
Write-Host "Processing module: est-$ModuleName" -ForegroundColor Magenta
Write-Host "========================================`n" -ForegroundColor Magenta

# 查找该模块下所有的子模块
$subModules = Get-ChildItem -Path $basePath -Directory | Where-Object { 
    $_.Name -match "^est-$ModuleName-" -or $_.Name -eq "est-$ModuleName"
}

foreach ($subModule in $subModules) {
    $subModuleName = $subModule.Name
    Write-Host "Processing submodule: $subModuleName" -ForegroundColor Cyan
    
    # 处理 main 源代码
    $mainSrcPath = Join-Path $subModule.FullName "src\main\java\ltd\idcu\est\features\$FeaturesName"
    if (Test-Path $mainSrcPath) {
        Write-Host "  Processing main source..." -ForegroundColor Yellow
        
        $mainDestPath = Join-Path $subModule.FullName "src\main\java\ltd\idcu\est\$ModuleName"
        
        New-Item -ItemType Directory -Path $mainDestPath -Force | Out-Null
        Get-ChildItem -Path $mainSrcPath -Recurse -File | ForEach-Object {
            $relativePath = $_.FullName.Substring($mainSrcPath.Length)
            $destFile = Join-Path $mainDestPath $relativePath
            $destDir = Split-Path -Parent $destFile
            if (!(Test-Path $destDir)) {
                New-Item -ItemType Directory -Path $destDir -Force | Out-Null
            }
            Copy-Item -Path $_.FullName -Destination $destFile -Force
        }
        
        # 更新包名和导入
        Get-ChildItem -Path $mainDestPath -Recurse -Filter "*.java" | ForEach-Object {
            $content = Get-Content $_.FullName -Raw
            $content = $content -replace "package ltd\.idcu\.est\.features\.$FeaturesName", "package ltd.idcu.est.$ModuleName"
            $content = $content -replace "import ltd\.idcu\.est\.features\.$FeaturesName", "import ltd.idcu.est.$ModuleName"
            Set-Content -Path $_.FullName -Value $content -NoNewline
        }
        
        # 删除旧目录
        Remove-Item -Path $mainSrcPath -Recurse -Force
        Write-Host "    ✅ Main source processed" -ForegroundColor Green
    }
    
    # 处理 test 源代码
    $testSrcPath = Join-Path $subModule.FullName "src\test\java\ltd\idcu\est\features\$FeaturesName"
    if (Test-Path $testSrcPath) {
        Write-Host "  Processing test source..." -ForegroundColor Yellow
        
        $testDestPath = Join-Path $subModule.FullName "src\test\java\ltd\idcu\est\$ModuleName"
        
        New-Item -ItemType Directory -Path $testDestPath -Force | Out-Null
        Get-ChildItem -Path $testSrcPath -Recurse -File | ForEach-Object {
            $relativePath = $_.FullName.Substring($testSrcPath.Length)
            $destFile = Join-Path $testDestPath $relativePath
            $destDir = Split-Path -Parent $destFile
            if (!(Test-Path $destDir)) {
                New-Item -ItemType Directory -Path $destDir -Force | Out-Null
            }
            Copy-Item -Path $_.FullName -Destination $destFile -Force
        }
        
        # 更新包名和导入
        Get-ChildItem -Path $testDestPath -Recurse -Filter "*.java" | ForEach-Object {
            $content = Get-Content $_.FullName -Raw
            $content = $content -replace "package ltd\.idcu\.est\.features\.$FeaturesName", "package ltd.idcu.est.$ModuleName"
            $content = $content -replace "import ltd\.idcu\.est\.features\.$FeaturesName", "import ltd.idcu.est.$ModuleName"
            Set-Content -Path $_.FullName -Value $content -NoNewline
        }
        
        # 删除旧目录
        Remove-Item -Path $testSrcPath -Recurse -Force
        Write-Host "    ✅ Test source processed" -ForegroundColor Green
    }
}

Write-Host "`nest-$ModuleName module processing completed!" -ForegroundColor Green
