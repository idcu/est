package ltd.idcu.est.features.messaging.activemq;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ActiveMqMessageProducer implements MessageProducer {
    
    private final ActiveMqConnection connection;
    private volatile boolean closed;
    
    public ActiveMqMessageProducer(ActiveMqConnection connection) {
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
        
        byte[] body = serializeBody(message.getBody());
        connection.send(destination, body);
    }
    
    @Override
    public void send(String destination, Object body) {
        checkClosed();
        
        byte[] bodyBytes = serializeBody(body);
        connection.send(destination, bodyBytes);
    }
    
    @Override
    public void send(String destination, String partitionKey, Object body) {
        checkClosed();
        
        byte[] bodyBytes = serializeBody(body);
        connection.send(destination, bodyBytes);
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
    
    private byte[] serializeBody(Object body) {
        if (body == null) {
            return new byte[0];
        }
        
        if (body instanceof byte[]) {
            return (byte[]) body;
        }
        
        if (body instanceof String) {
            return ((String) body).getBytes(StandardCharsets.UTF_8);
        }
        
        return body.toString().getBytes(StandardCharsets.UTF_8);
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Producer has been closed");
        }
    }
}
