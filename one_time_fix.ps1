
$ErrorActionPreference = "Continue"

$rootDir = $PWD.Path
Write-Host "Root directory: $rootDir"

$filesToFix = @(
    "d:\os\proj\java\est2.0\docs\README.md",
    "d:\os\proj\java\est2.0\est-core\README.md",
    "d:\os\proj\java\est2.0\est-demo\README.md",
    "d:\os\proj\java\est2.0\est-base\README.md",
    "d:\os\proj\java\est2.0\est-modules\README.md",
    "d:\os\proj\java\est2.0\est-app\README.md",
    "d:\os\proj\java\est2.0\est-tools\README.md",
    "d:\os\proj\java\est2.0\est-examples\README.md",
    "d:\os\proj\java\est2.0\deploy\README.md",
    "d:\os\proj\java\est2.0\docs\architecture\README.md",
    "d:\os\proj\java\est2.0\docs\best-practices\README.md",
    "d:\os\proj\java\est2.0\docs\examples\README.md",
    "d:\os\proj\java\est2.0\docs\getting-started\README.md",
    "d:\os\proj\java\est2.0\docs\guide\README.md",
    "d:\os\proj\java\est2.0\docs\modules\README.md",
    "d:\os\proj\java\est2.0\docs\tutorials\README.md",
    "d:\os\proj\java\est2.0\docs\api\README.md",
    "d:\os\proj\java\est2.0\docs\ai\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-aop\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-config\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-container\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-lifecycle\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-module\README.md",
    "d:\os\proj\java\est2.0\est-core\est-core-tx\README.md"
)

Write-Host "Processing $($filesToFix.Count) key files"

$count = 0

foreach ($filePath in $filesToFix) {
    $count++
    
    if (-not (Test-Path $filePath)) {
        Write-Host "[$count] Skipping (not found): $filePath"
        continue
    }
    
    Write-Host "[$count] Processing: $filePath"
    
    try {
        $gitPath = $filePath.Substring($rootDir.Length + 1).Replace("\", "/")
        Write-Host "  Getting from Git: $gitPath"
        
        $gitOutput = git show "HEAD:$gitPath" 2&gt;&amp;1
        if ($LASTEXITCODE -ne 0) {
            Write-Host "  Git error, falling back to file"
            $rawBytes = [System.IO.File]::ReadAllBytes($filePath)
            $gbk = [System.Text.Encoding]::GetEncoding("GBK")
            $content = $gbk.GetString($rawBytes)
        } else {
            $content = $gitOutput -join "`n"
        }
        
        [System.IO.File]::WriteAllText($filePath, $content, [System.Text.Encoding]::UTF8)
        Write-Host "  Saved successfully as UTF-8"
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
