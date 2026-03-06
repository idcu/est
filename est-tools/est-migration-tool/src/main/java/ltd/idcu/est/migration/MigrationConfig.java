package ltd.idcu.est.migration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MigrationConfig {
    private File sourceDirectory;
    private File targetDirectory;
    private SourceFramework sourceFramework;
    private boolean backup;
    private boolean overwrite;
    private boolean interactive;
    private final List<String> excludePatterns;
    private final List<String> includePatterns;

    public enum SourceFramework {
        SPRING_BOOT,
        SOLON,
        QUARKUS,
        MICRONAUT,
        EST_1X,
        OTHER
    }

    public MigrationConfig() {
        this.backup = true;
        this.overwrite = false;
        this.interactive = false;
        this.excludePatterns = new ArrayList<>();
        this.includePatterns = new ArrayList<>();
        this.sourceFramework = SourceFramework.SPRING_BOOT;
    }

    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = new File(sourceDirectory);
    }

    public File getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    public void setTargetDirectory(String targetDirectory) {
        this.targetDirectory = new File(targetDirectory);
    }

    public SourceFramework getSourceFramework() {
        return sourceFramework;
    }

    public void setSourceFramework(SourceFramework sourceFramework) {
        this.sourceFramework = sourceFramework;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }

    public void addExcludePattern(String pattern) {
        excludePatterns.add(pattern);
    }

    public List<String> getIncludePatterns() {
        return includePatterns;
    }

    public void addIncludePattern(String pattern) {
        includePatterns.add(pattern);
    }

    public boolean shouldIncludeFile(String fileName) {
        if (!includePatterns.isEmpty()) {
            for (String pattern : includePatterns) {
                if (fileName.matches(pattern)) {
                    return true;
                }
            }
            return false;
        }

        for (String pattern : excludePatterns) {
            if (fileName.matches(pattern)) {
                return false;
            }
        }
        return true;
    }
}
