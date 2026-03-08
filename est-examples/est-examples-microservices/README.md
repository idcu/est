# EST 寰湇鍔＄ず渚?杩欐槸涓€涓畬鏁寸殑寰湇鍔℃灦鏋勭ず渚嬶紝灞曠ず浜嗗浣曚娇鐢?EST Framework 蹇€熸瀯寤哄井鏈嶅姟搴旂敤銆?
## 鏋舵瀯姒傝堪

```
                    鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?                    鈹? API Gateway   鈹?                    鈹? (port 8080)   鈹?                    鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?                             鈹?              鈹屸攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹尖攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?              鈹?             鈹?             鈹?         鈹屸攢鈹€鈹€鈹€鈻尖攢鈹€鈹€鈹€鈹? 鈹屸攢鈹€鈹€鈹€鈻尖攢鈹€鈹€鈹€鈹? 鈹屸攢鈹€鈹€鈹€鈻尖攢鈹€鈹€鈹€鈹?         鈹俇ser    鈹? 鈹侽rder   鈹? 鈹?...    鈹?         鈹係ervice 鈹? 鈹係ervice 鈹? 鈹?       鈹?         鈹?081)   鈹? 鈹?082)   鈹? 鈹?       鈹?         鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹? 鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹? 鈹斺攢鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹€鈹?```

## 妯″潡璇存槑

### 1. API Gateway (`est-examples-microservices-gateway`)
- 缁熶竴鍏ュ彛锛岃矾鐢辫浆鍙?- CORS 鏀寔
- 璇锋眰鏃ュ織
- 闄愭祦淇濇姢锛堜护鐗屾《绠楁硶锛?- 鐔旀柇淇濇姢
- HTTPS/TLS 鏀寔
- WebSocket 鏀寔锛堢鍙?8081锛?- 閲戜笣闆€鍙戝竷/娴侀噺鍒囧垎
- 绔彛锛?080

### 2. User Service (`est-examples-microservices-user`)
- 鐢ㄦ埛绠＄悊鏈嶅姟
- REST API锛歚/users`
- 绔彛锛?081

### 3. Order Service (`est-examples-microservices-order`)
- 璁㈠崟绠＄悊鏈嶅姟
- REST API锛歚/orders`
- 绔彛锛?082

## 蹇€熷紑濮?### 缂栬瘧椤圭洰

```bash
cd est-examples/est-examples-microservices
mvn clean install
```

### 鍚姩鏈嶅姟

**1. 鍚姩 User Service锛堢粓绔?1锛夛細**
```bash
cd est-examples-microservices-user
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.user.UserServiceApp"
```

**2. 鍚姩 Order Service锛堢粓绔?2锛夛細**
```bash
cd est-examples-microservices-order
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.order.OrderServiceApp"
```

**3. 鍚姩 API Gateway锛堢粓绔?3锛夛細**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.GatewayApp"
```

**4. 鍚姩澧炲己鐗?API Gateway锛堝睍绀烘柊鍔熻兘锛夛細**
```bash
cd est-examples-microservices-gateway
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.microservices.gateway.EnhancedGatewayApp"
```

## API 娴嬭瘯

### 閫氳繃缃戝叧璁块棶锛堟帹鑽愶級

```bash
# 鑾峰彇鎵€鏈夌敤鎴?curl http://localhost:8080/api/users

# 鑾峰彇鍗曚釜鐢ㄦ埛
curl http://localhost:8080/api/users/1

# 鑾峰彇鎵€鏈夎鍗?curl http://localhost:8080/api/orders

# 鑾峰彇鍗曚釜璁㈠崟
curl http://localhost:8080/api/orders/1

# 鍒涘缓璁㈠崟
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":"1","productName":"iPad","quantity":1,"price":3999}'
```

### 閲戜笣闆€鍙戝竷娴嬭瘯

```bash
# 姝ｅ父璇锋眰锛堝ぇ閮ㄥ垎娴侀噺锛?curl http://localhost:8080/api/users

# 寮哄埗浣跨敤閲戜笣闆€鐗堟湰锛堥€氳繃 Header锛?curl -H "X-Canary: true" http://localhost:8080/api/users
```

### WebSocket 鑱婂ぉ娴嬭瘯

浣跨敤娴忚鍣ㄦ垨 WebSocket 瀹㈡埛绔繛鎺ワ細
```
ws://localhost:8081/ws/chat
```

杩炴帴鍚庡彲浠ュ彂閫佹秷鎭紝娑堟伅浼氬箍鎾粰鎵€鏈夊湪绾跨敤鎴枫€?
### 鐩存帴璁块棶鏈嶅姟锛堜笉閫氳繃缃戝叧锛?```bash
# User Service
curl http://localhost:8081/users
curl http://localhost:8081/users/1

# Order Service
curl http://localhost:8082/orders
curl http://localhost:8082/orders/1
curl http://localhost:8082/orders/user/1
```

## 澧炲己鍔熻兘璇存槑

### 1. 閰嶇疆鍔犲瘑

浣跨敤 AES 鍔犲瘑鏁忔劅閰嶇疆锛?
```java
ConfigEncryptor encryptor = new AesConfigEncryptor("my-secret-key");
String encrypted = encryptor.encrypt("db-password-123");
String decrypted = encryptor.decrypt(encrypted);
```

### 2. 閰嶇疆鐗堟湰绠＄悊

鏀寔閰嶇疆鐗堟湰鍒涘缓銆佸垪琛ㄣ€佸洖婊氾細

```java
ConfigVersionManager versionManager = new DefaultConfigVersionManager();
ConfigVersion v1 = versionManager.createVersion("config-id", configMap, "v1");
ConfigVersion v2 = versionManager.createVersion("config-id", newConfig, "v2");
ConfigVersion rolledBack = versionManager.rollback("config-id", "v1");
```

### 3. WebSocket 鏀寔

鏀寔 WebSocket 瀹炴椂閫氫俊锛?
```java
gateway.webSocketRoute("/ws/chat", new WebSocketHandler() {
    @Override
    public void onOpen(WebSocketSession session, Map<String, List<String>> headers) {
        // 澶勭悊杩炴帴
    }
    
    @Override
    public void onMessage(WebSocketSession session, String message) {
        // 澶勭悊娑堟伅
    }
});
```

### 4. 閲戜笣闆€鍙戝竷/娴侀噺鍒囧垎

鏀寔鍩轰簬鐧惧垎姣斻€丠eader銆丆ookie銆両P 鐨勬祦閲忓垏鍒嗭細

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

### 5. 閰嶇疆涓績鎸佷箙鍖?
鏀寔閰嶇疆淇濆瓨鍒?Properties銆乊AML銆丣SON 鏂囦欢锛?
```java
ConfigCenter configCenter = new DefaultConfigCenter();
configCenter.setProperty("app.name", "est-gateway");
configCenter.setProperty("app.version", "2.1.0");

// 淇濆瓨鍒?YAML 鏂囦欢
configCenter.saveToYaml("config.yaml");

// 浠?YAML 鏂囦欢鍔犺浇
ConfigCenter restored = new DefaultConfigCenter();
restored.loadFromYaml("config.yaml");

// 鑷姩淇濆瓨
configCenter.setAutoSavePath("auto-save.yaml");
configCenter.setAutoSave(true);
```

### 6. 鏈嶅姟鍙戠幇鎸佷箙鍖?
鏀寔鏈嶅姟娉ㄥ唽琛ㄤ繚瀛樺埌 JSON 鏂囦欢锛?
```java
ServiceRegistry registry = new DefaultServiceRegistry();
registry.register(new ServiceInstance("user-service", "inst-1", "localhost", 8081));
registry.register(new ServiceInstance("order-service", "inst-1", "localhost", 8082));

// 淇濆瓨鍒?JSON 鏂囦欢
registry.saveToJson("services.json");

// 浠?JSON 鏂囦欢鍔犺浇
ServiceRegistry restored = new DefaultServiceRegistry();
restored.loadFromJson("services.json");

// 鑷姩淇濆瓨
registry.setAutoSavePath("auto-save-services.json");
registry.setAutoSave(true);
```

### 7. 鐔旀柇鍣ㄧ姸鎬佹寔涔呭寲

鏀寔鐔旀柇鍣ㄧ姸鎬佷繚瀛樺埌 JSON 鏂囦欢锛?
```java
CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
registry.create("user-service");
registry.create("order-service");

// 淇濆瓨鍒?JSON 鏂囦欢
((DefaultCircuitBreakerRegistry) registry).saveToJson("circuit-breakers.json");

// 浠?JSON 鏂囦欢鍔犺浇
CircuitBreakerRegistry restored = new DefaultCircuitBreakerRegistry();
((DefaultCircuitBreakerRegistry) restored).loadFromJson("circuit-breakers.json");
```

### 8. 杩借釜鏁版嵁鎸佷箙鍖?
鏀寔杩借釜鏁版嵁寮傛鎵归噺淇濆瓨鍒版枃浠讹細

```java
FileSpanExporter exporter = new FileSpanExporter("traces.jsonl");
Tracer tracer = new DefaultTracer("gateway-service", exporter);

TraceContext span = tracer.startSpan("api-request");
tracer.addTag(span, "path", "/api/users");
tracer.addTag(span, "method", "GET");
tracer.endSpan(span, true);

exporter.flush();

// 鍔犺浇宸蹭繚瀛樼殑杩借釜鏁版嵁
List<TraceContext> spans = exporter.loadSpans();
```

## 渚濊禆璇存槑

姣忎釜寰湇鍔℃ā鍧楁寜闇€寮曞叆浠ヤ笅 EST 妯″潡锛?
| 妯″潡 | 鐢ㄩ€?|
|------|------|
| `est-web-impl` | Web 妗嗘灦锛屾彁渚?REST API 鏀寔 |
| `est-gateway` | API 缃戝叧锛堜粎缃戝叧鏈嶅姟浣跨敤锛?|
| `est-discovery-api/impl` | 鏈嶅姟鍙戠幇锛堟敮鎸佹寔涔呭寲锛?|
| `est-config-api/impl` | 閰嶇疆涓績锛堟敮鎸佸姞瀵嗐€佺増鏈鐞嗐€佹寔涔呭寲锛?|
| `est-circuitbreaker-api/impl` | 鐔旀柇鍣紙鏀寔鐘舵€佹寔涔呭寲锛?|
| `est-tracing-api/impl` | 鍒嗗竷寮忚拷韪紙鏀寔鏁版嵁鎸佷箙鍖栵級 |
| `est-logging-console` | 鎺у埗鍙版棩蹇?|

## 鎵╁睍寤鸿

瑕佹坊鍔犳柊鐨勫井鏈嶅姟锛屽彧闇€锛?1. 鍒涘缓鏂扮殑 Maven 妯″潡
2. 寮曞叆 `est-web-impl` 渚濊禆
3. 缂栧啓 `@RestController`
4. 鍚姩鏈嶅姟
5. 鍦ㄧ綉鍏充腑閰嶇疆璺敱

绀轰緥锛?```java
@RestController
public class ProductController {
    @Get("/products")
    public List<Product> getAllProducts() {
        // ...
    }
}
```
