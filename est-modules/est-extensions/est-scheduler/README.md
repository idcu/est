# EST Scheduler - 璋冨害绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸璋冨害绯荤粺锛?

鎯宠薄涓€涓嬶紝浣犳湁涓€涓櫤鑳介椆閽燂細
- 姣忓ぉ鏃╀笂 7 鐐瑰彨浣犺捣搴?
- 姣忓皬鏃舵彁閱掍綘鍠濅竴娆℃按
- 姣忓懆浜斾笅鍗?5 鐐规彁閱掍綘鍐欏懆鎶?

**璋冨害绯荤粺**灏卞儚绋嬪簭鐨勬櫤鑳介椆閽燂紝瀹冨彲浠ワ細
- 姣忛殧鍥哄畾鏃堕棿鎵ц浠诲姟
- 鍦ㄦ寚瀹氭椂闂存墽琛屼换鍔?
- 浣跨敤 Cron 琛ㄨ揪寮忕伒娲昏皟搴?
- 绠＄悊浠诲姟鐨勭敓鍛藉懆鏈?

璁╃▼搴忚嚜鍔ㄥ寲鎵ц锛岃В鏀句綘鐨勫弻鎵嬶紒

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑璋冨害绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-fixed</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-scheduler-cron</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class SchedulerFirstExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 璋冨害绯荤粺绀轰緥 ===\n");
        
        // 鍒涘缓鍥哄畾閫熺巼璋冨害鍣?
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 姣忛殧 1 绉掓墽琛屼竴娆′换鍔?
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("浠诲姟鎵ц: " + System.currentTimeMillis());
        }, 0, 1, TimeUnit.SECONDS);
        
        // 杩愯 5 绉掑悗鍋滄
        Thread.sleep(5000);
        scheduler.shutdown();
        
        System.out.println("\n鉁?璋冨害绀轰緥瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒颁换鍔℃瘡闅?1 绉掓墽琛屼竴娆★紒

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤璋冨害绯荤粺锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **浠诲姟锛圱ask锛?* | 闇€瑕佹墽琛岀殑宸ヤ綔 | 鎵撴壂鎴块棿銆佸啓浣滀笟 |
| **璋冨害鍣紙Scheduler锛?* | 绠＄悊浠诲姟鎵ц鐨勭粍浠?| 闂归挓銆佹棩绋嬭〃 |
| **鍥哄畾閫熺巼** | 姣忛殧鍥哄畾鏃堕棿鎵ц | 姣忓皬鏃跺枬涓€娆℃按 |
| **Cron 琛ㄨ揪寮?* | 鐏垫椿鐨勬椂闂磋〃杈惧紡 | 姣忓懆涓€鏃╀笂 9 鐐瑰紑浼?|

### 2. 鍥哄畾閫熺巼璋冨害

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class FixedRateExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 绔嬪嵆寮€濮嬶紝姣忛殧 2 绉掓墽琛屼竴娆?
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("鍥哄畾閫熺巼浠诲姟鎵ц: " + System.currentTimeMillis());
        }, 0, 2, TimeUnit.SECONDS);
        
        Thread.sleep(6000);
        scheduler.shutdown();
    }
}
```

### 3. Cron 琛ㄨ揪寮忚皟搴?

```java
import ltd.idcu.est.features.scheduler.cron.CronScheduler;
import ltd.idcu.est.features.scheduler.cron.CronSchedulers;

public class CronExample {
    public static void main(String[] args) throws InterruptedException {
        CronScheduler scheduler = CronSchedulers.newCronScheduler();
        
        // 姣忕鎵ц涓€娆★紙Cron 琛ㄨ揪寮忥級
        scheduler.schedule("* * * * * ?", () -> {
            System.out.println("Cron 浠诲姟鎵ц: " + System.currentTimeMillis());
        });
        
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 浠诲姟鐩戝惉

```java
import ltd.idcu.est.features.scheduler.api.TaskListener;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class TaskListenerExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        // 娣诲姞浠诲姟鐩戝惉鍣?
        scheduler.addTaskListener(new TaskListener() {
            @Override
            public void onTaskStart(String taskId) {
                System.out.println("浠诲姟寮€濮? " + taskId);
            }
            
            @Override
            public void onTaskComplete(String taskId) {
                System.out.println("浠诲姟瀹屾垚: " + taskId);
            }
            
            @Override
            public void onTaskError(String taskId, Throwable error) {
                System.out.println("浠诲姟鍑洪敊: " + taskId + ", 閿欒: " + error.getMessage());
            }
        });
        
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("鎵ц浠诲姟...");
        }, 0, 1, TimeUnit.SECONDS);
        
        Thread.sleep(3000);
        scheduler.shutdown();
    }
}
```

### 2. 涓?EST Collection 闆嗘垚

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SchedulerCollectionIntegrationExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        List<String> tasks = List.of("澶囦唤鏁版嵁", "娓呯悊缂撳瓨", "鍙戦€佹姤鍛?);
        
        // 浣跨敤 Collection 鎵归噺鍒涘缓浠诲姟
        Seqs.of(tasks)
                .forEach(taskName -> {
                    scheduler.scheduleAtFixedRate(() -> {
                        System.out.println("鎵ц浠诲姟: " + taskName);
                    }, 0, 2, TimeUnit.SECONDS);
                });
        
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 浼橀泤鍏抽棴璋冨害鍣?

```java
import ltd.idcu.est.features.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.features.scheduler.fixed.FixedRateSchedulers;

import java.util.concurrent.TimeUnit;

public class GracefulShutdownExample {
    public static void main(String[] args) throws InterruptedException {
        FixedRateScheduler scheduler = FixedRateSchedulers.newFixedRateScheduler();
        
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("浠诲姟鎵ц涓?..");
        }, 0, 1, TimeUnit.SECONDS);
        
        Thread.sleep(3000);
        
        // 浼橀泤鍏抽棴锛岀瓑寰?5 绉?
        System.out.println("寮€濮嬪叧闂皟搴﹀櫒...");
        scheduler.shutdown();
        if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
            scheduler.shutdownNow();
        }
        System.out.println("璋冨害鍣ㄥ凡鍏抽棴锛?);
    }
}
```

---

## 馃幆 鎬荤粨

璋冨害绯荤粺灏卞儚绋嬪簭鐨?鏅鸿兘闂归挓"锛岃绋嬪簭鑷姩鍖栨墽琛屼换鍔★紝鎻愰珮鏁堢巼锛?

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST Security 瀹夊叏绯荤粺锛侌煄?
