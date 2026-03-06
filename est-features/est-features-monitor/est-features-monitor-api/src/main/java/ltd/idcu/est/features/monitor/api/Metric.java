package ltd.idcu.est.features.monitor.api;

import java.time.Instant;

public class Metric {
    
    public enum Type {
        COUNTER,
        GAUGE,
        HISTOGRAM,
        TIMER,
        CUSTOM
    }
    
    private final String name;
    private final Object value;
    private final String unit;
    private final Instant timestamp;
    private final String description;
    private final Type type;
    
    public Metric(String name, Type type, Object value) {
        this(name, type, value, null, null);
    }
    
    public Metric(String name, Type type, Object value, String unit) {
        this(name, type, value, unit, null);
    }
    
    public Metric(String name, Type type, Object value, String unit, String description) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.timestamp = Instant.now();
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public Type getType() {
        return type;
    }
    
    public Object getValue() {
        return value;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getDoubleValue() {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new IllegalStateException("Metric value is not a number: " + name);
    }
    
    public long getLongValue() {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new IllegalStateException("Metric value is not a number: " + name);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Metric{name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", value=").append(value);
        if (unit != null) {
            sb.append(", unit='").append(unit).append('\'');
        }
        sb.append(", timestamp=").append(timestamp);
        if (description != null) {
            sb.append(", description='").append(description).append('\'');
        }
        sb.append('}');
        return sb.toString();
    }
}
