package ltd.idcu.est.audit.api;

public interface OperationLogEvent extends AuditEvent {
    
    OperationLog getOperationLog();
}
