package ltd.idcu.est.features.messaging.nats;

import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NatsConnection {
    
    private final MessagingConfig config;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private volatile boolean connected;
    private volatile boolean closed;
    private final Map<String, SubscriptionListener> subscriptions;
    
    public NatsConnection(MessagingConfig config) {
        this.config = config;
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 4222);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            
            readInfo();
            sendConnect();
            readConnectResponse();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to NATS server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 4222), e);
        }
    }
    
    private void readInfo() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("INFO")) {
                break;
            }
        }
    }
    
    private void sendConnect() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONNECT {\"verbose\":false,\"pedantic\":false");
        
        if (config.getUsername() != null && !config.getUsername().isEmpty()) {
            sb.append(",\"user\":\"").append(config.getUsername()).append("\"");
        }
        if (config.getPassword() != null && !config.getPassword().isEmpty()) {
            sb.append(",\"pass\":\"").append(config.getPassword()).append("\"");
        }
        
        sb.append("}\r\n");
        writer.print(sb.toString());
        writer.flush();
    }
    
    private void readConnectResponse() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("PING")) {
                sendPong();
            } else if (line.startsWith("+OK")) {
                break;
            } else if (line.startsWith("-ERR")) {
                throw new IOException("NATS error: " + line);
            }
        }
    }
    
    private void sendPong() {
        writer.print("PONG\r\n");
        writer.flush();
    }
    
    public void publish(String subject, String message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        sb.append("PUB ").append(subject).append(" ").append(message.length()).append("\r\n");
        sb.append(message).append("\r\n");
        writer.print(sb.toString());
        writer.flush();
    }
    
    public void subscribe(String subject, SubscriptionListener listener) throws MessagingException {
        subscriptions.put(subject, listener);
        String sid = "sub-" + System.currentTimeMillis();
        writer.print("SUB " + subject + " " + sid + "\r\n");
        writer.flush();
        
        startListening();
    }
    
    public void unsubscribe(String subject) throws MessagingException {
        subscriptions.remove(subject);
        writer.print("UNSUB " + subject + "\r\n");
        writer.flush();
    }
    
    private void startListening() {
        Thread listener = Thread.ofVirtual().name("nats-listener").start(() -> {
            try {
                String line;
                while (!closed && (line = reader.readLine()) != null) {
                    if (line.startsWith("MSG")) {
                        handleMessage(line);
                    } else if (line.startsWith("PING")) {
                        sendPong();
                    }
                }
            } catch (IOException e) {
                if (!closed) {
                    // Handle error
                }
            }
        });
        listener.setDaemon(true);
    }
    
    private void handleMessage(String line) throws IOException {
        String[] parts = line.split(" ");
        if (parts.length >= 3) {
            String subject = parts[1];
            int length = Integer.parseInt(parts[parts.length - 1]);
            char[] payload = new char[length];
            reader.read(payload, 0, length);
            reader.readLine();
            
            String message = new String(payload);
            SubscriptionListener listener = subscriptions.get(subject);
            if (listener != null) {
                listener.onMessage(subject, message);
            }
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
    
    @FunctionalInterface
    public interface SubscriptionListener {
        void onMessage(String subject, String message);
    }
    
    public static class NatsMessage {
        private final String subject;
        private final String message;
        
        public NatsMessage(String subject, String message) {
            this.subject = subject;
            this.message = message;
        }
        
        public String getSubject() {
            return subject;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
