# est-core-lifecycle - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
- [浠€涔堟槸 est-core-lifecycle](#浠€涔堟槸-est-core-lifecycle)
- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鏍稿績鍔熻兘](#鏍稿績鍔熻兘)
- [鐩稿叧璧勬簮](#鐩稿叧璧勬簮)

---

## 浠€涔堟槸 est-core-lifecycle

### 鐢ㄥぇ鐧借瘽鐞嗚В
est-core-lifecycle 灏卞儚"搴旂敤鐢熷懡鍛ㄦ湡绠″"锛岀鐞嗗簲鐢ㄤ粠鍚姩鍒板叧闂殑鏁翠釜鐢熷懡鍛ㄦ湡锛屽湪鍚勪釜闃舵鎵ц鐩稿簲鐨勬搷浣溿€?
### 鏍稿績鐗圭偣
- **鐢熷懡鍛ㄦ湡浜嬩欢**锛氬惎鍔ㄣ€佸氨缁€佸叧闂瓑浜嬩欢
- **鍚姩閽╁瓙**锛氬簲鐢ㄥ惎鍔ㄦ椂鎵ц鍒濆鍖?- **鍏抽棴閽╁瓙**锛氬簲鐢ㄥ叧闂椂鎵ц娓呯悊
- **鐘舵€佺鐞?*锛氬簲鐢ㄧ姸鎬佽拷韪?
---

## 蹇€熷叆闂?
### 1. 娣诲姞渚濊禆
```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-core-lifecycle</artifactId>
    <version>2.1.0</version>
</dependency>
```

### 2. 鐢熷懡鍛ㄦ湡鐩戝惉鍣?```java
@Component
public class MyLifecycleListener {
    
    @OnApplicationStart
    public void onStart() {
        System.out.println("搴旂敤鍚姩浜?);
    }
    
    @OnApplicationReady
    public void onReady() {
        System.out.println("搴旂敤灏辩华浜?);
    }
    
    @OnApplicationStop
    public void onStop() {
        System.out.println("搴旂敤鍏抽棴浜?);
    }
}
```

---

## 鏍稿績鍔熻兘

### 鍚姩浠诲姟
```java
@Component
@Order(1)
public class DatabaseInitTask implements StartupTask {
    
    @Override
    public void run() {
        System.out.println("鍒濆鍖栨暟鎹簱...");
    }
}

@Component
@Order(2)
public class CacheWarmupTask implements StartupTask {
    
    @Override
    public void run() {
        System.out.println("棰勭儹缂撳瓨...");
    }
}
```

### 鍏抽棴浠诲姟
```java
@Component
public class CleanupTask implements ShutdownTask {
    
    @Override
    public void run() {
        System.out.println("娓呯悊璧勬簮...");
    }
}
```

---

## 鐩稿叧璧勬簮

- [鐖舵ā鍧楁枃妗(../README.md)
- [渚濊禆娉ㄥ叆瀹瑰櫒](../est-core-container/README.md)
