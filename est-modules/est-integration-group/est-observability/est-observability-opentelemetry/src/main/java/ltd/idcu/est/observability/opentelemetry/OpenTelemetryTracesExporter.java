package ltd.idcu.est.observability.opentelemetry;

import ltd.idcu.est.observability.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OpenTelemetryTracesExporter implements TracesExporter {

    private final String serviceName;
    private final String serviceVersion;
    private final String otlpEndpoint;
    private final ThreadLocal<TraceScope> currentScope = new ThreadLocal<>();
    private boolean running = false;

    public OpenTelemetryTracesExporter(String serviceName, String serviceVersion, String otlpEndpoint) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.otlpEndpoint = otlpEndpoint;
    }

    public OpenTelemetryTracesExporter(String serviceName, String serviceVersion) {
        this(serviceName, serviceVersion, "http://localhost:4317");
    }

    @Override
    public void start() {
        if (!running) {
            running = true;
        }
    }

    @Override
    public void stop() {
        running = false;
        currentScope.remove();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public TraceScope startSpan(String spanName) {
        return startSpan(spanName, null);
    }

    @Override
    public TraceScope startSpan(String spanName, TraceContext parent) {
        String traceId;
        String spanId = generateSpanId();
        String parentSpanId = null;
        
        if (parent != null) {
            traceId = parent.getTraceId();
            parentSpanId = parent.getSpanId();
        } else {
            TraceContext current = getCurrentContext();
            if (current != null) {
                traceId = current.getTraceId();
                parentSpanId = current.getSpanId();
            } else {
                traceId = generateTraceId();
            }
        }
        
        SimpleTraceContext context = new SimpleTraceContext(traceId, spanId, parentSpanId, true);
        SimpleTraceScope scope = new SimpleTraceScope(spanName, context, this);
        currentScope.set(scope);
        return scope;
    }

    @Override
    public void endSpan(TraceScope scope) {
        endSpan(scope, true);
    }

    @Override
    public void endSpan(TraceScope scope, boolean success) {
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setSuccess(success);
        }
        if (currentScope.get() == scope) {
            currentScope.remove();
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, String value) {
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, long value) {
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, boolean value) {
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, double value) {
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addEvent(TraceScope scope, String eventName) {
    }

    @Override
    public void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes) {
    }

    @Override
    public <T> T trace(String spanName, Supplier<T> supplier) {
        try (TraceScope scope = startSpan(spanName)) {
            try {
                T result = supplier.get();
                addTag(scope, "success", true);
                return result;
            } catch (Exception e) {
                addTag(scope, "success", false);
                addTag(scope, "error", e.getMessage());
                endSpan(scope, false);
                throw e;
            }
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
    public TraceContext getCurrentContext() {
        TraceScope scope = currentScope.get();
        if (scope != null) {
            return scope.getContext();
        }
        return null;
    }

    @Override
    public void setCurrentContext(TraceContext context) {
        if (context != null) {
            SimpleTraceContext simpleContext = new SimpleTraceContext(
                    context.getTraceId(),
                    context.getSpanId(),
                    context.getParentSpanId(),
                    context.isSampled()
            );
            SimpleTraceScope scope = new SimpleTraceScope("context", simpleContext, this);
            currentScope.set(scope);
        }
    }

    @Override
    public void clearCurrentContext() {
        currentScope.remove();
    }

    @Override
    public String generateTraceId() {
        return SimpleTraceContext.generateTraceId();
    }

    @Override
    public String generateSpanId() {
        return SimpleTraceContext.generateSpanId();
    }
}
