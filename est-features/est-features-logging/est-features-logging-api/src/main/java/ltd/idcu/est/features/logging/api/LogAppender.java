package ltd.idcu.est.features.logging.api;

public interface LogAppender {
    
    void append(LogRecord record);
    
    void flush();
    
    void close();
    
    String getName();
    
    boolean isStarted();
    
    void start();
    
    void stop();
}
