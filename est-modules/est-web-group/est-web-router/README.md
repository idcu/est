# EST Web Router Web 璺敱妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Web Router锛焆(#浠€涔堟槸-est-web-router)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Web Router锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Web Router 灏卞儚鏄竴涓?浜ら€氭寚鎸ヤ腑蹇?銆傛兂璞′竴涓嬩綘鐨勭綉绔欐湁寰堝椤甸潰锛岀敤鎴疯闂笉鍚岀殑 URL 闇€瑕佹壘鍒板搴旂殑澶勭悊绋嬪簭锛?
**浼犵粺鏂瑰紡**锛氫綘闇€瑕佽嚜宸卞啓涓€澶у爢 if-else 鎴栬€?switch 鏉ュ垽鏂?URL 瑕佽皟鐢ㄥ摢涓嚱鏁帮紝寰堥夯鐑︼紒

**EST Web Router 鏂瑰紡**锛氱粰浣犱竴濂楁櫤鑳界殑璺敱绯荤粺锛屾敮鎸侊細
- 馃洠锔?**璺緞鍙傛暟** - `/users/{id}` 鍖归厤 `/users/123`
- 馃攳 **鏌ヨ鍙傛暟** - `?page=1&limit=10`
- 馃摝 **璺敱鍒嗙粍** - 鎶婄浉鍏崇殑璺敱缁勭粐鍦ㄤ竴璧?- 馃彈锔?**璺敱鏍?* - 楂樻€ц兘鐨?Trie 鏍戝疄鐜?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘瀹氫箟璺敱
- 鈿?**楂樻€ц兘** - 鍩轰簬 Trie 鏍戠殑璺敱鍖归厤
- 馃敡 **鐏垫椿寮哄ぇ** - 鏀寔璺緞鍙傛暟銆侀€氶厤绗︺€佽矾鐢卞垎缁?- 馃帹 **RESTful 鍙嬪ソ** - 鏀寔 GET銆丳OST銆丳UT銆丏ELETE 绛?HTTP 鏂规硶

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router-api</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web-router-impl</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓矾鐢卞簲鐢?
```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;
import ltd.idcu.est.web.router.Request;
import ltd.idcu.est.web.router.Response;

public class FirstRouterApp {
    public static void main(String[] args) {
        System.out.println("=== EST Web Router 绗竴涓ず渚?===\n");
        
        Router router = Routers.create();
        
        router.get("/", (req, res) -> {
            res.send("Hello, EST Router!");
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
        
        System.out.println("璺敱宸查厤缃紒");
        System.out.println("璁块棶 http://localhost:8080/ 鏌ョ湅棣栭〉");
        System.out.println("璁块棶 http://localhost:8080/hello/寮犱笁 鏌ョ湅涓€у寲闂€?);
    }
}
```

---

## 鍩虹绡?
### 1. 瀹氫箟璺敱

#### 鍩烘湰璺敱

```java
import ltd.idcu.est.web.router.Router;
import ltd.idcu.est.web.router.Routers;

Router router = Routers.create();

router.get("/", (req, res) -> res.send("棣栭〉"));
router.post("/users", (req, res) -> res.send("鍒涘缓鐢ㄦ埛"));
router.put("/users/{id}", (req, res) -> res.send("鏇存柊鐢ㄦ埛"));
router.delete("/users/{id}", (req, res) -> res.send("鍒犻櫎鐢ㄦ埛"));
```

#### HTTP 鏂规硶鏀寔

```java
Router router = Routers.create();

router.get("/users", listUsers);
router.post("/users", createUser);
router.put("/users/{id}", updateUser);
router.delete("/users/{id}", deleteUser);
router.patch("/users/{id}", patchUser);
router.head("/users/{id}", headUser);
router.options("/users", optionsUser);
```

### 2. 璺緞鍙傛暟

#### 绠€鍗曡矾寰勫弬鏁?
```java
Router router = Routers.create();

router.get("/users/{id}", (req, res) -> {
    String id = req.pathParam("id");
    res.send("鐢ㄦ埛 ID: " + id);
});

router.get("/posts/{postId}/comments/{commentId}", (req, res) -> {
    String postId = req.pathParam("postId");
    String commentId = req.pathParam("commentId");
    res.send("鏂囩珷 " + postId + " 鐨勮瘎璁?" + commentId);
});
```

#### 閫氶厤绗﹀尮閰?
```java
Router router = Routers.create();

router.get("/files/*", (req, res) -> {
    String path = req.pathParam("*");
    res.send("鏂囦欢璺緞: " + path);
});

router.get("/api/**", (req, res) -> {
    String path = req.path();
    res.send("API 璺緞: " + path);
});
```

### 3. 鏌ヨ鍙傛暟

#### 鑾峰彇鏌ヨ鍙傛暟

```java
Router router = Routers.create();

router.get("/search", (req, res) -> {
    String keyword = req.queryParam("keyword", "");
    int page = req.queryParamAsInt("page", 1);
    int limit = req.queryParamAsInt("limit", 10);
    
    res.json(Map.of(
        "keyword", keyword,
        "page", page,
        "limit", limit
    ));
});

router.get("/users", (req, res) -> {
    String sort = req.queryParam("sort", "id");
    boolean ascending = req.queryParamAsBoolean("asc", true);
    
    res.send("鎺掑簭瀛楁: " + sort + ", 鍗囧簭: " + ascending);
});
```

### 4. 璇锋眰鍜屽搷搴?
#### 璇锋眰瀵硅薄

```java
import ltd.idcu.est.web.router.Request;

router.get("/example", (req, res) -> {
    String method = req.method();
    String path = req.path();
    String userAgent = req.header("User-Agent");
    String contentType = req.header("Content-Type");
    
    String body = req.bodyAsString();
    User user = req.bodyAs(User.class);
    
    res.send("璇锋眰淇℃伅宸茶幏鍙?);
});
```

#### 鍝嶅簲瀵硅薄

```java
import ltd.idcu.est.web.router.Response;

router.get("/response", (req, res) -> {
    res.send("鏂囨湰鍝嶅簲");
    res.json(Map.of("key", "value"));
    res.status(201).send("宸插垱寤?);
    res.status(404).send("鏈壘鍒?);
    
    res.header("Content-Type", "application/json");
    res.cookie("sessionId", "abc123");
    res.cookie("rememberMe", "true", 86400);
});
```

---

## 杩涢樁绡?
### 1. 璺敱鍒嗙粍

#### 鍒嗙粍璺敱

```java
Router router = Routers.create();

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

#### 鍒嗙粍涓棿浠?
```java
Router router = Routers.create();

router.group("/api", () -> {
    router.use(authMiddleware);
    router.use(loggingMiddleware);
    
    router.get("/users", listUsers);
    router.get("/orders", listOrders);
});

router.get("/public", publicHandler);
```

### 2. 璺敱鍓嶇紑鍜屽瓙璺敱

#### 瀛愯矾鐢辨寕杞?
```java
Router apiRouter = Routers.create();
apiRouter.get("/users", listUsers);
apiRouter.get("/orders", listOrders);

Router mainRouter = Routers.create();
mainRouter.mount("/api/v1", apiRouter);
```

### 3. 璺敱姝ｅ垯琛ㄨ揪寮?
#### 姝ｅ垯璺緞鍙傛暟

```java
Router router = Routers.create();

router.get("/users/{id:\\d+}", (req, res) -> {
    String id = req.pathParam("id");
    res.send("鏁板瓧 ID: " + id);
});

router.get("/files/{path:.+\\.(png|jpg|gif)}", (req, res) -> {
    String path = req.pathParam("path");
    res.send("鍥剧墖鏂囦欢: " + path);
});
```

---

## 鏈€浣冲疄璺?
### 1. 璺敱缁勭粐

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
router.get("/api/orders", listOrders);
```

### 2. 璺緞鍙傛暟鍛藉悕

```java
// 鉁?鎺ㄨ崘锛氭竻鏅扮殑鍙傛暟鍚?router.get("/users/{userId}/posts/{postId}", handler);

// 鉂?涓嶆帹鑽愶細妯＄硦鐨勫弬鏁板悕
router.get("/users/{a}/posts/{b}", handler);
```

### 3. RESTful 椋庢牸

```java
// 鉁?鎺ㄨ崘锛歊ESTful 璺敱璁捐
router.get("/users", listUsers);          // 鑾峰彇鍒楄〃
router.get("/users/{id}", getUser);      // 鑾峰彇鍗曚釜
router.post("/users", createUser);       // 鍒涘缓
router.put("/users/{id}", updateUser);   // 鏇存柊
router.delete("/users/{id}", deleteUser);// 鍒犻櫎

// 鉂?涓嶆帹鑽愶細鍔ㄨ瘝鍦ㄨ矾寰勪腑
router.get("/getUsers", listUsers);
router.post("/createUser", createUser);
```

---

## 妯″潡缁撴瀯

```
est-web-router/
鈹溾攢鈹€ est-web-router-api/    # 璺敱 API
鈹斺攢鈹€ est-web-router-impl/   # 璺敱瀹炵幇
```

---

## 鐩稿叧璧勬簮

- [est-web-middleware README](../est-web-middleware/README.md) - 涓棿浠舵枃妗?- [est-web-session README](../est-web-session/README.md) - 浼氳瘽绠＄悊鏂囨。
- [EST Web Group README](../README.md) - Web 妯″潡缁勬枃妗?- [绀轰緥浠ｇ爜](../../../est-examples/est-examples-web/) - Web 绀轰緥浠ｇ爜

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
