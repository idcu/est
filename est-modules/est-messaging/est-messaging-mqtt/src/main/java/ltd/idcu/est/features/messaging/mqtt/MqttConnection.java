package ltd.idcu.est.features.messaging.mqtt;

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

public class MqttConnection {
    
    private static final byte CONNECT = 0x10;
    private static final byte CONNACK = 0x20;
    private static final byte PUBLISH = 0x30;
    private static final byte PUBACK = 0x40;
    private static final byte SUBSCRIBE = (byte) 0x82;
    private static final byte SUBACK = (byte) 0x90;
    private static final byte UNSUBSCRIBE = (byte) 0xA2;
    private static final byte UNSUBACK = (byte) 0xB0;
    private static final byte PINGREQ = (byte) 0xC0;
    private static final byte PINGRESP = (byte) 0xD0;
    private static final byte DISCONNECT = (byte) 0xE0;
    
    private static final int MQTT_VERSION = 4;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final AtomicInteger packetIdCounter;
    private final Map<Integer, PendingPacket> pendingPackets;
    private volatile boolean connected;
    private volatile boolean closed;
    
    public MqttConnection(MessagingConfig config) {
        this.config = config;
        this.packetIdCounter = new AtomicInteger(1);
        this.pendingPackets = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort());
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            sendConnect();
            receiveConnack();
            
            connected = true;
            startHeartbeat();
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to MQTT server: " + config.getHost() + ":" + config.getPort(), e);
        }
    }
    
    private void sendConnect() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        
        buffer.put((byte) 0);
        buffer.put((byte) 4);
        buffer.put("MQTT".getBytes(StandardCharsets.UTF_8));
        buffer.put((byte) MQTT_VERSION);
        
        byte connectFlags = 0x02;
        if (!config.getUsername().isEmpty()) {
            connectFlags |= 0x80;
        }
        if (!config.getPassword().isEmpty()) {
            connectFlags |= 0x40;
        }
        buffer.put(connectFlags);
        
        buffer.putShort((short) 60);
        
        writeMqttString(buffer, config.getVirtualHost().isEmpty() ? "est-client" : config.getVirtualHost());
        
        if (!config.getUsername().isEmpty()) {
            writeMqttString(buffer, config.getUsername());
        }
        if (!config.getPassword().isEmpty()) {
            writeMqttString(buffer, config.getPassword());
        }
        
        byte[] payload = new byte[buffer.position()];
        buffer.flip();
        buffer.get(payload);
        
        writePacket(CONNECT, payload);
    }
    
    private void receiveConnack() throws IOException {
        byte[] header = new byte[4];
        int read = inputStream.read(header);
        if (read < 4 || header[0] != CONNACK) {
            throw new MessagingException("Invalid CONNACK from server");
        }
        
        byte returnCode = header[3];
        if (returnCode != 0) {
            throw new MessagingException("Connection refused, return code: " + returnCode);
        }
    }
    
    public void publish(String topic, byte[] payload, int qos) throws MessagingException {
        checkConnected();
        
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512 + payload.length);
            writeMqttString(buffer, topic);
            
            if (qos > 0) {
                int packetId = nextPacketId();
                buffer.putShort((short) packetId);
                pendingPackets.put(packetId, new PendingPacket(topic, payload));
            }
            
            buffer.put(payload);
            
            byte[] data = new byte[buffer.position()];
            buffer.flip();
            buffer.get(data);
            
            byte flags = (byte) (qos << 1);
            writePacket((byte) (PUBLISH | flags), data);
        } catch (IOException e) {
            throw new MessagingException("Failed to publish to topic: " + topic, e);
        }
    }
    
    public void subscribe(String topic, int qos) throws MessagingException {
        checkConnected();
        
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            int packetId = nextPacketId();
            buffer.putShort((short) packetId);
            writeMqttString(buffer, topic);
            buffer.put((byte) qos);
            
            byte[] data = new byte[buffer.position()];
            buffer.flip();
            buffer.get(data);
            
            writePacket(SUBSCRIBE, data);
            
            byte[] response = readPacket();
            if (response == null || response.length < 3 || response[0] != SUBACK) {
                throw new MessagingException("Invalid SUBACK from server");
            }
        } catch (IOException e) {
            throw new MessagingException("Failed to subscribe to topic: " + topic, e);
        }
    }
    
    public void unsubscribe(String topic) throws MessagingException {
        checkConnected();
        
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            int packetId = nextPacketId();
            buffer.putShort((short) packetId);
            writeMqttString(buffer, topic);
            
            byte[] data = new byte[buffer.position()];
            buffer.flip();
            buffer.get(data);
            
            writePacket(UNSUBSCRIBE, data);
            
            byte[] response = readPacket();
            if (response == null || response.length < 2 || response[0] != UNSUBACK) {
                throw new MessagingException("Invalid UNSUBACK from server");
            }
        } catch (IOException e) {
            throw new MessagingException("Failed to unsubscribe from topic: " + topic, e);
        }
    }
    
    public MqttMessage receive() throws MessagingException {
        checkConnected();
        
        try {
            byte[] packet = readPacket();
            if (packet == null) {
                return null;
            }
            
            byte type = (byte) (packet[0] & 0xF0);
            if (type == PUBLISH) {
                return parsePublish(packet);
            } else if (type == PINGRESP) {
                return receive();
            }
            
            return null;
        } catch (IOException e) {
            throw new MessagingException("Failed to receive message", e);
        }
    }
    
    private MqttMessage parsePublish(byte[] packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet);
        buffer.position(1);
        
        int remainingLength = readRemainingLength(buffer);
        
        int topicLength = buffer.getShort() & 0xFFFF;
        byte[] topicBytes = new byte[topicLength];
        buffer.get(topicBytes);
        String topic = new String(topicBytes, StandardCharsets.UTF_8);
        
        byte flags = (byte) (packet[0] & 0x0F);
        int qos = (flags >> 1) & 0x03;
        
        int packetId = 0;
        if (qos > 0) {
            packetId = buffer.getShort() & 0xFFFF;
        }
        
        int payloadLength = remainingLength - topicLength - 2;
        if (qos > 0) {
            payloadLength -= 2;
        }
        
        byte[] payload = new byte[payloadLength];
        buffer.get(payload);
        
        return new MqttMessage(topic, payload, qos, packetId);
    }
    
    public void ack(int packetId) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.putShort((short) packetId);
            writePacket(PUBACK, buffer.array());
        } catch (IOException e) {
            throw new MessagingException("Failed to send PUBACK", e);
        }
    }
    
    private void startHeartbeat() {
        Thread heartbeatThread = Thread.ofVirtual().name("mqtt-heartbeat").start(() -> {
            while (connected && !closed) {
                try {
                    Thread.sleep(30000);
                    if (connected && !closed) {
                        writePacket(PINGREQ, new byte[0]);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });
        heartbeatThread.setDaemon(true);
    }
    
    private int nextPacketId() {
        return packetIdCounter.getAndIncrement();
    }
    
    private void writePacket(byte type, byte[] payload) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(5 + payload.length);
        buffer.put(type);
        buffer.put(encodeRemainingLength(payload.length));
        buffer.put(payload);
        
        outputStream.write(buffer.array(), 0, buffer.position());
        outputStream.flush();
    }
    
    private byte[] readPacket() throws IOException {
        int type = inputStream.read();
        if (type < 0) {
            return null;
        }
        
        int remainingLength = readRemainingLength(inputStream);
        
        byte[] payload = new byte[remainingLength];
        int totalRead = 0;
        while (totalRead < remainingLength) {
            int read = inputStream.read(payload, totalRead, remainingLength - totalRead);
            if (read < 0) break;
            totalRead += read;
        }
        
        byte[] packet = new byte[1 + remainingLength];
        packet[0] = (byte) type;
        System.arraycopy(payload, 0, packet, 1, remainingLength);
        
        return packet;
    }
    
    private int readRemainingLength(InputStream is) throws IOException {
        int multiplier = 1;
        int value = 0;
        byte digit;
        
        do {
            int b = is.read();
            if (b < 0) {
                throw new IOException("Unexpected end of stream");
            }
            digit = (byte) b;
            value += (digit & 0x7F) * multiplier;
            multiplier *= 128;
        } while ((digit & 0x80) != 0);
        
        return value;
    }
    
    private int readRemainingLength(ByteBuffer buffer) {
        int multiplier = 1;
        int value = 0;
        byte digit;
        
        do {
            digit = buffer.get();
            value += (digit & 0x7F) * multiplier;
            multiplier *= 128;
        } while ((digit & 0x80) != 0);
        
        return value;
    }
    
    private byte[] encodeRemainingLength(int length) {
        byte[] encoded = new byte[4];
        int index = 0;
        
        do {
            byte digit = (byte) (length % 128);
            length /= 128;
            if (length > 0) {
                digit |= 0x80;
            }
            encoded[index++] = digit;
        } while (length > 0);
        
        byte[] result = new byte[index];
        System.arraycopy(encoded, 0, result, 0, index);
        return result;
    }
    
    private void writeMqttString(ByteBuffer buffer, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buffer.putShort((short) bytes.length);
        buffer.put(bytes);
    }
    
    public boolean isConnected() {
        return connected && !closed;
    }
    
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        connected = false;
        
        try {
            if (outputStream != null) {
                writePacket(DISCONNECT, new byte[0]);
            }
        } catch (IOException e) {
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
    
    private void checkConnected() {
        if (!connected || closed) {
            throw new MessagingException("MQTT connection is not established");
        }
    }
    
    public MessagingConfig getConfig() {
        return config;
    }
    
    private static class PendingPacket {
        private final String topic;
        private final byte[] payload;
        
        PendingPacket(String topic, byte[] payload) {
            this.topic = topic;
            this.payload = payload;
        }
    }
    
    public static class MqttMessage {
        private final String topic;
        private final byte[] payload;
        private final int qos;
        private final int packetId;
        
        public MqttMessage(String topic, byte[] payload, int qos, int packetId) {
            this.topic = topic;
            this.payload = payload;
            this.qos = qos;
            this.packetId = packetId;
        }
        
        public String getTopic() {
            return topic;
        }
        
        public byte[] getPayload() {
            return payload;
        }
        
        public int getQos() {
            return qos;
        }
        
        public int getPacketId() {
            return packetId;
        }
    }
}
