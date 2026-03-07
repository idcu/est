package ltd.idcu.est.migration;

import java.util.ArrayList;
import java.util.List;

public class MigrationResult {
    private boolean success;
    private final List<String> migratedFiles;
    private final List<String> warnings;
    private final List<String> errors;
    private String summary;

    public MigrationResult() {
        this.migratedFiles = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getMigratedFiles() {
        return migratedFiles;
    }

    public void addMigratedFile(String file) {
        migratedFiles.add(file);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
        this.success = false;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void merge(MigrationResult other) {
        this.migratedFiles.addAll(other.migratedFiles);
        this.warnings.addAll(other.warnings);
        this.errors.addAll(other.errors);
        if (!other.success) {
            this.success = false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Migration Result: ").append(success ? "SUCCESS" : "FAILED").append("\n");
        sb.append("Migrated Files: ").append(migratedFiles.size()).append("\n");
        if (!migratedFiles.isEmpty()) {
            for (String file : migratedFiles) {
                sb.append("  - ").append(file).append("\n");
            }
        }
        sb.append("Warnings: ").append(warnings.size()).append("\n");
        if (!warnings.isEmpty()) {
            for (String warning : warnings) {
                sb.append("  ! ").append(warning).append("\n");
            }
        }
        sb.append("Errors: ").append(errors.size()).append("\n");
        if (!errors.isEmpty()) {
            for (String error : errors) {
                sb.append("  X ").append(error).append("\n");
            }
        }
        if (summary != null) {
            sb.append("\nSummary:\n").append(summary);
        }
        return sb.toString();
    }
}
