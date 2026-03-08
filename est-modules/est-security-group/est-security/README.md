# EST Security - 瀹夊叏绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸瀹夊叏绯荤粺锛?

鎯宠薄涓€涓嬶紝浣犲湪绠＄悊涓€涓珮妗ｅ皬鍖猴細
- 闂ㄥ彛鏈変繚瀹夛紝妫€鏌ヨ繘鍑轰汉鍛樼殑韬唤锛堣璇侊級
- 涓嶅悓鐨勪汉鏈変笉鍚岀殑鏉冮檺锛屾瘮濡備笟涓诲彲浠ヨ繘鎵€鏈夋ゼ锛岃瀹㈠彧鑳借繘鎸囧畾妤硷紙鎺堟潈锛?
- 杩涘嚭閮芥湁璁板綍锛屾柟渚胯拷鏌ワ紙瀹¤锛?

**瀹夊叏绯荤粺**灏辨槸绋嬪簭鐨?淇濆畨绯荤粺"锛屽畠鎻愪緵锛?
- 鐢ㄦ埛璁よ瘉锛堥獙璇佷綘鏄皝锛?
- 鏉冮檺绠＄悊锛堜綘鑳藉仛浠€涔堬級
- 澶氱璁よ瘉鏂瑰紡锛圔asic銆丣WT銆丱Auth2銆丄PI Key锛?

淇濇姢浣犵殑绋嬪簭瀹夊叏锛?

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑瀹夊叏绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-security-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-security-basic</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

public class SecurityFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 瀹夊叏绯荤粺绀轰緥 ===\n");
        
        // 鍒涘缓瀹夊叏涓婁笅鏂?
        BasicSecurity security = SecurityFactory.createBasicSecurity();
        
        // 娣诲姞鐢ㄦ埛
        User user = new DefaultUser("1", "寮犱笁", "zhangsan", "password123");
        security.getUserDetailsService().addUser(user);
        
        // 灏濊瘯璁よ瘉
        try {
            Authentication auth = security.authenticate("zhangsan", "password123");
            System.out.println("璁よ瘉鎴愬姛锛佺敤鎴? " + auth.getName());
            System.out.println("鏄惁宸茶璇? " + auth.isAuthenticated());
        } catch (Exception e) {
            System.out.println("璁よ瘉澶辫触: " + e.getMessage());
        }
        
        System.out.println("\n鉁?瀹夊叏绀轰緥瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒扮敤鎴疯璇佹垚鍔燂紒

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤瀹夊叏绯荤粺锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **璁よ瘉锛圓uthentication锛?* | 楠岃瘉鐢ㄦ埛韬唤 | 妫€鏌ヨ韩浠借瘉 |
| **鎺堟潈锛圓uthorization锛?* | 楠岃瘉鐢ㄦ埛鏉冮檺 | 妫€鏌ラ棬绂佸崱 |
| **鐢ㄦ埛锛圲ser锛?* | 浣跨敤绯荤粺鐨勪汉 | 灏忓尯灞呮皯 |
| **瑙掕壊锛圧ole锛?* | 鐢ㄦ埛鐨勮韩浠?| 涓氫富銆佽瀹€佷繚瀹?|
| **鏉冮檺锛圥ermission锛?* | 鑳藉仛鐨勬搷浣?| 杩涘ぇ闂ㄣ€佽繘杞﹀簱 |

### 2. 鍩虹璁よ瘉

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.BasicSecurity;
import ltd.idcu.est.features.security.basic.DefaultPermission;
import ltd.idcu.est.features.security.basic.DefaultRole;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

public class BasicAuthExample {
    public static void main(String[] args) {
        BasicSecurity security = SecurityFactory.createBasicSecurity();
        
        // 鍒涘缓鏉冮檺
        DefaultPermission readPerm = new DefaultPermission("read", "璇诲彇鏉冮檺");
        DefaultPermission writePerm = new DefaultPermission("write", "鍐欏叆鏉冮檺");
        
        // 鍒涘缓瑙掕壊
        DefaultRole userRole = new DefaultRole("user", "鏅€氱敤鎴?);
        userRole.addPermission(readPerm);
        
        DefaultRole adminRole = new DefaultRole("admin", "绠＄悊鍛?);
        adminRole.addPermission(readPerm);
        adminRole.addPermission(writePerm);
        
        // 鍒涘缓鐢ㄦ埛
        User user = new DefaultUser("1", "寮犱笁", "zhangsan", "password123");
        ((DefaultUser) user).addRole(userRole);
        
        User admin = new DefaultUser("2", "鏉庡洓", "admin", "admin123");
        ((DefaultUser) admin).addRole(adminRole);
        
        // 娣诲姞鐢ㄦ埛
        security.getUserDetailsService().addUser(user);
        security.getUserDetailsService().addUser(admin);
        
        // 璁よ瘉鍜屾巿鏉冩鏌?
        try {
            Authentication auth = security.authenticate("zhangsan", "password123");
            System.out.println("寮犱笁璁よ瘉鎴愬姛");
            System.out.println("鏈夎鍙栨潈闄? " + security.getAuthorization().hasPermission(auth, "read"));
            System.out.println("鏈夊啓鍏ユ潈闄? " + security.getAuthorization().hasPermission(auth, "write"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 3. JWT 璁よ瘉

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.api.Token;
import ltd.idcu.est.features.security.jwt.JwtSecurity;
import ltd.idcu.est.features.security.jwt.JwtSecurityContext;

public class JwtAuthExample {
    public static void main(String[] args) {
        JwtSecurity security = JwtSecurityContext.create();
        
        // 鐢熸垚 JWT Token
        Token token = security.getTokenProvider().generateToken("user123");
        System.out.println("鐢熸垚鐨?Token: " + token.getValue());
        
        // 楠岃瘉 Token
        try {
            Authentication auth = security.authenticate(token.getValue());
            System.out.println("Token 楠岃瘉鎴愬姛锛岀敤鎴? " + auth.getName());
        } catch (Exception e) {
            System.out.println("Token 楠岃瘉澶辫触: " + e.getMessage());
        }
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. API Key 璁よ瘉

```java
import ltd.idcu.est.features.security.api.Authentication;
import ltd.idcu.est.features.security.apikey.ApiKeySecurity;
import ltd.idcu.est.features.security.apikey.DefaultApiKeyAuthentication;

public class ApiKeyAuthExample {
    public static void main(String[] args) {
        ApiKeySecurity security = new ApiKeySecurity();
        
        // 娣诲姞 API Key
        String apiKey = "my-secret-api-key-12345";
        security.addApiKey(apiKey, "service-a");
        
        // 楠岃瘉 API Key
        try {
            Authentication auth = security.authenticate(apiKey);
            System.out.println("API Key 楠岃瘉鎴愬姛: " + auth.getName());
        } catch (Exception e) {
            System.out.println("API Key 楠岃瘉澶辫触: " + e.getMessage());
        }
    }
}
```

### 2. 涓?EST Collection 闆嗘垚

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.security.api.User;
import ltd.idcu.est.features.security.basic.DefaultUser;
import ltd.idcu.est.features.security.basic.SecurityFactory;

import java.util.List;

public class SecurityCollectionIntegrationExample {
    public static void main(String[] args) {
        var security = SecurityFactory.createBasicSecurity();
        
        List<User> users = List.of(
                new DefaultUser("1", "寮犱笁", "zhangsan", "pass1"),
                new DefaultUser("2", "鏉庡洓", "lisi", "pass2"),
                new DefaultUser("3", "鐜嬩簲", "wangwu", "pass3")
        );
        
        // 浣跨敤 Collection 鎵归噺娣诲姞鐢ㄦ埛
        Seqs.of(users)
                .forEach(user -> security.getUserDetailsService().addUser(user));
        
        System.out.println("宸叉坊鍔犵敤鎴锋暟: " + users.size());
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 瀵嗙爜鍔犲瘑

```java
import ltd.idcu.est.features.security.api.PasswordEncoder;
import ltd.idcu.est.features.security.basic.BCryptPasswordEncoder;

public class PasswordEncodingExample {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 鍔犲瘑瀵嗙爜
        String rawPassword = "mysecretpassword";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("鍘熷瀵嗙爜: " + rawPassword);
        System.out.println("鍔犲瘑鍚? " + encodedPassword);
        
        // 楠岃瘉瀵嗙爜
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("瀵嗙爜鍖归厤: " + matches);
    }
}
```

---

## 馃幆 鎬荤粨

瀹夊叏绯荤粺灏卞儚绋嬪簭鐨?淇濆畨绯荤粺"锛屼繚鎶や綘鐨勭▼搴忓厤鍙楁湭鎺堟潈璁块棶锛?

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST Messaging 娑堟伅绯荤粺锛侌煄?
