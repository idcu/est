package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;
import ltd.idcu.est.ai.api.vector.Vector;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.api.vector.EmbeddingModel;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultHybridRetriever extends DefaultRagRetriever implements HybridRetriever {
    
    private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+", Pattern.UNICODE_CHARACTER_CLASS);
    
    private final Map<String, Map<String, Integer>> termFreq;
    private final Map<String, Integer> docFreq;
    private final Map<String, DocumentChunk> chunkMap;
    private final double k1;
    private final double b;
    private final double avgDocLength;
    private int totalDocs;
    
    public DefaultHybridRetriever(VectorStore vectorStore, EmbeddingModel embeddingModel, DocumentChunker documentChunker) {
        super(vectorStore, embeddingModel, documentChunker);
        this.termFreq = new HashMap<>();
        this.docFreq = new HashMap<>();
        this.chunkMap = new HashMap<>();
        this.k1 = 1.5;
        this.b = 0.75;
        this.avgDocLength = 0.0;
        this.totalDocs = 0;
    }
    
    public DefaultHybridRetriever(EmbeddingModel embeddingModel, DocumentChunker documentChunker) {
        super(embeddingModel, documentChunker);
        this.termFreq = new HashMap<>();
        this.docFreq = new HashMap<>();
        this.chunkMap = new HashMap<>();
        this.k1 = 1.5;
        this.b = 0.75;
        this.avgDocLength = 0.0;
        this.totalDocs = 0;
    }
    
    @Override
    public void addChunk(DocumentChunk chunk) {
        super.addChunk(chunk);
        indexChunk(chunk);
    }
    
    @Override
    public void addDocument(Document document) {
        super.addDocument(document);
    }
    
    @Override
    public void clear() {
        super.clear();
        termFreq.clear();
        docFreq.clear();
        chunkMap.clear();
        totalDocs = 0;
    }
    
    private void indexChunk(DocumentChunk chunk) {
        String content = chunk.getContent().toLowerCase();
        Set<String> words = extractWords(content);
        Map<String, Integer> tf = new HashMap<>();
        
        for (String word : words) {
            tf.put(word, tf.getOrDefault(word, 0) + 1);
        }
        
        termFreq.put(chunk.getId(), tf);
        chunkMap.put(chunk.getId(), chunk);
        
        for (String word : tf.keySet()) {
            docFreq.put(word, docFreq.getOrDefault(word, 0) + 1);
        }
        
        totalDocs++;
        updateAvgDocLength();
    }
    
    private void updateAvgDocLength() {
        double totalLength = 0.0;
        for (DocumentChunk chunk : chunkMap.values()) {
            totalLength += chunk.getContent().length();
        }
        avgDocLength = totalDocs > 0 ? totalLength / totalDocs : 0.0;
    }
    
    @Override
    public List<DocumentChunk> bm25Search(String query, int topK) {
        String lowerQuery = query.toLowerCase();
        Set<String> queryWords = extractWords(lowerQuery);
        
        Map<String, Double> scores = new HashMap<>();
        
        for (Map.Entry<String, DocumentChunk> entry : chunkMap.entrySet()) {
            String chunkId = entry.getKey();
            DocumentChunk chunk = entry.getValue();
            double score = calculateBM25Score(queryWords, chunkId, chunk);
            scores.put(chunkId, score);
        }
        
        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(scores.entrySet());
        sortedScores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        List<DocumentChunk> result = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, sortedScores.size()); i++) {
            String chunkId = sortedScores.get(i).getKey();
            result.add(chunkMap.get(chunkId));
        }
        
        return result;
    }
    
    private double calculateBM25Score(Set<String> queryWords, String chunkId, DocumentChunk chunk) {
        double score = 0.0;
        Map<String, Integer> tf = termFreq.get(chunkId);
        
        if (tf == null) {
            return 0.0;
        }
        
        int docLength = chunk.getContent().length();
        
        for (String word : queryWords) {
            int f = tf.getOrDefault(word, 0);
            if (f == 0) {
                continue;
            }
            
            int df = docFreq.getOrDefault(word, 0);
            if (df == 0) {
                continue;
            }
            
            double idf = Math.log(1 + (totalDocs - df + 0.5) / (df + 0.5));
            double tfComponent = (f * (k1 + 1)) / (f + k1 * (1 - b + b * docLength / avgDocLength));
            score += idf * tfComponent;
        }
        
        return score;
    }
    
    @Override
    public List<DocumentChunk> hybridSearch(String query, int topK, double bm25Weight, double vectorWeight) {
        List<DocumentChunk> bm25Results = bm25Search(query, topK * 2);
        List<DocumentChunk> vectorResults = retrieve(query, topK * 2);
        
        Map<String, Double> combinedScores = new HashMap<>();
        
        for (int i = 0; i < bm25Results.size(); i++) {
            DocumentChunk chunk = bm25Results.get(i);
            double normalizedRank = 1.0 - (double) i / bm25Results.size();
            combinedScores.put(chunk.getId(), bm25Weight * normalizedRank);
        }
        
        for (int i = 0; i < vectorResults.size(); i++) {
            DocumentChunk chunk = vectorResults.get(i);
            double normalizedRank = 1.0 - (double) i / vectorResults.size();
            double currentScore = combinedScores.getOrDefault(chunk.getId(), 0.0);
            combinedScores.put(chunk.getId(), currentScore + vectorWeight * normalizedRank);
        }
        
        Map<String, DocumentChunk> allChunks = new HashMap<>();
        for (DocumentChunk chunk : bm25Results) {
            allChunks.put(chunk.getId(), chunk);
        }
        for (DocumentChunk chunk : vectorResults) {
            allChunks.put(chunk.getId(), chunk);
        }
        
        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(combinedScores.entrySet());
        sortedScores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        List<DocumentChunk> result = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, sortedScores.size()); i++) {
            String chunkId = sortedScores.get(i).getKey();
            result.add(allChunks.get(chunkId));
        }
        
        return result;
    }
    
    private Set<String> extractWords(String text) {
        Set<String> words = new HashSet<>();
        java.util.regex.Matcher matcher = WORD_PATTERN.matcher(text);
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }
        return words;
    }
}
