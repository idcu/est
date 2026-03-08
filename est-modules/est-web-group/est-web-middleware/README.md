# EST Web Middleware Web 涓棿浠舵ā鍧?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Web Middleware锛焆(#浠€涔堟槸-est-web-middleware)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Web Middleware锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Web Middleware 灏卞儚鏄竴涓?妫€鏌ョ珯"銆傛兂璞′竴涓嬫満鍦哄畨妫€锛屼箻瀹㈠湪鐧绘満鍓嶈缁忚繃瀹夋锛?
**浼犵粺鏂瑰紡**锛氭瘡涓埅鐝兘瑕佽嚜宸卞畨鎺掑畨妫€锛岄噸澶嶅伐浣滐紒

**EST Web Middleware 鏂瑰紡**锛氱粰浣犱竴濂椾腑闂翠欢绠￠亾锛屽湪璇锋眰鍒拌揪澶勭悊鍣ㄤ箣鍓嶅拰鍝嶅簲杩斿洖涔嬪悗鍋氬鐞嗭細
- 馃摑 **鏃ュ織涓棿浠?* - 璁板綍璇锋眰鏃ュ織
- 馃攼 **璁よ瘉涓棿浠?* - 楠岃瘉鐢ㄦ埛韬唤
- 馃寪 **CORS 涓棿浠?* - 澶勭悊璺ㄥ煙璇锋眰
- 馃洝锔?**瀹夊叏涓棿浠?* - 闃叉姢 XSS銆丆SRF 鏀诲嚮
- 鈿?**鍘嬬缉涓棿浠?* - Gzip 鍘嬬缉鍝嶅簲

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘娣诲姞涓棿浠?- 鈿?**楂樻€ц兘** - 浼樺寲鐨勪腑闂翠欢绠￠亾
- 馃敡 **鐏垫椿寮哄ぇ** - 鏀寔鑷畾涔変腑闂翠欢
- 馃帹 **鍔熻兘瀹屾暣** - 鍐呯疆甯哥敤涓棿浠?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware-api</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-middleware-impl</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓腑闂翠欢搴旂敤

```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;
import ltd.idcu.est.web.middleware.Middlewares;

public class FirstMiddlewareApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Middleware 绗竴涓ず渚?===\n");
        
        Router router = Routers.create();
        
        Middleware loggingMiddleware = (req, res, chain) -> {
            System.out.println(req.method() + " " + req.path());
            long start = System.currentTimeMillis();
            
            chain.proceed();
            
            long duration = System.currentTimeMillis() - start;
            System.out.println("鑰楁椂: " + duration + "ms");
        };
        
        router.use(loggingMiddleware);
        router.use(Middlewares.cors());
        router.use(Middlewares.security());
        
        router.get("/", (req, res) -> {
            res.send("Hello, Middleware!");
        });
        
        System.out.println("涓棿浠跺凡閰嶇疆锛?);
    }
}
```

---

## 鍩虹绡?
### 1. 鍐呯疆涓棿浠?
#### 鏃ュ織涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;

Router router = Routers.create();
router.use(Middlewares.logging());
```

#### CORS 涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.CorsConfig;

CorsConfig config = CorsConfig.builder()
    .allowOrigin("*")
    .allowMethods("GET", "POST", "PUT", "DELETE")
    .allowHeaders("Content-Type", "Authorization")
    .maxAge(3600)
    .build();

Router router = Routers.create();
router.use(Middlewares.cors(config));
```

#### 瀹夊叏涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.SecurityConfig;

SecurityConfig config = SecurityConfig.builder()
    .xssProtection(true)
    .frameOptions("DENY")
    .contentTypeOptions(true)
    .hsts(true)
    .build();

Router router = Routers.create();
router.use(Middlewares.security(config));
```

#### 鍘嬬缉涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.CompressionConfig;

CompressionConfig config = CompressionConfig.builder()
    .minSize(1024)
    .mimeTypes("text/plain", "text/html", "application/json")
    .build();

Router router = Routers.create();
router.use(Middlewares.gzip(config));
```

#### 闄愭祦涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middlewares;
import ltd.idcu.est.web.middleware.RateLimitConfig;

RateLimitConfig config = RateLimitConfig.builder()
    .requestsPerMinute(100)
    .burst(20)
    .build();

Router router = Routers.create();
router.use(Middlewares.rateLimit(config));
```

### 2. 鑷畾涔変腑闂翠欢

#### 绠€鍗曚腑闂翠欢

```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware myMiddleware = (req, res, chain) -> {
    System.out.println("璇锋眰鍓?);
    
    chain.proceed();
    
    System.out.println("鍝嶅簲鍚?);
};

Router router = Routers.create();
router.use(myMiddleware);
```

#### 璁よ瘉涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware authMiddleware = (req, res, chain) -> {
    String token = req.header("Authorization");
    
    if (token == null || !validateToken(token)) {
        res.status(401).send("鏈巿鏉?);
        return;
    }
    
    req.setAttribute("user", getUserFromToken(token));
    chain.proceed();
};

Router router = Routers.create();
router.use("/api/**", authMiddleware);
```

### 3. 涓棿浠堕『搴?
#### 鎵ц椤哄簭

```java
Router router = Routers.create();

router.use(loggingMiddleware);      // 1. 鏈€鍏堟墽琛?router.use(corsMiddleware);         // 2.
router.use(authMiddleware);         // 3.
router.use(compressionMiddleware);  // 4.

router.get("/", handler);           // 鏈€鍚庢墽琛?```

#### 鏉′欢涓棿浠?
```java
Router router = Routers.create();

router.use("/api/**", authMiddleware);
router.use("/admin/**", adminMiddleware);

router.get("/public", publicHandler);
```

---

## 杩涢樁绡?
### 1. 涓棿浠堕摼鎺у埗

#### 涓柇涓棿浠堕摼

```java
Middleware earlyReturnMiddleware = (req, res, chain) -> {
    if (someCondition) {
        res.send("鎻愬墠杩斿洖");
        return;
    }
    
    chain.proceed();
};
```

#### 閿欒澶勭悊涓棿浠?
```java
Middleware errorHandlerMiddleware = (req, res, chain) -> {
    try {
        chain.proceed();
    } catch (Exception e) {
        res.status(500).json(Map.of(
            "error", "鏈嶅姟鍣ㄩ敊璇?,
            "message", e.getMessage()
        ));
    }
};

Router router = Routers.create();
router.use(errorHandlerMiddleware);
```

### 2. 涓棿浠剁粍鍚?
#### 缁勫悎澶氫釜涓棿浠?
```java
import ltd.idcu.est.web.middleware.Middleware;
import ltd.idcu.est.web.middleware.MiddlewareChain;

Middleware combined = Middlewares.combine(
    loggingMiddleware,
    corsMiddleware,
    authMiddleware,
    compressionMiddleware
);

Router router = Routers.create();
router.use(combined);
```

### 3. 涓棿浠朵紭鍏堢骇

#### 璁剧疆浼樺厛绾?
```java
import ltd.idcu.est.web.middleware.Middleware;

Middleware highPriority = Middlewares.withPriority(
    loggingMiddleware,
    100
);

Middleware lowPriority = Middlewares.withPriority(
    compressionMiddleware,
    1
);

Router router = Routers.create();
router.use(highPriority);
router.use(lowPriority);
```

---

## 鏈€浣冲疄璺?
### 1. 涓棿浠堕『搴?
```java
// 鉁?鎺ㄨ崘锛氬悎鐞嗙殑涓棿浠堕『搴?router.use(loggingMiddleware);      // 1. 璁板綍璇锋眰
router.use(corsMiddleware);         // 2. 澶勭悊 CORS
router.use(authMiddleware);         // 3. 璁よ瘉
router.use(compressionMiddleware);  // 4. 鍘嬬缉鍝嶅簲
router.use(errorHandlerMiddleware); // 5. 閿欒澶勭悊

// 鉂?涓嶆帹鑽愶細閿欒鐨勯『搴?router.use(compressionMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);
router.use(loggingMiddleware);
```

### 2. 涓棿浠剁矑搴?
```java
// 鉁?鎺ㄨ崘锛氬崟涓€鑱岃矗鐨勪腑闂翠欢
router.use(loggingMiddleware);
router.use(authMiddleware);
router.use(corsMiddleware);

// 鉂?涓嶆帹鑽愶細澶ц€屽叏鐨勪腑闂翠欢
router.use(giantMiddleware); // 浠€涔堥兘鍋氾紝闅句互缁存姢
```

### 3. 鏉′欢浣跨敤

```java
// 鉁?鎺ㄨ崘锛氬彧鍦ㄩ渶瑕佺殑璺緞浣跨敤涓棿浠?router.use("/api/**", authMiddleware);
router.use("/admin/**", adminMiddleware);

// 鉂?涓嶆帹鑽愶細鎵€鏈夎矾寰勯兘浣跨敤
router.use(authMiddleware);
```

---

## 妯″潡缁撴瀯

```
est-web-middleware/
鈹溾攢鈹€ est-web-middleware-api/    # 涓棿浠?API
鈹斺攢鈹€ est-web-middleware-impl/   # 涓棿浠跺疄鐜?```

---

## 鐩稿叧璧勬簮

- [est-web-router README](../est-web-router/README.md) - 璺敱鏂囨。
- [est-web-session README](../est-web-session/README.md) - 浼氳瘽绠＄悊鏂囨。
- [EST Web Group README](../README.md) - Web 妯″潡缁勬枃妗?- [绀轰緥浠ｇ爜](../../../est-examples/est-examples-web/) - Web 绀轰緥浠ｇ爜

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
