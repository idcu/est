# EST Web - Web 搴旂敤妗嗘灦

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

EST Web 鏄?EST 妗嗘灦鐨?Web 搴旂敤妯″潡锛屽熀浜?Java 鍐呯疆 HttpServer锛屾彁渚涘畬鏁寸殑 Web 搴旂敤寮€鍙戝姛鑳姐€?

---

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡囷細璺敱绯荤粺](#鍩虹绡囪矾鐢辩郴缁?
- [鍩虹绡囷細璇锋眰鍝嶅簲](#鍩虹绡囪姹傚搷搴?
- [杩涢樁绡囷細涓棿浠禲(#杩涢樁绡囦腑闂翠欢)
- [杩涢樁绡囷細浼氳瘽绠＄悊](#杩涢樁绡囦細璇濈鐞?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸 Web 妗嗘灦锛?

鎯宠薄浣犺寮€涓€瀹跺晢搴楋紝闇€瑕侊細
- 馃彧 闂ㄥ簵锛堟湇鍔″櫒锛?
- 馃搵 鑿滃崟锛堣矾鐢憋級
- 馃懆鈥嶐煃?鍚庡帹锛堝鐞嗛€昏緫锛?
- 馃Ь 璐﹀崟锛堝搷搴旓級

**EST Web** 灏辨槸甯綘寮€搴楃殑宸ュ叿鍖咃紒

### 5鍒嗛挓涓婃墜

```java
import ltd.idcu.est.web.WebApplication;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Web 5鍒嗛挓涓婃墜 ===");
        System.out.println();

        WebApplication app = WebApplication.create("鎴戠殑绗竴涓綉绔?, "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("Hello, EST Web!");
        });
        
        System.out.println("鏈嶅姟鍣ㄥ凡鍚姩锛岃闂? http://localhost:8080");
        app.run(8080);
    }
}
```

杩愯鍚庯紝鍦ㄦ祻瑙堝櫒鎵撳紑 http://localhost:8080 灏辫兘鐪嬪埌浣犵殑绗竴涓綉椤典簡锛?馃帀

---

## 馃敯 鍩虹绡囷細璺敱绯荤粺

### 鐢熸椿绫绘瘮

璺敱灏卞儚鍟嗗簵鐨勮彍鍗曪紝瀹汉鐐逛粈涔堣彍锛屼綘灏辩粰浠€涔堣彍銆?

### 鍩烘湰璺敱

```java
import ltd.idcu.est.web.WebApplication;

public class RouterExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("璺敱绀轰緥", "1.0.0");
        
        app.get("/", (req, res) -> {
            res.send("棣栭〉");
        });
        
        app.get("/about", (req, res) -> {
            res.send("鍏充簬鎴戜滑");
        });
        
        app.get("/contact", (req, res) -> {
            res.send("鑱旂郴鏂瑰紡");
        });
        
        app.run(8080);
    }
}
```

### HTTP 鏂规硶

```java
app.get("/users", (req, res) -> {
    res.send("鑾峰彇鐢ㄦ埛鍒楄〃");
});

app.post("/users", (req, res) -> {
    res.send("鍒涘缓鏂扮敤鎴?);
});

app.put("/users/:id", (req, res) -> {
    String id = req.getPathParam("id");
    res.send("鏇存柊鐢ㄦ埛: " + id);
});

app.delete("/users/:id", (req, res) -> {
    String id = req.getPathParam("id");
    res.send("鍒犻櫎鐢ㄦ埛: " + id);
});
```

---

## 馃敯 鍩虹绡囷細璇锋眰鍝嶅簲

### 鐢熸椿绫绘瘮

璇锋眰灏辨槸瀹汉鐐圭殑鑿滐紝鍝嶅簲灏辨槸浣犵涓婃鐨勮彍銆?

### 鑾峰彇璇锋眰鍙傛暟

```java
import ltd.idcu.est.web.WebApplication;

public class RequestExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("璇锋眰绀轰緥", "1.0.0");
        
        app.get("/greet", (req, res) -> {
            String name = req.getQueryParam("name", "璁垮");
            res.send("浣犲ソ, " + name + "!");
        });
        
        app.get("/user/:id", (req, res) -> {
            String userId = req.getPathParam("id");
            res.send("鐢ㄦ埛ID: " + userId);
        });
        
        app.run(8080);
    }
}
```

### 杩斿洖鍝嶅簲

```java
app.get("/text", (req, res) -> {
    res.send("鏅€氭枃鏈?);
});

app.get("/json", (req, res) -> {
    res.json(Map.of(
        "name", "EST",
        "version", "1.0.0"
    ));
});

app.get("/status", (req, res) -> {
    res.status(200).send("鎴愬姛");
});

app.get("/error", (req, res) -> {
    res.status(500).send("鏈嶅姟鍣ㄩ敊璇?);
});
```

---

## 馃搱 杩涢樁绡囷細涓棿浠?

### 鐢熸椿绫绘瘮

涓棿浠跺氨鍍忛鍘呯殑鏈嶅姟鍛橈紝鍦ㄥ浜虹偣鍗曞拰涓婅彍涔嬮棿鍋氫竴浜涘鐞嗭細姣斿璁板綍璁㈠崟銆佹鏌ュ骇浣嶇瓑銆?

### 鏃ュ織涓棿浠?

```java
import ltd.idcu.est.web.WebApplication;

public class MiddlewareExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("涓棿浠剁ず渚?, "1.0.0");
        
        app.middleware((req, res) -> {
            System.out.println("璇锋眰: " + req.getMethod() + " " + req.getPath());
            return true;
        });
        
        app.get("/", (req, res) -> {
            res.send("棣栭〉");
        });
        
        app.useLogging();
        app.run(8080);
    }
}
```

### 鍐呯疆涓棿浠?

```java
app.useLogging();           // 鏃ュ織涓棿浠?
app.useCors();              // CORS 璺ㄥ煙涓棿浠?
app.usePerformanceMonitor(); // 鎬ц兘鐩戞帶涓棿浠?
```

---

## 馃搱 杩涢樁绡囷細浼氳瘽绠＄悊

### 鐢熸椿绫绘瘮

浼氳瘽灏卞儚椁愬巺鐨勪細鍛樺崱锛岃褰曞浜虹殑淇℃伅锛屼笅娆℃潵杩樿兘璁ゅ嚭浣犮€?

### 鐧诲綍鍜屼細璇?

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.web.api.Session;

public class SessionExample {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("浼氳瘽绀轰緥", "1.0.0");
        
        app.get("/login", (req, res) -> {
            Session session = req.getSession(true);
            session.setAttribute("username", "灏忔槑");
            res.send("鐧诲綍鎴愬姛锛?);
        });
        
        app.get("/profile", (req, res) -> {
            Session session = req.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                res.send("娆㈣繋鍥炴潵, " + username);
            } else {
                res.status(401).send("璇峰厛鐧诲綍");
            }
        });
        
        app.get("/logout", (req, res) -> {
            Session session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            res.send("宸查€€鍑虹櫥褰?);
        });
        
        app.run(8080);
    }
}
```

---

## 鉁?鏈€浣冲疄璺?

### 1. 璺敱鍒嗙粍

```java
app.routes(router -> {
    router.group("/api", (r, group) -> {
        r.get("/users", (req, res) -> res.send("鐢ㄦ埛鍒楄〃"));
        r.post("/users", (req, res) -> res.send("鍒涘缓鐢ㄦ埛"));
    });
});
```

### 2. 閿欒澶勭悊

```java
app.error((req, res, e) -> {
    res.status(500).send("鍑洪敊浜? " + e.getMessage());
});
```

### 3. 闈欐€佹枃浠?

```java
app.serveStatic("/static", "./public");
```

---

## 馃摝 妯″潡闆嗘垚

### 涓?est-collection 闆嗘垚

```java
import ltd.idcu.est.web.WebApplication;
import ltd.idcu.est.collection.impl.Seqs;

public class CollectionIntegration {
    public static void main(String[] args) {
        WebApplication app = WebApplication.create("闆嗘垚绀轰緥", "1.0.0");
        
        app.get("/users", (req, res) -> {
            List<User> users = getUsersFromDB();
            List<String> names = Seqs.of(users)
                .map(User::getName)
                .toList();
            res.json(names);
        });
        
        app.run(8080);
    }
}
```

---

## 馃摎 鏇村鍐呭

- [EST 椤圭洰涓婚〉](https://github.com/idcu/est)
- [EST Core](../../est-foundation/est-core/README.md)
- [EST Collection](../../est-foundation/est-collection/README.md)

---

**绁濅綘浣跨敤鎰夊揩锛?* 馃帀
