package ltd.idcu.est.codecli.search;

import ltd.idcu.est.test.api.Test;
import ltd.idcu.est.test.api.BeforeEach;

import java.util.List;
import java.util.Set;

import static ltd.idcu.est.test.Assertions.*;

public class FileIndexTest {

    private FileIndex fileIndex;

    @BeforeEach
    void beforeEach() {
        fileIndex = new FileIndex();
    }

    @Test
    void testIndexAndSearch() {
        fileIndex.indexFile("/test1.java", "public class TestClass { public void testMethod() { } }");
        fileIndex.indexFile("/test2.java", "public class AnotherClass { public void anotherMethod() { } }");

        List<FileIndex.SearchResult> results = fileIndex.search("TestClass");
        assertEquals(1, results.size());
        assertEquals("/test1.java", results.get(0).getDocument().getFilePath());
    }

    @Test
    void testMultipleResults() {
        fileIndex.indexFile("/file1.java", "This is a test file with Java code");
        fileIndex.indexFile("/file2.java", "Another test file with different content");
        fileIndex.indexFile("/file3.txt", "Text file without code");

        List<FileIndex.SearchResult> results = fileIndex.search("test");
        assertEquals(2, results.size());
    }

    @Test
    void testRemoveFile() {
        fileIndex.indexFile("/test.java", "test content");
        fileIndex.indexFile("/other.java", "other content");

        Set<String> files = fileIndex.getAllIndexedFiles();
        assertEquals(2, files.size());

        fileIndex.removeFile("/test.java");

        files = fileIndex.getAllIndexedFiles();
        assertEquals(1, files.size());
        assertFalse(files.contains("/test.java"));
    }

    @Test
    void testClear() {
        fileIndex.indexFile("/file1.java", "content1");
        fileIndex.indexFile("/file2.java", "content2");

        assertEquals(2, fileIndex.getAllIndexedFiles().size());

        fileIndex.clear();

        assertTrue(fileIndex.getAllIndexedFiles().isEmpty());
    }

    @Test
    void testSearchNoResults() {
        fileIndex.indexFile("/test.java", "public class Test { }");

        List<FileIndex.SearchResult> results = fileIndex.search("NonexistentKeyword");
        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchResultScoring() {
        fileIndex.indexFile("/high.java", "test test test test test");
        fileIndex.indexFile("/low.java", "test");

        List<FileIndex.SearchResult> results = fileIndex.search("test");
        assertEquals(2, results.size());
        assertTrue(results.get(0).getScore() > results.get(1).getScore());
    }

    @Test
    void testSnippetGeneration() {
        String content = "This is a long piece of text that contains the keyword we are looking for right here in the middle of the sentence.";
        fileIndex.indexFile("/snippet.java", content);

        List<FileIndex.SearchResult> results = fileIndex.search("keyword");
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getSnippet());
        assertTrue(results.get(0).getSnippet().contains("keyword"));
    }

    @Test
    void testStopWordsAreIgnored() {
        fileIndex.indexFile("/stopwords.java", "the a an and or but is are was were");

        List<FileIndex.SearchResult> results = fileIndex.search("the");
        assertTrue(results.isEmpty());
    }

    @Test
    void testShortTokensAreIgnored() {
        fileIndex.indexFile("/short.java", "a b c 1 2 3");

        List<FileIndex.SearchResult> results = fileIndex.search("a");
        assertTrue(results.isEmpty());
    }

    @Test
    void testCaseInsensitiveSearch() {
        fileIndex.indexFile("/case.java", "HelloWorld helloWorld HELLOWORLD");

        List<FileIndex.SearchResult> results = fileIndex.search("helloworld");
        assertEquals(1, results.size());
    }
}
