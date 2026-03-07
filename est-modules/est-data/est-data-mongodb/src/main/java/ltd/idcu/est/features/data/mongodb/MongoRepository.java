package ltd.idcu.est.features.data.mongodb;

import ltd.idcu.est.features.data.api.DataException;
import ltd.idcu.est.features.data.api.MongoClient;
import ltd.idcu.est.features.data.api.Repository;

import java.util.*;

public class MongoRepository<T, ID> implements Repository<T, ID> {
    
    private final MongoClient client;
    private final String collectionName;
    private final EntitySerializer<T> serializer;
    private final EntityIdExtractor<T, ID> idExtractor;
    private final EntityIdSetter<T, ID> idSetter;
    private final Class<T> entityClass;
    
    public MongoRepository(MongoClient client, String collectionName,
                          EntitySerializer<T> serializer,
                          EntityIdExtractor<T, ID> idExtractor,
                          EntityIdSetter<T, ID> idSetter,
                          Class<T> entityClass) {
        this.client = client;
        this.collectionName = collectionName;
        this.serializer = serializer;
        this.idExtractor = idExtractor;
        this.idSetter = idSetter;
        this.entityClass = entityClass;
    }
    
    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        ID id = idExtractor.extract(entity);
        if (id == null) {
            String generatedId = client.insertOne(collectionName, entity);
            if (idSetter != null) {
                entity = idSetter.setId(entity, (ID) generatedId);
            }
            return entity;
        } else {
            return client.updateOne(collectionName, String.valueOf(id), entity);
        }
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
        
        T entity = client.findOne(collectionName, String.valueOf(id), entityClass);
        return Optional.ofNullable(entity);
    }
    
    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            return false;
        }
        return client.findOne(collectionName, String.valueOf(id), entityClass) != null;
    }
    
    @Override
    public List<T> findAll() {
        return client.findAll(collectionName, entityClass);
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
        return client.count(collectionName);
    }
    
    @Override
    public void deleteById(ID id) {
        if (id != null) {
            client.deleteOne(collectionName, String.valueOf(id));
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
        Map<String, Object> emptyFilter = new HashMap<>();
        client.deleteMany(collectionName, emptyFilter);
    }
    
    public List<T> findByField(String fieldName, Object value) {
        return client.findByField(collectionName, fieldName, value, entityClass);
    }
    
    public List<T> findWithLimit(int limit) {
        return client.findWithLimit(collectionName, limit, entityClass);
    }
    
    public List<T> findWithSkipAndLimit(int skip, int limit) {
        return client.findWithSkipAndLimit(collectionName, skip, limit, entityClass);
    }
    
    public long count(Map<String, Object> filter) {
        return client.count(collectionName, filter);
    }
    
    public long deleteMany(Map<String, Object> filter) {
        return client.deleteMany(collectionName, filter);
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
    
    public static <T, ID> MongoRepositoryBuilder<T, ID> builder() {
        return new MongoRepositoryBuilder<>();
    }
    
    public static class MongoRepositoryBuilder<T, ID> {
        private MongoClient client;
        private String collectionName;
        private EntitySerializer<T> serializer;
        private EntityIdExtractor<T, ID> idExtractor;
        private EntityIdSetter<T, ID> idSetter;
        private Class<T> entityClass;
        
        public MongoRepositoryBuilder<T, ID> client(MongoClient client) {
            this.client = client;
            return this;
        }
        
        public MongoRepositoryBuilder<T, ID> collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }
        
        public MongoRepositoryBuilder<T, ID> serializer(EntitySerializer<T> serializer) {
            this.serializer = serializer;
            return this;
        }
        
        public MongoRepositoryBuilder<T, ID> idExtractor(EntityIdExtractor<T, ID> idExtractor) {
            this.idExtractor = idExtractor;
            return this;
        }
        
        public MongoRepositoryBuilder<T, ID> idSetter(EntityIdSetter<T, ID> idSetter) {
            this.idSetter = idSetter;
            return this;
        }
        
        public MongoRepositoryBuilder<T, ID> entityClass(Class<T> entityClass) {
            this.entityClass = entityClass;
            return this;
        }
        
        public MongoRepository<T, ID> build() {
            if (client == null) {
                throw new IllegalArgumentException("MongoClient is required");
            }
            if (collectionName == null || collectionName.isEmpty()) {
                throw new IllegalArgumentException("Collection name is required");
            }
            if (idExtractor == null) {
                throw new IllegalArgumentException("EntityIdExtractor is required");
            }
            if (entityClass == null) {
                throw new IllegalArgumentException("Entity class is required");
            }
            return new MongoRepository<>(client, collectionName, serializer, idExtractor, idSetter, entityClass);
        }
    }
}
