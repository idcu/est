package ltd.idcu.est.messaging.amqp;

import ltd.idcu.est.messaging.api.MessagingConfig;
import ltd.idcu.est.messaging.api.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AmqpConnection {
    
    private static final String AMQP_HEADER = "AMQP";
    private static final int AMQP_VERSION_MAJOR = 0;
    private static final int AMQP_VERSION_MINOR = 9;
    private static final int AMQP_VERSION_REVISION = 1;
    private static final int FRAME_METHOD = 1;
    private static final int FRAME_HEADER = 2;
    private static final int FRAME_BODY = 3;
    private static final int FRAME_HEARTBEAT = 8;
    
    private static final int CONNECTION_START = 0x000A000A;
    private static final int CONNECTION_START_OK = 0x000A000B;
    private static final int CONNECTION_TUNE = 0x000A001E;
    private static final int CONNECTION_TUNE_OK = 0x000A001F;
    private static final int CONNECTION_OPEN = 0x000A0028;
    private static final int CONNECTION_OPEN_OK = 0x000A0029;
    private static final int CONNECTION_CLOSE = 0x000A002C;
    private static final int CONNECTION_CLOSE_OK = 0x000A002D;
    
    private static final int CHANNEL_OPEN = 0x0014000A;
    private static final int CHANNEL_OPEN_OK = 0x0014000B;
    private static final int CHANNEL_CLOSE = 0x00140014;
    private static final int CHANNEL_CLOSE_OK = 0x00140015;
    
    private static final int BASIC_PUBLISH = 0x003C0028;
    private static final int BASIC_CONSUME = 0x003C0014;
    private static final int BASIC_CONSUME_OK = 0x003C0015;
    private static final int BASIC_DELIVER = 0x003C003C;
    private static final int BASIC_ACK = 0x003C0030;
    private static final int BASIC_QOS = 0x003C000A;
    private static final int BASIC_QOS_OK = 0x003C000B;
    
    private static final int QUEUE_DECLARE = 0x0032000A;
    private static final int QUEUE_DECLARE_OK = 0x0032000B;
    private static final int QUEUE_BIND = 0x00320014;
    private static final int QUEUE_BIND_OK = 0x00320015;
    
    private static final int EXCHANGE_DECLARE = 0x0028000A;
    private static final int EXCHANGE_DECLARE_OK = 0x0028000B;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final AtomicInteger channelCounter;
    private final AtomicLong deliveryTagCounter;
    private volatile boolean connected;
    private volatile boolean closed;
    
    public AmqpConnection(MessagingConfig config) {
        this.config = config;
        this.channelCounter = new AtomicInteger(0);
        this.deliveryTagCounter = new AtomicLong(1);
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort());
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            sendProtocolHeader();
            negotiateConnection();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to AMQP server: " + config.getHost() + ":" + config.getPort(), e);
        }
    }
    
    private void sendProtocolHeader() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(AMQP_HEADER.getBytes(StandardCharsets.US_ASCII));
        buffer.put((byte) 0);
        buffer.put((byte) AMQP_VERSION_MAJOR);
        buffer.put((byte) AMQP_VERSION_MINOR);
        buffer.put((byte) AMQP_VERSION_REVISION);
        outputStream.write(buffer.array());
        outputStream.flush();
    }
    
    private void negotiateConnection() throws IOException {
        Frame frame = readFrame();
        if (frame == null || frame.getType() != FRAME_METHOD) {
            throw new MessagingException("Invalid AMQP response from server");
        }
        
        Map<String, Object> serverProperties = new HashMap<>();
        int channelMax = 0;
        int frameMax = 131072;
        int heartbeat = 0;
        
        writeMethodFrame(0, CONNECTION_START_OK, buildStartOkArgs());
        
        frame = readFrame();
        if (frame != null && frame.getType() == FRAME_METHOD) {
            channelMax = 65535;
            frameMax = 131072;
            heartbeat = 60;
        }
        
        writeMethodFrame(0, CONNECTION_TUNE_OK, buildTuneOkArgs(channelMax, frameMax, heartbeat));
        
        writeMethodFrame(0, CONNECTION_OPEN, buildOpenArgs());
        
        frame = readFrame();
        if (frame == null || frame.getType() != FRAME_METHOD) {
            throw new MessagingException("Failed to open connection");
        }
    }
    
    private byte[] buildStartOkArgs() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        writeShortString(buffer, "PLAIN");
        
        String credentials = "\0" + config.getUsername() + "\0" + config.getPassword();
        byte[] credBytes = credentials.getBytes(StandardCharsets.UTF_8);
        writeLongString(buffer, credBytes);
        
        writeShortString(buffer, "en_US");
        
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }
    
    private byte[] buildTuneOkArgs(int channelMax, int frameMax, int heartbeat) {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putShort((short) channelMax);
        buffer.putInt(frameMax);
        buffer.putShort((short) heartbeat);
        return buffer.array();
    }
    
    private byte[] buildOpenArgs() {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        writeShortString(buffer, config.getVirtualHost());
        buffer.putShort((short) 0);
        buffer.put((byte) 1);
        
        byte[] result = new byte[buffer.position()];
        buffer.flip();
        buffer.get(result);
        return result;
    }
    
    public int createChannel() throws MessagingException {
        int channelId = channelCounter.incrementAndGet();
        
        try {
            writeMethodFrame(channelId, CHANNEL_OPEN, new byte[]{0});
            Frame frame = readFrame();
            if (frame == null) {
                throw new MessagingException("Failed to open channel");
            }
        } catch (IOException e) {
            throw new MessagingException("Failed to create channel", e);
        }
        
        return channelId;
    }
    
    public void closeChannel(int channelId) throws MessagingException {
        try {
            writeMethodFrame(channelId, CHANNEL_CLOSE, new byte[4]);
            readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to close channel", e);
        }
    }
    
    public void declareQueue(int channelId, String queueName, boolean durable, boolean exclusive, boolean autoDelete) 
            throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.putShort((short) 0);
            writeShortString(buffer, queueName);
            
            byte flags = 0;
            if (durable) flags |= 0x01;
            if (exclusive) flags |= 0x02;
            if (autoDelete) flags |= 0x04;
            buffer.put(flags);
            
            byte[] args = new byte[buffer.position()];
            buffer.flip();
            buffer.get(args);
            
            writeMethodFrame(channelId, QUEUE_DECLARE, args);
            readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to declare queue: " + queueName, e);
        }
    }
    
    public void declareExchange(int channelId, String exchangeName, String type, boolean durable) 
            throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.putShort((short) 0);
            writeShortString(buffer, exchangeName);
            writeShortString(buffer, type);
            
            byte flags = 0;
            if (durable) flags |= 0x01;
            buffer.put(flags);
            
            byte[] args = new byte[buffer.position()];
            buffer.flip();
            buffer.get(args);
            
            writeMethodFrame(channelId, EXCHANGE_DECLARE, args);
            readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to declare exchange: " + exchangeName, e);
        }
    }
    
    public void bindQueue(int channelId, String queueName, String exchangeName, String routingKey) 
            throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.putShort((short) 0);
            writeShortString(buffer, queueName);
            writeShortString(buffer, exchangeName);
            writeShortString(buffer, routingKey != null ? routingKey : "");
            buffer.put((byte) 0);
            
            byte[] args = new byte[buffer.position()];
            buffer.flip();
            buffer.get(args);
            
            writeMethodFrame(channelId, QUEUE_BIND, args);
            readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to bind queue: " + queueName, e);
        }
    }
    
    public void basicPublish(int channelId, String exchange, String routingKey, byte[] body) 
            throws MessagingException {
        try {
            ByteBuffer methodBuffer = ByteBuffer.allocate(512);
            methodBuffer.putShort((short) 0);
            writeShortString(methodBuffer, exchange != null ? exchange : "");
            writeShortString(methodBuffer, routingKey != null ? routingKey : "");
            methodBuffer.put((byte) 0);
            
            byte[] methodArgs = new byte[methodBuffer.position()];
            methodBuffer.flip();
            methodBuffer.get(methodArgs);
            
            writeMethodFrame(channelId, BASIC_PUBLISH, methodArgs);
            
            ByteBuffer headerBuffer = ByteBuffer.allocate(14);
            headerBuffer.putShort((short) 0x003C);
            headerBuffer.putShort((short) 0);
            headerBuffer.putLong(body.length);
            headerBuffer.putShort((short) 0);
            
            writeFrame(FRAME_HEADER, channelId, headerBuffer.array());
            
            int offset = 0;
            while (offset < body.length) {
                int chunkSize = Math.min(body.length - offset, 131072 - 8);
                byte[] chunk = new byte[chunkSize];
                System.arraycopy(body, offset, chunk, 0, chunkSize);
                writeFrame(FRAME_BODY, channelId, chunk);
                offset += chunkSize;
            }
        } catch (IOException e) {
            throw new MessagingException("Failed to publish message", e);
        }
    }
    
    public String basicConsume(int channelId, String queueName) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            buffer.putShort((short) 0);
            writeShortString(buffer, queueName);
            writeShortString(buffer, "");
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            
            byte[] args = new byte[buffer.position()];
            buffer.flip();
            buffer.get(args);
            
            writeMethodFrame(channelId, BASIC_CONSUME, args);
            Frame frame = readFrame();
            
            return "consumer-" + channelId;
        } catch (IOException e) {
            throw new MessagingException("Failed to consume from queue: " + queueName, e);
        }
    }
    
    public void basicAck(int channelId, long deliveryTag) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(13);
            buffer.putLong(deliveryTag);
            buffer.put((byte) 0);
            
            writeMethodFrame(channelId, BASIC_ACK, buffer.array());
        } catch (IOException e) {
            throw new MessagingException("Failed to ack message", e);
        }
    }
    
    public void basicQos(int channelId, int prefetchCount) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(11);
            buffer.putInt(0);
            buffer.putShort((short) prefetchCount);
            buffer.put((byte) 0);
            
            writeMethodFrame(channelId, BASIC_QOS, buffer.array());
            readFrame();
        } catch (IOException e) {
            throw new MessagingException("Failed to set QoS", e);
        }
    }
    
    public Frame readFrame() throws IOException {
        byte[] header = new byte[7];
        int read = inputStream.read(header);
        if (read < 7) {
            return null;
        }
        
        ByteBuffer headerBuffer = ByteBuffer.wrap(header);
        int type = headerBuffer.get() & 0xFF;
        int channelId = headerBuffer.getShort() & 0xFFFF;
        int payloadSize = headerBuffer.getInt();
        
        byte[] payload = new byte[payloadSize];
        int totalRead = 0;
        while (totalRead < payloadSize) {
            int r = inputStream.read(payload, totalRead, payloadSize - totalRead);
            if (r < 0) break;
            totalRead += r;
        }
        
        int frameEnd = inputStream.read();
        if (frameEnd != 0xCE) {
            throw new IOException("Invalid frame end marker");
        }
        
        return new Frame(type, channelId, payload);
    }
    
    public void writeMethodFrame(int channelId, int methodId, byte[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4 + args.length);
        buffer.putInt(methodId);
        buffer.put(args);
        writeFrame(FRAME_METHOD, channelId, buffer.array());
    }
    
    public void writeFrame(int type, int channelId, byte[] payload) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(7 + payload.length + 1);
        buffer.put((byte) type);
        buffer.putShort((short) channelId);
        buffer.putInt(payload.length);
        buffer.put(payload);
        buffer.put((byte) 0xCE);
        
        outputStream.write(buffer.array());
        outputStream.flush();
    }
    
    private void writeShortString(ByteBuffer buffer, String value) {
        if (value == null) {
            buffer.put((byte) 0);
        } else {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            buffer.put((byte) bytes.length);
            buffer.put(bytes);
        }
    }
    
    private void writeLongString(ByteBuffer buffer, byte[] value) {
        buffer.putInt(value.length);
        buffer.put(value);
    }
    
    public MessagingConfig getConfig() {
        return config;
    }
    
    public boolean isConnected() {
        return connected && !closed;
    }
    
    public long nextDeliveryTag() {
        return deliveryTagCounter.getAndIncrement();
    }
    
    public void close() {
        if (closed) {
            return;
        }
        
        closed = true;
        connected = false;
        
        try {
            if (outputStream != null) {
                writeMethodFrame(0, CONNECTION_CLOSE, new byte[8]);
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
    
    public static class Frame {
        private final int type;
        private final int channelId;
        private final byte[] payload;
        
        public Frame(int type, int channelId, byte[] payload) {
            this.type = type;
            this.channelId = channelId;
            this.payload = payload;
        }
        
        public int getType() {
            return type;
        }
        
        public int getChannelId() {
            return channelId;
        }
        
        public byte[] getPayload() {
            return payload;
        }
    }
}
