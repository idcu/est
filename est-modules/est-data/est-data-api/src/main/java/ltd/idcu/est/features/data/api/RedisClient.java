package ltd.idcu.est.features.data.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisClient extends AutoCloseable {
    
    void connect();
    
    void disconnect();
    
    boolean isConnected();
    
    String ping();
    
    String set(String key, String value);
    
    String set(String key, String value, long ttl, TimeUnit timeUnit);
    
    String get(String key);
    
    Long del(String... keys);
    
    Boolean exists(String key);
    
    Long expire(String key, long seconds);
    
    Long ttl(String key);
    
    Long incr(String key);
    
    Long incrBy(String key, long increment);
    
    Long decr(String key);
    
    Long hSet(String key, String field, String value);
    
    String hGet(String key, String field);
    
    Map<String, String> hGetAll(String key);
    
    Long hDel(String key, String... fields);
    
    Boolean hExists(String key, String field);
    
    Long lPush(String key, String... values);
    
    Long rPush(String key, String... values);
    
    String lPop(String key);
    
    String rPop(String key);
    
    Long lLen(String key);
    
    List<String> lRange(String key, long start, long stop);
    
    Long sAdd(String key, String... members);
    
    Long sRem(String key, String... members);
    
    Boolean sIsMember(String key, String member);
    
    Long sCard(String key);
    
    Long dbSize();
    
    String flushDb();
    
    String flushAll();
    
    Boolean setNx(String key, String value);
    
    Boolean setNx(String key, String value, long ttl, TimeUnit timeUnit);
    
    String getSet(String key, String value);
    
    Long append(String key, String value);
    
    Long strlen(String key);
    
    String getRange(String key, long start, long end);
    
    Long setRange(String key, long offset, String value);
    
    @Override
    void close();
}
