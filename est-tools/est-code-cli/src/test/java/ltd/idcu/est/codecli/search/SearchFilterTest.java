package ltd.idcu.est.codecli.search;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import static ltd.idcu.est.test.Assertions.*;

public class SearchFilterTest {

    private SearchFilter filter;

    @BeforeEach
    void beforeEach() {
        filter = new SearchFilter();
    }

    @Test
    void testDefaultValues() {
        assertFalse(filter.isCaseSensitive());
        assertFalse(filter.isFuzzySearch());
        assertEquals(2, filter.getFuzzyDistance());
        assertNull(filter.getMaxResults());
    }

    @Test
    void testIncludeExtension() {
        filter.addIncludeExtension("java");

        assertTrue(filter.matches("/src/Main.java", 10));
        assertFalse(filter.matches("/src/Main.py", 10));
        assertFalse(filter.matches("/src/Main.js", 10));
    }

    @Test
    void testIncludeExtensionWithDot() {
        filter.addIncludeExtension(".java");

        assertTrue(filter.matches("/src/Main.java", 10));
        assertFalse(filter.matches("/src/Main.py", 10));
    }

    @Test
    void testExcludeExtension() {
        filter.addExcludeExtension("class");

        assertTrue(filter.matches("/src/Main.java", 10));
        assertFalse(filter.matches("/src/Main.class", 10));
    }

    @Test
    void testMultipleIncludeExtensions() {
        filter.addIncludeExtension("java")
              .addIncludeExtension("py");

        assertTrue(filter.matches("/src/Main.java", 10));
        assertTrue(filter.matches("/src/Main.py", 10));
        assertFalse(filter.matches("/src/Main.js", 10));
    }

    @Test
    void testIncludePath() {
        filter.addIncludePath("src/main");

        assertTrue(filter.matches("/src/main/java/Main.java", 10));
        assertFalse(filter.matches("/src/test/java/MainTest.java", 10));
    }

    @Test
    void testExcludePath() {
        filter.addExcludePath("test");

        assertTrue(filter.matches("/src/main/java/Main.java", 10));
        assertFalse(filter.matches("/src/test/java/MainTest.java", 10));
    }

    @Test
    void testMinScore() {
        filter.setMinScore(5);

        assertTrue(filter.matches("/src/Main.java", 10));
        assertTrue(filter.matches("/src/Main.java", 5));
        assertFalse(filter.matches("/src/Main.java", 4));
    }

    @Test
    void testMaxResults() {
        filter.setMaxResults(10);
        assertEquals(Integer.valueOf(10), filter.getMaxResults());
    }

    @Test
    void testCaseSensitive() {
        filter.setCaseSensitive(true);
        assertTrue(filter.isCaseSensitive());

        filter.setCaseSensitive(false);
        assertFalse(filter.isCaseSensitive());
    }

    @Test
    void testFuzzySearch() {
        filter.setFuzzySearch(true);
        assertTrue(filter.isFuzzySearch());

        filter.setFuzzyDistance(3);
        assertEquals(3, filter.getFuzzyDistance());
    }

    @Test
    void testCombinedFilters() {
        filter.addIncludeExtension("java")
              .addExcludePath("test")
              .setMinScore(5);

        assertTrue(filter.matches("/src/main/java/Main.java", 10));
        assertFalse(filter.matches("/src/test/java/MainTest.java", 10));
        assertFalse(filter.matches("/src/main/java/Main.java", 3));
        assertFalse(filter.matches("/src/main/Main.py", 10));
    }

    @Test
    void testJavaFilesOnly() {
        SearchFilter javaFilter = SearchFilter.javaFilesOnly();

        assertTrue(javaFilter.matches("/src/Main.java", 10));
        assertFalse(javaFilter.matches("/src/Main.py", 10));
    }

    @Test
    void testExcludeTestFiles() {
        SearchFilter noTestFilter = SearchFilter.excludeTestFiles();

        assertTrue(noTestFilter.matches("/src/main/Main.java", 10));
        assertFalse(noTestFilter.matches("/src/test/MainTest.java", 10));
    }

    @Test
    void testSourceFilesOnly() {
        SearchFilter sourceFilter = SearchFilter.sourceFilesOnly();

        assertTrue(sourceFilter.matches("/src/main/Main.java", 10));
        assertTrue(sourceFilter.matches("/src/main/app.js", 10));
        assertTrue(sourceFilter.matches("/src/main/script.ts", 10));
        assertTrue(sourceFilter.matches("/src/main/main.py", 10));
        assertFalse(sourceFilter.matches("/src/test/MainTest.java", 10));
        assertFalse(sourceFilter.matches("/node_modules/package.json", 10));
    }

    @Test
    void testPathMatchingCaseInsensitive() {
        filter.addIncludePath("SOURCE");
        filter.addExcludePath("TEST");

        assertTrue(filter.matches("/source/main/Main.java", 10));
        assertFalse(filter.matches("/test/Main.java", 10));
    }

    @Test
    void testExtensionMatchingCaseInsensitive() {
        filter.addIncludeExtension("JAVA");
        filter.addExcludeExtension("CLASS");

        assertTrue(filter.matches("/src/Main.JAVA", 10));
        assertFalse(filter.matches("/src/Main.CLASS", 10));
    }

    @Test
    void testEmptyIncludeExtensions() {
        assertTrue(filter.matches("/src/any.file", 10));
    }

    @Test
    void testEmptyExcludeExtensions() {
        assertTrue(filter.matches("/src/any.file", 10));
    }

    @Test
    void testEmptyIncludePaths() {
        assertTrue(filter.matches("/src/any.file", 10));
    }

    @Test
    void testEmptyExcludePaths() {
        assertTrue(filter.matches("/src/any.file", 10));
    }
}
