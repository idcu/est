package ltd.idcu.est.logging.api;

public interface LogFormatter {
    
    String format(LogRecord record);
    
    String getContentType();
}
