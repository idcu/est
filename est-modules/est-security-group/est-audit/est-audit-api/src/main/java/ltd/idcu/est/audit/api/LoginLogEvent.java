package ltd.idcu.est.audit.api;

public interface LoginLogEvent extends AuditEvent {
    
    LoginLog getLoginLog();
}
