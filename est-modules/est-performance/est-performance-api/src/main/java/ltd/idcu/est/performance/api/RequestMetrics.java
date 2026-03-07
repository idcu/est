package ltd.idcu.est.performance.api;

import java.util.Map;

public interface RequestMetrics {
    void recordRequest(String path, int statusCode, long responseTimeMs);

    long getTotalRequests();

    long getSuccessfulRequests();

    long getFailedRequests();

    double getSuccessRate();

    double getAverageResponseTime();

    long getMaxResponseTime();

    long getMinResponseTime();

    long getUptimeMs();

    double getRequestsPerSecond();

    Map<Integer, Long> getStatusCodes();

    Map<String, Long> getPathCounts();

    void reset();
}
