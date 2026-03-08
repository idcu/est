package ltd.idcu.est.messaging.stomp;

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

public class StompMessageConsumer implements MessageConsumer {
    
    private final StompConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public StompMessageConsumer(StompConnection connection, MessagingConfig config) {
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
            StompConnection.StompFrame frame = connection.receive();
            if (frame != null && "MESSAGE".equals(frame.getCommand())) {
                return convertFrame(frame);
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
        
        for (String destination : handlers.keySet()) {
            subscribeToDestination(destination, handler);
        }
    }
    
    @Override
    public void subscribe(String destination, Consumer<Message> handler) {
        checkClosed();
        
        if (destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        handlers.put(destination, handler);
        subscribeToDestination(destination, handler);
    }
    
    private void subscribeToDestination(String destination, Consumer<Message> handler) {
        connection.subscribe(destination);
        
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    StompConnection.StompFrame frame = connection.receive();
                    if (frame != null && "MESSAGE".equals(frame.getCommand())) {
                        Message message = convertFrame(frame);
                        handler.accept(message);
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
        for (String destination : handlers.keySet()) {
            try {
                connection.unsubscribe(destination);
            } catch (Exception e) {
                // Ignore
            }
        }
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String destination) {
        try {
            connection.unsubscribe(destination);
        } catch (Exception e) {
            // Ignore
        }
        handlers.remove(destination);
    }
    
    @Override
    public void acknowledge(Message message) {
        // STOMP auto-acknowledges
    }
    
    @Override
    public void acknowledgeAll() {
        // STOMP auto-acknowledges
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
    
    private Message convertFrame(StompConnection.StompFrame frame) {
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .topic(frame.getDestination())
                .body(frame.getBody())
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Consumer has been closed");
        }
    }
}
