package ltd.idcu.est.features.messaging.kafka;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class KafkaMessageConsumer implements MessageConsumer {
    
    private final KafkaConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final Map<String, Long> offsets;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public KafkaMessageConsumer(KafkaConnection connection, MessagingConfig config) {
        this.connection = connection;
        this.config = config;
        this.executor = config.createExecutorService();
        this.handlers = new ConcurrentHashMap<>();
        this.offsets = new ConcurrentHashMap<>();
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
        
        for (Map.Entry<String, Long> entry : offsets.entrySet()) {
            String topic = entry.getKey();
            long offset = entry.getValue();
            
            try {
                List<KafkaConnection.KafkaRecord> records = connection.fetch(topic, 0, offset, 1024 * 1024);
                if (!records.isEmpty()) {
                    KafkaConnection.KafkaRecord record = records.get(0);
                    offsets.put(topic, record.getOffset() + 1);
                    return convertRecord(record);
                }
            } catch (Exception e) {
                // Continue to next topic
            }
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
        offsets.put(topic, 0L);
        subscribeToTopic(topic, handler);
    }
    
    private void subscribeToTopic(String topic, Consumer<Message> handler) {
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    Long offset = offsets.get(topic);
                    if (offset == null) {
                        offset = 0L;
                    }
                    
                    List<KafkaConnection.KafkaRecord> records = connection.fetch(topic, 0, offset, 1024 * 1024);
                    for (KafkaConnection.KafkaRecord record : records) {
                        Message message = convertRecord(record);
                        handler.accept(message);
                        offsets.put(topic, record.getOffset() + 1);
                    }
                    
                    if (records.isEmpty()) {
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
        offsets.clear();
    }
    
    @Override
    public void unsubscribe(String topic) {
        handlers.remove(topic);
        offsets.remove(topic);
    }
    
    @Override
    public void acknowledge(Message message) {
        // Kafka doesn't need explicit acknowledgment for simple fetch
    }
    
    @Override
    public void acknowledgeAll() {
        // Kafka doesn't need explicit acknowledgment for simple fetch
    }
    
    @Override
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        running.set(false);
        handlers.clear();
        offsets.clear();
        
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
    
    private Message convertRecord(KafkaConnection.KafkaRecord record) {
        String body = record.getValue() != null ? new String(record.getValue(), StandardCharsets.UTF_8) : null;
        
        return DefaultMessage.builder()
                .id(String.valueOf(record.getOffset()))
                .topic(record.getTopic())
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
