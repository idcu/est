# EST Security Group 瀹夊叏妯″潡缁?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Security Group锛焆(#浠€涔堟槸-est-security-group)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Security Group锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Security Group 灏卞儚鏄竴涓?瀹夊叏淇濋晼"銆傛兂璞′竴涓嬩綘鏈変竴涓噸瑕佺殑缃戠珯锛岄渶瑕佷繚鎶ょ敤鎴风殑瀹夊叏锛?
**浼犵粺鏂瑰紡**锛氫綘闇€瑕佽嚜宸卞啓璁よ瘉銆佹潈闄愭帶鍒躲€佸璁℃棩蹇?.. 寰堝鏉傦紝瀹规槗鍑洪敊锛?
**EST Security Group 鏂瑰紡**锛氱粰浣犱竴濂楀畬鏁寸殑瀹夊叏宸ュ叿绠憋紝閲岄潰鏈夛細
- 馃攼 **韬唤璁よ瘉** - 鏀寔 Basic銆丣WT銆丱Auth2銆丄PI Key銆丳olicy 绛夊绉嶈璇佹柟寮?- 馃懏 **鏉冮檺鎺у埗** - RBAC 鍩轰簬瑙掕壊鐨勮闂帶鍒?- 馃摑 **瀹¤鏃ュ織** - 璁板綍璋佸湪浠€涔堟椂鍊欏仛浜嗕粈涔堟搷浣?
### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍚敤瀹夊叏璁よ瘉
- 鈿?**澶氱璁よ瘉鏂瑰紡** - 鏀寔 Basic銆丣WT銆丱Auth2銆丄PI Key銆丳olicy
- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夎璇侀€昏緫鍜屾潈闄愮瓥鐣?- 馃帹 **鍔熻兘瀹屾暣** - 璁よ瘉銆佹巿鏉冦€佸璁′竴搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-security</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-rbac</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓畨鍏ㄥ簲鐢?
```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.BasicAuthenticator;
import ltd.idcu.est.security.user.InMemoryUserStore;
import ltd.idcu.est.security.user.User;
import ltd.idcu.est.security.user.UserBuilder;

public class FirstSecurityApp {
    public static void main(String[] args) {
        System.out.println("=== EST Security Group 绗竴涓ず渚?===\n");
        
        Security security = Security.create();
        
        InMemoryUserStore userStore = new InMemoryUserStore();
        User admin = UserBuilder.create()
            .username("admin")
            .password("admin123")
            .role("ADMIN")
            .build();
        User user = UserBuilder.create()
            .username("user")
            .password("user123")
            .role("USER")
            .build();
        userStore.addUser(admin);
        userStore.addUser(user);
        
        security.setAuthenticator(new BasicAuthenticator(userStore));
        security.requireRole("/admin/**", "ADMIN");
        security.requireRole("/user/**", "USER", "ADMIN");
        
        System.out.println("瀹夊叏閰嶇疆宸插畬鎴愶紒");
        System.out.println("admin/admin123 鍙互璁块棶 /admin 鍜?/user");
        System.out.println("user/user123 鍙兘璁块棶 /user");
    }
}
```

---

## 鍩虹绡?
### 1. est-security 韬唤璁よ瘉

#### Basic 璁よ瘉

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.BasicAuthenticator;
import ltd.idcu.est.security.user.InMemoryUserStore;

Security security = Security.create();

InMemoryUserStore userStore = new InMemoryUserStore();
userStore.addUser(UserBuilder.create()
    .username("admin")
    .password("admin123")
    .build());

security.setAuthenticator(new BasicAuthenticator(userStore));
```

#### JWT 璁よ瘉

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.JwtAuthenticator;
import ltd.idcu.est.security.jwt.JwtConfig;

Security security = Security.create();

JwtConfig config = JwtConfig.builder()
    .secretKey("my-secret-key-at-least-256-bits-long")
    .expiration(3600) // 1灏忔椂
    .build();

security.setAuthenticator(new JwtAuthenticator(config));
```

#### API Key 璁よ瘉

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.ApiKeyAuthenticator;
import ltd.idcu.est.security.apikey.InMemoryApiKeyStore;

Security security = Security.create();

InMemoryApiKeyStore apiKeyStore = new InMemoryApiKeyStore();
apiKeyStore.addApiKey("my-api-key-123", "service-1");

security.setAuthenticator(new ApiKeyAuthenticator(apiKeyStore));
```

#### OAuth2 璁よ瘉

```java
import ltd.idcu.est.security.Security;
import ltd.idcu.est.security.authentication.OAuth2Authenticator;
import ltd.idcu.est.security.oauth2.OAuth2Config;

Security security = Security.create();

OAuth2Config config = OAuth2Config.builder()
    .clientId("my-client-id")
    .clientSecret("my-client-secret")
    .authorizationUri("https://auth.example.com/authorize")
    .tokenUri("https://auth.example.com/token")
    .userInfoUri("https://auth.example.com/userinfo")
    .build();

security.setAuthenticator(new OAuth2Authenticator(config));
```

### 2. est-rbac 鍩轰簬瑙掕壊鐨勮闂帶鍒?
#### 瀹氫箟瑙掕壊鍜屾潈闄?
```java
import ltd.idcu.est.rbac.Rbac;
import ltd.idcu.est.rbac.Role;
import ltd.idcu.est.rbac.Permission;

Rbac rbac = Rbac.create();

Role adminRole = Role.create("ADMIN")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("user:write"))
    .addPermission(Permission.create("user:delete"))
    .addPermission(Permission.create("order:read"))
    .addPermission(Permission.create("order:write"))
    .addPermission(Permission.create("order:delete"));

Role userRole = Role.create("USER")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("order:read"));

rbac.addRole(adminRole);
rbac.addRole(userRole);
```

#### 妫€鏌ユ潈闄?
```java
boolean canReadUser = rbac.hasPermission("ADMIN", "user:read"); // true
boolean canDeleteUser = rbac.hasPermission("USER", "user:delete"); // false

boolean canAccessAdminPath = rbac.hasRole("ADMIN", "/admin/**");
```

#### 淇濇姢璺敱

```java
import ltd.idcu.est.web.router.WebRouter;
import ltd.idcu.est.security.Security;
import ltd.idcu.est.rbac.Rbac;

WebRouter router = WebRouter.create();
Security security = Security.create();
Rbac rbac = Rbac.create();

router.use(security.middleware());
router.use(rbac.middleware());

router.get("/admin/users", (req, res) -> {
    res.json(userService.findAll());
}).requireRole("ADMIN");

router.get("/user/profile", (req, res) -> {
    res.json(req.user());
}).requireRole("USER", "ADMIN");
```

### 3. est-audit 瀹¤鏃ュ織

#### 璁板綍鎿嶄綔

```java
import ltd.idcu.est.audit.Audit;
import ltd.idcu.est.audit.AuditLog;
import ltd.idcu.est.audit.AuditLogger;

Audit audit = Audit.create();
AuditLogger logger = audit.getLogger();

logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("LOGIN")
    .resource("auth")
    .details("鐢ㄦ埛鐧诲綍绯荤粺")
    .ipAddress("192.168.1.100")
    .build());

logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("CREATE")
    .resource("user")
    .resourceId("456")
    .details("鍒涘缓鐢ㄦ埛: 寮犱笁")
    .ipAddress("192.168.1.100")
    .build());
```

#### 鏌ヨ瀹¤鏃ュ織

```java
import ltd.idcu.est.audit.Audit;
import ltd.idcu.est.audit.AuditLog;
import ltd.idcu.est.audit.AuditQuery;
import ltd.idcu.est.collection.api.Seq;

Audit audit = Audit.create();
AuditQuery query = audit.createQuery();

Seq<AuditLog> logs = query
    .userId("123")
    .action("CREATE")
    .startTime(LocalDateTime.now().minusDays(7))
    .endTime(LocalDateTime.now())
    .find();

logs.forEach(log -> {
    System.out.println(log.getTimestamp() + " " + log.getUsername() + " " + log.getAction());
});
```

---

## 杩涢樁绡?
### 1. 鑷畾涔夌敤鎴峰瓨鍌?
```java
import ltd.idcu.est.security.user.UserStore;
import ltd.idcu.est.security.user.User;

public class DatabaseUserStore implements UserStore {
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseUserStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE username = ?",
            new Object[]{username},
            (rs, rowNum) -> UserBuilder.create()
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .role(rs.getString("role"))
                .build()
        );
    }
    
    @Override
    public boolean validatePassword(User user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}

Security security = Security.create();
security.setAuthenticator(new BasicAuthenticator(new DatabaseUserStore(jdbcTemplate)));
```

### 2. 鑷畾涔夋潈闄愮瓥鐣?
```java
import ltd.idcu.est.rbac.Policy;
import ltd.idcu.est.rbac.RbacContext;

public class CustomPolicy implements Policy {
    @Override
    public boolean check(RbacContext context) {
        String role = context.getRole();
        String resource = context.getResource();
        String action = context.getAction();
        
        if ("ADMIN".equals(role)) {
            return true;
        }
        
        if ("USER".equals(role)) {
            if ("user".equals(resource)) {
                return "read".equals(action);
            }
            if ("order".equals(resource)) {
                return "read".equals(action);
            }
        }
        
        return false;
    }
}

Rbac rbac = Rbac.create();
rbac.addPolicy(new CustomPolicy());
```

### 3. 瀹¤鏃ュ織瀛樺偍

```java
import ltd.idcu.est.audit.AuditStore;
import ltd.idcu.est.audit.AuditLog;

public class DatabaseAuditStore implements AuditStore {
    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseAuditStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public void save(AuditLog log) {
        jdbcTemplate.update(
            "INSERT INTO audit_logs (user_id, username, action, resource, resource_id, details, ip_address, timestamp) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
            log.getUserId(),
            log.getUsername(),
            log.getAction(),
            log.getResource(),
            log.getResourceId(),
            log.getDetails(),
            log.getIpAddress(),
            log.getTimestamp()
        );
    }
    
    @Override
    public Seq<AuditLog> query(AuditQuery query) {
        // 瀹炵幇鏌ヨ閫昏緫
        return Seqs.empty();
    }
}

Audit audit = Audit.create();
audit.setStore(new DatabaseAuditStore(jdbcTemplate));
```

---

## 鏈€浣冲疄璺?
### 1. 瀵嗙爜瀹夊叏

```java
import org.mindrot.jbcrypt.BCrypt;

// 鉁?鎺ㄨ崘锛氫娇鐢?BCrypt 绛夊己鍔犲瘑绠楁硶
String hashedPassword = BCrypt.hashpw("userpassword", BCrypt.gensalt());

// 鉂?涓嶆帹鑽愶細鏄庢枃瀛樺偍鎴栧急鍔犲瘑
String badPassword = "userpassword";
```

### 2. 鍚堢悊浣跨敤瑙掕壊鍜屾潈闄?
```java
// 鉁?鎺ㄨ崘锛氭渶灏忔潈闄愬師鍒?Role userRole = Role.create("USER")
    .addPermission(Permission.create("user:read"))
    .addPermission(Permission.create("order:read"));

// 鉂?涓嶆帹鑽愶細缁欐櫘閫氱敤鎴风鐞嗗憳鏉冮檺
Role badRole = Role.create("USER")
    .addPermission(Permission.create("*"));
```

### 3. 瀹¤鏃ュ織鐨勯噸瑕佸瓧娈?
```java
// 鉁?鎺ㄨ崘锛氳褰曞畬鏁寸殑瀹¤淇℃伅
logger.log(AuditLog.builder()
    .userId("123")
    .username("admin")
    .action("DELETE")
    .resource("user")
    .resourceId("456")
    .details("鍒犻櫎鐢ㄦ埛: 寮犱笁")
    .ipAddress("192.168.1.100")
    .userAgent("Mozilla/5.0...")
    .build());

// 鉂?涓嶆帹鑽愶細淇℃伅涓嶅叏
logger.log(AuditLog.builder()
    .action("DELETE")
    .build());
```

### 4. JWT 瀹夊叏閰嶇疆

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄥ己瀵嗛挜鍜屽悎鐞嗙殑杩囨湡鏃堕棿
JwtConfig config = JwtConfig.builder()
    .secretKey("my-very-long-secret-key-at-least-256-bits-for-hs256")
    .expiration(3600) // 1灏忔椂
    .issuer("my-application")
    .audience("my-users")
    .build();

// 鉂?涓嶆帹鑽愶細寮卞瘑閽ユ垨杩囬暱鐨勮繃鏈熸椂闂?JwtConfig badConfig = JwtConfig.builder()
    .secretKey("short")
    .expiration(86400 * 365) // 1骞达紝澶暱浜?    .build();
```

---

## 妯″潡缁撴瀯

```
est-security-group/
鈹溾攢鈹€ est-security/     # 韬唤璁よ瘉锛圔asic銆丣WT銆丱Auth2銆丄PI Key銆丳olicy锛?鈹溾攢鈹€ est-rbac/         # 鍩轰簬瑙掕壊鐨勮闂帶鍒?鈹斺攢鈹€ est-audit/        # 瀹¤鏃ュ織
```

---

## 鐩稿叧璧勬簮

- [est-security README](./est-security/README.md) - 瀹夊叏璁よ瘉璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-advanced/) - 楂樼骇绀轰緥
- [EST Web Group](../est-web-group/README.md) - Web 妯″潡
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
