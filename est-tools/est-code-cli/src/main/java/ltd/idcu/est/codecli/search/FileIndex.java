package ltd.idcu.est.codecli.search;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileIndex {
    
    private final Map<String, FileDocument> documents = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> invertedIndex = new ConcurrentHashMap<>();
    private final SearchHistory searchHistory;
    private final Set<String> stopWords = new HashSet<>(Arrays.asList(
        "the", "a", "an", "and", "or", "but", "is", "are", "was", "were",
        "be", "been", "being", "have", "has", "had", "do", "does", "did",
        "will", "would", "could", "should", "may", "might", "must", "shall",
        "can", "need", "dare", "ought", "used", "to", "of", "in", "for",
        "on", "with", "at", "by", "from", "as", "into", "through", "during",
        "before", "after", "above", "below", "between", "under", "again",
        "further", "then", "once", "here", "there", "when", "where", "why",
        "how", "all", "any", "both", "each", "few", "more", "most", "other",
        "some", "such", "no", "nor", "not", "only", "own", "same", "so",
        "than", "too", "very", "just", "also", "now", "public", "private",
        "protected", "static", "final", "class", "interface", "extends",
        "implements", "return", "if", "else", "for", "while", "do", "switch",
        "case", "break", "continue", "try", "catch", "finally", "throw",
        "throws", "package", "import", "this", "super", "new", "void", "int",
        "long", "double", "float", "char", "byte", "short", "boolean", "string"
    ));
    
    public FileIndex() {
        this.searchHistory = new SearchHistory();
    }
    
    public FileIndex(SearchHistory searchHistory) {
        this.searchHistory = searchHistory;
    }
    
    public void indexFile(String filePath, String content) {
        FileDocument doc = new FileDocument(filePath, content);
        documents.put(filePath, doc);
        
        String[] tokens = tokenize(content);
        for (String token : tokens) {
            if (!stopWords.contains(token.toLowerCase()) && token.length() > 1) {
                invertedIndex.computeIfAbsent(token.toLowerCase(), k -> new HashSet<>()).add(filePath);
            }
        }
    }
    
    public void removeFile(String filePath) {
        documents.remove(filePath);
        for (Set<String> fileSet : invertedIndex.values()) {
            fileSet.remove(filePath);
        }
    }
    
    public List<SearchResult> search(String query) {
        return search(query, new SearchFilter());
    }
    
    public List<SearchResult> search(String query, SearchFilter filter) {
        String[] queryTokens = tokenize(query);
        Map<String, Integer> scoreMap = new HashMap<>();
        
        for (String token : queryTokens) {
            String lowerToken = token.toLowerCase();
            
            Set<String> matchingTokens = new HashSet<>();
            matchingTokens.add(lowerToken);
            
            if (filter.isFuzzySearch()) {
                for (String indexToken : invertedIndex.keySet()) {
                    if (levenshteinDistance(lowerToken, indexToken) <= filter.getFuzzyDistance()) {
                        matchingTokens.add(indexToken);
                    }
                }
            }
            
            for (String matchToken : matchingTokens) {
                Set<String> filePaths = invertedIndex.get(matchToken);
                if (filePaths != null) {
                    for (String filePath : filePaths) {
                        if (filter.matches(filePath, scoreMap.getOrDefault(filePath, 0) + 1)) {
                            scoreMap.put(filePath, scoreMap.getOrDefault(filePath, 0) + 1);
                        }
                    }
                }
            }
        }
        
        List<SearchResult> results = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scoreMap.entrySet()) {
            FileDocument doc = documents.get(entry.getKey());
            if (doc != null) {
                String snippet = getSnippet(doc.getContent(), query);
                String highlightedSnippet = highlightMatches(snippet, query, filter.isCaseSensitive());
                results.add(new SearchResult(doc, entry.getValue(), snippet, highlightedSnippet));
            }
        }
        
        results.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        
        if (filter.getMaxResults() != null && results.size() > filter.getMaxResults()) {
            results = results.subList(0, filter.getMaxResults());
        }
        
        searchHistory.add(query, results.size());
        return results;
    }
    
    public Set<String> getAllIndexedFiles() {
        return new HashSet<>(documents.keySet());
    }
    
    public void clear() {
        documents.clear();
        invertedIndex.clear();
    }
    
    public SearchHistory getSearchHistory() {
        return searchHistory;
    }
    
    private String[] tokenize(String text) {
        return text.split("[^a-zA-Z0-9_]+");
    }
    
    private String getSnippet(String content, String query) {
        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();
        int index = lowerContent.indexOf(lowerQuery);
        
        if (index == -1) {
            return content.length() > 200 ? content.substring(0, 200) + "..." : content;
        }
        
        int start = Math.max(0, index - 50);
        int end = Math.min(content.length(), index + query.length() + 50);
        
        String snippet = content.substring(start, end);
        if (start > 0) {
            snippet = "..." + snippet;
        }
        if (end < content.length()) {
            snippet = snippet + "...";
        }
        
        return snippet;
    }
    
    private String highlightMatches(String text, String query, boolean caseSensitive) {
        if (query == null || query.trim().isEmpty()) {
            return text;
        }
        
        String pattern = Pattern.quote(query);
        int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
        Pattern p = Pattern.compile(pattern, flags);
        Matcher m = p.matcher(text);
        
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "<<$0>>");
        }
        m.appendTail(sb);
        
        return sb.toString();
    }
    
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    public static class FileDocument {
        private final String filePath;
        private final String content;
        private final long indexedAt;
        
        public FileDocument(String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
            this.indexedAt = System.currentTimeMillis();
        }
        
        public String getFilePath() {
            return filePath;
        }
        
        public String getContent() {
            return content;
        }
        
        public long getIndexedAt() {
            return indexedAt;
        }
    }
    
    public static class SearchResult {
        private final FileDocument document;
        private final int score;
        private final String snippet;
        private final String highlightedSnippet;
        
        public SearchResult(FileDocument document, int score, String snippet) {
            this(document, score, snippet, snippet);
        }
        
        public SearchResult(FileDocument document, int score, String snippet, String highlightedSnippet) {
            this.document = document;
            this.score = score;
            this.snippet = snippet;
            this.highlightedSnippet = highlightedSnippet;
        }
        
        public FileDocument getDocument() {
            return document;
        }
        
        public int getScore() {
            return score;
        }
        
        public String getSnippet() {
            return snippet;
        }
        
        public String getHighlightedSnippet() {
            return highlightedSnippet;
        }
    }
}
