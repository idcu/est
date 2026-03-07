package ltd.idcu.est.features.messaging.pulsar;

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

public class PulsarMessageConsumer implements MessageConsumer {
    
    private final PulsarConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public PulsarMessageConsumer(PulsarConnection connection, MessagingConfig config) {
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
            PulsarConnection.PulsarMessage pulsarMessage = connection.receive();
            if (pulsarMessage != null) {
                return convertMessage(pulsarMessage);
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
            connection.subscribe(topic, "est-subscription");
        } catch (MessagingException e) {
            // Ignore for now
        }
        subscribeToTopic(topic, handler);
    }
    
    private void subscribeToTopic(String topic, Consumer<Message> handler) {
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    PulsarConnection.PulsarMessage pulsarMessage = connection.receive();
                    if (pulsarMessage != null) {
                        Message message = convertMessage(pulsarMessage);
                        Consumer<Message> topicHandler = handlers.get(pulsarMessage.getTopic());
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
        // Pulsar acknowledgment handled by connection
    }
    
    @Override
    public void acknowledgeAll() {
        // Pulsar acknowledgment handled by connection
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
    
    private Message convertMessage(PulsarConnection.PulsarMessage pulsarMessage) {
        String body = pulsarMessage.getPayload() != null ? new String(pulsarMessage.getPayload(), StandardCharsets.UTF_8) : null;
        
        return DefaultMessage.builder()
                .id(String.valueOf(pulsarMessage.getMessageId()))
                .topic(pulsarMessage.getTopic())
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
