package ltd.idcu.est.features.monitor.api;

import java.time.Instant;

public class Metric {
    
    private final String name;
    private final Object value;
    private final String unit;
    private final Instant timestamp;
    private final String description;
    
    public Metric(String name, Object value) {
        this(name, value, null, null);
    }
    
    public Metric(String name, Object value, String unit) {
        this(name, value, unit, null);
    }
    
    public Metric(String name, Object value, String unit, String description) {
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.timestamp = Instant.now();
        this.description = description;
    }
    
    public String getName() {
        return name;
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
