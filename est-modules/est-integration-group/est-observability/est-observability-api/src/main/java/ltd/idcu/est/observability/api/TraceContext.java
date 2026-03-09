package ltd.idcu.est.observability.api;

import java.util.Map;

public interface TraceContext {
    
    String getTraceId();
    
    String getSpanId();
    
    String getParentSpanId();
    
    boolean isSampled();
    
    Map<String, String> getBaggage();
    
    String getBaggageItem(String key);
    
    void setBaggageItem(String key, String value);
    
    TraceContext createChild(String spanName);
    
    Map<String, Object> toMap();
    
    String toW3CTraceparent();
    
    String toW3CTracestate();
    
    static TraceContext fromW3CTraceparent(String traceparent) {
        return null;
    }
}
