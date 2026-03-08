package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;

import java.util.List;

public class LoggingSpanExporter implements SpanExporter {
    @Override
    public void export(TraceContext context) {
        System.out.println("[TRACE] " + context);
    }

    @Override
    public void exportBatch(List<TraceContext> contexts) {
        for (TraceContext context : contexts) {
            export(context);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void shutdown() {
    }
}
