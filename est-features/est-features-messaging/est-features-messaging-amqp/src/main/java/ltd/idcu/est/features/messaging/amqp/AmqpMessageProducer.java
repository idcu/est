package ltd.idcu.est.features.messaging.amqp;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class AmqpMessageProducer implements MessageProducer {
    
    private final AmqpConnection connection;
    private final int channelId;
    private volatile boolean closed;
    
    public AmqpMessageProducer(AmqpConnection connection) {
        this.connection = connection;
        this.channelId = connection.createChannel();
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String queue = message.getQueue();
        if (queue == null || queue.isEmpty()) {
            throw new IllegalArgumentException("Message queue cannot be null or empty");
        }
        
        try {
            connection.declareQueue(channelId, queue, true, false, false);
            
            byte[] body = serializeBody(message.getBody());
            connection.basicPublish(channelId, "", queue, body);
        } catch (Exception e) {
            throw new MessagingException("Failed to send message to queue: " + queue, e);
        }
    }
    
    @Override
    public void send(String queue, Object body) {
        checkClosed();
        
        Message message = DefaultMessage.of(queue, body);
        send(message);
    }
    
    @Override
    public void send(String queue, String topic, Object body) {
        checkClosed();
        
        try {
            if (topic != null && !topic.isEmpty()) {
                connection.declareExchange(channelId, topic, "topic", true);
                connection.declareQueue(channelId, queue, true, false, false);
                connection.bindQueue(channelId, queue, topic, queue);
            }
            
            Message message = DefaultMessage.of(queue, topic, body);
            byte[] bodyBytes = serializeBody(message.getBody());
            
            connection.basicPublish(channelId, topic != null ? topic : "", queue, bodyBytes);
        } catch (Exception e) {
            throw new MessagingException("Failed to send message", e);
        }
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String queue, Object body) {
        return CompletableFuture.runAsync(() -> send(queue, body));
    }
    
    @Override
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        
        try {
            connection.closeChannel(channelId);
        } catch (Exception e) {
            // Ignore
        }
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
