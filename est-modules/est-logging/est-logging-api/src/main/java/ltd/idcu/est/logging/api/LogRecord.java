package ltd.idcu.est.logging.api;

import java.time.Instant;

public class LogRecord {
    
    private final String loggerName;
    private final LogLevel level;
    private final String message;
    private final Throwable throwable;
    private final Instant timestamp;
    private final String threadName;
    private final long threadId;
    
    public LogRecord(String loggerName, LogLevel level, String message) {
        this(loggerName, level, message, null);
    }
    
    public LogRecord(String loggerName, LogLevel level, String message, Throwable throwable) {
        this.loggerName = loggerName;
        this.level = level;
        this.message = message;
        this.throwable = throwable;
        this.timestamp = Instant.now();
        Thread currentThread = Thread.currentThread();
        this.threadName = currentThread.getName();
        this.threadId = currentThread.threadId();
    }
    
    public String getLoggerName() {
        return loggerName;
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public String getThreadName() {
        return threadName;
    }
    
    public long getThreadId() {
        return threadId;
    }
    
    public static LogRecord of(String loggerName, LogLevel level, String message) {
        return new LogRecord(loggerName, level, message);
    }
    
    public static LogRecord of(String loggerName, LogLevel level, String message, Throwable throwable) {
        return new LogRecord(loggerName, level, message, throwable);
    }
}
