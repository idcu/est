package ltd.idcu.est.observability.api;

import java.util.Map;

public interface LogsExporter {
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void log(LogLevel level, String message);
    
    void log(LogLevel level, String message, Map<String, Object> context);
    
    void log(LogLevel level, String message, Throwable throwable);
    
    void log(LogLevel level, String message, Throwable throwable, Map<String, Object> context);
    
    void debug(String message);
    
    void debug(String message, Map<String, Object> context);
    
    void info(String message);
    
    void info(String message, Map<String, Object> context);
    
    void warn(String message);
    
    void warn(String message, Map<String, Object> context);
    
    void error(String message);
    
    void error(String message, Map<String, Object> context);
    
    void error(String message, Throwable throwable);
    
    void error(String message, Throwable throwable, Map<String, Object> context);
    
    enum LogLevel {
        DEBUG, INFO, WARN, ERROR, FATAL
    }
}
