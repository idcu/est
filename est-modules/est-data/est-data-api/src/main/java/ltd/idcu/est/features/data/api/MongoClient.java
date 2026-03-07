package ltd.idcu.est.features.data.api;

import java.util.List;
import java.util.Map;

public interface MongoClient extends AutoCloseable {
    
    void connect();
    
    void disconnect();
    
    boolean isConnected();
    
    void createCollection(String collectionName);
    
    void dropCollection(String collectionName);
    
    boolean collectionExists(String collectionName);
    
    <T> String insertOne(String collectionName, T document);
    
    <T> List<String> insertMany(String collectionName, List<T> documents);
    
    <T> T findOne(String collectionName, String id, Class<T> clazz);
    
    <T> List<T> findAll(String collectionName, Class<T> clazz);
    
    <T> List<T> findByField(String collectionName, String fieldName, Object value, Class<T> clazz);
    
    <T> T updateOne(String collectionName, String id, T document);
    
    boolean deleteOne(String collectionName, String id);
    
    long deleteMany(String collectionName, Map<String, Object> filter);
    
    long count(String collectionName);
    
    long count(String collectionName, Map<String, Object> filter);
    
    <T> List<T> findWithLimit(String collectionName, int limit, Class<T> clazz);
    
    <T> List<T> findWithSkipAndLimit(String collectionName, int skip, int limit, Class<T> clazz);
    
    @Override
    void close();
}
