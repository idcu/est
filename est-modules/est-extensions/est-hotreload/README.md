# est-hotreload - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-hotreload](#浠€涔堟槸-est-hotreload)
- [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
- [鍩虹绡囷細鏍稿績鍔熻兘](#鍩虹绡囨牳蹇冨姛鑳?
- [杩涢樁绡囷細楂樼骇鐢ㄦ硶](#杩涢樁绡囬珮绾х敤娉?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
- [妯″潡缁撴瀯](#妯″潡缁撴瀯)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-hotreload

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-hotreload 灏卞儚"鐑彃鎷?鍔熻兘銆傚紑鍙戞椂淇敼浠ｇ爜涓嶇敤閲嶅惎搴旂敤锛屾敼瀹岄┈涓婂氨鑳界湅鍒版晥鏋溿€傚氨鍍忕粰鎵嬫満鎹㈢數姹犱笉鐢ㄥ叧鏈猴紝鐩存帴鎹㈠畬灏辫兘鐢ㄣ€?
### 鏍稿績鐗圭偣
- **浠ｇ爜鐑姞杞?*锛氫慨鏀?Java 浠ｇ爜鏃犻渶閲嶅惎
- **璧勬簮鐑埛鏂?*锛氶厤缃枃浠躲€佹ā鏉挎枃浠惰嚜鍔ㄥ埛鏂?- **绫婚噸杞?*锛氭敮鎸佺被瀹氫箟鍔ㄦ€佹浛鎹?- **寮€鍙戜綋楠屼紭鍖?*锛氭彁鍗囧紑鍙戞晥鐜?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-hotreload</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 閰嶇疆鐑姞杞?```yaml
est:
  hotreload:
    enabled: true
    watch-paths:
      - src/main/java
      - src/main/resources
    scan-interval: 1000  # 1绉掓壂鎻忎竴娆?```

### 3. 鍚姩搴旂敤
```java
public class DevApplication {
    public static void main(String[] args) {
        EstApplication.run(DevApplication.class, args)
            .enableHotReload();
    }
}
```

---

## 鍩虹绡囷細鏍稿績鍔熻兘

### 1. 绫荤儹鍔犺浇

#### 鍩烘湰鐢ㄦ硶
```java
@Service
public class MyService {
    
    public String sayHello() {
        return "Hello World!";
    }
}

// 淇敼鍚庝繚瀛橈紝鏃犻渶閲嶅惎灏辫兘鐢熸晥锛?public String sayHello() {
    return "Hello EST!";
}
```

#### 闄愬埗璇存槑
```java
// 鉁?鍙互鐑姞杞界殑
- 鏂规硶浣撲慨鏀?- 鏂规硶鍐呮柊澧?鍒犻櫎
- 瀛楁鏂板/鍒犻櫎
- 娉ㄨВ淇敼

// 鈿狅笍 鏈夐檺鍒剁殑
- 绫荤户鎵垮叧绯讳慨鏀?- 鎺ュ彛瀹炵幇淇敼

// 鉂?涓嶈兘鐑姞杞界殑锛堥渶瑕侀噸鍚級
- 绫诲悕淇敼
- 鏂规硶绛惧悕淇敼
- 瀛楁绫诲瀷淇敼
```

### 2. 璧勬簮鏂囦欢鐑埛鏂?
#### 閰嶇疆鏂囦欢鍒锋柊
```yaml
# application.yml
app:
  message: 鍒濆娑堟伅
```

```java
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    @Value("${app.message}")
    @Refreshable
    private String message;
    
    public String getMessage() {
        return message;
    }
}
```

#### 妯℃澘鏂囦欢鍒锋柊
```java
@Service
public class TemplateService {
    
    @Inject
    private TemplateEngine templateEngine;
    
    @Get("/template")
    public String renderTemplate() {
        return templateEngine.render("template.ftl");
    }
}
```

### 3. 鐑姞杞界洃鍚櫒

#### 鐩戝惉绫诲彉鏇?```java
@Component
public class HotReloadListener {
    
    @OnClassReload
    public void onClassReload(ClassReloadEvent event) {
        Class<?> reloadedClass = event.getReloadedClass();
        System.out.println("绫诲凡閲嶆柊鍔犺浇: " + reloadedClass.getName());
    }
    
    @OnClassReload(classes = MyService.class)
    public void onMyServiceReload() {
        System.out.println("MyService 宸叉洿鏂颁簡锛?);
        cacheService.clear();
    }
}
```

#### 鐩戝惉璧勬簮鍙樻洿
```java
@Component
public class ResourceChangeListener {
    
    @OnResourceChange("*.yml")
    public void onConfigChange(ResourceChangeEvent event) {
        System.out.println("閰嶇疆鏂囦欢鍙樻洿: " + event.getResourcePath());
        configService.reload();
    }
    
    @OnResourceChange("*.ftl")
    public void onTemplateChange(ResourceChangeEvent event) {
        templateEngine.clearCache();
    }
}
```

---

## 妯″潡缁撴瀯

```
est-hotreload/
鈹溾攢鈹€ est-hotreload-api/        # API 鎺ュ彛瀹氫箟
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/extensions/hotreload/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ est-hotreload-impl/       # 瀹炵幇妯″潡
鈹?  鈹溾攢鈹€ src/main/java/
鈹?  鈹?  鈹斺攢鈹€ ltd/idcu/est/extensions/hotreload/
鈹?  鈹斺攢鈹€ pom.xml
鈹溾攢鈹€ README.md
鈹斺攢鈹€ pom.xml
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [鎻掍欢绯荤粺](../est-plugin/README.md)
- [EST 寮€鍙戞寚鍗梋(../../docs/development/README.md)
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-basic/)
