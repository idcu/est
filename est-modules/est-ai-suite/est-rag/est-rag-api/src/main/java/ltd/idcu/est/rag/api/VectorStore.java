package ltd.idcu.est.rag.api;

import java.util.List;

public interface VectorStore {
    
    void addEmbeddings(List<Embedding> embeddings);
    
    void addEmbedding(Embedding embedding);
    
    List<SearchResult> search(float[] queryVector, int topK);
    
    List<SearchResult> search(String query, int topK);
    
    void delete(String id);
    
    void deleteByDocumentId(String documentId);
    
    void clear();
    
    int size();
    
    void setEmbeddingModel(EmbeddingModel embeddingModel);
    
    EmbeddingModel getEmbeddingModel();
}
