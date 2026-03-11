package ltd.idcu.est.rag.api;

import java.util.List;

public interface RagEngine {
    
    void addDocument(Document document);
    
    void addDocuments(List<Document> documents);
    
    List<SearchResult> retrieve(String query, int topK);
    
    String generate(String query, List<SearchResult> contexts);
    
    String retrieveAndGenerate(String query, int topK);
    
    void setVectorStore(VectorStore vectorStore);
    
    void setTextSplitter(TextSplitter textSplitter);
    
    void setEmbeddingModel(EmbeddingModel embeddingModel);
    
    VectorStore getVectorStore();
    
    TextSplitter getTextSplitter();
    
    EmbeddingModel getEmbeddingModel();
}
