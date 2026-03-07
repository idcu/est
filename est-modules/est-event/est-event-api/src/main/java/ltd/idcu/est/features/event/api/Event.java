package ltd.idcu.est.features.event.api;

public interface Event {
    
    String getType();
    
    long getTimestamp();
    
    Object getSource();
    
    Object getPayload();
}
