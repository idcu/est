# EST Foundation 鍩虹璁炬柦妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Foundation锛焆(#浠€涔堟槸-est-foundation)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Foundation锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Foundation 灏卞儚鏄竴涓?鍩虹璁炬柦宸ュ叿绠?銆傛兂璞′竴涓嬩綘瑕佸缓鎴垮瓙锛岄渶瑕佹按鐢点€侀€氳銆佺洃鎺х瓑鍩虹璁炬柦锛?
**浼犵粺鏂瑰紡**锛氭瘡涓簲鐢ㄩ兘瑕佽嚜宸卞啓缂撳瓨銆佹棩蹇椼€侀厤缃€佺洃鎺?.. 閲嶅閫犺疆瀛愶紒

**EST Foundation 鏂瑰紡**锛氱粰浣犱竴濂楀畬鏁寸殑鍩虹璁炬柦锛岄噷闈㈡湁锛?- 馃捑 **缂撳瓨绯荤粺** - 澶氱缂撳瓨瀹炵幇锛堝唴瀛樸€佹枃浠躲€丷edis锛?- 馃摙 **浜嬩欢鎬荤嚎** - 浜嬩欢椹卞姩鏋舵瀯
- 馃摑 **鏃ュ織妗嗘灦** - 鏀寔鎺у埗鍙板拰鏂囦欢鏃ュ織
- 鈿欙笍 **閰嶇疆绠＄悊** - 缁熶竴鐨勯厤缃鐞?- 馃搳 **鐩戞帶绯荤粺** - 鐩戞帶鍜屾寚鏍囨敹闆?- 馃攳 **鍒嗗竷寮忚拷韪?* - 鍒嗗竷寮忕郴缁熻拷韪敮鎸?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍚敤鍩虹璁炬柦
- 鈿?**楂樻€ц兘** - 浼樺寲鐨勭紦瀛樸€佹棩蹇椼€佺洃鎺у疄鐜?- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夊悇绉嶅疄鐜?- 馃帹 **鍔熻兘瀹屾暣** - 缂撳瓨銆佷簨浠躲€佹棩蹇椼€侀厤缃€佺洃鎺с€佽拷韪竴搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓熀纭€璁炬柦搴旂敤

```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.memory.Caches;
import ltd.idcu.est.logging.Logger;
import ltd.idcu.est.logging.Loggers;
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;

public class FirstFoundationApp {
    public static void main(String[] args) {
        System.out.println("=== EST Foundation 绗竴涓ず渚?===\n");
        
        Logger logger = Loggers.getLogger("my-app");
        logger.info("搴旂敤鍚姩");
        
        Config config = Configs.load("application.properties");
        String appName = config.getString("app.name", "My App");
        logger.info("搴旂敤鍚嶇О: " + appName);
        
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        logger.info("缂撳瓨鍊? " + cache.get("key").orElse("鏈壘鍒?));
        
        logger.info("搴旂敤杩愯瀹屾垚");
    }
}
```

---

## 鍩虹绡?
### 1. est-cache 缂撳瓨绯荤粺

璇︾粏鏂囨。璇峰弬鑰冿細[est-cache README](./est-cache/README.md)

#### 鍩烘湰浣跨敤

```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.memory.Caches;

Cache<String, String> cache = Caches.newMemoryCache();

cache.put("user:1", "寮犱笁");
cache.put("user:2", "鏉庡洓");

String user1 = cache.get("user:1").orElse("鏈壘鍒?);
String user3 = cache.get("user:3", "榛樿鍊?);

System.out.println("缂撳瓨澶у皬: " + cache.size());
```

#### TTL 杩囨湡

```java
import java.util.concurrent.TimeUnit;

cache.put("temp:1", "涓存椂鏁版嵁", 10, TimeUnit.SECONDS);
```

### 2. est-event 浜嬩欢鎬荤嚎

#### 瀹氫箟浜嬩欢

```java
import ltd.idcu.est.event.Event;

public class UserCreatedEvent implements Event {
    private final String userId;
    private final String username;
    
    public UserCreatedEvent(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
}
```

#### 鍙戝竷鍜岃闃呬簨浠?
```java
import ltd.idcu.est.event.EventBus;
import ltd.idcu.est.event.EventBuses;
import ltd.idcu.est.event.EventListener;

EventBus eventBus = EventBuses.create();

eventBus.subscribe(UserCreatedEvent.class, event -> {
    System.out.println("鐢ㄦ埛鍒涘缓浜嬩欢: " + event.getUsername());
});

eventBus.publish(new UserCreatedEvent("123", "寮犱笁"));
```

### 3. est-logging 鏃ュ織妗嗘灦

#### 浣跨敤鏃ュ織

```java
import ltd.idcu.est.logging.Logger;
import ltd.idcu.est.logging.Loggers;
import ltd.idcu.est.logging.LogLevel;

Logger logger = Loggers.getLogger("my-app");

logger.trace("杩欐槸 trace 鏃ュ織");
logger.debug("杩欐槸 debug 鏃ュ織");
logger.info("杩欐槸 info 鏃ュ織");
logger.warn("杩欐槸 warn 鏃ュ織");
logger.error("杩欐槸 error 鏃ュ織", new Exception("鍑洪敊浜?));
```

#### 閰嶇疆鏃ュ織

```java
import ltd.idcu.est.logging.LoggerConfig;
import ltd.idcu.est.logging.console.ConsoleLogger;
import ltd.idcu.est.logging.file.FileLogger;

LoggerConfig config = LoggerConfig.builder()
    .level(LogLevel.INFO)
    .addLogger(new ConsoleLogger())
    .addLogger(new FileLogger("app.log"))
    .build();

Loggers.configure(config);
```

### 4. est-config 閰嶇疆绠＄悊

#### 鍔犺浇閰嶇疆

```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;

Config config = Configs.load("application.properties");

String appName = config.getString("app.name", "My App");
int port = config.getInt("server.port", 8080);
boolean debug = config.getBoolean("debug", false);
double timeout = config.getDouble("timeout", 30.0);
```

#### 澶氱閰嶇疆婧?
```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;
import ltd.idcu.est.config.source.PropertiesConfigSource;
import ltd.idcu.est.config.source.EnvironmentConfigSource;
import ltd.idcu.est.config.source.SystemConfigSource;

Config config = Configs.builder()
    .addSource(new SystemConfigSource())
    .addSource(new EnvironmentConfigSource())
    .addSource(new PropertiesConfigSource("application.properties"))
    .build();
```

### 5. est-monitor 鐩戞帶绯荤粺

#### 鏀堕泦鎸囨爣

```java
import ltd.idcu.est.monitor.Monitor;
import ltd.idcu.est.monitor.Monitors;
import ltd.idcu.est.monitor.Metric;

Monitor monitor = Monitors.create();

monitor.gauge("memory.used", () -> Runtime.getRuntime().totalMemory());
monitor.counter("requests.total").increment();
monitor.timer("request.time").record(100);
```

#### 瀵煎嚭鎸囨爣

```java
import ltd.idcu.est.monitor.Monitor;
import ltd.idcu.est.monitor.MetricExporter;
import ltd.idcu.est.monitor.exporter.PrometheusExporter;

Monitor monitor = Monitors.create();
MetricExporter exporter = new PrometheusExporter();

String metrics = exporter.export(monitor);
System.out.println(metrics);
```

### 6. est-tracing 鍒嗗竷寮忚拷韪?
#### 鍒涘缓鍜屼紶鎾拷韪?
```java
import ltd.idcu.est.tracing.Tracer;
import ltd.idcu.est.tracing.Tracers;
import ltd.idcu.est.tracing.Span;

Tracer tracer = Tracers.create();

try (Span span = tracer.startSpan("my-operation")) {
    span.setAttribute("user.id", "123");
    
    try (Span childSpan = tracer.startSpan("child-operation")) {
        childSpan.setAttribute("step", "1");
    }
}
```

---

## 杩涢樁绡?
### 1. 鑷畾涔夌紦瀛樺疄鐜?
```java
import ltd.idcu.est.cache.Cache;
import ltd.idcu.est.cache.AbstractCache;

public class CustomCache<K, V> extends AbstractCache<K, V> {
    
    @Override
    protected V doGet(K key) {
        // 鑷畾涔夎幏鍙栭€昏緫
        return null;
    }
    
    @Override
    protected void doPut(K key, V value) {
        // 鑷畾涔夊瓨鏀鹃€昏緫
    }
    
    @Override
    protected void doRemove(K key) {
        // 鑷畾涔夊垹闄ら€昏緫
    }
}
```

### 2. 寮傛浜嬩欢澶勭悊

```java
import ltd.idcu.est.event.EventBus;
import ltd.idcu.est.event.EventBuses;
import ltd.idcu.est.event.async.AsyncEventBus;

EventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));

eventBus.subscribe(UserCreatedEvent.class, event -> {
    // 寮傛澶勭悊浜嬩欢
});
```

### 3. 閰嶇疆鐑洿鏂?
```java
import ltd.idcu.est.config.Config;
import ltd.idcu.est.config.Configs;
import ltd.idcu.est.config.watcher.FileConfigWatcher;

Config config = Configs.load("application.properties");
FileConfigWatcher watcher = new FileConfigWatcher(config, Paths.get("application.properties"));

watcher.addListener(newConfig -> {
    System.out.println("閰嶇疆宸叉洿鏂?);
});

watcher.start();
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊浣跨敤缂撳瓨

```java
// 鉁?鎺ㄨ崘锛氱紦瀛樼儹鐐规暟鎹?Cache<String, Product> productCache = Caches.newMemoryCache();
productCache.put("product:1", product);

// 鉂?涓嶆帹鑽愶細缂撳瓨鎵€鏈夋暟鎹?// 浼氬崰鐢ㄥお澶氬唴瀛?```

### 2. 鏃ュ織绾у埆浣跨敤

```java
// 鉁?鎺ㄨ崘锛氬悎鐞嗕娇鐢ㄦ棩蹇楃骇鍒?logger.debug("璋冭瘯淇℃伅");
logger.info("閲嶈淇℃伅");
logger.warn("璀﹀憡淇℃伅");
logger.error("閿欒淇℃伅", exception);

// 鉂?涓嶆帹鑽愶細鎵€鏈夐兘鐢?info
logger.info("璋冭瘯淇℃伅");
logger.info("閲嶈淇℃伅");
```

### 3. 閰嶇疆浼樺厛绾?
```java
// 鉁?鎺ㄨ崘锛氱郴缁熷睘鎬?> 鐜鍙橀噺 > 閰嶇疆鏂囦欢
Config config = Configs.builder()
    .addSource(new SystemConfigSource())
    .addSource(new EnvironmentConfigSource())
    .addSource(new PropertiesConfigSource("application.properties"))
    .build();
```

---

## 妯″潡缁撴瀯

```
est-foundation/
鈹溾攢鈹€ est-cache/      # 缂撳瓨绯荤粺锛堝唴瀛樸€佹枃浠躲€丷edis锛?鈹溾攢鈹€ est-event/      # 浜嬩欢鎬荤嚎
鈹溾攢鈹€ est-logging/    # 鏃ュ織妗嗘灦锛堟帶鍒跺彴銆佹枃浠讹級
鈹溾攢鈹€ est-config/     # 閰嶇疆绠＄悊
鈹溾攢鈹€ est-monitor/    # 鐩戞帶绯荤粺
鈹斺攢鈹€ est-tracing/    # 鍒嗗竷寮忚拷韪?```

---

## 鐩稿叧璧勬簮

- [est-cache README](./est-cache/README.md) - 缂撳瓨璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-basic/) - 鍩虹绀轰緥
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
