package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.DefaultCorsMiddleware;
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.LoggingMiddleware;
import ltd.idcu.est.web.api.WebApplication;

import java.util.List;
import java.util.Map;

public class EnhancedWebExample {
    
    public static void main(String[] args) {
        System.out.println("\n=== Enhanced Web Examples ===");
        
        WebApplication app = new DefaultWebApplication("Enhanced Web App", "1.0.0");
        
        app.use(new LoggingMiddleware());
        
        corsExample(app);
        staticFilesExample(app);
        sessionExample(app);
        errorHandlingExample(app);
        
        app.onStartup(() -> {
            System.out.println("\nServer started on http://localhost:8080");
            System.out.println("Available routes:");
            System.out.println("  - GET /                    - Home page");
            System.out.println("  - GET /api/data           - CORS enabled API");
            System.out.println("  - GET /session/counter    - Session counter");
            System.out.println("  - GET /error              - Test error handling");
            System.out.println("  - Static files at /static/ - Served from 'static' directory");
            System.out.println("\nPress Ctrl+C to stop the server");
        });
        
        app.run(8080);
    }
    
    private static void corsExample(WebApplication app) {
        System.out.println("Setting up CORS example...");
        
        DefaultCorsMiddleware corsMiddleware = new DefaultCorsMiddleware();
        corsMiddleware.setAllowedOrigins(List.of("*"));
        corsMiddleware.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsMiddleware.setAllowedHeaders(List.of("*"));
        corsMiddleware.setAllowCredentials(true);
        corsMiddleware.setMaxAge(3600);
        
        app.enableCors(corsMiddleware);
        
        app.get("/api/data", (req, res) -> {
            res.json(Map.of(
                "message", "This is a CORS-enabled API endpoint",
                "timestamp", System.currentTimeMillis(),
                "data", List.of("item1", "item2", "item3")
            ));
        });
    }
    
    private static void staticFilesExample(WebApplication app) {
        System.out.println("Setting up static files example...");
        
        app.staticFiles("/static", "static");
        
        app.get("/", (req, res) -> {
            res.send("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>EST Web Examples</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 800px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .route { background: #f5f5f5; padding: 10px; margin: 10px 0; border-radius: 5px; }
                        .route code { background: #e0e0e0; padding: 2px 6px; border-radius: 3px; }
                    </style>
                </head>
                <body>
                    <h1>EST Web Framework Examples</h1>
                    <h2>Available Routes:</h2>
                    <div class="route">
                        <code>GET /</code> - This page
                    </div>
                    <div class="route">
                        <code>GET /api/data</code> - CORS enabled API (JSON)
                    </div>
                    <div class="route">
                        <code>GET /session/counter</code> - Session-based counter
                    </div>
                    <div class="route">
                        <code>GET /error</code> - Test error handling
                    </div>
                    <div class="route">
                        <code>GET /static/*</code> - Static files (try /static/index.html if exists)
                    </div>
                </body>
                </html>
                """);
        });
    }
    
    private static void sessionExample(WebApplication app) {
        System.out.println("Setting up session example...");
        
        app.get("/session/counter", (req, res) -> {
            Integer count = (Integer) req.session().getAttribute("counter");
            if (count == null) {
                count = 0;
            }
            count++;
            req.session().setAttribute("counter", count);
            
            res.json(Map.of(
                "sessionId", req.session().getId(),
                "counter", count,
                "message", "Refresh to increment the counter"
            ));
        });
    }
    
    private static void errorHandlingExample(WebApplication app) {
        System.out.println("Setting up error handling example...");
        
        app.exceptionHandler(IllegalArgumentException.class, e -> {
            System.err.println("Caught IllegalArgumentException: " + e.getMessage());
        });
        
        app.errorHandler(e -> {
            System.err.println("Global error handler caught: " + e.getClass().getName() + ": " + e.getMessage());
        });
        
        app.get("/error", (req, res) -> {
            throw new RuntimeException("This is a test error!");
        });
        
        app.get("/error/illegal", (req, res) -> {
            throw new IllegalArgumentException("Invalid argument provided!");
        });
    }
    
    public static void run() {
        main(new String[]{});
    }
}
