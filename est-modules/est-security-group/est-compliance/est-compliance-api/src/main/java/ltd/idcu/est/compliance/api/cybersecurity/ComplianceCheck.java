package ltd.idcu.est.compliance.api.cybersecurity;

import java.time.LocalDateTime;
import java.util.List;

public interface ComplianceCheck {

    String getId();

    String getName();

    String getStandard();

    String getDescription();

    ComplianceStatus getStatus();

    LocalDateTime getLastCheckedAt();

    List<ComplianceIssue> getIssues();

    int getScore();
}
