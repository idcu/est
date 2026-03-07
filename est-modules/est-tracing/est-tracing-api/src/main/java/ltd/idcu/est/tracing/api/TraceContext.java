package ltd.idcu.est.tracing.api;

import java.util.HashMap;
import java.util.Map;

public class TraceContext {
    private final String traceId;
    private final String spanId;
    private final String parentSpanId;
    private final String serviceName;
    private final long startTimeMs;
    private long endTimeMs;
    private boolean success;
    private final Map<String, Object> tags;

    public TraceContext(String traceId, String spanId, String parentSpanId, String serviceName) {
        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
        this.serviceName = serviceName;
        this.startTimeMs = System.currentTimeMillis();
        this.endTimeMs = 0;
        this.success = true;
        this.tags = new HashMap<>();
    }

    public TraceContext(String traceId, String spanId, String serviceName) {
        this(traceId, spanId, null, serviceName);
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getEndTimeMs() {
        return endTimeMs;
    }

    public void setEndTimeMs(long endTimeMs) {
        this.endTimeMs = endTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Object> getTags() {
        return new HashMap<>(tags);
    }

    public void addTag(String key, Object value) {
        tags.put(key, value);
    }

    public long getDurationMs() {
        if (endTimeMs > 0) {
            return endTimeMs - startTimeMs;
        }
        return System.currentTimeMillis() - startTimeMs;
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"traceId\":\"").append(escapeJson(traceId)).append("\",");
        sb.append("\"spanId\":\"").append(escapeJson(spanId)).append("\",");
        sb.append("\"parentSpanId\":").append(parentSpanId != null ? "\"" + escapeJson(parentSpanId) + "\"" : "null").append(",");
        sb.append("\"serviceName\":\"").append(escapeJson(serviceName)).append("\",");
        sb.append("\"startTimeMs\":").append(startTimeMs).append(",");
        sb.append("\"endTimeMs\":").append(endTimeMs).append(",");
        sb.append("\"success\":").append(success).append(",");
        sb.append("\"tags\":{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : tags.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"").append(escapeJson(entry.getKey())).append("\":");
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append("\"").append(escapeJson((String) value)).append("\"");
            } else {
                sb.append(value);
            }
            first = false;
        }
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    public static TraceContext fromJson(String json) {
        String traceId = extractJsonString(json, "traceId");
        String spanId = extractJsonString(json, "spanId");
        String parentSpanId = extractJsonStringOrNull(json, "parentSpanId");
        String serviceName = extractJsonString(json, "serviceName");
        long startTimeMs = Long.parseLong(extractJsonValue(json, "startTimeMs"));
        long endTimeMs = Long.parseLong(extractJsonValue(json, "endTimeMs"));
        boolean success = Boolean.parseBoolean(extractJsonValue(json, "success"));
        
        TraceContext context = new TraceContext(traceId, spanId, parentSpanId, serviceName);
        context.endTimeMs = endTimeMs;
        context.success = success;
        
        String tagsJson = extractJsonObject(json, "tags");
        if (tagsJson != null && !tagsJson.isEmpty()) {
            Map<String, Object> tags = parseJsonMap(tagsJson);
            for (Map.Entry<String, Object> entry : tags.entrySet()) {
                context.addTag(entry.getKey(), entry.getValue());
            }
        }
        
        return context;
    }

    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int quoteStart = json.indexOf("\"", start);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        return unescapeJson(json.substring(quoteStart + 1, quoteEnd));
    }

    private static String extractJsonStringOrNull(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return null;
        }
        start += searchKey.length();
        int nullIndex = json.indexOf("null", start);
        if (nullIndex == start) {
            return null;
        }
        int quoteStart = json.indexOf("\"", start);
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        return unescapeJson(json.substring(quoteStart + 1, quoteEnd));
    }

    private static String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        return json.substring(start, end).trim();
    }

    private static String extractJsonObject(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int braceStart = json.indexOf("{", start);
        int braceEnd = braceStart + 1;
        int count = 1;
        while (count > 0 && braceEnd < json.length()) {
            char c = json.charAt(braceEnd);
            if (c == '{') {
                count++;
            } else if (c == '}') {
                count--;
            }
            braceEnd++;
        }
        return json.substring(braceStart, braceEnd);
    }

    private static Map<String, Object> parseJsonMap(String json) {
        Map<String, Object> map = new HashMap<>();
        String content = json.substring(1, json.length() - 1).trim();
        if (content.isEmpty()) {
            return map;
        }
        
        int i = 0;
        while (i < content.length()) {
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }
            
            int keyStart = content.indexOf("\"", i);
            int keyEnd = content.indexOf("\"", keyStart + 1);
            String key = unescapeJson(content.substring(keyStart + 1, keyEnd));
            
            i = keyEnd + 1;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            i++;
            
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            
            Object value;
            if (content.charAt(i) == '\"') {
                int valueStart = i;
                int valueEnd = content.indexOf("\"", valueStart + 1);
                value = unescapeJson(content.substring(valueStart + 1, valueEnd));
                i = valueEnd + 1;
            } else if (content.charAt(i) == 't' || content.charAt(i) == 'f') {
                int trueEnd = content.indexOf("true", i);
                int falseEnd = content.indexOf("false", i);
                if (trueEnd == i) {
                    value = true;
                    i = trueEnd + 4;
                } else {
                    value = false;
                    i = falseEnd + 5;
                }
            } else {
                int end = i;
                while (end < content.length() && (Character.isDigit(content.charAt(end)) || content.charAt(end) == '.' || content.charAt(end) == '-')) {
                    end++;
                }
                String numStr = content.substring(i, end);
                if (numStr.contains(".")) {
                    value = Double.parseDouble(numStr);
                } else {
                    value = Long.parseLong(numStr);
                }
                i = end;
            }
            
            map.put(key, value);
            
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
        
        return map;
    }

    private static String unescapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }

    @Override
    public String toString() {
        return "TraceContext{" +
                "traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentSpanId='" + parentSpanId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", durationMs=" + getDurationMs() +
                '}';
    }
}
