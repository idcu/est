# EST Monitor - 鐩戞帶绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸鐩戞帶绯荤粺锛?

鎯宠薄涓€涓嬶紝浣犲湪绠＄悊涓€涓ぇ鍨嬪伐鍘傘€傚伐鍘傞噷鏈夊緢澶氭満鍣紝浣犻渶瑕佹椂鍒荤洃鎺э細
- 鏈哄櫒鐨勬俯搴︽甯稿悧锛?
- 鏈哄櫒鐨勮繍杞€熷害鎬庝箞鏍凤紵
- 鏈夋病鏈夋満鍣ㄥ嚭鏁呴殰锛?
- 鐢ㄧ數閲忔槸澶氬皯锛?

**鐩戞帶绯荤粺**灏卞儚宸ュ巶鐨勭洃鎺у锛屽畠鍙互瀹炴椂鐩戞帶绋嬪簭鐨勮繍琛岀姸鎬侊細
- JVM 鍐呭瓨浣跨敤鎯呭喌
- CPU 浣跨敤鐜?
- 绯荤粺鍋ュ悍鐘舵€?
- 鍚勯」鎬ц兘鎸囨爣

褰撶▼搴忓嚭鐜伴棶棰樻椂锛岀洃鎺х郴缁熷彲浠ュ強鏃跺彂鐜板苟鎶ヨ锛?

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑鐩戞帶绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-jvm</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-monitor-system</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.monitor.jvm.JvmMetrics;
import ltd.idcu.est.features.monitor.system.SystemMetrics;

public class MonitorFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 绯荤粺鐩戞帶绀轰緥 ===\n");
        
        // JVM 鐩戞帶
        System.out.println("--- JVM 淇℃伅 ---");
        JvmMetrics jvmMetrics = JvmMetrics.create();
        System.out.println("鍫嗗唴瀛樹娇鐢? " + jvmMetrics.getHeapMemoryUsed() + " / " + jvmMetrics.getHeapMemoryMax());
        System.out.println("闈炲爢鍐呭瓨浣跨敤: " + jvmMetrics.getNonHeapMemoryUsed());
        System.out.println("娲诲姩绾跨▼鏁? " + jvmMetrics.getThreadCount());
        
        // 绯荤粺鐩戞帶
        System.out.println("\n--- 绯荤粺淇℃伅 ---");
        SystemMetrics systemMetrics = SystemMetrics.create();
        System.out.println("鎿嶄綔绯荤粺: " + systemMetrics.getOsName());
        System.out.println("CPU 鏍稿績鏁? " + systemMetrics.getAvailableProcessors());
        System.out.println("绯荤粺璐熻浇: " + systemMetrics.getSystemLoadAverage());
        
        System.out.println("\n鉁?鐩戞帶绀轰緥瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒扮郴缁熺殑鐩戞帶淇℃伅锛?

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤鐩戞帶绯荤粺锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **鎸囨爣锛圡etric锛?* | 闇€瑕佺洃鎺х殑鏁版嵁椤?| 娓╁害銆佸帇鍔涖€侀€熷害 |
| **鍋ュ悍妫€鏌?* | 妫€鏌ョ郴缁熸槸鍚︽甯歌繍琛?| 浣撴 |
| **JVM 鐩戞帶** | 鐩戞帶 Java 铏氭嫙鏈虹殑杩愯鐘舵€?| 妫€鏌ユ苯杞﹀彂鍔ㄦ満 |
| **绯荤粺鐩戞帶** | 鐩戞帶鎿嶄綔绯荤粺鐨勮繍琛岀姸鎬?| 妫€鏌ユ苯杞︽暣浣撶姸鍐?|

### 2. JVM 鐩戞帶

```java
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.features.monitor.jvm.JvmMetrics;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

public class JvmMonitorExample {
    public static void main(String[] args) {
        // 鍒涘缓 JVM 鐩戞帶
        JvmMonitor monitor = new JvmMonitor();
        
        // 鑾峰彇 JVM 鎸囨爣
        JvmMetrics metrics = monitor.getMetrics();
        System.out.println("鍫嗗唴瀛樺凡鐢? " + metrics.getHeapMemoryUsed());
        System.out.println("鍫嗗唴瀛樻渶澶? " + metrics.getHeapMemoryMax());
        System.out.println("绾跨▼鏁? " + metrics.getThreadCount());
        System.out.println("GC 娆℃暟: " + metrics.getGcCount());
        
        // 鍋ュ悍妫€鏌?
        JvmHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("鍋ュ悍鐘舵€? " + healthCheck.getStatus());
        System.out.println("鍋ュ悍娑堟伅: " + healthCheck.getMessage());
    }
}
```

### 3. 绯荤粺鐩戞帶

```java
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;
import ltd.idcu.est.features.monitor.system.SystemMetrics;
import ltd.idcu.est.features.monitor.system.SystemMonitor;

public class SystemMonitorExample {
    public static void main(String[] args) {
        // 鍒涘缓绯荤粺鐩戞帶
        SystemMonitor monitor = new SystemMonitor();
        
        // 鑾峰彇绯荤粺鎸囨爣
        SystemMetrics metrics = monitor.getMetrics();
        System.out.println("鎿嶄綔绯荤粺: " + metrics.getOsName());
        System.out.println("CPU 鏍稿績鏁? " + metrics.getAvailableProcessors());
        System.out.println("绯荤粺璐熻浇: " + metrics.getSystemLoadAverage());
        System.out.println("鍙敤鍐呭瓨: " + metrics.getFreeMemory());
        
        // 鍋ュ悍妫€鏌?
        SystemHealthCheck healthCheck = monitor.checkHealth();
        System.out.println("鍋ュ悍鐘舵€? " + healthCheck.getStatus());
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 鑷畾涔夊仴搴锋鏌?

```java
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.api.HealthStatus;

public class DatabaseHealthCheck implements HealthCheck {
    @Override
    public String getName() {
        return "database";
    }
    
    @Override
    public HealthCheckResult check() {
        try {
            // 妫€鏌ユ暟鎹簱杩炴帴
            boolean isConnected = checkDatabaseConnection();
            if (isConnected) {
                return HealthCheckResult.healthy("鏁版嵁搴撹繛鎺ユ甯?);
            } else {
                return HealthCheckResult.unhealthy("鏁版嵁搴撹繛鎺ュけ璐?);
            }
        } catch (Exception e) {
            return HealthCheckResult.unhealthy("鏁版嵁搴撴鏌ュ紓甯? " + e.getMessage());
        }
    }
    
    private boolean checkDatabaseConnection() {
        // 妯℃嫙鏁版嵁搴撹繛鎺ユ鏌?
        return true;
    }
}
```

### 2. 涓?EST Collection 闆嗘垚

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.monitor.api.HealthCheck;
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.jvm.JvmHealthCheck;
import ltd.idcu.est.features.monitor.system.SystemHealthCheck;

import java.util.List;

public class MonitorCollectionIntegrationExample {
    public static void main(String[] args) {
        List<HealthCheck> checks = List.of(
                new JvmHealthCheck(),
                new SystemHealthCheck()
        );
        
        System.out.println("=== 绯荤粺鍋ュ悍妫€鏌?===");
        
        Seqs.of(checks)
                .map(check -> {
                    HealthCheckResult result = check.check();
                    return String.format("[%s] %s: %s", 
                            result.getStatus(), check.getName(), result.getMessage());
                })
                .forEach(System.out::println);
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 瀹氭湡鍋ュ悍妫€鏌?

```java
import ltd.idcu.est.features.monitor.api.HealthCheckResult;
import ltd.idcu.est.features.monitor.jvm.JvmMonitor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PeriodicHealthCheckExample {
    public static void main(String[] args) {
        JvmMonitor monitor = new JvmMonitor();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // 姣?30 绉掓墽琛屼竴娆″仴搴锋鏌?
        scheduler.scheduleAtFixedRate(() -> {
            HealthCheckResult result = monitor.checkHealth();
            System.out.println("鍋ュ悍妫€鏌? " + result.getStatus() + " - " + result.getMessage());
        }, 0, 30, TimeUnit.SECONDS);
    }
}
```

---

## 馃幆 鎬荤粨

鐩戞帶绯荤粺灏卞儚绋嬪簭鐨?浣撴鍖荤敓"锛屽畾鏈熸鏌ョ▼搴忕殑鍋ュ悍鐘跺喌锛屽強鏃跺彂鐜伴棶棰橈紒

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST Scheduler 璋冨害绯荤粺锛侌煄?
