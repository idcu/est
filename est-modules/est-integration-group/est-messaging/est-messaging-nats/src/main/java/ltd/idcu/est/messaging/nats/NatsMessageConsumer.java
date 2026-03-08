package ltd.idcu.est.messaging.nats;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class NatsMessageConsumer implements MessageConsumer {
    
    private final NatsConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public NatsMessageConsumer(NatsConnection connection, MessagingConfig config) {
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
        
        for (String subject : handlers.keySet()) {
            subscribeToSubject(subject, handler);
        }
    }
    
    @Override
    public void subscribe(String subject, Consumer<Message> handler) {
        checkClosed();
        
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        handlers.put(subject, handler);
        subscribeToSubject(subject, handler);
    }
    
    private void subscribeToSubject(String subject, Consumer<Message> handler) {
        connection.subscribe(subject, (sub, msg) -> {
            Message message = DefaultMessage.builder()
                    .id(String.valueOf(System.currentTimeMillis()))
                    .topic(sub)
                    .body(msg)
                    .timestamp(System.currentTimeMillis())
                    .build();
            handler.accept(message);
        });
    }
    
    @Override
    public void unsubscribe() {
        for (String subject : handlers.keySet()) {
            try {
                connection.unsubscribe(subject);
            } catch (Exception e) {
                // Ignore
            }
        }
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String subject) {
        try {
            connection.unsubscribe(subject);
        } catch (Exception e) {
            // Ignore
        }
        handlers.remove(subject);
    }
    
    @Override
    public void acknowledge(Message message) {
        // NATS doesn't need acknowledgment
    }
    
    @Override
    public void acknowledgeAll() {
        // NATS doesn't need acknowledgment
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
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Consumer has been closed");
        }
    }
}
