package ltd.idcu.est.examples.microservices.gateway;

import ltd.idcu.est.gateway.impl.Gateway;

public class GatewayApp {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Microservices - API Gateway");
        System.out.println("========================================\n");

        Gateway gateway = Gateway.create()
            .withLogging()
            .withCors()
            .route("/api/users/**", "user-service", "/$1")
            .route("/api/orders/**", "order-service", "/$1")
            .registerService("user-service", "http://localhost:8081")
            .registerService("order-service", "http://localhost:8082");

        System.out.println("Routes configured:");
        System.out.println("  /api/users/**  -> user-service  (http://localhost:8081)");
        System.out.println("  /api/orders/** -> order-service (http://localhost:8082)\n");

        System.out.println("Starting API Gateway on port 8080...");
        gateway.start(8080);
        System.out.println("API Gateway started on http://localhost:8080\n");

        System.out.println("Available endpoints:");
        System.out.println("  GET http://localhost:8080/api/users/{id}");
        System.out.println("  GET http://localhost:8080/api/users");
        System.out.println("  GET http://localhost:8080/api/orders/{id}");
        System.out.println("  GET http://localhost:8080/api/orders");
        System.out.println("  POST http://localhost:8080/api/orders\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down API Gateway...");
            gateway.stop();
            System.out.println("API Gateway stopped.");
        }));
    }
}
