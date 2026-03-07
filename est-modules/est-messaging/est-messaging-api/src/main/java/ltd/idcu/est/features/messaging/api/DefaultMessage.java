package ltd.idcu.est.features.messaging.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultMessage implements Message {
    
    private final String id;
    private final String queue;
    private final String topic;
    private final Object body;
    private final Map<String, Object> headers;
    private final long timestamp;
    private final int priority;
    private final long expiration;
    private final String correlationId;
    private final String replyTo;
    
    public DefaultMessage(String queue, Object body) {
        this(queue, null, body);
    }
    
    public DefaultMessage(String queue, String topic, Object body) {
        this.id = UUID.randomUUID().toString();
        this.queue = queue;
        this.topic = topic;
        this.body = body;
        this.headers = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        this.priority = 0;
        this.expiration = 0;
        this.correlationId = null;
        this.replyTo = null;
    }
    
    private DefaultMessage(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.queue = builder.queue;
        this.topic = builder.topic;
        this.body = builder.body;
        this.headers = new HashMap<>(builder.headers);
        this.timestamp = builder.timestamp > 0 ? builder.timestamp : System.currentTimeMillis();
        this.priority = builder.priority;
        this.expiration = builder.expiration;
        this.correlationId = builder.correlationId;
        this.replyTo = builder.replyTo;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static DefaultMessage of(String queue, Object body) {
        return new DefaultMessage(queue, body);
    }
    
    public static DefaultMessage of(String queue, String topic, Object body) {
        return new DefaultMessage(queue, topic, body);
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public String getQueue() {
        return queue;
    }
    
    @Override
    public String getTopic() {
        return topic;
    }
    
    @Override
    public Object getBody() {
        return body;
    }
    
    @Override
    public Map<String, Object> getHeaders() {
        return new HashMap<>(headers);
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public int getPriority() {
        return priority;
    }
    
    @Override
    public long getExpiration() {
        return expiration;
    }
    
    @Override
    public String getCorrelationId() {
        return correlationId;
    }
    
    @Override
    public String getReplyTo() {
        return replyTo;
    }
    
    public static class Builder {
        private String id;
        private String queue;
        private String topic;
        private Object body;
        private Map<String, Object> headers = new HashMap<>();
        private long timestamp;
        private int priority;
        private long expiration;
        private String correlationId;
        private String replyTo;
        
        public Builder id(String id) {
            this.id = id;
            return this;
        }
        
        public Builder queue(String queue) {
            this.queue = queue;
            return this;
        }
        
        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }
        
        public Builder body(Object body) {
            this.body = body;
            return this;
        }
        
        public Builder header(String key, Object value) {
            this.headers.put(key, value);
            return this;
        }
        
        public Builder headers(Map<String, Object> headers) {
            this.headers.putAll(headers);
            return this;
        }
        
        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder expiration(long expiration) {
            this.expiration = expiration;
            return this;
        }
        
        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }
        
        public Builder replyTo(String replyTo) {
            this.replyTo = replyTo;
            return this;
        }
        
        public DefaultMessage build() {
            return new DefaultMessage(this);
        }
    }
    
    @Override
    public String toString() {
        return "DefaultMessage{" +
                "id='" + id + '\'' +
                ", queue='" + queue + '\'' +
                ", topic='" + topic + '\'' +
                ", timestamp=" + timestamp +
                ", priority=" + priority +
                '}';
    }
}
