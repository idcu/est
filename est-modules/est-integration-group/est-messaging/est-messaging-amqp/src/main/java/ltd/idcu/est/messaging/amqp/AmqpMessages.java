package ltd.idcu.est.messaging.amqp;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class AmqpMessages {
    
    private static AmqpConnection defaultConnection;
    private static final Object lock = new Object();
    
    private AmqpMessages() {
    }
    
    public static AmqpConnection connect(String host, int port) {
        return connect(MessagingConfig.amqp(host, port));
    }
    
    public static AmqpConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.amqp(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static AmqpConnection connect(MessagingConfig config) {
        AmqpConnection connection = new AmqpConnection(config);
        connection.connect();
        return connection;
    }
    
    public static AmqpConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 5672);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(AmqpConnection connection) {
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
    
    public static MessageProducer newProducer(AmqpConnection connection) {
        return new AmqpMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(AmqpConnection connection) {
        return new AmqpMessageConsumer(connection, connection.getConfig());
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
    
    public static void send(String queue, String topic, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(queue, topic, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void subscribe(String queue, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(queue, handler);
    }
    
    public static AmqpMessagesBuilder builder() {
        return new AmqpMessagesBuilder();
    }
    
    public static class AmqpMessagesBuilder {
        private String host = "localhost";
        private int port = 5672;
        private String username = "guest";
        private String password = "guest";
        private String virtualHost = "/";
        private boolean useVirtualThreads = true;
        
        public AmqpMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public AmqpMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public AmqpMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public AmqpMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public AmqpMessagesBuilder virtualHost(String virtualHost) {
            this.virtualHost = virtualHost;
            return this;
        }
        
        public AmqpMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public AmqpConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setVirtualHost(virtualHost)
                    .setUseVirtualThreads(useVirtualThreads);
            
            AmqpConnection connection = new AmqpConnection(config);
            connection.connect();
            return connection;
        }
    }
}
