package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.ComplianceIssue;
import ltd.idcu.est.compliance.api.cybersecurity.IssueSeverity;

import java.util.UUID;

public class DefaultComplianceIssue implements ComplianceIssue {

    private final String id;
    private final String checkId;
    private final String description;
    private final IssueSeverity severity;
    private final String recommendation;
    private boolean resolved;

    public DefaultComplianceIssue(String checkId, String description, IssueSeverity severity, String recommendation) {
        this.id = UUID.randomUUID().toString();
        this.checkId = checkId;
        this.description = description;
        this.severity = severity;
        this.recommendation = recommendation;
        this.resolved = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCheckId() {
        return checkId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IssueSeverity getSeverity() {
        return severity;
    }

    @Override
    public String getRecommendation() {
        return recommendation;
    }

    @Override
    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
