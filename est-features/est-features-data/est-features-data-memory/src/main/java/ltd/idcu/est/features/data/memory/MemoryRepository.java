package ltd.idcu.est.features.data.memory;

import ltd.idcu.est.features.data.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MemoryRepository<T, ID> implements Repository<T, ID> {
    
    private final Map<ID, T> storage;
    private final IdGenerator<ID> idGenerator;
    private final EntityIdExtractor<T, ID> idExtractor;
    private final EntityIdSetter<T, ID> idSetter;
    private final AtomicLong idCounter;
    
    public MemoryRepository() {
        this(null, null, null);
    }
    
    public MemoryRepository(IdGenerator<ID> idGenerator, 
                           EntityIdExtractor<T, ID> idExtractor,
                           EntityIdSetter<T, ID> idSetter) {
        this.storage = new ConcurrentHashMap<>();
        this.idGenerator = idGenerator;
        this.idExtractor = idExtractor;
        this.idSetter = idSetter;
        this.idCounter = new AtomicLong(0);
    }
    
    @Override
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        
        ID id = extractId(entity);
        if (id == null && idGenerator != null) {
            id = idGenerator.generate();
            if (idSetter != null) {
                entity = idSetter.setId(entity, id);
            }
        }
        
        if (id == null) {
            throw new DataException("Entity ID cannot be null");
        }
        
        storage.put(id, entity);
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
        return Optional.ofNullable(storage.get(id));
    }
    
    @Override
    public boolean existsById(ID id) {
        return id != null && storage.containsKey(id);
    }
    
    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
    
    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        if (ids == null) {
            return Collections.emptyList();
        }
        
        List<T> result = new ArrayList<>();
        for (ID id : ids) {
            T entity = storage.get(id);
            if (entity != null) {
                result.add(entity);
            }
        }
        return result;
    }
    
    @Override
    public long count() {
        return storage.size();
    }
    
    @Override
    public void deleteById(ID id) {
        if (id != null) {
            storage.remove(id);
        }
    }
    
    @Override
    public void delete(T entity) {
        if (entity != null) {
            ID id = extractId(entity);
            if (id != null) {
                storage.remove(id);
            }
        }
    }
    
    @Override
    public void deleteAllById(Iterable<ID> ids) {
        if (ids != null) {
            for (ID id : ids) {
                storage.remove(id);
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
        storage.clear();
    }
    
    public List<T> findByPredicate(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    public Optional<T> findFirstByPredicate(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .findFirst();
    }
    
    public long countByPredicate(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .count();
    }
    
    public void deleteByPredicate(Predicate<T> predicate) {
        storage.entrySet().removeIf(entry -> predicate.test(entry.getValue()));
    }
    
    @SuppressWarnings("unchecked")
    private ID extractId(T entity) {
        if (idExtractor != null) {
            return idExtractor.extract(entity);
        }
        return null;
    }
    
    public interface IdGenerator<ID> {
        ID generate();
    }
    
    public interface EntityIdExtractor<T, ID> {
        ID extract(T entity);
    }
    
    public interface EntityIdSetter<T, ID> {
        T setId(T entity, ID id);
    }
    
    public static <T, ID> MemoryRepositoryBuilder<T, ID> builder() {
        return new MemoryRepositoryBuilder<>();
    }
    
    public static class MemoryRepositoryBuilder<T, ID> {
        private IdGenerator<ID> idGenerator;
        private EntityIdExtractor<T, ID> idExtractor;
        private EntityIdSetter<T, ID> idSetter;
        
        public MemoryRepositoryBuilder<T, ID> idGenerator(IdGenerator<ID> idGenerator) {
            this.idGenerator = idGenerator;
            return this;
        }
        
        public MemoryRepositoryBuilder<T, ID> idExtractor(EntityIdExtractor<T, ID> idExtractor) {
            this.idExtractor = idExtractor;
            return this;
        }
        
        public MemoryRepositoryBuilder<T, ID> idSetter(EntityIdSetter<T, ID> idSetter) {
            this.idSetter = idSetter;
            return this;
        }
        
        public MemoryRepository<T, ID> build() {
            return new MemoryRepository<>(idGenerator, idExtractor, idSetter);
        }
    }
}
