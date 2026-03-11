package ltd.idcu.est.compliance.api.cybersecurity;

public interface ComplianceIssue {

    String getId();

    String getCheckId();

    String getDescription();

    IssueSeverity getSeverity();

    String getRecommendation();

    boolean isResolved();
}
