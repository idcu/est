package ltd.idcu.est.features.security.api;

public interface Role {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    boolean isEnabled();
}
