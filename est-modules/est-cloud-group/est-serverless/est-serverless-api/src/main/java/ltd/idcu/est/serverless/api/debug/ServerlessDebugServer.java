package ltd.idcu.est.serverless.api.debug;

import ltd.idcu.est.serverless.api.ServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ServerlessDebugServer {
    
    private final int port;
    private final Map<String, ServerlessFunction<?, ?>> functions;
    private HttpServer server;
    private ExecutorService executor;
    
    public ServerlessDebugServer(int port) {
        this.port = port;
        this.functions = new HashMap<>();
    }
    
    public ServerlessDebugServer() {
        this(8080);
    }
    
    public void registerFunction(String path, ServerlessFunction<?, ?> function) {
        functions.put(path, function);
    }
    
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        executor = Executors.newFixedThreadPool(10);
        server.setExecutor(executor);
        
        for (Map.Entry<String, ServerlessFunction<?, ?>> entry : functions.entrySet()) {
            server.createContext(entry.getKey(), new FunctionHandler(entry.getValue()));
        }
        
        server.createContext("/health", new HealthHandler());
        server.createContext("/", new RootHandler());
        
        server.start();
        System.out.println("Serverless Debug Server started on port " + port);
        System.out.println("Available endpoints:");
        for (String path : functions.keySet()) {
            System.out.println("  - " + path);
        }
    }
    
    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Serverless Debug Server stopped");
        }
        if (executor != null) {
            executor.shutdown();
        }
    }
    
    private static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "EST Serverless Debug Server\n\n" +
                           "Use /health for health check\n" +
                           "Use registered function paths to test your functions";
            sendResponse(exchange, 200, response);
        }
    }
    
    private static class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "{\"status\":\"ok\",\"timestamp\":" + System.currentTimeMillis() + "}";
            sendResponse(exchange, 200, response, "application/json");
        }
    }
    
    private static class FunctionHandler implements HttpHandler {
        private final ServerlessFunction<?, ?> function;
        
        public FunctionHandler(ServerlessFunction<?, ?> function) {
            this.function = function;
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                
                Map<String, Object> context = new HashMap<>();
                context.put("method", method);
                context.put("path", path);
                context.put("headers", exchange.getRequestHeaders());
                context.put("query", exchange.getRequestURI().getQuery());
                context.put("debug", true);
                context.put("timestamp", System.currentTimeMillis());
                
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                
                @SuppressWarnings("unchecked")
                ServerlessFunction<String, String> stringFunction = (ServerlessFunction<String, String>) function;
                String result = stringFunction.handle(requestBody, context);
                
                sendResponse(exchange, 200, result);
                
            } catch (Exception e) {
                String errorResponse = "{\"error\":\"" + e.getMessage() + "\"}";
                sendResponse(exchange, 500, errorResponse, "application/json");
            }
        }
    }
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        sendResponse(exchange, statusCode, response, "text/plain");
    }
    
    private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
