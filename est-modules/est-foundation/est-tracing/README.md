# EST Tracing 鍒嗗竷寮忚拷韪ā鍧?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?

## 鐩綍
1. [浠€涔堟槸 EST Tracing锛焆(#浠€涔堟槸-est-tracing)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST Tracing锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Tracing 灏卞儚鏄竴涓?璇锋眰杩借釜鍣?銆傛兂璞′竴涓嬩綘鍦ㄩ€佸揩閫掞紝姣忎釜蹇€掗兘鏈変竴涓拷韪彿锛屼綘鍙互闅忔椂鏌ョ湅蹇€掔殑浣嶇疆鍜岀姸鎬侊細

**浼犵粺鏂瑰紡**锛氳姹傚湪澶氫釜鏈嶅姟闂存祦杞紝鍑轰簡闂鏍规湰涓嶇煡閬撳湪鍝竴姝ュ崱浣忥紒

**EST Tracing 鏂瑰紡**锛氭瘡涓姹傞兘鏈変竴涓敮涓€鐨勮拷韪狪D锛岃褰曡姹傜粡杩囩殑姣忎釜鏈嶅姟鍜屾瘡涓楠わ紒
- 鍒嗗竷寮忚拷韪細璺ㄦ湇鍔¤拷韪姹傞摼璺?
- Span绠＄悊锛氳褰曟瘡涓搷浣滅殑寮€濮嬪拰缁撴潫鏃堕棿
- 鏍囩鏀寔锛氫负杩借釜娣诲姞鑷畾涔夋爣绛?
- 澶氱瀵煎嚭锛氭敮鎸佹棩蹇椼€佹枃浠躲€丱penTelemetry瀵煎嚭

瀹冩敮鎸佸绉嶅鍑哄櫒锛氭棩蹇椼€佹枃浠躲€丱penTelemetry锛屾兂鐢ㄥ摢涓敤鍝釜锛?

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜屼娇鐢ㄨ拷韪櫒
- 馃殌 **鍒嗗竷寮忚拷韪?* - 鏀寔璺ㄦ湇鍔＄殑璇锋眰閾捐矾杩借釜
- 馃攧 **Span绠＄悊** - 璁板綍姣忎釜鎿嶄綔鐨勫紑濮嬪拰缁撴潫鏃堕棿
- 馃搳 **鏍囩鏀寔** - 涓鸿拷韪坊鍔犺嚜瀹氫箟鏍囩
- 馃捑 **鎸佷箙鍖栨敮鎸?* - 鏀寔JSON鏍煎紡鐨勮拷韪暟鎹寔涔呭寲
- 馃搱 **鍙墿灞?* - 杞绘澗娣诲姞鑷畾涔夊鍑哄櫒

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-tracing-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-tracing-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓拷韪?

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class FirstTracingExample {
    public static void main(String[] args) {
        System.out.println("=== EST Tracing 绗竴涓ず渚?===\n");
        
        Tracer tracer = new DefaultTracer("my-service");
        
        TraceContext span = tracer.startSpan("process-request");
        System.out.println("寮€濮嬭拷韪? " + span.getSpanId());
        
        try {
            Thread.sleep(100);
            System.out.println("澶勭悊璇锋眰...");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(span);
            System.out.println("缁撴潫杩借釜锛岃€楁椂: " + (span.getEndTime() - span.getStartTime()) + "ms");
        }
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Tracing 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Tracing 绗竴涓ず渚?===

寮€濮嬭拷韪? [span-id]
澶勭悊璇锋眰...
缁撴潫杩借釜锛岃€楁椂: 100ms

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Tracing 浜嗭紒
```

---

## 鍩虹绡?

### 1. 浠€涔堟槸 Tracer锛?

Tracer 灏辨槸涓€涓?杩借釜鍣?鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface Tracer {
    String getServiceName();                              // 鑾峰彇鏈嶅姟鍚嶇О
    TraceContext startSpan(String operationName);        // 寮€濮嬩竴涓柊鐨凷pan
    TraceContext startSpan(String operationName, TraceContext parent); // 寮€濮嬩竴涓瓙Span
    void endSpan(TraceContext context);                  // 缁撴潫Span
    void setSpanExporter(SpanExporter exporter);        // 璁剧疆Span瀵煎嚭鍣?
}
```

### 2. 鍒涘缓杩借釜鍣ㄧ殑鍑犵鏂瑰紡

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;

public class CreateTracerExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁よ拷韪櫒 ---");
        Tracer tracer1 = new DefaultTracer("service-a");
        System.out.println("榛樿杩借釜鍣ㄥ垱寤烘垚鍔?);
        
        System.out.println("\n--- 鏂瑰紡浜岋細甯﹀鍑哄櫒鐨勮拷韪櫒 ---");
        Tracer tracer2 = new DefaultTracer("service-b");
        tracer2.setSpanExporter(new LoggingSpanExporter());
        System.out.println("甯︽棩蹇楀鍑哄櫒鐨勮拷韪櫒鍒涘缓鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class BasicOperations {
    public static void main(String[] args) {
        Tracer tracer = new DefaultTracer("user-service");
        
        System.out.println("--- 1. 寮€濮嬩竴涓猄pan ---");
        TraceContext span = tracer.startSpan("login");
        System.out.println("TraceId: " + span.getTraceId());
        System.out.println("SpanId: " + span.getSpanId());
        
        System.out.println("\n--- 2. 娣诲姞鏍囩 ---");
        span.setTags(java.util.Arrays.asList("user-id=123", "auth-method=password"));
        System.out.println("鏍囩: " + span.getTags());
        
        System.out.println("\n--- 3. 缁撴潫Span ---");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(span);
            System.out.println("Span缁撴潫锛岃€楁椂: " + (span.getEndTime() - span.getStartTime()) + "ms");
        }
    }
}
```

---

## 杩涢樁绡?

### 1. 宓屽Span锛堝瓙Span锛?

浣犲彲浠ュ垱寤哄祵濂楃殑Span鏉ヨ〃绀烘搷浣滅殑灞傛缁撴瀯锛?

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;

public class NestedSpanExample {
    public static void main(String[] args) {
        System.out.println("--- 宓屽Span绀轰緥 ---");
        
        Tracer tracer = new DefaultTracer("order-service");
        
        TraceContext parentSpan = tracer.startSpan("create-order");
        System.out.println("鐖禨pan: " + parentSpan.getSpanId());
        
        try {
            TraceContext childSpan1 = tracer.startSpan("validate-order", parentSpan);
            System.out.println("瀛怱pan1: " + childSpan1.getSpanId());
            Thread.sleep(30);
            tracer.endSpan(childSpan1);
            
            TraceContext childSpan2 = tracer.startSpan("save-order", parentSpan);
            System.out.println("瀛怱pan2: " + childSpan2.getSpanId());
            Thread.sleep(50);
            tracer.endSpan(childSpan2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            tracer.endSpan(parentSpan);
            System.out.println("鐖禨pan缁撴潫");
        }
    }
}
```

### 2. Span瀵煎嚭鍣?

EST Tracing 鏀寔澶氱Span瀵煎嚭鍣細

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;
import ltd.idcu.est.tracing.impl.FileSpanExporter;

import java.io.File;

public class ExporterExample {
    public static void main(String[] args) {
        System.out.println("--- Span瀵煎嚭鍣ㄧず渚?---");
        
        Tracer tracer1 = new DefaultTracer("service-a");
        tracer1.setSpanExporter(new LoggingSpanExporter());
        
        TraceContext span1 = tracer1.startSpan("operation-1");
        tracer1.endSpan(span1);
        System.out.println("鏃ュ織瀵煎嚭鍣ㄤ娇鐢ㄥ畬鎴?);
        
        File dataFile = new File("traces.json");
        Tracer tracer2 = new DefaultTracer("service-b");
        tracer2.setSpanExporter(new FileSpanExporter(dataFile));
        
        TraceContext span2 = tracer2.startSpan("operation-2");
        tracer2.endSpan(span2);
        System.out.println("鏂囦欢瀵煎嚭鍣ㄤ娇鐢ㄥ畬鎴愶紝鏁版嵁宸蹭繚瀛樺埌: " + dataFile.getAbsolutePath());
    }
}
```

---

## 楂樼骇绡?

### 1. 杩借釜鍣ㄦ敞鍐岃〃锛圱racerRegistry锛?

浣跨敤TracerRegistry绠＄悊澶氫釜杩借釜鍣細

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TracerRegistry;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.DefaultTracerRegistry;

public class RegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 杩借釜鍣ㄦ敞鍐岃〃绀轰緥 ---");
        
        TracerRegistry registry = new DefaultTracerRegistry();
        
        Tracer tracer1 = new DefaultTracer("user-service");
        Tracer tracer2 = new DefaultTracer("order-service");
        
        registry.register(tracer1);
        registry.register(tracer2);
        
        System.out.println("娉ㄥ唽浜?2 涓拷韪櫒");
        System.out.println("鎵€鏈夎拷韪櫒: " + registry.getAllTracers());
        
        Tracer found = registry.getTracer("user-service");
        System.out.println("鎵惧埌 user-service: " + (found != null));
    }
}
```

### 2. 鏂囦欢Span瀵煎嚭鍣?

FileSpanExporter 鏀寔寮傛鎵归噺淇濆瓨杩借釜鏁版嵁锛?

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.FileSpanExporter;

import java.io.File;

public class FileExporterExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 鏂囦欢瀵煎嚭鍣ㄧず渚?---");
        
        File dataFile = new File("traces-batch.json");
        FileSpanExporter exporter = new FileSpanExporter(dataFile, 10, 5000);
        
        Tracer tracer = new DefaultTracer("batch-service");
        tracer.setSpanExporter(exporter);
        
        for (int i = 0; i < 15; i++) {
            TraceContext span = tracer.startSpan("batch-operation-" + i);
            Thread.sleep(10);
            tracer.endSpan(span);
        }
        
        System.out.println("宸插垱寤?5涓猄pan锛屾壒閲忓鍑轰腑...");
        Thread.sleep(6000);
        
        exporter.shutdown();
        System.out.println("瀵煎嚭瀹屾垚");
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?

EST Tracing 鍜?est-gateway 鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氱綉鍏?+ 鍒嗗竷寮忚拷韪?

```java
import ltd.idcu.est.tracing.api.Tracer;
import ltd.idcu.est.tracing.api.TraceContext;
import ltd.idcu.est.tracing.impl.DefaultTracer;
import ltd.idcu.est.tracing.impl.LoggingSpanExporter;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.GatewayMiddleware;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Tracing + EST Gateway 闆嗘垚绀轰緥 ===\n");
        
        ApiGateway gateway = new DefaultApiGateway();
        
        Tracer tracer = new DefaultTracer("api-gateway");
        tracer.setSpanExporter(new LoggingSpanExporter());
        
        gateway.addMiddleware(new GatewayMiddleware() {
            @Override
            public String getName() {
                return "tracing";
            }
            
            @Override
            public void before(GatewayContext context) {
                TraceContext span = tracer.startSpan("gateway-request");
                context.setAttribute("trace-span", span);
                System.out.println("寮€濮嬭拷韪姹? " + span.getTraceId());
            }
            
            @Override
            public void after(GatewayContext context) {
                TraceContext span = (TraceContext) context.getAttribute("trace-span");
                if (span != null) {
                    tracer.endSpan(span);
                    System.out.println("缁撴潫杩借釜璇锋眰");
                }
            }
        });
        
        System.out.println("杩借釜涓棿浠跺凡娣诲姞鍒扮綉鍏?);
    }
}
```

---

## 鏈€浣冲疄璺?

### 1. 鍚堢悊鍛藉悕鎿嶄綔

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄦ湁鎰忎箟鐨勬搷浣滃悕
TraceContext span = tracer.startSpan("user-login");

// 鉁?涓嶆帹鑽愶細鎿嶄綔鍚嶅お闅忔剰
TraceContext badSpan = tracer.startSpan("op1");
```

### 2. 娣诲姞鍏抽敭鏍囩

```java
// 鉁?鎺ㄨ崘锛氭坊鍔犲叧閿俊鎭爣绛?
span.setTags(java.util.Arrays.asList("user-id=123", "request-id=abc"));

// 鉁?涓嶆帹鑽愶細涓嶆坊鍔犱换浣曟爣绛?
// span娌℃湁鏍囩
```

### 3. 鐩戞帶杩借釜鎬ц兘

```java
// 鉁?鎺ㄨ崘锛氭敞鎰廠pan鐨勫垱寤哄拰缁撴潫
TraceContext span = tracer.startSpan("operation");
try {
    // 鎵ц鎿嶄綔
} finally {
    tracer.endSpan(span);
}
```

---

## 甯歌闂

### Q: 杩借釜鏁版嵁浼氭寔涔呭寲鍚楋紵

A: 鏄殑锛佸彲浠ヤ娇鐢?FileSpanExporter 灏嗚拷韪暟鎹繚瀛樺埌 JSON 鏂囦欢銆?

### Q: 鏀寔璺ㄦ湇鍔¤拷韪悧锛?

A: 鏀寔锛侀€氳繃浼犻€?TraceContext 鍙互瀹炵幇璺ㄦ湇鍔＄殑鍒嗗竷寮忚拷韪€?

### Q: 濡備綍瀵煎嚭杩借釜鏁版嵁锛?

A: 鍙互浣跨敤 LoggingSpanExporter銆丗ileSpanExporter 鎴?OpenTelemetrySpanExporter銆?

---

## 涓嬩竴姝?

- 瀛︿範 [est-gateway](../est-gateway/README.md) 杩涜缃戝叧璺敱
- 鏌ョ湅 [est-discovery](../est-discovery/) 浜嗚В鏈嶅姟鍙戠幇
- 灏濊瘯鑷畾涔?SpanExporter
- 闃呰 [API 鏂囨。](../../docs/api/tracing/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
