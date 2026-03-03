package ltd.idcu.est.features.messaging.local;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageStats;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class LocalMessageTopic implements ltd.idcu.est.features.messaging.api.MessageTopic {
    
    private final String name;
    private final List<Consumer<Message>> subscribers;
    private final MessageStats stats;
    private volatile boolean exists;
    
    public LocalMessageTopic(String name) {
        this.name = name;
        this.subscribers = new CopyOnWriteArrayList<>();
        this.stats = new MessageStats();
        this.exists = false;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean create() {
        if (exists) {
            return false;
        }
        exists = true;
        return true;
    }
    
    @Override
    public boolean delete() {
        if (!exists) {
            return false;
        }
        subscribers.clear();
        exists = false;
        return true;
    }
    
    @Override
    public boolean exists() {
        return exists;
    }
    
    @Override
    public int getSubscriberCount() {
        return subscribers.size();
    }
    
    @Override
    public void publish(Message message) {
        checkExists();
        
        stats.incrementSentCount();
        
        for (Consumer<Message> subscriber : subscribers) {
            try {
                long start = System.currentTimeMillis();
                subscriber.accept(message);
                stats.incrementReceivedCount();
                stats.incrementAcknowledgedCount();
                stats.addDeliveryTime(System.currentTimeMillis() - start);
            } catch (Exception e) {
                stats.incrementFailedCount();
            }
        }
    }
    
    @Override
    public void subscribe(Consumer<Message> handler) {
        checkExists();
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        subscribers.add(handler);
    }
    
    @Override
    public void unsubscribe(Consumer<Message> handler) {
        subscribers.remove(handler);
    }
    
    @Override
    public void close() {
        subscribers.clear();
    }
    
    private void checkExists() {
        if (!exists) {
            throw new MessagingException("Topic does not exist: " + name);
        }
    }
    
    public MessageStats getStats() {
        return stats.snapshot();
    }
}
