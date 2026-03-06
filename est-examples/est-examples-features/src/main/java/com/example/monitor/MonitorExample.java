package com.example.monitor;

import ltd.idcu.est.features.monitor.api.*;

import java.util.Map;

public class MonitorExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Framework Monitor Example ===\n");

        HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();
        Metrics metrics = new DefaultMetrics();

        System.out.println("1. Registering health checks...");
        healthCheckRegistry.register(new MemoryHealthCheck());
        healthCheckRegistry.register(new ThreadHealthCheck());
        healthCheckRegistry.register(new DiskSpaceHealthCheck());
        System.out.println("   Registered health checks: " + healthCheckRegistry.getNames() + "\n");

        System.out.println("2. Recording metrics...");
        for (int i = 0; i < 100; i++) {
            metrics.incrementCounter("requests_total");
            metrics.recordHistogram("response_time_ms", (long) (Math.random() * 500 + 50));
            metrics.recordTimer("api_latency_ms", (long) (Math.random() * 300 + 100));
            if (i % 10 == 0) {
                metrics.recordGauge("active_connections", (long) (Math.random() * 50));
            }
        }
        System.out.println("   Metrics recorded\n");

        System.out.println("3. Running health checks...");
        Map<String, HealthCheckResult> healthResults = healthCheckRegistry.checkAll();
        for (Map.Entry<String, HealthCheckResult> entry : healthResults.entrySet()) {
            HealthCheckResult result = entry.getValue();
            System.out.println("   [" + entry.getKey() + "] " + result.getStatus().getName() + 
                             " - " + result.getMessage());
        }
        System.out.println();

        System.out.println("4. Aggregate health status:");
        HealthStatus aggregateStatus = healthCheckRegistry.getAggregateStatus();
        System.out.println("   Status: " + aggregateStatus.getName());
        System.out.println("   Message: " + aggregateStatus.getMessage() + "\n");

        System.out.println("5. Metrics overview:");
        System.out.println("   Total requests: " + metrics.getCounter("requests_total"));
        System.out.println("   Active connections: " + metrics.getMetric("active_connections"));
        System.out.println("   Response time p50: " + metrics.getHistogramPercentile("response_time_ms", 0.5) + "ms");
        System.out.println("   Response time p95: " + metrics.getHistogramPercentile("response_time_ms", 0.95) + "ms");
        System.out.println();

        System.out.println("6. Health check JSON (simplified):");
        Map<String, Object> healthMap = healthCheckRegistry.toMap();
        System.out.println("   " + healthMap);
        System.out.println();

        System.out.println("7. Metrics JSON (simplified):");
        Map<String, Object> metricsMap = ((DefaultMetrics) metrics).toMap();
        System.out.println("   " + metricsMap);
        System.out.println();

        System.out.println("=== Example Complete ===");
    }
}
