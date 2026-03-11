package ltd.idcu.est.gateway.impl.middleware;

import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.impl.DefaultGatewayContext;

import java.util.UUID;
import java.util.function.Function;

public class TracingMiddleware implements GatewayMiddleware {
    private static final String NAME = "tracing";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String SPAN_ID_HEADER = "X-Span-Id";
    public static final String PARENT_SPAN_ID_HEADER = "X-Parent-Span-Id";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Function<GatewayContext, GatewayContext> process() {
        return context -> {
            String traceId = context.getRequestHeaders().get(TRACE_ID_HEADER);
            String parentSpanId = context.getRequestHeaders().get(SPAN_ID_HEADER);
            
            if (traceId == null || traceId.isEmpty()) {
                traceId = generateId();
            }
            
            String spanId = generateId();
            
            context.setAttribute("traceId", traceId);
            context.setAttribute("spanId", spanId);
            context.setAttribute("parentSpanId", parentSpanId);
            
            if (context instanceof DefaultGatewayContext) {
                DefaultGatewayContext ctx = (DefaultGatewayContext) context;
                ctx.addRequestHeader(TRACE_ID_HEADER, traceId);
                ctx.addRequestHeader(SPAN_ID_HEADER, spanId);
                if (parentSpanId != null) {
                    ctx.addRequestHeader(PARENT_SPAN_ID_HEADER, parentSpanId);
                }
                
                ctx.addResponseHeader(TRACE_ID_HEADER, traceId);
            }
            
            return context;
        };
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public int getOrder() {
        return -150;
    }
}
