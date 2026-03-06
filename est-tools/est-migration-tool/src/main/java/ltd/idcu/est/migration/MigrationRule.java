package ltd.idcu.est.migration;

public interface MigrationRule {
    String getName();
    String getDescription();
    boolean canApply(String content, MigrationConfig config);
    String apply(String content, MigrationConfig config, MigrationResult result);
    int getPriority();
}
