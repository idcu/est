package ltd.idcu.est.messaging.redis;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class RedisMessages {
    
    private static RedisConnection defaultConnection;
    private static final Object lock = new Object();
    
    private RedisMessages() {
    }
    
    public static RedisConnection connect(String host, int port) {
        return connect(MessagingConfig.redis(host, port));
    }
    
    public static RedisConnection connect(String host, int port, String password) {
        MessagingConfig config = MessagingConfig.redis(host, port)
                .setPassword(password);
        return connect(config);
    }
    
    public static RedisConnection connect(MessagingConfig config) {
        RedisConnection connection = new RedisConnection(config);
        connection.connect();
        return connection;
    }
    
    public static RedisConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 6379);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(RedisConnection connection) {
        synchronized (lock) {
            if (defaultConnection != null && defaultConnection.isConnected()) {
                defaultConnection.close();
            }
            defaultConnection = connection;
        }
    }
    
    public static MessageProducer newProducer() {
        return newProducer(defaultConnection());
    }
    
    public static MessageProducer newProducer(RedisConnection connection) {
        return new RedisMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(RedisConnection connection) {
        return new RedisMessageConsumer(connection, connection.getConfig());
    }
    
    public static void publish(String channel, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(channel, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void publish(String channel, String subChannel, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(channel, subChannel, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void subscribe(String channel, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(channel, handler);
    }
    
    public static RedisMessagesBuilder builder() {
        return new RedisMessagesBuilder();
    }
    
    public static class RedisMessagesBuilder {
        private String host = "localhost";
        private int port = 6379;
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public RedisMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public RedisMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public RedisMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public RedisMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public RedisConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            RedisConnection connection = new RedisConnection(config);
            connection.connect();
            return connection;
        }
    }
}
