package ltd.idcu.est.features.data.mongodb;

import ltd.idcu.est.features.data.api.*;
import ltd.idcu.est.features.data.jdbc.TestUser;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MongoOrmTest {
    
    @Test
    public void testCreation() {
        MongoOrm orm = new MongoOrm(new MockMongoClient());
        Assertions.assertNotNull(orm);
    }
    
    @Test
    public void testCreationWithDatabase() {
        MongoOrm orm = new MongoOrm(new MockMongoClient(), "testdb");
        Assertions.assertNotNull(orm);
    }
    
    @Test
    public void testGetTableName() {
        MongoOrm orm = new MongoOrm(new MockMongoClient());
        String collectionName = orm.getTableName(TestUser.class);
        Assertions.assertNotNull(collectionName);
    }
    
    @Test
    public void testGetIdFieldName() {
        MongoOrm orm = new MongoOrm(new MockMongoClient());
        String idFieldName = orm.getIdFieldName(TestUser.class);
        Assertions.assertNotNull(idFieldName);
    }
    
    private static class MockMongoClient implements MongoClient {
        private final Map<String, Map<String, String>> collections = new HashMap<>();
        
        @Override
        public void insert(String collection, String document) {
            collections.computeIfAbsent(collection, k -> new HashMap<>())
                .put(String.valueOf(System.currentTimeMillis()), document);
        }
        
        @Override
        public void update(String collection, String id, String document) {
            Map<String, String> col = collections.get(collection);
            if (col != null) {
                col.put(id, document);
            }
        }
        
        @Override
        public Optional<String> findById(String collection, String id) {
            Map<String, String> col = collections.get(collection);
            return col != null ? Optional.ofNullable(col.get(id)) : Optional.empty();
        }
        
        @Override
        public void delete(String collection, String id) {
            Map<String, String> col = collections.get(collection);
            if (col != null) {
                col.remove(id);
            }
        }
        
        @Override
        public boolean exists(String collection, String id) {
            Map<String, String> col = collections.get(collection);
            return col != null && col.containsKey(id);
        }
        
        @Override
        public long count(String collection) {
            Map<String, String> col = collections.get(collection);
            return col != null ? col.size() : 0;
        }
    }
}