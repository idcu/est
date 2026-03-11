package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.ComplianceCheck;
import ltd.idcu.est.compliance.api.cybersecurity.ComplianceIssue;
import ltd.idcu.est.compliance.api.cybersecurity.ComplianceStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultComplianceCheck implements ComplianceCheck {

    private final String id;
    private final String name;
    private final String standard;
    private final String description;
    private ComplianceStatus status;
    private LocalDateTime lastCheckedAt;
    private final List<ComplianceIssue> issues;
    private int score;

    public DefaultComplianceCheck(String name, String standard, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.standard = standard;
        this.description = description;
        this.status = ComplianceStatus.NOT_CHECKED;
        this.issues = new ArrayList<>();
        this.score = 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStandard() {
        return standard;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ComplianceStatus getStatus() {
        return status;
    }

    public void setStatus(ComplianceStatus status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(LocalDateTime lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    @Override
    public List<ComplianceIssue> getIssues() {
        return new ArrayList<>(issues);
    }

    public void addIssue(ComplianceIssue issue) {
        this.issues.add(issue);
    }

    @Override
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
