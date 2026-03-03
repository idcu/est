package ltd.idcu.est.features.messaging.api;

public interface MessageQueue {
    
    String getName();
    
    boolean create();
    
    boolean create(QueueConfig config);
    
    boolean delete();
    
    boolean exists();
    
    int size();
    
    boolean isEmpty();
    
    void clear();
    
    Message peek();
    
    Message poll();
    
    boolean offer(Message message);
    
    MessageStats getStats();
}
