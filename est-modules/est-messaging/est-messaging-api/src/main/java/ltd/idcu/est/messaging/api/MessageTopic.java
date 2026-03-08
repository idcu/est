package ltd.idcu.est.messaging.api;

import java.util.function.Consumer;

public interface MessageTopic {
    
    String getName();
    
    boolean create();
    
    boolean delete();
    
    boolean exists();
    
    int getSubscriberCount();
    
    void publish(Message message);
    
    void subscribe(Consumer<Message> handler);
    
    void unsubscribe(Consumer<Message> handler);
    
    void close();
}
