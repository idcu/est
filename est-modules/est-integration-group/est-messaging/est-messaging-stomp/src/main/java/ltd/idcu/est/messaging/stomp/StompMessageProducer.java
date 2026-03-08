package ltd.idcu.est.messaging.stomp;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class StompMessageProducer implements MessageProducer {
    
    private final StompConnection connection;
    private volatile boolean closed;
    
    public StompMessageProducer(StompConnection connection) {
        this.connection = connection;
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String destination = message.getTopic();
        if (destination == null || destination.isEmpty()) {
            destination = message.getQueue();
        }
        
        if (destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Message destination cannot be null or empty");
        }
        
        String body = serializeBody(message.getBody());
        connection.send(destination, body);
    }
    
    @Override
    public void send(String destination, Object body) {
        checkClosed();
        
        String bodyStr = serializeBody(body);
        connection.send(destination, bodyStr);
    }
    
    @Override
    public void send(String destination, String subDestination, Object body) {
        checkClosed();
        
        String fullDestination = subDestination != null && !subDestination.isEmpty()
                ? destination + "/" + subDestination
                : destination;
        
        String bodyStr = serializeBody(body);
        connection.send(fullDestination, bodyStr);
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String destination, Object body) {
        return CompletableFuture.runAsync(() -> send(destination, body));
    }
    
    @Override
    public void close() {
        closed = true;
    }
    
    private String serializeBody(Object body) {
        if (body == null) {
            return "";
        }
        
        if (body instanceof String) {
            return (String) body;
        }
        
        return body.toString();
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Producer has been closed");
        }
    }
}
