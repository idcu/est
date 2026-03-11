package ltd.idcu.est.rag.api;

public class Embedding {
    
    private String id;
    private float[] vector;
    private String chunkId;
    private String documentId;
    
    public Embedding() {
    }
    
    public Embedding(String id, float[] vector) {
        this.id = id;
        this.vector = vector;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public float[] getVector() {
        return vector;
    }
    
    public void setVector(float[] vector) {
        this.vector = vector;
    }
    
    public String getChunkId() {
        return chunkId;
    }
    
    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }
    
    public String getDocumentId() {
        return documentId;
    }
    
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
