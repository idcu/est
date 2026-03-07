package ltd.idcu.est.features.messaging.redis;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class RedisMessageConsumer implements MessageConsumer {
    
    private final RedisConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public RedisMessageConsumer(RedisConnection connection, MessagingConfig config) {
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
            List<Object> messages = connection.receiveMessages();
            if (!messages.isEmpty() && messages.get(0) instanceof RedisConnection.RedisMessage) {
                RedisConnection.RedisMessage redisMsg = (RedisConnection.RedisMessage) messages.get(0);
                return convertMessage(redisMsg);
            }
        } catch (Exception e) {
            // Continue
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
        
        for (String channel : handlers.keySet()) {
            subscribeToChannel(channel, handler);
        }
    }
    
    @Override
    public void subscribe(String channel, Consumer<Message> handler) {
        checkClosed();
        
        if (channel == null || channel.isEmpty()) {
            throw new IllegalArgumentException("Channel cannot be null or empty");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        handlers.put(channel, handler);
        subscribeToChannel(channel, handler);
    }
    
    private void subscribeToChannel(String channel, Consumer<Message> handler) {
        connection.subscribe(channel, (ch, msg) -> {
            Message message = DefaultMessage.builder()
                    .id(String.valueOf(System.currentTimeMillis()))
                    .topic(ch)
                    .body(msg)
                    .timestamp(System.currentTimeMillis())
                    .build();
            handler.accept(message);
        });
        
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    connection.receiveMessages();
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
        for (String channel : handlers.keySet()) {
            try {
                connection.unsubscribe(channel);
            } catch (Exception e) {
                // Ignore
            }
        }
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String channel) {
        try {
            connection.unsubscribe(channel);
        } catch (Exception e) {
            // Ignore
        }
        handlers.remove(channel);
    }
    
    @Override
    public void acknowledge(Message message) {
        // Redis doesn't need acknowledgment
    }
    
    @Override
    public void acknowledgeAll() {
        // Redis doesn't need acknowledgment
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
    
    private Message convertMessage(RedisConnection.RedisMessage redisMsg) {
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .topic(redisMsg.getChannel())
                .body(redisMsg.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Consumer has been closed");
        }
    }
}
