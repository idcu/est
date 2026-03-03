package ltd.idcu.est.features.cache.redis;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RedisClient implements AutoCloseable {
    
    private final String host;
    private final int port;
    private final String password;
    private final int database;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean connected;
    
    public RedisClient() {
        this("localhost", 6379);
    }
    
    public RedisClient(String host, int port) {
        this(host, port, null, 0);
    }
    
    public RedisClient(String host, int port, String password, int database) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
        this.connected = false;
    }
    
    public void connect() throws IOException {
        if (connected) {
            return;
        }
        
        socket = new Socket(host, port);
        socket.setSoTimeout(5000);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        connected = true;
        
        if (password != null && !password.isEmpty()) {
            auth(password);
        }
        
        if (database > 0) {
            select(database);
        }
    }
    
    private void ensureConnected() throws IOException {
        if (!connected || socket == null || socket.isClosed()) {
            connect();
        }
    }
    
    public String set(String key, String value) throws IOException {
        ensureConnected();
        sendCommand("SET", key, value);
        return readSimpleString();
    }
    
    public String set(String key, String value, long ttlSeconds) throws IOException {
        ensureConnected();
        sendCommand("SET", key, value, "EX", String.valueOf(ttlSeconds));
        return readSimpleString();
    }
    
    public String get(String key) throws IOException {
        ensureConnected();
        sendCommand("GET", key);
        return readBulkString();
    }
    
    public Long del(String... keys) throws IOException {
        ensureConnected();
        sendCommand("DEL", keys);
        return readInteger();
    }
    
    public Boolean exists(String key) throws IOException {
        ensureConnected();
        sendCommand("EXISTS", key);
        return readInteger() == 1;
    }
    
    public Long expire(String key, long seconds) throws IOException {
        ensureConnected();
        sendCommand("EXPIRE", key, String.valueOf(seconds));
        return readInteger();
    }
    
    public Long ttl(String key) throws IOException {
        ensureConnected();
        sendCommand("TTL", key);
        return readInteger();
    }
    
    public Long incr(String key) throws IOException {
        ensureConnected();
        sendCommand("INCR", key);
        return readInteger();
    }
    
    public Long decr(String key) throws IOException {
        ensureConnected();
        sendCommand("DECR", key);
        return readInteger();
    }
    
    public Long incrBy(String key, long increment) throws IOException {
        ensureConnected();
        sendCommand("INCRBY", key, String.valueOf(increment));
        return readInteger();
    }
    
    public String setEx(String key, long seconds, String value) throws IOException {
        ensureConnected();
        sendCommand("SETEX", key, String.valueOf(seconds), value);
        return readSimpleString();
    }
    
    public String setNx(String key, String value) throws IOException {
        ensureConnected();
        sendCommand("SETNX", key, value);
        return readInteger() == 1 ? "OK" : null;
    }
    
    public Long append(String key, String value) throws IOException {
        ensureConnected();
        sendCommand("APPEND", key, value);
        return readInteger();
    }
    
    public String getRange(String key, long start, long end) throws IOException {
        ensureConnected();
        sendCommand("GETRANGE", key, String.valueOf(start), String.valueOf(end));
        return readBulkString();
    }
    
    public Long strlen(String key) throws IOException {
        ensureConnected();
        sendCommand("STRLEN", key);
        return readInteger();
    }
    
    public Long hSet(String key, String field, String value) throws IOException {
        ensureConnected();
        sendCommand("HSET", key, field, value);
        return readInteger();
    }
    
    public String hGet(String key, String field) throws IOException {
        ensureConnected();
        sendCommand("HGET", key, field);
        return readBulkString();
    }
    
    public Long hDel(String key, String... fields) throws IOException {
        ensureConnected();
        String[] args = new String[fields.length + 1];
        args[0] = key;
        System.arraycopy(fields, 0, args, 1, fields.length);
        sendCommand("HDEL", args);
        return readInteger();
    }
    
    public Boolean hExists(String key, String field) throws IOException {
        ensureConnected();
        sendCommand("HEXISTS", key, field);
        return readInteger() == 1;
    }
    
    public Long lPush(String key, String... values) throws IOException {
        ensureConnected();
        String[] args = new String[values.length + 1];
        args[0] = key;
        System.arraycopy(values, 0, args, 1, values.length);
        sendCommand("LPUSH", args);
        return readInteger();
    }
    
    public Long rPush(String key, String... values) throws IOException {
        ensureConnected();
        String[] args = new String[values.length + 1];
        args[0] = key;
        System.arraycopy(values, 0, args, 1, values.length);
        sendCommand("RPUSH", args);
        return readInteger();
    }
    
    public String lPop(String key) throws IOException {
        ensureConnected();
        sendCommand("LPOP", key);
        return readBulkString();
    }
    
    public String rPop(String key) throws IOException {
        ensureConnected();
        sendCommand("RPOP", key);
        return readBulkString();
    }
    
    public Long lLen(String key) throws IOException {
        ensureConnected();
        sendCommand("LLEN", key);
        return readInteger();
    }
    
    public String lIndex(String key, long index) throws IOException {
        ensureConnected();
        sendCommand("LINDEX", key, String.valueOf(index));
        return readBulkString();
    }
    
    public Long sAdd(String key, String... members) throws IOException {
        ensureConnected();
        String[] args = new String[members.length + 1];
        args[0] = key;
        System.arraycopy(members, 0, args, 1, members.length);
        sendCommand("SADD", args);
        return readInteger();
    }
    
    public Long sRem(String key, String... members) throws IOException {
        ensureConnected();
        String[] args = new String[members.length + 1];
        args[0] = key;
        System.arraycopy(members, 0, args, 1, members.length);
        sendCommand("SREM", args);
        return readInteger();
    }
    
    public Boolean sIsMember(String key, String member) throws IOException {
        ensureConnected();
        sendCommand("SISMEMBER", key, member);
        return readInteger() == 1;
    }
    
    public Long sCard(String key) throws IOException {
        ensureConnected();
        sendCommand("SCARD", key);
        return readInteger();
    }
    
    public String ping() throws IOException {
        ensureConnected();
        sendCommand("PING");
        return readSimpleString();
    }
    
    public String select(int database) throws IOException {
        ensureConnected();
        sendCommand("SELECT", String.valueOf(database));
        return readSimpleString();
    }
    
    private String auth(String password) throws IOException {
        sendCommand("AUTH", password);
        return readSimpleString();
    }
    
    public Long dbSize() throws IOException {
        ensureConnected();
        sendCommand("DBSIZE");
        return readInteger();
    }
    
    public String flushDb() throws IOException {
        ensureConnected();
        sendCommand("FLUSHDB");
        return readSimpleString();
    }
    
    public String flushAll() throws IOException {
        ensureConnected();
        sendCommand("FLUSHALL");
        return readSimpleString();
    }
    
    private void sendCommand(String command, String... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(args.length + 1).append("\r\n");
        sb.append("$").append(command.length()).append("\r\n");
        sb.append(command).append("\r\n");
        
        for (String arg : args) {
            if (arg == null) {
                arg = "";
            }
            sb.append("$").append(arg.getBytes(StandardCharsets.UTF_8).length).append("\r\n");
            sb.append(arg).append("\r\n");
        }
        
        writer.write(sb.toString());
        writer.flush();
    }
    
    private String readSimpleString() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Connection closed");
        }
        if (line.startsWith("+")) {
            return line.substring(1);
        }
        if (line.startsWith("-")) {
            throw new IOException("Redis error: " + line.substring(1));
        }
        throw new IOException("Unexpected response: " + line);
    }
    
    private String readBulkString() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Connection closed");
        }
        if (line.startsWith("$-1")) {
            return null;
        }
        if (line.startsWith("$")) {
            int length = Integer.parseInt(line.substring(1));
            if (length < 0) {
                return null;
            }
            char[] buffer = new char[length];
            int read = reader.read(buffer, 0, length);
            if (read != length) {
                throw new IOException("Incomplete read");
            }
            reader.readLine();
            return new String(buffer);
        }
        if (line.startsWith("-")) {
            throw new IOException("Redis error: " + line.substring(1));
        }
        throw new IOException("Unexpected response: " + line);
    }
    
    private long readInteger() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Connection closed");
        }
        if (line.startsWith(":")) {
            return Long.parseLong(line.substring(1));
        }
        if (line.startsWith("-")) {
            throw new IOException("Redis error: " + line.substring(1));
        }
        throw new IOException("Unexpected response: " + line);
    }
    
    private List<String> readArray() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Connection closed");
        }
        if (line.startsWith("*-1")) {
            return null;
        }
        if (line.startsWith("*")) {
            int count = Integer.parseInt(line.substring(1));
            List<String> result = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                result.add(readBulkString());
            }
            return result;
        }
        throw new IOException("Unexpected response: " + line);
    }
    
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed() && socket.isConnected();
    }
    
    @Override
    public void close() {
        connected = false;
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ignored) {
        }
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ignored) {
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
