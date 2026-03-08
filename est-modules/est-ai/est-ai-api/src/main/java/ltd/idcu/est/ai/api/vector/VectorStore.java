package ltd.idcu.est.ai.api.vector;

import java.util.List;
import java.util.Map;

public interface VectorStore {
    
    String getName();
    
    void connect(Map<String, Object> config);
    
    void disconnect();
    
    boolean isConnected();
    
    void createCollection(String collectionName, int dimension);
    
    void createCollection(String collectionName, int dimension, Map<String, Object> options);
    
    void deleteCollection(String collectionName);
    
    boolean collectionExists(String collectionName);
    
    List<String> listCollections();
    
    void upsert(String collectionName, Vector vector);
    
    void upsert(String collectionName, List<Vector> vectors);
    
    Vector get(String collectionName, String id);
    
    List<Vector> get(String collectionName, List<String> ids);
    
    void delete(String collectionName, String id);
    
    void delete(String collectionName, List<String> ids);
    
    List<Vector> search(String collectionName, float[] queryVector, int topK);
    
    List<Vector> search(String collectionName, float[] queryVector, int topK, Map<String, Object> filter);
    
    List<Vector> search(String collectionName, Vector queryVector, int topK);
    
    List<Vector> search(String collectionName, Vector queryVector, int topK, Map<String, Object> filter);
    
    long count(String collectionName);
    
    void clear(String collectionName);
}
