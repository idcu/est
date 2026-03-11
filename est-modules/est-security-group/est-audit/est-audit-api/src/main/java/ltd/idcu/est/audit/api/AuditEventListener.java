package ltd.idcu.est.audit.api;

public interface AuditEventListener {
    
    void onAuditEvent(AuditEvent event);
    
    default boolean supports(String eventType) {
        return true;
    }
}
