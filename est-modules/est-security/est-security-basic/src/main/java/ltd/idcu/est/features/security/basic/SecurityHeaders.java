package ltd.idcu.est.features.security.basic;

import java.util.HashMap;
import java.util.Map;

public class SecurityHeaders {
    
    private final Map<String, String> headers = new HashMap<>();
    
    public SecurityHeaders() {
        setDefaultSecurityHeaders();
    }
    
    private void setDefaultSecurityHeaders() {
        headers.put("X-Content-Type-Options", "nosniff");
        headers.put("X-Frame-Options", "DENY");
        headers.put("X-XSS-Protection", "1; mode=block");
        headers.put("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        headers.put("Content-Security-Policy", "default-src 'self'");
        headers.put("Referrer-Policy", "strict-origin-when-cross-origin");
        headers.put("Permissions-Policy", "geolocation=(), microphone=(), camera=()");
    }
    
    public SecurityHeaders setXContentTypeOptions(String value) {
        headers.put("X-Content-Type-Options", value);
        return this;
    }
    
    public SecurityHeaders setXFrameOptions(String value) {
        headers.put("X-Frame-Options", value);
        return this;
    }
    
    public SecurityHeaders setXXssProtection(String value) {
        headers.put("X-XSS-Protection", value);
        return this;
    }
    
    public SecurityHeaders setStrictTransportSecurity(String value) {
        headers.put("Strict-Transport-Security", value);
        return this;
    }
    
    public SecurityHeaders setContentSecurityPolicy(String value) {
        headers.put("Content-Security-Policy", value);
        return this;
    }
    
    public SecurityHeaders setReferrerPolicy(String value) {
        headers.put("Referrer-Policy", value);
        return this;
    }
    
    public SecurityHeaders setPermissionsPolicy(String value) {
        headers.put("Permissions-Policy", value);
        return this;
    }
    
    public SecurityHeaders addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }
    
    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }
    
    public String getHeader(String name) {
        return headers.get(name);
    }
}
