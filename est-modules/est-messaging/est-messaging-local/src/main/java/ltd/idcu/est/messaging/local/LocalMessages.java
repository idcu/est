package ltd.idcu.est.messaging.local;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.QueueConfig;

import java.util.function.Consumer;

public final class LocalMessages {
    
    private static final LocalMessageBroker DEFAULT_BROKER = new LocalMessageBroker();
    
    private LocalMessages() {
    }
    
    public static LocalMessageBroker newBroker() {
        return new LocalMessageBroker();
    }
    
    public static LocalMessageBroker newBroker(MessagingConfig config) {
        return new LocalMessageBroker(config);
    }
    
    public static LocalMessageBroker defaultBroker() {
        return DEFAULT_BROKER;
    }
    
    public static LocalMessageQueue createQueue(String name) {
        return DEFAULT_BROKER.createQueue(name);
    }
    
    public static LocalMessageQueue createQueue(String name, QueueConfig config) {
        return DEFAULT_BROKER.createQueue(name, config);
    }
    
    public static LocalMessageQueue getQueue(String name) {
        return DEFAULT_BROKER.getQueue(name);
    }
    
    public static boolean deleteQueue(String name) {
        return DEFAULT_BROKER.deleteQueue(name);
    }
    
    public static LocalMessageTopic createTopic(String name) {
        return DEFAULT_BROKER.createTopic(name);
    }
    
    public static LocalMessageTopic getTopic(String name) {
        return DEFAULT_BROKER.getTopic(name);
    }
    
    public static boolean deleteTopic(String name) {
        return DEFAULT_BROKER.deleteTopic(name);
    }
    
    public static MessageProducer newProducer() {
        return DEFAULT_BROKER.createProducer();
    }
    
    public static MessageConsumer newConsumer() {
        return DEFAULT_BROKER.createConsumer();
    }
    
    public static void send(String queue, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(queue, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void publish(String topic, Object body) {
        LocalMessageTopic messageTopic = getTopic(topic);
        if (messageTopic == null) {
            messageTopic = createTopic(topic);
        }
        
        Message message = ltd.idcu.est.messaging.api.DefaultMessage.of(null, topic, body);
        messageTopic.publish(message);
    }
    
    public static void subscribe(String topic, Consumer<Message> handler) {
        LocalMessageTopic messageTopic = getTopic(topic);
        if (messageTopic == null) {
            messageTopic = createTopic(topic);
        }
        messageTopic.subscribe(handler);
    }
    
    public static LocalMessagesBuilder builder() {
        return new LocalMessagesBuilder();
    }
    
    public static class LocalMessagesBuilder {
        private final MessagingConfig config = new MessagingConfig();
        
        public LocalMessagesBuilder maxQueueSize(int maxSize) {
            config.setMaxQueueSize(maxSize);
            return this;
        }
        
        public LocalMessagesBuilder useVirtualThreads(boolean use) {
            config.setUseVirtualThreads(use);
            return this;
        }
        
        public LocalMessageBroker build() {
            return new LocalMessageBroker(config);
        }
    }
}
