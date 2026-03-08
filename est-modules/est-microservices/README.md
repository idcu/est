# EST Microservices 寰湇鍔℃ā鍧?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Microservices锛焆(#浠€涔堟槸-est-microservices)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Microservices锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Microservices 灏卞儚鏄竴涓?寰湇鍔″伐鍏风"銆傛兂璞′竴涓嬩綘鏈変竴涓ぇ鍨嬬郴缁燂紝鎷嗗垎鎴愬涓皬鏈嶅姟锛?
**浼犵粺鏂瑰紡**锛氭湇鍔′箣闂存壘涓嶅埌瀵规柟锛屼竴涓湇鍔℃寕浜嗘暣涓郴缁熼兘鐦簡锛屼笉鐭ラ亾鎬ц兘鎬庝箞鏍?.. 寰堥夯鐑︼紒

**EST Microservices 鏂瑰紡**锛氱粰浣犱竴濂楀畬鏁寸殑寰湇鍔″伐鍏凤紝閲岄潰鏈夛細
- 馃摗 **鏈嶅姟鍙戠幇** - 鏈嶅姟涔嬮棿鑳借嚜鍔ㄦ壘鍒板鏂?- 馃洝锔?**鐔旀柇鍣?* - 涓€涓湇鍔℃寕浜嗕笉浼氬奖鍝嶅叾浠栨湇鍔?- 馃搳 **鎬ц兘鐩戞帶** - 瀹炴椂鐩戞帶鏈嶅姟鐨勬€ц兘鎸囨爣

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍚敤寰湇鍔″姛鑳?- 鈿?**楂樻€ц兘** - 鍩轰簬楂樻€ц兘鐨勬湇鍔″彂鐜板拰鐔旀柇瀹炵幇
- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夋湇鍔″彂鐜扮瓥鐣ュ拰鐔旀柇瑙勫垯
- 馃帹 **鍔熻兘瀹屾暣** - 鏈嶅姟鍙戠幇銆佺啍鏂櫒銆佹€ц兘鐩戞帶涓€搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓井鏈嶅姟搴旂敤

```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.performance.PerformanceMonitor;

public class FirstMicroserviceApp {
    public static void main(String[] args) {
        System.out.println("=== EST Microservices 绗竴涓ず渚?===\n");
        
        ServiceRegistry registry = ServiceRegistry.create();
        ServiceInstance instance = ServiceInstance.builder()
            .serviceName("user-service")
            .host("localhost")
            .port(8081)
            .build();
        registry.register(instance);
        System.out.println("鏈嶅姟宸叉敞鍐? user-service");
        
        ServiceDiscovery discovery = ServiceDiscovery.create();
        ServiceInstance found = discovery.findOne("user-service");
        System.out.println("鍙戠幇鏈嶅姟: " + found.getHost() + ":" + found.getPort());
        
        CircuitBreaker cb = CircuitBreaker.create("user-service-call");
        String result = cb.call(() -> {
            return "璋冪敤鎴愬姛锛?;
        });
        System.out.println("鐔旀柇鍣ㄨ皟鐢ㄧ粨鏋? " + result);
        
        PerformanceMonitor monitor = PerformanceMonitor.create();
        monitor.start("api-call");
        Thread.sleep(100);
        monitor.stop("api-call");
        System.out.println("鎬ц兘鐩戞帶: " + monitor.getStats("api-call"));
    }
}
```

---

## 鍩虹绡?
### 1. est-discovery 鏈嶅姟鍙戠幇

#### 娉ㄥ唽鏈嶅姟

```java
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.discovery.ServiceInstance;

ServiceRegistry registry = ServiceRegistry.create();

ServiceInstance instance = ServiceInstance.builder()
    .serviceName("user-service")
    .instanceId("user-service-1")
    .host("192.168.1.100")
    .port(8081)
    .healthCheckUrl("http://192.168.1.100:8081/health")
    .metadata(Map.of(
        "version", "1.0.0",
        "zone", "us-east-1"
    ))
    .build();

registry.register(instance);
System.out.println("鏈嶅姟宸叉敞鍐?);
```

#### 鍙戠幇鏈嶅姟

```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.collection.api.Seq;

ServiceDiscovery discovery = ServiceDiscovery.create();

ServiceInstance one = discovery.findOne("user-service");
System.out.println("鎵惧埌涓€涓疄渚? " + one.getHost() + ":" + one.getPort());

Seq<ServiceInstance> all = discovery.findAll("user-service");
System.out.println("鎵惧埌 " + all.size() + " 涓疄渚?);
all.forEach(instance -> {
    System.out.println("- " + instance.getHost() + ":" + instance.getPort());
});
```

#### 蹇冭烦鍜屽仴搴锋鏌?
```java
import ltd.idcu.est.discovery.ServiceRegistry;
import ltd.idcu.est.discovery.Heartbeat;

ServiceRegistry registry = ServiceRegistry.create();
Heartbeat heartbeat = registry.createHeartbeat(instance, 30); // 30绉掑績璺?heartbeat.start();

Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    heartbeat.stop();
    registry.deregister(instance);
}));
```

### 2. est-circuitbreaker 鐔旀柇鍣?
#### 鍒涘缓鐔旀柇鍣?
```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitBreakerConfig;

CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(5) // 5娆″け璐ュ悗鎵撳紑鐔旀柇鍣?    .waitDuration(30000) // 30绉掑悗灏濊瘯鍗婂紑鐘舵€?    .successThreshold(3) // 3娆℃垚鍔熷悗鍏抽棴鐔旀柇鍣?    .build();

CircuitBreaker cb = CircuitBreaker.create("user-service-call", config);
```

#### 浣跨敤鐔旀柇鍣?
```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitBreakerException;

CircuitBreaker cb = CircuitBreaker.create("user-service-call");

try {
    String result = cb.call(() -> {
        return userService.getUser(userId);
    });
    System.out.println("璋冪敤鎴愬姛: " + result);
} catch (CircuitBreakerException e) {
    System.out.println("鐔旀柇鍣ㄥ凡鎵撳紑锛屼娇鐢ㄩ檷绾ф柟妗?);
    return getFallbackUser(userId);
}
```

#### 鐔旀柇鍣ㄧ姸鎬?
```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.CircuitState;

CircuitBreaker cb = CircuitBreaker.create("user-service-call");

CircuitState state = cb.getState();
System.out.println("鐔旀柇鍣ㄧ姸鎬? " + state); // CLOSED, OPEN, HALF_OPEN

if (state == CircuitState.OPEN) {
    System.out.println("鐔旀柇鍣ㄥ凡鎵撳紑锛屾湇鍔′笉鍙敤");
}
```

### 3. est-performance 鎬ц兘鐩戞帶

#### 鐩戞帶鏂规硶鎵ц鏃堕棿

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceStats;

PerformanceMonitor monitor = PerformanceMonitor.create();

monitor.start("getUser");
User user = userService.getUser(userId);
monitor.stop("getUser");

monitor.start("getOrder");
Order order = orderService.getOrder(orderId);
monitor.stop("getOrder");
```

#### 鑾峰彇鎬ц兘缁熻

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceStats;

PerformanceMonitor monitor = PerformanceMonitor.create();

PerformanceStats stats = monitor.getStats("getUser");
System.out.println("璋冪敤娆℃暟: " + stats.getCount());
System.out.println("骞冲潎鑰楁椂: " + stats.getAverageTime() + "ms");
System.out.println("鏈€澶ц€楁椂: " + stats.getMaxTime() + "ms");
System.out.println("鏈€灏忚€楁椂: " + stats.getMinTime() + "ms");
System.out.println("鎬昏€楁椂: " + stats.getTotalTime() + "ms");
```

#### 瀵煎嚭鎬ц兘鏁版嵁

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceExporter;

PerformanceMonitor monitor = PerformanceMonitor.create();
PerformanceExporter exporter = new PerformanceExporter();

String json = exporter.exportToJson(monitor);
System.out.println(json);

exporter.exportToCsv(monitor, Paths.get("performance.csv"));
```

---

## 杩涢樁绡?
### 1. 鑷畾涔夋湇鍔″彂鐜?
```java
import ltd.idcu.est.discovery.ServiceDiscovery;
import ltd.idcu.est.discovery.ServiceInstance;
import ltd.idcu.est.discovery.LoadBalancer;

public class CustomLoadBalancer implements LoadBalancer {
    @Override
    public ServiceInstance choose(Seq<ServiceInstance> instances) {
        return instances
            .sortBy(instance -> instance.getMetadata().getOrDefault("weight", "1"))
            .first()
            .orElse(null);
    }
}

ServiceDiscovery discovery = ServiceDiscovery.create();
discovery.setLoadBalancer(new CustomLoadBalancer());
```

### 2. 鑷畾涔夌啍鏂鍒?
```java
import ltd.idcu.est.circuitbreaker.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.FallbackStrategy;

public class CustomFallbackStrategy implements FallbackStrategy {
    @Override
    public Object fallback(String operation, Throwable error) {
        if ("getUser".equals(operation)) {
            return new User("fallback", "Fallback User");
        }
        if ("getOrder".equals(operation)) {
            return new Order("fallback", "Fallback Order");
        }
        return null;
    }
}

CircuitBreaker cb = CircuitBreaker.create("user-service-call");
cb.setFallbackStrategy(new CustomFallbackStrategy());
```

### 3. 鎬ц兘鐩戞帶闆嗘垚

```java
import ltd.idcu.est.performance.PerformanceMonitor;
import ltd.idcu.est.performance.PerformanceReporter;
import ltd.idcu.est.monitor.MetricRegistry;

public class PrometheusPerformanceReporter implements PerformanceReporter {
    private final MetricRegistry registry;
    
    public PrometheusPerformanceReporter(MetricRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public void report(PerformanceMonitor monitor) {
        monitor.getAllStats().forEach((name, stats) -> {
            registry.gauge("performance." + name + ".count", () -> stats.getCount());
            registry.gauge("performance." + name + ".avg", () -> stats.getAverageTime());
            registry.gauge("performance." + name + ".max", () -> stats.getMaxTime());
        });
    }
}

PerformanceMonitor monitor = PerformanceMonitor.create();
monitor.setReporter(new PrometheusPerformanceReporter(registry));
```

---

## 鏈€浣冲疄璺?
### 1. 鏈嶅姟鍛藉悕瑙勮寖

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄦ湁鎰忎箟鐨勬湇鍔″悕
ServiceInstance.builder()
    .serviceName("user-service")
    .instanceId("user-service-" + UUID.randomUUID())
    .build();

// 鉂?涓嶆帹鑽愶細妯＄硦鐨勬湇鍔″悕
ServiceInstance.builder()
    .serviceName("service1")
    .instanceId("instance1")
    .build();
```

### 2. 鍚堢悊璁剧疆鐔旀柇鍣ㄥ弬鏁?
```java
// 鉁?鎺ㄨ崘锛氭牴鎹疄闄呮儏鍐佃皟鏁?CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(10)
    .waitDuration(60000)
    .successThreshold(5)
    .build();

// 鉂?涓嶆帹鑽愶細澶晱鎰熸垨澶繜閽?CircuitBreakerConfig badConfig = CircuitBreakerConfig.builder()
    .failureThreshold(1) // 1娆″け璐ュ氨鐔旀柇锛屽お鏁忔劅
    .waitDuration(3600000) // 1灏忔椂鎵嶅皾璇曟仮澶嶏紝澶箙
    .build();
```

### 3. 鎬ц兘鐩戞帶鍏抽敭鎸囨爣

```java
// 鉁?鎺ㄨ崘锛氱洃鎺у叧閿搷浣?monitor.start("db.query");
monitor.start("api.call");
monitor.start("cache.get");

// 鉂?涓嶆帹鑽愶細鐩戞帶澶缁嗘灊鏈妭
monitor.start("for.loop.iteration");
monitor.start("variable.assignment");
```

### 4. 鏈嶅姟鍋ュ悍妫€鏌?
```java
// 鉁?鎺ㄨ崘锛氬疄鐜板仴搴锋鏌?ServiceInstance.builder()
    .healthCheckUrl("http://localhost:8080/health")
    .build();

// 鉂?涓嶆帹鑽愶細涓嶆彁渚涘仴搴锋鏌?ServiceInstance.builder()
    .build();
```

---

## 妯″潡缁撴瀯

```
est-microservices/
鈹溾攢鈹€ est-discovery/      # 鏈嶅姟鍙戠幇
鈹溾攢鈹€ est-circuitbreaker/ # 鐔旀柇鍣ㄦā寮?鈹斺攢鈹€ est-performance/    # 鎬ц兘鐩戞帶
```

---

## 鐩稿叧璧勬簮

- [est-discovery README](./est-discovery/README.md) - 鏈嶅姟鍙戠幇璇︾粏鏂囨。
- [est-circuitbreaker README](./est-circuitbreaker/README.md) - 鐔旀柇鍣ㄨ缁嗘枃妗?- [绀轰緥浠ｇ爜](../../est-examples/est-examples-microservices/) - 寰湇鍔＄ず渚嬩唬鐮?- [EST Web Group](../est-web-group/README.md) - Web 妯″潡
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
