package ltd.idcu.est.features.messaging.kafka;

import ltd.idcu.est.features.messaging.api.MessagingConfig;
import ltd.idcu.est.features.messaging.api.MessagingException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaConnection {
    
    private static final int API_KEY_PRODUCE = 0;
    private static final int API_KEY_FETCH = 1;
    private static final int API_KEY_METADATA = 3;
    private static final int API_VERSION = 1;
    
    private final MessagingConfig config;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final AtomicInteger correlationIdCounter;
    private volatile boolean connected;
    private volatile boolean closed;
    private final Map<String, TopicMetadata> topicMetadata;
    
    public KafkaConnection(MessagingConfig config) {
        this.config = config;
        this.correlationIdCounter = new AtomicInteger(1);
        this.topicMetadata = new ConcurrentHashMap<>();
        this.connected = false;
        this.closed = false;
    }
    
    public void connect() throws MessagingException {
        try {
            socket = new Socket(config.getHost(), config.getPort() != 0 ? config.getPort() : 9092);
            socket.setSoTimeout(config.getConnectionTimeout());
            socket.setTcpNoDelay(true);
            
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            
            connected = true;
        } catch (IOException e) {
            throw new MessagingException("Failed to connect to Kafka server: " + config.getHost() + ":" + (config.getPort() != 0 ? config.getPort() : 9092), e);
        }
    }
    
    public void fetchMetadata(String topic) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            int correlationId = nextCorrelationId();
            
            buffer.putInt(0);
            buffer.putShort((short) API_KEY_METADATA);
            buffer.putShort((short) API_VERSION);
            buffer.putInt(correlationId);
            
            writeShortString(buffer, "est-client");
            
            buffer.putInt(1);
            writeShortString(buffer, topic);
            
            int length = buffer.position() - 4;
            buffer.putInt(0, length);
            
            writeRequest(buffer.array());
            
            ByteBuffer response = readResponse(correlationId);
            parseMetadataResponse(response, topic);
            
        } catch (IOException e) {
            throw new MessagingException("Failed to fetch metadata for topic: " + topic, e);
        }
    }
    
    private void parseMetadataResponse(ByteBuffer buffer, String topic) {
        buffer.getInt();
        
        int brokerCount = buffer.getInt();
        for (int i = 0; i < brokerCount; i++) {
            buffer.getInt();
            buffer.getInt();
            readShortString(buffer);
        }
        
        int topicCount = buffer.getInt();
        for (int i = 0; i < topicCount; i++) {
            buffer.getShort();
            String topicName = readShortString(buffer);
            boolean isInternal = buffer.get() == 1;
            
            int partitionCount = buffer.getInt();
            List<Integer> partitions = new ArrayList<>();
            for (int j = 0; j < partitionCount; j++) {
                buffer.getShort();
                int partitionId = buffer.getInt();
                partitions.add(partitionId);
                buffer.getInt();
                
                int replicaCount = buffer.getInt();
                for (int k = 0; k < replicaCount; k++) {
                    buffer.getInt();
                }
                
                int isrCount = buffer.getInt();
                for (int k = 0; k < isrCount; k++) {
                    buffer.getInt();
                }
            }
            
            topicMetadata.put(topicName, new TopicMetadata(topicName, partitions));
        }
    }
    
    public void produce(String topic, int partition, byte[] key, byte[] value) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024 + value.length);
            
            int correlationId = nextCorrelationId();
            
            buffer.putInt(0);
            buffer.putShort((short) API_KEY_PRODUCE);
            buffer.putShort((short) API_VERSION);
            buffer.putInt(correlationId);
            
            writeShortString(buffer, "est-client");
            
            buffer.putShort((short) 1);
            buffer.putInt(100);
            
            buffer.putInt(1);
            writeShortString(buffer, topic);
            
            buffer.putInt(1);
            buffer.putInt(partition);
            
            int messageSetSizePos = buffer.position();
            buffer.putInt(0);
            
            long offset = 0;
            buffer.putLong(offset);
            
            int messageSizePos = buffer.position();
            buffer.putInt(0);
            
            int crcPos = buffer.position();
            buffer.putInt(0);
            
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            
            if (key == null) {
                buffer.putInt(-1);
            } else {
                buffer.putInt(key.length);
                buffer.put(key);
            }
            
            if (value == null) {
                buffer.putInt(-1);
            } else {
                buffer.putInt(value.length);
                buffer.put(value);
            }
            
            int messageSize = buffer.position() - messageSizePos - 4;
            buffer.putInt(messageSizePos, messageSize);
            
            int messageSetSize = buffer.position() - messageSetSizePos - 4;
            buffer.putInt(messageSetSizePos, messageSetSize);
            
            int totalSize = buffer.position() - 4;
            buffer.putInt(0, totalSize);
            
            writeRequest(buffer.array());
            
            readResponse(correlationId);
            
        } catch (IOException e) {
            throw new MessagingException("Failed to produce message to topic: " + topic, e);
        }
    }
    
    public List<KafkaRecord> fetch(String topic, int partition, long offset, int maxBytes) throws MessagingException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            
            int correlationId = nextCorrelationId();
            
            buffer.putInt(0);
            buffer.putShort((short) API_KEY_FETCH);
            buffer.putShort((short) API_VERSION);
            buffer.putInt(correlationId);
            
            writeShortString(buffer, "est-client");
            
            buffer.putInt(-1);
            buffer.putInt(500);
            buffer.putInt(maxBytes);
            
            buffer.putInt(1);
            writeShortString(buffer, topic);
            
            buffer.putInt(1);
            buffer.putInt(partition);
            buffer.putLong(offset);
            buffer.putInt(maxBytes);
            
            int totalSize = buffer.position() - 4;
            buffer.putInt(0, totalSize);
            
            writeRequest(buffer.array());
            
            ByteBuffer response = readResponse(correlationId);
            return parseFetchResponse(response);
            
        } catch (IOException e) {
            throw new MessagingException("Failed to fetch messages from topic: " + topic, e);
        }
    }
    
    private List<KafkaRecord> parseFetchResponse(ByteBuffer buffer) {
        List<KafkaRecord> records = new ArrayList<>();
        
        buffer.getInt();
        
        int topicCount = buffer.getInt();
        for (int i = 0; i < topicCount; i++) {
            String topic = readShortString(buffer);
            
            int partitionCount = buffer.getInt();
            for (int j = 0; j < partitionCount; j++) {
                int partition = buffer.getInt();
                short errorCode = buffer.getShort();
                long highWatermark = buffer.getLong();
                
                int messageSetSize = buffer.getInt();
                if (messageSetSize > 0) {
                    int endPosition = buffer.position() + messageSetSize;
                    
                    while (buffer.position() < endPosition) {
                        long offset = buffer.getLong();
                        int messageSize = buffer.getInt();
                        
                        if (messageSize > 0) {
                            int crc = buffer.getInt();
                            byte magicByte = buffer.get();
                            byte attributes = buffer.get();
                            
                            byte[] key = null;
                            int keyLength = buffer.getInt();
                            if (keyLength >= 0) {
                                key = new byte[keyLength];
                                buffer.get(key);
                            }
                            
                            byte[] value = null;
                            int valueLength = buffer.getInt();
                            if (valueLength >= 0) {
                                value = new byte[valueLength];
                                buffer.get(value);
                            }
                            
                            records.add(new KafkaRecord(topic, partition, offset, key, value));
                        }
                    }
                }
            }
        }
        
        return records;
    }
    
    private int nextCorrelationId() {
        return correlationIdCounter.getAndIncrement();
    }
    
    private void writeRequest(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }
    
    private ByteBuffer readResponse(int expectedCorrelationId) throws IOException {
        byte[] sizeBytes = new byte[4];
        inputStream.read(sizeBytes);
        int size = ByteBuffer.wrap(sizeBytes).getInt();
        
        byte[] response = new byte[size];
        int read = 0;
        while (read < size) {
            int r = inputStream.read(response, read, size - read);
            if (r < 0) break;
            read += r;
        }
        
        ByteBuffer buffer = ByteBuffer.wrap(response);
        int correlationId = buffer.getInt();
        
        if (correlationId != expectedCorrelationId) {
            throw new IOException("Correlation ID mismatch");
        }
        
        return buffer;
    }
    
    private void writeShortString(ByteBuffer buffer, String value) {
        if (value == null) {
            buffer.putShort((short) -1);
        } else {
            byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
            buffer.putShort((short) bytes.length);
            buffer.put(bytes);
        }
    }
    
    private String readShortString(ByteBuffer buffer) {
        short length = buffer.getShort();
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
        
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // Ignore
        }
    }
    
    private static class TopicMetadata {
        final String name;
        final List<Integer> partitions;
        
        TopicMetadata(String name, List<Integer> partitions) {
            this.name = name;
            this.partitions = partitions;
        }
    }
    
    public static class KafkaRecord {
        private final String topic;
        private final int partition;
        private final long offset;
        private final byte[] key;
        private final byte[] value;
        
        public KafkaRecord(String topic, int partition, long offset, byte[] key, byte[] value) {
            this.topic = topic;
            this.partition = partition;
            this.offset = offset;
            this.key = key;
            this.value = value;
        }
        
        public String getTopic() {
            return topic;
        }
        
        public int getPartition() {
            return partition;
        }
        
        public long getOffset() {
            return offset;
        }
        
        public byte[] getKey() {
            return key;
        }
        
        public byte[] getValue() {
            return value;
        }
    }
}
