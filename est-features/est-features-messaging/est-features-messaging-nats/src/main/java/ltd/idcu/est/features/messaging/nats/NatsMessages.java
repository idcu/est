package ltd.idcu.est.features.messaging.nats;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class NatsMessages {
    
    private static NatsConnection defaultConnection;
    private static final Object lock = new Object();
    
    private NatsMessages() {
    }
    
    public static NatsConnection connect(String host, int port) {
        return connect(MessagingConfig.nats(host, port));
    }
    
    public static NatsConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.nats(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static NatsConnection connect(MessagingConfig config) {
        NatsConnection connection = new NatsConnection(config);
        connection.connect();
        return connection;
    }
    
    public static NatsConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 4222);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(NatsConnection connection) {
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
    
    public static MessageProducer newProducer(NatsConnection connection) {
        return new NatsMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(NatsConnection connection) {
        return new NatsMessageConsumer(connection, connection.getConfig());
    }
    
    public static void publish(String subject, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(subject, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void publish(String subject, String subSubject, Object body) {
        MessageProducer producer = null;
        try {
            producer = newProducer();
            producer.send(subject, subSubject, body);
        } finally {
            if (producer != null) {
                producer.close();
            }
        }
    }
    
    public static void subscribe(String subject, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(subject, handler);
    }
    
    public static NatsMessagesBuilder builder() {
        return new NatsMessagesBuilder();
    }
    
    public static class NatsMessagesBuilder {
        private String host = "localhost";
        private int port = 4222;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public NatsMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public NatsMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public NatsMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public NatsMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public NatsMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public NatsConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            NatsConnection connection = new NatsConnection(config);
            connection.connect();
            return connection;
        }
    }
}
