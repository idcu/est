package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultContextCompressor implements ContextCompressor {
    
    private static final int AVG_CHARS_PER_TOKEN = 4;
    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[^.!?]+[.!?]+", Pattern.UNICODE_CHARACTER_CLASS);
    
    private final Reranker reranker;
    
    public DefaultContextCompressor() {
        this(new DefaultReranker());
    }
    
    public DefaultContextCompressor(Reranker reranker) {
        this.reranker = reranker;
    }
    
    @Override
    public String getName() {
        return "Default Context Compressor";
    }
    
    @Override
    public List<DocumentChunk> compress(String query, List<DocumentChunk> chunks, int maxTokens) {
        int maxChars = maxTokens * AVG_CHARS_PER_TOKEN;
        
        List<DocumentChunk> reranked = reranker.rerank(query, chunks, chunks.size());
        List<DocumentChunk> result = new ArrayList<>();
        int totalChars = 0;
        
        for (DocumentChunk chunk : reranked) {
            String content = chunk.getContent();
            int chunkChars = content.length();
            
            if (totalChars + chunkChars <= maxChars) {
                result.add(chunk);
                totalChars += chunkChars;
            } else {
                int remainingChars = maxChars - totalChars;
                if (remainingChars > 100) {
                    DocumentChunk truncated = truncateChunk(chunk, remainingChars);
                    result.add(truncated);
                }
                break;
            }
        }
        
        return result;
    }
    
    @Override
    public String compressToString(String query, List<DocumentChunk> chunks, int maxTokens) {
        List<DocumentChunk> compressed = compress(query, chunks, maxTokens);
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < compressed.size(); i++) {
            DocumentChunk chunk = compressed.get(i);
            sb.append("--- 上下文 ").append(i + 1).append(" ---\n");
            sb.append(chunk.getContent()).append("\n\n");
        }
        
        return sb.toString();
    }
    
    private DocumentChunk truncateChunk(DocumentChunk chunk, int maxChars) {
        String content = chunk.getContent();
        if (content.length() <= maxChars) {
            return chunk;
        }
        
        List<String> sentences = extractSentences(content);
        StringBuilder truncated = new StringBuilder();
        
        for (String sentence : sentences) {
            if (truncated.length() + sentence.length() <= maxChars) {
                truncated.append(sentence);
            } else {
                break;
            }
        }
        
        if (truncated.length() == 0) {
            truncated.append(content, 0, maxChars);
        }
        
        truncated.append("...");
        
        return new DefaultDocumentChunk(
            chunk.getId(),
            truncated.toString(),
            chunk.getStartIndex(),
            chunk.getStartIndex() + truncated.length(),
            chunk.getChunkIndex(),
            chunk.getMetadata(),
            chunk.getSourceDocumentId()
        );
    }
    
    private List<String> extractSentences(String content) {
        List<String> sentences = new ArrayList<>();
        java.util.regex.Matcher matcher = SENTENCE_PATTERN.matcher(content);
        
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
}
