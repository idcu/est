package ltd.idcu.est.tracing.api;

import java.util.function.Supplier;

public interface Tracer {
    TraceContext startSpan(String spanName);

    TraceContext startSpan(String spanName, TraceContext parent);

    void endSpan(TraceContext context);

    void endSpan(TraceContext context, boolean success);

    void addTag(TraceContext context, String key, String value);

    void addTag(TraceContext context, String key, long value);

    void addTag(TraceContext context, String key, boolean value);

    <T> T trace(String spanName, Supplier<T> supplier);

    void trace(String spanName, Runnable runnable);

    String generateTraceId();

    String generateSpanId();

    TraceContext getCurrentContext();

    void setCurrentContext(TraceContext context);

    void clearCurrentContext();
}
