package ltd.idcu.est.ai.api.rag;

import ltd.idcu.est.ai.api.vector.Vector;

import java.util.List;

public interface RagRetriever {
    
    List<DocumentChunk> retrieve(String query, int topK);
    
    List<Vector> retrieveVectors(String query, int topK);
    
    void addDocument(Document document);
    
    void addChunk(DocumentChunk chunk);
    
    void clear();
}
