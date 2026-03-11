package ltd.idcu.est.rag.api;

import java.util.List;

public interface TextSplitter {
    
    List<DocumentChunk> split(Document document);
    
    List<DocumentChunk> split(String text, String documentId);
    
    void setChunkSize(int chunkSize);
    
    void setChunkOverlap(int chunkOverlap);
    
    int getChunkSize();
    
    int getChunkOverlap();
}
