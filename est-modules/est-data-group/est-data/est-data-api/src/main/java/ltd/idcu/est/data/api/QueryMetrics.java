package ltd.idcu.est.data.api;

public class QueryMetrics {

    private long totalQueries;
    private long totalTimeMs;
    private long slowQueries;

    public void recordQuery(long timeMs) {
        totalQueries++;
        totalTimeMs += timeMs;
        if (timeMs > 1000) {
            slowQueries++;
        }
    }

    public long getTotalQueries() {
        return totalQueries;
    }

    public long getTotalTimeMs() {
        return totalTimeMs;
    }

    public long getSlowQueries() {
        return slowQueries;
    }

    public double getAverageTimeMs() {
        return totalQueries == 0 ? 0 : (double) totalTimeMs / totalQueries;
    }

    public void reset() {
        totalQueries = 0;
        totalTimeMs = 0;
        slowQueries = 0;
    }
}
