package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Embedding;
import ltd.idcu.est.rag.api.EmbeddingModel;
import ltd.idcu.est.rag.api.SearchResult;
import ltd.idcu.est.rag.api.VectorStore;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryVectorStore implements VectorStore {
    
    private final Map<String, Embedding> embeddings = new HashMap<>();
    private final Map<String, String> chunkContentMap = new HashMap<>();
    private final Map<String, String> chunkToDocumentMap = new HashMap<>();
    private EmbeddingModel embeddingModel;
    
    public InMemoryVectorStore() {
    }
    
    public InMemoryVectorStore(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
    
    @Override
    public void addEmbeddings(List<Embedding> embeddingList) {
        for (Embedding embedding : embeddingList) {
            addEmbedding(embedding);
        }
    }
    
    @Override
    public void addEmbedding(Embedding embedding) {
        embeddings.put(embedding.getId(), embedding);
        if (embedding.getChunkId() != null) {
            chunkToDocumentMap.put(embedding.getChunkId(), embedding.getDocumentId());
        }
    }
    
    public void addChunkContent(String chunkId, String content) {
        chunkContentMap.put(chunkId, content);
    }
    
    @Override
    public List<SearchResult> search(float[] queryVector, int topK) {
        List<SearchResult> results = new ArrayList<>();
        
        for (Map.Entry<String, Embedding> entry : embeddings.entrySet()) {
            Embedding embedding = entry.getValue();
            double similarity = cosineSimilarity(queryVector, embedding.getVector());
            
            SearchResult result = new SearchResult();
            result.setChunkId(embedding.getChunkId());
            result.setDocumentId(embedding.getDocumentId());
            result.setContent(chunkContentMap.get(embedding.getChunkId()));
            result.setScore(similarity);
            
            results.add(result);
        }
        
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return results.stream().limit(topK).collect(Collectors.toList());
    }
    
    @Override
    public List<SearchResult> search(String query, int topK) {
        if (embeddingModel == null) {
            throw new IllegalStateException("EmbeddingModel not set");
        }
        
        float[] queryVector = embeddingModel.embedToVector(query);
        return search(queryVector, topK);
    }
    
    @Override
    public void delete(String id) {
        embeddings.remove(id);
    }
    
    @Override
    public void deleteByDocumentId(String documentId) {
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, Embedding> entry : embeddings.entrySet()) {
            if (documentId.equals(entry.getValue().getDocumentId())) {
                toRemove.add(entry.getKey());
            }
        }
        for (String id : toRemove) {
            embeddings.remove(id);
        }
    }
    
    @Override
    public void clear() {
        embeddings.clear();
        chunkContentMap.clear();
        chunkToDocumentMap.clear();
    }
    
    @Override
    public int size() {
        return embeddings.size();
    }
    
    @Override
    public void setEmbeddingModel(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
    
    @Override
    public EmbeddingModel getEmbeddingModel() {
        return embeddingModel;
    }
    
    private double cosineSimilarity(float[] v1, float[] v2) {
        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < v1.length; i++) {
            dotProduct += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        
        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
