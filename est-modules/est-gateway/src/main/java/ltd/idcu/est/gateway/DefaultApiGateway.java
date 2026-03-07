package ltd.idcu.est.gateway;

import ltd.idcu.est.gateway.api.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class DefaultApiGateway implements ApiGateway {
    private final GatewayRouter router;
    private final List<GatewayMiddleware> middlewares;
    private final HttpClient httpClient;
    private final Map<String, String> serviceRegistry;
    private HttpServer server;

    public DefaultApiGateway() {
        this.router = new DefaultGatewayRouter();
        this.middlewares = new CopyOnWriteArrayList<>();
        this.httpClient = new HttpClient();
        this.serviceRegistry = new ConcurrentHashMap<>();
    }

    public void registerService(String serviceId, String baseUrl) {
        serviceRegistry.put(serviceId, baseUrl);
    }

    @Override
    public void start(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new GatewayHttpHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start gateway server", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    @Override
    public GatewayRouter getRouter() {
        return router;
    }

    @Override
    public void addMiddleware(GatewayMiddleware middleware) {
        middlewares.add(middleware);
        middlewares.sort(Comparator.comparingInt(GatewayMiddleware::getOrder));
    }

    @Override
    public void removeMiddleware(String name) {
        middlewares.removeIf(m -&gt; m.getName().equals(name));
    }

    private class GatewayHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            
            Map&lt;String, String&gt; requestHeaders = new HashMap&lt;&gt;();
            for (Map.Entry&lt;String, List&lt;String&gt;&gt; entry : exchange.getRequestHeaders().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    requestHeaders.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            
            byte[] requestBody;
            try (InputStream is = exchange.getRequestBody()) {
                requestBody = is.readAllBytes();
            }
            
            DefaultGatewayContext context = new DefaultGatewayContext(
                requestPath, requestMethod, requestHeaders, requestBody);
            
            Route matchedRoute = router.match(requestPath);
            context.setMatchedRoute(matchedRoute);
            
            Function&lt;GatewayContext, GatewayContext&gt; pipeline = ctx -&gt; ctx;
            for (GatewayMiddleware middleware : middlewares) {
                pipeline = pipeline.andThen(middleware.process());
            }
            context = (DefaultGatewayContext) pipeline.apply(context);
            
            if (matchedRoute != null) {
                forwardRequest(context, exchange);
            } else {
                sendError(exchange, 404, "Not Found: " + requestPath);
            }
        }

        private void forwardRequest(DefaultGatewayContext context, HttpExchange exchange) throws IOException {
            Route route = context.getMatchedRoute();
            String serviceId = route.getServiceId();
            String serviceUrl = serviceRegistry.get(serviceId);
            
            if (serviceUrl == null) {
                sendError(exchange, 503, "Service not available: " + serviceId);
                return;
            }
            
            String rewrittenPath = route.rewritePath(context.getRequestPath());
            if (!rewrittenPath.startsWith("/")) {
                rewrittenPath = "/" + rewrittenPath;
            }
            String targetUrl = serviceUrl + rewrittenPath;
            
            String query = exchange.getRequestURI().getQuery();
            if (query != null &amp;&amp; !query.isEmpty()) {
                targetUrl += "?" + query;
            }
            
            context.setTargetUrl(targetUrl);
            
            try {
                HttpClient.HttpResponse response = httpClient.execute(
                    context.getRequestMethod(),
                    targetUrl,
                    context.getRequestHeaders(),
                    context.getRequestBody()
                );
                
                context.setResponseStatus(response.getStatusCode());
                context.setResponseHeaders(response.getHeaders());
                context.setResponseBody(response.getBody());
                
                for (Map.Entry&lt;String, String&gt; header : response.getHeaders().entrySet()) {
                    if (!header.getKey().equalsIgnoreCase("Transfer-Encoding") &amp;&amp;
                        !header.getKey().equalsIgnoreCase("Content-Length") &amp;&amp;
                        !header.getKey().equalsIgnoreCase("Connection")) {
                        exchange.getResponseHeaders().add(header.getKey(), header.getValue());
                    }
                }
                
                byte[] responseBody = response.getBody();
                exchange.sendResponseHeaders(response.getStatusCode(), responseBody.length);
                if (responseBody.length &gt; 0) {
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(responseBody);
                    }
                }
            } catch (Exception e) {
                sendError(exchange, 502, "Gateway Error: " + e.getMessage());
            }
        }

        private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            byte[] body = message.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        }
    }
}
