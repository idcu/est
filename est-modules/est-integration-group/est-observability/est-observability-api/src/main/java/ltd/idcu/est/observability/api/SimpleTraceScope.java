package ltd.idcu.est.observability.api;

import java.util.HashMap;
import java.util.Map;

public class SimpleTraceScope implements TraceScope {

    private final String spanName;
    private final TraceContext context;
    private final long startTime;
    private long endTime;
    private boolean success = true;
    private final Map<String, Object> tags = new HashMap<>();
    private final TracesExporter exporter;

    public SimpleTraceScope(String spanName, TraceContext context, TracesExporter exporter) {
        this.spanName = spanName;
        this.context = context;
        this.startTime = System.currentTimeMillis();
        this.exporter = exporter;
    }

    @Override
    public String getTraceId() {
        return context.getTraceId();
    }

    @Override
    public String getSpanId() {
        return context.getSpanId();
    }

    @Override
    public String getParentSpanId() {
        return context.getParentSpanId();
    }

    @Override
    public String getSpanName() {
        return spanName;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }

    @Override
    public long getDuration() {
        return endTime > 0 ? endTime - startTime : System.currentTimeMillis() - startTime;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public TraceContext getContext() {
        return context;
    }

    public void setTag(String key, Object value) {
        tags.put(key, value);
    }

    public Map<String, Object> getTags() {
        return new HashMap<>(tags);
    }

    @Override
    public void close() {
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
            exporter.endSpan(this, success);
        }
    }

    public void end() {
        close();
    }
}
