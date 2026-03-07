package ltd.idcu.est.features.messaging.mqtt;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MqttMessageConsumer implements MessageConsumer {
    
    private final MqttConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public MqttMessageConsumer(MqttConnection connection, MessagingConfig config) {
        this.connection = connection;
        this.config = config;
        this.executor = config.createExecutorService();
        this.handlers = new ConcurrentHashMap<>();
        this.running = new AtomicBoolean(true);
        this.closed = false;
    }
    
    @Override
    public Message receive() {
        return receive(0);
    }
    
    @Override
    public Message receive(long timeout) {
        checkClosed();
        
        MqttConnection.MqttMessage mqttMessage = connection.receive();
        if (mqttMessage != null) {
            return convertMessage(mqttMessage);
        }
        
        return null;
    }
    
    @Override
    public CompletableFuture<Message> receiveAsync() {
        return CompletableFuture.supplyAsync(this::receive, executor);
    }
    
    @Override
    public void subscribe(Consumer<Message> handler) {
        checkClosed();
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        for (String topic : handlers.keySet()) {
            subscribeToTopic(topic, handler);
        }
    }
    
    @Override
    public void subscribe(String topic, Consumer<Message> handler) {
        checkClosed();
        
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        handlers.put(topic, handler);
        subscribeToTopic(topic, handler);
    }
    
    private void subscribeToTopic(String topic, Consumer<Message> handler) {
        connection.subscribe(topic, 1);
        
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    MqttConnection.MqttMessage mqttMessage = connection.receive();
                    if (mqttMessage != null) {
                        Message message = convertMessage(mqttMessage);
                        handler.accept(message);
                        
                        if (mqttMessage.getQos() > 0) {
                            connection.ack(mqttMessage.getPacketId());
                        }
                    }
                } catch (Exception e) {
                    if (running.get()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }
            }
        });
    }
    
    @Override
    public void unsubscribe() {
        for (String topic : handlers.keySet()) {
            try {
                connection.unsubscribe(topic);
            } catch (Exception e) {
                // Ignore
            }
        }
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String topic) {
        try {
            connection.unsubscribe(topic);
        } catch (Exception e) {
            // Ignore
        }
        handlers.remove(topic);
    }
    
    @Override
    public void acknowledge(Message message) {
        // MQTT acknowledges automatically based on QoS
    }
    
    @Override
    public void acknowledgeAll() {
        // MQTT acknowledges automatically based on QoS
    }
    
    @Override
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        running.set(false);
        
        unsubscribe();
        
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private Message convertMessage(MqttConnection.MqttMessage mqttMessage) {
        String body = new String(mqttMessage.getPayload(), StandardCharsets.UTF_8);
        
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .topic(mqttMessage.getTopic())
                .body(body)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Consumer has been closed");
        }
    }
}
