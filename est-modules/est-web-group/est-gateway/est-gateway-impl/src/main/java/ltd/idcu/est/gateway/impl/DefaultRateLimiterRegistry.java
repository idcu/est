package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.RateLimiter;
import ltd.idcu.est.gateway.api.RateLimiterRegistry;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultRateLimiterRegistry implements RateLimiterRegistry {
    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private boolean autoSaveEnabled = false;
    private String autoSavePath = null;

    @Override
    public RateLimiter create(String name, long capacity, long refillRate) {
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(name, k -> new TokenBucketRateLimiter(capacity, refillRate));
        autoSaveIfEnabled();
        return rateLimiter;
    }

    @Override
    public Optional<RateLimiter> get(String name) {
        return Optional.ofNullable(rateLimiters.get(name));
    }

    @Override
    public RateLimiter getOrCreate(String name, long capacity, long refillRate) {
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(name, k -> new TokenBucketRateLimiter(capacity, refillRate));
        autoSaveIfEnabled();
        return rateLimiter;
    }

    @Override
    public void remove(String name) {
        rateLimiters.remove(name);
        autoSaveIfEnabled();
    }

    @Override
    public Map<String, RateLimiter> getAll() {
        return new ConcurrentHashMap<>(rateLimiters);
    }

    @Override
    public void clear() {
        rateLimiters.clear();
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
        sb.append("  \"rateLimiters\": [\n");
        boolean first = true;
        for (Map.Entry<String, RateLimiter> entry : rateLimiters.entrySet()) {
            if (!first) {
                sb.append(",\n");
            }
            String rlJson = ((TokenBucketRateLimiter) entry.getValue()).toJson(entry.getKey());
            sb.append("    ").append(rlJson);
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
        rateLimiters.clear();
        String rlArray = extractJsonArray(json, "rateLimiters");
        if (rlArray != null && !rlArray.isEmpty()) {
            parseRateLimitersArray(rlArray);
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

    private void parseRateLimitersArray(String arrayStr) {
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
            String rlJson = content.substring(braceStart, braceEnd);
            TokenBucketRateLimiter rateLimiter = TokenBucketRateLimiter.fromJson(rlJson);
            String name = TokenBucketRateLimiter.extractNameFromJson(rlJson);
            rateLimiters.put(name, rateLimiter);
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
