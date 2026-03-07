package ltd.idcu.est.config.api;

import java.time.Instant;

public class ConfigAuditEvent {
    private final String key;
    private final Object oldValue;
    private final Object newValue;
    private final ChangeType changeType;
    private final Instant timestamp;
    private final String source;

    public enum ChangeType {
        ADDED,
        MODIFIED,
        DELETED
    }

    public ConfigAuditEvent(String key, Object oldValue, Object newValue, ChangeType changeType) {
        this(key, oldValue, newValue, changeType, "unknown");
    }

    public ConfigAuditEvent(String key, Object oldValue, Object newValue, ChangeType changeType, String source) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeType = changeType;
        this.timestamp = Instant.now();
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "ConfigAuditEvent{" +
                "key='" + key + '\'' +
                ", oldValue=" + oldValue +
                ", newValue=" + newValue +
                ", changeType=" + changeType +
                ", timestamp=" + timestamp +
                ", source='" + source + '\'' +
                '}';
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"key\":\"").append(escapeJson(key)).append("\",");
        sb.append("\"oldValue\":").append(valueToJson(oldValue)).append(",");
        sb.append("\"newValue\":").append(valueToJson(newValue)).append(",");
        sb.append("\"changeType\":\"").append(changeType.name()).append("\",");
        sb.append("\"timestamp\":\"").append(timestamp.toString()).append("\",");
        sb.append("\"source\":\"").append(escapeJson(source)).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private static String valueToJson(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return "\"" + escapeJson((String) value) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        return "\"" + escapeJson(value.toString()) + "\"";
    }

    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
