package ltd.idcu.est.monitor.api;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HealthCheckRegistry {
    private final Map<String, HealthCheck> healthChecks = new ConcurrentHashMap<>();
    private final Map<String, HealthCheckResult> lastResults = new ConcurrentHashMap<>();

    public void register(HealthCheck healthCheck) {
        Objects.requireNonNull(healthCheck, "HealthCheck cannot be null");
        healthChecks.put(healthCheck.getName(), healthCheck);
    }

    public void unregister(String name) {
        healthChecks.remove(name);
        lastResults.remove(name);
    }

    public Set<String> getNames() {
        return Collections.unmodifiableSet(healthChecks.keySet());
    }

    public Optional<HealthCheck> getHealthCheck(String name) {
        return Optional.ofNullable(healthChecks.get(name));
    }

    public HealthCheckResult check(String name) {
        HealthCheck healthCheck = healthChecks.get(name);
        if (healthCheck == null) {
            return HealthCheckResult.unknown(name, "Health check not found");
        }

        HealthCheckResult result;
        try {
            HealthStatus status = healthCheck.check();
            result = new HealthCheckResult(
                status, 
                name, 
                status.getMessage(),
                status.getDetails(),
                status.getError()
            );
        } catch (Exception e) {
            result = HealthCheckResult.unhealthy(name, "Health check failed", e);
        }

        lastResults.put(name, result);
        return result;
    }

    public Map<String, HealthCheckResult> checkAll() {
        return healthChecks.keySet().stream()
            .collect(Collectors.toMap(
                name -> name,
                this::check
            ));
    }

    public HealthStatus getAggregateStatus() {
        Map<String, HealthCheckResult> results = checkAll();
        
        boolean hasUnhealthy = results.values().stream()
            .anyMatch(HealthCheckResult::isUnhealthy);
        if (hasUnhealthy) {
            return HealthStatus.unhealthy("One or more health checks failed");
        }

        boolean hasDegraded = results.values().stream()
            .anyMatch(HealthCheckResult::isDegraded);
        if (hasDegraded) {
            return HealthStatus.degraded("One or more health checks are degraded");
        }

        return HealthStatus.healthy("All health checks passed");
    }

    public Optional<HealthCheckResult> getLastResult(String name) {
        return Optional.ofNullable(lastResults.get(name));
    }

    public Map<String, HealthCheckResult> getAllLastResults() {
        return Collections.unmodifiableMap(lastResults);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        HealthStatus aggregateStatus = getAggregateStatus();
        map.put("status", aggregateStatus.getName());
        map.put("timestamp", Instant.now().toString());
        
        Map<String, Object> checks = new LinkedHashMap<>();
        for (Map.Entry<String, HealthCheckResult> entry : checkAll().entrySet()) {
            Map<String, Object> checkMap = new LinkedHashMap<>();
            checkMap.put("status", entry.getValue().getStatus().getName());
            checkMap.put("message", entry.getValue().getMessage());
            checkMap.put("timestamp", entry.getValue().getTimestamp().toString());
            if (entry.getValue().getDetails() != null) {
                checkMap.put("details", entry.getValue().getDetails());
            }
            checks.put(entry.getKey(), checkMap);
        }
        map.put("checks", checks);
        
        return map;
    }

    public void clear() {
        healthChecks.clear();
        lastResults.clear();
    }
}
