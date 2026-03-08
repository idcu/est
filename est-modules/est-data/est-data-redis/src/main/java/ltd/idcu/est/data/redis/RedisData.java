package ltd.idcu.est.data.redis;

import ltd.idcu.est.data.api.*;

public final class RedisData {
    
    private RedisData() {
    }
    
    public static RedisClient newClient() {
        return new DefaultRedisClient();
    }
    
    public static RedisClient newClient(String host, int port) {
        return new DefaultRedisClient(host, port);
    }
    
    public static RedisClient newClient(String host, int port, String password, int database) {
        return new DefaultRedisClient(host, port, password, database);
    }
    
    public static RedisClient newClient(RedisConfig config) {
        return new DefaultRedisClient(config);
    }
    
    public static Orm newOrm(RedisClient client) {
        return new RedisOrm(client);
    }
    
    public static Orm newOrm(RedisClient client, String keyPrefix) {
        return new RedisOrm(client, keyPrefix);
    }
    
    public static <T, ID> RedisRepository.RedisRepositoryBuilder<T, ID> repositoryBuilder() {
        return RedisRepository.builder();
    }
    
    public static RedisConfig.RedisConfigBuilder configBuilder() {
        return new RedisConfig.RedisConfigBuilder();
    }
}
