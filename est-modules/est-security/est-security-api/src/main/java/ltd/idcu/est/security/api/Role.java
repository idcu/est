package ltd.idcu.est.security.api;

public interface Role {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    boolean isEnabled();
}
