# PowerShell script to migrate modules from est-features to est-modules

# Function to update pom.xml files
function Update-PomXml {
    param(
        [string]$filePath,
        [string]$oldParent,
        [string]$newParent,
        [string]$oldArtifactIdPrefix,
        [string]$newArtifactIdPrefix
    )
    
    if (-not (Test-Path $filePath)) {
        Write-Host "File not found: $filePath"
        return
    }
    
    $content = Get-Content $filePath -Raw
    $originalContent = $content
    
    # Update parent
    if ($oldParent -ne $newParent) {
        $content = $content -replace "<artifactId>$oldParent</artifactId>", "<artifactId>$newParent</artifactId>"
    }
    
    # Update artifactId
    if ($oldArtifactIdPrefix -ne $newArtifactIdPrefix) {
        $content = $content -replace "<artifactId>$oldArtifactIdPrefix-", "<artifactId>$newArtifactIdPrefix-"
        $content = $content -replace "<artifactId>$oldArtifactIdPrefix</artifactId>", "<artifactId>$newArtifactIdPrefix</artifactId>"
    }
    
    # Update module references
    if ($oldArtifactIdPrefix -ne $newArtifactIdPrefix) {
        $content = $content -replace "<module>$oldArtifactIdPrefix-", "<module>$newArtifactIdPrefix-"
    }
    
    # Update name
    $content = $content -replace "EST Features ([A-Za-z]+)", "EST `$1"
    
    # Update description
    $content = $content -replace "EST Features ([A-Za-z]+) -", "EST `$1 -"
    
    if ($content -ne $originalContent) {
        Set-Content -Path $filePath -Value $content -NoNewline
        Write-Host "Updated: $filePath"
    }
}

# Function to rename directories
function Rename-ModuleDirectories {
    param(
        [string]$baseDir,
        [string]$oldPrefix,
        [string]$newPrefix
    )
    
    Get-ChildItem -Path $baseDir -Directory | Where-Object { $_.Name -like "$oldPrefix-*" } | ForEach-Object {
        $oldName = $_.Name
        $newName = $_.Name -replace "^$oldPrefix-", "$newPrefix-"
        $oldPath = $_.FullName
        $newPath = Join-Path $baseDir $newName
        
        if ($oldPath -ne $newPath) {
            Rename-Item -Path $oldPath -NewName $newName
            Write-Host "Renamed directory: $oldName -> $newName"
            
            # Recursively rename subdirectories
            Rename-ModuleDirectories -baseDir $newPath -oldPrefix $oldPrefix -newPrefix $newPrefix
        }
    }
}

# Process each feature module
$modules = @(
    @{ Old = "est-features-cache"; New = "est-cache" },
    @{ Old = "est-features-logging"; New = "est-logging" },
    @{ Old = "est-features-data"; New = "est-data" },
    @{ Old = "est-features-security"; New = "est-security" },
    @{ Old = "est-features-messaging"; New = "est-messaging" },
    @{ Old = "est-features-monitor"; New = "est-monitor" },
    @{ Old = "est-features-scheduler"; New = "est-scheduler" },
    @{ Old = "est-features-event"; New = "est-event" },
    @{ Old = "est-features-circuitbreaker"; New = "est-circuitbreaker" },
    @{ Old = "est-features-discovery"; New = "est-discovery" },
    @{ Old = "est-features-config"; New = "est-config" },
    @{ Old = "est-features-performance"; New = "est-performance" },
    @{ Old = "est-features-hotreload"; New = "est-hotreload" },
    @{ Old = "est-features-ai"; New = "est-ai" }
)

foreach ($module in $modules) {
    $oldName = $module.Old
    $newName = $module.New
    $modulePath = Join-Path "est-modules" $newName
    
    if (Test-Path $modulePath) {
        Write-Host "`nProcessing module: $oldName -> $newName"
        
        # Rename subdirectories
        Rename-ModuleDirectories -baseDir $modulePath -oldPrefix $oldName -newPrefix $newName
        
        # Update pom.xml files
        Get-ChildItem -Path $modulePath -Filter "pom.xml" -Recurse | ForEach-Object {
            Update-PomXml -filePath $_.FullName -oldParent "est-features" -newParent "est-modules" -oldArtifactIdPrefix $oldName -newArtifactIdPrefix $newName
        }
    }
}

# Process est-plugin (already has correct name)
Write-Host "`nProcessing module: est-plugin"
$pluginPath = "est-modules\est-plugin"
if (Test-Path $pluginPath) {
    Get-ChildItem -Path $pluginPath -Filter "pom.xml" -Recurse | ForEach-Object {
        Update-PomXml -filePath $_.FullName -oldParent "est-extensions" -newParent "est-modules" -oldArtifactIdPrefix "est-plugin" -newArtifactIdPrefix "est-plugin"
    }
}

# Process est-gateway
Write-Host "`nProcessing module: est-gateway"
$gatewayPath = "est-modules\est-gateway"
if (Test-Path $gatewayPath) {
    $gatewayPom = Join-Path $gatewayPath "pom.xml"
    if (Test-Path $gatewayPom) {
        $content = Get-Content $gatewayPom -Raw
        $content = $content -replace "<artifactId>est-microservices-gateway</artifactId>", "<artifactId>est-gateway</artifactId>"
        $content = $content -replace "<parent>.*<artifactId>est-microservices</artifactId>.*</parent>", "<parent><groupId>ltd.idcu</groupId><artifactId>est-modules</artifactId><version>1.3.0-SNAPSHOT</version></parent>"
        $content = $content -replace "<name>EST Microservices Gateway</name>", "<name>EST Gateway</name>"
        $content = $content -replace "<description>EST Microservices Gateway</description>", "<description>EST Gateway</description>"
        Set-Content -Path $gatewayPom -Value $content -NoNewline
        Write-Host "Updated: $gatewayPom"
    }
}

Write-Host "`nMigration completed!"
