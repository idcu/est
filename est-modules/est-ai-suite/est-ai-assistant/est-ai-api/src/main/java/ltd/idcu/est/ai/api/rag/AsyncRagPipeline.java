package ltd.idcu.est.ai.api.rag;

import java.util.concurrent.CompletableFuture;

public interface AsyncRagPipeline {
    
    CompletableFuture<RagResponse> generateAsync(String query);
    
    CompletableFuture<RagResponse> generateAsync(String query, int topK);
    
    CompletableFuture<Void> addDocumentAsync(Document document);
    
    CompletableFuture<Void> clearAsync();
}
