package ltd.idcu.est.examples.serverless;

import ltd.idcu.est.serverless.api.ColdStartOptimizer;
import ltd.idcu.est.serverless.api.HttpServerlessFunction;
import ltd.idcu.est.serverless.api.ServerlessRequest;
import ltd.idcu.est.serverless.api.ServerlessResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OptimizedFunction implements HttpServerlessFunction {

    private static final String FUNCTION_NAME = "optimized-function";
    private final ColdStartOptimizer optimizer = ColdStartOptimizer.getInstance();
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    @Override
    public void initialize(Map<String, Object> config) {
        System.out.println("[OptimizedFunction] Initializing...");
        
        long startTime = System.currentTimeMillis();
        
        optimizer.recordColdStart(FUNCTION_NAME);
        
        preWarmResources();
        
        long initTime = System.currentTimeMillis() - startTime;
        System.out.println("[OptimizedFunction] Initialized in " + initTime + "ms");
        
        initialized = true;
    }

    private void preWarmResources() {
        System.out.println("[OptimizedFunction] Pre-warming resources...");
        
        optimizer.preWarm(FUNCTION_NAME, () -> {
            cache.put("welcome_message", "Welcome to optimized function!");
            cache.put("initialized_at", System.currentTimeMillis());
            System.out.println("[OptimizedFunction] Resources pre-warmed");
        });
    }

    @Override
    public ServerlessResponse handle(ServerlessRequest request, Map<String, Object> context) {
        if (!initialized) {
            return ServerlessResponse.serverError("{\"error\":\"Function not initialized\"}");
        }

        optimizer.recordWarmStart(FUNCTION_NAME);

        String action = request.getQueryParameters().getOrDefault("action", "status");

        try {
            switch (action.toLowerCase()) {
                case "status":
                    return getStatus();
                case "cache":
                    return getCacheInfo();
                case "stats":
                    return getOptimizerStats();
                case "reset":
                    return resetStats();
                default:
                    return ServerlessResponse.badRequest("{\"error\":\"Unknown action: " + action + "\"}");
            }
        } catch (Exception e) {
            return ServerlessResponse.serverError("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private ServerlessResponse getStatus() {
        String response = String.format(
            "{\"status\":\"ok\",\"function\":\"%s\",\"initialized\":%b,\"cache_size\":%d}",
            FUNCTION_NAME, initialized, cache.size()
        );
        ServerlessResponse res = ServerlessResponse.ok(response);
        res.addHeader("Content-Type", "application/json");
        return res;
    }

    private ServerlessResponse getCacheInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"cache\":{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : cache.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":\"")
              .append(entry.getValue()).append("\"");
            first = false;
        }
        sb.append("}}");
        
        ServerlessResponse res = ServerlessResponse.ok(sb.toString());
        res.addHeader("Content-Type", "application/json");
        return res;
    }

    private ServerlessResponse getOptimizerStats() {
        Map<String, Object> stats = optimizer.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":\"")
              .append(entry.getValue()).append("\"");
            first = false;
        }
        sb.append("}");
        
        ServerlessResponse res = ServerlessResponse.ok(sb.toString());
        res.addHeader("Content-Type", "application/json");
        return res;
    }

    private ServerlessResponse resetStats() {
        optimizer.reset();
        cache.clear();
        String response = "{\"message\":\"Stats and cache reset successfully\"}";
        ServerlessResponse res = ServerlessResponse.ok(response);
        res.addHeader("Content-Type", "application/json");
        return res;
    }

    @Override
    public void destroy() {
        System.out.println("[OptimizedFunction] Destroying...");
        cache.clear();
        initialized = false;
        System.out.println("[OptimizedFunction] Destroyed");
    }
}
