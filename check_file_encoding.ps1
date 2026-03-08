# Check file encoding and try various decodings
$filePath = "README.md"

Write-Host "Checking file: $filePath"
Write-Host ""

# Read raw bytes
$bytes = [System.IO.File]::ReadAllBytes($filePath)
Write-Host "File size: $($bytes.Length) bytes"
Write-Host "First 100 bytes (hex):"
for ($i = 0; $i -lt [Math]::Min(100, $bytes.Length); $i++) {
    Write-Host ("{0:X2} " -f $bytes[$i]) -NoNewline
    if (($i + 1) % 16 -eq 0) { Write-Host "" }
}
Write-Host ""
Write-Host ""

# Try various encodings
$encodings = @(
    @{Name="UTF-8"; Encoding=[System.Text.Encoding]::UTF8},
    @{Name="GBK"; Encoding=[System.Text.Encoding]::GetEncoding("GBK")},
    @{Name="GB2312"; Encoding=[System.Text.Encoding]::GetEncoding("GB2312")},
    @{Name="Big5"; Encoding=[System.Text.Encoding]::GetEncoding("Big5")},
    @{Name="ISO-8859-1"; Encoding=[System.Text.Encoding]::GetEncoding("ISO-8859-1")},
    @{Name="Windows-1252"; Encoding=[System.Text.Encoding]::GetEncoding("Windows-1252")}
)

foreach ($enc in $encodings) {
    Write-Host "=== Decoding as $($enc.Name) ==="
    try {
        $content = $enc.Encoding.GetString($bytes)
        Write-Host $content.Substring(0, [Math]::Min(500, $content.Length))
    }
    catch {
        Write-Host "Error: $_"
    }
    Write-Host ""
    Write-Host "----------------------------------------"
    Write-Host ""
}
