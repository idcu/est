package ltd.idcu.est.features.security.api;

public interface Authentication {
    
    String getToken();
    
    User getUser();
    
    boolean isAuthenticated();
    
    void setAuthenticated(boolean authenticated);
    
    Object getCredentials();
    
    Object getDetails();
}
