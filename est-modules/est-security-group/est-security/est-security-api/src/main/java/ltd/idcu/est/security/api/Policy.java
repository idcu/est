package ltd.idcu.est.security.api;

import java.util.Map;

public interface Policy {
    
    String getId();
    
    String getName();
    
    String getDescription();
    
    PolicyEffect getEffect();
    
    boolean evaluate(Map<String, Object> context);
    
    int getPriority();
}
