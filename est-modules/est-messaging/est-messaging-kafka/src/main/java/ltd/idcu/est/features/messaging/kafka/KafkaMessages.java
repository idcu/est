package ltd.idcu.est.features.messaging.kafka;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class KafkaMessages {
    
    private static KafkaConnection defaultConnection;
    private static final Object lock = new Object();
    
    private KafkaMessages() {
    }
    
    public static KafkaConnection connect(String host, int port) {
        return connect(MessagingConfig.kafka(host, port));
    }
    
    public static KafkaConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.kafka(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static KafkaConnection connect(MessagingConfig config) {
        KafkaConnection connection = new KafkaConnection(config);
        connection.connect();
        return connection;
    }
    
    public static KafkaConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 9092);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(KafkaConnection connection) {
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
    
    public static MessageProducer newProducer(KafkaConnection connection) {
        return new KafkaMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(KafkaConnection connection) {
        return new KafkaMessageConsumer(connection, connection.getConfig());
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
    
    public static KafkaMessagesBuilder builder() {
        return new KafkaMessagesBuilder();
    }
    
    public static class KafkaMessagesBuilder {
        private String host = "localhost";
        private int port = 9092;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public KafkaMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public KafkaMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public KafkaMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public KafkaMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public KafkaMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public KafkaConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            KafkaConnection connection = new KafkaConnection(config);
            connection.connect();
            return connection;
        }
    }
}
