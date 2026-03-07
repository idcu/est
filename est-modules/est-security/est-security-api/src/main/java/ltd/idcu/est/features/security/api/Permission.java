package ltd.idcu.est.features.security.api;

public interface Permission {
    
    String getId();
    
    String getName();
    
    String getResource();
    
    String getAction();
    
    String getDescription();
    
    boolean isEnabled();
}
