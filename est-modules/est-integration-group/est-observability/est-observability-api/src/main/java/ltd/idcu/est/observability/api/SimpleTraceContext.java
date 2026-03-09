package ltd.idcu.est.observability.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleTraceContext implements TraceContext {

    private final String traceId;
    private final String spanId;
    private final String parentSpanId;
    private final boolean sampled;
    private final Map<String, String> baggage;

    public SimpleTraceContext(String traceId, String spanId, String parentSpanId, boolean sampled) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
        this.sampled = sampled;
        this.baggage = new ConcurrentHashMap<>();
    }

    public SimpleTraceContext(String traceId, String spanId, boolean sampled) {
        this(traceId, spanId, null, sampled);
    }

    @Override
    public String getTraceId() {
        return traceId;
    }

    @Override
    public String getSpanId() {
        return spanId;
    }

    @Override
    public String getParentSpanId() {
        return parentSpanId;
    }

    @Override
    public boolean isSampled() {
        return sampled;
    }

    @Override
    public Map<String, String> getBaggage() {
        return new HashMap<>(baggage);
    }

    @Override
    public String getBaggageItem(String key) {
        return baggage.get(key);
    }

    @Override
    public void setBaggageItem(String key, String value) {
        baggage.put(key, value);
    }

    @Override
    public TraceContext createChild(String spanName) {
        return new SimpleTraceContext(traceId, generateSpanId(), spanId, sampled);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("traceId", traceId);
        map.put("spanId", spanId);
        if (parentSpanId != null) {
            map.put("parentSpanId", parentSpanId);
        }
        map.put("sampled", sampled);
        map.put("baggage", new HashMap<>(baggage));
        return map;
    }

    @Override
    public String toW3CTraceparent() {
        String flags = sampled ? "01" : "00";
        return String.format("00-%s-%s-%s", traceId, spanId, flags);
    }

    @Override
    public String toW3CTracestate() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : baggage.entrySet()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    public static String generateSpanId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
