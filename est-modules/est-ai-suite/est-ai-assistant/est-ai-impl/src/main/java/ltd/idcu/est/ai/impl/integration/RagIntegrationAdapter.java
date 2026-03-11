package ltd.idcu.est.ai.impl.integration;

import ltd.idcu.est.rag.api.*;
import ltd.idcu.est.rag.impl.*;

import java.util.ArrayList;
import java.util.List;

public class RagIntegrationAdapter {
    
    private final RagEngine ragEngine;
    
    public RagIntegrationAdapter() {
        this(new DefaultRagEngine());
    }
    
    public RagIntegrationAdapter(RagEngine ragEngine) {
        this.ragEngine = ragEngine;
        initializeDefaultComponents();
    }
    
    private void initializeDefaultComponents() {
        if (ragEngine.getVectorStore() == null) {
            ragEngine.setVectorStore(new InMemoryVectorStore());
        }
        if (ragEngine.getTextSplitter() == null) {
            ragEngine.setTextSplitter(new FixedSizeTextSplitter(500, 100));
        }
        if (ragEngine.getEmbeddingModel() == null) {
            ragEngine.setEmbeddingModel(new SimpleEmbeddingModel());
        }
    }
    
    public void addDocument(String id, String content, String metadata) {
        Document doc = new Document(id, content, metadata);
        ragEngine.addDocument(doc);
    }
    
    public void addDocuments(List<DocumentInfo> documentInfos) {
        List<Document> docs = new ArrayList<>();
        for (DocumentInfo info : documentInfos) {
            docs.add(new Document(info.id(), info.content(), info.metadata()));
        }
        ragEngine.addDocuments(docs);
    }
    
    public List<SearchResultInfo> retrieve(String query, int topK) {
        List<SearchResult> results = ragEngine.retrieve(query, topK);
        List<SearchResultInfo> infos = new ArrayList<>();
        for (SearchResult result : results) {
            infos.add(new SearchResultInfo(
                result.getDocumentId(),
                result.getContent(),
                result.getScore(),
                result.getMetadata()
            ));
        }
        return infos;
    }
    
    public String generateWithRag(String query, int topK) {
        return ragEngine.retrieveAndGenerate(query, topK);
    }
    
    public void setVectorStore(VectorStore vectorStore) {
        ragEngine.setVectorStore(vectorStore);
    }
    
    public void setTextSplitter(TextSplitter textSplitter) {
        ragEngine.setTextSplitter(textSplitter);
    }
    
    public void setEmbeddingModel(EmbeddingModel embeddingModel) {
        ragEngine.setEmbeddingModel(embeddingModel);
    }
    
    public VectorStore getVectorStore() {
        return ragEngine.getVectorStore();
    }
    
    public TextSplitter getTextSplitter() {
        return ragEngine.getTextSplitter();
    }
    
    public EmbeddingModel getEmbeddingModel() {
        return ragEngine.getEmbeddingModel();
    }
    
    public RagEngine getRagEngine() {
        return ragEngine;
    }
    
    public record DocumentInfo(String id, String content, String metadata) {}
    
    public record SearchResultInfo(String documentId, String content, double score, String metadata) {}
}
