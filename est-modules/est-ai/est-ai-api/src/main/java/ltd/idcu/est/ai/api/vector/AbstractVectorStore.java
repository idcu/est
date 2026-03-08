package ltd.idcu.est.ai.api.vector;

import java.util.List;
import java.util.Map;

public abstract class AbstractVectorStore implements VectorStore {
    
    protected String name;
    protected boolean connected;
    protected Map<String, Object> config;
    
    public AbstractVectorStore(String name) {
        this.name = name;
        this.connected = false;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void disconnect() {
        connected = false;
        doDisconnect();
    }
    
    protected abstract void doDisconnect();
    
    @Override
    public void createCollection(String collectionName, int dimension) {
        createCollection(collectionName, dimension, null);
    }
    
    @Override
    public List<Vector> search(String collectionName, Vector queryVector, int topK) {
        return search(collectionName, queryVector, topK, null);
    }
    
    @Override
    public List<Vector> search(String collectionName, Vector queryVector, int topK, Map<String, Object> filter) {
        return search(collectionName, queryVector.getEmbedding(), topK, filter);
    }
    
    @Override
    public List<Vector> search(String collectionName, float[] queryVector, int topK) {
        return search(collectionName, queryVector, topK, null);
    }
    
    protected void checkConnected() {
        if (!connected) {
            throw new VectorStoreException("Vector store not connected: " + name);
        }
    }
}
