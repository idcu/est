package ltd.idcu.est.ai.api.rag;

import java.util.List;
import java.util.Map;

public interface Document {
    
    String getId();
    
    String getContent();
    
    Map<String, Object> getMetadata();
    
    String getSource();
}
