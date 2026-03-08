package ltd.idcu.est.messaging.amqp;

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

public class AmqpMessageConsumer implements MessageConsumer {
    
    private final AmqpConnection connection;
    private final MessagingConfig config;
    private final int channelId;
    private final ExecutorService executor;
    private final Map<String, Consumer<Message>> handlers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public AmqpMessageConsumer(AmqpConnection connection, MessagingConfig config) {
        this.connection = connection;
        this.config = config;
        this.channelId = connection.createChannel();
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
            connection.basicQos(channelId, config.getPrefetchCount());
            
            AmqpConnection.Frame frame = connection.readFrame();
            if (frame != null && frame.getType() == 1) {
                return parseMessage(frame);
            }
        } catch (Exception e) {
            throw new MessagingException("Failed to receive message", e);
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
        
        for (String queue : handlers.keySet()) {
            subscribeToQueue(queue, handler);
        }
    }
    
    @Override
    public void subscribe(String queue, Consumer<Message> handler) {
        checkClosed();
        
        if (queue == null || queue.isEmpty()) {
            throw new IllegalArgumentException("Queue cannot be null or empty");
        }
        
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }
        
        handlers.put(queue, handler);
        subscribeToQueue(queue, handler);
    }
    
    private void subscribeToQueue(String queue, Consumer<Message> handler) {
        try {
            connection.declareQueue(channelId, queue, true, false, false);
            connection.basicQos(channelId, config.getPrefetchCount());
            connection.basicConsume(channelId, queue);
            
            executor.submit(() -> {
                while (running.get() && !closed && connection.isConnected()) {
                    try {
                        AmqpConnection.Frame frame = connection.readFrame();
                        if (frame != null) {
                            Message message = parseMessage(frame);
                            if (message != null) {
                                handler.accept(message);
                                connection.basicAck(channelId, connection.nextDeliveryTag());
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
        } catch (Exception e) {
            throw new MessagingException("Failed to subscribe to queue: " + queue, e);
        }
    }
    
    @Override
    public void unsubscribe() {
        handlers.clear();
    }
    
    @Override
    public void unsubscribe(String queue) {
        handlers.remove(queue);
    }
    
    @Override
    public void acknowledge(Message message) {
        checkClosed();
        
        try {
            connection.basicAck(channelId, connection.nextDeliveryTag());
        } catch (Exception e) {
            throw new MessagingException("Failed to acknowledge message", e);
        }
    }
    
    @Override
    public void acknowledgeAll() {
        // Acknowledged individually
    }
    
    @Override
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        running.set(false);
        handlers.clear();
        
        try {
            connection.closeChannel(channelId);
        } catch (Exception e) {
            // Ignore
        }
        
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
    
    private Message parseMessage(AmqpConnection.Frame frame) {
        if (frame == null || frame.getPayload() == null) {
            return null;
        }
        
        byte[] payload = frame.getPayload();
        String body = new String(payload, StandardCharsets.UTF_8);
        
        return DefaultMessage.builder()
                .id(String.valueOf(System.currentTimeMillis()))
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
