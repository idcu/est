package ltd.idcu.est.examples.microservices.order;

import ltd.idcu.est.web.DefaultWebApplication;

public class OrderServiceApp {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Microservices - Order Service");
        System.out.println("========================================\n");

        DefaultWebApplication app = new DefaultWebApplication();
        
        System.out.println("Starting Order Service on port 8082...");
        app.start(8082);
        System.out.println("Order Service started on http://localhost:8082\n");

        System.out.println("Available endpoints:");
        System.out.println("  GET  http://localhost:8082/orders");
        System.out.println("  GET  http://localhost:8082/orders/{id}");
        System.out.println("  GET  http://localhost:8082/orders/user/{userId}");
        System.out.println("  POST http://localhost:8082/orders\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -&gt; {
            System.out.println("\nShutting down Order Service...");
            app.stop();
            System.out.println("Order Service stopped.");
        }));
    }
}
