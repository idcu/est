package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.Document;
import ltd.idcu.est.ai.api.rag.DocumentChunk;
import ltd.idcu.est.ai.api.rag.DocumentChunker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultDocumentChunker implements DocumentChunker {
    
    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private static final int DEFAULT_OVERLAP_SIZE = 200;
    private static final String STRATEGY_SENTENCE = "sentence";
    private static final String STRATEGY_PARAGRAPH = "paragraph";
    private static final String STRATEGY_HEADING = "heading";
    private static final String STRATEGY_SMART = "smart";
    
    private int chunkSize;
    private int overlapSize;
    private String strategy;
    private int totalDocumentsProcessed;
    private int totalChunksCreated;
    
    public DefaultDocumentChunker() {
        this(DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP_SIZE, STRATEGY_SENTENCE);
    }
    
    public DefaultDocumentChunker(int chunkSize, int overlapSize) {
        this(chunkSize, overlapSize, STRATEGY_SENTENCE);
    }
    
    public DefaultDocumentChunker(int chunkSize, int overlapSize, String strategy) {
        this.chunkSize = chunkSize;
        this.overlapSize = overlapSize;
        this.strategy = strategy;
        this.totalDocumentsProcessed = 0;
        this.totalChunksCreated = 0;
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
        
        totalDocumentsProcessed++;
        totalChunksCreated += chunks.size();
        
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
    
    @Override
    public List<DocumentChunk> chunkSmart(Document document) {
        String content = document.getContent();
        
        if (hasHeadings(content)) {
            return chunkByHeading(document);
        } else if (hasParagraphs(content)) {
            return chunkByParagraph(document);
        } else {
            return chunk(document);
        }
    }
    
    @Override
    public List<DocumentChunk> chunkByParagraph(Document document) {
        List<DocumentChunk> chunks = new ArrayList<>();
        String content = document.getContent();
        String documentId = document.getId();
        
        if (content == null || content.isEmpty()) {
            return chunks;
        }
        
        String[] paragraphs = content.split("\\n\\s*\\n");
        StringBuilder currentChunk = new StringBuilder();
        int currentStartIndex = 0;
        int chunkIndex = 0;
        int totalChars = 0;
        
        for (String paragraph : paragraphs) {
            paragraph = paragraph.trim();
            if (paragraph.isEmpty()) {
                continue;
            }
            
            if (currentChunk.length() + paragraph.length() > chunkSize && currentChunk.length() > 0) {
                chunks.add(createChunk(currentChunk.toString(), currentStartIndex, 
                                        totalChars, chunkIndex++, documentId));
                
                int overlapStart = Math.max(0, currentChunk.length() - overlapSize);
                String overlapText = currentChunk.substring(overlapStart);
                currentChunk = new StringBuilder(overlapText);
                currentStartIndex = totalChars - overlapText.length();
            }
            
            if (currentChunk.length() > 0) {
                currentChunk.append("\n\n");
            }
            currentChunk.append(paragraph);
            totalChars += paragraph.length() + 2;
        }
        
        if (currentChunk.length() > 0) {
            chunks.add(createChunk(currentChunk.toString(), currentStartIndex, 
                                    totalChars, chunkIndex, documentId));
        }
        
        totalDocumentsProcessed++;
        totalChunksCreated += chunks.size();
        
        return chunks;
    }
    
    @Override
    public List<DocumentChunk> chunkByHeading(Document document) {
        List<DocumentChunk> chunks = new ArrayList<>();
        String content = document.getContent();
        String documentId = document.getId();
        
        if (content == null || content.isEmpty()) {
            return chunks;
        }
        
        Pattern headingPattern = Pattern.compile("^#{1,6}\\s+.+$", Pattern.MULTILINE);
        Matcher matcher = headingPattern.matcher(content);
        
        List<Integer> headingPositions = new ArrayList<>();
        while (matcher.find()) {
            headingPositions.add(matcher.start());
        }
        
        if (headingPositions.isEmpty()) {
            return chunk(document);
        }
        
        int lastPos = 0;
        int chunkIndex = 0;
        
        for (int pos : headingPositions) {
            if (pos > lastPos) {
                String section = content.substring(lastPos, pos).trim();
                if (!section.isEmpty()) {
                    chunks.add(createChunk(section, lastPos, pos, chunkIndex++, documentId));
                }
            }
            lastPos = pos;
        }
        
        if (lastPos < content.length()) {
            String lastSection = content.substring(lastPos).trim();
            if (!lastSection.isEmpty()) {
                chunks.add(createChunk(lastSection, lastPos, content.length(), chunkIndex, documentId));
            }
        }
        
        totalDocumentsProcessed++;
        totalChunksCreated += chunks.size();
        
        return chunks;
    }
    
    @Override
    public void setChunkerStrategy(String strategy) {
        this.strategy = strategy;
    }
    
    @Override
    public String getChunkerStrategy() {
        return strategy;
    }
    
    @Override
    public Map<String, Object> getChunkingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDocumentsProcessed", totalDocumentsProcessed);
        stats.put("totalChunksCreated", totalChunksCreated);
        stats.put("currentStrategy", strategy);
        stats.put("chunkSize", chunkSize);
        stats.put("overlapSize", overlapSize);
        return stats;
    }
    
    private boolean hasHeadings(String content) {
        Pattern headingPattern = Pattern.compile("^#{1,6}\\s+.+$", Pattern.MULTILINE);
        Matcher matcher = headingPattern.matcher(content);
        int count = 0;
        while (matcher.find() && count < 3) {
            count++;
        }
        return count >= 2;
    }
    
    private boolean hasParagraphs(String content) {
        String[] paragraphs = content.split("\\n\\s*\\n");
        return paragraphs.length >= 3;
    }
}
