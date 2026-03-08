package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.api.Tracer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class DefaultTracer implements Tracer {
    private final String serviceName;
    private final SpanExporter exporter;
    private final ThreadLocal<TraceContext> currentContext = new ThreadLocal<>();
    private final Map<String, Map<String, Object>> tags = new HashMap<>();

    public DefaultTracer(String serviceName) {
        this(serviceName, null);
    }

    public DefaultTracer(String serviceName, SpanExporter exporter) {
        this.serviceName = serviceName;
        this.exporter = exporter;
    }

    @Override
    public TraceContext startSpan(String spanName) {
        TraceContext parent = currentContext.get();
        return startSpan(spanName, parent);
    }

    @Override
    public TraceContext startSpan(String spanName, TraceContext parent) {
        String traceId = parent != null ? parent.getTraceId() : generateTraceId();
        String spanId = generateSpanId();
        String parentSpanId = parent != null ? parent.getSpanId() : null;

        TraceContext context = new TraceContext(traceId, spanId, parentSpanId, serviceName);
        currentContext.set(context);
        tags.put(spanId, new HashMap<>());
        return context;
    }

    @Override
    public void endSpan(TraceContext context) {
        endSpan(context, true);
    }

    @Override
    public void endSpan(TraceContext context, boolean success) {
        context.setEndTimeMs(System.currentTimeMillis());
        context.setSuccess(success);
        
        Map<String, Object> spanTags = tags.get(context.getSpanId());
        if (spanTags != null) {
            for (Map.Entry<String, Object> entry : spanTags.entrySet()) {
                context.addTag(entry.getKey(), entry.getValue());
            }
        }
        
        if (exporter != null) {
            exporter.export(context);
        }

        TraceContext current = currentContext.get();
        if (current != null && current.getSpanId().equals(context.getSpanId())) {
            clearCurrentContext();
        }
        
        tags.remove(context.getSpanId());
    }

    @Override
    public void addTag(TraceContext context, String key, String value) {
        getTags(context).put(key, value);
    }

    @Override
    public void addTag(TraceContext context, String key, long value) {
        getTags(context).put(key, value);
    }

    @Override
    public void addTag(TraceContext context, String key, boolean value) {
        getTags(context).put(key, value);
    }

    private Map<String, Object> getTags(TraceContext context) {
        return tags.computeIfAbsent(context.getSpanId(), k -> new HashMap<>());
    }

    @Override
    public <T> T trace(String spanName, Supplier<T> supplier) {
        TraceContext context = startSpan(spanName);
        try {
            T result = supplier.get();
            endSpan(context, true);
            return result;
        } catch (Exception e) {
            endSpan(context, false);
            throw e;
        }
    }

    @Override
    public void trace(String spanName, Runnable runnable) {
        trace(spanName, () -> {
            runnable.run();
            return null;
        });
    }

    @Override
    public String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    public TraceContext getCurrentContext() {
        return currentContext.get();
    }

    @Override
    public void setCurrentContext(TraceContext context) {
        currentContext.set(context);
    }

    @Override
    public void clearCurrentContext() {
        currentContext.remove();
    }
}
