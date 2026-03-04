package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.LoggingMiddleware;
import ltd.idcu.est.web.api.WebApplication;

public class BasicWebAppExample {
    public static void main(String[] args) {
        System.out.println("\n=== Basic Web App Example ===");
        
        WebApplication app = new DefaultWebApplication();
        
        app.use(new LoggingMiddleware());
        
        app.routes(router -> {
            router.get("/", "handler:home");
            router.get("/hello/:name", "handler:hello");
            router.get("/api/user", "handler:apiUser");
        });
        
        app.onStartup(() -> {
            System.out.println("Server started on http://localhost:8080");
            System.out.println("Available routes:");
            System.out.println("  - GET /          - Home page");
            System.out.println("  - GET /hello/:name - Greeting");
            System.out.println("  - GET /api/user  - User API");
            System.out.println("\nPress Ctrl+C to stop the server");
        });
        
        app.run(8080);
    }
    
    public static void run() {
        main(new String[]{});
    }
}
