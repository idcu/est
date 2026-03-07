package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.RateLimiter;

import java.util.concurrent.atomic.AtomicLong;

public class TokenBucketRateLimiter implements RateLimiter {
    private final long capacity;
    private final long refillRate;
    private final AtomicLong tokens;
    private final AtomicLong lastRefillTime;
    private final Object lock = new Object();

    public TokenBucketRateLimiter(long capacity, long refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTime = new AtomicLong(System.currentTimeMillis());
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRefillTime.get();
        if (elapsed > 0) {
            long tokensToAdd = (elapsed * refillRate) / 1000;
            if (tokensToAdd > 0) {
                long currentTokens = tokens.get();
                long newTokens = Math.min(currentTokens + tokensToAdd, capacity);
                tokens.set(newTokens);
                lastRefillTime.set(now);
            }
        }
    }

    @Override
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    @Override
    public boolean tryAcquire(int permits) {
        synchronized (lock) {
            refill();
            if (tokens.get() >= permits) {
                tokens.addAndGet(-permits);
                return true;
            }
            return false;
        }
    }

    public long getAvailableTokens() {
        synchronized (lock) {
            refill();
            return tokens.get();
        }
    }

    public long getCapacity() {
        return capacity;
    }

    public long getRefillRate() {
        return refillRate;
    }

    public String toJson(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\":\"").append(escapeJson(name)).append("\",");
        sb.append("\"capacity\":").append(capacity).append(",");
        sb.append("\"refillRate\":").append(refillRate).append(",");
        sb.append("\"tokens\":").append(tokens.get()).append(",");
        sb.append("\"lastRefillTime\":").append(lastRefillTime.get());
        sb.append("}");
        return sb.toString();
    }

    public static TokenBucketRateLimiter fromJson(String json) {
        String name = extractJsonString(json, "name");
        long capacity = Long.parseLong(extractJsonValue(json, "capacity"));
        long refillRate = Long.parseLong(extractJsonValue(json, "refillRate"));
        long tokens = Long.parseLong(extractJsonValue(json, "tokens"));
        long lastRefillTime = Long.parseLong(extractJsonValue(json, "lastRefillTime"));
        
        TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(capacity, refillRate);
        rateLimiter.tokens.set(tokens);
        rateLimiter.lastRefillTime.set(lastRefillTime);
        return rateLimiter;
    }

    public static String extractNameFromJson(String json) {
        return extractJsonString(json, "name");
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
}
