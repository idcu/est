package ltd.idcu.est.messaging.zeromq;

import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ZeroMqConnection {
    
    private static final byte SIGNATURE = (byte) 0xFF;
    private static final byte VERSION = (byte) 0x01;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected;
    private volatile boolean closed;
    private final AtomicInteger messageIdCounter;
    private final Map<String, Subscription> subscriptions;
    
    public ZeroMqConnection(MessagingConfig config) {
        this.config = config;
        this.messageIdCounter = new AtomicInteger(1);
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 5555);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            performGreeting();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to ZeroMQ server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 5555), e);
        }
    }
    
    private void performGreeting() throws IOException {
        byte[] greeting = new byte[64];
        greeting[0] = SIGNATURE;
        greeting[9] = VERSION;
        greeting[10] = 0x00;
        
        outputStream.write(greeting);
        outputStream.flush();
        
        byte[] response = new byte[64];
        inputStream.read(response);
    }
    
    public void publish(String topic, byte[] message) throws MessagingException {
        try {
            byte[] topicBytes = topic.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(1 + topicBytes.length + 1 + message.length);
            
            buffer.put((byte) 0x01);
            buffer.put(topicBytes);
            buffer.put((byte) 0x00);
            buffer.put(message);
            
            writeFrame(buffer.array());
        } catch (IOException e) {
            throw new MessagingException("Failed to publish message to topic: " + topic, e);
        }
    }
    
    public void subscribe(String topic) throws MessagingException {
        try {
            byte[] topicBytes = topic.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.allocate(1 + topicBytes.length);
            
            buffer.put((byte) 0x01);
            buffer.put(topicBytes);
            
            writeFrame(buffer.array());
            
            subscriptions.put(topic, new Subscription(topic));
        } catch (IOException e) {
            throw new MessagingException("Failed to subscribe to topic: " + topic, e);
        }
    }
    
    public ZeroMqMessage receive() throws MessagingException {
        try {
            byte[] frame = readFrame();
            if (frame == null || frame.length < 2) {
                return null;
            }
            
            int nullIndex = -1;
            for (int i = 0; i < frame.length; i++) {
                if (frame[i] == 0x00) {
                    nullIndex = i;
                    break;
                }
            }
            
            if (nullIndex == -1) {
                return null;
            }
            
            String topic = new String(frame, 1, nullIndex - 1, StandardCharsets.UTF_8);
            byte[] body = new byte[frame.length - nullIndex - 1];
            System.arraycopy(frame, nullIndex + 1, body, 0, body.length);
            
            return new ZeroMqMessage(topic, body);
        } catch (IOException e) {
            throw new MessagingException("Failed to receive message", e);
        }
    }
    
    private void writeFrame(byte[] data) throws IOException {
        if (data.length < 255) {
            outputStream.write((byte) data.length);
        } else {
            outputStream.write((byte) 0xFF);
            outputStream.write(ByteBuffer.allocate(8).putLong(data.length).array());
        }
        outputStream.write(data);
        outputStream.flush();
    }
    
    private byte[] readFrame() throws IOException {
        int firstByte = inputStream.read();
        if (firstByte == -1) {
            return null;
        }
        
        long length;
        if (firstByte == 0xFF) {
            byte[] lengthBytes = new byte[8];
            inputStream.read(lengthBytes);
            length = ByteBuffer.wrap(lengthBytes).getLong();
        } else {
            length = firstByte;
        }
        
        byte[] data = new byte[(int) length];
        int read = 0;
        while (read < length) {
            int r = inputStream.read(data, read, (int) length - read);
            if (r < 0) break;
            read += r;
        }
        
        return data;
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
    
    public static class ZeroMqMessage {
        private final String topic;
        private final byte[] body;
        
        public ZeroMqMessage(String topic, byte[] body) {
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
