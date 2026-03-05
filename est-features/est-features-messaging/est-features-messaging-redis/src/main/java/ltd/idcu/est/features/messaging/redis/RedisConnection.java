package ltd.idcu.est.features.messaging.redis;

import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisConnection {
    
    private final MessagingConfig config;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private volatile boolean connected;
    private volatile boolean closed;
    private final Map<String, SubscriptionListener> subscriptions;
    
    public RedisConnection(MessagingConfig config) {
        this.config = config;
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 6379);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            
            if (config.getPassword() != null && !config.getPassword().isEmpty()) {
                sendCommand("AUTH", config.getPassword());
                readResponse();
            }
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to Redis server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 6379), e);
        }
    }
    
    public void publish(String channel, String message) throws MessagingException {
        sendCommand("PUBLISH", channel, message);
        readResponse();
    }
    
    public void subscribe(String channel, SubscriptionListener listener) throws MessagingException {
        subscriptions.put(channel, listener);
        sendCommand("SUBSCRIBE", channel);
    }
    
    public void unsubscribe(String channel) throws MessagingException {
        subscriptions.remove(channel);
        sendCommand("UNSUBSCRIBE", channel);
    }
    
    public List<Object> receiveMessages() throws MessagingException {
        try {
            List<Object> messages = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("*")) {
                    int count = Integer.parseInt(line.substring(1));
                    List<String> parts = new ArrayList<>();
                    for (int i = 0; i < count; i++) {
                        reader.readLine();
                        String part = reader.readLine();
                        parts.add(part);
                    }
                    
                    if (parts.size() >= 3 && "message".equals(parts.get(0))) {
                        String channel = parts.get(1);
                        String message = parts.get(2);
                        SubscriptionListener listener = subscriptions.get(channel);
                        if (listener != null) {
                            listener.onMessage(channel, message);
                        }
                        messages.add(new RedisMessage(channel, message));
                    }
                }
            }
            return messages;
        } catch (IOException e) {
            throw new MessagingException("Failed to receive messages", e);
        }
    }
    
    private void sendCommand(String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(args.length).append("\r\n");
        for (String arg : args) {
            sb.append("$").append(arg.getBytes(StandardCharsets.UTF_8).length).append("\r\n");
            sb.append(arg).append("\r\n");
        }
        writer.print(sb.toString());
        writer.flush();
    }
    
    private String readResponse() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }
        return line;
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
    
    @FunctionalInterface
    public interface SubscriptionListener {
        void onMessage(String channel, String message);
    }
    
    public static class RedisMessage {
        private final String channel;
        private final String message;
        
        public RedisMessage(String channel, String message) {
            this.channel = channel;
            this.message = message;
        }
        
        public String getChannel() {
            return channel;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
