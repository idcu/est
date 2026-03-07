package ltd.idcu.est.examples.features;

import ltd.idcu.est.config.api.*;
import ltd.idcu.est.circuitbreaker.api.*;
import ltd.idcu.est.discovery.api.*;

import java.util.Optional;

public class MicroservicesExample {
    public static void main(String[] args) {
        System.out.println("=== EST Framework Microservices Features Example ===\n");

        System.out.println("1. Setting up Config Center...");
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("app.name", "est-microservice");
        configCenter.setProperty("app.version", "1.0.0");
        configCenter.setProperty("service.timeout", 5000);
        configCenter.setProperty("circuit.failure.threshold", 3);
        configCenter.setProperty("circuit.wait.duration", 10000);
        
        configCenter.addChangeListener(event -> {
            System.out.println("   [Config Changed] " + event.getKey() + ": " + 
                             event.getOldValue() + " -> " + event.getNewValue() + 
                             " (" + event.getChangeType() + ")");
        });
        
        System.out.println("   App Name: " + configCenter.getString("app.name", "unknown"));
        System.out.println("   Timeout: " + configCenter.getInt("service.timeout", 1000) + "ms\n");

        System.out.println("2. Setting up Service Discovery...");
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        
        ServiceInstance orderService1 = new ServiceInstance("order-service", "order-1", "192.168.1.10", 8080)
                .addMetadata("version", "1.0.0")
                .addMetadata("region", "us-east-1");
        ServiceInstance orderService2 = new ServiceInstance("order-service", "order-2", "192.168.1.11", 8080)
                .addMetadata("version", "1.0.0")
                .addMetadata("region", "us-east-1");
        ServiceInstance userService = new ServiceInstance("user-service", "user-1", "192.168.1.20", 8081)
                .addMetadata("version", "2.0.0")
                .addMetadata("region", "us-east-1");
        
        serviceRegistry.register(orderService1);
        serviceRegistry.register(orderService2);
        serviceRegistry.register(userService);
        
        System.out.println("   Registered services: " + serviceRegistry.getServiceIds());
        
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer(serviceRegistry);
        System.out.println("   Using Round Robin Load Balancer\n");

        System.out.println("3. Setting up Circuit Breakers...");
        CircuitBreakerConfig cbConfig = new CircuitBreakerConfig(
                configCenter.getInt("circuit.failure.threshold", 5),
                configCenter.getLong("circuit.wait.duration", 30000),
                2,
                5000
        );
        
        CircuitBreakerRegistry circuitBreakerRegistry = new CircuitBreakerRegistry();
        CircuitBreaker orderServiceCb = circuitBreakerRegistry.create("order-service", cbConfig);
        CircuitBreaker userServiceCb = circuitBreakerRegistry.create("user-service", cbConfig);
        
        System.out.println("   Created circuit breakers: " + circuitBreakerRegistry.getAll().keySet() + "\n");

        System.out.println("4. Demonstrating service calls with circuit breaker...");
        for (int i = 0; i < 5; i++) {
            final int attempt = i;
            try {
                Optional<ServiceInstance> instance = loadBalancer.choose("order-service");
                if (instance.isPresent()) {
                    String result = orderServiceCb.execute(() -> {
                        if (attempt < 3) {
                            return "Success from " + instance.get().getUri();
                        } else {
                            throw new RuntimeException("Service unavailable");
                        }
                    });
                    System.out.println("   Attempt " + (i + 1) + ": " + result);
                }
            } catch (Exception e) {
                System.out.println("   Attempt " + (i + 1) + ": Failed - " + e.getMessage());
            }
        }
        
        System.out.println();
        System.out.println("5. Circuit breaker metrics:");
        CircuitBreakerMetrics metrics = orderServiceCb.getMetrics();
        System.out.println("   State: " + orderServiceCb.getState());
        System.out.println("   Success: " + metrics.getSuccessCount());
        System.out.println("   Failures: " + metrics.getFailureCount());
        System.out.println("   Failure Rate: " + String.format("%.2f%%", metrics.getFailureRate() * 100));
        System.out.println("   Rejected: " + metrics.getRejectedRequests());
        
        System.out.println();
        System.out.println("6. Updating configuration dynamically...");
        configCenter.setProperty("service.timeout", 10000);
        configCenter.setProperty("feature.new-feature", true);
        
        System.out.println();
        System.out.println("7. Final configuration:");
        configCenter.getAllProperties().forEach((k, v) -> 
                System.out.println("   " + k + " = " + v));
        
        System.out.println();
        System.out.println("=== Example Complete ===");
    }
}
