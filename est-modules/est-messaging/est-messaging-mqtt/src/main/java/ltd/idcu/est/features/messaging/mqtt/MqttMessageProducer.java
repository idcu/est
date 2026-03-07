package ltd.idcu.est.features.messaging.mqtt;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class MqttMessageProducer implements MessageProducer {
    
    private final MqttConnection connection;
    private final int qos;
    private volatile boolean closed;
    
    public MqttMessageProducer(MqttConnection connection) {
        this(connection, 1);
    }
    
    public MqttMessageProducer(MqttConnection connection, int qos) {
        this.connection = connection;
        this.qos = qos;
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
        connection.publish(topic, body, qos);
    }
    
    @Override
    public void send(String topic, Object body) {
        checkClosed();
        
        byte[] bodyBytes = serializeBody(body);
        connection.publish(topic, bodyBytes, qos);
    }
    
    @Override
    public void send(String topic, String routingKey, Object body) {
        checkClosed();
        
        String fullTopic = routingKey != null && !routingKey.isEmpty() 
                ? topic + "/" + routingKey 
                : topic;
        
        byte[] bodyBytes = serializeBody(body);
        connection.publish(fullTopic, bodyBytes, qos);
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
