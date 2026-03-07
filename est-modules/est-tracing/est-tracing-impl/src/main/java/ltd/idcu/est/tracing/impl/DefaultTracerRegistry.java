package ltd.idcu.est.tracing.impl;

import ltd.idcu.est.tracing.api.SpanExporter;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TracerRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTracerRegistry implements TracerRegistry {
    private final Map<String, Tracer> tracers = new ConcurrentHashMap<>();
    private boolean autoSaveEnabled = false;
    private String autoSavePath = null;

    @Override
    public Tracer create(String serviceName) {
        return create(serviceName, null);
    }

    @Override
    public Tracer create(String serviceName, SpanExporter exporter) {
        Tracer tracer = tracers.computeIfAbsent(serviceName, k -> new DefaultTracer(serviceName, exporter));
        autoSaveIfEnabled();
        return tracer;
    }

    @Override
    public Optional<Tracer> get(String serviceName) {
        return Optional.ofNullable(tracers.get(serviceName));
    }

    @Override
    public Tracer getOrCreate(String serviceName) {
        Tracer tracer = tracers.computeIfAbsent(serviceName, k -> new DefaultTracer(serviceName));
        autoSaveIfEnabled();
        return tracer;
    }

    @Override
    public void remove(String serviceName) {
        tracers.remove(serviceName);
        autoSaveIfEnabled();
    }

    @Override
    public Map<String, Tracer> getAll() {
        return new ConcurrentHashMap<>(tracers);
    }

    @Override
    public void clear() {
        tracers.clear();
        autoSaveIfEnabled();
    }

    @Override
    public void saveToJson(String path) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(path)) {
            saveToJson(outputStream);
        }
    }

    @Override
    public void saveToJson(OutputStream outputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"tracers\": [\n");
        boolean first = true;
        for (String serviceName : tracers.keySet()) {
            if (!first) {
                sb.append(",\n");
            }
            sb.append("    {\n");
            sb.append("      \"serviceName\":\"").append(escapeJson(serviceName)).append("\"\n");
            sb.append("    }");
            first = false;
        }
        sb.append("\n");
        sb.append("  ]\n");
        sb.append("}");
        outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void loadFromJson(String path) throws IOException {
        try (InputStream inputStream = new FileInputStream(path)) {
            loadFromJson(inputStream);
        }
    }

    @Override
    public void loadFromJson(InputStream inputStream) throws IOException {
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        tracers.clear();
        String tracersArray = extractJsonArray(json, "tracers");
        if (tracersArray != null && !tracersArray.isEmpty()) {
            parseTracersArray(tracersArray);
        }
    }

    @Override
    public void setAutoSave(boolean enabled) {
        this.autoSaveEnabled = enabled;
    }

    @Override
    public void setAutoSavePath(String path) {
        this.autoSavePath = path;
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return autoSaveEnabled;
    }

    @Override
    public String getAutoSavePath() {
        return autoSavePath;
    }

    private void autoSaveIfEnabled() {
        if (autoSaveEnabled && autoSavePath != null) {
            try {
                saveToJson(autoSavePath);
            } catch (IOException e) {
            }
        }
    }

    private String extractJsonArray(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int start = json.indexOf(searchKey);
        if (start == -1) {
            return "";
        }
        start += searchKey.length();
        int bracketStart = json.indexOf("[", start);
        int bracketEnd = bracketStart + 1;
        int count = 1;
        while (count > 0 && bracketEnd < json.length()) {
            char c = json.charAt(bracketEnd);
            if (c == '[') {
                count++;
            } else if (c == ']') {
                count--;
            }
            bracketEnd++;
        }
        return json.substring(bracketStart, bracketEnd);
    }

    private void parseTracersArray(String arrayStr) {
        String content = arrayStr.substring(1, arrayStr.length() - 1).trim();
        if (content.isEmpty()) {
            return;
        }
        int i = 0;
        while (i < content.length()) {
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i >= content.length()) {
                break;
            }
            int braceStart = content.indexOf("{", i);
            int braceEnd = braceStart + 1;
            int count = 1;
            while (count > 0 && braceEnd < content.length()) {
                char c = content.charAt(braceEnd);
                if (c == '{') {
                    count++;
                } else if (c == '}') {
                    count--;
                }
                braceEnd++;
            }
            String tracerJson = content.substring(braceStart, braceEnd);
            String serviceName = extractJsonString(tracerJson, "serviceName");
            if (serviceName != null && !serviceName.isEmpty()) {
                tracers.computeIfAbsent(serviceName, k -> new DefaultTracer(serviceName));
            }
            i = braceEnd;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
    }

    private String extractJsonString(String json, String key) {
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

    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private String unescapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
}
