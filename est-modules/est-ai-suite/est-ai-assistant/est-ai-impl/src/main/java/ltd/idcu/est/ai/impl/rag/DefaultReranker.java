package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultReranker implements Reranker {
    
    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
    private static final String STRATEGY_DEFAULT = "default";
    private static final String STRATEGY_BM25 = "bm25";
    private static final String STRATEGY_TFIDF = "tfidf";
    
    private String strategy;
    private Map<String, Double> weights;
    private int totalQueries;
    private int totalChunksReranked;
    
    public DefaultReranker() {
        this.strategy = STRATEGY_DEFAULT;
        this.weights = new HashMap<>();
        this.weights.put("keywordMatch", 0.5);
        this.weights.put("position", 0.2);
        this.weights.put("length", 0.15);
        this.weights.put("density", 0.15);
        this.totalQueries = 0;
        this.totalChunksReranked = 0;
    }
    
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
    
    @Override
    public void setRerankStrategy(String strategy) {
        this.strategy = strategy;
    }
    
    @Override
    public String getRerankStrategy() {
        return strategy;
    }
    
    @Override
    public void setWeights(Map<String, Double> weights) {
        this.weights = new HashMap<>(weights);
    }
    
    @Override
    public Map<String, Double> getWeights() {
        return new HashMap<>(weights);
    }
    
    @Override
    public Map<String, Object> getRerankingStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQueries", totalQueries);
        stats.put("totalChunksReranked", totalChunksReranked);
        stats.put("currentStrategy", strategy);
        stats.put("weights", new HashMap<>(weights));
        return stats;
    }
    
    @Override
    public List<ScoredChunk> rerankWithScores(String query, List<DocumentChunk> chunks) {
        totalQueries++;
        totalChunksReranked += chunks.size();
        
        if (STRATEGY_BM25.equals(strategy)) {
            return rerankWithBM25(query, chunks);
        } else if (STRATEGY_TFIDF.equals(strategy)) {
            return rerankWithTFIDF(query, chunks);
        } else {
            return rerankWithDefault(query, chunks);
        }
    }
    
    private List<ScoredChunk> rerankWithDefault(String query, List<DocumentChunk> chunks) {
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
    
    private List<ScoredChunk> rerankWithBM25(String query, List<DocumentChunk> chunks) {
        Set<String> queryWords = extractWords(query.toLowerCase());
        List<ScoredChunk> scoredChunks = new ArrayList<>();
        
        double k1 = 1.5;
        double b = 0.75;
        double avgDocLength = calculateAverageDocLength(chunks);
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            String content = chunk.getContent().toLowerCase();
            Set<String> chunkWords = extractWords(content);
            int docLength = chunkWords.size();
            
            double score = 0.0;
            for (String word : queryWords) {
                int f = countWordOccurrences(word, content);
                if (f > 0) {
                    double numerator = f * (k1 + 1);
                    double denominator = f + k1 * (1 - b + b * docLength / avgDocLength);
                    score += Math.log((chunks.size() - countDocumentsWithWord(word, chunks) + 0.5) / 
                                      (countDocumentsWithWord(word, chunks) + 0.5)) * 
                             (numerator / denominator);
                }
            }
            
            scoredChunks.add(new DefaultScoredChunk(chunk, score, i));
        }
        
        scoredChunks.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return scoredChunks;
    }
    
    private List<ScoredChunk> rerankWithTFIDF(String query, List<DocumentChunk> chunks) {
        Set<String> queryWords = extractWords(query.toLowerCase());
        List<ScoredChunk> scoredChunks = new ArrayList<>();
        
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            String content = chunk.getContent().toLowerCase();
            Set<String> chunkWords = extractWords(content);
            
            double score = 0.0;
            for (String word : queryWords) {
                if (chunkWords.contains(word)) {
                    double tf = (double) countWordOccurrences(word, content) / chunkWords.size();
                    double idf = Math.log((double) chunks.size() / (1 + countDocumentsWithWord(word, chunks)));
                    score += tf * idf;
                }
            }
            
            scoredChunks.add(new DefaultScoredChunk(chunk, score, i));
        }
        
        scoredChunks.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        return scoredChunks;
    }
    
    private double calculateAverageDocLength(List<DocumentChunk> chunks) {
        if (chunks.isEmpty()) return 1.0;
        
        int totalLength = 0;
        for (DocumentChunk chunk : chunks) {
            totalLength += extractWords(chunk.getContent().toLowerCase()).size();
        }
        return (double) totalLength / chunks.size();
    }
    
    private int countWordOccurrences(String word, String content) {
        int count = 0;
        int index = 0;
        while ((index = content.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }
    
    private int countDocumentsWithWord(String word, List<DocumentChunk> chunks) {
        int count = 0;
        for (DocumentChunk chunk : chunks) {
            if (chunk.getContent().toLowerCase().contains(word)) {
                count++;
            }
        }
        return count;
    }
}
