package ltd.idcu.est.monitor.api;

public class MonitorException extends RuntimeException {
    
    public MonitorException(String message) {
        super(message);
    }
    
    public MonitorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public MonitorException(Throwable cause) {
        super(cause);
    }
}
