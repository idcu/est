package ltd.idcu.est.messaging.amqp;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageStats;
import ltd.idcu.est.messaging.api.MessagingException;
import ltd.idcu.est.messaging.api.QueueConfig;

import java.util.concurrent.atomic.AtomicInteger;

public class AmqpMessageQueue implements ltd.idcu.est.messaging.api.MessageQueue {
    
    private final String name;
    private final AmqpConnection connection;
    private final int channelId;
    private final MessageStats stats;
    private final AtomicInteger size;
    private volatile boolean exists;
    
    public AmqpMessageQueue(String name, AmqpConnection connection) {
        this.name = name;
        this.connection = connection;
        this.channelId = connection.createChannel();
        this.stats = new MessageStats();
        this.size = new AtomicInteger(0);
        this.exists = false;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean create() {
        return create(QueueConfig.defaultConfig(name));
    }
    
    @Override
    public boolean create(QueueConfig config) {
        if (exists) {
            return false;
        }
        
        try {
            connection.declareQueue(channelId, name, config.isDurable(), config.isExclusive(), config.isAutoDelete());
            exists = true;
            return true;
        } catch (Exception e) {
            throw new MessagingException("Failed to create queue: " + name, e);
        }
    }
    
    @Override
    public boolean delete() {
        if (!exists) {
            return false;
        }
        
        try {
            connection.closeChannel(channelId);
            exists = false;
            return true;
        } catch (Exception e) {
            throw new MessagingException("Failed to delete queue: " + name, e);
        }
    }
    
    @Override
    public boolean exists() {
        return exists;
    }
    
    @Override
    public int size() {
        return size.get();
    }
    
    @Override
    public boolean isEmpty() {
        return size.get() == 0;
    }
    
    @Override
    public void clear() {
        size.set(0);
    }
    
    @Override
    public Message peek() {
        throw new MessagingException("Peek not supported for AMQP queues");
    }
    
    @Override
    public Message poll() {
        throw new MessagingException("Poll not supported for AMQP queues, use consumer instead");
    }
    
    @Override
    public boolean offer(Message message) {
        throw new MessagingException("Offer not supported for AMQP queues, use producer instead");
    }
    
    @Override
    public MessageStats getStats() {
        return stats.snapshot();
    }
}
