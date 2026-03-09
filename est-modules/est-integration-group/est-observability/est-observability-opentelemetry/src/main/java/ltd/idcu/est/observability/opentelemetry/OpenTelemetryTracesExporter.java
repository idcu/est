package ltd.idcu.est.observability.opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import ltd.idcu.est.observability.api.SimpleTraceContext;
import ltd.idcu.est.observability.api.SimpleTraceScope;
import ltd.idcu.est.observability.api.TraceContext;
import ltd.idcu.est.observability.api.TraceScope;
import ltd.idcu.est.observability.api.TracesExporter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class OpenTelemetryTracesExporter implements TracesExporter {

    private final String serviceName;
    private final String serviceVersion;
    private final String otlpEndpoint;
    private OpenTelemetry openTelemetry;
    private Tracer tracer;
    private SdkTracerProvider tracerProvider;
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
        if (running) {
            return;
        }
        
        Resource resource = Resource.getDefault().toBuilder()
                .put("service.name", serviceName)
                .put("service.version", serviceVersion)
                .build();

        SpanExporter otlpExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        BatchSpanProcessor spanProcessor = BatchSpanProcessor.builder(otlpExporter)
                .build();

        tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(spanProcessor)
                .setResource(resource)
                .build();

        ContextPropagators propagators = ContextPropagators.create(
                W3CTraceContextPropagator.getInstance()
        );

        openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(propagators)
                .build();

        tracer = openTelemetry.getTracer(serviceName, serviceVersion);
        running = true;
    }

    @Override
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (tracerProvider != null) {
            tracerProvider.close();
        }
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
        if (!running) {
            SimpleTraceContext context = new SimpleTraceContext(
                    generateTraceId(), generateSpanId(), true
            );
            SimpleTraceScope scope = new SimpleTraceScope(spanName, context, this);
            currentScope.set(scope);
            return scope;
        }

        Context otelContext = Context.current();
        if (parent != null) {
            // Propagate from our TraceContext
            SpanContext spanContext = SpanContext.createFromRemoteParent(
                    parent.getTraceId(),
                    parent.getSpanId(),
                    io.opentelemetry.api.trace.TraceFlags.getDefault(),
                    io.opentelemetry.api.trace.TraceState.getDefault()
            );
            otelContext = otelContext.with(Span.wrap(spanContext));
        }

        Span span = tracer.spanBuilder(spanName)
                .setParent(otelContext)
                .startSpan();

        try (Scope ignored = span.makeCurrent()) {
            SimpleTraceContext context = new SimpleTraceContext(
                    span.getSpanContext().getTraceId(),
                    span.getSpanContext().getSpanId(),
                    span.getParentSpanContext().isValid() ? span.getParentSpanContext().getSpanId() : null,
                    span.getSpanContext().isSampled()
            );
            SimpleTraceScope scope = new SimpleTraceScope(spanName, context, this);
            currentScope.set(scope);
            return scope;
        }
    }

    @Override
    public void endSpan(TraceScope scope) {
        endSpan(scope, true);
    }

    @Override
    public void endSpan(TraceScope scope, boolean success) {
        if (running && scope instanceof SimpleTraceScope) {
            // End the OpenTelemetry span
            Span currentSpan = Span.current();
            if (currentSpan != null) {
                if (!success) {
                    currentSpan.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR);
                }
                currentSpan.end();
            }
        }
        if (currentScope.get() == scope) {
            currentScope.remove();
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, String value) {
        if (running) {
            Span.current().setAttribute(key, value);
        }
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, long value) {
        if (running) {
            Span.current().setAttribute(key, value);
        }
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, boolean value) {
        if (running) {
            Span.current().setAttribute(key, value);
        }
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addTag(TraceScope scope, String key, double value) {
        if (running) {
            Span.current().setAttribute(key, value);
        }
        if (scope instanceof SimpleTraceScope) {
            ((SimpleTraceScope) scope).setTag(key, value);
        }
    }

    @Override
    public void addEvent(TraceScope scope, String eventName) {
        if (running) {
            Span.current().addEvent(eventName);
        }
    }

    @Override
    public void addEvent(TraceScope scope, String eventName, Map<String, Object> attributes) {
        if (running) {
            Attributes.Builder builder = Attributes.builder();
            attributes.forEach((key, value) -> {
                if (value instanceof String) {
                    builder.put(key, (String) value);
                } else if (value instanceof Long) {
                    builder.put(key, (Long) value);
                } else if (value instanceof Boolean) {
                    builder.put(key, (Boolean) value);
                } else if (value instanceof Double) {
                    builder.put(key, (Double) value);
                }
            });
            Span.current().addEvent(eventName, builder.build());
        }
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
        if (running) {
            Span currentSpan = Span.current();
            if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
                return new SimpleTraceContext(
                        currentSpan.getSpanContext().getTraceId(),
                        currentSpan.getSpanContext().getSpanId(),
                        currentSpan.getParentSpanContext().isValid() ? currentSpan.getParentSpanContext().getSpanId() : null,
                        currentSpan.getSpanContext().isSampled()
                );
            }
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
