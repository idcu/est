package ltd.idcu.est.messaging.local;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingException;

import java.util.concurrent.CompletableFuture;

public class LocalMessageProducer implements MessageProducer {
    
    private final LocalMessageBroker broker;
    private volatile boolean closed;
    
    public LocalMessageProducer(LocalMessageBroker broker) {
        this.broker = broker;
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String queue = message.getQueue();
        if (queue == null || queue.isEmpty()) {
            throw new IllegalArgumentException("Message queue cannot be null or empty");
        }
        
        LocalMessageQueue messageQueue = broker.getQueue(queue);
        if (messageQueue == null) {
            throw new MessagingException("Queue does not exist: " + queue);
        }
        
        messageQueue.offer(message);
    }
    
    @Override
    public void send(String queue, Object body) {
        checkClosed();
        
        Message message = DefaultMessage.of(queue, body);
        send(message);
    }
    
    @Override
    public void send(String queue, String topic, Object body) {
        checkClosed();
        
        if (topic != null && !topic.isEmpty()) {
            LocalMessageTopic messageTopic = broker.getTopic(topic);
            if (messageTopic == null) {
                throw new MessagingException("Topic does not exist: " + topic);
            }
            
            Message message = DefaultMessage.of(queue, topic, body);
            messageTopic.publish(message);
        } else {
            send(queue, body);
        }
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String queue, Object body) {
        return CompletableFuture.runAsync(() -> send(queue, body));
    }
    
    @Override
    public void close() {
        closed = true;
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Producer has been closed");
        }
    }
}
