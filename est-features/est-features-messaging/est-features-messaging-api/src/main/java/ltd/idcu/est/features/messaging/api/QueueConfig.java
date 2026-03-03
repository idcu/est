package ltd.idcu.est.features.messaging.api;

public class QueueConfig {
    
    private String name;
    private boolean durable = true;
    private boolean exclusive = false;
    private boolean autoDelete = false;
    private int maxLength = 0;
    private long messageTtl = 0;
    private int prefetchCount = 1;
    
    public QueueConfig() {
    }
    
    public QueueConfig(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public QueueConfig setName(String name) {
        this.name = name;
        return this;
    }
    
    public boolean isDurable() {
        return durable;
    }
    
    public QueueConfig setDurable(boolean durable) {
        this.durable = durable;
        return this;
    }
    
    public boolean isExclusive() {
        return exclusive;
    }
    
    public QueueConfig setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
        return this;
    }
    
    public boolean isAutoDelete() {
        return autoDelete;
    }
    
    public QueueConfig setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
        return this;
    }
    
    public int getMaxLength() {
        return maxLength;
    }
    
    public QueueConfig setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }
    
    public long getMessageTtl() {
        return messageTtl;
    }
    
    public QueueConfig setMessageTtl(long messageTtl) {
        this.messageTtl = messageTtl;
        return this;
    }
    
    public int getPrefetchCount() {
        return prefetchCount;
    }
    
    public QueueConfig setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
        return this;
    }
    
    public static QueueConfig defaultConfig(String name) {
        return new QueueConfig(name);
    }
    
    public static QueueConfig durable(String name) {
        return new QueueConfig(name).setDurable(true);
    }
    
    public static QueueConfig temporary(String name) {
        return new QueueConfig(name)
                .setDurable(false)
                .setAutoDelete(true);
    }
    
    @Override
    public String toString() {
        return "QueueConfig{" +
                "name='" + name + '\'' +
                ", durable=" + durable +
                ", exclusive=" + exclusive +
                ", autoDelete=" + autoDelete +
                ", maxLength=" + maxLength +
                ", messageTtl=" + messageTtl +
                ", prefetchCount=" + prefetchCount +
                '}';
    }
}
