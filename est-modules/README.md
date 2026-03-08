# EST Modules 鍔熻兘妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Modules锛焆(#浠€涔堟槸-est-modules)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [妯″潡姒傝](#妯″潡姒傝)
4. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Modules锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Modules 灏卞儚鏄竴涓?鍔熻兘瓒呭競"銆傛兂璞′竴涓嬩綘瑕佸仛楗紝闇€瑕佸悇绉嶉鏉愬拰璋冩枡锛氳敩鑿溿€佽倝绫汇€佹补鐩愰叡閱?..

**浼犵粺鏂瑰紡**锛氭瘡娆″仛楗兘瑕佽嚜宸卞噯澶囨墍鏈変笢瑗匡紝寰堥夯鐑︺€?
**EST Modules 鏂瑰紡**锛氱粰浣犱竴涓婊″姛鑳芥ā鍧楃殑瓒呭競锛岄噷闈㈡湁锛?- 馃彈锔?**鍩虹璁炬柦** - 缂撳瓨銆佷簨浠躲€佹棩蹇椼€侀厤缃€佺洃鎺с€佽拷韪?- 馃梽锔?**鏁版嵁璁块棶** - 鏁版嵁瀛樺偍銆佸伐浣滄祦寮曟搸
- 馃攼 **瀹夊叏鏉冮檺** - 璁よ瘉鎺堟潈銆丷BAC銆佸璁?- 馃摠 **娑堟伅闆嗘垚** - 娑堟伅闃熷垪銆佺郴缁熼泦鎴?- 馃寪 **Web 妗嗘灦** - 璺敱銆佺綉鍏炽€佷腑闂翠欢
- 馃 **AI 濂椾欢** - AI 鍔╂墜銆丩LM 闆嗘垚
- 鈿欙笍 **寰湇鍔?* - 鐔旀柇鍣ㄣ€佹湇鍔″彂鐜般€佹€ц兘浼樺寲
- 馃攲 **鎵╁睍鍔熻兘** - 鎻掍欢銆佽皟搴︺€佺儹鍔犺浇

### 鏍稿績鐗圭偣

- 馃幆 **妯″潡鍖栬璁?* - 鎸夐渶寮曞叆锛岀伒娲荤粍鍚?- 鈿?**寮€绠卞嵆鐢?* - 棰勭疆甯哥敤鍔熻兘
- 馃敡 **鍙墿灞?* - 鍙互鑷畾涔夊拰鎵╁睍
- 馃敀 **闆朵緷璧?* - 绾?Java 瀹炵幇

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔犻渶瑕佺殑妯″潡锛?
```xml
<dependencies>
    <!-- 缂撳瓨妯″潡 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-cache</artifactId>
        <version>2.1.0</version>
    </dependency>
    
    <!-- 鏃ュ織妯″潡 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-logging</artifactId>
        <version>2.1.0</version>
    </dependency>
    
    <!-- 鏁版嵁璁块棶妯″潡 -->
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-data</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣跨敤妯″潡

```java
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.Caches;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.api.Loggers;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryData;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Modules 绗竴涓ず渚?===\n");
        
        // 1. 浣跨敤缂撳瓨
        Cache<String, String> cache = Caches.newMemoryCache();
        cache.put("key", "value");
        System.out.println("缂撳瓨: " + cache.get("key").orElse("null"));
        
        // 2. 浣跨敤鏃ュ織
        Logger logger = Loggers.getLogger(FirstExample.class);
        logger.info("杩欐槸涓€鏉℃棩蹇?);
        
        // 3. 浣跨敤鏁版嵁璁块棶
        Repository<User, Long> repo = MemoryData.newRepository();
        User user = new User();
        user.setName("寮犱笁");
        repo.save(user);
        System.out.println("鐢ㄦ埛: " + repo.findAll());
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Modules 浜嗭紒");
    }
}
```

---

## 妯″潡姒傝

### 1. est-foundation 鍩虹璁炬柦妯″潡

鎻愪緵浼佷笟绾у簲鐢ㄥ紑鍙戞墍闇€鐨勫熀纭€璁炬柦銆?
璇︾粏鏂囨。璇峰弬鑰冿細[est-foundation README](./est-foundation/README.md)

#### 鍖呭惈妯″潡

- **est-cache** - 缂撳瓨绯荤粺锛堝唴瀛樸€佹枃浠躲€丷edis锛?- **est-event** - 浜嬩欢鎬荤嚎锛堟湰鍦般€佸紓姝ワ級
- **est-logging** - 鏃ュ織绯荤粺锛堟帶鍒跺彴銆佹枃浠讹級
- **est-config** - 閰嶇疆绠＄悊
- **est-monitor** - 鐩戞帶绯荤粺锛圝VM銆佺郴缁燂級
- **est-tracing** - 鍒嗗竷寮忚拷韪?
#### 蹇€熺ず渚?
```java
// 缂撳瓨
Cache<String, User> userCache = Caches.newMemoryCache();
userCache.put("user:1", user);

// 鏃ュ織
Logger logger = Loggers.getLogger(MyClass.class);
logger.info("搴旂敤鍚姩");

// 浜嬩欢
EventBus eventBus = EventBus.create();
eventBus.subscribe(UserCreatedEvent.class, event -> {
    System.out.println("鐢ㄦ埛鍒涘缓: " + event.getUserId());
});
```

### 2. est-data-group 鏁版嵁璁块棶妯″潡

鎻愪緵鏁版嵁鎸佷箙鍖栧拰宸ヤ綔娴佸姛鑳姐€?
璇︾粏鏂囨。璇峰弬鑰冿細[est-data-group README](./est-data-group/README.md)

#### 鍖呭惈妯″潡

- **est-data** - 鏁版嵁璁块棶锛圝DBC銆佸唴瀛樸€丮ongoDB銆丷edis锛?- **est-workflow** - 宸ヤ綔娴佸紩鎿?
#### 蹇€熺ず渚?
```java
// 鏁版嵁璁块棶
Repository<User, Long> userRepo = MemoryData.newRepository();
User user = userRepo.findById(1L).orElse(null);

// 宸ヤ綔娴?WorkflowEngine engine = WorkflowEngine.create();
Workflow workflow = engine.getWorkflow("order-process");
workflow.start(orderData);
```

### 3. est-security-group 瀹夊叏鏉冮檺妯″潡

鎻愪緵璁よ瘉銆佹巿鏉冨拰瀹¤鍔熻兘銆?
璇︾粏鏂囨。璇峰弬鑰冿細[est-security-group README](./est-security-group/README.md)

#### 鍖呭惈妯″潡

- **est-security** - 瀹夊叏璁よ瘉锛圝WT銆丅asic銆丱Auth2銆丄PI Key锛?- **est-rbac** - 鍩轰簬瑙掕壊鐨勮闂帶鍒?- **est-audit** - 瀹¤鏃ュ織

#### 蹇€熺ず渚?
```java
// 瀹夊叏璁よ瘉
JwtService jwtService = JwtService.create();
String token = jwtService.generateToken(user);
User authenticated = jwtService.validateToken(token);

// RBAC
Role adminRole = Role.create("admin");
adminRole.addPermission("user:create");
adminRole.addPermission("user:delete");
```

### 4. est-integration-group 娑堟伅闆嗘垚妯″潡

鎻愪緵娑堟伅闃熷垪鍜岀郴缁熼泦鎴愬姛鑳姐€?
璇︾粏鏂囨。璇峰弬鑰冿細[est-integration-group README](./est-integration-group/README.md)

#### 鍖呭惈妯″潡

- **est-messaging** - 娑堟伅绯荤粺锛圞afka銆丷abbitMQ銆丄ctiveMQ銆丷edis銆乄ebSocket 绛夛級
- **est-integration** - 绯荤粺闆嗘垚

#### 蹇€熺ず渚?
```java
// 娑堟伅闃熷垪
MessageProducer producer = MessageProducer.create("kafka");
producer.send("orders", orderMessage);

MessageConsumer consumer = MessageConsumer.create("kafka");
consumer.subscribe("orders", message -> {
    System.out.println("鏀跺埌娑堟伅: " + message);
});
```

### 5. est-web-group Web 妗嗘灦妯″潡

鎻愪緵 Web 寮€鍙戠浉鍏冲姛鑳姐€?
璇︾粏鏂囨。璇峰弬鑰冿細[est-web-group README](./est-web-group/README.md)

#### 鍖呭惈妯″潡

- **est-web-router** - Web 璺敱
- **est-web-middleware** - Web 涓棿浠?- **est-web-session** - 浼氳瘽绠＄悊
- **est-web-template** - 妯℃澘寮曟搸
- **est-gateway** - API 缃戝叧

#### 蹇€熺ず渚?
```java
// API 缃戝叧
Gateway gateway = Gateway.create();
gateway.route("/api/users", "http://user-service:8080");
gateway.route("/api/orders", "http://order-service:8080");
gateway.start(8080);
```

### 6. est-ai-suite AI 濂椾欢妯″潡

鎻愪緵 AI 鍜?LLM 鐩稿叧鍔熻兘銆?
璇︾粏鏂囨。璇峰弬鑰冿細[est-ai-suite README](./est-ai-suite/README.md)

#### 鍖呭惈妯″潡

- **est-ai-config** - AI 閰嶇疆绠＄悊
- **est-llm-core** - 鏍稿績 LLM 鎶借薄
- **est-llm** - LLM 鎻愪緵鍟嗗疄鐜帮紙OpenAI銆佹櫤璋便€侀€氫箟鍗冮棶銆佹枃蹇冧竴瑷€銆佽眴鍖呫€並imi銆丱llama锛?- **est-ai-assistant** - AI 鍔╂墜鍜屼唬鐮佺敓鎴?
#### 蹇€熺ず渚?
```java
// AI 鍔╂墜
AiAssistant assistant = new DefaultAiAssistant();
String response = assistant.chat("浣犲ソ锛岃浠嬬粛涓€涓?EST 妗嗘灦");

// 浠ｇ爜鐢熸垚
CodeGenerator generator = new DefaultCodeGenerator();
String entityCode = generator.generateEntity("User", "com.example.entity", options);
```

### 7. est-microservices 寰湇鍔℃ā鍧?
鎻愪緵寰湇鍔＄浉鍏冲姛鑳姐€?
璇︾粏鏂囨。璇峰弬鑰冿細[est-microservices README](./est-microservices/README.md)

#### 鍖呭惈妯″潡

- **est-circuitbreaker** - 鐔旀柇鍣?- **est-discovery** - 鏈嶅姟鍙戠幇
- **est-performance** - 鎬ц兘浼樺寲

#### 蹇€熺ず渚?
```java
// 鐔旀柇鍣?CircuitBreaker breaker = CircuitBreaker.create();
breaker.execute(() -> {
    return remoteService.call();
});

// 鏈嶅姟鍙戠幇
ServiceDiscovery discovery = ServiceDiscovery.create();
List<ServiceInstance> instances = discovery.getInstances("user-service");
```

### 8. est-extensions 鎵╁睍鍔熻兘妯″潡

鎻愪緵鎵╁睍鍔熻兘銆?
璇︾粏鏂囨。璇峰弬鑰冿細[est-extensions README](./est-extensions/README.md)

#### 鍖呭惈妯″潡

- **est-plugin** - 鎻掍欢绯荤粺
- **est-scheduler** - 璋冨害绯荤粺锛堝浐瀹氥€丆ron锛?- **est-hotreload** - 鐑姞杞?
#### 蹇€熺ず渚?
```java
// 璋冨害
Scheduler scheduler = Scheduler.create();
scheduler.scheduleAtFixedRate(() -> {
    System.out.println("瀹氭椂浠诲姟鎵ц");
}, 1, TimeUnit.HOURS);

// 鎻掍欢
PluginManager pluginManager = PluginManager.create();
pluginManager.loadPlugin(Paths.get("./plugins/my-plugin.jar"));
```

---

## 鏈€浣冲疄璺?
### 1. 鎸夐渶寮曞叆妯″潡

```xml
<!-- 鉁?鎺ㄨ崘锛氬彧寮曞叆闇€瑕佺殑妯″潡 -->
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
</dependencies>

<!-- 鉂?涓嶆帹鑽愶細寮曞叆鎵€鏈夋ā鍧?-->
<!-- 浼氬鍔犱笉蹇呰鐨勪緷璧栧拰浣撶Н -->
```

### 2. 鍚堢悊缁勫悎妯″潡

```java
// 鉁?鎺ㄨ崘锛氱粍鍚堜娇鐢ㄥ涓ā鍧?Cache<String, User> cache = Caches.newMemoryCache();
Logger logger = Loggers.getLogger(MyClass.class);

public User getUser(Long id) {
    logger.info("鏌ヨ鐢ㄦ埛: " + id);
    
    User user = cache.get("user:" + id).orElse(null);
    if (user == null) {
        user = repository.findById(id).orElse(null);
        if (user != null) {
            cache.put("user:" + id, user);
        }
    }
    return user;
}
```

### 3. 鐞嗚В妯″潡渚濊禆鍏崇郴

```
est-modules/
鈹溾攢鈹€ est-foundation/    # 鍩虹灞傦紙鍏朵粬妯″潡鍙兘渚濊禆锛?鈹溾攢鈹€ est-data-group/    # 鏁版嵁灞?鈹溾攢鈹€ est-security-group/
鈹溾攢鈹€ est-integration-group/
鈹溾攢鈹€ est-web-group/
鈹溾攢鈹€ est-ai-suite/
鈹溾攢鈹€ est-microservices/
鈹斺攢鈹€ est-extensions/
```

---

## 妯″潡缁撴瀯

```
est-modules/
鈹溾攢鈹€ est-foundation/        # 鍩虹璁炬柦
鈹溾攢鈹€ est-data-group/        # 鏁版嵁璁块棶
鈹溾攢鈹€ est-security-group/    # 瀹夊叏鏉冮檺
鈹溾攢鈹€ est-integration-group/ # 娑堟伅闆嗘垚
鈹溾攢鈹€ est-web-group/         # Web 妗嗘灦
鈹溾攢鈹€ est-ai-suite/          # AI 濂椾欢
鈹溾攢鈹€ est-microservices/     # 寰湇鍔?鈹斺攢鈹€ est-extensions/        # 鎵╁睍鍔熻兘
```

---

## 鐩稿叧璧勬簮

- [est-foundation README](./est-foundation/README.md) - 鍩虹璁炬柦璇︾粏鏂囨。
- [est-data-group README](./est-data-group/README.md) - 鏁版嵁璁块棶璇︾粏鏂囨。
- [est-security-group README](./est-security-group/README.md) - 瀹夊叏鏉冮檺璇︾粏鏂囨。
- [est-web-group README](./est-web-group/README.md) - Web 妗嗘灦璇︾粏鏂囨。
- [est-ai-suite README](./est-ai-suite/README.md) - AI 濂椾欢璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../est-examples/) - 绀轰緥浠ｇ爜
- [EST Core](../est-core/README.md) - 鏍稿績妯″潡
- [EST App](../est-app/README.md) - 搴旂敤妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
