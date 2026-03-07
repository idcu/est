package ltd.idcu.est.features.logging.api;

public interface LogFormatter {
    
    String format(LogRecord record);
    
    String getContentType();
}
