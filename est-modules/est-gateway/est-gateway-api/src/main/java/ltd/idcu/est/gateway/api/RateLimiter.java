package ltd.idcu.est.gateway.api;

public interface RateLimiter {
    boolean tryAcquire();
    boolean tryAcquire(int permits);
}
