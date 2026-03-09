package ltd.idcu.est.data.api;

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
    private long successfulBorrows;
    private long failedBorrows;
    private int statementCacheSize;

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

    public long getSuccessfulBorrows() {
        return successfulBorrows;
    }

    public void setSuccessfulBorrows(long successfulBorrows) {
        this.successfulBorrows = successfulBorrows;
    }

    public long getFailedBorrows() {
        return failedBorrows;
    }

    public void setFailedBorrows(long failedBorrows) {
        this.failedBorrows = failedBorrows;
    }

    public int getStatementCacheSize() {
        return statementCacheSize;
    }

    public void setStatementCacheSize(int statementCacheSize) {
        this.statementCacheSize = statementCacheSize;
    }
}
