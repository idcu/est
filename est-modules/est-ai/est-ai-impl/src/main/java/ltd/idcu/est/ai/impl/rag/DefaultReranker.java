package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultReranker implements Reranker {
    
    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
    
    @Override
    public String getName() {
        return "Default Reranker";
    }
    
    @Override
    public List<DocumentChunk> rerank(String query, List<DocumentChunk> chunks, int topK) {
        List<ScoredChunk> scoredChunks = rerankWithScores(query, chunks);
        List<DocumentChunk> result = new ArrayList<>();
        
        for (int i = 0; i < Math.min(topK, scoredChunks.size()); i++) {
            result.add(scoredChunks.get(i).getChunk());
        }
        
        return result;
    }
    
    @Override
    public List<ScoredChunk> rerankWithScores(String query, List<DocumentChunk> chunks) {
        Set<String> queryWords = extractWords(query.toLowerCase());
        List<ScoredChunk> scoredChunks = new ArrayList<>();
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            double score = calculateScore(query, queryWords, chunk);
            scoredChunks.add(new DefaultScoredChunk(chunk, score, i));
        }
        
        scoredChunks.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return scoredChunks;
    }
    
    private double calculateScore(String query, Set<String> queryWords, DocumentChunk chunk) {
        String content = chunk.getContent().toLowerCase();
        Set<String> chunkWords = extractWords(content);
        
        double score = 0.0;
        
        double keywordMatchScore = calculateKeywordMatchScore(queryWords, chunkWords);
        double positionScore = calculatePositionScore(query, content);
        double lengthScore = calculateLengthScore(content);
        double densityScore = calculateDensityScore(queryWords, content);
        
        score = 0.5 * keywordMatchScore + 
                0.2 * positionScore + 
                0.15 * lengthScore + 
                0.15 * densityScore;
        
        return score;
    }
    
    private Set<String> extractWords(String text) {
        Set<String> words = new HashSet<>();
        java.util.regex.Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }
    
    private double calculateKeywordMatchScore(Set<String> queryWords, Set<String> chunkWords) {
        if (queryWords.isEmpty()) {
            return 0.0;
        }
        
        int matches = 0;
        for (String word : queryWords) {
            if (chunkWords.contains(word)) {
                matches++;
            }
        }
        
        return (double) matches / queryWords.size();
    }
    
    private double calculatePositionScore(String query, String content) {
        int firstMatchIndex = Integer.MAX_VALUE;
        String lowerQuery = query.toLowerCase();
        String[] queryTokens = lowerQuery.split("\\s+");
        
        for (String token : queryTokens) {
            if (token.length() > 2) {
                int index = content.indexOf(token);
                if (index >= 0 && index < firstMatchIndex) {
                    firstMatchIndex = index;
                }
            }
        }
        
        if (firstMatchIndex == Integer.MAX_VALUE) {
            return 0.0;
        }
        
        return 1.0 - Math.min(1.0, (double) firstMatchIndex / 1000.0);
    }
    
    private double calculateLengthScore(String content) {
        int length = content.length();
        if (length < 50) {
            return 0.3;
        } else if (length < 200) {
            return 0.7;
        } else if (length < 500) {
            return 1.0;
        } else if (length < 1000) {
            return 0.8;
        } else {
            return 0.6;
        }
    }
    
    private double calculateDensityScore(Set<String> queryWords, String content) {
        if (queryWords.isEmpty()) {
            return 0.0;
        }
        
        int totalMatches = 0;
        for (String word : queryWords) {
            int count = 0;
            int index = 0;
            while ((index = content.indexOf(word, index)) != -1) {
                count++;
                index += word.length();
            }
            totalMatches += count;
        }
        
        int wordCount = content.split("\\s+").length;
        if (wordCount == 0) {
            return 0.0;
        }
        
        double density = (double) totalMatches / wordCount;
        return Math.min(1.0, density * 5);
    }
}
