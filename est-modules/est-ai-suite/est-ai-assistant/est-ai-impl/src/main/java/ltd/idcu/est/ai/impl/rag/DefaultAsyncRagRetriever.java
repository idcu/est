package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.api.vector.EmbeddingModel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAsyncRagRetriever implements AsyncRagRetriever {
    
    private final RagRetriever delegate;
    private final ExecutorService executor;
    
    public DefaultAsyncRagRetriever(RagRetriever delegate) {
        this(delegate, Executors.newCachedThreadPool());
    }
    
    public DefaultAsyncRagRetriever(RagRetriever delegate, ExecutorService executor) {
        this.delegate = delegate;
        this.executor = executor;
    }
    
    @Override
    public CompletableFuture<List<DocumentChunk>> retrieveAsync(String query, int topK) {
        return CompletableFuture.supplyAsync(() -> delegate.retrieve(query, topK), executor);
    }
    
    @Override
    public CompletableFuture<Void> addChunkAsync(DocumentChunk chunk) {
        return CompletableFuture.runAsync(() -> delegate.addChunk(chunk), executor);
    }
    
    @Override
    public CompletableFuture<Void> addDocumentAsync(Document document) {
        return CompletableFuture.runAsync(() -> delegate.addDocument(document), executor);
    }
    
    @Override
    public CompletableFuture<Void> clearAsync() {
        return CompletableFuture.runAsync(delegate::clear, executor);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
}
