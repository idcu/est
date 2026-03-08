package ltd.idcu.est.security.api;

public interface Permission {
    
    String getId();
    
    String getName();
    
    String getResource();
    
    String getAction();
    
    String getDescription();
    
    boolean isEnabled();
}
