package ltd.idcu.est.logging.api;

public interface Logger {
    
    String getName();
    
    boolean isTraceEnabled();
    
    boolean isDebugEnabled();
    
    boolean isInfoEnabled();
    
    boolean isWarnEnabled();
    
    boolean isErrorEnabled();
    
    void trace(String message);
    
    void trace(String format, Object... args);
    
    void trace(String message, Throwable throwable);
    
    void debug(String message);
    
    void debug(String format, Object... args);
    
    void debug(String message, Throwable throwable);
    
    void info(String message);
    
    void info(String format, Object... args);
    
    void info(String message, Throwable throwable);
    
    void warn(String message);
    
    void warn(String format, Object... args);
    
    void warn(String message, Throwable throwable);
    
    void error(String message);
    
    void error(String format, Object... args);
    
    void error(String message, Throwable throwable);
    
    void log(LogLevel level, String message);
    
    void log(LogLevel level, String format, Object... args);
    
    void log(LogLevel level, String message, Throwable throwable);
}
