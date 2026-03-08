# EST Demo - EST 妗嗘灦婕旂ず搴旂敤

杩欐槸涓€涓狤ST妗嗘灦鐨勫畬鏁存紨绀哄簲鐢紝灞曠ず浜咵ST妗嗘灦鐨勬牳蹇冨姛鑳藉拰浣跨敤鏂规硶銆?
## 椤圭洰姒傝堪

EST Demo鏄竴涓姛鑳戒赴瀵岀殑Web搴旂敤锛屽睍绀轰簡EST妗嗘灦鐨勪互涓嬪姛鑳斤細
- 闆朵緷璧栫殑Web鏈嶅姟鍣?- 绠€娲佺殑璺敱绯荤粺鍜屼腑闂翠欢鏀寔
- RESTful API鏀寔
- JSON鍝嶅簲澶勭悊
- 鏌ヨ鍙傛暟鍜岃矾寰勫弬鏁板鐞?- CORS璺ㄥ煙鏀寔
- **鏃ュ織绯荤粺**锛堟帶鍒跺彴鍜屾枃浠舵棩蹇楋級
- **缂撳瓨绯荤粺**锛堝唴瀛樺拰鏂囦欢缂撳瓨锛?- **浜嬩欢鎬荤嚎**锛堝悓姝ュ拰寮傛浜嬩欢锛?- **浠诲姟璋冨害**
- **绯荤粺鐩戞帶**锛圝VM鍜岀郴缁熸寚鏍囷級
- **鏁版嵁瀛樺偍**锛堝唴瀛樺瓨鍌ㄥ簱锛?- **瀹屾暣鐨刉eb UI鐣岄潰**

## 蹇€熷紑濮?
### 鐜瑕佹眰
- JDK 21+
- Maven 3.6+

### 鏋勫缓椤圭洰
```bash
cd est-demo
mvn clean compile
```

### 杩愯搴旂敤
```bash
mvn exec:java -Dexec.mainClass="ltd.idcu.est.demo.EstDemoApplication"
```

鎴栬€呯洿鎺ヨ繍琛岀紪璇戝悗鐨勭被锛?```bash
java -cp target/classes ltd.idcu.est.demo.EstDemoApplication
```

## 鍔熻兘鐗规€?
### 1. Web UI 鐣岄潰
璁块棶 http://localhost:8080 鍗冲彲鐪嬪埌涓€涓編瑙傜殑Web鐣岄潰锛屽寘鍚細
- 搴旂敤缁熻灞曠ず
- 鐢ㄦ埛绠＄悊鍔熻兘
- 寰呭姙浜嬮」绠＄悊
- 缂撳瓨鎿嶄綔
- 浜嬩欢鍙戝竷
- 鐩戞帶鎸囨爣鏌ョ湅

### 2. 鍙敤鐨凙PI绔偣

#### 鍩虹 API
- **GET /** - 涓婚〉锛圵eb UI锛?- **GET /api/hello** - 闂€欐帴鍙ｏ紙鏀寔name鍙傛暟锛?
#### 鐢ㄦ埛绠＄悊 API
- **GET /api/users** - 鑾峰彇鎵€鏈夌敤鎴?- **GET /api/users/:id** - 鑾峰彇鍗曚釜鐢ㄦ埛
- **POST /api/users** - 鍒涘缓鐢ㄦ埛
- **PUT /api/users/:id** - 鏇存柊鐢ㄦ埛
- **DELETE /api/users/:id** - 鍒犻櫎鐢ㄦ埛

#### 寰呭姙浜嬮」 API
- **GET /api/todos** - 鑾峰彇鎵€鏈夊緟鍔?- **GET /api/todos/:id** - 鑾峰彇鍗曚釜寰呭姙
- **POST /api/todos** - 鍒涘缓寰呭姙
- **PUT /api/todos/:id** - 鏇存柊寰呭姙
- **DELETE /api/todos/:id** - 鍒犻櫎寰呭姙
- **PATCH /api/todos/:id/complete** - 鍒囨崲瀹屾垚鐘舵€?
#### 缂撳瓨 API
- **GET /api/cache/memory** - 鍒楀嚭鎵€鏈夊唴瀛樼紦瀛橀敭
- **GET /api/cache/memory/:key** - 鑾峰彇鍐呭瓨缂撳瓨
- **PUT /api/cache/memory/:key** - 璁剧疆鍐呭瓨缂撳瓨
- **DELETE /api/cache/memory/:key** - 鍒犻櫎鍐呭瓨缂撳瓨
- **GET /api/cache/file/:key** - 鑾峰彇鏂囦欢缂撳瓨
- **PUT /api/cache/file/:key** - 璁剧疆鏂囦欢缂撳瓨

#### 浜嬩欢 API
- **POST /api/events/local** - 鍙戝竷鏈湴浜嬩欢
- **POST /api/events/async** - 鍙戝竷寮傛浜嬩欢

#### 鐩戞帶 API
- **GET /api/monitor/jvm** - 鑾峰彇JVM鎸囨爣
- **GET /api/monitor/system** - 鑾峰彇绯荤粺鎸囨爣
- **GET /api/monitor/stats** - 鑾峰彇搴旂敤缁熻

## 浠ｇ爜绀轰緥

### 鍒涘缓Web搴旂敤鍜屼腑闂翠欢
```java
WebApplication app = Web.create("EST Demo", "2.1.0");

app.use((req, res, next) -> {
    requestCount.incrementAndGet();
    long startTime = System.currentTimeMillis();
    consoleLogger.info("Request: {} {}", req.getMethod(), req.getPath());
    next.handle();
    long duration = System.currentTimeMillis() - startTime;
    consoleLogger.info("Response: {} {} - {}ms", req.getMethod(), req.getPath(), duration);
});
```

### 鏃ュ織绯荤粺
```java
Logger consoleLogger = ConsoleLogs.getLogger(EstDemoApplication.class);
Logger fileLogger = FileLogs.getLogger("est-demo.log");

consoleLogger.info("杩欐槸涓€鏉℃帶鍒跺彴鏃ュ織");
fileLogger.info("杩欐槸涓€鏉℃枃浠舵棩蹇?);
```

### 缂撳瓨绯荤粺
```java
Cache<String, Object> memoryCache = new MemoryCache<>();
Cache<String, Object> fileCache = new FileCache<>("est-demo-cache");

memoryCache.put("key", "value");
Object value = memoryCache.get("key");
```

### 浜嬩欢鎬荤嚎
```java
EventBus localEventBus = new LocalEventBus();
EventBus asyncEventBus = new AsyncEventBus();

localEventBus.subscribe(UserCreatedEvent.class, event -> {
    consoleLogger.info("鏀跺埌鐢ㄦ埛鍒涘缓浜嬩欢: {}", event.getUsername());
});

localEventBus.publish(new UserCreatedEvent("username", "email"));
```

### 浠诲姟璋冨害
```java
Scheduler scheduler = new FixedScheduler();

scheduler.scheduleAtFixedRate(() -> {
    consoleLogger.debug("瀹氭椂浠诲姟鎵ц");
}, 10, 30, TimeUnit.SECONDS);
```

### 绯荤粺鐩戞帶
```java
Monitor jvmMonitor = new JvmMonitor();
Monitor systemMonitor = new SystemMonitor();

Object heapMemory = jvmMonitor.getMetric("heapMemoryUsage");
Object processors = systemMonitor.getMetric("availableProcessors");
```

### 鏁版嵁瀛樺偍
```java
Repository<String, User> userRepository = new MemoryRepository<>();

userRepository.save("1", new User("1", "Alice", "alice@example.com", "admin"));
User user = userRepository.findById("1").orElse(null);
List<User> allUsers = userRepository.findAll();
```

## 椤圭洰缁撴瀯

```
est-demo/
鈹溾攢鈹€ pom.xml                          # Maven閰嶇疆鏂囦欢
鈹溾攢鈹€ README.md                        # 鏈枃浠?鈹溾攢鈹€ QUICKSTART.md                    # 蹇€熷紑濮嬫寚鍗?鈹溾攢鈹€ run.bat                          # Windows杩愯鑴氭湰
鈹溾攢鈹€ run.ps1                          # PowerShell杩愯鑴氭湰
鈹溾攢鈹€ run-with-classes.ps1             # 绫昏矾寰勮繍琛岃剼鏈?鈹斺攢鈹€ src/
    鈹斺攢鈹€ main/
        鈹斺攢鈹€ java/
            鈹斺攢鈹€ ltd/
                鈹斺攢鈹€ idcu/
                    鈹斺攢鈹€ est/
                        鈹斺攢鈹€ demo/
                            鈹斺攢鈹€ EstDemoApplication.java  # 涓诲簲鐢ㄧ被
```

## 渚濊禆鐨凟ST妯″潡

婕旂ず搴旂敤渚濊禆浠ヤ笅EST妯″潡锛?- `est-web-impl` - Web妗嗘灦瀹炵幇
- `est-util-common` - 閫氱敤宸ュ叿
- `est-util-format-json` - JSON鏍煎紡鍖?- `est-logging-console` - 鎺у埗鍙版棩蹇?- `est-logging-file` - 鏂囦欢鏃ュ織
- `est-cache-memory` - 鍐呭瓨缂撳瓨
- `est-cache-file` - 鏂囦欢缂撳瓨
- `est-event-local` - 鏈湴浜嬩欢鎬荤嚎
- `est-event-async` - 寮傛浜嬩欢鎬荤嚎
- `est-scheduler-fixed` - 鍥哄畾棰戠巼璋冨害
- `est-scheduler-cron` - Cron琛ㄨ揪寮忚皟搴?- `est-monitor-jvm` - JVM鐩戞帶
- `est-monitor-system` - 绯荤粺鐩戞帶
- `est-data-memory` - 鍐呭瓨鏁版嵁瀛樺偍
- `est-config-impl` - 閰嶇疆绠＄悊

## 涓嬩竴姝?
鏌ョ湅EST妗嗘灦鐨勫叾浠栨ā鍧楋紝浜嗚В鏇村鍔熻兘锛?- Redis缂撳瓨
- 瀹夊叏璁よ瘉锛圔asic, JWT, OAuth2锛?- 娑堟伅绯荤粺锛圞afka, RabbitMQ绛夛級
- 宸ヤ綔娴佸紩鎿?- 鐔旀柇鍣?- 鏈嶅姟鍙戠幇
- API缃戝叧
- 绛夌瓑

## 璁稿彲璇?
MIT License

## 鐩稿叧閾炬帴

- [EST妗嗘灦涓绘枃妗(../README.md)
- [EST Web妯″潡鏂囨。](../est-app/est-web/README.md)
