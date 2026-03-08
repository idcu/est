package ltd.idcu.est.ai.api.rag;

public interface RagPipeline {
    
    RagResponse query(String query);
    
    RagResponse query(String query, int topK);
    
    void addDocument(Document document);
    
    void addDocuments(List<Document> documents);
    
    void clear();
    
    RagRetriever getRetriever();
    
    void setRetriever(RagRetriever retriever);
}
