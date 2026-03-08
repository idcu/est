# EST Gateway 缃戝叧妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Gateway锛焆(#浠€涔堟槸-est-gateway)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST Gateway锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Gateway 灏卞儚鏄竴涓?鏅鸿兘浜ら€氭寚鎸ヤ腑蹇?銆傛兂璞′竴涓嬩綘鍦ㄨ繍钀ヤ竴涓ぇ鍨嬭喘鐗╀腑蹇冿紝閲岄潰鏈夊緢澶氬簵閾猴紙鏈嶅姟锛夛紝椤惧锛堣姹傦級闇€瑕佸幓涓嶅悓鐨勫簵閾猴細

**浼犵粺鏂瑰紡**锛氶【瀹㈠繀椤荤煡閬撴瘡瀹跺簵閾虹殑鍏蜂綋浣嶇疆锛岃嚜宸辨壘璺繃鍘伙紝寰堥夯鐑︼紒

**EST Gateway 鏂瑰紡**锛氫綘鏈変竴涓粺涓€鐨勫叆鍙ｏ紝鍛婅瘔鎸囨尌涓績浣犳兂鍘诲摢閲岋紝瀹冭嚜鍔ㄥ府浣犲鑸繃鍘伙紒
- 鏀寔璺敱瑙勫垯锛氭妸 `/api/users` 杞彂鍒扮敤鎴锋湇鍔?- 鏀寔涓棿浠讹細鍙互鍦ㄨ姹傚墠鍚庡仛澶勭悊锛堝鏃ュ織銆丆ORS锛?- 鍔ㄦ€侀厤缃細闅忔椂娣诲姞鏂扮殑璺敱瑙勫垯

瀹冩敮鎸佸绉嶄腑闂翠欢锛氭棩蹇椼€丆ORS锛屼綘涔熷彲浠ヨ嚜宸卞啓锛?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜屽惎鍔ㄧ綉鍏?- 馃殌 **楂樻€ц兘** - 鍩轰簬 HTTP 鏈嶅姟鍣紝杞彂閫熷害蹇?- 馃攧 **涓棿浠舵敮鎸?* - 鐏垫椿鐨勮姹傚鐞嗛摼
- 馃搳 **璺敱绠＄悊** - 鏀寔鍔ㄦ€佽矾鐢遍厤缃?- 馃殾 **闄愭祦鏀寔** - 鍐呯疆浠ょ墝妗堕檺娴佸櫒
- 馃捑 **鎸佷箙鍖栨敮鎸?* - 闄愭祦鍣ㄧ姸鎬佹敮鎸丣SON鎸佷箙鍖?- 馃搱 **鍙墿灞?* - 杞绘澗娣诲姞鑷畾涔変腑闂翠欢

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-gateway-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-gateway-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓綉鍏?
```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class FirstGatewayExample {
    public static void main(String[] args) {
        System.out.println("=== EST Gateway 绗竴涓ず渚?===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        Route route = new DefaultRoute("/api/users", "http://localhost:8081/users");
        gateway.getRouter().addRoute(route);
        
        gateway.start(8080);
        System.out.println("缃戝叧宸插惎鍔紝鐩戝惉绔彛 8080");
        System.out.println("璁块棶 http://localhost:8080/api/users 灏嗚浆鍙戝埌 http://localhost:8081/users");
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Gateway 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Gateway 绗竴涓ず渚?===

缃戝叧宸插惎鍔紝鐩戝惉绔彛 8080
璁块棶 http://localhost:8080/api/users 灏嗚浆鍙戝埌 http://localhost:8081/users

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Gateway 浜嗭紒
```

---

## 鍩虹绡?
### 1. 浠€涔堟槸 ApiGateway锛?
ApiGateway 灏辨槸涓€涓?缁熶竴鍏ュ彛"鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface ApiGateway {
    void start(int port);                    // 鍚姩缃戝叧
    void stop();                              // 鍋滄缃戝叧
    GatewayRouter getRouter();                // 鑾峰彇璺敱绠＄悊鍣?    void addMiddleware(GatewayMiddleware middleware);  // 娣诲姞涓棿浠?    void removeMiddleware(String name);       // 绉婚櫎涓棿浠?}
```

### 2. 鍒涘缓缃戝叧鐨勫嚑绉嶆柟寮?
```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class CreateGatewayExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁ょ綉鍏?---");
        ApiGateway gateway1 = new DefaultApiGateway();
        System.out.println("榛樿缃戝叧鍒涘缓鎴愬姛");
        
        System.out.println("\n--- 鏂瑰紡浜岋細娣诲姞璺敱 ---");
        ApiGateway gateway2 = new DefaultApiGateway();
        Route route = new DefaultRoute("/api", "http://localhost:8081");
        gateway2.getRouter().addRoute(route);
        System.out.println("甯﹁矾鐢辩殑缃戝叧鍒涘缓鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class BasicOperations {
    public static void main(String[] args) {
        ApiGateway gateway = new DefaultApiGateway();
        GatewayRouter router = gateway.getRouter();
        
        System.out.println("--- 1. 娣诲姞璺敱 ---");
        Route route1 = new DefaultRoute("/api/users", "http://localhost:8081/users");
        Route route2 = new DefaultRoute("/api/orders", "http://localhost:8082/orders");
        router.addRoute(route1);
        router.addRoute(route2);
        System.out.println("娣诲姞浜?2 涓矾鐢?);
        
        System.out.println("\n--- 2. 鏌ユ壘璺敱 ---");
        Route found = router.findRoute("/api/users/123");
        System.out.println("鎵惧埌璺敱: " + found);
        
        System.out.println("\n--- 3. 鍒犻櫎璺敱 ---");
        router.removeRoute("/api/orders");
        System.out.println("鍒犻櫎浜嗚鍗曡矾鐢?);
        
        System.out.println("\n--- 4. 鑾峰彇鎵€鏈夎矾鐢?---");
        System.out.println("褰撳墠璺敱鏁? " + router.getRoutes().size());
    }
}
```

---

## 杩涢樁绡?
### 1. 涓棿浠讹紙Middleware锛?
浣犲彲浠ユ坊鍔犱腑闂翠欢鏉ュ鐞嗚姹傦細

```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.middleware.LoggingMiddleware;
import ltd.idcu.est.gateway.impl.middleware.CorsMiddleware;

public class MiddlewareExample {
    public static void main(String[] args) {
        System.out.println("--- 涓棿浠剁ず渚?---");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        gateway.addMiddleware(new LoggingMiddleware());
        gateway.addMiddleware(new CorsMiddleware());
        
        System.out.println("娣诲姞浜嗘棩蹇楀拰 CORS 涓棿浠?);
    }
}
```

### 2. 鑷畾涔変腑闂翠欢

```java
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;

public class CustomMiddleware implements GatewayMiddleware {
    @Override
    public String getName() {
        return "custom";
    }
    
    @Override
    public void before(GatewayContext context) {
        System.out.println("[Custom] 璇锋眰鍓嶅鐞? " + context.getRequestPath());
    }
    
    @Override
    public void after(GatewayContext context) {
        System.out.println("[Custom] 璇锋眰鍚庡鐞嗭紝鐘舵€佺爜: " + context.getResponseStatusCode());
    }
}
```

---

## 楂樼骇绡?
### 1. 鍔ㄦ€佽矾鐢遍厤缃?
```java
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayRouter;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class DynamicRoutingExample {
    public static void main(String[] args) {
        System.out.println("--- 鍔ㄦ€佽矾鐢遍厤缃ず渚?---");
        
        ApiGateway gateway = new DefaultApiGateway();
        GatewayRouter router = gateway.getRouter();
        
        String[][] routes = {
            {"/api/users", "http://localhost:8081"},
            {"/api/orders", "http://localhost:8082"},
            {"/api/products", "http://localhost:8083"}
        };
        
        for (String[] r : routes) {
            Route route = new DefaultRoute(r[0], r[1]);
            router.addRoute(route);
            System.out.println("娣诲姞璺敱: " + r[0] + " -> " + r[1]);
        }
        
        System.out.println("\n褰撳墠璺敱閰嶇疆瀹屾垚");
    }
}
```

---

## 楂樼骇绡?
### 1. 浠ょ墝妗堕檺娴佸櫒

Gateway 鍐呯疆浠ょ墝妗堕檺娴佸櫒锛屾敮鎸侀檺娴佺姸鎬佹寔涔呭寲锛?
```java
import ltd.idcu.est.gateway.api.RateLimiter;
import ltd.idcu.est.gateway.api.RateLimiterRegistry;
import ltd.idcu.est.gateway.impl.TokenBucketRateLimiter;
import ltd.idcu.est.gateway.impl.DefaultRateLimiterRegistry;

import java.io.File;

public class RateLimiterExample {
    public static void main(String[] args) {
        System.out.println("--- 闄愭祦鍣ㄧず渚?---");
        
        File dataFile = new File("rate-limiters.json");
        RateLimiterRegistry registry = new DefaultRateLimiterRegistry(dataFile);
        
        RateLimiter limiter = new TokenBucketRateLimiter("api-gateway", 100, 10);
        registry.register(limiter);
        
        for (int i = 0; i < 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.println("璇锋眰 " + (i + 1) + ": " + (allowed ? "鍏佽" : "鎷掔粷"));
        }
        
        System.out.println("闄愭祦鍣ㄧ姸鎬佸凡鑷姩淇濆瓨");
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?
EST Gateway 鍜?est-discovery 鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氭湇鍔″彂鐜?+ 缃戝叧璺敱

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.discovery.impl.RoundRobinLoadBalancer;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

import java.util.List;

public class DiscoveryIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Gateway + EST Discovery 闆嗘垚绀轰緥 ===\n");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
        
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8082);
        registry.register(instance1);
        registry.register(instance2);
        
        ApiGateway gateway = new DefaultApiGateway();
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        ServiceInstance selected = loadBalancer.select(instances);
        
        Route route = new DefaultRoute("/api/users", 
            "http://" + selected.getHost() + ":" + selected.getPort());
        gateway.getRouter().addRoute(route);
        
        System.out.println("璺敱宸查厤缃紝鎸囧悜: " + selected);
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊瑙勫垝璺敱鍓嶇紑

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄧ粺涓€鐨勫墠缂€鍒嗙粍
Route userRoute = new DefaultRoute("/api/users", "http://user-service");
Route orderRoute = new DefaultRoute("/api/orders", "http://order-service");

// 鉁?涓嶆帹鑽愶細璺敱娣蜂贡
Route route1 = new DefaultRoute("/users", "http://user-service");
Route route2 = new DefaultRoute("/api-orders", "http://order-service");
```

### 2. 浣跨敤涓棿浠堕摼

```java
// 鉁?鎺ㄨ崘锛氭寜椤哄簭娣诲姞涓棿浠?gateway.addMiddleware(new LoggingMiddleware());
gateway.addMiddleware(new CorsMiddleware());
gateway.addMiddleware(customMiddleware);
```

### 3. 鐩戞帶璺敱鐘舵€?
```java
GatewayRouter router = gateway.getRouter();
System.out.println("褰撳墠璺敱鏁? " + router.getRoutes().size());
```

---

## 甯歌闂

### Q: 缃戝叧鏀寔 HTTPS 鍚楋紵

A: 褰撳墠鐗堟湰榛樿浣跨敤 HTTP锛屽悗缁増鏈細鏀寔 HTTPS 閰嶇疆銆?
### Q: 璺敱鍖归厤瑙勫垯鏄粈涔堬紵

A: 浣跨敤鍓嶇紑鍖归厤锛宍/api/users` 浼氬尮閰?`/api/users/123` 绛夎矾寰勩€?
### Q: 鍙互鍚屾椂杩愯澶氫釜缃戝叧鍚楋紵

A: 鍙互锛屾瘡涓綉鍏崇洃鍚笉鍚岀殑绔彛鍗冲彲銆?
---

## 涓嬩竴姝?
- 瀛︿範 [est-discovery](../est-discovery/README.md) 杩涜鏈嶅姟鍙戠幇
- 鏌ョ湅 [est-circuitbreaker](../est-circuitbreaker/) 浜嗚В鐔旀柇淇濇姢
- 灏濊瘯鑷畾涔変腑闂翠欢瀹炵幇
- 闃呰 [API 鏂囨。](../../docs/api/gateway/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
