package ltd.idcu.est.cache.api;

public class CacheStats {
    
    private long hitCount;
    private long missCount;
    private long putCount;
    private long removeCount;
    private long evictionCount;
    private long totalLoadTime;
    private long loadCount;
    
    public CacheStats() {
        this.hitCount = 0;
        this.missCount = 0;
        this.putCount = 0;
        this.removeCount = 0;
        this.evictionCount = 0;
        this.totalLoadTime = 0;
        this.loadCount = 0;
    }
    
    public long getHitCount() {
        return hitCount;
    }
    
    public void incrementHitCount() {
        this.hitCount++;
    }
    
    public void addHitCount(long count) {
        this.hitCount += count;
    }
    
    public long getMissCount() {
        return missCount;
    }
    
    public void incrementMissCount() {
        this.missCount++;
    }
    
    public void addMissCount(long count) {
        this.missCount += count;
    }
    
    public long getPutCount() {
        return putCount;
    }
    
    public void incrementPutCount() {
        this.putCount++;
    }
    
    public void addPutCount(long count) {
        this.putCount += count;
    }
    
    public long getRemoveCount() {
        return removeCount;
    }
    
    public void incrementRemoveCount() {
        this.removeCount++;
    }
    
    public void addRemoveCount(long count) {
        this.removeCount += count;
    }
    
    public long getEvictionCount() {
        return evictionCount;
    }
    
    public void incrementEvictionCount() {
        this.evictionCount++;
    }
    
    public void addEvictionCount(long count) {
        this.evictionCount += count;
    }
    
    public long getTotalLoadTime() {
        return totalLoadTime;
    }
    
    public void addLoadTime(long time) {
        this.totalLoadTime += time;
        this.loadCount++;
    }
    
    public long getLoadCount() {
        return loadCount;
    }
    
    public long getRequestCount() {
        return hitCount + missCount;
    }
    
    public double getHitRate() {
        long requestCount = getRequestCount();
        if (requestCount == 0) {
            return 0.0;
        }
        return (double) hitCount / requestCount;
    }
    
    public double getMissRate() {
        long requestCount = getRequestCount();
        if (requestCount == 0) {
            return 0.0;
        }
        return (double) missCount / requestCount;
    }
    
    public double getAverageLoadTime() {
        if (loadCount == 0) {
            return 0.0;
        }
        return (double) totalLoadTime / loadCount;
    }
    
    public void reset() {
        this.hitCount = 0;
        this.missCount = 0;
        this.putCount = 0;
        this.removeCount = 0;
        this.evictionCount = 0;
        this.totalLoadTime = 0;
        this.loadCount = 0;
    }
    
    public CacheStats snapshot() {
        CacheStats snapshot = new CacheStats();
        snapshot.hitCount = this.hitCount;
        snapshot.missCount = this.missCount;
        snapshot.putCount = this.putCount;
        snapshot.removeCount = this.removeCount;
        snapshot.evictionCount = this.evictionCount;
        snapshot.totalLoadTime = this.totalLoadTime;
        snapshot.loadCount = this.loadCount;
        return snapshot;
    }
    
    @Override
    public String toString() {
        return "CacheStats{" +
                "hitCount=" + hitCount +
                ", missCount=" + missCount +
                ", putCount=" + putCount +
                ", removeCount=" + removeCount +
                ", evictionCount=" + evictionCount +
                ", hitRate=" + String.format("%.2f%%", getHitRate() * 100) +
                ", averageLoadTime=" + getAverageLoadTime() + "ms" +
                '}';
    }
}
