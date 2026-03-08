# est-web-session - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-web-session](#浠€涔堟槸-est-web-session)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [杩涢樁绡囷細楂樼骇鐢ㄦ硶](#杩涢樁绡囬珮绾х敤娉?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-web-session

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-web-session 灏卞儚缃戠珯缁欐瘡涓敤鎴峰彂鐨?浼氬憳鍗?銆傚綋浣犵涓€娆¤闂綉绔欐椂锛岀郴缁熺粰浣犱竴寮犲崱锛圫ession锛夛紝涓婇潰璁扮潃浣犵殑鐧诲綍鐘舵€併€佽喘鐗╄溅鍟嗗搧绛変俊鎭€備笅娆″啀鏉ワ紝鍒蜂竴涓嬪崱锛岀綉绔欏氨鐭ラ亾鏄綘浜嗐€?
### 鏍稿績鐗圭偣
- **澶氬瓨鍌ㄥ悗绔?*锛氭敮鎸佸唴瀛樸€丷edis銆佹暟鎹簱绛夊绉嶅瓨鍌ㄦ柟寮?- **鑷姩绠＄悊**锛氳嚜鍔ㄥ鐞?Session 鐨勫垱寤恒€佽繃鏈熴€侀攢姣?- **鍒嗗竷寮忔敮鎸?*锛氶泦缇ょ幆澧冧笅 Session 鍏变韩
- **瀹夊叏鍔犲浐**锛氶槻鍔寔銆侀槻绡℃敼鏈哄埗

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-web-session</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆 Session
```yaml
est:
  web:
    session:
      timeout: 3600  # Session 杩囨湡鏃堕棿锛堢锛?      store-type: memory  # 瀛樺偍绫诲瀷锛歮emory/redis/jdbc
```

### 3. 浣跨敤 Session
```java
@Controller
public class UserController {
    
    @Get("/login")
    public void login(String username, String password, Session session) {
        if (validate(username, password)) {
            session.setAttribute("userId", getUserId(username));
            session.setAttribute("username", username);
        }
    }
    
    @Get("/profile")
    public String profile(Session session) {
        String username = session.getAttribute("username");
        return "娆㈣繋鍥炴潵锛? + username;
    }
    
    @Get("/logout")
    public void logout(Session session) {
        session.invalidate();
    }
}
```

---

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. Session 鍩烘湰鎿嶄綔

#### 鍒涘缓鍜岃幏鍙?Session
```java
// 鑾峰彇 Session锛屽鏋滀笉瀛樺湪鍒欏垱寤?Session session = request.getSession(true);

// 浠呰幏鍙栧凡瀛樺湪鐨?Session
Session session = request.getSession(false);
```

#### 瀛樺彇灞炴€?```java
// 瀛樺偍灞炴€?session.setAttribute("user", user);
session.setAttribute("cart", cart);

// 鑾峰彇灞炴€?User user = session.getAttribute("user");
Cart cart = session.getAttribute("cart");

// 鍒犻櫎灞炴€?session.removeAttribute("cart");

// 娓呯┖鎵€鏈夊睘鎬?session.clearAttributes();
```

#### Session 鐢熷懡鍛ㄦ湡
```java
// 鑾峰彇 Session ID
String sessionId = session.getId();

// 鑾峰彇鍒涘缓鏃堕棿
long createTime = session.getCreationTime();

// 鑾峰彇鏈€鍚庤闂椂闂?long lastAccessTime = session.getLastAccessedTime();

// 璁剧疆鏈€澶т笉娲诲姩鏃堕棿锛堢锛?session.setMaxInactiveInterval(1800);

// 閿€姣?Session
session.invalidate();
```

### 2. 澶氱瀛樺偍鍚庣

#### 鍐呭瓨瀛樺偍锛堝紑鍙戠幆澧冿級
```yaml
est:
  web:
    session:
      store-type: memory
```

#### Redis 瀛樺偍锛堢敓浜х幆澧冿級
```yaml
est:
  web:
    session:
      store-type: redis
      redis:
        namespace: est:session
        key-prefix: sess:
```

#### 鏁版嵁搴撳瓨鍌?```yaml
est:
  web:
    session:
      store-type: jdbc
      table-name: est_sessions
```

### 3. Session 鐩戝惉鍣?
#### 鐩戝惉 Session 鍒涘缓鍜岄攢姣?```java
@WebListener
public class MySessionListener implements SessionListener {
    
    @Override
    public void sessionCreated(SessionEvent event) {
        System.out.println("Session 鍒涘缓: " + event.getSession().getId());
    }
    
    @Override
    public void sessionDestroyed(SessionEvent event) {
        System.out.println("Session 閿€姣? " + event.getSession().getId());
    }
}
```

#### 鐩戝惉灞炴€у彉鍖?```java
@WebListener
public class MyAttributeListener implements SessionAttributeListener {
    
    @Override
    public void attributeAdded(SessionAttributeEvent event) {
        System.out.println("灞炴€ф坊鍔? " + event.getName());
    }
    
    @Override
    public void attributeRemoved(SessionAttributeEvent event) {
        System.out.println("灞炴€у垹闄? " + event.getName());
    }
    
    @Override
    public void attributeReplaced(SessionAttributeEvent event) {
        System.out.println("灞炴€ф浛鎹? " + event.getName());
    }
}
```

---

## 杩涢樁绡囷細楂樼骇鐢ㄦ硶

### 1. 鍒嗗竷寮?Session 鍏变韩

#### 閰嶇疆 Redis 浼氳瘽瀛樺偍
```java
@Configuration
public class SessionConfig {
    
    @Bean
    public SessionStore sessionStore(RedisClient redisClient) {
        return new RedisSessionStore(redisClient)
            .setNamespace("est:session")
            .setKeyPrefix("sess:");
    }
}
```

#### 浼氳瘽绮樻€ч厤缃紙鍙€夛級
```yaml
est:
  web:
    session:
      sticky: true  # 鍚敤浼氳瘽绮樻€?      cookie:
        name: SESSIONID
        http-only: true
        secure: false
        same-site: LAX
```

### 2. Session 瀹夊叏鍔犲浐

#### 閰嶇疆瀹夊叏閫夐」
```yaml
est:
  web:
    session:
      cookie:
        http-only: true      # 闃叉 XSS 璇诲彇 Cookie
        secure: true         # 浠?HTTPS 浼犺緭
        same-site: STRICT    # 闃叉 CSRF
      regeneration:
        enabled: true        # 鐧诲綍鍚庨噸鏂扮敓鎴?Session ID
        on-login: true
      fixation:
        prevention: true     # 闃叉浼氳瘽鍥哄畾鏀诲嚮
```

#### 鑷畾涔?Session ID 鐢熸垚鍣?```java
public class SecureSessionIdGenerator implements SessionIdGenerator {
    
    @Override
    public String generate() {
        return SecureRandomUtil.generateSecureId(32);
    }
}
```

### 3. Session 鎸佷箙鍖栧拰鎭㈠

#### 鎵嬪姩鎸佷箙鍖?```java
public class SessionBackupService {
    
    @Inject
    private SessionStore sessionStore;
    
    public void backupSession(String sessionId) {
        Session session = sessionStore.load(sessionId);
        if (session != null) {
            byte[] data = serialize(session);
            saveToBackup(sessionId, data);
        }
    }
    
    public Session restoreSession(String sessionId) {
        byte[] data = loadFromBackup(sessionId);
        if (data != null) {
            Session session = deserialize(data);
            sessionStore.save(session);
            return session;
        }
        return null;
    }
}
```

### 4. 浼氳瘽闆嗙兢鐩戞帶

#### 浼氳瘽缁熻
```java
@Service
public class SessionMonitor {
    
    @Inject
    private SessionRegistry sessionRegistry;
    
    public SessionStats getStats() {
        return SessionStats.builder()
            .activeSessions(sessionRegistry.getActiveSessionCount())
            .totalSessions(sessionRegistry.getTotalSessionCount())
            .expiredSessions(sessionRegistry.getExpiredSessionCount())
            .build();
    }
    
    public List<SessionInfo> listActiveSessions() {
        return sessionRegistry.getActiveSessions().stream()
            .map(this::toSessionInfo)
            .collect(Collectors.toList());
    }
}
```

---

## 鏈€浣冲疄璺?
### 鉁?鎺ㄨ崘鍋氭硶

| 鍦烘櫙 | 鎺ㄨ崘鍋氭硶 | 璇存槑 |
|------|---------|------|
| 瀛樺偍閫夋嫨 | 鐢熶骇鐢?Redis锛屽紑鍙戠敤鍐呭瓨 | Redis 鎬ц兘濂姐€佹敮鎸佸垎甯冨紡 |
| 杩囨湡鏃堕棿 | 30鍒嗛挓-2灏忔椂 | 骞宠　瀹夊叏鍜岀敤鎴蜂綋楠?|
| 鏁忔劅鏁版嵁 | 涓嶈瀛樺瘑鐮侊紝鍙瓨鐢ㄦ埛 ID | Session 涓嶆槸瀹夊叏瀛樺偍 |
| 鐧诲綍澶勭悊 | 鐧诲綍鍚庨噸鏂扮敓鎴?Session ID | 闃叉浼氳瘽鍥哄畾鏀诲嚮 |
| Cookie 璁剧疆 | HttpOnly + Secure + SameSite | 澶氬眰瀹夊叏闃叉姢 |

### 鉂?涓嶆帹鑽愬仛娉?
```java
// 鉂?涓嶈鍦?Session 涓瓨澶ч噺鏁版嵁
session.setAttribute("bigData", hugeObject);  // 鍐呭瓨鐖嗙偢

// 鉂?涓嶈瀛樻晱鎰熶俊鎭?session.setAttribute("password", "123456");  // 瀹夊叏椋庨櫓

// 鉂?涓嶈渚濊禆 Session 鍋氭寔涔呭寲
session.setAttribute("order", order);  // 閲嶅惎灏变涪澶?
// 鉁?搴旇鐢ㄦ暟鎹簱瀛?orderRepository.save(order);
session.setAttribute("orderId", order.getId());
```

---

## 妯″潡缁撴瀯

```
est-web-session/
鈹溾攢鈹€ est-web-session-api/          # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/web/session/
鈹?  鈹?      鈹溾攢鈹€ Session.java              # Session 鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ SessionStore.java         # Session 瀛樺偍鎺ュ彛
鈹?  鈹?      鈹溾攢鈹€ SessionListener.java      # Session 鐩戝惉鍣?鈹?  鈹?      鈹斺攢鈹€ SessionConfig.java        # 閰嶇疆鎺ュ彛
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-web-session-impl/         # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/web/session/
鈹?  鈹?      鈹溾攢鈹€ MemorySessionStore.java   # 鍐呭瓨瀛樺偍
鈹?  鈹?      鈹溾攢鈹€ RedisSessionStore.java    # Redis 瀛樺偍
鈹?  鈹?      鈹溾攢鈹€ JdbcSessionStore.java     # 鏁版嵁搴撳瓨鍌?鈹?  鈹?      鈹斺攢鈹€ DefaultSession.java       # 榛樿瀹炵幇
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [Web 璺敱妯″潡](../est-web-router/README.md)
- [Web 涓棿浠舵ā鍧梋(../est-web-middleware/README.md)
- [API 缃戝叧](../est-gateway/README.md)
- [EST 蹇€熷叆闂╙(../../docs/getting-started/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-web/)
