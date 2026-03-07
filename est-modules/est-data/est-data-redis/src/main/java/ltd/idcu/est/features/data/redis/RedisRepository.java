package ltd.idcu.est.features.data.redis;

import ltd.idcu.est.features.data.api.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisRepository<T, ID> implements Repository<T, ID> {
    
    private final RedisClient client;
    private final String keyPrefix;
    private final EntitySerializer<T> serializer;
    private final EntityIdExtractor<T, ID> idExtractor;
    private final EntityIdSetter<T, ID> idSetter;
    
    public RedisRepository(RedisClient client, String keyPrefix,
                          EntitySerializer<T> serializer,
                          EntityIdExtractor<T, ID> idExtractor,
                          EntityIdSetter<T, ID> idSetter) {
        this.client = client;
        this.keyPrefix = keyPrefix != null ? keyPrefix : "";
        this.serializer = serializer;
        this.idExtractor = idExtractor;
        this.idSetter = idSetter;
    }
    
    private String buildKey(ID id) {
        return keyPrefix.isEmpty() ? String.valueOf(id) : keyPrefix + ":" + id;
    }
    
    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        ID id = idExtractor.extract(entity);
        if (id == null) {
            throw new DataException("Entity ID cannot be null");
        }
        
        String key = buildKey(id);
        String value = serializer.serialize(entity);
        client.set(key, value);
        return entity;
    }
    
    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        List<T> saved = new ArrayList<>();
        for (T entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }
    
    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            return Optional.empty();
        }
        
        String key = buildKey(id);
        String value = client.get(key);
        
        if (value == null) {
            return Optional.empty();
        }
        
        T entity = serializer.deserialize(value);
        return Optional.ofNullable(entity);
    }
    
    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }
        
        String key = buildKey(id);
        return client.exists(key);
    }
    
    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException("Use findAllById or scan for all entities");
    }
    
    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            findById(id).ifPresent(result::add);
        }
        return result;
    }
    
    @Override
    public long count() {
        throw new UnsupportedOperationException("Use dbSize for approximate count");
    }
    
    @Override
    public void deleteById(ID id) {
        if (id != null) {
            String key = buildKey(id);
            client.del(key);
        }
    }
    
    @Override
    public void delete(T entity) {
        if (entity != null) {
            ID id = idExtractor.extract(entity);
            if (id != null) {
                deleteById(id);
            }
        }
    }
    
    @Override
    public void deleteAllById(Iterable<ID> ids) {
        if (ids != null) {
            for (ID id : ids) {
                deleteById(id);
            }
        }
    }
    
    @Override
    public void deleteAll(Iterable<T> entities) {
        if (entities != null) {
            for (T entity : entities) {
                delete(entity);
            }
        }
    }
    
    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Use flushDb to clear all data");
    }
    
    public T saveWithTtl(T entity, long ttl, TimeUnit timeUnit) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        ID id = idExtractor.extract(entity);
        if (id == null) {
            throw new DataException("Entity ID cannot be null");
        }
        
        String key = buildKey(id);
        String value = serializer.serialize(entity);
        client.set(key, value, ttl, timeUnit);
        return entity;
    }
    
    public boolean setExpire(ID id, long ttl, TimeUnit timeUnit) {
        String key = buildKey(id);
        Long result = client.expire(key, timeUnit.toSeconds(ttl));
        return result != null && result == 1;
    }
    
    public long getTtl(ID id) {
        String key = buildKey(id);
        Long ttl = client.ttl(key);
        return ttl != null ? ttl : -2;
    }
    
    public boolean existsWithTtl(ID id) {
        long ttl = getTtl(id);
        return ttl > 0 || ttl == -1;
    }
    
    public interface EntitySerializer<T> {
        String serialize(T entity);
        T deserialize(String value);
    }
    
    public interface EntityIdExtractor<T, ID> {
        ID extract(T entity);
    }
    
    public interface EntityIdSetter<T, ID> {
        T setId(T entity, ID id);
    }
    
    public static <T, ID> RedisRepositoryBuilder<T, ID> builder() {
        return new RedisRepositoryBuilder<>();
    }
    
    public static class RedisRepositoryBuilder<T, ID> {
        private RedisClient client;
        private String keyPrefix;
        private EntitySerializer<T> serializer;
        private EntityIdExtractor<T, ID> idExtractor;
        private EntityIdSetter<T, ID> idSetter;
        
        public RedisRepositoryBuilder<T, ID> client(RedisClient client) {
            this.client = client;
            return this;
        }
        
        public RedisRepositoryBuilder<T, ID> keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }
        
        public RedisRepositoryBuilder<T, ID> serializer(EntitySerializer<T> serializer) {
            this.serializer = serializer;
            return this;
        }
        
        public RedisRepositoryBuilder<T, ID> idExtractor(EntityIdExtractor<T, ID> idExtractor) {
            this.idExtractor = idExtractor;
            return this;
        }
        
        public RedisRepositoryBuilder<T, ID> idSetter(EntityIdSetter<T, ID> idSetter) {
            this.idSetter = idSetter;
            return this;
        }
        
        public RedisRepository<T, ID> build() {
            if (client == null) {
                throw new IllegalArgumentException("RedisClient is required");
            }
            if (serializer == null) {
                throw new IllegalArgumentException("EntitySerializer is required");
            }
            if (idExtractor == null) {
                throw new IllegalArgumentException("EntityIdExtractor is required");
            }
            return new RedisRepository<>(client, keyPrefix, serializer, idExtractor, idSetter);
        }
    }
}
