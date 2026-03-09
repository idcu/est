package ltd.idcu.est.observability.api;

public interface TraceScope extends AutoCloseable {
    
    String getTraceId();
    
    String getSpanId();
    
    String getParentSpanId();
    
    String getSpanName();
    
    long getStartTime();
    
    long getEndTime();
    
    long getDuration();
    
    boolean isSuccess();
    
    void setSuccess(boolean success);
    
    TraceContext getContext();
    
    @Override
    void close();
}
