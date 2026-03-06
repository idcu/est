package ltd.idcu.est.features.performance.api;

public class GCMetrics {
    private final long youngGcCount;
    private final long youngGcTime;
    private final long oldGcCount;
    private final long oldGcTime;
    private final long totalGcCount;
    private final long totalGcTime;
    private final long heapUsed;
    private final long heapMax;
    private final long heapCommitted;
    private final long nonHeapUsed;
    private final long nonHeapMax;
    private final long nonHeapCommitted;
    private final double gcOverheadPercent;

    public GCMetrics(long youngGcCount, long youngGcTime, long oldGcCount, long oldGcTime,
                      long totalGcCount, long totalGcTime, long heapUsed, long heapMax,
                      long heapCommitted, long nonHeapUsed, long nonHeapMax,
                      long nonHeapCommitted, double gcOverheadPercent) {
        this.youngGcCount = youngGcCount;
        this.youngGcTime = youngGcTime;
        this.oldGcCount = oldGcCount;
        this.oldGcTime = oldGcTime;
        this.totalGcCount = totalGcCount;
        this.totalGcTime = totalGcTime;
        this.heapUsed = heapUsed;
        this.heapMax = heapMax;
        this.heapCommitted = heapCommitted;
        this.nonHeapUsed = nonHeapUsed;
        this.nonHeapMax = nonHeapMax;
        this.nonHeapCommitted = nonHeapCommitted;
        this.gcOverheadPercent = gcOverheadPercent;
    }

    public long getYoungGcCount() {
        return youngGcCount;
    }

    public long getYoungGcTime() {
        return youngGcTime;
    }

    public long getOldGcCount() {
        return oldGcCount;
    }

    public long getOldGcTime() {
        return oldGcTime;
    }

    public long getTotalGcCount() {
        return totalGcCount;
    }

    public long getTotalGcTime() {
        return totalGcTime;
    }

    public long getHeapUsed() {
        return heapUsed;
    }

    public long getHeapMax() {
        return heapMax;
    }

    public long getHeapCommitted() {
        return heapCommitted;
    }

    public long getNonHeapUsed() {
        return nonHeapUsed;
    }

    public long getNonHeapMax() {
        return nonHeapMax;
    }

    public long getNonHeapCommitted() {
        return nonHeapCommitted;
    }

    public double getGcOverheadPercent() {
        return gcOverheadPercent;
    }

    public double getHeapUsagePercent() {
        return heapMax > 0 ? (double) heapUsed / heapMax * 100 : 0;
    }

    @Override
    public String toString() {
        return "GCMetrics{" +
                "youngGcCount=" + youngGcCount +
                ", youngGcTime=" + youngGcTime + "ms" +
                ", oldGcCount=" + oldGcCount +
                ", oldGcTime=" + oldGcTime + "ms" +
                ", totalGcCount=" + totalGcCount +
                ", totalGcTime=" + totalGcTime + "ms" +
                ", heapUsed=" + formatBytes(heapUsed) +
                ", heapMax=" + formatBytes(heapMax) +
                ", heapUsage=" + String.format("%.1f", getHeapUsagePercent()) + "%" +
                ", gcOverhead=" + String.format("%.2f", gcOverheadPercent) + "%" +
                '}';
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
