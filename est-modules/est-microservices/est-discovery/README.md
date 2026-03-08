# EST Discovery 鏈嶅姟鍙戠幇妯″潡 - 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Discovery锛焆(#浠€涔堟槸-est-discovery)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [楂樼骇绡嘳(#楂樼骇绡?
6. [涓庡叾浠栨ā鍧楅泦鎴怾(#涓庡叾浠栨ā鍧楅泦鎴?
7. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?
8. [甯歌闂](#甯歌闂)
9. [涓嬩竴姝(#涓嬩竴姝?

---

## 浠€涔堟槸 EST Discovery锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Discovery 灏卞儚鏄竴涓?鏈嶅姟閫氳褰?銆傛兂璞′竴涓嬩綘鍦ㄤ竴涓ぇ鍏徃閲岋紝鏈夊緢澶氶儴闂紙鏈嶅姟锛夛紝浣犻渶瑕佹壘鏌愪釜閮ㄩ棬鍔炰簨锛?
**浼犵粺鏂瑰紡**锛氫綘蹇呴』璁颁綇姣忎釜閮ㄩ棬鐨勫叿浣撲綅缃拰鐢佃瘽锛屾崲浣嶇疆浜嗚繕瑕侀噸鏂拌锛屽お楹荤儲锛?
**EST Discovery 鏂瑰紡**锛氭湁涓€涓粺涓€鐨勯€氳褰曪紝鏈嶅姟鍚姩鏃惰嚜鍔ㄧ櫥璁帮紝浣犺鎵捐皝灏卞幓閫氳褰曟煡锛?- 鏈嶅姟娉ㄥ唽锛氭湇鍔″惎鍔ㄦ椂鑷姩鎶ュ埌
- 蹇冭烦妫€娴嬶細瀹氭湡纭鏈嶅姟杩樻椿鐫€
- 璐熻浇鍧囪　锛氬涓疄渚嬫椂鑷姩閫夋嫨
- 鏈嶅姟鍙戠幇锛氬揩閫熸壘鍒伴渶瑕佺殑鏈嶅姟

瀹冩敮鎸佸绉嶈礋杞藉潎琛＄瓥鐣ワ細闅忔満銆佽疆璇紝鎯崇敤鍝鐢ㄥ摢绉嶏紒

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 鍑犺浠ｇ爜灏辫兘娉ㄥ唽鍜屽彂鐜版湇鍔?- 馃殌 **楂樻€ц兘** - 鍐呭瓨瀛樺偍锛屾煡璇㈤€熷害蹇?- 馃攧 **蹇冭烦鏈哄埗** - 鑷姩妫€娴嬫湇鍔＄姸鎬?- 馃搳 **璐熻浇鍧囪　** - 鏀寔澶氱绛栫暐
- 馃捑 **鎸佷箙鍖栨敮鎸?* - 鏀寔JSON鏍煎紡鐨勬湇鍔″疄渚嬫寔涔呭寲
- 馃攧 **鑷姩淇濆瓨** - 鏈嶅姟鍙樻洿鏃惰嚜鍔ㄤ繚瀛樺埌鏂囦欢
- 馃搱 **鍙墿灞?* - 杞绘澗娣诲姞鑷畾涔夎礋杞藉潎琛″櫒

---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery-api</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-discovery-impl</artifactId>
        <version>2.1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓湇鍔℃敞鍐?
```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class FirstDiscoveryExample {
    public static void main(String[] args) {
        System.out.println("=== EST Discovery 绗竴涓ず渚?===\n");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        registry.register(instance);
        
        System.out.println("鏈嶅姟娉ㄥ唽鎴愬姛: " + instance);
        System.out.println("褰撳墠鏈嶅姟鏁? " + registry.getServiceIds().size());
        
        System.out.println("\n鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Discovery 浜嗭紒");
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細
```
=== EST Discovery 绗竴涓ず渚?===

鏈嶅姟娉ㄥ唽鎴愬姛: ServiceInstance{serviceId='user-service', instanceId='instance-1', host='localhost', port=8081}
褰撳墠鏈嶅姟鏁? 1

鎭枩浣狅紒浣犲凡缁忔垚鍔熶娇鐢?EST Discovery 浜嗭紒
```

---

## 鍩虹绡?
### 1. 浠€涔堟槸 ServiceRegistry锛?
ServiceRegistry 灏辨槸涓€涓?鏈嶅姟娉ㄥ唽涓績"鎺ュ彛锛屽畠鐨勬牳蹇冩搷浣滈潪甯哥畝鍗曪細

```java
public interface ServiceRegistry {
    void register(ServiceInstance instance);              // 娉ㄥ唽鏈嶅姟
    void unregister(String serviceId, String instanceId); // 娉ㄩ攢鏈嶅姟
    void heartbeat(String serviceId, String instanceId);  // 鍙戦€佸績璺?    Optional<ServiceInstance> getInstance(String serviceId, String instanceId); // 鑾峰彇瀹炰緥
    List<ServiceInstance> getInstances(String serviceId); // 鑾峰彇鏈嶅姟鐨勬墍鏈夊疄渚?    List<String> getServiceIds();                          // 鑾峰彇鎵€鏈夋湇鍔D
    void clear();                                           // 娓呯┖鎵€鏈夋湇鍔?}
```

### 2. 鍒涘缓娉ㄥ唽涓績鐨勫嚑绉嶆柟寮?
```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class CreateRegistryExample {
    public static void main(String[] args) {
        System.out.println("--- 鏂瑰紡涓€锛氶粯璁ゆ敞鍐屼腑蹇?---");
        ServiceRegistry registry1 = new DefaultServiceRegistry();
        System.out.println("榛樿娉ㄥ唽涓績鍒涘缓鎴愬姛");
        
        System.out.println("\n--- 鏂瑰紡浜岋細娉ㄥ唽鏈嶅姟 ---");
        ServiceRegistry registry2 = new DefaultServiceRegistry();
        ServiceInstance instance = new ServiceInstance("order-service", "instance-1", "localhost", 8082);
        registry2.register(instance);
        System.out.println("鏈嶅姟娉ㄥ唽鎴愬姛");
    }
}
```

### 3. 鍩烘湰鎿嶄綔

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

import java.util.List;
import java.util.Optional;

public class BasicOperations {
    public static void main(String[] args) {
        ServiceRegistry registry = new DefaultServiceRegistry();
        
        System.out.println("--- 1. 娉ㄥ唽鏈嶅姟 ---");
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8082);
        ServiceInstance instance3 = new ServiceInstance("order-service", "instance-1", "localhost", 8083);
        registry.register(instance1);
        registry.register(instance2);
        registry.register(instance3);
        System.out.println("娉ㄥ唽浜?3 涓湇鍔″疄渚?);
        
        System.out.println("\n--- 2. 鑾峰彇鎵€鏈夋湇鍔D ---");
        List<String> serviceIds = registry.getServiceIds();
        System.out.println("鏈嶅姟鍒楄〃: " + serviceIds);
        
        System.out.println("\n--- 3. 鑾峰彇鏈嶅姟鐨勬墍鏈夊疄渚?---");
        List<ServiceInstance> userInstances = registry.getInstances("user-service");
        System.out.println("user-service 瀹炰緥鏁? " + userInstances.size());
        
        System.out.println("\n--- 4. 鑾峰彇鐗瑰畾瀹炰緥 ---");
        Optional<ServiceInstance> found = registry.getInstance("user-service", "instance-1");
        found.ifPresent(i -> System.out.println("鎵惧埌瀹炰緥: " + i));
        
        System.out.println("\n--- 5. 鍙戦€佸績璺?---");
        registry.heartbeat("user-service", "instance-1");
        System.out.println("蹇冭烦鍙戦€佹垚鍔?);
        
        System.out.println("\n--- 6. 娉ㄩ攢鏈嶅姟 ---");
        registry.unregister("order-service", "instance-1");
        System.out.println("娉ㄩ攢浜?order-service");
    }
}
```

---

## 杩涢樁绡?
### 1. 璐熻浇鍧囪　锛圠oadBalancer锛?
浣犲彲浠ヤ娇鐢ㄨ礋杞藉潎琛″櫒鏉ラ€夋嫨鏈嶅姟瀹炰緥锛?
```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.discovery.impl.RandomLoadBalancer;
import ltd.idcu.est.discovery.impl.RoundRobinLoadBalancer;

import java.util.List;

public class LoadBalancerExample {
    public static void main(String[] args) {
        System.out.println("--- 璐熻浇鍧囪　绀轰緥 ---");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(new ServiceInstance("user-service", "instance-1", "localhost", 8081));
        registry.register(new ServiceInstance("user-service", "instance-2", "localhost", 8082));
        registry.register(new ServiceInstance("user-service", "instance-3", "localhost", 8083));
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        
        System.out.println("\n--- 闅忔満绛栫暐 ---");
        LoadBalancer randomBalancer = new RandomLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServiceInstance selected = randomBalancer.select(instances);
            System.out.println("閫夋嫨: " + selected.getInstanceId());
        }
        
        System.out.println("\n--- 杞绛栫暐 ---");
        LoadBalancer roundRobinBalancer = new RoundRobinLoadBalancer();
        for (int i = 0; i < 5; i++) {
            ServiceInstance selected = roundRobinBalancer.select(instances);
            System.out.println("閫夋嫨: " + selected.getInstanceId());
        }
    }
}
```

### 2. 鑷畾涔夎礋杞藉潎琛″櫒

```java
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.api.ServiceInstance;

import java.util.List;

public class WeightedLoadBalancer implements LoadBalancer {
    @Override
    public ServiceInstance select(List<ServiceInstance> instances) {
        if (instances == null || instances.isEmpty()) {
            return null;
        }
        return instances.get(0);
    }
}
```

---

## 楂樼骇绡?
### 1. 蹇冭烦绠＄悊

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

public class HeartbeatExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 蹇冭烦绠＄悊绀轰緥 ---");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        registry.register(instance);
        
        System.out.println("鏈嶅姟宸叉敞鍐?);
        
        for (int i = 0; i < 3; i++) {
            Thread.sleep(1000);
            registry.heartbeat("user-service", "instance-1");
            System.out.println("鍙戦€佸績璺?" + (i + 1));
        }
        
        System.out.println("蹇冭烦绠＄悊瀹屾垚");
    }
}
```

---

## 楂樼骇绡?
### 1. 鏈嶅姟瀹炰緥鎸佷箙鍖?
DefaultServiceRegistry 鏀寔 JSON 鏍煎紡鐨勬寔涔呭寲锛岀▼搴忛噸鍚悗鍙互鎭㈠鏈嶅姟瀹炰緥鏁版嵁锛?
```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;

import java.io.File;

public class PersistenceExample {
    public static void main(String[] args) {
        System.out.println("--- 鏈嶅姟鍙戠幇鎸佷箙鍖栫ず渚?---");
        
        File dataFile = new File("service-registry.json");
        
        ServiceRegistry registry = new DefaultServiceRegistry(dataFile);
        
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        instance.setTags(java.util.Arrays.asList("production", "v1.0"));
        registry.register(instance);
        
        System.out.println("鏈嶅姟宸叉敞鍐岋紝鏁版嵁宸茶嚜鍔ㄤ繚瀛樺埌: " + dataFile.getAbsolutePath());
        
        ServiceRegistry newRegistry = new DefaultServiceRegistry(dataFile);
        System.out.println("浠庢枃浠跺姞杞藉悗锛屾湇鍔℃暟: " + newRegistry.getServiceIds().size());
    }
}
```

### 2. 鏈嶅姟瀹炰緥鏍囩

ServiceInstance 鏀寔娣诲姞鏍囩锛屾柟渚胯繘琛屾湇鍔″垎缁勫拰绛涢€夛細

```java
import ltd.idcu.est.discovery.api.ServiceInstance;

public class TagsExample {
    public static void main(String[] args) {
        ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        instance.setTags(java.util.Arrays.asList("production", "v1.0", "primary"));
        
        System.out.println("鏈嶅姟鏍囩: " + instance.getTags());
    }
}
```

---

## 涓庡叾浠栨ā鍧楅泦鎴?
EST Discovery 鍜?est-gateway 鏄粷閰嶏紒璁╂垜浠湅鐪嬪畠浠浣曢厤鍚堜娇鐢細

### 鍦烘櫙锛氭湇鍔″彂鐜?+ 缃戝叧璺敱

```java
import ltd.idcu.est.discovery.api.ServiceRegistry;
import ltd.idcu.est.discovery.api.ServiceInstance;
import ltd.idcu.est.discovery.api.LoadBalancer;
import ltd.idcu.est.discovery.impl.DefaultServiceRegistry;
import ltd.idcu.est.discovery.impl.RoundRobinLoadBalancer;
import ltd.idcu.est.gateway.api.ApiGateway;
import ltd.idcu.est.gateway.api.Route;
import ltd.idcu.est.gateway.impl.DefaultApiGateway;
import ltd.idcu.est.gateway.impl.DefaultRoute;

import java.util.List;

public class GatewayIntegrationExample {
    public static void main(String[] args) {
        System.out.println("=== EST Discovery + EST Gateway 闆嗘垚绀轰緥 ===\n");
        
        ServiceRegistry registry = new DefaultServiceRegistry();
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
        
        ServiceInstance instance1 = new ServiceInstance("user-service", "instance-1", "localhost", 8081);
        ServiceInstance instance2 = new ServiceInstance("user-service", "instance-2", "localhost", 8082);
        registry.register(instance1);
        registry.register(instance2);
        
        ApiGateway gateway = new DefaultApiGateway();
        
        List<ServiceInstance> instances = registry.getInstances("user-service");
        ServiceInstance selected = loadBalancer.select(instances);
        
        Route route = new DefaultRoute("/api/users", 
            "http://" + selected.getHost() + ":" + selected.getPort());
        gateway.getRouter().addRoute(route);
        
        System.out.println("璺敱宸查厤缃紝鎸囧悜: " + selected);
    }
}
```

---

## 鏈€浣冲疄璺?
### 1. 鍚堢悊璁剧疆鏈嶅姟ID

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄦ湁鎰忎箟鐨勬湇鍔D
ServiceInstance instance = new ServiceInstance("user-service", "instance-1", "localhost", 8081);

// 鉁?涓嶆帹鑽愶細鏈嶅姟ID澶殢鎰?ServiceInstance badInstance = new ServiceInstance("srv1", "i1", "localhost", 8081);
```

### 2. 瀹氭湡鍙戦€佸績璺?
```java
// 鉁?鎺ㄨ崘锛氬畾鏈熷彂閫佸績璺充繚鎸佹湇鍔℃椿璺?ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(() -> {
    registry.heartbeat("user-service", "instance-1");
}, 0, 30, TimeUnit.SECONDS);
```

### 3. 鐩戞帶鏈嶅姟鐘舵€?
```java
List<String> serviceIds = registry.getServiceIds();
System.out.println("褰撳墠鍦ㄧ嚎鏈嶅姟鏁? " + serviceIds.size());
```

---

## 甯歌闂

### Q: 鏈嶅姟淇℃伅浼氭寔涔呭寲鍚楋紵

A: DefaultServiceRegistry 鏄唴瀛樺瓨鍌紝绋嬪簭閲嶅惎鍚庢暟鎹細涓㈠け銆傚悗缁増鏈細鏀寔鎸佷箙鍖栥€?
### Q: 濡備綍瀹炵幇鏈嶅姟鍋ュ悍妫€鏌ワ紵

A: 鍙互閫氳繃蹇冭烦鏈哄埗鏉ュ疄鐜帮紝瀹氭湡鍙戦€佸績璺崇‘璁ゆ湇鍔″瓨娲汇€?
### Q: 鏀寔璺ㄦ満鎴块儴缃插悧锛?
A: 褰撳墠鐗堟湰鏀寔鍚屼竴缃戠粶鍐呯殑鏈嶅姟鍙戠幇锛屽悗缁増鏈細鏀寔璺ㄦ満鎴裤€?
---

## 涓嬩竴姝?
- 瀛︿範 [est-gateway](../est-gateway/README.md) 杩涜缃戝叧璺敱
- 鏌ョ湅 [est-circuitbreaker](../est-circuitbreaker/) 浜嗚В鐔旀柇淇濇姢
- 灏濊瘯鑷畾涔夎礋杞藉潎琛″櫒
- 闃呰 [API 鏂囨。](../../docs/api/discovery/) 浜嗚В鏇村缁嗚妭

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-06  
**缁存姢鑰?*: EST 鏋舵瀯鍥㈤槦
