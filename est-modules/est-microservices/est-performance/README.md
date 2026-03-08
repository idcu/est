# EST Performance 鎬ц兘浼樺寲妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Performance锛焆(#浠€涔堟槸-est-performance)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST Performance锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Performance 灏卞儚鏄竴涓?鎬ц兘浼樺寲涓撳"銆傛兂璞′竴涓嬩綘鐨勫簲鐢ㄨ窇寰楀緢鎱紝浣犱笉鐭ラ亾闂鍑哄湪鍝噷锛?
**浼犵粺鏂瑰紡**锛氱洸鐩紭鍖栵紝涓嶇煡閬撳摢閲屾湁闂锛屾晥鏋滃緢宸紒

**EST Performance 鏂瑰紡**锛氬府浣犵洃鎺?GC銆佷紭鍖?HTTP 鏈嶅姟鍣ㄣ€佺粺璁¤姹傛寚鏍囷紝璁╁簲鐢ㄩ璧锋潵锛?- GC 璋冧紭锛氱洃鎺у瀮鍦惧洖鏀讹紝缁欏嚭浼樺寲寤鸿
- HTTP 浼樺寲锛氭櫤鑳介厤缃?HTTP 鏈嶅姟鍣ㄥ弬鏁?- 璇锋眰缁熻锛氳褰曡姹傛鏁般€佸搷搴旀椂闂淬€佹垚鍔熺巼
- JVM 淇℃伅锛氳幏鍙栧綋鍓?JVM 鐨勮缁嗕俊鎭?
瀹冩敮鎸佸绉嶅姛鑳斤細GC 璋冧紭銆丠TTP 浼樺寲銆佽姹傜洃鎺э紝鎯崇敤鍝釜鐢ㄥ摢涓紒

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘寮€濮嬫€ц兘浼樺寲
- 馃殌 **瀹炵敤鏈夋晥** - 鍩轰簬瀹為檯鍦烘櫙鐨勪紭鍖栧缓璁?- 馃攧 **瀹炴椂鐩戞帶** - 瀹炴椂鏀堕泦鎬ц兘鎸囨爣
- 馃搳 **璇︾粏缁熻** - 鎻愪緵鍏ㄩ潰鐨勬€ц兘鏁版嵁
- 馃搱 **鏅鸿兘鎺ㄨ崘** - 鏍规嵁鎸囨爣缁欏嚭浼樺寲寤鸿

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-performance-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓€ц兘浼樺寲

```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.GCMetrics;
import ltd.idcu.est.performance.api.GCRecommendation;
import ltd.idcu.est.performance.impl.DefaultGCTuner;

public class FirstPerformanceExample {
    public static void main(String[] args) {
        System.out.println("=== EST Performance 绗竴涓ず渚?===\n");
        
        GCTuner gcTuner = new DefaultGCTuner();
        
        System.out.println("JVM 淇℃伅:\n" + gcTuner.getJVMInfo());
        
        GCMetrics metrics = gcTuner.collectMetrics();
        System.out.println("\nGC 鎸囨爣:\n" + metrics);
        
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("\n浼樺寲寤鸿:\n" + recommendation);
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Performance 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒扮被浼艰繖鏍风殑杈撳嚭锛?```
=== EST Performance 绗竴涓ず渚?===

JVM 淇℃伅:
JVM Version: 17.0.1
Heap Size: 256 MB
...

GC 鎸囨爣:
Collection Count: 10
Collection Time: 500ms
...

浼樺寲寤鸿:
寤鸿澧炲姞鍫嗗唴瀛樺ぇ灏?...

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Performance 浜嗭紒
```

---

## 鍩虹绡?
### 1. 浠€涔堟槸 GCTuner锛?
GCTuner 灏辨槸涓€涓?GC 璋冧紭鍣?鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface GCTuner {
    GCMetrics collectMetrics();          // 鏀堕泦 GC 鎸囨爣
    GCRecommendation getRecommendation(GCMetrics metrics); // 鑾峰彇浼樺寲寤鸿
    String getJVMInfo();                   // 鑾峰彇 JVM 淇℃伅
}
```

### 2. 鍒涘缓鎬ц兘浼樺寲宸ュ叿鐨勫嚑绉嶆柟寮?
```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.HttpServerOptimizer;
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultGCTuner;
import ltd.idcu.est.performance.impl.DefaultHttpServerOptimizer;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;

public class CreatePerformanceExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛欸C 璋冧紭鍣?---");
        GCTuner gcTuner = new DefaultGCTuner();
        System.out.println("GC 璋冧紭鍣ㄥ垱寤烘垚鍔?);
        
        System.out.println("\n--- 鏂瑰紡浜岋細HTTP 鏈嶅姟鍣ㄤ紭鍖栧櫒 ---");
        HttpServerOptimizer devOptimizer = DefaultHttpServerOptimizer.forDevelopment();
        HttpServerOptimizer prodOptimizer = DefaultHttpServerOptimizer.forProduction();
        System.out.println("HTTP 浼樺寲鍣ㄥ垱寤烘垚鍔?);
        
        System.out.println("\n--- 鏂瑰紡涓夛細璇锋眰鎸囨爣缁熻 ---");
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        System.out.println("璇锋眰鎸囨爣缁熻鍒涘缓鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.performance.api.*;
import ltd.idcu.est.performance.impl.*;

import java.util.Map;

public class BasicOperations {
    public static void main(String[] args) {
        System.out.println("--- 1. GC 璋冧紭 ---");
        GCTuner gcTuner = new DefaultGCTuner();
        System.out.println("JVM 淇℃伅:\n" + gcTuner.getJVMInfo());
        GCMetrics metrics = gcTuner.collectMetrics();
        System.out.println("GC 鎸囨爣: " + metrics);
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("浼樺寲寤鸿: " + recommendation);
        
        System.out.println("\n--- 2. HTTP 鏈嶅姟鍣ㄤ紭鍖?---");
        HttpServerOptimizer devOptimizer = DefaultHttpServerOptimizer.forDevelopment();
        System.out.println("寮€鍙戠幆澧冮厤缃? " + devOptimizer);
        HttpServerOptimizer prodOptimizer = DefaultHttpServerOptimizer.forProduction();
        System.out.println("鐢熶骇鐜閰嶇疆: " + prodOptimizer);
        Map<String, Object> configMap = prodOptimizer.toMap();
        System.out.println("閰嶇疆椤规暟閲? " + configMap.size());
        
        System.out.println("\n--- 3. 璇锋眰鎸囨爣缁熻 ---");
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        requestMetrics.recordRequest("/api/users", 200, 50);
        requestMetrics.recordRequest("/api/orders", 200, 100);
        requestMetrics.recordRequest("/api/users", 500, 200);
        System.out.println("鎬昏姹傛暟: " + requestMetrics.getTotalRequests());
        System.out.println("鎴愬姛鐜? " + String.format("%.1f%%", requestMetrics.getSuccessRate() * 100));
        System.out.println("骞冲潎鍝嶅簲鏃堕棿: " + requestMetrics.getAverageResponseTime() + "ms");
    }
}
```

---

## 杩涢樁绡?
### 1. HTTP 鏈嶅姟鍣ㄤ紭鍖栭厤缃?
浣犲彲浠ユ牴鎹幆澧冮€夋嫨涓嶅悓鐨勯厤缃紝涔熷彲浠ヨ嚜瀹氫箟锛?
```java
import ltd.idcu.est.performance.api.HttpServerOptimizer;
import ltd.idcu.est.performance.impl.DefaultHttpServerOptimizer;

public class HttpServerOptimizerExample {
    public static void main(String[] args) {
        System.out.println("--- HTTP 鏈嶅姟鍣ㄤ紭鍖栫ず渚?---");
        
        System.out.println("1. 寮€鍙戠幆澧冮厤缃?");
        HttpServerOptimizer dev = DefaultHttpServerOptimizer.forDevelopment();
        System.out.println(dev);
        
        System.out.println("\n2. 鐢熶骇鐜閰嶇疆:");
        HttpServerOptimizer prod = DefaultHttpServerOptimizer.forProduction();
        System.out.println(prod);
        
        System.out.println("\n3. 鑷畾涔夐厤缃?");
        HttpServerOptimizer custom = new DefaultHttpServerOptimizer()
            .setBacklog(200)
            .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 8)
            .setUseVirtualThreads(true)
            .setConnectionTimeout(120000)
            .setRequestTimeout(60000)
            .setMaxRequestSize(100 * 1024 * 1024)
            .setEnableCompression(true)
            .setEnableCaching(true)
            .setCacheMaxAge(14400);
        System.out.println(custom);
    }
}
```

### 2. 璇锋眰鎸囨爣璇︾粏缁熻

```java
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;

import java.util.Map;

public class RequestMetricsExample {
    public static void main(String[] args) {
        System.out.println("--- 璇锋眰鎸囨爣缁熻绀轰緥 ---");
        
        RequestMetrics metrics = new DefaultRequestMetrics();
        
        for (int i = 0; i < 100; i++) {
            String path = i % 3 == 0 ? "/api/users" : (i % 3 == 1 ? "/api/orders" : "/api/products");
            int status = i % 20 == 0 ? 500 : (i % 10 == 0 ? 404 : 200);
            long responseTime = (long) (Math.random() * 200 + 20);
            metrics.recordRequest(path, status, responseTime);
        }
        
        System.out.println("\n--- 姒傝 ---");
        System.out.println("鎬昏姹傛暟: " + metrics.getTotalRequests());
        System.out.println("鎴愬姛鐜? " + String.format("%.1f%%", metrics.getSuccessRate() * 100));
        System.out.println("骞冲潎鍝嶅簲鏃堕棿: " + String.format("%.2fms", metrics.getAverageResponseTime()));
        System.out.println("鏈€澶у搷搴旀椂闂? " + metrics.getMaxResponseTime() + "ms");
        System.out.println("鏈€灏忓搷搴旀椂闂? " + metrics.getMinResponseTime() + "ms");
        System.out.println("姣忕璇锋眰鏁? " + String.format("%.2f", metrics.getRequestsPerSecond()));
        
        System.out.println("\n--- 鐘舵€佺爜缁熻 ---");
        Map<Integer, Long> statusCodes = metrics.getStatusCodes();
        statusCodes.forEach((code, count) -> 
            System.out.println("  " + code + ": " + count));
        
        System.out.println("\n--- 璺緞缁熻 ---");
        Map<String, Long> pathCounts = metrics.getPathCounts();
        pathCounts.forEach((path, count) -> 
            System.out.println("  " + path + ": " + count));
    }
}
```

---

## 楂樼骇绡?
### 1. GC 鐩戞帶鍜屼紭鍖栧缓璁?
```java
import ltd.idcu.est.performance.api.GCTuner;
import ltd.idcu.est.performance.api.GCMetrics;
import ltd.idcu.est.performance.api.GCRecommendation;
import ltd.idcu.est.performance.impl.DefaultGCTuner;

public class GCMonitoringExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- GC 鐩戞帶绀轰緥 ---");
        
        GCTuner gcTuner = new DefaultGCTuner();
        
        System.out.println("鍒濆 GC 鎸囨爣:");
        GCMetrics initial = gcTuner.collectMetrics();
        System.out.println(initial);
        
        System.out.println("\n妯℃嫙涓€浜涘伐浣滆礋杞?..");
        for (int i = 0; i < 10000; i++) {
            byte[] temp = new byte[1024];
        }
        Thread.sleep(100);
        
        System.out.println("\n鏇存柊鍚庣殑 GC 鎸囨爣:");
        GCMetrics updated = gcTuner.collectMetrics();
        System.out.println(updated);
        
        System.out.println("\n浼樺寲寤鸿:");
        GCRecommendation recommendation = gcTuner.getRecommendation(updated);
        System.out.println(recommendation);
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?
EST Performance 鍜屽叾浠栨ā鍧楅兘鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氱綉鍏?+ 鎬ц兘鐩戞帶

```java
import ltd.idcu.est.performance.api.RequestMetrics;
import ltd.idcu.est.performance.impl.DefaultRequestMetrics;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Performance + EST Gateway 闆嗘垚绀轰緥 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        RequestMetrics requestMetrics = new DefaultRequestMetrics();
        
        gateway.addMiddleware(new GatewayMiddleware() {
            private long startTime;
            
            @Override
            public String getName() {
                return "performance-monitor";
            }
            
            @Override
            public void before(GatewayContext context) {
                startTime = System.currentTimeMillis();
            }
            
            @Override
            public void after(GatewayContext context) {
                long duration = System.currentTimeMillis() - startTime;
                int statusCode = context.getResponseStatusCode();
                String path = context.getRequestPath();
                requestMetrics.recordRequest(path, statusCode, duration);
                System.out.println("[鐩戞帶] " + path + " - " + statusCode + " - " + duration + "ms");
            }
        });
        
        System.out.println("鎬ц兘鐩戞帶涓棿浠跺凡娣诲姞鍒扮綉鍏?);
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 閫夋嫨鍚堥€傜殑 HTTP 閰嶇疆

```java
// 鉁?鎺ㄨ崘锛氭牴鎹幆澧冮€夋嫨閰嶇疆
HttpServerOptimizer optimizer;
if (isDevelopment) {
    optimizer = DefaultHttpServerOptimizer.forDevelopment();
} else {
    optimizer = DefaultHttpServerOptimizer.forProduction();
}

// 鉁?鎺ㄨ崘锛氳嚜瀹氫箟浼樺寲閰嶇疆
HttpServerOptimizer custom = new DefaultHttpServerOptimizer()
    .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 8)
    .setUseVirtualThreads(true);
```

### 2. 瀹氭湡鐩戞帶 GC

```java
// 鉁?鎺ㄨ崘锛氬畾鏈熸敹闆?GC 鎸囨爣
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(() -> {
    GCMetrics metrics = gcTuner.collectMetrics();
    if (metrics.getCollectionTime() > tooHigh) {
        GCRecommendation recommendation = gcTuner.getRecommendation(metrics);
        System.out.println("闇€瑕佷紭鍖? " + recommendation);
    }
}, 0, 1, TimeUnit.MINUTES);
```

### 3. 鍒嗘瀽璇锋眰鎸囨爣

```java
// 鉁?鎺ㄨ崘锛氬畾鏈熷垎鏋愯姹傛寚鏍?if (requestMetrics.getSuccessRate() < 0.9) {
    System.out.println("鎴愬姛鐜囧お浣庯紝闇€瑕佹鏌?);
    Map<Integer, Long> statusCodes = requestMetrics.getStatusCodes();
    statusCodes.forEach((code, count) -> {
        if (code >= 400) {
            System.out.println("鐘舵€佺爜 " + code + " 鍑虹幇 " + count + " 娆?);
        }
    });
}
```

---

## 甯歌闂

### Q: GC 浼樺寲寤鸿鎬绘槸鍑嗙‘鍚楋紵

A: 寤鸿鏄熀浜庝竴鑸儏鍐电粰鍑虹殑锛屽叿浣撹繕闇€瑕佺粨鍚堜綘鐨勫簲鐢ㄥ満鏅潵璋冩暣銆?
### Q: 铏氭嫙绾跨▼涓€瀹氭洿濂藉悧锛?
A: 涓嶄竴瀹氾紒铏氭嫙绾跨▼閫傚悎 I/O 瀵嗛泦鍨嬪簲鐢紝CPU 瀵嗛泦鍨嬪簲鐢ㄨ繕鏄敤骞冲彴绾跨▼鏇村ソ銆?
### Q: 璇锋眰鎸囨爣浼氬崰鐢ㄥ緢澶氬唴瀛樺悧锛?
A: DefaultRequestMetrics 浣跨敤鍚堢悊鐨勬暟鎹粨鏋勶紝鍐呭瓨鍗犵敤鍙帶銆?
---

## 涓嬩竴姝?
- 瀛︿範 [est-gateway](../est-gateway/README.md) 杩涜缃戝叧璺敱
- 鏌ョ湅 [est-config](../est-config/) 浜嗚В閰嶇疆绠＄悊
- 灏濊瘯鑷畾涔夋€ц兘浼樺寲绛栫暐
- 闃呰 [API 鏂囨。](../../docs/api/performance/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
