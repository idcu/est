package ltd.idcu.est.compliance.api.cybersecurity;

public interface AccessControlPolicy {

    String getId();

    String getName();

    String getDescription();

    boolean isEnabled();

    boolean checkAccess(String userId, String resource, String action);

    AccessControlType getType();
}
