# EST Config 閰嶇疆涓績妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Config锛焆(#浠€涔堟槸-est-config)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST Config锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Config 灏卞儚鏄竴涓?鏅鸿兘閰嶇疆绠＄悊涓績"銆傛兂璞′竴涓嬩綘鍦ㄧ鐞嗕竴涓ぇ鍨嬪簲鐢紝鏈夊緢澶氶厤缃」鍒嗘暎鍦ㄥ悇涓湴鏂癸細

**浼犵粺鏂瑰紡**锛氶厤缃啓姝诲湪浠ｇ爜閲岋紝鏀逛釜閰嶇疆杩樿閲嶆柊缂栬瘧閮ㄧ讲锛屽お楹荤儲锛?
**EST Config 鏂瑰紡**锛氱粺涓€绠＄悊鎵€鏈夐厤缃紝鏀寔鍔ㄦ€佹洿鏂帮紝杩樻湁鐩戝惉鍣ㄩ€氱煡鍙樺寲锛?- 绫诲瀷瀹夊叏锛氭敮鎸?String銆乮nt銆乴ong銆乨ouble銆乥oolean 绛夌被鍨?- 鍔ㄦ€佺洃鍚細閰嶇疆鍙樺寲鏃惰嚜鍔ㄩ€氱煡
- 缁熶竴绠＄悊锛氭墍鏈夐厤缃湪涓€涓湴鏂?- 绠€鍗曟槗鐢細鍑犺浠ｇ爜灏辫兘鎼炲畾

瀹冩敮鎸佸绉嶆搷浣滐細鑾峰彇銆佽缃€佸垹闄ゃ€佺洃鍚紝鎯崇敤鍝釜鐢ㄥ摢涓紒

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜屼娇鐢ㄩ厤缃腑蹇?- 馃殌 **绫诲瀷瀹夊叏** - 鏀寔澶氱鏁版嵁绫诲瀷
- 馃攧 **鍙樻洿鐩戝惉** - 閰嶇疆鍙樺寲鏃惰嚜鍔ㄩ€氱煡
- 馃搳 **缁熶竴绠＄悊** - 鎵€鏈夐厤缃泦涓鐞?- 馃捑 **鎸佷箙鍖栨敮鎸?* - 鏀寔Properties鍜孻AML鏍煎紡鐨勯厤缃寔涔呭寲
- 馃攧 **鑷姩淇濆瓨** - 閰嶇疆鍙樻洿鏃惰嚜鍔ㄤ繚瀛樺埌鏂囦欢
- 馃搱 **鍙墿灞?* - 杞绘澗娣诲姞鑷畾涔夊姛鑳?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-config-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓厤缃腑蹇?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class FirstConfigExample {
    public static void main(String[] args) {
        System.out.println("=== EST Config 绗竴涓ず渚?===\n");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.setProperty("app.name", "My Application");
        configCenter.setProperty("app.port", 8080);
        configCenter.setProperty("app.debug", true);
        
        System.out.println("搴旂敤鍚嶇О: " + configCenter.getString("app.name", "Unknown"));
        System.out.println("搴旂敤绔彛: " + configCenter.getInt("app.port", 80));
        System.out.println("璋冭瘯妯″紡: " + configCenter.getBoolean("app.debug", false));
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Config 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Config 绗竴涓ず渚?===

搴旂敤鍚嶇О: My Application
搴旂敤绔彛: 8080
璋冭瘯妯″紡: true

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Config 浜嗭紒
```

---

## 鍩虹绡?
### 1. 浠€涔堟槸 ConfigCenter锛?
ConfigCenter 灏辨槸涓€涓?閰嶇疆涓績"鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface ConfigCenter {
    void setProperty(String key, Object value);                  // 璁剧疆灞炴€?    Optional<Object> getProperty(String key);                     // 鑾峰彇灞炴€?    <T> Optional<T> getProperty(String key, Class<T> type);      // 鑾峰彇灞炴€э紙鎸囧畾绫诲瀷锛?    String getString(String key, String defaultValue);            // 鑾峰彇瀛楃涓?    int getInt(String key, int defaultValue);                     // 鑾峰彇鏁存暟
    long getLong(String key, long defaultValue);                  // 鑾峰彇闀挎暣鏁?    double getDouble(String key, double defaultValue);            // 鑾峰彇娴偣鏁?    boolean getBoolean(String key, boolean defaultValue);         // 鑾峰彇甯冨皵鍊?    boolean containsProperty(String key);                          // 妫€鏌ユ槸鍚﹀瓨鍦?    void removeProperty(String key);                               // 鍒犻櫎灞炴€?    Map<String, Object> getAllProperties();                       // 鑾峰彇鎵€鏈夊睘鎬?    void addChangeListener(ConfigChangeListener listener);        // 娣诲姞鍙樻洿鐩戝惉鍣?    void removeChangeListener(ConfigChangeListener listener);     // 鍒犻櫎鍙樻洿鐩戝惉鍣?    List<ConfigChangeListener> getChangeListeners();              // 鑾峰彇鎵€鏈夌洃鍚櫒
    void clear();                                                  // 娓呯┖閰嶇疆
}
```

### 2. 鍒涘缓閰嶇疆涓績鐨勫嚑绉嶆柟寮?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class CreateConfigExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁ら厤缃腑蹇?---");
        ConfigCenter config1 = new DefaultConfigCenter();
        System.out.println("榛樿閰嶇疆涓績鍒涘缓鎴愬姛");
        
        System.out.println("\n--- 鏂瑰紡浜岋細娣诲姞鍒濆閰嶇疆 ---");
        ConfigCenter config2 = new DefaultConfigCenter();
        config2.setProperty("key1", "value1");
        config2.setProperty("key2", 123);
        System.out.println("甯﹀垵濮嬮厤缃殑閰嶇疆涓績鍒涘缓鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.util.Map;
import java.util.Optional;

public class BasicOperations {
    public static void main(String[] args) {
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        System.out.println("--- 1. 璁剧疆閰嶇疆 ---");
        configCenter.setProperty("string.key", "Hello World");
        configCenter.setProperty("int.key", 42);
        configCenter.setProperty("long.key", 1000000L);
        configCenter.setProperty("double.key", 3.14);
        configCenter.setProperty("boolean.key", true);
        System.out.println("璁剧疆浜?5 涓厤缃」");
        
        System.out.println("\n--- 2. 鑾峰彇閰嶇疆 ---");
        System.out.println("瀛楃涓? " + configCenter.getString("string.key", "default"));
        System.out.println("鏁存暟: " + configCenter.getInt("int.key", 0));
        System.out.println("闀挎暣鏁? " + configCenter.getLong("long.key", 0L));
        System.out.println("娴偣鏁? " + configCenter.getDouble("double.key", 0.0));
        System.out.println("甯冨皵鍊? " + configCenter.getBoolean("boolean.key", false));
        
        System.out.println("\n--- 3. 妫€鏌ラ厤缃槸鍚﹀瓨鍦?---");
        System.out.println("string.key 瀛樺湪: " + configCenter.containsProperty("string.key"));
        System.out.println("not.exists 瀛樺湪: " + configCenter.containsProperty("not.exists"));
        
        System.out.println("\n--- 4. 鑾峰彇鎵€鏈夐厤缃?---");
        Map<String, Object> all = configCenter.getAllProperties();
        System.out.println("閰嶇疆鎬绘暟: " + all.size());
        all.forEach((k, v) -> System.out.println("  " + k + " = " + v));
        
        System.out.println("\n--- 5. 鍒犻櫎閰嶇疆 ---");
        configCenter.removeProperty("string.key");
        System.out.println("鍒犻櫎鍚?string.key 瀛樺湪: " + configCenter.containsProperty("string.key"));
        
        System.out.println("\n--- 6. 娓呯┖閰嶇疆 ---");
        configCenter.clear();
        System.out.println("娓呯┖鍚庨厤缃€绘暟: " + configCenter.getAllProperties().size());
    }
}
```

---

## 杩涢樁绡?
### 1. 閰嶇疆鍙樻洿鐩戝惉

浣犲彲浠ョ洃鍚厤缃殑鍙樺寲锛?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigChangeListener;
import ltd.idcu.est.config.api.ConfigChangeEvent;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class ListenerExample {
    public static void main(String[] args) {
        System.out.println("--- 閰嶇疆鍙樻洿鐩戝惉绀轰緥 ---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        configCenter.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent event) {
                System.out.println("[鍙樻洿] " + event.getKey() + 
                    " 浠?" + event.getOldValue() + 
                    " 鍙樹负 " + event.getNewValue());
            }
        });
        
        configCenter.setProperty("test.key", "value1");
        configCenter.setProperty("test.key", "value2");
        configCenter.setProperty("another.key", 123);
    }
}
```

### 2. 绫诲瀷瀹夊叏鐨勯厤缃幏鍙?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.util.Optional;

public class TypeSafeExample {
    public static void main(String[] args) {
        System.out.println("--- 绫诲瀷瀹夊叏绀轰緥 ---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        configCenter.setProperty("string.key", "Hello");
        configCenter.setProperty("int.key", 42);
        
        Optional<String> strValue = configCenter.getProperty("string.key", String.class);
        strValue.ifPresent(v -> System.out.println("瀛楃涓? " + v));
        
        Optional<Integer> intValue = configCenter.getProperty("int.key", Integer.class);
        intValue.ifPresent(v -> System.out.println("鏁存暟: " + v));
        
        Optional<Double> doubleValue = configCenter.getProperty("int.key", Double.class);
        System.out.println("杞崲涓?Double: " + doubleValue.orElse(null));
    }
}
```

---

## 楂樼骇绡?
### 1. 澶氫釜鐩戝惉鍣?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.api.ConfigChangeListener;
import ltd.idcu.est.config.api.ConfigChangeEvent;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

public class MultipleListenersExample {
    public static void main(String[] args) {
        System.out.println("--- 澶氫釜鐩戝惉鍣ㄧず渚?---");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        
        ConfigChangeListener listener1 = event -> 
            System.out.println("[鐩戝惉鍣?] " + event.getKey());
        
        ConfigChangeListener listener2 = event -> 
            System.out.println("[鐩戝惉鍣?] " + event.getKey() + " changed");
        
        configCenter.addChangeListener(listener1);
        configCenter.addChangeListener(listener2);
        
        System.out.println("褰撳墠鐩戝惉鍣ㄦ暟: " + configCenter.getChangeListeners().size());
        
        configCenter.setProperty("test.key", "value");
        
        configCenter.removeChangeListener(listener1);
        System.out.println("\n鍒犻櫎鐩戝惉鍣?鍚庯紝鐩戝惉鍣ㄦ暟: " + configCenter.getChangeListeners().size());
        
        configCenter.setProperty("test.key", "new value");
    }
}
```

---

## 楂樼骇绡?
### 1. 閰嶇疆鎸佷箙鍖?
DefaultConfigCenter 鏀寔 Properties 鍜?YAML 鏍煎紡鐨勬寔涔呭寲锛?
```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 閰嶇疆涓績鎸佷箙鍖栫ず渚?---");
        
        File propertiesFile = new File("config.properties");
        File yamlFile = new File("config.yml");
        
        ConfigCenter config1 = new DefaultConfigCenter(propertiesFile);
        config1.setProperty("app.name", "My Application");
        config1.setProperty("app.port", 8080);
        System.out.println("閰嶇疆宸蹭繚瀛樺埌 Properties 鏂囦欢");
        
        ConfigCenter config2 = new DefaultConfigCenter(yamlFile);
        config2.setProperty("app.name", "My Application");
        config2.setProperty("app.port", 8080);
        System.out.println("閰嶇疆宸蹭繚瀛樺埌 YAML 鏂囦欢");
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?
EST Config 鍜屽叾浠栨ā鍧楅兘鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氶厤缃?+ 缃戝叧

```java
import ltd.idcu.est.config.api.ConfigCenter;
import ltd.idcu.est.config.impl.DefaultConfigCenter;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Config + EST Gateway 闆嗘垚绀轰緥 ===\n");
        
        ConfigCenter configCenter = new DefaultConfigCenter();
        configCenter.setProperty("gateway.port", 8080);
        configCenter.setProperty("user.service.url", "http://localhost:8081");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        int port = configCenter.getInt("gateway.port", 80);
        String userServiceUrl = configCenter.getString("user.service.url", "http://localhost:8080");
        
        Route route = new DefaultRoute("/api/users", userServiceUrl);
        gateway.getRouter().addRoute(route);
        
        System.out.println("缃戝叧閰嶇疆瀹屾垚锛岀鍙? " + port);
        System.out.println("鐢ㄦ埛鏈嶅姟鍦板潃: " + userServiceUrl);
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊瑙勫垝閰嶇疆閿?
```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄥ垎灞傜殑閿悕
configCenter.setProperty("app.name", "MyApp");
configCenter.setProperty("app.port", 8080);
configCenter.setProperty("db.url", "jdbc:mysql://localhost:3306/db");

// 鉁?涓嶆帹鑽愶細閿悕娣蜂贡
configCenter.setProperty("name", "MyApp");
configCenter.setProperty("p", 8080);
configCenter.setProperty("database", "jdbc:mysql://localhost:3306/db");
```

### 2. 浣跨敤榛樿鍊?
```java
// 鉁?鎺ㄨ崘锛氭€绘槸鎻愪緵榛樿鍊?String name = configCenter.getString("app.name", "Default App");
int port = configCenter.getInt("app.port", 8080);

// 鉁?涓嶆帹鑽愶細鐩存帴 get 鍙兘杩斿洖 null
String name = configCenter.getProperty("app.name").orElse(null);
```

### 3. 浣跨敤鐩戝惉鍣ㄥ姩鎬佹洿鏂?
```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄧ洃鍚櫒鍝嶅簲閰嶇疆鍙樺寲
configCenter.addChangeListener(event -> {
    if ("app.port".equals(event.getKey())) {
        System.out.println("绔彛宸插彉鏇达紝閲嶆柊鍔犺浇...");
    }
});
```

---

## 甯歌闂

### Q: 閰嶇疆浼氭寔涔呭寲鍚楋紵

A: DefaultConfigCenter 鏄唴瀛樺瓨鍌紝绋嬪簭閲嶅惎鍚庢暟鎹細涓㈠け銆傚悗缁増鏈細鏀寔鎸佷箙鍖栥€?
### Q: 鏀寔閰嶇疆鏂囦欢鍚楋紵

A: 褰撳墠鐗堟湰鏀寔鍐呭瓨閰嶇疆锛屽悗缁増鏈細鏀寔 properties銆亂aml 绛夐厤缃枃浠躲€?
### Q: 澶氱嚎绋嬬幆澧冧笅瀹夊叏鍚楋紵

A: 鏄殑锛丏efaultConfigCenter 鏄嚎绋嬪畨鍏ㄧ殑銆?
---

## 涓嬩竴姝?
- 瀛︿範 [est-gateway](../est-gateway/README.md) 杩涜缃戝叧璺敱
- 鏌ョ湅 [est-discovery](../est-discovery/) 浜嗚В鏈嶅姟鍙戠幇
- 灏濊瘯鑷畾涔夐厤缃簮
- 闃呰 [API 鏂囨。](../../docs/api/config/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
