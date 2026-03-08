package ltd.idcu.est.messaging.websocket;

import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class WebSocketMessages {
    
    private static WebSocketConnection defaultConnection;
    private static final Object lock = new Object();
    
    private WebSocketMessages() {
    }
    
    public static WebSocketConnection connect(String host, int port) {
        return connect(MessagingConfig.websocket(host, port));
    }
    
    public static WebSocketConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.websocket(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static WebSocketConnection connect(MessagingConfig config) {
        WebSocketConnection connection = new WebSocketConnection(config);
        connection.connect();
        return connection;
    }
    
    public static WebSocketConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 8080);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(WebSocketConnection connection) {
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
    
    public static MessageProducer newProducer(WebSocketConnection connection) {
        return new WebSocketMessageProducer(connection);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(WebSocketConnection connection) {
        return new WebSocketMessageConsumer(connection, connection.getConfig());
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
    
    public static WebSocketMessagesBuilder builder() {
        return new WebSocketMessagesBuilder();
    }
    
    public static class WebSocketMessagesBuilder {
        private String host = "localhost";
        private int port = 8080;
        private String username = "";
        private String password = "";
        private boolean useVirtualThreads = true;
        
        public WebSocketMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public WebSocketMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public WebSocketMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public WebSocketMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public WebSocketMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public WebSocketConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setUseVirtualThreads(useVirtualThreads);
            
            WebSocketConnection connection = new WebSocketConnection(config);
            connection.connect();
            return connection;
        }
    }
}
