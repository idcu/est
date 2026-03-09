package ltd.idcu.est.observability.api;

import java.util.Map;
import java.util.function.Supplier;

public interface TracesExporter {
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    TraceScope startSpan(String spanName);
    
    TraceScope startSpan(String spanName, TraceContext parent);
    
    void endSpan(TraceScope scope);
    
    void endSpan(TraceScope scope, boolean success);
    
    void addTag(TraceScope scope, String key, String value);
    
    void addTag(TraceScope scope, String key, long value);
    
    void addTag(TraceScope scope, String key, boolean value);
    
    void addTag(TraceScope scope, String key, double value);
    
    void addEvent(TraceScope scope, String eventName);
    
    void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes);
    
    <T> T trace(String spanName, Supplier<T> supplier);
    
    void trace(String spanName, Runnable runnable);
    
    TraceContext getCurrentContext();
    
    void setCurrentContext(TraceContext context);
    
    void clearCurrentContext();
    
    String generateTraceId();
    
    String generateSpanId();
}
