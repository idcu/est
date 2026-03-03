package ltd.idcu.est.features.data.api;

public class ConnectionPoolStats {
    
    private int totalConnections;
    private int activeConnections;
    private int idleConnections;
    private long totalBorrowed;
    private long totalReturned;
    private long totalCreated;
    private long totalDestroyed;
    private long totalWaitTime;
    private long borrowCount;
    
    public ConnectionPoolStats() {
    }
    
    public int getTotalConnections() {
        return totalConnections;
    }
    
    public void setTotalConnections(int totalConnections) {
        this.totalConnections = totalConnections;
    }
    
    public int getActiveConnections() {
        return activeConnections;
    }
    
    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }
    
    public int getIdleConnections() {
        return idleConnections;
    }
    
    public void setIdleConnections(int idleConnections) {
        this.idleConnections = idleConnections;
    }
    
    public long getTotalBorrowed() {
        return totalBorrowed;
    }
    
    public void setTotalBorrowed(long totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }
    
    public long getTotalReturned() {
        return totalReturned;
    }
    
    public void setTotalReturned(long totalReturned) {
        this.totalReturned = totalReturned;
    }
    
    public long getTotalCreated() {
        return totalCreated;
    }
    
    public void setTotalCreated(long totalCreated) {
        this.totalCreated = totalCreated;
    }
    
    public long getTotalDestroyed() {
        return totalDestroyed;
    }
    
    public void setTotalDestroyed(long totalDestroyed) {
        this.totalDestroyed = totalDestroyed;
    }
    
    public long getTotalWaitTime() {
        return totalWaitTime;
    }
    
    public void setTotalWaitTime(long totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }
    
    public long getBorrowCount() {
        return borrowCount;
    }
    
    public void setBorrowCount(long borrowCount) {
        this.borrowCount = borrowCount;
    }
    
    public double getAverageWaitTime() {
        if (borrowCount == 0) {
            return 0.0;
        }
        return (double) totalWaitTime / borrowCount;
    }
    
    public double getUtilizationRate() {
        if (totalConnections == 0) {
            return 0.0;
        }
        return (double) activeConnections / totalConnections;
    }
    
    @Override
    public String toString() {
        return "ConnectionPoolStats{" +
                "totalConnections=" + totalConnections +
                ", activeConnections=" + activeConnections +
                ", idleConnections=" + idleConnections +
                ", utilizationRate=" + String.format("%.2f%%", getUtilizationRate() * 100) +
                ", averageWaitTime=" + getAverageWaitTime() + "ms" +
                '}';
    }
}
