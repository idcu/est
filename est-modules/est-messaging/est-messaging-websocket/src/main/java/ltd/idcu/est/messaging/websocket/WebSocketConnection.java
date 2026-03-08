package ltd.idcu.est.messaging.websocket;

import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class WebSocketConnection {
    
    private static final byte OPCODE_TEXT = 0x01;
    private static final byte OPCODE_BINARY = 0x02;
    private static final byte OPCODE_CLOSE = 0x08;
    private static final byte OPCODE_PING = 0x09;
    private static final byte OPCODE_PONG = 0x0A;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean connected;
    private volatile boolean closed;
    private final AtomicLong requestIdCounter;
    private final Map<String, Subscription> subscriptions;
    
    public WebSocketConnection(MessagingConfig config) {
        this.config = config;
        this.requestIdCounter = new AtomicLong(1);
        this.subscriptions = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 8080);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            performWebSocketHandshake();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to WebSocket server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 8080), e);
        }
    }
    
    private void performWebSocketHandshake() throws IOException {
        String key = generateWebSocketKey();
        String request = "GET / HTTP/1.1\r\n" +
                "Host: " + config.getHost() + "\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n" +
                "Sec-WebSocket-Key: " + key + "\r\n" +
                "Sec-WebSocket-Version: 13\r\n" +
                "\r\n";
        
        outputStream.write(request.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        
        byte[] response = new byte[1024];
        int read = inputStream.read(response);
        String responseStr = new String(response, 0, read, StandardCharsets.UTF_8);
        
        if (!responseStr.contains("101 Switching Protocols")) {
            throw new IOException("WebSocket handshake failed");
        }
    }
    
    private String generateWebSocketKey() {
        byte[] random = new byte[16];
        for (int i = 0; i < 16; i++) {
            random[i] = (byte) (Math.random() * 256);
        }
        return Base64.getEncoder().encodeToString(random);
    }
    
    public void send(String topic, byte[] message) throws MessagingException {
        try {
            String jsonMessage = "{\"topic\":\"" + topic + "\",\"body\":" + 
                    (message != null ? "\"" + Base64.getEncoder().encodeToString(message) + "\"" : "null") + "}";
            sendTextFrame(jsonMessage);
        } catch (IOException e) {
            throw new MessagingException("Failed to send message to topic: " + topic, e);
        }
    }
    
    public void subscribe(String topic) throws MessagingException {
        try {
            String subscribeMessage = "{\"action\":\"subscribe\",\"topic\":\"" + topic + "\"}";
            sendTextFrame(subscribeMessage);
            subscriptions.put(topic, new Subscription(topic));
        } catch (IOException e) {
            throw new MessagingException("Failed to subscribe to topic: " + topic, e);
        }
    }
    
    public WebSocketMessage receive() throws MessagingException {
        try {
            WebSocketFrame frame = readFrame();
            if (frame == null) {
                return null;
            }
            
            if (frame.opcode == OPCODE_TEXT) {
                String payload = new String(frame.payload, StandardCharsets.UTF_8);
                return parseMessage(payload);
            } else if (frame.opcode == OPCODE_CLOSE) {
                close();
                return null;
            } else if (frame.opcode == OPCODE_PING) {
                sendPong(frame.payload);
                return receive();
            }
            
            return null;
        } catch (IOException e) {
            throw new MessagingException("Failed to receive message", e);
        }
    }
    
    private WebSocketMessage parseMessage(String json) {
        try {
            int topicStart = json.indexOf("\"topic\":\"") + 9;
            int topicEnd = json.indexOf("\"", topicStart);
            String topic = json.substring(topicStart, topicEnd);
            
            int bodyStart = json.indexOf("\"body\":\"") + 8;
            int bodyEnd = json.indexOf("\"", bodyStart);
            String bodyBase64 = json.substring(bodyStart, bodyEnd);
            byte[] body = Base64.getDecoder().decode(bodyBase64);
            
            return new WebSocketMessage(topic, body);
        } catch (Exception e) {
            return null;
        }
    }
    
    private void sendTextFrame(String text) throws IOException {
        byte[] payload = text.getBytes(StandardCharsets.UTF_8);
        sendFrame(OPCODE_TEXT, payload);
    }
    
    private void sendPong(byte[] payload) throws IOException {
        sendFrame(OPCODE_PONG, payload);
    }
    
    private void sendFrame(byte opcode, byte[] payload) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(2 + (payload.length > 125 ? (payload.length > 65535 ? 8 : 2) : 0) + payload.length);
        
        buffer.put((byte) (0x80 | opcode));
        
        if (payload.length <= 125) {
            buffer.put((byte) payload.length);
        } else if (payload.length <= 65535) {
            buffer.put((byte) 126);
            buffer.putShort((short) payload.length);
        } else {
            buffer.put((byte) 127);
            buffer.putLong(payload.length);
        }
        
        buffer.put(payload);
        
        outputStream.write(buffer.array());
        outputStream.flush();
    }
    
    private WebSocketFrame readFrame() throws IOException {
        int firstByte = inputStream.read();
        if (firstByte == -1) {
            return null;
        }
        
        byte opcode = (byte) (firstByte & 0x0F);
        boolean fin = (firstByte & 0x80) != 0;
        
        int secondByte = inputStream.read();
        if (secondByte == -1) {
            return null;
        }
        
        boolean masked = (secondByte & 0x80) != 0;
        long payloadLength = secondByte & 0x7F;
        
        if (payloadLength == 126) {
            byte[] lengthBytes = new byte[2];
            inputStream.read(lengthBytes);
            payloadLength = ByteBuffer.wrap(lengthBytes).getShort() & 0xFFFF;
        } else if (payloadLength == 127) {
            byte[] lengthBytes = new byte[8];
            inputStream.read(lengthBytes);
            payloadLength = ByteBuffer.wrap(lengthBytes).getLong();
        }
        
        byte[] maskingKey = null;
        if (masked) {
            maskingKey = new byte[4];
            inputStream.read(maskingKey);
        }
        
        byte[] payload = new byte[(int) payloadLength];
        int read = 0;
        while (read < payloadLength) {
            int r = inputStream.read(payload, read, (int) payloadLength - read);
            if (r < 0) break;
            read += r;
        }
        
        if (masked && maskingKey != null) {
            for (int i = 0; i < payload.length; i++) {
                payload[i] ^= maskingKey[i % 4];
            }
        }
        
        return new WebSocketFrame(opcode, payload);
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
    
    private static class WebSocketFrame {
        final byte opcode;
        final byte[] payload;
        
        WebSocketFrame(byte opcode, byte[] payload) {
            this.opcode = opcode;
            this.payload = payload;
        }
    }
    
    public static class WebSocketMessage {
        private final String topic;
        private final byte[] body;
        
        public WebSocketMessage(String topic, byte[] body) {
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
