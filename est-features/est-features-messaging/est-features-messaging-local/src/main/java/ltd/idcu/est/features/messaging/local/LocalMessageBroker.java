package ltd.idcu.est.features.messaging.local;

import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.QueueConfig;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalMessageBroker {
    
    private final Map<String, LocalMessageQueue> queues;
    private final Map<String, LocalMessageTopic> topics;
    private final MessagingConfig config;
    
    public LocalMessageBroker() {
        this(MessagingConfig.local());
    }
    
    public LocalMessageBroker(MessagingConfig config) {
        this.config = config;
        this.queues = new ConcurrentHashMap<>();
        this.topics = new ConcurrentHashMap<>();
    }
    
    public LocalMessageQueue createQueue(String name) {
        return createQueue(name, QueueConfig.defaultConfig(name));
    }
    
    public LocalMessageQueue createQueue(String name, QueueConfig config) {
        return queues.computeIfAbsent(name, k -> {
            LocalMessageQueue queue = new LocalMessageQueue(name, config);
            queue.create(config);
            return queue;
        });
    }
    
    public LocalMessageQueue getQueue(String name) {
        return queues.get(name);
    }
    
    public boolean deleteQueue(String name) {
        LocalMessageQueue queue = queues.remove(name);
        if (queue != null) {
            queue.delete();
            return true;
        }
        return false;
    }
    
    public Collection<LocalMessageQueue> getAllQueues() {
        return queues.values();
    }
    
    public LocalMessageTopic createTopic(String name) {
        return topics.computeIfAbsent(name, k -> {
            LocalMessageTopic topic = new LocalMessageTopic(name);
            topic.create();
            return topic;
        });
    }
    
    public LocalMessageTopic getTopic(String name) {
        return topics.get(name);
    }
    
    public boolean deleteTopic(String name) {
        LocalMessageTopic topic = topics.remove(name);
        if (topic != null) {
            topic.delete();
            return true;
        }
        return false;
    }
    
    public Collection<LocalMessageTopic> getAllTopics() {
        return topics.values();
    }
    
    public MessageProducer createProducer() {
        return new LocalMessageProducer(this);
    }
    
    public MessageConsumer createConsumer() {
        return new LocalMessageConsumer(this, config);
    }
    
    public void close() {
        for (LocalMessageQueue queue : queues.values()) {
            queue.delete();
        }
        for (LocalMessageTopic topic : topics.values()) {
            topic.delete();
        }
        queues.clear();
        topics.clear();
    }
    
    public int getQueueCount() {
        return queues.size();
    }
    
    public int getTopicCount() {
        return topics.size();
    }
}
