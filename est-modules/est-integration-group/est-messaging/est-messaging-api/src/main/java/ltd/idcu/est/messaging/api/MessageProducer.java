package ltd.idcu.est.messaging.api;

import java.util.concurrent.CompletableFuture;

public interface MessageProducer {
    
    void send(Message message);
    
    void send(String queue, Object body);
    
    void send(String queue, String topic, Object body);
    
    CompletableFuture<Void> sendAsync(Message message);
    
    CompletableFuture<Void> sendAsync(String queue, Object body);
    
    void close();
}
