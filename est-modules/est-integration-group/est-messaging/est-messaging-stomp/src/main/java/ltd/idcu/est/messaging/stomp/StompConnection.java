package ltd.idcu.est.messaging.stomp;

import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StompConnection {
    
    private final MessagingConfig config;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private volatile boolean connected;
    private volatile boolean closed;
    private String sessionId;
    
    public StompConnection(MessagingConfig config) {
        this.config = config;
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 61613);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            
            sendConnectFrame();
            readConnectedFrame();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to STOMP server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 61613), e);
        }
    }
    
    private void sendConnectFrame() {
        Map<String, String> headers = new HashMap<>();
        headers.put("accept-version", "1.2");
        headers.put("host", config.getHost());
        
        if (config.getUsername() != null && !config.getUsername().isEmpty()) {
            headers.put("login", config.getUsername());
        }
        if (config.getPassword() != null && !config.getPassword().isEmpty()) {
            headers.put("passcode", config.getPassword());
        }
        
        sendFrame("CONNECT", headers, null);
    }
    
    private void readConnectedFrame() throws IOException {
        StompFrame frame = readFrame();
        if (frame == null || !"CONNECTED".equals(frame.command)) {
            throw new IOException("Expected CONNECTED frame, got: " + (frame != null ? frame.command : "null"));
        }
        sessionId = frame.headers.get("session");
    }
    
    public void subscribe(String destination) throws MessagingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("destination", destination);
        headers.put("id", "sub-" + System.currentTimeMillis());
        headers.put("ack", "auto");
        
        sendFrame("SUBSCRIBE", headers, null);
    }
    
    public void unsubscribe(String destination) throws MessagingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("destination", destination);
        
        sendFrame("UNSUBSCRIBE", headers, null);
    }
    
    public void send(String destination, String body) throws MessagingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("destination", destination);
        
        sendFrame("SEND", headers, body);
    }
    
    public StompFrame receive() throws MessagingException {
        try {
            return readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to receive frame", e);
        }
    }
    
    private void sendFrame(String command, Map<String, String> headers, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append(command).append("\n");
        
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
        }
        
        sb.append("\n");
        
        if (body != null) {
            sb.append(body);
        }
        
        sb.append("\0");
        
        writer.print(sb.toString());
        writer.flush();
    }
    
    private StompFrame readFrame() throws IOException {
        String command = null;
        Map<String, String> headers = new HashMap<>();
        StringBuilder body = new StringBuilder();
        
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            
            if (command == null) {
                command = line;
            } else {
                int colonIndex = line.indexOf(':');
                if (colonIndex > 0) {
                    String key = line.substring(0, colonIndex).trim();
                    String value = line.substring(colonIndex + 1).trim();
                    headers.put(key, value);
                }
            }
        }
        
        if (command == null) {
            return null;
        }
        
        int c;
        while ((c = reader.read()) != -1 && c != 0) {
            body.append((char) c);
        }
        
        return new StompFrame(command, headers, body.length() > 0 ? body.toString() : null);
    }
    
    public boolean isConnected() {
        return connected && !closed;
    }
    
    public MessagingConfig getConfig() {
        return config;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        connected = false;
        
        try {
            if (writer != null) {
                sendFrame("DISCONNECT", new HashMap<>(), null);
            }
        } catch (Exception e) {
            // Ignore
        }
        
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
    
    public static class StompFrame {
        final String command;
        final Map<String, String> headers;
        final String body;
        
        public StompFrame(String command, Map<String, String> headers, String body) {
            this.command = command;
            this.headers = headers;
            this.body = body;
        }
        
        public String getCommand() {
            return command;
        }
        
        public Map<String, String> getHeaders() {
            return headers;
        }
        
        public String getBody() {
            return body;
        }
        
        public String getDestination() {
            return headers.get("destination");
        }
    }
}
