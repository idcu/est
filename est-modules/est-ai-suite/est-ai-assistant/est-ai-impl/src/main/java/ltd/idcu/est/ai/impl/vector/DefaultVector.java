package ltd.idcu.est.ai.impl.vector;

import ltd.idcu.est.ai.api.vector.Vector;

import java.util.HashMap;
import java.util.Map;

public class DefaultVector implements Vector {
    
    private final String id;
    private final float[] values;
    private final Map<String, Object> metadata;
    private double score;
    
    public DefaultVector(String id, float[] values) {
        this(id, values, new HashMap<>());
    }
    
    public DefaultVector(String id, float[] values, Map<String, Object> metadata) {
        this.id = id;
        this.values = values;
        this.metadata = metadata;
        this.score = 0.0;
    }
    
    @Override
    public float[] getValues() {
        return values;
    }
    
    @Override
    public int getDimension() {
        return values != null ? values.length : 0;
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public double getScore() {
        return score;
    }
    
    @Override
    public void setScore(double score) {
        this.score = score;
    }
}
