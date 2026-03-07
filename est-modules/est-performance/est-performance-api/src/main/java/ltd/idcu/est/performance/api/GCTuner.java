package ltd.idcu.est.performance.api;

public interface GCTuner {
    GCMetrics collectMetrics();

    GCRecommendation getRecommendation(GCMetrics metrics);

    String getJVMInfo();
}
