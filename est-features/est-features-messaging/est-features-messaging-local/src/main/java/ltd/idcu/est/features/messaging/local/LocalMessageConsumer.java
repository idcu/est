package ltd.idcu.est.features.messaging.local;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class LocalMessageConsumer implements MessageConsumer {
    
    private final LocalMessageBroker broker;
    private final MessagingConfig config;
    private final ExecutorService executor;
    private final Map<String, CopyOnWriteArrayList<Consumer<Message>>> topicSubscribers;
    private final Map<String, Consumer<Message>> queueSubscribers;
    private final AtomicBoolean running;
    private volatile boolean closed;
    
    public LocalMessageConsumer(LocalMessageBroker broker, MessagingConfig config) {
        this.broker = broker;
        this.config = config;
        this.executor = config.createExecutorService();
        this.topicSubscribers = new ConcurrentHashMap<>();
        this.queueSubscribers = new ConcurrentHashMap<>();
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
        
        for (LocalMessageQueue queue : broker.getAllQueues()) {
            Message message = queue.poll();
            if (message != null) {
                return message;
            }
        }
        
        if (timeout > 0) {
            long endTime = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < endTime) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
                
                for (LocalMessageQueue queue : broker.getAllQueues()) {
                    Message message = queue.poll();
                    if (message != null) {
                        return message;
                    }
                }
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
        
        for (LocalMessageQueue queue : broker.getAllQueues()) {
            subscribeToQueue(queue.getName(), handler);
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
        
        LocalMessageTopic messageTopic = broker.getTopic(topic);
        if (messageTopic == null) {
            throw new MessagingException("Topic does not exist: " + topic);
        }
        
        messageTopic.subscribe(handler);
        
        topicSubscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(handler);
    }
    
    private void subscribeToQueue(String queueName, Consumer<Message> handler) {
        queueSubscribers.put(queueName, handler);
        
        executor.submit(() -> {
            while (running.get() && !closed) {
                try {
                    LocalMessageQueue queue = broker.getQueue(queueName);
                    if (queue != null) {
                        Message message = queue.poll();
                        if (message != null) {
                            handler.accept(message);
                        }
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    // Continue polling
                }
            }
        });
    }
    
    @Override
    public void unsubscribe() {
        for (Map.Entry<String, CopyOnWriteArrayList<Consumer<Message>>> entry : topicSubscribers.entrySet()) {
            LocalMessageTopic topic = broker.getTopic(entry.getKey());
            if (topic != null) {
                for (Consumer<Message> handler : entry.getValue()) {
                    topic.unsubscribe(handler);
                }
            }
        }
        topicSubscribers.clear();
        queueSubscribers.clear();
    }
    
    @Override
    public void unsubscribe(String topic) {
        CopyOnWriteArrayList<Consumer<Message>> handlers = topicSubscribers.remove(topic);
        if (handlers != null) {
            LocalMessageTopic messageTopic = broker.getTopic(topic);
            if (messageTopic != null) {
                for (Consumer<Message> handler : handlers) {
                    messageTopic.unsubscribe(handler);
                }
            }
        }
    }
    
    @Override
    public void acknowledge(Message message) {
        // Local implementation acknowledges automatically
    }
    
    @Override
    public void acknowledgeAll() {
        // Local implementation acknowledges automatically
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
