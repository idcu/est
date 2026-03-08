package ltd.idcu.est.security.api;

public class AuthorizationException extends SecurityException {
    
    private final String resource;
    private final String action;
    
    public AuthorizationException(String message) {
        super(message, "AUTHORIZATION_ERROR");
        this.resource = null;
        this.action = null;
    }
    
    public AuthorizationException(String message, String resource, String action) {
        super(message, "AUTHORIZATION_ERROR");
        this.resource = resource;
        this.action = action;
    }
    
    public AuthorizationException(String message, String errorCode, String resource, String action) {
        super(message, errorCode);
        this.resource = resource;
        this.action = action;
    }
    
    public String getResource() {
        return resource;
    }
    
    public String getAction() {
        return action;
    }
}
