package ltd.idcu.est.compliance.cybersecurity;

import ltd.idcu.est.compliance.api.cybersecurity.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultComplianceService implements ComplianceService {

    private final Map<String, ComplianceCheck> checkStore = new ConcurrentHashMap<>();

    public DefaultComplianceService() {
        initializeDefaultChecks();
    }

    private void initializeDefaultChecks() {
        checkStore.put("gdpr-basic", new DefaultComplianceCheck("GDPR Basic Compliance", "GDPR", "Basic GDPR compliance check"));
        checkStore.put("cybersecurity-level1", new DefaultComplianceCheck("Cybersecurity Level 1", "等保1级", "Basic cybersecurity protection"));
        checkStore.put("cybersecurity-level2", new DefaultComplianceCheck("Cybersecurity Level 2", "等保2级", "Intermediate cybersecurity protection"));
        checkStore.put("cybersecurity-level3", new DefaultComplianceCheck("Cybersecurity Level 3", "等保3级", "Advanced cybersecurity protection"));
    }

    @Override
    public ComplianceCheck runCheck(String checkName) {
        DefaultComplianceCheck check = (DefaultComplianceCheck) checkStore.values().stream()
            .filter(c -> c.getName().equals(checkName))
            .findFirst()
            .orElse(null);
        
        if (check != null) {
            check.setLastCheckedAt(LocalDateTime.now());
            check.setStatus(ComplianceStatus.PARTIALLY_COMPLIANT);
            check.setScore(75);
            check.addIssue(new DefaultComplianceIssue(
                check.getId(),
                "Sample compliance issue",
                IssueSeverity.MEDIUM,
                "Fix the issue to improve compliance"
            ));
        }
        return check;
    }

    @Override
    public List<ComplianceCheck> runAllChecks() {
        List<ComplianceCheck> results = new ArrayList<>();
        for (String checkName : checkStore.keySet()) {
            ComplianceCheck result = runCheck(checkStore.get(checkName).getName());
            if (result != null) {
                results.add(result);
            }
        }
        return results;
    }

    @Override
    public Optional<ComplianceCheck> getCheck(String checkId) {
        return Optional.ofNullable(checkStore.get(checkId));
    }

    @Override
    public List<ComplianceCheck> getAllChecks() {
        return new ArrayList<>(checkStore.values());
    }

    @Override
    public List<ComplianceCheck> getChecksByStandard(String standard) {
        return checkStore.values().stream()
            .filter(c -> c.getStandard().equals(standard))
            .collect(Collectors.toList());
    }

    @Override
    public ComplianceCheck resolveIssue(String checkId, String issueId) {
        DefaultComplianceCheck check = (DefaultComplianceCheck) checkStore.get(checkId);
        if (check != null) {
            for (ComplianceIssue issue : check.getIssues()) {
                if (issue.getId().equals(issueId)) {
                    ((DefaultComplianceIssue) issue).setResolved(true);
                }
            }
            updateCheckStatus(check);
        }
        return check;
    }

    private void updateCheckStatus(DefaultComplianceCheck check) {
        long unresolvedCount = check.getIssues().stream()
            .filter(i -> !i.isResolved())
            .count();
        
        if (unresolvedCount == 0) {
            check.setStatus(ComplianceStatus.COMPLIANT);
            check.setScore(100);
        } else if (unresolvedCount < 3) {
            check.setStatus(ComplianceStatus.PARTIALLY_COMPLIANT);
            check.setScore(80);
        } else {
            check.setStatus(ComplianceStatus.NON_COMPLIANT);
            check.setScore(50);
        }
    }
}
