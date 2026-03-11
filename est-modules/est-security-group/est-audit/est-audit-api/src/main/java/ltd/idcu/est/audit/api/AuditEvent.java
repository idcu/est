package ltd.idcu.est.audit.api;

public interface AuditEvent {
    
    String getId();
    
    long getTimestamp();
    
    String getEventType();
    
    Object getSource();
}
