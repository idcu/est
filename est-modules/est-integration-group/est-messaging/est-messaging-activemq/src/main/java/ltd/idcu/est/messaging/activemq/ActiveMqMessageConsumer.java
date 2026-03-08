package ltd.idcu.est.messaging.activemq;

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

public class ActiveMqMessageConsumer implements MessageConsumer {
    
    private final ActiveMqConnection connection;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public ActiveMqMessageConsumer(ActiveMqConnection connection, MessagingConfig config) {
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
            ActiveMqConnection.ActiveMqMessage activeMqMessage = connection.receive();
            if (activeMqMessage != null) {
                return convertMessage(activeMqMessage);
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
        try {
            connection.subscribe(destination);
        } catch (MessagingException e) {
            // Ignore for now
        }
        subscribeToDestination(destination, handler);
    }
    
    private void subscribeToDestination(String destination, Consumer<Message> handler) {
        executor.submit(() -> {
            while (running.get() && !closed && connection.isConnected()) {
                try {
                    ActiveMqConnection.ActiveMqMessage activeMqMessage = connection.receive();
                    if (activeMqMessage != null) {
                        Message message = convertMessage(activeMqMessage);
                        Consumer<Message> destHandler = handlers.get(activeMqMessage.getDestination());
                        if (destHandler != null) {
                            destHandler.accept(message);
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
    public void unsubscribe(String destination) {
        handlers.remove(destination);
    }
    
    @Override
    public void acknowledge(Message message) {
        // ActiveMQ acknowledgment handled by connection
    }
    
    @Override
    public void acknowledgeAll() {
        // ActiveMQ acknowledgment handled by connection
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
    
    private Message convertMessage(ActiveMqConnection.ActiveMqMessage activeMqMessage) {
        String body = activeMqMessage.getBody() != null ? new String(activeMqMessage.getBody(), StandardCharsets.UTF_8) : null;
        
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .topic(activeMqMessage.getDestination())
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
