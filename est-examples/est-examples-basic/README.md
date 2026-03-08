# EST 鍩虹绀轰緥

娆㈣繋鏉ュ埌 EST 鍩虹绀轰緥妯″潡锛佽繖閲屾槸鏂版墜鐨勮捣鐐癸紝鍖呭惈浜?EST 妗嗘灦鏈€鍩烘湰銆佹渶甯哥敤鐨勫姛鑳界ず渚嬨€?

## 馃搵 鐩綍

1. [浠€涔堟槸 EST 鍩虹绀轰緥锛焆(#浠€涔堟槸-est-鍩虹绀轰緥)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [绀轰緥鍒嗙被](#绀轰緥鍒嗙被)
4. [濡備綍杩愯绀轰緥](#濡備綍杩愯绀轰緥)
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
6. [甯歌闂](#甯歌闂)
7. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST 鍩虹绀轰緥锛?

### 鐢ㄥぇ鐧借瘽鐞嗚В

EST 鍩虹绀轰緥灏卞儚鏄竴涓?缂栫▼宸ュ叿绠?銆傛兂璞′竴涓嬩綘鍦ㄥ楠戣嚜琛岃溅锛?

**浼犵粺鏂瑰紡**锛氫竴涓婃潵灏遍獞灞卞湴杞︼紝杩樻兂鐜╃壒鎶€锛屽鏄撴憯璺ゃ€?

**EST 鍩虹绀轰緥鏂瑰紡**锛氬厛楠戠杞︼紝鏈夎緟鍔╄疆锛屾參鎱㈠锛岀瓑鐔熺粌浜嗗啀鍗囩骇銆?

瀹冨寘鍚殑鍐呭閮芥槸浣犳棩甯稿紑鍙戜腑鏈€甯哥敤鐨勶細
- 渚濊禆娉ㄥ叆鎬庝箞鐢?
- 閰嶇疆鎬庝箞绠?
- 缂撳瓨鎬庝箞瀛?
- 鏁版嵁鎬庝箞鏌?
- 绠€鍗曠殑 Web 搴旂敤鎬庝箞鍐?

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鎳?* - 姣忎釜绀轰緥閮戒笓娉ㄤ竴涓姛鑳斤紝娌℃湁澶嶆潅閫昏緫
- 鈿?**鍙洿鎺ヨ繍琛?* - 澶嶅埗灏辫兘璺戯紝涓嶇敤棰濆閰嶇疆
- 馃摎 **瑕嗙洊鍏ㄩ潰** - 浠庡鍣ㄥ埌 Web锛屼粠缂撳瓨鍒版暟鎹?
- 馃帗 **寰簭娓愯繘** - 浠庣畝鍗曞埌澶嶆潅锛岄€傚悎涓嶅悓姘村钩
- 馃搱 **鐪熷疄鍦烘櫙** - 绀轰緥鏉ヨ嚜瀹為檯椤圭洰闇€姹?

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細纭繚椤圭洰宸叉瀯寤?

```bash
cd est1.3
mvn clean install -DskipTests
```

### 绗簩姝ワ細杩愯浣犵殑绗竴涓ず渚?

```java
import ltd.idcu.est.core.container.api.Container;
import ltd.idcu.est.core.container.impl.DefaultContainer;

public class FirstExample {
    public static void main(String[] args) {
        System.out.println("=== EST 鍩虹绀轰緥 - 绗竴涓鍣?===\n");
        
        // 1. 鍒涘缓瀹瑰櫒
        Container container = new DefaultContainer();
        
        // 2. 娉ㄥ唽涓€涓瓧绗︿覆
        container.registerSingleton(String.class, "浣犲ソ锛孍ST 鍩虹绀轰緥锛?);
        
        // 3. 鑾峰彇骞朵娇鐢?
        String message = container.get(String.class);
        System.out.println("缁撴灉锛? + message);
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熻繍琛屼簡绗竴涓熀纭€绀轰緥锛?);
    }
}
```

### 绗笁姝ワ細杩愯 Main 绫伙紙鎺ㄨ崘锛?

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

---

## 绀轰緥鍒嗙被

### 1. 鏍稿績瀹瑰櫒绀轰緥 (core/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Core01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/core/Core01_FirstExample.java) | 绗竴涓鍣ㄧず渚?| 瀹瑰櫒鍩虹 | 猸?|
| [Core02_BasicRegistration](src/main/java/ltd/idcu/est/examples/basic/core/Core02_BasicRegistration.java) | 鍩虹娉ㄥ唽 | 渚濊禆娉ㄥ唽 | 猸?|
| [Core03_ConstructorInjection](src/main/java/ltd/idcu/est/examples/basic/core/Core03_ConstructorInjection.java) | 鏋勯€犲櫒娉ㄥ叆 | 鏋勯€犲櫒娉ㄥ叆 | 猸?|
| [Core04_AnnotationInjection](src/main/java/ltd/idcu/est/examples/basic/core/Core04_AnnotationInjection.java) | 娉ㄨВ娉ㄥ叆 | 娉ㄨВ娉ㄥ叆 | 猸?|
| [Core05_ScopeExample](src/main/java/ltd/idcu/est/examples/basic/core/Core05_ScopeExample.java) | Bean 浣滅敤鍩?| Singleton/Prototype | 猸?|
| [Core06_LifecycleExample](src/main/java/ltd/idcu/est/examples/basic/core/Core06_LifecycleExample.java) | 鐢熷懡鍛ㄦ湡 | 鍒濆鍖?閿€姣?| 猸?|
| [Core07_ConfigExample](src/main/java/ltd/idcu/est/examples/basic/core/Core07_ConfigExample.java) | 閰嶇疆绠＄悊 | 閰嶇疆璇诲啓 | 猸?|
| [Core08_CollectionIntegration](src/main/java/ltd/idcu/est/examples/basic/core/Core08_CollectionIntegration.java) | 闆嗗悎闆嗘垚 | 涓?est-collection 闆嗘垚 | 猸愨瓙 |

### 2. 缂撳瓨绀轰緥 (cache/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Cache01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/cache/Cache01_FirstExample.java) | 缂撳瓨绗竴涓ず渚?| 缂撳瓨鍩虹 | 猸?|
| [Cache02_BasicOperations](src/main/java/ltd/idcu/est/examples/basic/cache/Cache02_BasicOperations.java) | 缂撳瓨鍩虹鎿嶄綔 | CRUD 鎿嶄綔 | 猸?|
| [Cache03_StatsExample](src/main/java/ltd/idcu/est/examples/basic/cache/Cache03_StatsExample.java) | 缂撳瓨缁熻 | 缁熻鎸囨爣 | 猸?|
| [Cache04_LruExample](src/main/java/ltd/idcu/est/examples/basic/cache/Cache04_LruExample.java) | LRU 缂撳瓨 | 娣樻卑绛栫暐 | 猸愨瓙 |
| [Cache05_CollectionIntegration](src/main/java/ltd/idcu/est/examples/basic/cache/Cache05_CollectionIntegration.java) | 闆嗗悎闆嗘垚 | 涓?est-collection 闆嗘垚 | 猸愨瓙 |

### 3. 浜嬩欢绀轰緥 (event/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Event01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/event/Event01_FirstExample.java) | 浜嬩欢绗竴涓ず渚?| 浜嬩欢鎬荤嚎 | 猸?|
| [Event02_CreateEventBus](src/main/java/ltd/idcu/est/examples/basic/event/Event02_CreateEventBus.java) | 鍒涘缓浜嬩欢鎬荤嚎 | 浜嬩欢鎬荤嚎鍒涘缓 | 猸?|
| [Event03_Subscribe](src/main/java/ltd/idcu/est/examples/basic/event/Event03_Subscribe.java) | 璁㈤槄浜嬩欢 | 浜嬩欢璁㈤槄 | 猸?|
| [Event04_Publish](src/main/java/ltd/idcu/est/examples/basic/event/Event04_Publish.java) | 鍙戝竷浜嬩欢 | 浜嬩欢鍙戝竷 | 猸?|
| [Event05_Unsubscribe](src/main/java/ltd/idcu/est/examples/basic/event/Event05_Unsubscribe.java) | 鍙栨秷璁㈤槄 | 鍙栨秷璁㈤槄 | 猸?|
| [Event06_Stats](src/main/java/ltd/idcu/est/examples/basic/event/Event06_Stats.java) | 浜嬩欢缁熻 | 缁熻鎸囨爣 | 猸?|
| [Event07_ECommerce](src/main/java/ltd/idcu/est/examples/basic/event/Event07_ECommerce.java) | 鐢靛晢鍦烘櫙 | 瀹為檯鍦烘櫙搴旂敤 | 猸愨瓙 |

### 4. 鏁版嵁绀轰緥 (data/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Data01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/data/Data01_FirstExample.java) | 鏁版嵁璁块棶绗竴涓ず渚?| 鏁版嵁璁块棶 | 猸?|
| [Data02_BasicCrud](src/main/java/ltd/idcu/est/examples/basic/data/Data02_BasicCrud.java) | 鍩虹 CRUD | CRUD 鎿嶄綔 | 猸?|
| [Data03_AdvancedQuery](src/main/java/ltd/idcu/est/examples/basic/data/Data03_AdvancedQuery.java) | 楂樼骇鏌ヨ | 鏌ヨ鎿嶄綔 | 猸愨瓙 |
| [Data04_CollectionIntegration](src/main/java/ltd/idcu/est/examples/basic/data/Data04_CollectionIntegration.java) | 闆嗗悎闆嗘垚 | 涓?est-collection 闆嗘垚 | 猸愨瓙 |

### 5. 闆嗗悎绀轰緥 (collection/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Collection01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/collection/Collection01_FirstExample.java) | 闆嗗悎绗竴涓ず渚?| 闆嗗悎鎿嶄綔 | 猸?|
| [Collection02_CreateSeq](src/main/java/ltd/idcu/est/examples/basic/collection/Collection02_CreateSeq.java) | 鍒涘缓搴忓垪 | 搴忓垪鍒涘缓 | 猸?|
| [Collection03_BasicOperations](src/main/java/ltd/idcu/est/examples/basic/collection/Collection03_BasicOperations.java) | 鍩虹鎿嶄綔 | 鍩虹鎿嶄綔 | 猸?|
| [Collection04_FilterAndMap](src/main/java/ltd/idcu/est/examples/basic/collection/Collection04_FilterAndMap.java) | 杩囨护鍜屾槧灏?| 鍑芥暟寮忔搷浣?| 猸?|
| [Collection05_SortAndSlice](src/main/java/ltd/idcu/est/examples/basic/collection/Collection05_SortAndSlice.java) | 鎺掑簭鍜屽垏鐗?| 鎺掑簭鍜屽垏鐗?| 猸?|
| [Collection06_ObjectOperations](src/main/java/ltd/idcu/est/examples/basic/collection/Collection06_ObjectOperations.java) | 瀵硅薄鎿嶄綔 | 瀵硅薄鎿嶄綔 | 猸愨瓙 |
| [Collection07_Aggregation](src/main/java/ltd/idcu/est/examples/basic/collection/Collection07_Aggregation.java) | 鑱氬悎鎿嶄綔 | 鑱氬悎鎿嶄綔 | 猸愨瓙 |
| [Collection08_RealWorldExample](src/main/java/ltd/idcu/est/examples/basic/collection/Collection08_RealWorldExample.java) | 鐪熷疄鍦烘櫙 | 瀹為檯鍦烘櫙搴旂敤 | 猸愨瓙 |

### 6. Web 绀轰緥 (web/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Web01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/web/Web01_FirstExample.java) | Web 绗竴涓ず渚?| Web 鍩虹 | 猸?|
| [Web02_Router](src/main/java/ltd/idcu/est/examples/basic/web/Web02_Router.java) | 璺敱绀轰緥 | 璺敱閰嶇疆 | 猸?|
| [Web03_HotReloadExample](src/main/java/ltd/idcu/est/examples/basic/web/Web03_HotReloadExample.java) | 鐑噸杞界ず渚?| 鐑噸杞?| 猸愨瓙 |

### 7. 璁捐妯″紡绀轰緥 (patterns/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Patterns01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/patterns/Patterns01_FirstExample.java) | 璁捐妯″紡绗竴涓ず渚?| 璁捐妯″紡 | 猸?|
| [Patterns02_Singleton](src/main/java/ltd/idcu/est/examples/basic/patterns/Patterns02_Singleton.java) | 鍗曚緥妯″紡 | 鍗曚緥妯″紡 | 猸?|
| [Patterns03_Factory](src/main/java/ltd/idcu/est/examples/basic/patterns/Patterns03_Factory.java) | 宸ュ巶妯″紡 | 宸ュ巶妯″紡 | 猸?|
| [Patterns04_Observer](src/main/java/ltd/idcu/est/examples/basic/patterns/Patterns04_Observer.java) | 瑙傚療鑰呮ā寮?| 瑙傚療鑰呮ā寮?| 猸愨瓙 |

### 8. 宸ュ叿绫荤ず渚?(utils/)

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [Utils01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/utils/Utils01_FirstExample.java) | 宸ュ叿绫荤涓€涓ず渚?| 宸ュ叿绫?| 猸?|
| [Utils02_StringUtils](src/main/java/ltd/idcu/est/examples/basic/utils/Utils02_StringUtils.java) | 瀛楃涓插伐鍏?| 瀛楃涓叉搷浣?| 猸?|
| [Utils03_ObjectUtils](src/main/java/ltd/idcu/est/examples/basic/utils/Utils03_ObjectUtils.java) | 瀵硅薄宸ュ叿 | 瀵硅薄鎿嶄綔 | 猸?|
| [Utils04_AssertUtils](src/main/java/ltd/idcu/est/examples/basic/utils/Utils04_AssertUtils.java) | 鏂█宸ュ叿 | 鏂█ | 猸?|
| [Utils05_ArrayUtils](src/main/java/ltd/idcu/est/examples/basic/utils/Utils05_ArrayUtils.java) | 鏁扮粍宸ュ叿 | 鏁扮粍鎿嶄綔 | 猸?|
| [Utils06_NumberUtils](src/main/java/ltd/idcu/est/examples/basic/utils/Utils06_NumberUtils.java) | 鏁板瓧宸ュ叿 | 鏁板瓧鎿嶄綔 | 猸?|
| [Utils07_CollectionIntegration](src/main/java/ltd/idcu/est/examples/basic/utils/Utils07_CollectionIntegration.java) | 闆嗗悎闆嗘垚 | 涓?est-collection 闆嗘垚 | 猸愨瓙 |

### 9. 缁煎悎绀轰緥

| 绀轰緥 | 璇存槑 | 鐭ヨ瘑鐐?| 闅惧害 |
|------|------|--------|------|
| [CoreExample](src/main/java/ltd/idcu/est/examples/basic/CoreExample.java) | 鏍稿績鍔熻兘缁煎悎绀轰緥 | 鏍稿績鍔熻兘 | 猸?|
| [AutowiringExample](src/main/java/ltd/idcu/est/examples/basic/AutowiringExample.java) | 鑷姩瑁呴厤绀轰緥 | 鑷姩瑁呴厤 | 猸?|
| [ComponentScanExample](src/main/java/ltd/idcu/est/examples/basic/ComponentScanExample.java) | 缁勪欢鎵弿绀轰緥 | 缁勪欢鎵弿 | 猸?|
| [ConditionalInjectionExample](src/main/java/ltd/idcu/est/examples/basic/ConditionalInjectionExample.java) | 鏉′欢娉ㄥ叆绀轰緥 | 鏉′欢娉ㄥ叆 | 猸愨瓙 |
| [EnhancedContainerExample](src/main/java/ltd/idcu/est/examples/basic/EnhancedContainerExample.java) | 澧炲己瀹瑰櫒绀轰緥 | 澧炲己鍔熻兘 | 猸愨瓙 |
| [LifecycleContainerExample](src/main/java/ltd/idcu/est/examples/basic/LifecycleContainerExample.java) | 鐢熷懡鍛ㄦ湡瀹瑰櫒绀轰緥 | 鐢熷懡鍛ㄦ湡 | 猸?|
| [PatternExample](src/main/java/ltd/idcu/est/examples/basic/PatternExample.java) | 璁捐妯″紡绀轰緥 | 璁捐妯″紡 | 猸?|
| [UtilsExample](src/main/java/ltd/idcu/est/examples/basic/UtilsExample.java) | 宸ュ叿绫荤ず渚?| 宸ュ叿绫?| 猸?|
| [Main](src/main/java/ltd/idcu/est/examples/basic/Main.java) | 涓诲叆鍙?| 杩愯鎵€鏈夌ず渚?| 猸?|

---

## 濡備綍杩愯绀轰緥

### 鍓嶇疆鏉′欢

- 鉁?JDK 21+
- 鉁?Maven 3.6+
- 鉁?宸茬粡鏋勫缓浜?EST 椤圭洰锛坄mvn clean install`锛?

### 鏂瑰紡涓€锛氳繍琛?Main 绫伙紙鎺ㄨ崘锛?

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.Main"
```

### 鏂瑰紡浜岋細杩愯鐗瑰畾绀轰緥

```bash
cd est-examples/est-examples-basic
mvn exec:java -Dexec.mainClass="ltd.idcu.est.examples.basic.core.Core01_FirstExample"
```

### 鏂瑰紡涓夛細鍦?IDE 涓繍琛?

1. 鐢?IDE 鎵撳紑椤圭洰
2. 鎵惧埌绀轰緥绫?
3. 鐩存帴杩愯 main 鏂规硶

---

## 鏈€浣冲疄璺?

### 1. 浠庣畝鍗曞紑濮?

```java
// 鉁?鎺ㄨ崘锛氬厛鐢ㄧ畝鍗曞疄鐜?
Container container = new DefaultContainer();
container.registerSingleton(String.class, "Hello");

// 鉂?涓嶆帹鑽愶細涓€涓婃潵灏辩敤澶嶆潅閰嶇疆
Container container = DefaultContainer.builder()
    .scanPackages("com.example")
    .enableLazyLoading()
    .build();
```

**涓轰粈涔?*锛?
- 鍏堣窇閫氾紝鍐嶄紭鍖?
- 绠€鍗曞疄鐜版洿瀹规槗鐞嗚В鍜岃皟璇?
- 绛変綘鐔熸倝浜嗭紝鍐嶈€冭檻楂樼骇閰嶇疆

### 2. 鏈夋剰涔夌殑鍛藉悕

```java
// 鉁?鎺ㄨ崘锛氭湁鎰忎箟鐨勫彉閲忓悕
Cache<String, User> userCache = Caches.newMemoryCache();
userCache.put("user:1", new User("寮犱笁"));

// 鉂?涓嶆帹鑽愶細鍙橀噺鍚嶇湅涓嶆噦
Cache<String, User> c = Caches.newMemoryCache();
c.put("u:1", new User("zs"));
```

### 3. 姝ｇ‘浣跨敤 Optional

```java
// 鉁?鎺ㄨ崘锛氫娇鐢?Optional 澶勭悊鍙兘涓虹┖鐨勬儏鍐?
Optional<User> userOpt = userCache.get("user:1");
userOpt.ifPresent(user -> System.out.println(user));

// 鎴栨彁渚涢粯璁ゅ€?
User user = userOpt.orElse(defaultUser);

// 鉂?涓嶆帹鑽愶細鐩存帴 get锛屽彲鑳芥姏寮傚父
User user = userCache.get("user:1").get();
```

### 4. 璁板緱娓呯悊璧勬簮

```java
// 鉁?鎺ㄨ崘锛氫娇鐢?try-with-resources
try (Container container = new DefaultContainer()) {
    container.registerSingleton(String.class, "Hello");
    String message = container.get(String.class);
    System.out.println(message);
}

// 鉂?涓嶆帹鑽愶細涓嶅叧闂祫婧愶紝鍙兘瀵艰嚧娉勬紡
Container container = new DefaultContainer();
// 浣跨敤瀹屽氨涓嶇浜?
```

---

## 甯歌闂

### Q: 绀轰緥杩愯澶辫触鎬庝箞鍔烇紵

A: 棣栧厛纭繚锛?
1. 浣犲凡缁忚繍琛屼簡 `mvn clean install`
2. JDK 鐗堟湰鏄?21+
3. Maven 鐗堟湰鏄?3.6+
4. 妫€鏌ユ槸鍚︽湁缂栬瘧閿欒

### Q: 濡備綍鎵惧埌鎴戦渶瑕佺殑绀轰緥锛?

A: 
1. 鍏堢湅鍒嗙被锛屾壘鍒扮浉鍏冲姛鑳?
2. 鐪嬬ず渚嬭鏄庯紝鎵惧埌鍖归厤鐨?
3. 浠庡熀纭€绀轰緥寮€濮嬶紝寰簭娓愯繘

### Q: 绀轰緥浠ｇ爜鍙互鐩存帴鐢ㄥ埌椤圭洰涓悧锛?

A: 鍙互锛佷絾寤鸿锛?
1. 鍏堢悊瑙ｇず渚嬩唬鐮?
2. 鏍规嵁浣犵殑椤圭洰闇€姹傝皟鏁?
3. 缁撳悎鏈€浣冲疄璺典娇鐢?

---

## 涓嬩竴姝?

- 馃殌 浠?[Core01_FirstExample](src/main/java/ltd/idcu/est/examples/basic/core/Core01_FirstExample.java) 寮€濮?
- 馃摉 鎯崇湅鏇村绀轰緥锛岀湅 [鍔熻兘绀轰緥](../est-examples-features/)
- 馃挕 鎯充簡瑙?Web 寮€鍙戯紝鐪?[Web 绀轰緥](../est-examples-web/)
- 馃帗 鎯冲鏈€浣冲疄璺碉紝鐪?[鏈€浣冲疄璺佃绋媇(../../docs/best-practices/course/README.md)

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-07  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
