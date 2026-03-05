package ltd.idcu.est.features.messaging.zeromq;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class ZeroMqMessages {
    
    private static ZeroMqConnection defaultConnection;
    private static final Object lock = new Object();
    
    private ZeroMqMessages() {
    }
    
    public static ZeroMqConnection connect(String host, int port) {
        return connect(MessagingConfig.zeromq(host, port));
    }
    
    public static ZeroMqConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.zeromq(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static ZeroMqConnection connect(MessagingConfig config) {
        ZeroMqConnection connection = new ZeroMqConnection(config);
        connection.connect();
        return connection;
    }
    
    public static ZeroMqConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 5555);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(ZeroMqConnection connection) {
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
    
    public static MessageProducer newProducer(ZeroMqConnection connection) {
        return new ZeroMqMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(ZeroMqConnection connection) {
        return new ZeroMqMessageConsumer(connection, connection.getConfig());
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
    
    public static ZeroMqMessagesBuilder builder() {
        return new ZeroMqMessagesBuilder();
    }
    
    public static class ZeroMqMessagesBuilder {
        private String host = "localhost";
        private int port = 5555;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public ZeroMqMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public ZeroMqMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public ZeroMqMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public ZeroMqMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public ZeroMqMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public ZeroMqConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            ZeroMqConnection connection = new ZeroMqConnection(config);
            connection.connect();
            return connection;
        }
    }
}
