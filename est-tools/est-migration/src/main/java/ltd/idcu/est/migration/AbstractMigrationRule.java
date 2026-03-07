package ltd.idcu.est.migration;

public abstract class AbstractMigrationRule implements MigrationRule {
    protected final String name;
    protected final String description;
    protected final int priority;

    protected AbstractMigrationRule(String name, String description, int priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected String replaceImport(String content, String oldImport, String newImport, MigrationResult result) {
        if (content.contains(oldImport)) {
            String newContent = content.replace(oldImport, newImport);
            result.addWarning("Replaced import: " + oldImport + " -> " + newImport);
            return newContent;
        }
        return content;
    }

    protected String replaceAnnotation(String content, String oldAnnotation, String newAnnotation, MigrationResult result) {
        if (content.contains(oldAnnotation)) {
            String newContent = content.replace(oldAnnotation, newAnnotation);
            result.addWarning("Replaced annotation: " + oldAnnotation + " -> " + newAnnotation);
            return newContent;
        }
        return content;
    }
}
