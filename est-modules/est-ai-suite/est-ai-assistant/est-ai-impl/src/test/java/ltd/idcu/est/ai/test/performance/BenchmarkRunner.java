package ltd.idcu.est.ai.test.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BenchmarkRunner {
    
    private final List<BenchmarkResult> results = new ArrayList<>();
    private int warmupIterations = 10;
    private int measurementIterations = 100;
    
    public BenchmarkRunner() {
    }
    
    public BenchmarkRunner(int warmupIterations, int measurementIterations) {
        this.warmupIterations = warmupIterations;
        this.measurementIterations = measurementIterations;
    }
    
    public void setWarmupIterations(int iterations) {
        this.warmupIterations = iterations;
    }
    
    public void setMeasurementIterations(int iterations) {
        this.measurementIterations = iterations;
    }
    
    public BenchmarkResult run(String testName, Runnable task) {
        return run(testName, () -> {
            task.run();
            return null;
        });
    }
    
    public <T> BenchmarkResult run(String testName, Supplier<T> task) {
        System.out.println("Running benchmark: " + testName);
        
        System.out.println("  Warmup (" + warmupIterations + " iterations)...");
        for (int i = 0; i < warmupIterations; i++) {
            task.get();
        }
        
        System.out.println("  Measurement (" + measurementIterations + " iterations)...");
        long[] durations = new long[measurementIterations];
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < measurementIterations; i++) {
            long iterStart = System.nanoTime();
            task.get();
            long iterEnd = System.nanoTime();
            durations[i] = (iterEnd - iterStart) / 1_000_000;
        }
        
        long endTime = System.currentTimeMillis();
        long totalDuration = endTime - startTime;
        
        BenchmarkResult result = new BenchmarkResult(testName, totalDuration, measurementIterations, durations);
        results.add(result);
        
        result.print();
        return result;
    }
    
    public List<BenchmarkResult> getResults() {
        return new ArrayList<>(results);
    }
    
    public void printSummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("BENCHMARK SUMMARY");
        System.out.println("=".repeat(80));
        
        System.out.println("\n" + BenchmarkResult.getCsvHeader());
        for (BenchmarkResult result : results) {
            System.out.println(result.toCsv());
        }
        
        System.out.println("\n" + "=".repeat(80));
    }
    
    public void clearResults() {
        results.clear();
    }
}
