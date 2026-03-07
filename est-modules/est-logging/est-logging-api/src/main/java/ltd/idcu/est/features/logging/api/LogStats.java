package ltd.idcu.est.features.logging.api;

import java.util.concurrent.atomic.AtomicLong;

public class LogStats {
    
    private final AtomicLong traceCount;
    private final AtomicLong debugCount;
    private final AtomicLong infoCount;
    private final AtomicLong warnCount;
    private final AtomicLong errorCount;
    private final AtomicLong droppedCount;
    
    public LogStats() {
        this.traceCount = new AtomicLong(0);
        this.debugCount = new AtomicLong(0);
        this.infoCount = new AtomicLong(0);
        this.warnCount = new AtomicLong(0);
        this.errorCount = new AtomicLong(0);
        this.droppedCount = new AtomicLong(0);
    }
    
    public long getTraceCount() {
        return traceCount.get();
    }
    
    public long getDebugCount() {
        return debugCount.get();
    }
    
    public long getInfoCount() {
        return infoCount.get();
    }
    
    public long getWarnCount() {
        return warnCount.get();
    }
    
    public long getErrorCount() {
        return errorCount.get();
    }
    
    public long getDroppedCount() {
        return droppedCount.get();
    }
    
    public long getTotalCount() {
        return traceCount.get() + debugCount.get() + infoCount.get() + 
               warnCount.get() + errorCount.get();
    }
    
    public void increment(LogLevel level) {
        switch (level) {
            case TRACE -> traceCount.incrementAndGet();
            case DEBUG -> debugCount.incrementAndGet();
            case INFO -> infoCount.incrementAndGet();
            case WARN -> warnCount.incrementAndGet();
            case ERROR -> errorCount.incrementAndGet();
        }
    }
    
    public void incrementDropped() {
        droppedCount.incrementAndGet();
    }
    
    public void add(LogStats other) {
        traceCount.addAndGet(other.getTraceCount());
        debugCount.addAndGet(other.getDebugCount());
        infoCount.addAndGet(other.getInfoCount());
        warnCount.addAndGet(other.getWarnCount());
        errorCount.addAndGet(other.getErrorCount());
        droppedCount.addAndGet(other.getDroppedCount());
    }
    
    public void reset() {
        traceCount.set(0);
        debugCount.set(0);
        infoCount.set(0);
        warnCount.set(0);
        errorCount.set(0);
        droppedCount.set(0);
    }
    
    public LogStats snapshot() {
        LogStats snapshot = new LogStats();
        snapshot.traceCount.set(this.traceCount.get());
        snapshot.debugCount.set(this.debugCount.get());
        snapshot.infoCount.set(this.infoCount.get());
        snapshot.warnCount.set(this.warnCount.get());
        snapshot.errorCount.set(this.errorCount.get());
        snapshot.droppedCount.set(this.droppedCount.get());
        return snapshot;
    }
    
    @Override
    public String toString() {
        return "LogStats{" +
                "total=" + getTotalCount() +
                ", trace=" + traceCount +
                ", debug=" + debugCount +
                ", info=" + infoCount +
                ", warn=" + warnCount +
                ", error=" + errorCount +
                ", dropped=" + droppedCount +
                '}';
    }
}
