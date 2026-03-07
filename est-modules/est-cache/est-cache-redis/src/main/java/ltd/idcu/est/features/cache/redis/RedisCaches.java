package ltd.idcu.est.features.cache.redis;

import ltd.idcu.est.features.cache.api.Cache;

public final class RedisCaches {
    
    private RedisCaches() {
    }
    
    public static Cache<String, String> newRedisCache() {
        return new RedisCache();
    }
    
    public static Cache<String, String> newRedisCache(String host, int port) {
        return new RedisCache(host, port);
    }
    
    public static Cache<String, String> newRedisCache(String host, int port, String password, int database) {
        return new RedisCache(host, port, password, database);
    }
    
    public static Cache<String, String> newRedisCache(RedisClient client) {
        return new RedisCache(client);
    }
    
    public static RedisCacheBuilder builder() {
        return new RedisCacheBuilder();
    }
    
    public static class RedisCacheBuilder {
        private String host = "localhost";
        private int port = 6379;
        private String password;
        private int database = 0;
        private String keyPrefix = "";
        
        public RedisCacheBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public RedisCacheBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public RedisCacheBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public RedisCacheBuilder database(int database) {
            this.database = database;
            return this;
        }
        
        public RedisCacheBuilder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }
        
        public Cache<String, String> build() {
            RedisClient client = new RedisClient(host, port, password, database);
            return new RedisCache(client, keyPrefix);
        }
    }
}
