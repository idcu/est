# PowerShell script to create compatibility modules

$moduleMappings = @(
    @{ OldName = "est-features-cache"; NewName = "est-cache"; OldPath = "est-features\est-features-cache"; NewPath = "est-modules\est-cache" },
    @{ OldName = "est-features-event"; NewName = "est-event"; OldPath = "est-features\est-features-event"; NewPath = "est-modules\est-event" },
    @{ OldName = "est-features-logging"; NewName = "est-logging"; OldPath = "est-features\est-features-logging"; NewPath = "est-modules\est-logging" },
    @{ OldName = "est-features-data"; NewName = "est-data"; OldPath = "est-features\est-features-data"; NewPath = "est-modules\est-data" },
    @{ OldName = "est-features-security"; NewName = "est-security"; OldPath = "est-features\est-features-security"; NewPath = "est-modules\est-security" },
    @{ OldName = "est-features-messaging"; NewName = "est-messaging"; OldPath = "est-features\est-features-messaging"; NewPath = "est-modules\est-messaging" },
    @{ OldName = "est-features-monitor"; NewName = "est-monitor"; OldPath = "est-features\est-features-monitor"; NewPath = "est-modules\est-monitor" },
    @{ OldName = "est-features-scheduler"; NewName = "est-scheduler"; OldPath = "est-features\est-features-scheduler"; NewPath = "est-modules\est-scheduler" },
    @{ OldName = "est-features-ai"; NewName = "est-ai"; OldPath = "est-features\est-features-ai"; NewPath = "est-modules\est-ai" },
    @{ OldName = "est-features-discovery"; NewName = "est-discovery"; OldPath = "est-features\est-features-discovery"; NewPath = "est-modules\est-discovery" },
    @{ OldName = "est-features-config"; NewName = "est-config"; OldPath = "est-features\est-features-config"; NewPath = "est-modules\est-config" },
    @{ OldName = "est-features-circuitbreaker"; NewName = "est-circuitbreaker"; OldPath = "est-features\est-features-circuitbreaker"; NewPath = "est-modules\est-circuitbreaker" },
    @{ OldName = "est-features-performance"; NewName = "est-performance"; OldPath = "est-features\est-features-performance"; NewPath = "est-modules\est-performance" },
    @{ OldName = "est-features-hotreload"; NewName = "est-hotreload"; OldPath = "est-features\est-features-hotreload"; NewPath = "est-modules\est-hotreload" }
)

foreach ($mapping in $moduleMappings) {
    $oldPath = $mapping.OldPath
    $oldName = $mapping.OldName
    $newName = $mapping.NewName
    
    Write-Host "Creating compatibility module: $oldName -> $newName"
    
    # Create compatibility pom.xml for the main module
    $oldPomPath = Join-Path $oldPath "pom.xml"
    
    $compatPomContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </parent>

    <artifactId>$oldName</artifactId>
    <packaging>pom</packaging>

    <name>EST Features $($newName.Replace('est-', '')) (Deprecated)</name>
    <description>EST Features $($newName.Replace('est-', '')) - DEPRECATED, use $newName instead</description>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>$newName</artifactId>
            <version>\${project.version}</version>
            <type>pom</type>
        </dependency>
    </dependencies>

</project>
"@

    Set-Content -Path $oldPomPath -Value $compatPomContent -NoNewline
    Write-Host "Updated: $oldPomPath"
    
    # Get all submodules from the new module
    $newPath = $mapping.NewPath
    if (Test-Path $newPath) {
        $newPomPath = Join-Path $newPath "pom.xml"
        if (Test-Path $newPomPath) {
            [xml]$newPom = Get-Content $newPomPath
            if ($newPom.project.modules) {
                foreach ($moduleNode in $newPom.project.modules.module) {
                    $subModuleName = $moduleNode
                    $oldSubModuleName = $subModuleName -replace "^$newName-", "$oldName-"
                    $oldSubModulePath = Join-Path $oldPath $oldSubModuleName
                    $newSubModulePath = Join-Path $newPath $subModuleName
                    
                    # Create submodule directory if it doesn't exist
                    if (-not (Test-Path $oldSubModulePath)) {
                        New-Item -ItemType Directory -Path $oldSubModulePath -Force | Out-Null
                    }
                    
                    # Create compatibility pom.xml for submodule
                    $oldSubPomPath = Join-Path $oldSubModulePath "pom.xml"
                    
                    $oldSubPomContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>ltd.idcu</groupId>
        <artifactId>$oldName</artifactId>
        <version>1.3.0-SNAPSHOT</version>
    </parent>

    <artifactId>$oldSubModuleName</artifactId>

    <name>EST Features $($oldSubModuleName.Replace("$oldName-", "")) (Deprecated)</name>
    <description>EST Features $($oldSubModuleName.Replace("$oldName-", "")) - DEPRECATED, use $subModuleName instead</description>

    <dependencies>
        <dependency>
            <groupId>ltd.idcu</groupId>
            <artifactId>$subModuleName</artifactId>
            <version>\${project.version}</version>
        </dependency>
    </dependencies>

</project>
"@

                    Set-Content -Path $oldSubPomPath -Value $oldSubPomContent -NoNewline
                    Write-Host "Updated: $oldSubPomPath"
                }
            }
        }
    }
    
    # Update the main pom.xml to include all submodules
    $subModules = @()
    if (Test-Path $newPath) {
        $newPomPath = Join-Path $newPath "pom.xml"
        if (Test-Path $newPomPath) {
            [xml]$newPom = Get-Content $newPomPath
            if ($newPom.project.modules) {
                foreach ($moduleNode in $newPom.project.modules.module) {
                    $subModuleName = $moduleNode
                    $oldSubModuleName = $subModuleName -replace "^$newName-", "$oldName-"
                    $subModules += $oldSubModuleName
                }
            }
        }
    }
    
    if ($subModules.Count -gt 0) {
        [xml]$oldPom = Get-Content $oldPomPath
        $modulesElement = $oldPom.CreateElement("modules")
        foreach ($subModule in $subModules) {
            $moduleElement = $oldPom.CreateElement("module")
            $moduleElement.InnerText = $subModule
            $modulesElement.AppendChild($moduleElement) | Out-Null
        }
        $oldPom.project.AppendChild($modulesElement) | Out-Null
        $oldPom.Save($oldPomPath)
        Write-Host "Updated modules in: $oldPomPath"
    }
}

Write-Host "`nCompatibility modules created successfully!"
