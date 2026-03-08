package ltd.idcu.est.event.api;

import java.util.concurrent.atomic.AtomicLong;

public class EventStats {
    
    private final AtomicLong publishedCount;
    private final AtomicLong deliveredCount;
    private final AtomicLong failedCount;
    private final AtomicLong totalDeliveryTime;
    
    public EventStats() {
        this.publishedCount = new AtomicLong(0);
        this.deliveredCount = new AtomicLong(0);
        this.failedCount = new AtomicLong(0);
        this.totalDeliveryTime = new AtomicLong(0);
    }
    
    public long getPublishedCount() {
        return publishedCount.get();
    }
    
    public void incrementPublishedCount() {
        publishedCount.incrementAndGet();
    }
    
    public void addPublishedCount(long count) {
        publishedCount.addAndGet(count);
    }
    
    public long getDeliveredCount() {
        return deliveredCount.get();
    }
    
    public void incrementDeliveredCount() {
        deliveredCount.incrementAndGet();
    }
    
    public void addDeliveredCount(long count) {
        deliveredCount.addAndGet(count);
    }
    
    public long getFailedCount() {
        return failedCount.get();
    }
    
    public void incrementFailedCount() {
        failedCount.incrementAndGet();
    }
    
    public void addFailedCount(long count) {
        failedCount.addAndGet(count);
    }
    
    public long getTotalDeliveryTime() {
        return totalDeliveryTime.get();
    }
    
    public void addDeliveryTime(long time) {
        totalDeliveryTime.addAndGet(time);
    }
    
    public double getAverageDeliveryTime() {
        long delivered = deliveredCount.get();
        if (delivered == 0) {
            return 0.0;
        }
        return (double) totalDeliveryTime.get() / delivered;
    }
    
    public double getSuccessRate() {
        long total = deliveredCount.get() + failedCount.get();
        if (total == 0) {
            return 1.0;
        }
        return (double) deliveredCount.get() / total;
    }
    
    public void reset() {
        publishedCount.set(0);
        deliveredCount.set(0);
        failedCount.set(0);
        totalDeliveryTime.set(0);
    }
    
    public EventStats snapshot() {
        EventStats snapshot = new EventStats();
        snapshot.publishedCount.set(this.publishedCount.get());
        snapshot.deliveredCount.set(this.deliveredCount.get());
        snapshot.failedCount.set(this.failedCount.get());
        snapshot.totalDeliveryTime.set(this.totalDeliveryTime.get());
        return snapshot;
    }
    
    @Override
    public String toString() {
        return "EventStats{" +
                "publishedCount=" + publishedCount +
                ", deliveredCount=" + deliveredCount +
                ", failedCount=" + failedCount +
                ", successRate=" + String.format("%.2f%%", getSuccessRate() * 100) +
                ", averageDeliveryTime=" + String.format("%.2fms", getAverageDeliveryTime()) +
                '}';
    }
}
