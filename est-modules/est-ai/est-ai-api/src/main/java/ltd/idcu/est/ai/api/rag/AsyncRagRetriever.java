package ltd.idcu.est.ai.api.rag;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncRagRetriever {
    
    CompletableFuture<List<DocumentChunk>> retrieveAsync(String query, int topK);
    
    CompletableFuture<Void> addChunkAsync(DocumentChunk chunk);
    
    CompletableFuture<Void> addDocumentAsync(Document document);
    
    CompletableFuture<Void> clearAsync();
}
