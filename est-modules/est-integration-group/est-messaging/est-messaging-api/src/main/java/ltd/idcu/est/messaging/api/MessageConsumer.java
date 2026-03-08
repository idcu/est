package ltd.idcu.est.messaging.api;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MessageConsumer {
    
    Message receive();
    
    Message receive(long timeout);
    
    CompletableFuture<Message> receiveAsync();
    
    void subscribe(Consumer<Message> handler);
    
    void subscribe(String topic, Consumer<Message> handler);
    
    void unsubscribe();
    
    void unsubscribe(String topic);
    
    void acknowledge(Message message);
    
    void acknowledgeAll();
    
    void close();
}
