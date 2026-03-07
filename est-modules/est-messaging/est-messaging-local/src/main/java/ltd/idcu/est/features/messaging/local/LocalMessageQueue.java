package ltd.idcu.est.features.messaging.local;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageStats;
import ltd.idcu.est.features.messaging.api.MessagingException;
import ltd.idcu.est.features.messaging.api.QueueConfig;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

public class LocalMessageQueue implements ltd.idcu.est.features.messaging.api.MessageQueue {
    
    private final String name;
    private final PriorityQueue<MessageWrapper> queue;
    private final MessageStats stats;
    private final QueueConfig config;
    private final ReentrantLock lock;
    private volatile boolean exists;
    
    public LocalMessageQueue(String name) {
        this(name, QueueConfig.defaultConfig(name));
    }
    
    public LocalMessageQueue(String name, QueueConfig config) {
        this.name = name;
        this.config = config;
        this.queue = new PriorityQueue<>(Comparator
                .comparingInt(MessageWrapper::getPriority).reversed()
                .thenComparingLong(MessageWrapper::getTimestamp));
        this.stats = new MessageStats();
        this.lock = new ReentrantLock();
        this.exists = false;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean create() {
        return create(config);
    }
    
    @Override
    public boolean create(QueueConfig config) {
        lock.lock();
        try {
            if (exists) {
                return false;
            }
            exists = true;
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean delete() {
        lock.lock();
        try {
            if (!exists) {
                return false;
            }
            queue.clear();
            exists = false;
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean exists() {
        return exists;
    }
    
    @Override
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void clear() {
        lock.lock();
        try {
            queue.clear();
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public Message peek() {
        lock.lock();
        try {
            checkExists();
            while (!queue.isEmpty()) {
                MessageWrapper wrapper = queue.peek();
                if (wrapper.getMessage().isExpired()) {
                    queue.poll();
                    stats.incrementExpiredCount();
                    continue;
                }
                return wrapper.getMessage();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public Message poll() {
        lock.lock();
        try {
            checkExists();
            while (!queue.isEmpty()) {
                MessageWrapper wrapper = queue.poll();
                if (wrapper.getMessage().isExpired()) {
                    stats.incrementExpiredCount();
                    continue;
                }
                stats.incrementReceivedCount();
                return wrapper.getMessage();
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public boolean offer(Message message) {
        lock.lock();
        try {
            checkExists();
            
            if (config.getMaxLength() > 0 && queue.size() >= config.getMaxLength()) {
                throw new MessagingException("Queue is full: " + name);
            }
            
            if (message.isExpired()) {
                stats.incrementExpiredCount();
                return false;
            }
            
            queue.offer(new MessageWrapper(message));
            stats.incrementSentCount();
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public MessageStats getStats() {
        return stats.snapshot();
    }
    
    private void checkExists() {
        if (!exists) {
            throw new MessagingException("Queue does not exist: " + name);
        }
    }
    
    private static class MessageWrapper {
        private final Message message;
        private final int priority;
        private final long timestamp;
        
        MessageWrapper(Message message) {
            this.message = message;
            this.priority = message.getPriority();
            this.timestamp = message.getTimestamp();
        }
        
        Message getMessage() {
            return message;
        }
        
        int getPriority() {
            return priority;
        }
        
        long getTimestamp() {
            return timestamp;
        }
    }
}
