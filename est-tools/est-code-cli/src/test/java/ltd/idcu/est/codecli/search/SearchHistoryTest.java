package ltd.idcu.est.codecli.search;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class SearchHistoryTest {

    private SearchHistory searchHistory;

    @BeforeEach
    void beforeEach() {
        searchHistory = new SearchHistory();
    }

    @Test
    void testAddAndGetAll() {
        searchHistory.add("test query 1", 5);
        searchHistory.add("test query 2", 10);

        List<SearchHistory.SearchEntry> entries = searchHistory.getAll();
        assertEquals(2, entries.size());
        assertEquals("test query 2", entries.get(0).getQuery());
        assertEquals("test query 1", entries.get(1).getQuery());
    }

    @Test
    void testGetRecentQueries() {
        searchHistory.add("query 1", 1);
        searchHistory.add("query 2", 2);
        searchHistory.add("query 3", 3);
        searchHistory.add("query 4", 4);

        List<String> recent = searchHistory.getRecentQueries(2);
        assertEquals(2, recent.size());
        assertEquals("query 4", recent.get(0));
        assertEquals("query 3", recent.get(1));
    }

    @Test
    void testGetRecentQueriesLimitExceeded() {
        searchHistory.add("query 1", 1);
        searchHistory.add("query 2", 2);

        List<String> recent = searchHistory.getRecentQueries(10);
        assertEquals(2, recent.size());
    }

    @Test
    void testGetLast() {
        assertNull(searchHistory.getLast());

        searchHistory.add("query 1", 5);
        SearchHistory.SearchEntry last = searchHistory.getLast();
        assertNotNull(last);
        assertEquals("query 1", last.getQuery());
        assertEquals(5, last.getResultCount());
    }

    @Test
    void testClear() {
        searchHistory.add("query 1", 1);
        searchHistory.add("query 2", 2);

        assertEquals(2, searchHistory.size());
        assertFalse(searchHistory.isEmpty());

        searchHistory.clear();

        assertEquals(0, searchHistory.size());
        assertTrue(searchHistory.isEmpty());
    }

    @Test
    void testSizeAndIsEmpty() {
        assertTrue(searchHistory.isEmpty());
        assertEquals(0, searchHistory.size());

        searchHistory.add("query", 1);

        assertFalse(searchHistory.isEmpty());
        assertEquals(1, searchHistory.size());
    }

    @Test
    void testFindSimilar() {
        searchHistory.add("java class", 5);
        searchHistory.add("python function", 3);
        searchHistory.add("javascript class", 8);
        searchHistory.add("java method", 2);

        List<SearchHistory.SearchEntry> similar = searchHistory.findSimilar("java");
        assertEquals(2, similar.size());

        similar = searchHistory.findSimilar("class");
        assertEquals(2, similar.size());
    }

    @Test
    void testMaxSizeLimit() {
        SearchHistory history = new SearchHistory(3);
        history.add("query 1", 1);
        history.add("query 2", 2);
        history.add("query 3", 3);
        history.add("query 4", 4);

        assertEquals(3, history.size());
        List<String> recent = history.getRecentQueries(3);
        assertEquals("query 4", recent.get(0));
        assertEquals("query 3", recent.get(1));
        assertEquals("query 2", recent.get(2));
    }

    @Test
    void testSearchEntryProperties() {
        long before = System.currentTimeMillis();
        searchHistory.add("test query", 10);
        long after = System.currentTimeMillis();

        SearchHistory.SearchEntry entry = searchHistory.getLast();
        assertEquals("test query", entry.getQuery());
        assertEquals(10, entry.getResultCount());
        assertTrue(entry.getTimestamp() >= before && entry.getTimestamp() <= after);
    }
}
