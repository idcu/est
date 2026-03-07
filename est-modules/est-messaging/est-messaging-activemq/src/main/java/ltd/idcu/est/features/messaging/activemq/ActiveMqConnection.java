package ltd.idcu.est.features.messaging.activemq;

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

public class ActiveMqConnection {
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected;
    private volatile boolean closed;
    private final AtomicLong requestIdCounter;
    private final Map<String, Subscription> subscriptions;
    
    public ActiveMqConnection(MessagingConfig config) {
        this.config = config;
        this.requestIdCounter = new AtomicLong(1);
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 61616);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to ActiveMQ server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 61616), e);
        }
    }
    
    public void send(String destination, byte[] message) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4 + destination.length() + 4 + message.length);
            
            buffer.putInt(destination.length());
            buffer.put(destination.getBytes(StandardCharsets.UTF_8));
            buffer.putInt(message.length);
            buffer.put(message);
            
            outputStream.write(buffer.array());
            outputStream.flush();
        } catch (IOException e) {
            throw new MessagingException("Failed to send message to destination: " + destination, e);
        }
    }
    
    public void subscribe(String destination) throws MessagingException {
        subscriptions.put(destination, new Subscription(destination));
    }
    
    public ActiveMqMessage receive() throws MessagingException {
        try {
            byte[] destLenBytes = new byte[4];
            int read = inputStream.read(destLenBytes);
            if (read < 4) {
                return null;
            }
            
            int destLen = ByteBuffer.wrap(destLenBytes).getInt();
            byte[] destBytes = new byte[destLen];
            read = 0;
            while (read < destLen) {
                int r = inputStream.read(destBytes, read, destLen - read);
                if (r < 0) break;
                read += r;
            }
            String destination = new String(destBytes, StandardCharsets.UTF_8);
            
            byte[] bodyLenBytes = new byte[4];
            inputStream.read(bodyLenBytes);
            int bodyLen = ByteBuffer.wrap(bodyLenBytes).getInt();
            byte[] body = new byte[bodyLen];
            read = 0;
            while (read < bodyLen) {
                int r = inputStream.read(body, read, bodyLen - read);
                if (r < 0) break;
                read += r;
            }
            
            return new ActiveMqMessage(destination, body);
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
        final String destination;
        
        Subscription(String destination) {
            this.destination = destination;
        }
    }
    
    public static class ActiveMqMessage {
        private final String destination;
        private final byte[] body;
        
        public ActiveMqMessage(String destination, byte[] body) {
            this.destination = destination;
            this.body = body;
        }
        
        public String getDestination() {
            return destination;
        }
        
        public byte[] getBody() {
            return body;
        }
    }
}
