package ltd.idcu.est.messaging.redis;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingException;

import java.util.concurrent.CompletableFuture;

public class RedisMessageProducer implements MessageProducer {
    
    private final RedisConnection connection;
    private volatile boolean closed;
    
    public RedisMessageProducer(RedisConnection connection) {
        this.connection = connection;
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String channel = message.getTopic();
        if (channel == null || channel.isEmpty()) {
            channel = message.getQueue();
        }
        
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Message channel cannot be null or empty");
        }
        
        String body = serializeBody(message.getBody());
        connection.publish(channel, body);
    }
    
    @Override
    public void send(String channel, Object body) {
        checkClosed();
        
        String bodyStr = serializeBody(body);
        connection.publish(channel, bodyStr);
    }
    
    @Override
    public void send(String channel, String subChannel, Object body) {
        checkClosed();
        
        String fullChannel = subChannel != null && !subChannel.isEmpty()
                ? channel + ":" + subChannel
                : channel;
        
        String bodyStr = serializeBody(body);
        connection.publish(fullChannel, bodyStr);
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String channel, Object body) {
        return CompletableFuture.runAsync(() -> send(channel, body));
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
