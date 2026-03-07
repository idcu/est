# PowerShell script to batch update all compatibility modules

$moduleList = @(
    "est-features-cache", "est-features-event", "est-features-logging", "est-features-data",
    "est-features-security", "est-features-messaging", "est-features-monitor", "est-features-scheduler",
    "est-features-ai", "est-features-discovery", "est-features-config", "est-features-circuitbreaker",
    "est-features-performance", "est-features-hotreload"
)

foreach ($oldModule in $moduleList) {
    $newModule = $oldModule -replace "^est-features-", "est-"
    Write-Host "Processing $oldModule -> $newModule"
    
    $oldPath = "est-features\$oldModule"
    
    # Update main module pom.xml
    $mainPomPath = "$oldPath\pom.xml"
    if (Test-Path $mainPomPath) {
        [xml]$pom = Get-Content $mainPomPath
        
        # Update name and description
        if ($pom.project.name) {
            $pom.project.name = "$($pom.project.name) (Deprecated)"
        }
        if ($pom.project.description) {
            $pom.project.description = "$($pom.project.description) - DEPRECATED, use $newModule instead"
        }
        
        # Add dependency on new module
        $deps = $pom.SelectSingleNode("//dependencies")
        if (-not $deps) {
            $deps = $pom.CreateElement("dependencies")
            $pom.project.AppendChild($deps) | Out-Null
        }
        
        $dep = $pom.CreateElement("dependency")
        $groupId = $pom.CreateElement("groupId")
        $groupId.InnerText = "ltd.idcu"
        $artifactId = $pom.CreateElement("artifactId")
        $artifactId.InnerText = $newModule
        $version = $pom.CreateElement("version")
        $version.InnerText = "`${project.version}"
        $type = $pom.CreateElement("type")
        $type.InnerText = "pom"
        
        $dep.AppendChild($groupId) | Out-Null
        $dep.AppendChild($artifactId) | Out-Null
        $dep.AppendChild($version) | Out-Null
        $dep.AppendChild($type) | Out-Null
        $deps.AppendChild($dep) | Out-Null
        
        $pom.Save($mainPomPath)
        Write-Host "  Updated main pom: $mainPomPath"
    }
    
    # Update submodules
    if (Test-Path $oldPath) {
        Get-ChildItem -Path $oldPath -Directory | Where-Object { $_.Name -like "$oldModule-*" } | ForEach-Object {
            $subOldName = $_.Name
            $subNewName = $subOldName -replace "^$oldModule-", "$newModule-"
            $subPomPath = Join-Path $_.FullName "pom.xml"
            
            if (Test-Path $subPomPath) {
                [xml]$subPom = Get-Content $subPomPath
                
                # Update name and description
                if ($subPom.project.name) {
                    $subPom.project.name = "$($subPom.project.name) (Deprecated)"
                }
                if ($subPom.project.description) {
                    $subPom.project.description = "$($subPom.project.description) - DEPRECATED, use $subNewName instead"
                }
                
                # Replace dependencies with new module
                $subDeps = $subPom.SelectSingleNode("//dependencies")
                if ($subDeps) {
                    foreach ($depNode in $subDeps.ChildNodes) {
                        if ($depNode.Name -eq "dependency" -and $depNode.artifactId) {
                            $depNode.artifactId = $depNode.artifactId -replace "^$oldModule-", "$newModule-"
                        }
                    }
                } else {
                    # Create new dependencies with just the new module
                    $subDeps = $subPom.CreateElement("dependencies")
                    $newDep = $subPom.CreateElement("dependency")
                    $gId = $subPom.CreateElement("groupId")
                    $gId.InnerText = "ltd.idcu"
                    $aId = $subPom.CreateElement("artifactId")
                    $aId.InnerText = $subNewName
                    $ver = $subPom.CreateElement("version")
                    $ver.InnerText = "`${project.version}"
                    
                    $newDep.AppendChild($gId) | Out-Null
                    $newDep.AppendChild($aId) | Out-Null
                    $newDep.AppendChild($ver) | Out-Null
                    $subDeps.AppendChild($newDep) | Out-Null
                    $subPom.project.AppendChild($subDeps) | Out-Null
                }
                
                $subPom.Save($subPomPath)
                Write-Host "  Updated submodule: $subPomPath"
            }
        }
    }
}

Write-Host "`nCompatibility modules updated!"
