# est-core-config - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-config](#浠€涔堟槸-est-core-config)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-config

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-config 灏卞儚"閰嶇疆绠＄悊鍣?锛屽府浣犵粺涓€绠＄悊搴旂敤鐨勯厤缃紝鏀寔澶氱閰嶇疆婧愶紝鏂逛究鍒囨崲鐜銆?
### 鏍稿績鐗圭偣
- **澶氭簮閰嶇疆**锛歒AML銆丳roperties銆佺幆澧冨彉閲忋€佺郴缁熷睘鎬?- **绫诲瀷瀹夊叏**锛氭敮鎸侀厤缃璞＄粦瀹?- **閰嶇疆鍒锋柊**锛氭敮鎸佽繍琛屾椂閰嶇疆鍒锋柊
- **閰嶇疆浼樺厛**锛氭敮鎸佸閰嶇疆婧愪紭鍏堢骇

---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-config</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鍒涘缓閰嶇疆绫?```java
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    @Value("${app.name:est-app}")
    private String name;
    
    @Value("${app.port:8080}")
    private int port;
    
    public String getName() { return name; }
    public int getPort() { return port; }
}
```

### 3. 浣跨敤閰嶇疆
```java
@Service
public class MyService {
    
    @Inject
    private AppConfig appConfig;
    
    public void start() {
        System.out.println("搴旂敤鍚? " + appConfig.getName());
        System.out.println("绔彛: " + appConfig.getPort());
    }
}
```

---

## 鏍稿績鍔熻兘

### 澶氱幆澧冮厤缃?```yaml
# application.yml
app:
  name: est-app

---
# application-dev.yml
app:
  port: 8080

---
# application-prod.yml
app:
  port: 80
```

### 閰嶇疆鍒锋柊
```java
@Service
public class RefreshableService {
    
    @Value("${app.message}")
    @Refreshable
    private String message;
    
    public String getMessage() {
        return message;
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [渚濊禆娉ㄥ叆瀹瑰櫒](../est-core-container/README.md)
