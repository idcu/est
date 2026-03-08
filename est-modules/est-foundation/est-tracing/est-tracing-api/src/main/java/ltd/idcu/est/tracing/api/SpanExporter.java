package ltd.idcu.est.tracing.api;

import java.util.List;

public interface SpanExporter {
    void export(TraceContext context);

    void exportBatch(List<TraceContext> contexts);

    void flush();

    void shutdown();
}
