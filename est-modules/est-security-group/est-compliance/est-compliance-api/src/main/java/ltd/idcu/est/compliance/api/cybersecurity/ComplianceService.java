package ltd.idcu.est.compliance.api.cybersecurity;

import java.util.List;
import java.util.Optional;

public interface ComplianceService {

    ComplianceCheck runCheck(String checkName);

    List<ComplianceCheck> runAllChecks();

    Optional<ComplianceCheck> getCheck(String checkId);

    List<ComplianceCheck> getAllChecks();

    List<ComplianceCheck> getChecksByStandard(String standard);

    ComplianceCheck resolveIssue(String checkId, String issueId);
}
