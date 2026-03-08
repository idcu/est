# EST Features Cache 缂撳瓨妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?

## 鐩綍
1. [浠€涔堟槸 EST Features Cache锛焆(#浠€涔堟槸-est-features-cache)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓?est-collection 闆嗘垚](#涓?est-collection-闆嗘垚)
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Features Cache锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Features Cache 灏卞儚鏄竴涓?瓒呯骇鍌ㄧ墿鏌?銆傛兂璞′竴涓嬩綘鍦ㄥ仛涓€涓數鍟嗙綉绔欙紝姣忔鐢ㄦ埛鏌ヨ浜у搧淇℃伅閮借鍘绘暟鎹簱鏌ワ細

**浼犵粺鏂瑰紡**锛氭瘡娆￠兘鍘绘暟鎹簱鎵撅紝鎱㈡浜嗭紒鏁版嵁搴撻兘绱灝浜嗐€?

**EST Features Cache 鏂瑰紡**锛氫綘鎶婄粡甯告煡璇㈢殑涓滆タ鍏堟斁鍒板偍鐗╂煖閲岋紝涓嬫鏈変汉瑕佺洿鎺ョ粰浠栵紒
- 鍌ㄧ墿鏌滄弧浜嗭紵鎶婃渶涔呮病鐢ㄧ殑涓滆タ鎵旀帀锛圠RU绛栫暐锛?
- 涓滆タ鏀惧お涔咃紵鑷姩杩囨湡娓呯悊
- 鎯崇煡閬撳偍鐗╂煖鐢ㄥ緱鎬庝箞鏍凤紵鏈夌粺璁℃暟鎹粰浣犵湅

瀹冩敮鎸佸绉嶅瓨鍌ㄦ柟寮忥細鍐呭瓨缂撳瓨銆佹枃浠剁紦瀛樸€丷edis 缂撳瓨锛屾兂鐢ㄥ摢绉嶇敤鍝锛?

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘鍒涘缓鍜屼娇鐢ㄧ紦瀛?
- 鈿?**楂樻€ц兘** - 鍐呭瓨瀛樺偍锛岃闂€熷害鏋佸揩
- 馃攧 **鑷姩杩囨湡** - 鏀寔 TTL锛圱ime To Live锛夎缃?
- 馃搳 **LRU 绛栫暐** - 鑷姩娣樻卑鏈€涔呮湭浣跨敤鐨勬暟鎹?
- 馃搱 **缁熻鐩戞帶** - 鎻愪緵鍛戒腑鐜囥€佽繃鏈熸暟绛夎缁嗙粺璁?

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-api</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-features-cache-memory</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓▼搴?

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST Cache 绗竴涓ず渚?===\n");
        
        Cache<String, String> cache = Caches.newMemoryCache();
        
        cache.put("product:1", "iPhone 15");
        cache.put("product:2", "MacBook Pro");
        cache.put("product:3", "iPad");
        
        System.out.println("product:1 = " + cache.get("product:1").orElse("鏈壘鍒?));
        System.out.println("product:2 = " + cache.get("product:2").orElse("鏈壘鍒?));
        System.out.println("product:4 = " + cache.get("product:4", "榛樿鍊?));
        
        System.out.println("\n缂撳瓨涓叡鏈?" + cache.size() + " 涓暟鎹?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Cache 绗竴涓ず渚?===

product:1 = iPhone 15
product:2 = MacBook Pro
product:4 = 榛樿鍊?

缂撳瓨涓叡鏈?3 涓暟鎹?
```

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Features Cache 浜嗭紒

---

## 鍩虹绡?

### 1. 浠€涔堟槸 Cache锛?

Cache 灏辨槸涓€涓?閿€煎瀛樺偍"鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface Cache<K, V> {
    void put(K key, V value);                    // 鏀惧叆鏁版嵁
    Optional<V> get(K key);                      // 鑾峰彇鏁版嵁
    V get(K key, V defaultValue);                // 鑾峰彇鏁版嵁锛堝甫榛樿鍊硷級
    boolean containsKey(K key);                  // 妫€鏌ユ槸鍚﹀瓨鍦?
    void remove(K key);                          // 鍒犻櫎鏁版嵁
    void clear();                                // 娓呯┖缂撳瓨
    int size();                                  // 鑾峰彇澶у皬
    boolean isEmpty();                           // 妫€鏌ユ槸鍚︿负绌?
    CacheStats getStats();                       // 鑾峰彇缁熻淇℃伅
}
```

### 2. 鍒涘缓缂撳瓨鐨勫嚑绉嶆柟寮?

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheConfig;
import ltd.idcu.est.features.cache.memory.Caches;

public class CreateCache {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁ら厤缃?---");
        Cache<String, String> cache1 = Caches.newMemoryCache();
        cache1.put("key1", "value1");
        System.out.println("榛樿缂撳瓨澶у皬锛? + cache1.size());
        
        System.out.println("\n--- 鏂瑰紡浜岋細鎸囧畾鏈€澶у閲?---");
        Cache<String, String> cache2 = Caches.newMemoryCache(100);
        cache2.put("key2", "value2");
        System.out.println("鎸囧畾瀹归噺缂撳瓨澶у皬锛? + cache2.size());
        
        System.out.println("\n--- 鏂瑰紡涓夛細浣跨敤 Builder ---");
        Cache<String, String> cache3 = Caches.<String, String>builder()
            .maxSize(500)
            .defaultTtl(3600000)
            .evictionPolicy("LRU")
            .build();
        cache3.put("key3", "value3");
        System.out.println("Builder 鍒涘缓鐨勭紦瀛樺ぇ灏忥細" + cache3.size());
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BasicOperations {
    public static void main(String[] args) {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- 1. 鏀惧叆鏁版嵁 ---");
        cache.put("user:1", "寮犱笁");
        cache.put("user:2", "鏉庡洓");
        System.out.println("鏀惧叆 2 涓敤鎴?);
        
        System.out.println("\n--- 2. 鑾峰彇鏁版嵁 ---");
        Optional<String> user1 = cache.get("user:1");
        user1.ifPresent(name -> System.out.println("user:1 = " + name));
        
        String user3 = cache.get("user:3", "鏈煡鐢ㄦ埛");
        System.out.println("user:3 = " + user3);
        
        System.out.println("\n--- 3. 妫€鏌ユ槸鍚﹀瓨鍦?---");
        System.out.println("user:1 瀛樺湪锛? + cache.containsKey("user:1"));
        System.out.println("user:4 瀛樺湪锛? + cache.containsKey("user:4"));
        
        System.out.println("\n--- 4. 鍒犻櫎鏁版嵁 ---");
        cache.remove("user:2");
        System.out.println("鍒犻櫎 user:2 鍚庯紝缂撳瓨澶у皬锛? + cache.size());
        
        System.out.println("\n--- 5. 璁剧疆杩囨湡鏃堕棿 ---");
        cache.put("temp:1", "涓存椂鏁版嵁", 10, TimeUnit.SECONDS);
        System.out.println("鏀惧叆 10 绉掕繃鏈熺殑鏁版嵁");
        
        System.out.println("\n--- 6. 娓呯┖缂撳瓨 ---");
        cache.clear();
        System.out.println("娓呯┖鍚庯紝缂撳瓨澶у皬锛? + cache.size());
        System.out.println("缂撳瓨涓虹┖锛? + cache.isEmpty());
    }
}
```

---

## 杩涢樁绡?

### 1. TTL锛堣繃鏈熸椂闂达級

浣犲彲浠ヤ负缂撳瓨鏁版嵁璁剧疆杩囨湡鏃堕棿锛屾椂闂村埌浜嗚嚜鍔ㄦ竻鐞嗭細

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

import java.util.concurrent.TimeUnit;

public class TtlExample {
    public static void main(String[] args) throws InterruptedException {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- TTL 绀轰緥 ---");
        
        cache.put("permanent", "姘镐箙鏁版嵁");
        cache.put("short", "1绉掕繃鏈?, 1, TimeUnit.SECONDS);
        cache.put("medium", "5绉掕繃鏈?, 5, TimeUnit.SECONDS);
        
        System.out.println("鍒濆鐘舵€侊細");
        System.out.println("permanent = " + cache.get("permanent").orElse("null"));
        System.out.println("short = " + cache.get("short").orElse("null"));
        System.out.println("medium = " + cache.get("medium").orElse("null"));
        
        System.out.println("\n绛夊緟 2 绉?..");
        Thread.sleep(2000);
        
        System.out.println("\n2 绉掑悗锛?);
        System.out.println("permanent = " + cache.get("permanent").orElse("null"));
        System.out.println("short = " + cache.get("short").orElse("宸茶繃鏈?));
        System.out.println("medium = " + cache.get("medium").orElse("null"));
    }
}
```

### 2. 缂撳瓨缁熻锛圕acheStats锛?

缂撳瓨浼氳嚜鍔ㄧ粺璁″悇绉嶄俊鎭紝鏂逛究浣犱簡瑙ｄ娇鐢ㄦ儏鍐碉細

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheStats;
import ltd.idcu.est.features.cache.memory.Caches;

public class StatsExample {
    public static void main(String[] args) {
        Cache<String, String> cache = Caches.newMemoryCache();
        
        System.out.println("--- 缂撳瓨缁熻绀轰緥 ---");
        
        for (int i = 1; i <= 10; i++) {
            cache.put("key:" + i, "value:" + i);
        }
        
        for (int i = 1; i <= 15; i++) {
            cache.get("key:" + i);
        }
        
        CacheStats stats = cache.getStats();
        System.out.println("鍛戒腑娆℃暟锛? + stats.getHitCount());
        System.out.println("鏈懡涓鏁帮細" + stats.getMissCount());
        System.out.println("鍛戒腑鐜囷細" + String.format("%.2f%%", stats.getHitRate() * 100));
        System.out.println("鏀惧叆娆℃暟锛? + stats.getPutCount());
        System.out.println("鍒犻櫎娆℃暟锛? + stats.getRemoveCount());
        System.out.println("娣樻卑娆℃暟锛? + stats.getEvictionCount());
    }
}
```

### 3. LRU 娣樻卑绛栫暐

褰撶紦瀛樻弧浜嗙殑鏃跺€欙紝浼氳嚜鍔ㄦ窐姹版渶涔呮湭浣跨敤鐨勬暟鎹細

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;

public class LruExample {
    public static void main(String[] args) {
        System.out.println("--- LRU 娣樻卑绛栫暐绀轰緥 ---");
        
        Cache<String, String> cache = Caches.newMemoryCache(3);
        
        cache.put("A", "鏁版嵁A");
        cache.put("B", "鏁版嵁B");
        cache.put("C", "鏁版嵁C");
        System.out.println("鏀惧叆 A銆丅銆丆锛岀紦瀛樺ぇ灏忥細" + cache.size());
        
        cache.get("A");
        System.out.println("璁块棶浜?A锛堢幇鍦?A 鏄渶杩戜娇鐢ㄧ殑锛?);
        
        cache.put("D", "鏁版嵁D");
        System.out.println("鏀惧叆 D锛堢紦瀛樻弧浜嗭紝浼氭窐姹版渶涔呮湭鐢ㄧ殑 B锛?);
        
        System.out.println("\n妫€鏌ュ悇涓?key锛?);
        System.out.println("A 瀛樺湪锛? + cache.containsKey("A"));
        System.out.println("B 瀛樺湪锛? + cache.containsKey("B"));
        System.out.println("C 瀛樺湪锛? + cache.containsKey("C"));
        System.out.println("D 瀛樺湪锛? + cache.containsKey("D"));
        
        System.out.println("\n缂撳瓨缁熻锛?);
        System.out.println("娣樻卑娆℃暟锛? + cache.getStats().getEvictionCount());
    }
}
```

---

## 楂樼骇绡?

### 1. 缂撳瓨鐩戝惉鍣紙CacheListener锛?

浣犲彲浠ョ洃鍚紦瀛樼殑鍚勭浜嬩欢锛?

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheListener;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

public class ListenerExample {
    public static void main(String[] args) {
        System.out.println("--- 缂撳瓨鐩戝惉鍣ㄧず渚?---");
        
        MemoryCache<String, String> cache = (MemoryCache<String, String>) Caches.newMemoryCache();
        
        cache.addListener(new CacheListener<String, String>() {
            @Override
            public void onPut(String key, String value) {
                System.out.println("[浜嬩欢] 鏀惧叆鏁版嵁锛? + key + " = " + value);
            }
            
            @Override
            public void onRemove(String key, String value) {
                System.out.println("[浜嬩欢] 鍒犻櫎鏁版嵁锛? + key);
            }
            
            @Override
            public void onEvict(String key, String value) {
                System.out.println("[浜嬩欢] 娣樻卑鏁版嵁锛? + key);
            }
            
            @Override
            public void onExpire(String key, String value) {
                System.out.println("[浜嬩欢] 杩囨湡鏁版嵁锛? + key);
            }
            
            @Override
            public void onClear() {
                System.out.println("[浜嬩欢] 娓呯┖缂撳瓨");
            }
        });
        
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.remove("key1");
        cache.clear();
    }
}
```

### 2. 缂撳瓨鍔犺浇鍣紙CacheLoader锛?

褰撶紦瀛樹腑娌℃湁鏁版嵁鏃讹紝鍙互鑷姩浠庢暟鎹簮鍔犺浇锛?

```java
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.api.CacheLoader;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

public class LoaderExample {
    public static void main(String[] args) {
        System.out.println("--- 缂撳瓨鍔犺浇鍣ㄧず渚?---");
        
        MemoryCache<Long, String> cache = (MemoryCache<Long, String>) Caches.newMemoryCache();
        
        cache.setLoader(new CacheLoader<Long, String>() {
            @Override
            public String load(Long key) {
                System.out.println("[鍔犺浇鍣╙ 浠庢暟鎹簱鍔犺浇 key锛? + key);
                return "鏁版嵁搴撲腑鐨勬暟鎹?" + key;
            }
        });
        
        System.out.println("\n绗竴娆¤幏鍙?key=1锛堜細瑙﹀彂鍔犺浇锛夛細");
        System.out.println(cache.get(1L).orElse("null"));
        
        System.out.println("\n绗簩娆¤幏鍙?key=1锛堜粠缂撳瓨鑾峰彇锛夛細");
        System.out.println(cache.get(1L).orElse("null"));
        
        System.out.println("\n鑾峰彇 key=2锛堜細瑙﹀彂鍔犺浇锛夛細");
        System.out.println(cache.get(2L).orElse("null"));
        
        System.out.println("\n缂撳瓨缁熻锛?);
        System.out.println("鍛戒腑娆℃暟锛? + cache.getStats().getHitCount());
        System.out.println("鏈懡涓鏁帮細" + cache.getStats().getMissCount());
    }
}
```

---

## 涓?est-collection 闆嗘垚

EST Features Cache 鍜?est-collection 鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢ㄣ€?

### 鍦烘櫙锛氬晢鍝佷俊鎭紦瀛?+ 鏁版嵁澶勭悊

```java
import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.Caches;
import ltd.idcu.est.features.cache.memory.MemoryCache;

import java.util.ArrayList;
import java.util.List;

public class CollectionIntegration {
    public static void main(String[] args) {
        System.out.println("=== EST Cache + EST Collection 闆嗘垚绀轰緥 ===\n");
        
        MemoryCache<String, Product> cache = (MemoryCache<String, Product>) Caches.newMemoryCache();
        
        Product p1 = new Product(1L, "iPhone 15", 5999.0, 100);
        Product p2 = new Product(2L, "iPhone 15 Pro", 8999.0, 50);
        Product p3 = new Product(3L, "MacBook Pro", 14999.0, 30);
        Product p4 = new Product(4L, "iPad", 3999.0, 200);
        Product p5 = new Product(5L, "AirPods Pro", 1999.0, 150);
        
        cache.put("product:1", p1);
        cache.put("product:2", p2);
        cache.put("product:3", p3);
        cache.put("product:4", p4);
        cache.put("product:5", p5);
        
        System.out.println("--- 1. 浠庣紦瀛樿幏鍙栨墍鏈変骇鍝侊紝杞崲涓?Seq ---");
        List<Product> productList = new ArrayList<>(cache.values());
        Seq<Product> products = Seqs.from(productList);
        
        System.out.println("\n--- 2. 绛涢€変环鏍煎皬浜?10000 鐨勪骇鍝?---");
        products
            .where(p -> p.getPrice() < 10000)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 3. 璁＄畻鎵€鏈変骇鍝佺殑鎬诲簱瀛樹环鍊?---");
        double totalValue = products
            .mapToDouble(p -> p.getPrice() * p.getStock())
            .sum();
        System.out.println("搴撳瓨鎬讳环鍊硷細" + totalValue + " 鍏?);
        
        System.out.println("\n--- 4. 鎵惧嚭搴撳瓨鏈€澶氱殑 3 涓骇鍝?---");
        products
            .sortBy(Product::getStock, true)
            .take(3)
            .forEach(p -> System.out.println(p));
        
        System.out.println("\n--- 5. 鎸変环鏍煎尯闂村垎缁勭粺璁?---");
        long cheapCount = products.where(p -> p.getPrice() < 5000).count();
        long midCount = products.where(p -> p.getPrice() >= 5000 && p.getPrice() < 10000).count();
        long expensiveCount = products.where(p -> p.getPrice() >= 10000).count();
        
        System.out.println("浠锋牸 < 5000锛? + cheapCount + " 涓?);
        System.out.println("5000-10000锛? + midCount + " 涓?);
        System.out.println("浠锋牸 >= 10000锛? + expensiveCount + " 涓?);
        
        System.out.println("\n--- 6. 缂撳瓨鐑棬浜у搧鍒版柊鐨勭紦瀛?---");
        Cache<String, Product> hotCache = Caches.newMemoryCache();
        products
            .where(p -> p.getStock() > 100)
            .forEach(p -> hotCache.put("hot:" + p.getId(), p));
        
        System.out.println("鐑棬浜у搧缂撳瓨澶у皬锛? + hotCache.size());
    }
}

class Product {
    private Long id;
    private String name;
    private double price;
    private int stock;
    
    public Product(Long id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
    
    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}
```

---

## 鏈€浣冲疄璺?

### 1. 鍚堢悊璁剧疆缂撳瓨澶у皬

```java
// 鉁?鎺ㄨ崘锛氭牴鎹疄闄呴渶姹傝缃?
Cache<String, String> cache = Caches.newMemoryCache(10000);

// 鉂?涓嶆帹鑽愶細璁剧疆澶ぇ娴垂鍐呭瓨
Cache<String, String> badCache = Caches.newMemoryCache(10000000);
```

### 2. 鍠勭敤 TTL

```java
// 鉁?鎺ㄨ崘锛氫负缁忓父鍙樺寲鐨勬暟鎹缃緝鐭殑 TTL
cache.put("hot:products", hotProducts, 5, TimeUnit.MINUTES);

// 鉁?鎺ㄨ崘锛氫负鍩烘湰涓嶅彉鐨勬暟鎹缃緝闀跨殑 TTL 鎴栦笉杩囨湡
cache.put("config:settings", settings);
```

### 3. 鐩戞帶缂撳瓨鍛戒腑鐜?

```java
CacheStats stats = cache.getStats();
double hitRate = stats.getHitRate();

if (hitRate < 0.5) {
    System.out.println("缂撳瓨鍛戒腑鐜囧お浣庯紝鑰冭檻璋冩暣绛栫暐锛?);
}
```

### 4. 浣跨敤 CacheLoader

```java
// 鉁?鎺ㄨ崘锛氫娇鐢?CacheLoader 鑷姩鍔犺浇
cache.setLoader(key -> loadFromDatabase(key));
Optional<Data> data = cache.get(key);

// 鉂?涓嶆帹鑽愶細鎵嬪姩鍒ゆ柇
Optional<Data> data = cache.get(key);
if (data.isEmpty()) {
    Data dbData = loadFromDatabase(key);
    cache.put(key, dbData);
}
```

## 甯歌闂

### Q: 鍐呭瓨缂撳瓨鏁版嵁浼氭寔涔呭寲鍚楋紵

A: MemoryCache 鏄唴瀛樺瓨鍌紝绋嬪簭閲嶅惎鍚庢暟鎹細涓㈠け銆傚鏋滈渶瑕佹寔涔呭寲锛岃浣跨敤 Redis 鎴栨枃浠剁紦瀛樺疄鐜般€?

### Q: 濡備綍閫夋嫨鍚堥€傜殑缂撳瓨澶у皬锛?

A: 鏍规嵁浣犵殑鏁版嵁閲忓拰鍙敤鍐呭瓨鏉ュ喅瀹氥€傚缓璁厛鍋氭€ц兘娴嬭瘯锛屾壘鍒版渶浣冲钩琛＄偣銆?

### Q: 澶氱嚎绋嬬幆澧冧笅瀹夊叏鍚楋紵

A: 鏄殑锛丮emoryCache 浣跨敤 ConcurrentHashMap锛屾槸绾跨▼瀹夊叏鐨勩€?

## 涓嬩竴姝?

- 瀛︿範 [est-collection](../../est-foundation/est-collection/README.md) 杩涜鏁版嵁澶勭悊
- 鏌ョ湅 [est-features-data](../est-features-data/) 浜嗚В鏁版嵁鎸佷箙鍖?
- 灏濊瘯浣跨敤 Redis 鎴栨枃浠剁紦瀛樺疄鐜?
