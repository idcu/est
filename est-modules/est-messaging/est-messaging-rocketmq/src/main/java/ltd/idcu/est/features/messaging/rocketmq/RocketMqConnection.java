package ltd.idcu.est.features.messaging.rocketmq;

import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RocketMqConnection {
    
    private static final int HEADER_LENGTH = 4;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected;
    private volatile boolean closed;
    private final AtomicInteger requestIdCounter;
    private final Map<String, Subscription> subscriptions;
    
    public RocketMqConnection(MessagingConfig config) {
        this.config = config;
        this.requestIdCounter = new AtomicInteger(1);
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 9876);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to RocketMQ server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 9876), e);
        }
    }
    
    public void send(String topic, byte[] message) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_LENGTH + 4 + topic.length() + 4 + message.length);
            
            buffer.putInt(0);
            buffer.putInt(topic.length());
            buffer.put(topic.getBytes(StandardCharsets.UTF_8));
            buffer.putInt(message.length);
            buffer.put(message);
            
            int totalLength = buffer.position();
            buffer.putInt(0, totalLength - HEADER_LENGTH);
            
            outputStream.write(buffer.array());
            outputStream.flush();
        } catch (IOException e) {
            throw new MessagingException("Failed to send message to topic: " + topic, e);
        }
    }
    
    public void subscribe(String topic) throws MessagingException {
        subscriptions.put(topic, new Subscription(topic));
    }
    
    public RocketMqMessage receive() throws MessagingException {
        try {
            byte[] sizeBytes = new byte[HEADER_LENGTH];
            int read = inputStream.read(sizeBytes);
            if (read < HEADER_LENGTH) {
                return null;
            }
            
            int size = ByteBuffer.wrap(sizeBytes).getInt();
            byte[] data = new byte[size];
            read = 0;
            while (read < size) {
                int r = inputStream.read(data, read, size - read);
                if (r < 0) break;
                read += r;
            }
            
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int topicLength = buffer.getInt();
            byte[] topicBytes = new byte[topicLength];
            buffer.get(topicBytes);
            String topic = new String(topicBytes, StandardCharsets.UTF_8);
            
            int bodyLength = buffer.getInt();
            byte[] body = new byte[bodyLength];
            buffer.get(body);
            
            return new RocketMqMessage(topic, body);
        } catch (IOException e) {
            throw new MessagingException("Failed to receive message", e);
        }
    }
    
    public boolean isConnected() {
        return connected && !closed;
    }
    
    public MessagingConfig getConfig() {
        return config;
    }
    
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        connected = false;
        subscriptions.clear();
        
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
    
    private static class Subscription {
        final String topic;
        
        Subscription(String topic) {
            this.topic = topic;
        }
    }
    
    public static class RocketMqMessage {
        private final String topic;
        private final byte[] body;
        
        public RocketMqMessage(String topic, byte[] body) {
            this.topic = topic;
            this.body = body;
        }
        
        public String getTopic() {
            return topic;
        }
        
        public byte[] getBody() {
            return body;
        }
    }
}
