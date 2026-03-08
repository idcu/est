# EST App 搴旂敤妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST App锛焆(#浠€涔堟槸-est-app)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST App锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST App 灏卞儚鏄竴涓?搴旂敤妯℃澘"銆傛兂璞′竴涓嬩綘瑕佺洊鎴垮瓙锛岄渶瑕佹墦濂藉湴鍩恒€佹惌濂芥鏋躲€佽濂介棬绐?..

**浼犵粺鏂瑰紡**锛氭瘡娆＄洊鎴垮瓙閮借浠庡ご寮€濮嬶紝寰堥夯鐑︺€?
**EST App 鏂瑰紡**锛氱粰浣犵幇鎴愮殑搴旂敤妯℃澘锛岄噷闈㈡湁锛?- 馃寪 **Web 搴旂敤妗嗘灦** - 蹇€熷垱寤?Web 搴旂敤
- 馃敡 **绠＄悊鍚庡彴** - 瀹屾暣鐨勫墠鍚庣鍒嗙绠＄悊绯荤粺
- 馃捇 **鎺у埗鍙板簲鐢?* - 鍛戒护琛屽簲鐢ㄦ敮鎸?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓搴旂敤
- 鈿?**寮€绠卞嵆鐢?* - 棰勭疆甯哥敤鍔熻兘
- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夊拰鎵╁睍
- 馃帹 **澶氱绫诲瀷** - 鏀寔 Web銆佺鐞嗗悗鍙般€佹帶鍒跺彴搴旂敤

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-web</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓?Web 搴旂敤

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

public class FirstWebApp {
    public static void main(String[] args) {
        System.out.println("=== EST App 绗竴涓ず渚?===\n");
        
        WebApplication app = Web.create("鎴戠殑绗竴涓?Web 搴旂敤", "1.0.0");
        
        app.get("/", (req, res) -> res.send("Hello, EST App!"));
        
        app.get("/hello/{name}", (req, res) -> {
            String name = req.pathParam("name");
            res.send("Hello, " + name + "!");
        });
        
        app.get("/api/user", (req, res) -> {
            res.json(Map.of(
                "name", "寮犱笁",
                "age", 25,
                "email", "zhangsan@example.com"
            ));
        });
        
        app.run(8080);
        System.out.println("搴旂敤宸插惎鍔? http://localhost:8080");
    }
}
```

杩愯杩欎釜绋嬪簭锛岀劧鍚庤闂細
- http://localhost:8080 - 棣栭〉
- http://localhost:8080/hello/寮犱笁 - 涓€у寲闂€?- http://localhost:8080/api/user - JSON API

---

## 鍩虹绡?
### 1. est-web Web 搴旂敤妗嗘灦

璇︾粏鏂囨。璇峰弬鑰冿細[est-web README](./est-web/README.md)

#### 鍒涘缓 Web 搴旂敤

```java
import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;

WebApplication app = Web.create("鎴戠殑搴旂敤", "1.0.0");
```

#### 娣诲姞璺敱

```java
// GET 璇锋眰
app.get("/users", (req, res) -> {
    List<User> users = userService.findAll();
    res.json(users);
});

// POST 璇锋眰
app.post("/users", (req, res) -> {
    User user = req.bodyAs(User.class);
    User saved = userService.save(user);
    res.status(201).json(saved);
});

// PUT 璇锋眰
app.put("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    User user = req.bodyAs(User.class);
    User updated = userService.update(id, user);
    res.json(updated);
});

// DELETE 璇锋眰
app.delete("/users/{id}", (req, res) -> {
    Long id = Long.parseLong(req.pathParam("id"));
    userService.delete(id);
    res.status(204).send();
});
```

#### 璇锋眰鍜屽搷搴?
```java
app.get("/example", (req, res) -> {
    // 鑾峰彇璺緞鍙傛暟
    String id = req.pathParam("id");
    
    // 鑾峰彇鏌ヨ鍙傛暟
    String name = req.queryParam("name");
    int page = req.queryParamAsInt("page", 1);
    
    // 鑾峰彇璇锋眰澶?    String userAgent = req.header("User-Agent");
    
    // 鑾峰彇璇锋眰浣?    User user = req.bodyAs(User.class);
    
    // 鍙戦€佹枃鏈搷搴?    res.send("Hello!");
    
    // 鍙戦€?JSON 鍝嶅簲
    res.json(user);
    
    // 璁剧疆鐘舵€佺爜
    res.status(404).send("Not Found");
    
    // 璁剧疆鍝嶅簲澶?    res.header("Content-Type", "application/json");
});
```

### 2. est-admin 绠＄悊鍚庡彴

EST Admin 鏄竴涓畬鏁寸殑鍓嶅悗绔垎绂荤鐞嗙郴缁熴€?
#### 鍚姩绠＄悊鍚庡彴

```java
import ltd.idcu.est.admin.Admin;

public class AdminApp {
    public static void main(String[] args) {
        Admin admin = Admin.create();
        admin.run(8080);
        System.out.println("绠＄悊鍚庡彴宸插惎鍔? http://localhost:8080");
    }
}
```

#### 绠＄悊鍚庡彴鍔熻兘

- 馃懃 **鐢ㄦ埛绠＄悊** - 鐢ㄦ埛澧炲垹鏀规煡
- 馃彚 **閮ㄩ棬绠＄悊** - 閮ㄩ棬缁勭粐鏋舵瀯
- 馃憯 **瑙掕壊绠＄悊** - 瑙掕壊鏉冮檺閰嶇疆
- 馃搵 **鑿滃崟绠＄悊** - 鑿滃崟鏉冮檺鎺у埗
- 馃彚 **绉熸埛绠＄悊** - 澶氱鎴锋敮鎸?- 馃搳 **鍦ㄧ嚎鐢ㄦ埛** - 鍦ㄧ嚎鐢ㄦ埛鐩戞帶
- 馃摑 **鎿嶄綔鏃ュ織** - 鎿嶄綔璁板綍瀹¤
- 馃搱 **鐧诲綍鏃ュ織** - 鐧诲綍璁板綍鏌ヨ
- 馃敡 **绯荤粺鐩戞帶** - 绯荤粺鐘舵€佺洃鎺?
#### 鍓嶇

绠＄悊鍚庡彴鍓嶇浣跨敤 Vue 3 + Element Plus锛岃缁嗕俊鎭鍙傝€冿細[est-admin-ui README](../est-admin-ui/README.md)

### 3. est-console 鎺у埗鍙板簲鐢?
EST Console 鐢ㄤ簬鍒涘缓鍛戒护琛屽簲鐢ㄣ€?
```java
import ltd.idcu.est.console.Console;

public class ConsoleApp {
    public static void main(String[] args) {
        Console console = Console.create("鎴戠殑鎺у埗鍙板簲鐢?, "1.0.0");
        
        console.addCommand("hello", () -> {
            System.out.println("Hello, Console!");
        });
        
        console.addCommand("greet", (name) -> {
            System.out.println("Hello, " + name + "!");
        });
        
        console.run();
    }
}
```

---

## 杩涢樁绡?
### 1. Web 搴旂敤杩涢樁

璇︾粏鍐呭璇峰弬鑰冿細[est-web 杩涢樁绡嘳(./est-web/README.md)

#### 涓棿浠?
```java
import ltd.idcu.est.web.api.Middleware;

// 鏃ュ織涓棿浠?Middleware loggingMiddleware = (req, res, next) -> {
    System.out.println(req.method() + " " + req.path());
    next.proceed();
};

app.use(loggingMiddleware);

// CORS 涓棿浠?app.use(Web.corsMiddleware());

// 瀹夊叏涓棿浠?app.use(Web.securityMiddleware());
```

#### 闈欐€佹枃浠?
```java
// 鏈嶅姟闈欐€佹枃浠?app.staticFiles("/static", "src/main/resources/static");

// 棣栭〉
app.get("/", (req, res) -> {
    res.render("index.html", Map.of("title", "棣栭〉"));
});
```

#### 浼氳瘽绠＄悊

```java
app.get("/login", (req, res) -> {
    req.session().set("user", user);
    res.send("鐧诲綍鎴愬姛");
});

app.get("/profile", (req, res) -> {
    User user = req.session().get("user");
    res.json(user);
});

app.get("/logout", (req, res) -> {
    req.session().invalidate();
    res.send("鐧诲嚭鎴愬姛");
});
```

### 2. 绠＄悊鍚庡彴杩涢樁

#### 鑷畾涔夋湇鍔?
```java
import ltd.idcu.est.admin.Admin;
import ltd.idcu.est.admin.api.UserService;

Admin admin = Admin.create();

// 鑷畾涔夌敤鎴锋湇鍔?admin.setUserService(new MyUserService());

admin.run(8080);
```

#### 娣诲姞鑷畾涔?API

```java
Admin admin = Admin.create();

WebApplication app = admin.getWebApplication();

app.get("/api/custom", (req, res) -> {
    res.json(Map.of("message", "鑷畾涔?API"));
});

admin.run(8080);
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊缁勭粐璺敱

```java
// 鉁?鎺ㄨ崘锛氭寜鍔熻兘鍒嗙粍
app.group("/api/users", () -> {
    app.get("", listUsers);
    app.get("/{id}", getUser);
    app.post("", createUser);
    app.put("/{id}", updateUser);
    app.delete("/{id}", deleteUser);
});

// 鉂?涓嶆帹鑽愶細闆舵暎鐨勮矾鐢?app.get("/api/users", listUsers);
app.get("/api/users/:id", getUser);
// ...
```

### 2. 浣跨敤涓棿浠?
```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄤ腑闂翠欢澶勭悊妯垏鍏虫敞鐐?app.use(loggingMiddleware);
app.use(authMiddleware);
app.use(corsMiddleware);

// 鉂?涓嶆帹鑽愶細姣忎釜璺敱閮介噸澶嶅啓
app.get("/api/users", (req, res) -> {
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

---

## 妯″潡缁撴瀯

```
est-app/
鈹溾攢鈹€ est-web/          # Web 搴旂敤妗嗘灦
鈹溾攢鈹€ est-admin/        # 绠＄悊鍚庡彴
鈹斺攢鈹€ est-console/      # 鎺у埗鍙板簲鐢?```

---

## 鐩稿叧璧勬簮

- [est-web README](./est-web/README.md) - Web 搴旂敤璇︾粏鏂囨。
- [est-admin-ui README](../est-admin-ui/README.md) - 绠＄悊鍚庡彴鍓嶇鏂囨。
- [绀轰緥浠ｇ爜](../est-examples/est-examples-web/) - Web 绀轰緥浠ｇ爜
- [绀轰緥浠ｇ爜](../est-examples/est-examples-advanced/) - 楂樼骇绀轰緥
- [EST Core](../est-core/README.md) - 鏍稿績妯″潡
- [EST Modules](../est-modules/README.md) - 鍔熻兘妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
