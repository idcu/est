package ltd.idcu.est.features.data.redis;

import ltd.idcu.est.features.data.api.*;
import ltd.idcu.est.features.data.jdbc.TestUser;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class RedisOrmTest {
    
    @Test
    public void testCreation() {
        RedisOrm orm = new RedisOrm(new MockRedisClient());
        Assertions.assertNotNull(orm);
    }
    
    @Test
    public void testCreationWithKeyPrefix() {
        RedisOrm orm = new RedisOrm(new MockRedisClient(), "test");
        Assertions.assertNotNull(orm);
    }
    
    @Test
    public void testGetTableName() {
        RedisOrm orm = new RedisOrm(new MockRedisClient());
        String keyPrefix = orm.getTableName(TestUser.class);
        Assertions.assertNotNull(keyPrefix);
    }
    
    @Test
    public void testGetIdFieldName() {
        RedisOrm orm = new RedisOrm(new MockRedisClient());
        String idFieldName = orm.getIdFieldName(TestUser.class);
        Assertions.assertNotNull(idFieldName);
    }
    
    private static class MockRedisClient implements RedisClient {
        private final Map<String, String> data = new HashMap<>();
        
        @Override
        public void set(String key, String value) {
            data.put(key, value);
        }
        
        @Override
        public String get(String key) {
            return data.get(key);
        }
        
        @Override
        public void del(String key) {
            data.remove(key);
        }
        
        @Override
        public boolean exists(String key) {
            return data.containsKey(key);
        }
        
        @Override
        public boolean expire(String key, long ttl, TimeUnit timeUnit) {
            return true;
        }
        
        @Override
        public long ttl(String key) {
            return -1;
        }
        
        @Override
        public long dbSize() {
            return data.size();
        }
    }
}