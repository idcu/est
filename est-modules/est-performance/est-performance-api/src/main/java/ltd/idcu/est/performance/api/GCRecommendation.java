package ltd.idcu.est.performance.api;

import java.util.List;

public class GCRecommendation {
    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    private final Priority priority;
    private final List<String> recommendations;
    private final GCMetrics metrics;

    public GCRecommendation(Priority priority, List<String> recommendations, GCMetrics metrics) {
        this.priority = priority;
        this.recommendations = recommendations;
        this.metrics = metrics;
    }

    public Priority getPriority() {
        return priority;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    public GCMetrics getMetrics() {
        return metrics;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== GC Recommendation (").append(priority).append(") ===\n");
        sb.append("\nCurrent Metrics:\n");
        sb.append("  ").append(metrics).append("\n");
        sb.append("\nRecommendations:\n");
        for (int i = 0; i < recommendations.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(recommendations.get(i)).append("\n");
        }
        return sb.toString();
    }
}
