package ltd.idcu.est.features.messaging.api;

import java.util.concurrent.atomic.AtomicLong;

public class MessageStats {
    
    private final AtomicLong sentCount;
    private final AtomicLong receivedCount;
    private final AtomicLong acknowledgedCount;
    private final AtomicLong failedCount;
    private final AtomicLong expiredCount;
    private final AtomicLong totalDeliveryTime;
    
    public MessageStats() {
        this.sentCount = new AtomicLong(0);
        this.receivedCount = new AtomicLong(0);
        this.acknowledgedCount = new AtomicLong(0);
        this.failedCount = new AtomicLong(0);
        this.expiredCount = new AtomicLong(0);
        this.totalDeliveryTime = new AtomicLong(0);
    }
    
    public long getSentCount() {
        return sentCount.get();
    }
    
    public void incrementSentCount() {
        sentCount.incrementAndGet();
    }
    
    public void addSentCount(long count) {
        sentCount.addAndGet(count);
    }
    
    public long getReceivedCount() {
        return receivedCount.get();
    }
    
    public void incrementReceivedCount() {
        receivedCount.incrementAndGet();
    }
    
    public void addReceivedCount(long count) {
        receivedCount.addAndGet(count);
    }
    
    public long getAcknowledgedCount() {
        return acknowledgedCount.get();
    }
    
    public void incrementAcknowledgedCount() {
        acknowledgedCount.incrementAndGet();
    }
    
    public void addAcknowledgedCount(long count) {
        acknowledgedCount.addAndGet(count);
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
    
    public long getExpiredCount() {
        return expiredCount.get();
    }
    
    public void incrementExpiredCount() {
        expiredCount.incrementAndGet();
    }
    
    public void addExpiredCount(long count) {
        expiredCount.addAndGet(count);
    }
    
    public long getTotalDeliveryTime() {
        return totalDeliveryTime.get();
    }
    
    public void addDeliveryTime(long time) {
        totalDeliveryTime.addAndGet(time);
    }
    
    public double getAverageDeliveryTime() {
        long received = receivedCount.get();
        if (received == 0) {
            return 0.0;
        }
        return (double) totalDeliveryTime.get() / received;
    }
    
    public double getSuccessRate() {
        long total = acknowledgedCount.get() + failedCount.get();
        if (total == 0) {
            return 1.0;
        }
        return (double) acknowledgedCount.get() / total;
    }
    
    public void reset() {
        sentCount.set(0);
        receivedCount.set(0);
        acknowledgedCount.set(0);
        failedCount.set(0);
        expiredCount.set(0);
        totalDeliveryTime.set(0);
    }
    
    public MessageStats snapshot() {
        MessageStats snapshot = new MessageStats();
        snapshot.sentCount.set(this.sentCount.get());
        snapshot.receivedCount.set(this.receivedCount.get());
        snapshot.acknowledgedCount.set(this.acknowledgedCount.get());
        snapshot.failedCount.set(this.failedCount.get());
        snapshot.expiredCount.set(this.expiredCount.get());
        snapshot.totalDeliveryTime.set(this.totalDeliveryTime.get());
        return snapshot;
    }
    
    @Override
    public String toString() {
        return "MessageStats{" +
                "sentCount=" + sentCount +
                ", receivedCount=" + receivedCount +
                ", acknowledgedCount=" + acknowledgedCount +
                ", failedCount=" + failedCount +
                ", expiredCount=" + expiredCount +
                ", successRate=" + String.format("%.2f%%", getSuccessRate() * 100) +
                ", averageDeliveryTime=" + String.format("%.2fms", getAverageDeliveryTime()) +
                '}';
    }
}
