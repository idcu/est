package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private final Map<String, Map<String, String>> versionedServiceRegistry;
    private final Map<String, WebSocketHandler> webSocketHandlers;
    private final Map<String, String> webSocketServiceRoutes;
    private CanaryReleaseManager canaryReleaseManager;
    private HttpServer server;
    private ServerSocket webSocketServer;
    private ExecutorService executorService;
    private volatile boolean running;

    public DefaultApiGateway() {
        this.router = new DefaultGatewayRouter();
        this.middlewares = new CopyOnWriteArrayList<>();
        this.httpClient = new HttpClient();
        this.serviceRegistry = new ConcurrentHashMap<>();
        this.versionedServiceRegistry = new ConcurrentHashMap<>();
        this.webSocketHandlers = new ConcurrentHashMap<>();
        this.webSocketServiceRoutes = new ConcurrentHashMap<>();
    }

    public void registerService(String serviceId, String baseUrl) {
        serviceRegistry.put(serviceId, baseUrl);
    }

    @Override
    public void registerService(String serviceId, String version, String baseUrl) {
        versionedServiceRegistry.computeIfAbsent(serviceId, k -> new ConcurrentHashMap<>())
            .put(version, baseUrl);
    }

    @Override
    public void setCanaryReleaseManager(CanaryReleaseManager manager) {
        this.canaryReleaseManager = manager;
    }

    @Override
    public CanaryReleaseManager getCanaryReleaseManager() {
        return this.canaryReleaseManager;
    }

    @Override
    public void start(int port) {
        start(port, null);
    }

    @Override
    public void start(int port, javax.net.ssl.SSLContext sslContext) {
        try {
            this.running = true;
            this.executorService = Executors.newCachedThreadPool();
            
            if (sslContext != null) {
                server = com.sun.net.httpserver.HttpsServer.create(new InetSocketAddress(port), 0);
                ((com.sun.net.httpserver.HttpsServer) server).setHttpsConfigurator(
                    new com.sun.net.httpserver.HttpsConfigurator(sslContext));
            } else {
                server = HttpServer.create(new InetSocketAddress(port), 0);
            }
            server.createContext("/", new GatewayHttpHandler());
            server.setExecutor(executorService);
            server.start();
            
            startWebSocketServer(port + 1);
        } catch (IOException e) {
            throw new RuntimeException("Failed to start gateway server", e);
        }
    }

    private void startWebSocketServer(int port) {
        executorService.submit(() -> {
            try {
                webSocketServer = new ServerSocket(port);
                while (running) {
                    try {
                        Socket clientSocket = webSocketServer.accept();
                        executorService.submit(new WebSocketAcceptor(clientSocket));
                    } catch (IOException e) {
                        if (running) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                if (running) {
                    throw new RuntimeException("Failed to start WebSocket server", e);
                }
            }
        });
    }

    @Override
    public void stop() {
        this.running = false;
        if (server != null) {
            server.stop(0);
            server = null;
        }
        if (webSocketServer != null) {
            try {
                webSocketServer.close();
            } catch (IOException e) {
            }
            webSocketServer = null;
        }
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
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
        middlewares.removeIf(m -> m.getName().equals(name));
    }

    @Override
    public void addWebSocketRoute(String path, WebSocketHandler handler) {
        webSocketHandlers.put(path, handler);
    }

    @Override
    public void addWebSocketRoute(String path, String serviceId) {
        webSocketServiceRoutes.put(path, serviceId);
    }

    @Override
    public void removeWebSocketRoute(String path) {
        webSocketHandlers.remove(path);
        webSocketServiceRoutes.remove(path);
    }

    private class GatewayHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            
            Map<String, String> requestHeaders = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : exchange.getRequestHeaders().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    requestHeaders.put(entry.getKey(), entry.getValue().get(0));
                }
            }
            
            Map<String, String> requestCookies = parseCookies(requestHeaders.get("Cookie"));
            
            byte[] requestBody;
            try (InputStream is = exchange.getRequestBody()) {
                requestBody = is.readAllBytes();
            }
            
            DefaultGatewayContext context = new DefaultGatewayContext(
                requestPath, requestMethod, requestHeaders, requestCookies, requestBody);
            
            Route matchedRoute = router.match(requestPath, requestHeaders, requestCookies);
            context.setMatchedRoute(matchedRoute);
            
            Function<GatewayContext, GatewayContext> pipeline = ctx -> ctx;
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

        private Map<String, String> parseCookies(String cookieHeader) {
            Map<String, String> cookies = new ConcurrentHashMap<>();
            if (cookieHeader != null && !cookieHeader.isEmpty()) {
                String[] pairs = cookieHeader.split(";");
                for (String pair : pairs) {
                    int idx = pair.indexOf('=');
                    if (idx > 0) {
                        String name = pair.substring(0, idx).trim();
                        String value = pair.substring(idx + 1).trim();
                        cookies.put(name, value);
                    }
                }
            }
            return cookies;
        }

        private void forwardRequest(DefaultGatewayContext context, HttpExchange exchange) throws IOException {
            Route route = context.getMatchedRoute();
            String serviceId = route.getServiceId();
            String serviceUrl = null;
            
            Map<String, String> versionedServices = versionedServiceRegistry.get(serviceId);
            if (versionedServices != null && canaryReleaseManager != null) {
                String selectedVersion = canaryReleaseManager.selectTargetVersion(serviceId, context);
                if (selectedVersion != null) {
                    serviceUrl = versionedServices.get(selectedVersion);
                }
            }
            
            if (serviceUrl == null) {
                serviceUrl = serviceRegistry.get(serviceId);
            }
            
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
            if (query != null && !query.isEmpty()) {
                targetUrl += "?" + query;
            }
            
            context.setTargetUrl(targetUrl);
            
            try {
                Map<String, String> rewrittenHeaders = route.rewriteHeaders(context.getRequestHeaders());
                
                HttpClient.HttpResponse response = httpClient.execute(
                    context.getRequestMethod(),
                    targetUrl,
                    rewrittenHeaders,
                    context.getRequestBody()
                );
                
                Map<String, String> rewrittenResponseHeaders = route.rewriteResponseHeaders(response.getHeaders());
                
                context.setResponseStatus(response.getStatusCode());
                context.setResponseHeaders(rewrittenResponseHeaders);
                context.setResponseBody(response.getBody());
                
                for (Map.Entry<String, String> header : rewrittenResponseHeaders.entrySet()) {
                    if (!header.getKey().equalsIgnoreCase("Transfer-Encoding") &&
                        !header.getKey().equalsIgnoreCase("Content-Length") &&
                        !header.getKey().equalsIgnoreCase("Connection")) {
                        exchange.getResponseHeaders().add(header.getKey(), header.getValue());
                    }
                }
                
                byte[] responseBody = response.getBody();
                exchange.sendResponseHeaders(response.getStatusCode(), responseBody.length);
                if (responseBody.length > 0) {
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

    private class WebSocketAcceptor implements Runnable {
        private final Socket clientSocket;

        public WebSocketAcceptor(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                Map<String, String> headers = new HashMap<>();
                String path = null;

                String line = reader.readLine();
                if (line != null && line.startsWith("GET")) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 2) {
                        path = parts[1];
                    }

                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        int colonIndex = line.indexOf(':');
                        if (colonIndex > 0) {
                            String key = line.substring(0, colonIndex).trim();
                            String value = line.substring(colonIndex + 1).trim();
                            headers.put(key, value);
                        }
                    }
                }

                String upgrade = headers.get("Upgrade");
                String connection = headers.get("Connection");
                String secWebSocketKey = headers.get("Sec-WebSocket-Key");

                if (path != null && "websocket".equalsIgnoreCase(upgrade) &&
                    connection != null && connection.toLowerCase().contains("upgrade") &&
                    secWebSocketKey != null) {

                    WebSocketHandler handler = webSocketHandlers.get(path);
                    String serviceId = webSocketServiceRoutes.get(path);

                    if (handler != null || serviceId != null) {
                        String acceptKey = DefaultWebSocketSession.generateAcceptKey(secWebSocketKey);
                        
                        OutputStream os = clientSocket.getOutputStream();
                        String response = "HTTP/1.1 101 Switching Protocols\r\n" +
                            "Upgrade: websocket\r\n" +
                            "Connection: Upgrade\r\n" +
                            "Sec-WebSocket-Accept: " + acceptKey + "\r\n\r\n";
                        os.write(response.getBytes(StandardCharsets.UTF_8));
                        os.flush();

                        DefaultWebSocketSession session = new DefaultWebSocketSession(
                            UUID.randomUUID().toString(), clientSocket);

                        if (handler != null) {
                            Map<String, List<String>> headerMap = new HashMap<>();
                            for (Map.Entry<String, String> entry : headers.entrySet()) {
                                headerMap.put(entry.getKey(), Collections.singletonList(entry.getValue()));
                            }
                            new Thread(new WebSocketHandlerAdapter(session, handler, headerMap)).start();
                        } else {
                            String serviceUrl = serviceRegistry.get(serviceId);
                            if (serviceUrl != null) {
                                new Thread(new WebSocketProxyHandler(session, serviceUrl)).start();
                            } else {
                                session.close(1011, "Service not available");
                            }
                        }
                    } else {
                        sendError(clientSocket, 404, "Not Found");
                    }
                } else {
                    sendError(clientSocket, 400, "Bad Request");
                }
            } catch (Exception e) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                }
            }
        }

        private void sendError(Socket socket, int statusCode, String message) throws IOException {
            String response = "HTTP/1.1 " + statusCode + " " + message + "\r\n" +
                "Content-Type: text/plain; charset=UTF-8\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
            socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }
    }

    private class WebSocketProxyHandler implements Runnable {
        private final DefaultWebSocketSession clientSession;
        private final String serviceUrl;
        private Socket backendSocket;
        private DefaultWebSocketSession backendSession;

        public WebSocketProxyHandler(DefaultWebSocketSession clientSession, String serviceUrl) {
            this.clientSession = clientSession;
            this.serviceUrl = serviceUrl;
        }

        @Override
        public void run() {
            try {
                String host = serviceUrl.replace("http://", "").replace("https://", "");
                int port = host.contains(":") ? Integer.parseInt(host.split(":")[1]) : 80;
                host = host.split(":")[0];

                backendSocket = new Socket(host, port);
                OutputStream backendOs = backendSocket.getOutputStream();
                
                String upgradeRequest = "GET / HTTP/1.1\r\n" +
                    "Host: " + host + "\r\n" +
                    "Upgrade: websocket\r\n" +
                    "Connection: Upgrade\r\n" +
                    "Sec-WebSocket-Key: " + Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()) + "\r\n" +
                    "Sec-WebSocket-Version: 13\r\n\r\n";
                backendOs.write(upgradeRequest.getBytes(StandardCharsets.UTF_8));
                backendOs.flush();

                backendSession = new DefaultWebSocketSession(UUID.randomUUID().toString(), backendSocket);

                Thread clientToBackend = new Thread(() -> {
                    try {
                        while (clientSession.isOpen() && backendSession.isOpen()) {
                            DataInputStream in = clientSession.getInputStream();
                            int b1 = in.readUnsignedByte();
                            int opcode = b1 & 0x0F;
                            boolean fin = (b1 & 0x80) != 0;
                            int b2 = in.readUnsignedByte();
                            boolean masked = (b2 & 0x80) != 0;
                            int length = b2 & 0x7F;
                            if (length == 126) {
                                length = in.readUnsignedShort();
                            } else if (length == 127) {
                                length = (int) in.readLong();
                            }
                            byte[] mask = new byte[4];
                            if (masked) {
                                in.readFully(mask);
                            }
                            byte[] payload = new byte[length];
                            in.readFully(payload);
                            if (masked) {
                                for (int i = 0; i < payload.length; i++) {
                                    payload[i] ^= mask[i % 4];
                                }
                            }
                            backendSession.sendFrame(opcode, payload);
                        }
                    } catch (IOException e) {
                    }
                });

                Thread backendToClient = new Thread(() -> {
                    try {
                        while (clientSession.isOpen() && backendSession.isOpen()) {
                            DataInputStream in = backendSession.getInputStream();
                            int b1 = in.readUnsignedByte();
                            int opcode = b1 & 0x0F;
                            boolean fin = (b1 & 0x80) != 0;
                            int b2 = in.readUnsignedByte();
                            int length = b2 & 0x7F;
                            if (length == 126) {
                                length = in.readUnsignedShort();
                            } else if (length == 127) {
                                length = (int) in.readLong();
                            }
                            byte[] payload = new byte[length];
                            in.readFully(payload);
                            clientSession.sendFrame(opcode, payload);
                        }
                    } catch (IOException e) {
                    }
                });

                clientToBackend.start();
                backendToClient.start();
                clientToBackend.join();
                backendToClient.join();
            } catch (Exception e) {
            } finally {
                try {
                    if (clientSession.isOpen()) clientSession.close();
                    if (backendSession != null && backendSession.isOpen()) backendSession.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
