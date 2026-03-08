
$ErrorActionPreference = "Continue"

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
        $rawBytes = [System.IO.File]::ReadAllBytes($filePath)
        
        try {
            $gbk = [System.Text.Encoding]::GetEncoding("GBK")
            $content = $gbk.GetString($rawBytes)
            [System.IO.File]::WriteAllText($filePath, $content, [System.Text.Encoding]::UTF8)
            Write-Host "  Converted successfully"
        } catch {
            Write-Host "  Error converting: $_"
        }
    } catch {
        Write-Host "  Error: $_"
    }
}

Write-Host "All done!"
