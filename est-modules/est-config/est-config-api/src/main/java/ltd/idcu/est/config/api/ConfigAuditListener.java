package ltd.idcu.est.config.api;

public interface ConfigAuditListener {
    void onAuditEvent(ConfigAuditEvent event);
}
