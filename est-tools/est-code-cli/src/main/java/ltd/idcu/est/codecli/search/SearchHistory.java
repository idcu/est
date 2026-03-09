package ltd.idcu.est.codecli.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchHistory {

    private final LinkedList<SearchEntry> history;
    private final int maxSize;

    public SearchHistory() {
        this(50);
    }

    public SearchHistory(int maxSize) {
        this.history = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public void add(String query, int resultCount) {
        SearchEntry entry = new SearchEntry(query, resultCount);
        history.addFirst(entry);
        trimHistory();
    }

    public List<SearchEntry> getAll() {
        return new ArrayList<>(history);
    }

    public List<String> getRecentQueries(int count) {
        List<String> queries = new ArrayList<>();
        int limit = Math.min(count, history.size());
        for (int i = 0; i < limit; i++) {
            queries.add(history.get(i).getQuery());
        }
        return queries;
    }

    public SearchEntry getLast() {
        if (history.isEmpty()) {
            return null;
        }
        return history.getFirst();
    }

    public void clear() {
        history.clear();
    }

    public int size() {
        return history.size();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public List<SearchEntry> findSimilar(String query) {
        List<SearchEntry> similar = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (SearchEntry entry : history) {
            if (entry.getQuery().toLowerCase().contains(lowerQuery) ||
                lowerQuery.contains(entry.getQuery().toLowerCase())) {
                similar.add(entry);
            }
        }
        return similar;
    }

    private void trimHistory() {
        while (history.size() > maxSize) {
            history.removeLast();
        }
    }

    public static class SearchEntry {
        private final String query;
        private final int resultCount;
        private final long timestamp;

        public SearchEntry(String query, int resultCount) {
            this.query = query;
            this.resultCount = resultCount;
            this.timestamp = System.currentTimeMillis();
        }

        public String getQuery() {
            return query;
        }

        public int getResultCount() {
            return resultCount;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
