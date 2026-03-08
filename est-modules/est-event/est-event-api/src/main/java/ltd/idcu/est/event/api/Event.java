package ltd.idcu.est.event.api;

public interface Event {
    
    String getType();
    
    long getTimestamp();
    
    Object getSource();
    
    Object getPayload();
}
