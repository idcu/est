package ltd.idcu.est.ai.impl.vector;

import ltd.idcu.est.ai.api.vector.Vector;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.api.vector.VectorStoreException;
import ltd.idcu.est.ai.api.vector.VectorStoreRuntimeException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryVectorStore implements VectorStore {
    
    private final Map<String, Map<String, Vector>> collections;
    private boolean connected;
    private final String name;
    
    public InMemoryVectorStore() {
        this("InMemory");
    }
    
    public InMemoryVectorStore(String name) {
        this.name = name;
        this.collections = new ConcurrentHashMap<>();
        this.connected = false;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void connect(Map<String, Object> config) {
        this.connected = true;
    }
    
    @Override
    public void disconnect() {
        this.connected = false;
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void createCollection(String collectionName, int dimension) {
        createCollection(collectionName, dimension, new HashMap<>());
    }
    
    @Override
    public void createCollection(String collectionName, int dimension, Map<String, Object> options) {
        if (!collections.containsKey(collectionName)) {
            collections.put(collectionName, new ConcurrentHashMap<>());
        }
    }
    
    @Override
    public void deleteCollection(String collectionName) {
        collections.remove(collectionName);
    }
    
    @Override
    public boolean collectionExists(String collectionName) {
        return collections.containsKey(collectionName);
    }
    
    @Override
    public List<String> listCollections() {
        return new ArrayList<>(collections.keySet());
    }
    
    @Override
    public void upsert(String collectionName, Vector vector) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        collection.put(vector.getId(), vector);
    }
    
    @Override
    public void upsert(String collectionName, List<Vector> vectors) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        for (Vector vector : vectors) {
            collection.put(vector.getId(), vector);
        }
    }
    
    @Override
    public Vector get(String collectionName, String id) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        return collection.get(id);
    }
    
    @Override
    public List<Vector> get(String collectionName, List<String> ids) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        return ids.stream()
                .map(collection::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(String collectionName, String id) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        collection.remove(id);
    }
    
    @Override
    public void delete(String collectionName, List<String> ids) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        for (String id : ids) {
            collection.remove(id);
        }
    }
    
    @Override
    public List<Vector> search(String collectionName, float[] queryVector, int topK) {
        return search(collectionName, queryVector, topK, new HashMap<>());
    }
    
    @Override
    public List<Vector> search(String collectionName, float[] queryVector, int topK, Map<String, Object> filter) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        
        List<Vector> results = new ArrayList<>(collection.values());
        
        for (Vector vector : results) {
            double similarity = cosineSimilarity(queryVector, vector.getValues());
            vector.setScore(similarity);
        }
        
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return results.stream().limit(topK).collect(Collectors.toList());
    }
    
    @Override
    public List<Vector> search(String collectionName, Vector queryVector, int topK) {
        return search(collectionName, queryVector, topK, new HashMap<>());
    }
    
    @Override
    public List<Vector> search(String collectionName, Vector queryVector, int topK, Map<String, Object> filter) {
        return search(collectionName, queryVector.getValues(), topK, filter);
    }
    
    @Override
    public long count(String collectionName) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        return collection.size();
    }
    
    @Override
    public void clear(String collectionName) {
        Map<String, Vector> collection = getCollectionOrThrow(collectionName);
        collection.clear();
    }
    
    private Map<String, Vector> getCollectionOrThrow(String collectionName) {
        Map<String, Vector> collection = collections.get(collectionName);
        if (collection == null) {
            throw new VectorStoreRuntimeException(
                VectorStoreException.ErrorType.COLLECTION_ERROR,
                "Collection not found: " + collectionName,
                getName(),
                null
            );
        }
        return collection;
    }
    
    private double cosineSimilarity(float[] a, float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vectors must have the same dimension");
        }
        
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        
        if (normA == 0 || normB == 0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
