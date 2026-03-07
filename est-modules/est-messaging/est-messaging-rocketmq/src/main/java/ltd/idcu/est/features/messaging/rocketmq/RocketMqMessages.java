package ltd.idcu.est.features.messaging.rocketmq;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class RocketMqMessages {
    
    private static RocketMqConnection defaultConnection;
    private static final Object lock = new Object();
    
    private RocketMqMessages() {
    }
    
    public static RocketMqConnection connect(String host, int port) {
        return connect(MessagingConfig.rocketmq(host, port));
    }
    
    public static RocketMqConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.rocketmq(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static RocketMqConnection connect(MessagingConfig config) {
        RocketMqConnection connection = new RocketMqConnection(config);
        connection.connect();
        return connection;
    }
    
    public static RocketMqConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 9876);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(RocketMqConnection connection) {
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
    
    public static MessageProducer newProducer(RocketMqConnection connection) {
        return new RocketMqMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(RocketMqConnection connection) {
        return new RocketMqMessageConsumer(connection, connection.getConfig());
    }
    
    public static void send(String topic, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(topic, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void send(String topic, String partitionKey, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(topic, partitionKey, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void subscribe(String topic, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(topic, handler);
    }
    
    public static RocketMqMessagesBuilder builder() {
        return new RocketMqMessagesBuilder();
    }
    
    public static class RocketMqMessagesBuilder {
        private String host = "localhost";
        private int port = 9876;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public RocketMqMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public RocketMqMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public RocketMqMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public RocketMqMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public RocketMqMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public RocketMqConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            RocketMqConnection connection = new RocketMqConnection(config);
            connection.connect();
            return connection;
        }
    }
}
