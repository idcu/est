package ltd.idcu.est.features.event.api;

public class DefaultEvent implements Event {
    
    private final String type;
    private final long timestamp;
    private final Object source;
    private final Object payload;
    
    public DefaultEvent(String type) {
        this(type, null, null);
    }
    
    public DefaultEvent(String type, Object payload) {
        this(type, payload, null);
    }
    
    public DefaultEvent(String type, Object payload, Object source) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        this.type = type;
        this.timestamp = System.currentTimeMillis();
        this.payload = payload;
        this.source = source;
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public Object getSource() {
        return source;
    }
    
    @Override
    public Object getPayload() {
        return payload;
    }
    
    public static DefaultEvent of(String type) {
        return new DefaultEvent(type);
    }
    
    public static DefaultEvent of(String type, Object payload) {
        return new DefaultEvent(type, payload);
    }
    
    public static DefaultEvent of(String type, Object payload, Object source) {
        return new DefaultEvent(type, payload, source);
    }
    
    @Override
    public String toString() {
        return "DefaultEvent{" +
                "type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", source=" + source +
                ", payload=" + payload +
                '}';
    }
}
