package ltd.idcu.est.security.api;

import java.util.Map;

public interface AttributeBasedAuthorization extends Authorization {
    
    boolean evaluate(String resource, String action, Map<String, Object> attributes);
    
    Map<String, Object> getSubjectAttributes();
    
    Map<String, Object> getResourceAttributes(String resource);
    
    Map<String, Object> getEnvironmentAttributes();
}
