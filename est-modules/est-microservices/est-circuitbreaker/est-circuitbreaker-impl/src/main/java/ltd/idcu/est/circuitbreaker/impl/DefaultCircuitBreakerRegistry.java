package ltd.idcu.est.circuitbreaker.impl;

import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCircuitBreakerRegistry implements CircuitBreakerRegistry {
    private final Map<String, CircuitBreaker> circuitBreakers = new ConcurrentHashMap<>();
    private boolean autoSaveEnabled = false;
    private String autoSavePath = null;

    @Override
    public CircuitBreaker create(String name) {
        CircuitBreaker circuitBreaker = create(name, new CircuitBreakerConfig());
        autoSaveIfEnabled();
        return circuitBreaker;
    }

    @Override
    public CircuitBreaker create(String name, CircuitBreakerConfig config) {
        CircuitBreaker circuitBreaker = circuitBreakers.computeIfAbsent(name, k -> new DefaultCircuitBreaker(name, config));
        autoSaveIfEnabled();
        return circuitBreaker;
    }

    @Override
    public Optional<CircuitBreaker> get(String name) {
        return Optional.ofNullable(circuitBreakers.get(name));
    }

    @Override
    public CircuitBreaker getOrCreate(String name) {
        CircuitBreaker circuitBreaker = circuitBreakers.computeIfAbsent(name, k -> new DefaultCircuitBreaker(name));
        autoSaveIfEnabled();
        return circuitBreaker;
    }

    @Override
    public void remove(String name) {
        circuitBreakers.remove(name);
        autoSaveIfEnabled();
    }

    @Override
    public Map<String, CircuitBreaker> getAll() {
        return new ConcurrentHashMap<>(circuitBreakers);
    }

    @Override
    public void clear() {
        circuitBreakers.clear();
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
        sb.append("  \"circuitBreakers\": [\n");
        boolean first = true;
        for (CircuitBreaker circuitBreaker : circuitBreakers.values()) {
            if (!first) {
                sb.append(",\n");
            }
            String cbJson = ((DefaultCircuitBreaker) circuitBreaker).toJson();
            sb.append("    ").append(cbJson);
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
        circuitBreakers.clear();
        String cbArray = extractJsonArray(json, "circuitBreakers");
        if (cbArray != null && !cbArray.isEmpty()) {
            parseCircuitBreakersArray(cbArray);
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

    private void parseCircuitBreakersArray(String arrayStr) {
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
            String cbJson = content.substring(braceStart, braceEnd);
            DefaultCircuitBreaker circuitBreaker = DefaultCircuitBreaker.fromJson(cbJson);
            circuitBreakers.put(circuitBreaker.getName(), circuitBreaker);
            i = braceEnd;
            while (i < content.length() && Character.isWhitespace(content.charAt(i))) {
                i++;
            }
            if (i < content.length() && content.charAt(i) == ',') {
                i++;
            }
        }
    }
}
