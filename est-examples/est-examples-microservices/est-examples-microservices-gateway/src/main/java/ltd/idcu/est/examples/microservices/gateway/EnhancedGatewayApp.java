package ltd.idcu.est.examples.microservices.gateway;

import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigEncryptor;
import ltd.idcu.est.config.api.ConfigVersion;
import ltd.idcu.est.config.api.ConfigVersionManager;
import ltd.idcu.est.config.impl.AesConfigEncryptor;
import ltd.idcu.est.config.impl.DefaultConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigVersionManager;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreakerRegistry;
import ltd.idcu.est.gateway.api.CanaryReleaseConfig;
import ltd.idcu.est.gateway.api.WebSocketHandler;
import ltd.idcu.est.gateway.api.WebSocketSession;
import ltd.idcu.est.gateway.impl.Gateway;
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.api.TracerRegistry;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.DefaultTracerRegistry;
import ltd.idcu.est.tracing.impl.FileSpanExporter;

import java.io.File;
import java.util.*;

public class EnhancedGatewayApp {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("  EST Microservices - Enhanced Gateway");
        System.out.println("========================================\n");

        ConfigCenter configCenter = new DefaultConfigCenter();
        ConfigEncryptor encryptor = new AesConfigEncryptor("my-secret-key-123456");
        ConfigVersionManager versionManager = new DefaultConfigVersionManager();

        System.out.println("=== 1. 配置加密示例 ===");
        String plainPassword = "db-password-123";
        String encryptedPassword = encryptor.encrypt(plainPassword);
        System.out.println("明文密码: " + plainPassword);
        System.out.println("加密密码: " + encryptedPassword);
        System.out.println("解密密码: " + encryptor.decrypt(encryptedPassword) + "\n");

        System.out.println("=== 2. 配置版本管理示例 ===");
        Map<String, Object> configV1 = new HashMap<>();
        configV1.put("gateway.port", 8080);
        configV1.put("user.service.url", "http://localhost:8081");
        ConfigVersion v1 = versionManager.createVersion("gateway-config", configV1, "初始配置");
        System.out.println("创建版本: " + v1.getVersionId());

        Map<String, Object> configV2 = new HashMap<>();
        configV2.put("gateway.port", 8080);
        configV2.put("user.service.url", "http://localhost:8081");
        configV2.put("order.service.url", "http://localhost:8082");
        ConfigVersion v2 = versionManager.createVersion("gateway-config", configV2, "添加订单服务");
        System.out.println("创建版本: " + v2.getVersionId());

        List<ConfigVersion> versions = versionManager.listVersions("gateway-config", 0, 10);
        System.out.println("版本列表: " + versions.size() + " 个版本\n");

        System.out.println("=== 3. 配置中心持久化示例 ===");
        configCenter.setProperty("app.name", "est-gateway");
        configCenter.setProperty("app.version", "2.0.0");
        configCenter.setProperty("feature.persistence", "enabled");
        String configPath = System.getProperty("java.io.tmpdir") + "/gateway-config.yaml";
        configCenter.saveToYaml(configPath);
        System.out.println("配置已保存到: " + configPath);
        ConfigCenter restoredConfig = new DefaultConfigCenter();
        restoredConfig.loadFromYaml(configPath);
        System.out.println("从文件恢复的配置: app.name=" + restoredConfig.getProperty("app.name"));
        System.out.println();

        System.out.println("=== 4. 服务发现持久化示例 ===");
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(new ServiceInstance("user-service", "inst-1", "localhost", 8081));
        serviceRegistry.register(new ServiceInstance("order-service", "inst-1", "localhost", 8082));
        String servicePath = System.getProperty("java.io.tmpdir") + "/service-registry.json";
        serviceRegistry.saveToJson(servicePath);
        System.out.println("服务注册表已保存到: " + servicePath);
        ServiceRegistry restoredRegistry = new DefaultServiceRegistry();
        restoredRegistry.loadFromJson(servicePath);
        System.out.println("从文件恢复的服务数量: " + restoredRegistry.getServiceIds().size());
        System.out.println();

        System.out.println("=== 5. 熔断器持久化示例 ===");
        CircuitBreakerRegistry cbRegistry = new DefaultCircuitBreakerRegistry();
        cbRegistry.create("user-service");
        cbRegistry.create("order-service");
        String cbPath = System.getProperty("java.io.tmpdir") + "/circuit-breakers.json";
        ((DefaultCircuitBreakerRegistry) cbRegistry).saveToJson(cbPath);
        System.out.println("熔断器注册表已保存到: " + cbPath);
        CircuitBreakerRegistry restoredCbRegistry = new DefaultCircuitBreakerRegistry();
        ((DefaultCircuitBreakerRegistry) restoredCbRegistry).loadFromJson(cbPath);
        System.out.println("从文件恢复的熔断器数量: " + restoredCbRegistry.getAll().size());
        System.out.println();

        System.out.println("=== 6. 追踪数据持久化示例 ===");
        String tracePath = System.getProperty("java.io.tmpdir") + "/traces.jsonl";
        FileSpanExporter spanExporter = new FileSpanExporter(tracePath);
        TracerRegistry tracerRegistry = new DefaultTracerRegistry();
        Tracer tracer = tracerRegistry.create("gateway-service", spanExporter);
        TraceContext span = tracer.startSpan("api-gateway-request");
        tracer.addTag(span, "path", "/api/users");
        tracer.addTag(span, "method", "GET");
        tracer.endSpan(span, true);
        spanExporter.flush();
        System.out.println("追踪数据已保存到: " + tracePath);
        System.out.println();

        System.out.println("=== 7. 创建增强型网关 ===");
        Gateway gateway = Gateway.create()
            .withLogging()
            .withCors()
            .withRateLimiting(100, 10)
            .withCircuitBreaker("user-service")
            .withCircuitBreaker("order-service")
            .withCanaryRelease()
            .route("/api/users/**", "user-service", "/$1")
            .route("/api/orders/**", "order-service", "/$1")
            .registerService("user-service", "http://localhost:8081")
            .registerService("order-service", "http://localhost:8082")
            .registerService("user-service", "v1", "http://localhost:8081")
            .registerService("user-service", "v2", "http://localhost:8083")
            .addCanaryConfig("user-service", "v1", "v2", 10)
            .webSocketRoute("/ws/chat", new ChatWebSocketHandler());

        CanaryReleaseConfig canaryConfig = new CanaryReleaseConfig("order-service", "v1", "v2", 5);
        Map<String, List<String>> headerMatchers = new HashMap<>();
        headerMatchers.put("X-Canary", Collections.singletonList("true"));
        canaryConfig.setHeaderMatchers(headerMatchers);
        gateway.addCanaryConfig(canaryConfig);

        System.out.println("\nRoutes configured:");
        System.out.println("  /api/users/**  -> user-service  (v1: http://localhost:8081, v2: http://localhost:8083)");
        System.out.println("  /api/orders/** -> order-service (http://localhost:8082)");
        System.out.println("  /ws/chat       -> WebSocket Chat");
        System.out.println("\nCanary Release configured:");
        System.out.println("  user-service: 10% traffic to v2");
        System.out.println("  order-service: Header X-Canary=true routes to v2\n");

        System.out.println("Starting Enhanced API Gateway on port 8080...");
        System.out.println("WebSocket server on port 8081...");
        gateway.start(8080);
        System.out.println("Enhanced API Gateway started on http://localhost:8080\n");

        System.out.println("Available endpoints:");
        System.out.println("  GET    http://localhost:8080/api/users/{id}");
        System.out.println("  GET    http://localhost:8080/api/users");
        System.out.println("  GET    http://localhost:8080/api/orders/{id}");
        System.out.println("  GET    http://localhost:8080/api/orders");
        System.out.println("  POST   http://localhost:8080/api/orders");
        System.out.println("  WS     ws://localhost:8081/ws/chat\n");

        System.out.println("Canary testing:");
        System.out.println("  curl -H \"X-Canary: true\" http://localhost:8080/api/users\n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down Enhanced API Gateway...");
            gateway.stop();
            System.out.println("Enhanced API Gateway stopped.");
        }));
    }

    private static class ChatWebSocketHandler implements WebSocketHandler {
        private final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());

        @Override
        public void onOpen(WebSocketSession session, Map<String, List<String>> headers) {
            System.out.println("[WebSocket] New connection: " + session.getId());
            sessions.add(session);
            try {
                session.sendText("Welcome to EST Chat! There are " + sessions.size() + " users online.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocketSession session, String message) {
            System.out.println("[WebSocket] Message from " + session.getId() + ": " + message);
            String broadcastMessage = "[" + session.getId().substring(0, 8) + "] " + message;
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    try {
                        s.sendText(broadcastMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onBinaryMessage(WebSocketSession session, byte[] message) {
            System.out.println("[WebSocket] Binary message from " + session.getId());
        }

        @Override
        public void onClose(WebSocketSession session, int code, String reason) {
            System.out.println("[WebSocket] Connection closed: " + session.getId() + " (code: " + code + ")");
            sessions.remove(session);
            String leaveMessage = "User " + session.getId().substring(0, 8) + " left. " + sessions.size() + " users online.";
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendText(leaveMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onError(WebSocketSession session, Throwable error) {
            System.err.println("[WebSocket] Error from " + session.getId() + ": " + error.getMessage());
        }
    }
}
