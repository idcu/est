package ltd.idcu.est.features.messaging.mqtt;

import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessagingConfig;

import java.util.function.Consumer;

public final class MqttMessages {
    
    private static MqttConnection defaultConnection;
    private static final Object lock = new Object();
    
    private MqttMessages() {
    }
    
    public static MqttConnection connect(String host, int port) {
        return connect(MessagingConfig.mqtt(host, port));
    }
    
    public static MqttConnection connect(String host, int port, String username, String password) {
        MessagingConfig config = MessagingConfig.mqtt(host, port)
                .setUsername(username)
                .setPassword(password);
        return connect(config);
    }
    
    public static MqttConnection connect(MessagingConfig config) {
        MqttConnection connection = new MqttConnection(config);
        connection.connect();
        return connection;
    }
    
    public static MqttConnection defaultConnection() {
        if (defaultConnection == null) {
            synchronized (lock) {
                if (defaultConnection == null) {
                    defaultConnection = connect("localhost", 1883);
                }
            }
        }
        return defaultConnection;
    }
    
    public static void setDefaultConnection(MqttConnection connection) {
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
    
    public static MessageProducer newProducer(MqttConnection connection) {
        return new MqttMessageProducer(connection);
    }
    
    public static MessageProducer newProducer(MqttConnection connection, int qos) {
        return new MqttMessageProducer(connection, qos);
    }
    
    public static MessageConsumer newConsumer() {
        return newConsumer(defaultConnection());
    }
    
    public static MessageConsumer newConsumer(MqttConnection connection) {
        return new MqttMessageConsumer(connection, connection.getConfig());
    }
    
    public static void publish(String topic, Object body) {
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
    
    public static void subscribe(String topic, Consumer<Message> handler) {
        MessageConsumer consumer = newConsumer();
        consumer.subscribe(topic, handler);
    }
    
    public static MqttMessagesBuilder builder() {
        return new MqttMessagesBuilder();
    }
    
    public static class MqttMessagesBuilder {
        private String host = "localhost";
        private int port = 1883;
        private String username = "";
        private String password = "";
        private String clientId = "est-mqtt-client";
        private boolean useVirtualThreads = true;
        private int qos = 1;
        
        public MqttMessagesBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public MqttMessagesBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public MqttMessagesBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public MqttMessagesBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public MqttMessagesBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public MqttMessagesBuilder useVirtualThreads(boolean use) {
            this.useVirtualThreads = use;
            return this;
        }
        
        public MqttMessagesBuilder qos(int qos) {
            this.qos = qos;
            return this;
        }
        
        public MqttConnection build() {
            MessagingConfig config = new MessagingConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setVirtualHost(clientId)
                    .setUseVirtualThreads(useVirtualThreads);
            
            MqttConnection connection = new MqttConnection(config);
            connection.connect();
            return connection;
        }
    }
}
