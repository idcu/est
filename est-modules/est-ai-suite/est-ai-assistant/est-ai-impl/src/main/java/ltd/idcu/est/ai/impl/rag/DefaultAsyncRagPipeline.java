package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.llm.LlmClient;
import ltd.idcu.est.ai.api.rag.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultAsyncRagPipeline implements AsyncRagPipeline {
    
    private final RagPipeline delegate;
    private final ExecutorService executor;
    
    public DefaultAsyncRagPipeline(RagPipeline delegate) {
        this(delegate, Executors.newCachedThreadPool());
    }
    
    public DefaultAsyncRagPipeline(RagPipeline delegate, ExecutorService executor) {
        this.delegate = delegate;
        this.executor = executor;
    }
    
    @Override
    public CompletableFuture<RagResponse> generateAsync(String query) {
        return CompletableFuture.supplyAsync(() -> delegate.generate(query), executor);
    }
    
    @Override
    public CompletableFuture<RagResponse> generateAsync(String query, int topK) {
        return CompletableFuture.supplyAsync(() -> delegate.generate(query, topK), executor);
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
