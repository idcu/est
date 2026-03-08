package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;
import ltd.idcu.est.ai.api.vector.Vector;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.api.vector.EmbeddingModel;
import ltd.idcu.est.ai.impl.vector.DefaultVector;
import ltd.idcu.est.ai.impl.vector.VectorStoreFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultRagRetriever implements RagRetriever {
    
    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final DocumentChunker documentChunker;
    private final String collectionName;
    private final Map<String, DocumentChunk> chunkCache;
    
    public DefaultRagRetriever(VectorStore vectorStore, EmbeddingModel embeddingModel, DocumentChunker documentChunker) {
        this(vectorStore, embeddingModel, documentChunker, "rag-documents");
    }
    
    public DefaultRagRetriever(VectorStore vectorStore, EmbeddingModel embeddingModel, 
                               DocumentChunker documentChunker, String collectionName) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.documentChunker = documentChunker;
        this.collectionName = collectionName;
        this.chunkCache = new HashMap<>();
        
        if (!vectorStore.collectionExists(collectionName)) {
            vectorStore.createCollection(collectionName, embeddingModel.getDimension());
        }
    }
    
    public DefaultRagRetriever(EmbeddingModel embeddingModel, DocumentChunker documentChunker) {
        this(VectorStoreFactory.create(), embeddingModel, documentChunker);
    }
    
    @Override
    public List<DocumentChunk> retrieve(String query, int topK) {
        float[] queryEmbedding = embeddingModel.embed(query);
        List<Vector> vectors = vectorStore.search(collectionName, queryEmbedding, topK);
        
        List<DocumentChunk> chunks = new ArrayList<>();
        for (Vector vector : vectors) {
            DocumentChunk chunk = chunkCache.get(vector.getId());
            if (chunk != null) {
                chunks.add(chunk);
            }
        }
        
        return chunks;
    }
    
    @Override
    public List<Vector> retrieveVectors(String query, int topK) {
        float[] queryEmbedding = embeddingModel.embed(query);
        return vectorStore.search(collectionName, queryEmbedding, topK);
    }
    
    @Override
    public void addDocument(Document document) {
        List<DocumentChunk> chunks = documentChunker.chunk(document);
        for (DocumentChunk chunk : chunks) {
            addChunk(chunk);
        }
    }
    
    @Override
    public void addChunk(DocumentChunk chunk) {
        float[] embedding = embeddingModel.embed(chunk.getContent());
        
        Map<String, Object> metadata = new HashMap<>(chunk.getMetadata());
        metadata.put("sourceDocumentId", chunk.getSourceDocumentId());
        metadata.put("chunkIndex", chunk.getChunkIndex());
        metadata.put("startIndex", chunk.getStartIndex());
        metadata.put("endIndex", chunk.getEndIndex());
        metadata.put("content", chunk.getContent());
        
        Vector vector = new DefaultVector(chunk.getId(), embedding, metadata);
        vectorStore.upsert(collectionName, vector);
        chunkCache.put(chunk.getId(), chunk);
    }
    
    @Override
    public void clear() {
        vectorStore.clear(collectionName);
        chunkCache.clear();
    }
    
    public VectorStore getVectorStore() {
        return vectorStore;
    }
    
    public EmbeddingModel getEmbeddingModel() {
        return embeddingModel;
    }
    
    public DocumentChunker getDocumentChunker() {
        return documentChunker;
    }
}
