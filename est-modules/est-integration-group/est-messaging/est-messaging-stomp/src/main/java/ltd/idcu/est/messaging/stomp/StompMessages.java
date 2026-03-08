package ltd.idcu.est.messaging.stomp;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class StompMessages {
    
    private static StompConnection defaultConnection;
    private static final Object lock = new Object();
    
    private StompMessages() {
    }
    
    public static StompConnection connect(String host, int port) {
        return connect(MessagingConfig.stomp(host, port));
    }
    
    public static StompConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.stomp(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static StompConnection connect(MessagingConfig config) {
        StompConnection connection = new StompConnection(config);
        connection.connect();
        return connection;
    }
    
    public static StompConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 61613);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(StompConnection connection) {
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
    
    public static MessageProducer newProducer(StompConnection connection) {
        return new StompMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(StompConnection connection) {
        return new StompMessageConsumer(connection, connection.getConfig());
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
    
    public static void send(String destination, String subDestination, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(destination, subDestination, body);
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
    
    public static StompMessagesBuilder builder() {
        return new StompMessagesBuilder();
    }
    
    public static class StompMessagesBuilder {
        private String host = "localhost";
        private int port = 61613;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public StompMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public StompMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public StompMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public StompMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public StompMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public StompConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            StompConnection connection = new StompConnection(config);
            connection.connect();
            return connection;
        }
    }
}
