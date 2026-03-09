package ltd.idcu.est.ratelimiter.api;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class TokenBucketRateLimiter implements RateLimiter {
    private final String name;
    private final RateLimitConfig config;
    private final AtomicLong tokens;
    private final AtomicLong lastRefillTime;
    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong allowedRequests = new AtomicLong(0);
    private final AtomicLong rejectedRequests = new AtomicLong(0);
    private final AtomicLong totalWaitTime = new AtomicLong(0);
    private final Lock lock = new ReentrantLock();

    public TokenBucketRateLimiter(String name, RateLimitConfig config) {
        this.name = name;
        this.config = config;
        this.tokens = new AtomicLong(config.getBurstCapacity());
        this.lastRefillTime = new AtomicLong(System.nanoTime());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RateLimitConfig getConfig() {
        return config;
    }

    @Override
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    @Override
    public boolean tryAcquire(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("permits must be positive");
        }
        totalRequests.incrementAndGet();
        
        refill();
        
        long currentTokens;
        do {
            currentTokens = tokens.get();
            if (currentTokens < permits) {
                rejectedRequests.incrementAndGet();
                return false;
            }
        } while (!tokens.compareAndSet(currentTokens, currentTokens - permits));
        
        allowedRequests.incrementAndGet();
        return true;
    }

    @Override
    public void acquire() throws InterruptedException {
        acquire(1);
    }

    @Override
    public void acquire(int permits) throws InterruptedException {
        if (permits <= 0) {
            throw new IllegalArgumentException("permits must be positive");
        }
        long startTime = System.nanoTime();
        totalRequests.incrementAndGet();
        
        while (true) {
            refill();
            
            long currentTokens;
            do {
                currentTokens = tokens.get();
                if (currentTokens >= permits) {
                    if (tokens.compareAndSet(currentTokens, currentTokens - permits)) {
                        allowedRequests.incrementAndGet();
                        long waitTime = System.nanoTime() - startTime;
                        totalWaitTime.addAndGet(TimeUnit.NANOSECONDS.toMillis(waitTime));
                        return;
                    }
                }
            } while (currentTokens >= permits);
            
            long waitTime = calculateWaitTime(permits);
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }
        }
    }

    @Override
    public <T> T execute(Supplier<T> supplier) throws RateLimitExceededException {
        if (!tryAcquire()) {
            throw new RateLimitExceededException("Rate limit exceeded for: " + name);
        }
        return supplier.get();
    }

    @Override
    public void execute(Runnable runnable) throws RateLimitExceededException {
        if (!tryAcquire()) {
            throw new RateLimitExceededException("Rate limit exceeded for: " + name);
        }
        runnable.run();
    }

    @Override
    public <T> T execute(Supplier<T> supplier, Supplier<T> fallback) {
        if (tryAcquire()) {
            return supplier.get();
        }
        return fallback.get();
    }

    @Override
    public void execute(Runnable runnable, Runnable fallback) {
        if (tryAcquire()) {
            runnable.run();
        } else {
            fallback.run();
        }
    }

    @Override
    public RateLimitMetrics getMetrics() {
        long total = totalRequests.get();
        long allowed = allowedRequests.get();
        long rejected = rejectedRequests.get();
        double successRate = total == 0 ? 100.0 : (double) allowed / total * 100;
        double avgWaitTime = allowed == 0 ? 0.0 : (double) totalWaitTime.get() / allowed;
        return new RateLimitMetrics(total, allowed, rejected, successRate, avgWaitTime);
    }

    @Override
    public void reset() {
        lock.lock();
        try {
            tokens.set(config.getBurstCapacity());
            lastRefillTime.set(System.nanoTime());
            totalRequests.set(0);
            allowedRequests.set(0);
            rejectedRequests.set(0);
            totalWaitTime.set(0);
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long now = System.nanoTime();
        long lastTime = lastRefillTime.get();
        
        if (now <= lastTime) {
            return;
        }
        
        long elapsedNanos = now - lastTime;
        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        long tokensToAdd = (long) (elapsedSeconds * config.getPermitsPerSecond());
        
        if (tokensToAdd > 0) {
            if (lastRefillTime.compareAndSet(lastTime, now)) {
                long currentTokens;
                do {
                    currentTokens = tokens.get();
                    long newTokens = Math.min(currentTokens + tokensToAdd, config.getBurstCapacity());
                    if (tokens.compareAndSet(currentTokens, newTokens)) {
                        break;
                    }
                } while (true);
            }
        }
    }

    private long calculateWaitTime(int permits) {
        long currentTokens = tokens.get();
        long needed = permits - currentTokens;
        if (needed <= 0) {
            return 0;
        }
        return (long) (needed * 1000.0 / config.getPermitsPerSecond());
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\":\"").append(escapeJson(name)).append("\",");
        sb.append("\"permitsPerSecond\":").append(config.getPermitsPerSecond()).append(",");
        sb.append("\"burstCapacity\":").append(config.getBurstCapacity()).append(",");
        sb.append("\"algorithm\":\"").append(config.getAlgorithm().name()).append("\",");
        sb.append("\"tokens\":").append(tokens.get()).append(",");
        sb.append("\"totalRequests\":").append(totalRequests.get()).append(",");
        sb.append("\"allowedRequests\":").append(allowedRequests.get()).append(",");
        sb.append("\"rejectedRequests\":").append(rejectedRequests.get());
        sb.append("}");
        return sb.toString();
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
}
