package ltd.idcu.est.messaging.rocketmq;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RocketMqMessageConsumer implements MessageConsumer {
    
    private final RocketMqConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public RocketMqMessageConsumer(RocketMqConnection connection, MessagingConfig config) {
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
        
        try {
            RocketMqConnection.RocketMqMessage rocketMqMessage = connection.receive();
            if (rocketMqMessage != null) {
                return convertMessage(rocketMqMessage);
            }
        } catch (Exception e) {
            // Ignore
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
        try {
            connection.subscribe(topic);
        } catch (MessagingException e) {
            // Ignore for now
        }
        subscribeToTopic(topic, handler);
    }
    
    private void subscribeToTopic(String topic, Consumer<Message> handler) {
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    RocketMqConnection.RocketMqMessage rocketMqMessage = connection.receive();
                    if (rocketMqMessage != null) {
                        Message message = convertMessage(rocketMqMessage);
                        Consumer<Message> topicHandler = handlers.get(rocketMqMessage.getTopic());
                        if (topicHandler != null) {
                            topicHandler.accept(message);
                        }
                    } else {
                        Thread.sleep(100);
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
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String topic) {
        handlers.remove(topic);
    }
    
    @Override
    public void acknowledge(Message message) {
        // RocketMQ acknowledgment handled by connection
    }
    
    @Override
    public void acknowledgeAll() {
        // RocketMQ acknowledgment handled by connection
    }
    
    @Override
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        running.set(false);
        handlers.clear();
        
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
    
    private Message convertMessage(RocketMqConnection.RocketMqMessage rocketMqMessage) {
        String body = rocketMqMessage.getBody() != null ? new String(rocketMqMessage.getBody(), StandardCharsets.UTF_8) : null;
        
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .topic(rocketMqMessage.getTopic())
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
