package ltd.idcu.est.features.messaging.amqp;

import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

public final class AmqpMessages {
    
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
    
    public static MessageProducer newProducer(AmqpConnection connection) {
        return new AmqpMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer(AmqpConnection connection) {
        return new AmqpMessageConsumer(connection, connection.getConfig());
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
