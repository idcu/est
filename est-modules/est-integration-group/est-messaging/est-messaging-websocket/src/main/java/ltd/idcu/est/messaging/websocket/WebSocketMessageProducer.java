package ltd.idcu.est.messaging.websocket;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class WebSocketMessageProducer implements MessageProducer {
    
    private final WebSocketConnection connection;
    private volatile boolean closed;
    
    public WebSocketMessageProducer(WebSocketConnection connection) {
        this.connection = connection;
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String topic = message.getTopic();
        if (topic == null || topic.isEmpty()) {
            topic = message.getQueue();
        }
        
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("Message topic cannot be null or empty");
        }
        
        byte[] body = serializeBody(message.getBody());
        connection.send(topic, body);
    }
    
    @Override
    public void send(String topic, Object body) {
        checkClosed();
        
        byte[] bodyBytes = serializeBody(body);
        connection.send(topic, bodyBytes);
    }
    
    @Override
    public void send(String topic, String partitionKey, Object body) {
        checkClosed();
        
        byte[] bodyBytes = serializeBody(body);
        connection.send(topic, bodyBytes);
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String topic, Object body) {
        return CompletableFuture.runAsync(() -> send(topic, body));
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
