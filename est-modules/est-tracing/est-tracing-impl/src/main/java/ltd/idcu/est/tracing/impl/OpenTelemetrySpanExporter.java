package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;

import java.util.List;

public class OpenTelemetrySpanExporter implements SpanExporter {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public void export(TraceContext context) {
        sb.append("{");
        sb.append("\"traceId\":\"").append(context.getTraceId()).append("\",");
        sb.append("\"spanId\":\"").append(context.getSpanId()).append("\",");
        if (context.getParentSpanId() != null) {
            sb.append("\"parentSpanId\":\"").append(context.getParentSpanId()).append("\",");
        }
        sb.append("\"serviceName\":\"").append(context.getServiceName()).append("\",");
        sb.append("\"durationMs\":").append(context.getDurationMs());
        sb.append("}");
        sb.append("\n");
    }

    @Override
    public void exportBatch(List<TraceContext> contexts) {
        for (TraceContext context : contexts) {
            export(context);
        }
    }

    @Override
    public void flush() {
        if (sb.length() > 0) {
            System.out.println("[OTEL] " + sb);
            sb.setLength(0);
        }
    }

    @Override
    public void shutdown() {
        flush();
    }

    public String getExportedData() {
        return sb.toString();
    }
}
