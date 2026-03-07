package ltd.idcu.est.features.messaging.nats;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.util.concurrent.CompletableFuture;

public class NatsMessageProducer implements MessageProducer {
    
    private final NatsConnection connection;
    private volatile boolean closed;
    
    public NatsMessageProducer(NatsConnection connection) {
        this.connection = connection;
        this.closed = false;
    }
    
    @Override
    public void send(Message message) {
        checkClosed();
        
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }
        
        String subject = message.getTopic();
        if (subject == null || subject.isEmpty()) {
            subject = message.getQueue();
        }
        
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Message subject cannot be null or empty");
        }
        
        String body = serializeBody(message.getBody());
        connection.publish(subject, body);
    }
    
    @Override
    public void send(String subject, Object body) {
        checkClosed();
        
        String bodyStr = serializeBody(body);
        connection.publish(subject, bodyStr);
    }
    
    @Override
    public void send(String subject, String subSubject, Object body) {
        checkClosed();
        
        String fullSubject = subSubject != null && !subSubject.isEmpty()
                ? subject + "." + subSubject
                : subject;
        
        String bodyStr = serializeBody(body);
        connection.publish(fullSubject, bodyStr);
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(Message message) {
        return CompletableFuture.runAsync(() -> send(message));
    }
    
    @Override
    public CompletableFuture<Void> sendAsync(String subject, Object body) {
        return CompletableFuture.runAsync(() -> send(subject, body));
    }
    
    @Override
    public void close() {
        closed = true;
    }
    
    private String serializeBody(Object body) {
        if (body == null) {
            return "";
        }
        
        if (body instanceof String) {
            return (String) body;
        }
        
        return body.toString();
    }
    
    private void checkClosed() {
        if (closed) {
            throw new MessagingException("Producer has been closed");
        }
    }
}
