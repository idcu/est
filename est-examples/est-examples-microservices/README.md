# EST Microservices Examples

This is a complete microservices architecture example demonstrating how to use EST Framework to quickly build microservices applications.

## Architecture Overview

```
                    +-------------------------------+
                    |        API Gateway           |
                    |       (port 8080)           |
                    +---------------+---------------+
                                    |
              +---------------------+---------------------+
              |                     |                     |
         +----v----+         +----v----+         +----v----+
         |  User   |         |  Order  |         |  ...    |
         | Service |         | Service |         |  ...    |
         | (8081)  |         | (8082)  |         |  ...    |
         +---------+         +---------+         +---------+
```

## Module Description

### 1. API Gateway (`est-examples-microservices-gateway`)
- Unified entry point, request routing
- CORS support
- Request logging
- Rate limiting protection (token bucket algorithm)
- Circuit breaker protection
- HTTPS/TLS support
- WebSocket support (port 8081)
- Canary release/traffic splitting
- Port: 8080

### 2. User Service (`est-examples-microservices-user`)
- User management service
- REST API: `/users`
- Port: 8081

### 3. Order Service (`est-examples-microservices-order`)
- Order management service
- REST API: `/orders`
- Port: 8082

## Quick Start

### Compile Project

```bash
cd est-examples/est-examples-microservices
mvn clean install
```

### Start Services

**1. Start User Service (Terminal 1):**
```bash
cd est-examples-microservices-user
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
```

**2. Start Order Service (Terminal 2):**
```bash
cd est-examples-microservices-order
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
```

**3. Start API Gateway (Terminal 3):**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
```

**4. Start Enhanced API Gateway (demonstrates new features):**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.EnhancedGatewayApp"
```

## API Testing

### Access through Gateway (Recommended)
```bash
# Get all users
curl http://localhost:8080/api/users

# Get single user
curl http://localhost:8080/api/users/1

# Get all orders
curl http://localhost:8080/api/orders

# Get single order
curl http://localhost:8080/api/orders/1

# Create order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":"1","productName":"iPad","quantity":1,"price":3999}'
```

### Canary Release Testing
```bash
# Normal request (most traffic)
curl http://localhost:8080/api/users

# Force use canary version (through Header)
curl -H "X-Canary: true" http://localhost:8080/api/users
```

### WebSocket Chat Testing

Use browser or WebSocket client to connect:
```
ws://localhost:8081/ws/chat
```

After connecting, you can send messages, which will be broadcast to all online users.

### Directly Access Services (Not through Gateway)
```bash
# User Service
curl http://localhost:8081/users
curl http://localhost:8081/users/1

# Order Service
curl http://localhost:8082/orders
curl http://localhost:8082/orders/1
curl http://localhost:8082/orders/user/1
```

## Enhanced Features Description

### 1. Configuration Encryption

Use AES to encrypt sensitive configuration:
```java
ConfigEncryptor encryptor = new AesConfigEncryptor("my-secret-key");
String encrypted = encryptor.encrypt("db-password-123");
String decrypted = encryptor.decrypt(encrypted);
```

### 2. Configuration Version Management

Supports configuration version creation, listing, and rollback:

```java
ConfigVersionManager versionManager = new DefaultConfigVersionManager();
ConfigVersion v1 = versionManager.createVersion("config-id", configMap, "v1");
ConfigVersion v2 = versionManager.createVersion("config-id", newConfig, "v2");
ConfigVersion rolledBack = versionManager.rollback("config-id", "v1");
```

### 3. WebSocket Support

Supports WebSocket real-time communication:
```java
gateway.webSocketRoute("/ws/chat", new WebSocketHandler() {
    @Override
    public void onOpen(WebSocketSession session, Map<String, List<String>> headers) {
        // Handle connection
    }
    
    @Override
    public void onMessage(WebSocketSession session, String message) {
        // Handle message
    }
});
```

### 4. Canary Release/Traffic Splitting

Supports traffic splitting based on percentage, Header, Cookie, and IP:
```java
gateway.withCanaryRelease()
    .registerService("user-service", "v1", "http://localhost:8081")
    .registerService("user-service", "v2", "http://localhost:8083")
    .addCanaryConfig("user-service", "v1", "v2", 10);

CanaryReleaseConfig config = new CanaryReleaseConfig("service", "v1", "v2", 5);
Map<String, List<String>> headers = new HashMap<>();
headers.put("X-Canary", Collections.singletonList("true"));
config.setHeaderMatchers(headers);
gateway.addCanaryConfig(config);
```

### 5. Configuration Center Persistence

Supports saving configuration to Properties, YAML, and JSON files:
```java
ConfigCenter configCenter = new DefaultConfigCenter();
configCenter.setProperty("app.name", "est-gateway");
configCenter.setProperty("app.version", "2.3.0-SNAPSHOT");

// Save to YAML file
configCenter.saveToYaml("config.yaml");

// Load from YAML file
ConfigCenter restored = new DefaultConfigCenter();
restored.loadFromYaml("config.yaml");

// Auto-save
configCenter.setAutoSavePath("auto-save.yaml");
configCenter.setAutoSave(true);
```

### 6. Service Discovery Persistence

Supports saving service registry to JSON files:
```java
ServiceRegistry registry = new DefaultServiceRegistry();
registry.register(new ServiceInstance("user-service", "inst-1", "localhost", 8081));
registry.register(new ServiceInstance("order-service", "inst-1", "localhost", 8082));

// Save to JSON file
registry.saveToJson("services.json");

// Load from JSON file
ServiceRegistry restored = new DefaultServiceRegistry();
restored.loadFromJson("services.json");

// Auto-save
registry.setAutoSavePath("auto-save-services.json");
registry.setAutoSave(true);
```

### 7. Circuit Breaker State Persistence

Supports saving circuit breaker state to JSON files:
```java
CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
registry.create("user-service");
registry.create("order-service");

// Save to JSON file
((DefaultCircuitBreakerRegistry) registry).saveToJson("circuit-breakers.json");

// Load from JSON file
CircuitBreakerRegistry restored = new DefaultCircuitBreakerRegistry();
((DefaultCircuitBreakerRegistry) restored).loadFromJson("circuit-breakers.json");
```

### 8. Trace Data Persistence

Supports asynchronous batch saving of trace data to files:

```java
FileSpanExporter exporter = new FileSpanExporter("traces.jsonl");
Tracer tracer = new DefaultTracer("gateway-service", exporter);

TraceContext span = tracer.startSpan("api-request");
tracer.addTag(span, "path", "/api/users");
tracer.addTag(span, "method", "GET");
tracer.endSpan(span, true);

exporter.flush();

// Load saved trace data
List<TraceContext> spans = exporter.loadSpans();
```

## Dependency Description

Each microservices module needs to import the following EST modules as needed:
| Module | Usage |
|--------|-------|
| `est-web-impl` | Web framework, provides REST API support |
| `est-gateway` | API Gateway (only used by gateway service) |
| `est-discovery-api/impl` | Service discovery (supports persistence) |
| `est-config-api/impl` | Configuration center (supports encryption, version management, persistence) |
| `est-circuitbreaker-api/impl` | Circuit breaker (supports state persistence) |
| `est-tracing-api/impl` | Distributed tracing (supports data persistence) |
| `est-logging-console` | Console logging |

## Extension Suggestions

To add a new microservice, you only need:
1. Create a new Maven module
2. Import `est-web-impl` dependency
3. Write `@RestController`
4. Start the service
5. Configure routing in gateway

Example:
```java
@RestController
public class ProductController {
    @Get("/products")
    public List<Product> getAllProducts() {
        // ...
    }
}
```
