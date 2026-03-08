package ltd.idcu.est.ai.api.vector;

import java.util.Map;

public interface Vector {
    
    float[] getValues();
    
    int getDimension();
    
    Map<String, Object> getMetadata();
    
    String getId();
    
    double getScore();
    
    void setScore(double score);
}
