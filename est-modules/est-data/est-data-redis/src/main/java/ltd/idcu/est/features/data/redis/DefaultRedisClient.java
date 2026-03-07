package ltd.idcu.est.features.data.redis;

import ltd.idcu.est.features.data.api.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DefaultRedisClient implements RedisClient {
    
    private final String host;
    private final int port;
    private final String password;
    private final int database;
    private final int connectionTimeout;
    private final int readTimeout;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean connected;
    
    public DefaultRedisClient() {
        this("localhost", 6379);
    }
    
    public DefaultRedisClient(String host, int port) {
        this(host, port, null, 0);
    }
    
    public DefaultRedisClient(String host, int port, String password, int database) {
        this(host, port, password, database, 5000, 5000);
    }
    
    public DefaultRedisClient(String host, int port, String password, int database, 
                              int connectionTimeout, int readTimeout) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;
        this.connected = false;
    }
    
    public DefaultRedisClient(RedisConfig config) {
        this(config.getHost(), config.getPort(), config.getPassword(), config.getDatabase(),
                config.getConnectionTimeout(), config.getReadTimeout());
    }
    
    @Override
    public void connect() {
        if (connected) {
            return;
        }
        
        try {
            socket = new Socket(host, port);
            socket.setSoTimeout(readTimeout);
            socket.setSoLinger(true, 0);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            connected = true;
            
            if (password != null && !password.isEmpty()) {
                auth(password);
            }
            
            if (database > 0) {
                select(database);
            }
        } catch (IOException e) {
            throw new DataException("Failed to connect to Redis: " + host + ":" + port, e);
        }
    }
    
    private void ensureConnected() {
        if (!connected || socket == null || socket.isClosed()) {
            connect();
        }
    }
    
    @Override
    public void disconnect() {
        close();
    }
    
    @Override
    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed() && socket.isConnected();
    }
    
    @Override
    public String ping() {
        ensureConnected();
        try {
            sendCommand("PING");
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to ping Redis", e);
        }
    }
    
    @Override
    public String set(String key, String value) {
        ensureConnected();
        try {
            sendCommand("SET", key, value);
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to set key: " + key, e);
        }
    }
    
    @Override
    public String set(String key, String value, long ttl, TimeUnit timeUnit) {
        ensureConnected();
        try {
            long ttlSeconds = timeUnit.toSeconds(ttl);
            sendCommand("SET", key, value, "EX", String.valueOf(ttlSeconds));
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to set key with TTL: " + key, e);
        }
    }
    
    @Override
    public String get(String key) {
        ensureConnected();
        try {
            sendCommand("GET", key);
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to get key: " + key, e);
        }
    }
    
    @Override
    public Long del(String... keys) {
        ensureConnected();
        try {
            sendCommand("DEL", keys);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to delete keys", e);
        }
    }
    
    @Override
    public Boolean exists(String key) {
        ensureConnected();
        try {
            sendCommand("EXISTS", key);
            return readInteger() == 1;
        } catch (IOException e) {
            throw new DataException("Failed to check existence for key: " + key, e);
        }
    }
    
    @Override
    public Long expire(String key, long seconds) {
        ensureConnected();
        try {
            sendCommand("EXPIRE", key, String.valueOf(seconds));
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to set expire for key: " + key, e);
        }
    }
    
    @Override
    public Long ttl(String key) {
        ensureConnected();
        try {
            sendCommand("TTL", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to get TTL for key: " + key, e);
        }
    }
    
    @Override
    public Long incr(String key) {
        ensureConnected();
        try {
            sendCommand("INCR", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to increment key: " + key, e);
        }
    }
    
    @Override
    public Long incrBy(String key, long increment) {
        ensureConnected();
        try {
            sendCommand("INCRBY", key, String.valueOf(increment));
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to increment key: " + key, e);
        }
    }
    
    @Override
    public Long decr(String key) {
        ensureConnected();
        try {
            sendCommand("DECR", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to decrement key: " + key, e);
        }
    }
    
    @Override
    public Long hSet(String key, String field, String value) {
        ensureConnected();
        try {
            sendCommand("HSET", key, field, value);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to hset key: " + key, e);
        }
    }
    
    @Override
    public String hGet(String key, String field) {
        ensureConnected();
        try {
            sendCommand("HGET", key, field);
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to hget key: " + key, e);
        }
    }
    
    @Override
    public Map<String, String> hGetAll(String key) {
        ensureConnected();
        try {
            sendCommand("HGETALL", key);
            return readMap();
        } catch (IOException e) {
            throw new DataException("Failed to hgetall key: " + key, e);
        }
    }
    
    @Override
    public Long hDel(String key, String... fields) {
        ensureConnected();
        try {
            String[] args = new String[fields.length + 1];
            args[0] = key;
            System.arraycopy(fields, 0, args, 1, fields.length);
            sendCommand("HDEL", args);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to hdel key: " + key, e);
        }
    }
    
    @Override
    public Boolean hExists(String key, String field) {
        ensureConnected();
        try {
            sendCommand("HEXISTS", key, field);
            return readInteger() == 1;
        } catch (IOException e) {
            throw new DataException("Failed to hexists key: " + key, e);
        }
    }
    
    @Override
    public Long lPush(String key, String... values) {
        ensureConnected();
        try {
            String[] args = new String[values.length + 1];
            args[0] = key;
            System.arraycopy(values, 0, args, 1, values.length);
            sendCommand("LPUSH", args);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to lpush key: " + key, e);
        }
    }
    
    @Override
    public Long rPush(String key, String... values) {
        ensureConnected();
        try {
            String[] args = new String[values.length + 1];
            args[0] = key;
            System.arraycopy(values, 0, args, 1, values.length);
            sendCommand("RPUSH", args);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to rpush key: " + key, e);
        }
    }
    
    @Override
    public String lPop(String key) {
        ensureConnected();
        try {
            sendCommand("LPOP", key);
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to lpop key: " + key, e);
        }
    }
    
    @Override
    public String rPop(String key) {
        ensureConnected();
        try {
            sendCommand("RPOP", key);
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to rpop key: " + key, e);
        }
    }
    
    @Override
    public Long lLen(String key) {
        ensureConnected();
        try {
            sendCommand("LLEN", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to llen key: " + key, e);
        }
    }
    
    @Override
    public List<String> lRange(String key, long start, long stop) {
        ensureConnected();
        try {
            sendCommand("LRANGE", key, String.valueOf(start), String.valueOf(stop));
            return readArray();
        } catch (IOException e) {
            throw new DataException("Failed to lrange key: " + key, e);
        }
    }
    
    @Override
    public Long sAdd(String key, String... members) {
        ensureConnected();
        try {
            String[] args = new String[members.length + 1];
            args[0] = key;
            System.arraycopy(members, 0, args, 1, members.length);
            sendCommand("SADD", args);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to sadd key: " + key, e);
        }
    }
    
    @Override
    public Long sRem(String key, String... members) {
        ensureConnected();
        try {
            String[] args = new String[members.length + 1];
            args[0] = key;
            System.arraycopy(members, 0, args, 1, members.length);
            sendCommand("SREM", args);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to srem key: " + key, e);
        }
    }
    
    @Override
    public Boolean sIsMember(String key, String member) {
        ensureConnected();
        try {
            sendCommand("SISMEMBER", key, member);
            return readInteger() == 1;
        } catch (IOException e) {
            throw new DataException("Failed to sismember key: " + key, e);
        }
    }
    
    @Override
    public Long sCard(String key) {
        ensureConnected();
        try {
            sendCommand("SCARD", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to scard key: " + key, e);
        }
    }
    
    @Override
    public Long dbSize() {
        ensureConnected();
        try {
            sendCommand("DBSIZE");
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to get db size", e);
        }
    }
    
    @Override
    public String flushDb() {
        ensureConnected();
        try {
            sendCommand("FLUSHDB");
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to flush db", e);
        }
    }
    
    @Override
    public String flushAll() {
        ensureConnected();
        try {
            sendCommand("FLUSHALL");
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to flush all", e);
        }
    }
    
    @Override
    public Boolean setNx(String key, String value) {
        ensureConnected();
        try {
            sendCommand("SETNX", key, value);
            return readInteger() == 1;
        } catch (IOException e) {
            throw new DataException("Failed to setnx key: " + key, e);
        }
    }
    
    @Override
    public Boolean setNx(String key, String value, long ttl, TimeUnit timeUnit) {
        ensureConnected();
        try {
            sendCommand("SET", key, value, "NX", "EX", String.valueOf(timeUnit.toSeconds(ttl)));
            String result = readSimpleString();
            return "OK".equals(result);
        } catch (IOException e) {
            throw new DataException("Failed to setnx key with TTL: " + key, e);
        }
    }
    
    @Override
    public String getSet(String key, String value) {
        ensureConnected();
        try {
            sendCommand("GETSET", key, value);
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to getset key: " + key, e);
        }
    }
    
    @Override
    public Long append(String key, String value) {
        ensureConnected();
        try {
            sendCommand("APPEND", key, value);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to append key: " + key, e);
        }
    }
    
    @Override
    public Long strlen(String key) {
        ensureConnected();
        try {
            sendCommand("STRLEN", key);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to strlen key: " + key, e);
        }
    }
    
    @Override
    public String getRange(String key, long start, long end) {
        ensureConnected();
        try {
            sendCommand("GETRANGE", key, String.valueOf(start), String.valueOf(end));
            return readBulkString();
        } catch (IOException e) {
            throw new DataException("Failed to getrange key: " + key, e);
        }
    }
    
    @Override
    public Long setRange(String key, long offset, String value) {
        ensureConnected();
        try {
            sendCommand("SETRANGE", key, String.valueOf(offset), value);
            return readInteger();
        } catch (IOException e) {
            throw new DataException("Failed to setrange key: " + key, e);
        }
    }
    
    private String select(int database) {
        try {
            sendCommand("SELECT", String.valueOf(database));
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to select database: " + database, e);
        }
    }
    
    private String auth(String password) {
        try {
            sendCommand("AUTH", password);
            return readSimpleString();
        } catch (IOException e) {
            throw new DataException("Failed to authenticate", e);
        }
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
    
    private Map<String, String> readMap() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("Connection closed");
        }
        if (line.startsWith("*-1")) {
            return null;
        }
        if (line.startsWith("*")) {
            int count = Integer.parseInt(line.substring(1));
            Map<String, String> result = new LinkedHashMap<>();
            for (int i = 0; i < count; i += 2) {
                String key = readBulkString();
                String value = readBulkString();
                if (key != null) {
                    result.put(key, value);
                }
            }
            return result;
        }
        throw new IOException("Unexpected response: " + line);
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
