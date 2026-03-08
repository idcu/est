package ltd.idcu.est.ai.test.performance;

public class BenchmarkResult {
    
    private final String testName;
    private final long durationMs;
    private final long iterations;
    private final double avgDurationMs;
    private final double opsPerSecond;
    private final long minDurationMs;
    private final long maxDurationMs;
    private final double p50Ms;
    private final double p95Ms;
    private final double p99Ms;
    
    public BenchmarkResult(String testName, long durationMs, long iterations, 
                          long[] allDurations) {
        this.testName = testName;
        this.durationMs = durationMs;
        this.iterations = iterations;
        this.avgDurationMs = (double) durationMs / iterations;
        this.opsPerSecond = (double) iterations / (durationMs / 1000.0);
        
        if (allDurations.length > 0) {
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;
            long sum = 0;
            for (long d : allDurations) {
                if (d < min) min = d;
                if (d > max) max = d;
                sum += d;
            }
            this.minDurationMs = min;
            this.maxDurationMs = max;
            
            java.util.Arrays.sort(allDurations);
            this.p50Ms = allDurations[(int) (allDurations.length * 0.5)];
            this.p95Ms = allDurations[(int) (allDurations.length * 0.95)];
            this.p99Ms = allDurations[(int) (allDurations.length * 0.99)];
        } else {
            this.minDurationMs = 0;
            this.maxDurationMs = 0;
            this.p50Ms = 0;
            this.p95Ms = 0;
            this.p99Ms = 0;
        }
    }
    
    public String getTestName() {
        return testName;
    }
    
    public long getDurationMs() {
        return durationMs;
    }
    
    public long getIterations() {
        return iterations;
    }
    
    public double getAvgDurationMs() {
        return avgDurationMs;
    }
    
    public double getOpsPerSecond() {
        return opsPerSecond;
    }
    
    public long getMinDurationMs() {
        return minDurationMs;
    }
    
    public long getMaxDurationMs() {
        return maxDurationMs;
    }
    
    public double getP50Ms() {
        return p50Ms;
    }
    
    public double getP95Ms() {
        return p95Ms;
    }
    
    public double getP99Ms() {
        return p99Ms;
    }
    
    public void print() {
        System.out.println("\n=== " + testName + " ===");
        System.out.printf("总耗时: %.2f s%n", durationMs / 1000.0);
        System.out.printf("迭代次数: %d%n", iterations);
        System.out.printf("平均耗时: %.4f ms%n", avgDurationMs);
        System.out.printf("吞吐量: %.2f ops/s%n", opsPerSecond);
        System.out.printf("最小耗时: %d ms%n", minDurationMs);
        System.out.printf("最大耗时: %d ms%n", maxDurationMs);
        System.out.printf("P50: %.2f ms%n", p50Ms);
        System.out.printf("P95: %.2f ms%n", p95Ms);
        System.out.printf("P99: %.2f ms%n", p99Ms);
    }
    
    public String toCsv() {
        return String.format("%s,%.2f,%d,%.4f,%.2f,%d,%d,%.2f,%.2f,%.2f",
            testName, durationMs / 1000.0, iterations, avgDurationMs, opsPerSecond,
            minDurationMs, maxDurationMs, p50Ms, p95Ms, p99Ms);
    }
    
    public static String getCsvHeader() {
        return "TestName,TotalTime(s),Iterations,AvgTime(ms),Ops/s,Min(ms),Max(ms),P50(ms),P95(ms),P99(ms)";
    }
}
