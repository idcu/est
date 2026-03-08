package ltd.idcu.est.messaging.activemq;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class ActiveMqMessages {
    
    private static ActiveMqConnection defaultConnection;
    private static final Object lock = new Object();
    
    private ActiveMqMessages() {
    }
    
    public static ActiveMqConnection connect(String host, int port) {
        return connect(MessagingConfig.activemq(host, port));
    }
    
    public static ActiveMqConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.activemq(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static ActiveMqConnection connect(MessagingConfig config) {
        ActiveMqConnection connection = new ActiveMqConnection(config);
        connection.connect();
        return connection;
    }
    
    public static ActiveMqConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 61616);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(ActiveMqConnection connection) {
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
    
    public static MessageProducer newProducer(ActiveMqConnection connection) {
        return new ActiveMqMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(ActiveMqConnection connection) {
        return new ActiveMqMessageConsumer(connection, connection.getConfig());
    }
    
    public static void send(String destination, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(destination, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void send(String destination, String partitionKey, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(destination, partitionKey, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void subscribe(String destination, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(destination, handler);
    }
    
    public static ActiveMqMessagesBuilder builder() {
        return new ActiveMqMessagesBuilder();
    }
    
    public static class ActiveMqMessagesBuilder {
        private String host = "localhost";
        private int port = 61616;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public ActiveMqMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public ActiveMqMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public ActiveMqMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public ActiveMqMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public ActiveMqMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public ActiveMqConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            ActiveMqConnection connection = new ActiveMqConnection(config);
            connection.connect();
            return connection;
        }
    }
}
