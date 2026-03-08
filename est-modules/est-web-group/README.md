# EST Web Group Web 妯″潡缁?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Web Group锛焆(#浠€涔堟槸-est-web-group)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Web Group锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Web Group 灏卞儚鏄竴涓?Web 宸ュ叿鍖?銆傛兂璞′竴涓嬩綘瑕佸缓涓€涓綉绔欙紝闇€瑕佸悇绉嶅伐鍏凤細

**浼犵粺鏂瑰紡**锛氫綘闇€瑕佽嚜宸辨惌寤鸿矾鐢便€佸鐞嗕腑闂翠欢銆佺鐞嗕細璇濄€佹覆鏌撴ā鏉?.. 寰堥夯鐑︼紒

**EST Web Group 鏂瑰紡**锛氱粰浣犱竴濂楀畬鏁寸殑 Web 寮€鍙戝伐鍏峰寘锛岄噷闈㈡湁锛?- 馃洠锔?**璺敱绯荤粺** - 瀹氫箟 URL 璺敱锛屽鐞嗚姹?- 馃敡 **涓棿浠剁閬?* - 鍦ㄨ姹傚墠鍚庡仛澶勭悊锛堟棩蹇椼€佽璇併€丆ORS 绛夛級
- 馃崻 **浼氳瘽绠＄悊** - 绠＄悊鐢ㄦ埛浼氳瘽锛岃浣忕櫥褰曠姸鎬?- 馃帹 **妯℃澘寮曟搸** - 娓叉煋鍔ㄦ€佺綉椤?- 馃毆 **API 缃戝叧** - 缁熶竴绠＄悊 API 鍏ュ彛锛岃矾鐢辫浆鍙?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍚姩 Web 鏈嶅姟
- 鈿?**楂樻€ц兘** - 鍩轰簬楂樻€ц兘鐨勫簳灞傚疄鐜?- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔変腑闂翠欢銆佹ā鏉跨瓑
- 馃帹 **鍔熻兘瀹屾暣** - 璺敱銆佷腑闂翠欢銆佷細璇濄€佹ā鏉夸竴搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓?Web 搴旂敤

```java
import ltd.idcu.est.web.router.WebRouter;
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

public class FirstWebApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Group 绗竴涓ず渚?===\n");
        
        WebRouter router = WebRouter.create();
        
        router.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        router.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        router.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "name", "寮犱笁",
                "age", 25,
                "email", "zhangsan@example.com"
            ));
        });
        
        router.listen(8080);
        System.out.println("Web 鏈嶅姟宸插惎鍔? http://localhost:8080");
    }
}
```

杩愯杩欎釜绋嬪簭锛岀劧鍚庤闂細
- http://localhost:8080 - 棣栭〉
- http://localhost:8080/hello/寮犱笁 - 涓€у寲闂€?- http://localhost:8080/api/user - JSON API

---

## 鍩虹绡?
### 1. est-web-router 璺敱绯荤粺

#### 瀹氫箟璺敱

```java
import ltd.idcu.est.web.router.WebRouter;

WebRouter router = WebRouter.create();

// GET 璇锋眰
router.get("/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

// POST 璇锋眰
router.post("/users", (req, res) -> {
    User user = req.bodyAs(User.class);
    User saved = userService.save(user);
    res.status(201).json(saved);
});

// PUT 璇锋眰
router.put("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    User user = req.bodyAs(User.class);
    User updated = userService.update(id, user);
    res.json(updated);
});

// DELETE 璇锋眰
router.delete("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    userService.delete(id);
    res.status(204).send();
});
```

#### 璺緞鍙傛暟鍜屾煡璇㈠弬鏁?
```java
router.get("/users/{id}/posts/{postId}", (req, res) -> {
    // 璺緞鍙傛暟
    String userId = req.pathParam("id");
    String postId = req.pathParam("postId");
    
    // 鏌ヨ鍙傛暟
    String page = req.queryParam("page", "1");
    int limit = req.queryParamAsInt("limit", 10);
    
    res.send("鐢ㄦ埛 " + userId + " 鐨勬枃绔?" + postId + ", 绗?" + page + " 椤?);
});
```

#### 璇锋眰鍜屽搷搴?
```java
router.get("/example", (req, res) -> {
    // 鑾峰彇璇锋眰澶?    String userAgent = req.header("User-Agent");
    
    // 鑾峰彇璇锋眰浣?    User user = req.bodyAs(User.class);
    
    // 鍙戦€佹枃鏈搷搴?    res.send("Hello!");
    
    // 鍙戦€?JSON 鍝嶅簲
    res.json(user);
    
    // 璁剧疆鐘舵€佺爜
    res.status(404).send("Not Found");
    
    // 璁剧疆鍝嶅簲澶?    res.header("Content-Type", "application/json");
    
    // 璁剧疆 Cookie
    res.cookie("sessionId", "abc123");
});
```

### 2. est-web-middleware 涓棿浠?
#### 浠€涔堟槸涓棿浠讹紵

涓棿浠跺氨鍍忔槸"杩囨护鍣?锛屽彲浠ュ湪璇锋眰鍒拌揪澶勭悊鍣ㄤ箣鍓嶅拰鍝嶅簲杩斿洖涔嬪悗鍋氫竴浜涘鐞嗐€?
```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

// 鏃ュ織涓棿浠?Middleware loggingMiddleware = (req, res, chain) -> {
    System.out.println(req.method() + " " + req.path());
    long start = System.currentTimeMillis();
    
    chain.proceed();
    
    long duration = System.currentTimeMillis() - start;
    System.out.println("鑰楁椂: " + duration + "ms");
};

// 浣跨敤涓棿浠?router.use(loggingMiddleware);
```

#### 甯哥敤涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;

// CORS 涓棿浠?router.use(Middlewares.cors());

// 瀹夊叏涓棿浠讹紙XSS銆丆SRF 闃叉姢锛?router.use(Middlewares.security());

// 鍘嬬缉涓棿浠?router.use(Middlewares.gzip());

// 闄愭祦涓棿浠?router.use(Middlewares.rateLimit(100, 60)); // 姣忓垎閽?100 娆¤姹?```

### 3. est-web-session 浼氳瘽绠＄悊

#### 鍚敤浼氳瘽

```java
import ltd.idcu.est.web.session.SessionManager;
import ltd.idcu.est.web.session.MemorySessionStore;

SessionManager sessionManager = new SessionManager(new MemorySessionStore());
router.use(sessionManager.middleware());

router.get("/login", (req, res) -> {
    req.session().set("userId", 123L);
    req.session().set("username", "寮犱笁");
    res.send("鐧诲綍鎴愬姛");
});

router.get("/profile", (req, res) -> {
    Long userId = req.session().get("userId");
    String username = req.session().get("username");
    res.json(Map.of("userId", userId, "username", username));
});

router.get("/logout", (req, res) -> {
    req.session().invalidate();
    res.send("鐧诲嚭鎴愬姛");
});
```

### 4. est-web-template 妯℃澘寮曟搸

#### 娓叉煋妯℃澘

```java
import ltd.idcu.est.web.template.TemplateEngine;
import ltd.idcu.est.web.template.ThymeleafEngine;

TemplateEngine templateEngine = new ThymeleafEngine("templates/");
router.setTemplateEngine(templateEngine);

router.get("/", (req, res) -> {
    res.render("index.html", Map.of(
        "title", "棣栭〉",
        "message", "娆㈣繋鏉ュ埌 EST Web!"
    ));
});
```

### 5. est-gateway API 缃戝叧

#### 閰嶇疆缃戝叧

```java
import ltd.idcu.est.web.gateway.ApiGateway;
import ltd.idcu.est.web.gateway.Route;

ApiGateway gateway = ApiGateway.create();

// 閰嶇疆璺敱
gateway.route("/api/users/**")
    .to("http://user-service:8081")
    .stripPrefix(1); // 鍘绘帀 /api/users 鍓嶇紑

gateway.route("/api/orders/**")
    .to("http://order-service:8082")
    .stripPrefix(1);

// 娣诲姞缃戝叧涓棿浠?gateway.use(loggingMiddleware);
gateway.use(authMiddleware);

// 鍚姩缃戝叧
gateway.listen(9000);
```

---

## 杩涢樁绡?
### 1. 璺敱鍒嗙粍

```java
router.group("/api", () -> {
    router.group("/users", () -> {
        router.get("", listUsers);
        router.get("/{id}", getUser);
        router.post("", createUser);
        router.put("/{id}", updateUser);
        router.delete("/{id}", deleteUser);
    });
    
    router.group("/orders", () -> {
        router.get("", listOrders);
        router.get("/{id}", getOrder);
        router.post("", createOrder);
    });
});
```

### 2. 鑷畾涔変細璇濆瓨鍌?
```java
import ltd.idcu.est.web.session.SessionStore;
import ltd.idcu.est.web.session.Session;

public class RedisSessionStore implements SessionStore {
    private final RedisClient redis;
    
    public RedisSessionStore(RedisClient redis) {
        this.redis = redis;
    }
    
    @Override
    public Session get(String sessionId) {
        return redis.get("session:" + sessionId);
    }
    
    @Override
    public void save(Session session) {
        redis.setex("session:" + session.getId(), 3600, session);
    }
    
    @Override
    public void delete(String sessionId) {
        redis.del("session:" + sessionId);
    }
}

SessionManager sessionManager = new SessionManager(new RedisSessionStore(redis));
```

### 3. 缃戝叧璐熻浇鍧囪　

```java
import ltd.idcu.est.web.gateway.LoadBalancer;
import ltd.idcu.est.web.gateway.RoundRobinLoadBalancer;

LoadBalancer loadBalancer = new RoundRobinLoadBalancer(
    "http://user-service-1:8081",
    "http://user-service-2:8081",
    "http://user-service-3:8081"
);

gateway.route("/api/users/**")
    .to(loadBalancer)
    .stripPrefix(1);
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊缁勭粐璺敱

```java
// 鉁?鎺ㄨ崘锛氭寜鍔熻兘鍒嗙粍
router.group("/api", () -> {
    router.group("/users", () -> {
        // 鐢ㄦ埛鐩稿叧璺敱
    });
    router.group("/orders", () -> {
        // 璁㈠崟鐩稿叧璺敱
    });
});

// 鉂?涓嶆帹鑽愶細闆舵暎鐨勮矾鐢?router.get("/api/users", listUsers);
router.get("/api/users/:id", getUser);
// ...
```

### 2. 浣跨敤涓棿浠跺鐞嗘í鍒囧叧娉ㄧ偣

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄤ腑闂翠欢
router.use(loggingMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);

// 鉂?涓嶆帹鑽愶細姣忎釜璺敱閮介噸澶嶅啓
router.get("/api/users", (req, res) -> {
    // 閲嶅鐨勬棩蹇椾唬鐮?    // 閲嶅鐨勮璇佷唬鐮?    // ...
});
```

### 3. 鍚堢悊浣跨敤浼氳瘽

```java
// 鉁?鎺ㄨ崘锛氬彧鍦ㄤ細璇濅腑瀛樺偍蹇呰淇℃伅
req.session().set("userId", user.getId());
req.session().set("username", user.getName());

// 鉂?涓嶆帹鑽愶細鍦ㄤ細璇濅腑瀛樺偍澶ч噺鏁版嵁
req.session().set("user", user);  // 鏁翠釜鐢ㄦ埛瀵硅薄
req.session().set("permissions", permissions);  // 澶ч噺鏉冮檺鏁版嵁
```

### 4. 缃戝叧瀹夊叏

```java
// 鉁?鎺ㄨ崘锛氬湪缃戝叧灞傜粺涓€澶勭悊璁よ瘉鍜岄檺娴?gateway.use(authMiddleware);
gateway.use(rateLimitMiddleware);
gateway.use(securityMiddleware);

// 鍚庣鏈嶅姟鍙叧娉ㄤ笟鍔￠€昏緫
```

---

## 妯″潡缁撴瀯

```
est-web-group/
鈹溾攢鈹€ est-web-router/      # 璺敱绯荤粺
鈹溾攢鈹€ est-web-middleware/  # 涓棿浠剁閬?鈹溾攢鈹€ est-web-session/     # 浼氳瘽绠＄悊
鈹溾攢鈹€ est-web-template/    # 妯℃澘寮曟搸
鈹斺攢鈹€ est-gateway/         # API 缃戝叧
```

---

## 鐩稿叧璧勬簮

- [est-gateway README](./est-gateway/README.md) - API 缃戝叧璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-web/) - Web 绀轰緥浠ｇ爜
- [EST App](../../est-app/README.md) - 搴旂敤妯″潡
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
