package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Document;
import ltd.idcu.est.rag.api.DocumentChunk;
import ltd.idcu.est.rag.api.TextSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FixedSizeTextSplitter implements TextSplitter {
    
    private int chunkSize = 512;
    private int chunkOverlap = 100;
    
    public FixedSizeTextSplitter() {
    }
    
    public FixedSizeTextSplitter(int chunkSize, int chunkOverlap) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }
    
    @Override
    public List<DocumentChunk> split(Document document) {
        return split(document.getContent(), document.getId());
    }
    
    @Override
    public List<DocumentChunk> split(String text, String documentId) {
        List<DocumentChunk> chunks = new ArrayList<>();
        
        if (text == null || text.isEmpty()) {
            return chunks;
        }
        
        int textLength = text.length();
        int start = 0;
        int chunkIndex = 0;
        
        while (start < textLength) {
            int end = Math.min(start + chunkSize, textLength);
            
            if (end < textLength) {
                int lastSpace = text.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace;
                }
            }
            
            String chunkContent = text.substring(start, end);
            String chunkId = UUID.randomUUID().toString();
            
            DocumentChunk chunk = new DocumentChunk(chunkId, documentId, chunkContent, chunkIndex);
            chunk.setStartPosition(start);
            chunk.setEndPosition(end);
            
            chunks.add(chunk);
            
            start = end - chunkOverlap;
            if (start < 0) {
                start = 0;
            }
            chunkIndex++;
        }
        
        return chunks;
    }
    
    @Override
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    @Override
    public void setChunkOverlap(int chunkOverlap) {
        this.chunkOverlap = chunkOverlap;
    }
    
    @Override
    public int getChunkSize() {
        return chunkSize;
    }
    
    @Override
    public int getChunkOverlap() {
        return chunkOverlap;
    }
}
