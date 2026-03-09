package ltd.idcu.est.codecli.performance;

import ltd.idcu.est.codecli.search.FileIndex;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;
import ltd.idcu.est.test.annotation.AfterEach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ltd.idcu.est.test.Assertions.*;

public class PerformanceBenchmarkTest {

    private PerformanceMonitor monitor;
    private FileIndex fileIndex;
    private Random random;

    @BeforeEach
    void beforeEach() {
        monitor = new PerformanceMonitor();
        fileIndex = new FileIndex();
        random = new Random(42);
    }

    @AfterEach
    void afterEach() {
        System.out.println(monitor.generateReport());
    }

    @Test
    void benchmarkIndexing_100Files() {
        benchmarkIndexing(100);
    }

    @Test
    void benchmarkIndexing_1000Files() {
        benchmarkIndexing(1000);
    }

    @Test
    void benchmarkIndexing_5000Files() {
        benchmarkIndexing(5000);
    }

    @Test
    void benchmarkSearch_100Files() {
        benchmarkSearch(100);
    }

    @Test
    void benchmarkSearch_1000Files() {
        benchmarkSearch(1000);
    }

    @Test
    void benchmarkSearch_5000Files() {
        benchmarkSearch(5000);
    }

    @Test
    void benchmarkMixedOperations() {
        int fileCount = 1000;
        
        monitor.measure("benchmark.mixed." + fileCount + ".setup", () -> {
            for (int i = 0; i < fileCount; i++) {
                String content = generateRandomContent(200);
                fileIndex.indexFile("/file" + i + ".java", content);
            }
        });

        for (int round = 0; round < 100; round++) {
            String searchTerm = "keyword" + random.nextInt(100);
            monitor.measure("benchmark.mixed.search", () -> {
                fileIndex.search(searchTerm);
            });
        }

        MetricSnapshot snapshot = monitor.getSnapshot("benchmark.mixed.search");
        assertNotNull(snapshot);
        assertTrue(snapshot.count() >= 100);
        assertTrue(snapshot.avgTimeMs() < 100);
    }

    @Test
    void benchmarkMemoryUsage() {
        int fileCount = 1000;
        List<String> contents = new ArrayList<>();
        
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        monitor.measure("benchmark.memory.indexing", () -> {
            for (int i = 0; i < fileCount; i++) {
                String content = generateRandomContent(500);
                contents.add(content);
                fileIndex.indexFile("/file" + i + ".java", content);
            }
        });

        runtime.gc();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        System.out.println("Memory usage for " + fileCount + " files: " + (memoryUsed / 1024 / 1024) + " MB");
        assertTrue(memoryUsed > 0);
    }

    @Test
    void benchmarkConcurrentSearches() {
        int fileCount = 1000;
        
        for (int i = 0; i < fileCount; i++) {
            String content = generateRandomContent(200);
            fileIndex.indexFile("/file" + i + ".java", content);
        }

        List<Thread> threads = new ArrayList<>();
        for (int t = 0; t < 10; t++) {
            final int threadId = t;
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 50; i++) {
                    String searchTerm = "keyword" + random.nextInt(100);
                    monitor.measure("benchmark.concurrent.search", () -> {
                        fileIndex.search(searchTerm);
                    });
                }
            });
            threads.add(thread);
        }

        monitor.measure("benchmark.concurrent.total", () -> {
            threads.forEach(Thread::start);
            threads.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        });

        MetricSnapshot snapshot = monitor.getSnapshot("benchmark.concurrent.search");
        assertNotNull(snapshot);
        assertTrue(snapshot.count() >= 500);
    }

    @Test
    void benchmarkIndexIncrementalUpdate() {
        int initialFiles = 500;
        int updateFiles = 100;
        
        monitor.measure("benchmark.incremental.initial", () -> {
            for (int i = 0; i < initialFiles; i++) {
                String content = generateRandomContent(200);
                fileIndex.indexFile("/file" + i + ".java", content);
            }
        });

        monitor.measure("benchmark.incremental.update", () -> {
            for (int i = 0; i < updateFiles; i++) {
                String content = generateRandomContent(200);
                fileIndex.indexFile("/file" + i + ".java", content);
            }
        });

        MetricSnapshot initial = monitor.getSnapshot("benchmark.incremental.initial");
        MetricSnapshot update = monitor.getSnapshot("benchmark.incremental.update");
        
        assertNotNull(initial);
        assertNotNull(update);
        assertTrue(update.avgTimeMs() < initial.avgTimeMs() * 2);
    }

    @Test
    void benchmarkSearchResultScoring() {
        int fileCount = 1000;
        
        for (int i = 0; i < fileCount; i++) {
            String content = generateContentWithKeywordDensity(i);
            fileIndex.indexFile("/file" + i + ".java", content);
        }

        monitor.measure("benchmark.scoring.search", () -> {
            List<FileIndex.SearchResult> results = fileIndex.search("targetkeyword");
            assertFalse(results.isEmpty());
            assertTrue(results.get(0).getScore() >= results.get(results.size() - 1).getScore());
        });

        MetricSnapshot snapshot = monitor.getSnapshot("benchmark.scoring.search");
        assertNotNull(snapshot);
        assertTrue(snapshot.avgTimeMs() < 50);
    }

    private void benchmarkIndexing(int fileCount) {
        List<String> contents = new ArrayList<>();
        for (int i = 0; i < fileCount; i++) {
            contents.add(generateRandomContent(200));
        }

        monitor.measure("benchmark.index." + fileCount + ".files", () -> {
            for (int i = 0; i < fileCount; i++) {
                fileIndex.indexFile("/file" + i + ".java", contents.get(i));
            }
        });

        MetricSnapshot snapshot = monitor.getSnapshot("benchmark.index." + fileCount + ".files");
        assertNotNull(snapshot);
        
        double avgTimePerFile = snapshot.avgTimeMs() / fileCount * 1000;
        System.out.println("Average time per file (" + fileCount + " files): " + avgTimePerFile + " µs");
        
        if (fileCount <= 1000) {
            assertTrue(snapshot.totalTimeMs() < 30000);
        }
    }

    private void benchmarkSearch(int fileCount) {
        for (int i = 0; i < fileCount; i++) {
            String content = generateRandomContent(200);
            fileIndex.indexFile("/file" + i + ".java", content);
        }

        for (int round = 0; round < 100; round++) {
            String searchTerm = "keyword" + random.nextInt(100);
            monitor.measure("benchmark.search." + fileCount + ".files", () -> {
                fileIndex.search(searchTerm);
            });
        }

        MetricSnapshot snapshot = monitor.getSnapshot("benchmark.search." + fileCount + ".files");
        assertNotNull(snapshot);
        assertTrue(snapshot.count() >= 100);
        
        if (fileCount <= 1000) {
            assertTrue(snapshot.avgTimeMs() < 1000);
        }
        
        System.out.println("Average search time (" + fileCount + " files): " + snapshot.avgTimeMs() + " ms");
    }

    private String generateRandomContent(int wordCount) {
        StringBuilder sb = new StringBuilder();
        String[] words = {"class", "method", "variable", "function", "test", "public", "private", 
                          "return", "if", "else", "for", "while", "switch", "case", "break",
                          "keyword0", "keyword1", "keyword2", "keyword3", "keyword4",
                          "keyword5", "keyword6", "keyword7", "keyword8", "keyword9"};
        
        for (int i = 0; i < wordCount; i++) {
            sb.append(words[random.nextInt(words.length)]).append(" ");
        }
        
        return sb.toString();
    }

    private String generateContentWithKeywordDensity(int densityLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < densityLevel % 20; i++) {
            sb.append("targetkeyword ");
        }
        for (int i = 0; i < 50; i++) {
            sb.append("otherword ");
        }
        return sb.toString();
    }
}
