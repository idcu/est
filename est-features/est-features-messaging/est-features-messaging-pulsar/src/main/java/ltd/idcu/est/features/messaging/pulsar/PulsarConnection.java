package ltd.idcu.est.features.messaging.pulsar;

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
import java.util.concurrent.atomic.AtomicLong;

public class PulsarConnection {
    
    private static final int CONNECT_CMD = 0;
    private static final int SEND_CMD = 2;
    private static final int SUBSCRIBE_CMD = 4;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected;
    private volatile boolean closed;
    private final AtomicLong requestIdCounter;
    private final Map<String, Subscription> subscriptions;
    
    public PulsarConnection(MessagingConfig config) {
        this.config = config;
        this.requestIdCounter = new AtomicLong(1);
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 6650);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            performConnect();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to Pulsar server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 6650), e);
        }
    }
    
    private void performConnect() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        buffer.putInt(CONNECT_CMD);
        buffer.putLong(nextRequestId());
        
        String clientVersion = "EST-Client/1.0";
        writeString(buffer, clientVersion);
        writeString(buffer, "est-producer");
        
        writeString(buffer, config.getUsername());
        writeString(buffer, config.getPassword());
        
        int length = buffer.position();
        ByteBuffer fullBuffer = ByteBuffer.allocate(4 + length);
        fullBuffer.putInt(length);
        fullBuffer.put(buffer.array(), 0, length);
        
        outputStream.write(fullBuffer.array());
        outputStream.flush();
        
        readResponse();
    }
    
    public void send(String topic, byte[] message) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 + message.length);
            
            buffer.putInt(SEND_CMD);
            buffer.putLong(nextRequestId());
            
            long producerId = 1;
            buffer.putLong(producerId);
            long sequenceId = requestIdCounter.get();
            buffer.putLong(sequenceId);
            
            writeString(buffer, topic);
            
            buffer.putInt(message.length);
            buffer.put(message);
            
            int length = buffer.position();
            ByteBuffer fullBuffer = ByteBuffer.allocate(4 + length);
            fullBuffer.putInt(length);
            fullBuffer.put(buffer.array(), 0, length);
            
            outputStream.write(fullBuffer.array());
            outputStream.flush();
            
            readResponse();
        } catch (IOException e) {
            throw new MessagingException("Failed to send message to topic: " + topic, e);
        }
    }
    
    public void subscribe(String topic, String subscriptionName) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            buffer.putInt(SUBSCRIBE_CMD);
            buffer.putLong(nextRequestId());
            
            long consumerId = requestIdCounter.get();
            buffer.putLong(consumerId);
            
            writeString(buffer, topic);
            writeString(buffer, subscriptionName);
            
            int type = 0;
            buffer.putInt(type);
            
            int length = buffer.position();
            ByteBuffer fullBuffer = ByteBuffer.allocate(4 + length);
            fullBuffer.putInt(length);
            fullBuffer.put(buffer.array(), 0, length);
            
            outputStream.write(fullBuffer.array());
            outputStream.flush();
            
            subscriptions.put(topic, new Subscription(topic, subscriptionName, consumerId));
        } catch (IOException e) {
            throw new MessagingException("Failed to subscribe to topic: " + topic, e);
        }
    }
    
    public PulsarMessage receive() throws MessagingException {
        try {
            byte[] response = readResponse();
            if (response == null) {
                return null;
            }
            
            ByteBuffer buffer = ByteBuffer.wrap(response);
            int cmd = buffer.getInt();
            
            if (cmd == 3) {
                long consumerId = buffer.getLong();
                long messageId = buffer.getLong();
                
                String topic = readString(buffer);
                int payloadLength = buffer.getInt();
                byte[] payload = new byte[payloadLength];
                buffer.get(payload);
                
                return new PulsarMessage(topic, messageId, payload);
            }
            
            return null;
        } catch (IOException e) {
            throw new MessagingException("Failed to receive message", e);
        }
    }
    
    private byte[] readResponse() throws IOException {
        byte[] sizeBytes = new byte[4];
        inputStream.read(sizeBytes);
        int size = ByteBuffer.wrap(sizeBytes).getInt();
        
        if (size <= 0) {
            return null;
        }
        
        byte[] response = new byte[size];
        int read = 0;
        while (read < size) {
            int r = inputStream.read(response, read, size - read);
            if (r < 0) break;
            read += r;
        }
        
        return response;
    }
    
    private long nextRequestId() {
        return requestIdCounter.getAndIncrement();
    }
    
    private void writeString(ByteBuffer buffer, String value) {
        if (value == null) {
            buffer.putInt(-1);
        } else {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            buffer.putInt(bytes.length);
            buffer.put(bytes);
        }
    }
    
    private String readString(ByteBuffer buffer) {
        int length = buffer.getInt();
        if (length < 0) {
            return null;
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
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
        final String subscriptionName;
        final long consumerId;
        
        Subscription(String topic, String subscriptionName, long consumerId) {
            this.topic = topic;
            this.subscriptionName = subscriptionName;
            this.consumerId = consumerId;
        }
    }
    
    public static class PulsarMessage {
        private final String topic;
        private final long messageId;
        private final byte[] payload;
        
        public PulsarMessage(String topic, long messageId, byte[] payload) {
            this.topic = topic;
            this.messageId = messageId;
            this.payload = payload;
        }
        
        public String getTopic() {
            return topic;
        }
        
        public long getMessageId() {
            return messageId;
        }
        
        public byte[] getPayload() {
            return payload;
        }
    }
}
