package ltd.idcu.est.messaging.pulsar;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class PulsarMessages {
    
    private static PulsarConnection defaultConnection;
    private static final Object lock = new Object();
    
    private PulsarMessages() {
    }
    
    public static PulsarConnection connect(String host, int port) {
        return connect(MessagingConfig.pulsar(host, port));
    }
    
    public static PulsarConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.pulsar(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static PulsarConnection connect(MessagingConfig config) {
        PulsarConnection connection = new PulsarConnection(config);
        connection.connect();
        return connection;
    }
    
    public static PulsarConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 6650);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(PulsarConnection connection) {
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
    
    public static MessageProducer newProducer(PulsarConnection connection) {
        return new PulsarMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(PulsarConnection connection) {
        return new PulsarMessageConsumer(connection, connection.getConfig());
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
    
    public static PulsarMessagesBuilder builder() {
        return new PulsarMessagesBuilder();
    }
    
    public static class PulsarMessagesBuilder {
        private String host = "localhost";
        private int port = 6650;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public PulsarMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public PulsarMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public PulsarMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public PulsarMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public PulsarMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public PulsarConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            PulsarConnection connection = new PulsarConnection(config);
            connection.connect();
            return connection;
        }
    }
}
