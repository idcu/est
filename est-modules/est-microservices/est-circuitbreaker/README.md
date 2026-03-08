# EST CircuitBreaker 鐔旀柇鍣ㄦā鍧?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST CircuitBreaker锛焆(#浠€涔堟槸-est-circuitbreaker)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST CircuitBreaker锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST CircuitBreaker 灏卞儚鏄竴涓?鐢佃矾淇濇姢鍣?銆傛兂璞′竴涓嬩綘瀹堕噷鐨勭數璺紝濡傛灉鐢靛櫒澶鎴栬€呮湁鏁呴殰锛屼繚闄╀笣浼氳嚜鍔ㄦ柇寮€锛岄槻姝㈡洿涓ラ噸鐨勯棶棰橈細

**浼犵粺鏂瑰紡**锛氭湇鍔′竴鐩磋皟鐢ㄥけ璐ョ殑鎺ュ彛锛屽鑷存暣涓郴缁熷彉鎱㈢敋鑷冲穿婧冿紒

**EST CircuitBreaker 鏂瑰紡**锛氬綋澶辫触娆℃暟杈惧埌闃堝€兼椂锛岃嚜鍔?璺抽椄"锛屽仠姝㈣皟鐢紝缁欑郴缁熸仮澶嶆椂闂达紒
- 鐔旀柇鐘舵€侊細鍏抽棴銆佸紑鍚€佸崐寮€涓夌鐘舵€?- 鑷姩鎭㈠锛氫竴娈垫椂闂村悗灏濊瘯鎭㈠
- 澶辫触缁熻锛氳褰曞け璐ユ鏁板拰鎴愬姛鐜?- 鍙厤缃細鐏垫椿璁剧疆鍚勭闃堝€?
瀹冩敮鎸佽嚜瀹氫箟閰嶇疆锛氬け璐ョ巼銆佽秴鏃舵椂闂淬€佹仮澶嶆椂闂达紝鎯虫€庝箞璁惧氨鎬庝箞璁撅紒

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜屼娇鐢ㄧ啍鏂櫒
- 馃殌 **淇濇姢绯荤粺** - 闃叉绾ц仈澶辫触
- 馃攧 **鑷姩鎭㈠** - 鏀寔鑷姩妫€娴嬪拰鎭㈠
- 馃搳 **鐘舵€佺洃鎺?* - 鎻愪緵璇︾粏鐨勬寚鏍囩粺璁?- 馃捑 **鎸佷箙鍖栨敮鎸?* - 鏀寔JSON鏍煎紡鐨勭啍鏂櫒鐘舵€佹寔涔呭寲
- 馃攧 **鑷姩淇濆瓨** - 鐘舵€佸彉鏇存椂鑷姩淇濆瓨鍒版枃浠?- 馃搱 **鍙厤缃?* - 鐏垫椿鐨勫弬鏁伴厤缃?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-circuitbreaker-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓啍鏂櫒

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class FirstCircuitBreakerExample {
    public static void main(String[] args) throws Exception {
        System.out.println("=== EST CircuitBreaker 绗竴涓ず渚?===\n");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .timeout(1000)
            .waitDuration(5000)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("my-service", config);
        
        String result = circuitBreaker.execute(() -> "Hello, CircuitBreaker!");
        System.out.println("鎵ц缁撴灉: " + result);
        System.out.println("褰撳墠鐘舵€? " + circuitBreaker.getState());
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST CircuitBreaker 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST CircuitBreaker 绗竴涓ず渚?===

鎵ц缁撴灉: Hello, CircuitBreaker!
褰撳墠鐘舵€? CLOSED

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST CircuitBreaker 浜嗭紒
```

---

## 鍩虹绡?
### 1. 浠€涔堟槸 CircuitBreaker锛?
CircuitBreaker 灏辨槸涓€涓?鐔旀柇鍣?鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface CircuitBreaker {
    String getName();                                      // 鑾峰彇鍚嶇О
    CircuitState getState();                               // 鑾峰彇鐘舵€?    <T> T execute(Supplier<T> supplier) throws Exception; // 鎵ц甯﹁繑鍥炲€肩殑鎿嶄綔
    void execute(Runnable runnable) throws Exception;     // 鎵ц鏃犺繑鍥炲€肩殑鎿嶄綔
    void reset();                                           // 閲嶇疆鐔旀柇鍣?    CircuitBreakerMetrics getMetrics();                    // 鑾峰彇鎸囨爣
}
```

### 2. 鍒涘缓鐔旀柇鍣ㄧ殑鍑犵鏂瑰紡

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class CreateCircuitBreakerExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁ら厤缃?---");
        CircuitBreakerConfig config1 = CircuitBreakerConfig.builder().build();
        CircuitBreaker cb1 = new DefaultCircuitBreaker("service1", config1);
        System.out.println("榛樿鐔旀柇鍣ㄥ垱寤烘垚鍔?);
        
        System.out.println("\n--- 鏂瑰紡浜岋細鑷畾涔夐厤缃?---");
        CircuitBreakerConfig config2 = CircuitBreakerConfig.builder()
            .failureThreshold(10)
            .timeout(2000)
            .waitDuration(10000)
            .build();
        CircuitBreaker cb2 = new DefaultCircuitBreaker("service2", config2);
        System.out.println("鑷畾涔夌啍鏂櫒鍒涘缓鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitState;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class BasicOperations {
    public static void main(String[] args) throws Exception {
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(3)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test-service", config);
        
        System.out.println("--- 1. 鎵ц鎴愬姛鎿嶄綔 ---");
        String result = circuitBreaker.execute(() -> "Success!");
        System.out.println("缁撴灉: " + result);
        System.out.println("鐘舵€? " + circuitBreaker.getState());
        
        System.out.println("\n--- 2. 鏌ョ湅鐘舵€?---");
        CircuitState state = circuitBreaker.getState();
        System.out.println("鐔旀柇鍣ㄥ悕绉? " + circuitBreaker.getName());
        System.out.println("褰撳墠鐘舵€? " + state);
        
        System.out.println("\n--- 3. 鏌ョ湅鎸囨爣 ---");
        System.out.println("鎸囨爣: " + circuitBreaker.getMetrics());
        
        System.out.println("\n--- 4. 閲嶇疆鐔旀柇鍣?---");
        circuitBreaker.reset();
        System.out.println("鐔旀柇鍣ㄥ凡閲嶇疆");
    }
}
```

---

## 杩涢樁绡?
### 1. 鐔旀柇鐘舵€侊紙CircuitState锛?
鐔旀柇鍣ㄦ湁涓夌鐘舵€侊細

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitState;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;

public class CircuitStateExample {
    public static void main(String[] args) throws Exception {
        System.out.println("--- 鐔旀柇鐘舵€佺ず渚?---");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(2)
            .waitDuration(1000)
            .build();
        
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("test-service", config);
        
        System.out.println("鍒濆鐘舵€? " + circuitBreaker.getState());
        
        try {
            circuitBreaker.execute(() -> {
                throw new RuntimeException("澶辫触");
            });
        } catch (Exception e) {
            System.out.println("绗竴娆″け璐?);
        }
        
        try {
            circuitBreaker.execute(() -> {
                throw new RuntimeException("澶辫触");
            });
        } catch (Exception e) {
            System.out.println("绗簩娆″け璐ワ紝鐔旀柇鍣ㄥ紑鍚?);
        }
        
        System.out.println("褰撳墠鐘舵€? " + circuitBreaker.getState());
    }
}
```

### 2. 鐔旀柇閰嶇疆锛圕ircuitBreakerConfig锛?
```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;

public class ConfigExample {
    public static void main(String[] args) {
        System.out.println("--- 鐔旀柇閰嶇疆绀轰緥 ---");
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)           // 澶辫触闃堝€?            .timeout(1000)                  // 瓒呮椂鏃堕棿锛堟绉掞級
            .waitDuration(5000)             // 绛夊緟鎭㈠鏃堕棿锛堟绉掞級
            .successThreshold(2)            // 鍗婂紑鐘舵€佹垚鍔熼槇鍊?            .build();
        
        System.out.println("閰嶇疆鍒涘缓鎴愬姛: " + config);
    }
}
```

---

## 楂樼骇绡?
### 1. 鐔旀柇鍣ㄦ敞鍐屼腑蹇冿紙CircuitBreakerRegistry锛?
```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreakerRegistry;

public class RegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 鐔旀柇鍣ㄦ敞鍐屼腑蹇冪ず渚?---");
        
        CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry();
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder().build();
        CircuitBreaker cb1 = new DefaultCircuitBreaker("service1", config);
        CircuitBreaker cb2 = new DefaultCircuitBreaker("service2", config);
        
        registry.register(cb1);
        registry.register(cb2);
        
        System.out.println("娉ㄥ唽浜?2 涓啍鏂櫒");
        System.out.println("鎵€鏈夌啍鏂櫒: " + registry.getAllCircuitBreakers());
        
        CircuitBreaker found = registry.getCircuitBreaker("service1");
        System.out.println("鎵惧埌 service1: " + (found != null));
    }
}
```

---

## 楂樼骇绡?
### 1. 鐔旀柇鍣ㄧ姸鎬佹寔涔呭寲

DefaultCircuitBreakerRegistry 鏀寔 JSON 鏍煎紡鐨勬寔涔呭寲锛岀▼搴忛噸鍚悗鍙互鎭㈠鐔旀柇鍣ㄧ姸鎬侊細

```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerRegistry;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreakerRegistry;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 鐔旀柇鍣ㄦ寔涔呭寲绀轰緥 ---");
        
        File dataFile = new File("circuit-breakers.json");
        
        CircuitBreakerRegistry registry = new DefaultCircuitBreakerRegistry(dataFile);
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .build();
        CircuitBreaker cb = new DefaultCircuitBreaker("user-service", config);
        registry.register(cb);
        
        System.out.println("鐔旀柇鍣ㄥ凡娉ㄥ唽锛屾暟鎹凡鑷姩淇濆瓨鍒? " + dataFile.getAbsolutePath());
        
        CircuitBreakerRegistry newRegistry = new DefaultCircuitBreakerRegistry(dataFile);
        System.out.println("浠庢枃浠跺姞杞藉悗锛岀啍鏂櫒鏁? " + newRegistry.getAllCircuitBreakers().size());
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?
EST CircuitBreaker 鍜?est-gateway 鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氱綉鍏?+ 鐔旀柇鍣ㄤ繚鎶?
```java
import ltd.idcu.est.circuitbreaker.api.CircuitBreaker;
import ltd.idcu.est.circuitbreaker.api.CircuitBreakerConfig;
import ltd.idcu.est.circuitbreaker.impl.DefaultCircuitBreaker;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST CircuitBreaker + EST Gateway 闆嗘垚绀轰緥 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        CircuitBreakerConfig config = CircuitBreakerConfig.builder()
            .failureThreshold(5)
            .build();
        CircuitBreaker circuitBreaker = new DefaultCircuitBreaker("gateway-service", config);
        
        gateway.addMiddleware(new GatewayMiddleware() {
            @Override
            public String getName() {
                return "circuit-breaker";
            }
            
            @Override
            public void before(GatewayContext context) {
                try {
                    circuitBreaker.execute(() -> {
                        System.out.println("璇锋眰閫氳繃鐔旀柇鍣ㄦ鏌?);
                        return null;
                    });
                } catch (Exception e) {
                    System.out.println("鐔旀柇鍣ㄥ凡寮€鍚紝闃绘璇锋眰");
                }
            }
            
            @Override
            public void after(GatewayContext context) {
            }
        });
        
        System.out.println("鐔旀柇鍣ㄤ腑闂翠欢宸叉坊鍔犲埌缃戝叧");
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊璁剧疆闃堝€?
```java
// 鉁?鎺ㄨ崘锛氭牴鎹疄闄呮儏鍐佃缃悎鐞嗙殑闃堝€?CircuitBreakerConfig config = CircuitBreakerConfig.builder()
    .failureThreshold(10)
    .timeout(2000)
    .waitDuration(30000)
    .build();

// 鉁?涓嶆帹鑽愶細闃堝€煎お鏁忔劅鎴栧お瀹芥澗
CircuitBreakerConfig badConfig = CircuitBreakerConfig.builder()
    .failureThreshold(1)
    .build();
```

### 2. 涓烘瘡涓湇鍔″崟鐙厤缃?
```java
// 鉁?鎺ㄨ崘锛氭瘡涓湇鍔℃湁鐙珛鐨勭啍鏂櫒
CircuitBreaker userServiceCb = new DefaultCircuitBreaker("user-service", userConfig);
CircuitBreaker orderServiceCb = new DefaultCircuitBreaker("order-service", orderConfig);
```

### 3. 鐩戞帶鐔旀柇鐘舵€?
```java
CircuitBreakerMetrics metrics = circuitBreaker.getMetrics();
System.out.println("澶辫触娆℃暟: " + metrics.getFailureCount());
System.out.println("鎴愬姛鐜? " + metrics.getSuccessRate());
```

---

## 甯歌闂

### Q: 鐔旀柇鍣ㄥ紑鍚悗浼氳嚜鍔ㄦ仮澶嶅悧锛?
A: 浼氱殑锛佺瓑寰?`waitDuration` 鏃堕棿鍚庝細杩涘叆鍗婂紑鐘舵€侊紝灏濊瘯鏀捐璇锋眰銆?
### Q: 濡備綍鎵嬪姩閲嶇疆鐔旀柇鍣紵

A: 璋冪敤 `circuitBreaker.reset()` 鏂规硶鍙互鎵嬪姩閲嶇疆銆?
### Q: 鏀寔澶氫釜鐔旀柇鍣ㄥ悧锛?
A: 鏀寔锛佸彲浠ヤ娇鐢?CircuitBreakerRegistry 绠＄悊澶氫釜鐔旀柇鍣ㄣ€?
---

## 涓嬩竴姝?
- 瀛︿範 [est-gateway](../est-gateway/README.md) 杩涜缃戝叧璺敱
- 鏌ョ湅 [est-discovery](../est-discovery/) 浜嗚В鏈嶅姟鍙戠幇
- 灏濊瘯鑷畾涔夌啍鏂厤缃?- 闃呰 [API 鏂囨。](../../docs/api/circuitbreaker/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
