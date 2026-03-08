package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.Document;
import ltd.idcu.est.ai.api.rag.DocumentChunk;
import ltd.idcu.est.ai.api.rag.DocumentChunker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDocumentChunker implements DocumentChunker {
    
    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private static final int DEFAULT_OVERLAP_SIZE = 200;
    
    private int chunkSize;
    private int overlapSize;
    
    public DefaultDocumentChunker() {
        this(DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP_SIZE);
    }
    
    public DefaultDocumentChunker(int chunkSize, int overlapSize) {
        this.chunkSize = chunkSize;
        this.overlapSize = overlapSize;
    }
    
    @Override
    public List<DocumentChunk> chunk(Document document) {
        return chunk(document.getContent(), document.getId());
    }
    
    @Override
    public List<DocumentChunk> chunk(String content) {
        return chunk(content, null);
    }
    
    @Override
    public List<DocumentChunk> chunk(String content, String documentId) {
        List<DocumentChunk> chunks = new ArrayList<>();
        
        if (content == null || content.isEmpty()) {
            return chunks;
        }
        
        List<String> sentences = splitIntoSentences(content);
        
        StringBuilder currentChunk = new StringBuilder();
        int currentStartIndex = 0;
        int chunkIndex = 0;
        int totalChars = 0;
        
        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > chunkSize && currentChunk.length() > 0) {
                chunks.add(createChunk(currentChunk.toString(), currentStartIndex, 
                                        totalChars, chunkIndex++, documentId));
                
                int overlapStart = Math.max(0, currentChunk.length() - overlapSize);
                String overlapText = currentChunk.substring(overlapStart);
                currentChunk = new StringBuilder(overlapText);
                currentStartIndex = totalChars - overlapText.length();
            }
            
            if (currentChunk.length() > 0) {
                currentChunk.append(" ");
            }
            currentChunk.append(sentence);
            totalChars += sentence.length() + 1;
        }
        
        if (currentChunk.length() > 0) {
            chunks.add(createChunk(currentChunk.toString(), currentStartIndex, 
                                    totalChars, chunkIndex, documentId));
        }
        
        return chunks;
    }
    
    private List<String> splitIntoSentences(String content) {
        List<String> sentences = new ArrayList<>();
        
        Pattern pattern = Pattern.compile("[^.!?]+[.!?]+", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(content);
        
        int lastEnd = 0;
        while (matcher.find()) {
            sentences.add(matcher.group().trim());
            lastEnd = matcher.end();
        }
        
        if (lastEnd < content.length()) {
            String remaining = content.substring(lastEnd).trim();
            if (!remaining.isEmpty()) {
                sentences.add(remaining);
            }
        }
        
        if (sentences.isEmpty()) {
            sentences.add(content);
        }
        
        return sentences;
    }
    
    private DocumentChunk createChunk(String content, int startIndex, int endIndex, 
                                      int chunkIndex, String documentId) {
        return new DefaultDocumentChunk(content.trim(), startIndex, endIndex, chunkIndex, documentId);
    }
    
    @Override
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    @Override
    public void setOverlapSize(int overlapSize) {
        this.overlapSize = overlapSize;
    }
    
    @Override
    public int getChunkSize() {
        return chunkSize;
    }
    
    @Override
    public int getOverlapSize() {
        return overlapSize;
    }
}
