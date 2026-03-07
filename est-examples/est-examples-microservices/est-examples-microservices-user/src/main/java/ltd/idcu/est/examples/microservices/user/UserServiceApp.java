package ltd.idcu.est.examples.microservices.user;

import ltd.idcu.est.web.DefaultWebApplication;

public class UserServiceApp {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Microservices - User Service");
        System.out.println("========================================\n");

        DefaultWebApplication app = new DefaultWebApplication();
        
        System.out.println("Starting User Service on port 8081...");
        app.start(8081);
        System.out.println("User Service started on http://localhost:8081\n");

        System.out.println("Available endpoints:");
        System.out.println("  GET  http://localhost:8081/users");
        System.out.println("  GET  http://localhost:8081/users/{id}");
        System.out.println("  POST http://localhost:8081/users\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down User Service...");
            app.stop();
            System.out.println("User Service stopped.");
        }));
    }
}
