package com.example.performance;

import ltd.idcu.est.features.performance.api.*;

import java.util.Map;

public class PerformanceExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Framework Performance Optimization Example ===\n");

        System.out.println("1. GC Tuning Demo...");
        GCTuner gcTuner = new GCTuner();
        System.out.println(gcTuner.getJVMInfo());

        System.out.println("   Collecting initial GC metrics...");
        GCMetrics initialMetrics = gcTuner.collectMetrics();
        System.out.println("   Initial: " + initialMetrics);

        System.out.println("\n   Simulating some work...");
        for (int i = 0; i < 10000; i++) {
            byte[] temp = new byte[1024];
        }
        Thread.sleep(100);

        System.out.println("\n   Collecting updated GC metrics...");
        GCMetrics updatedMetrics = gcTuner.collectMetrics();
        System.out.println("   Updated: " + updatedMetrics);

        System.out.println("\n   Getting GC recommendations...");
        GCRecommendation recommendation = gcTuner.getRecommendation(updatedMetrics);
        System.out.println(recommendation);

        System.out.println("\n2. HTTP Server Optimization Demo...");
        System.out.println("   Development configuration:");
        HttpServerOptimizer devOptimizer = HttpServerOptimizer.forDevelopment();
        System.out.println("   " + devOptimizer);

        System.out.println("\n   Production configuration:");
        HttpServerOptimizer prodOptimizer = HttpServerOptimizer.forProduction();
        System.out.println("   " + prodOptimizer);

        System.out.println("\n   Production config map:");
        Map<String, Object> configMap = prodOptimizer.toMap();
        configMap.forEach((k, v) -> System.out.println("   " + k + ": " + v));

        System.out.println("\n3. Request Metrics Demo...");
        RequestMetrics requestMetrics = new RequestMetrics();

        System.out.println("   Simulating HTTP requests...");
        for (int i = 0; i < 100; i++) {
            String path = i % 3 == 0 ? "/api/users" : (i % 3 == 1 ? "/api/orders" : "/api/products");
            int status = i % 20 == 0 ? 500 : (i % 10 == 0 ? 404 : 200);
            long responseTime = (long) (Math.random() * 200 + 20);
            requestMetrics.recordRequest(path, status, responseTime);
        }

        System.out.println("\n   Request metrics:");
        System.out.println("   " + requestMetrics);
        System.out.println("\n   Detailed metrics:");
        System.out.println("   Total requests: " + requestMetrics.getTotalRequests());
        System.out.println("   Success rate: " + String.format("%.1f", requestMetrics.getSuccessRate()) + "%");
        System.out.println("   Avg response time: " + String.format("%.2f", requestMetrics.getAverageResponseTime()) + "ms");
        System.out.println("   Max response time: " + requestMetrics.getMaxResponseTime() + "ms");
        System.out.println("   Min response time: " + requestMetrics.getMinResponseTime() + "ms");
        System.out.println("   Requests/sec: " + String.format("%.2f", requestMetrics.getRequestsPerSecond()));

        System.out.println("\n   Status codes:");
        requestMetrics.getStatusCodes().forEach((code, count) -> 
                System.out.println("   " + code + ": " + count.get()));

        System.out.println("\n   Path counts:");
        requestMetrics.getPathCounts().forEach((path, count) -> 
                System.out.println("   " + path + ": " + count.get()));

        System.out.println("\n4. Custom optimization configuration...");
        HttpServerOptimizer customOptimizer = new HttpServerOptimizer()
                .setBacklog(200)
                .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 8)
                .setUseVirtualThreads(true)
                .setConnectionTimeout(120000)
                .setRequestTimeout(60000)
                .setMaxRequestSize(100 * 1024 * 1024)
                .setEnableCompression(true)
                .setEnableCaching(true)
                .setCacheMaxAge(14400);

        System.out.println("   Custom configuration:");
        System.out.println("   " + customOptimizer);

        System.out.println("\n=== Example Complete ===");
    }
}
