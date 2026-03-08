# EST Event - 浜嬩欢鎬荤嚎绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸浜嬩欢鎬荤嚎锛?

鎯宠薄涓€涓嬶紝浣犲湪涓€涓ぇ鍨嬪叕鍙搁噷宸ヤ綔銆傚叕鍙告湁寰堝閮ㄩ棬锛氶攢鍞儴銆佽储鍔￠儴銆佷汉浜嬮儴銆佹妧鏈儴绛夌瓑銆?

浠ュ墠锛屽綋閿€鍞儴鎺ュ埌涓€涓鍗曟椂锛屼粬浠渶瑕侊細
1. 鎵撶數璇濈粰璐㈠姟閮紝璁╀粬浠璐?
2. 鍙戦偖浠剁粰浠撳簱锛岃浠栦滑鍑嗗鍙戣揣
3. 鍙戠煭淇＄粰瀹㈡湇锛岃浠栦滑璺熻繘瀹㈡埛

姣忎釜閮ㄩ棬閮借鎵嬪姩閫氱煡锛岄潪甯搁夯鐑︼紒

**浜嬩欢鎬荤嚎**灏卞儚鍏徃鐨勫箍鎾郴缁燂細
- 閿€鍞儴鍙渶瑕佸枈涓€澹帮細"鎴戜滑鎺ュ埌璁㈠崟鍟︼紒"锛堝彂甯冧簨浠讹級
- 璐㈠姟閮ㄥ惉鍒板箍鎾紝鑷姩寮€濮嬭璐︼紙鐩戝惉浜嬩欢锛?
- 浠撳簱鍚埌骞挎挱锛岃嚜鍔ㄥ噯澶囧彂璐э紙鐩戝惉浜嬩欢锛?
- 瀹㈡湇鍚埌骞挎挱锛岃嚜鍔ㄨ窡杩涘鎴凤紙鐩戝惉浜嬩欢锛?

杩欐牱鏄笉鏄柟渚垮浜嗭紵

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑浜嬩欢鎬荤嚎绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-event-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-event-local</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventFirstExample {
    public static void main(String[] args) {
        // 1. 鍒涘缓涓€涓簨浠舵€荤嚎锛堝氨鍍忔惌寤哄箍鎾郴缁燂級
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 2. 璁㈤槄浜嬩欢锛堝氨鍍忔墦寮€鏀堕煶鏈烘敹鍚箍鎾級
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("璐㈠姟閮ㄦ敹鍒拌鍗? " + orderData);
            System.out.println("寮€濮嬭璐?..");
        });
        
        eventBus.subscribe("order_placed", (event, orderData) -> {
            System.out.println("浠撳簱鏀跺埌璁㈠崟: " + orderData);
            System.out.println("鍑嗗鍙戣揣...");
        });
        
        // 3. 鍙戝竷浜嬩欢锛堝氨鍍忛€氳繃骞挎挱绯荤粺璇磋瘽锛?
        System.out.println("=== 閿€鍞儴鍙戝竷璁㈠崟浜嬩欢 ===");
        eventBus.publish("order_placed", "璁㈠崟鍙? 1001, 鍟嗗搧: 绗旇鏈數鑴?);
        
        System.out.println("\n鉁?绋嬪簭鎵ц瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒帮細

```
=== 閿€鍞儴鍙戝竷璁㈠崟浜嬩欢 ===
璐㈠姟閮ㄦ敹鍒拌鍗? 璁㈠崟鍙? 1001, 鍟嗗搧: 绗旇鏈數鑴?
寮€濮嬭璐?..
浠撳簱鏀跺埌璁㈠崟: 璁㈠崟鍙? 1001, 鍟嗗搧: 绗旇鏈數鑴?
鍑嗗鍙戣揣...

鉁?绋嬪簭鎵ц瀹屾垚锛?
```

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤浜嬩欢鎬荤嚎锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

鍦ㄦ繁鍏ュ涔犱箣鍓嶏紝璁╂垜浠厛鐞嗚В鍑犱釜鏍稿績姒傚康锛?

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **浜嬩欢绫诲瀷** | 浜嬩欢鐨勭绫伙紝姣斿 "order_placed"銆?user_registered" | 骞挎挱棰戦亾锛屾瘮濡?鏂伴椈棰戦亾"銆?闊充箰棰戦亾" |
| **浜嬩欢鏁版嵁** | 浜嬩欢鎼哄甫鐨勫叿浣撲俊鎭?| 骞挎挱鐨勫唴瀹癸紝姣斿"浠婂ぉ澶╂皵鏅存湕" |
| **浜嬩欢鐩戝惉鍣?* | 澶勭悊浜嬩欢鐨勪唬鐮?| 鏀跺惉骞挎挱鐨勪汉 |
| **浜嬩欢鎬荤嚎** | 绠＄悊浜嬩欢鍙戝竷鍜岃闃呯殑鏍稿績缁勪欢 | 骞挎挱绯荤粺 |
| **鍙戝竷** | 鍙戦€佷簨浠跺埌浜嬩欢鎬荤嚎 | 鍦ㄥ箍鎾郴缁熶腑璇磋瘽 |
| **璁㈤槄** | 娉ㄥ唽鐩戝惉鍣ㄦ潵鎺ユ敹浜嬩欢 | 鎵撳紑鏀堕煶鏈烘敹鍚煇涓閬?|

### 2. 鍒涘缓浜嬩欢鎬荤嚎

EST Event 鎻愪緵浜嗕袱绉嶄富瑕佺殑浜嬩欢鎬荤嚎瀹炵幇锛?

#### 2.1 鏈湴浜嬩欢鎬荤嚎锛圠ocalEventBus锛?

閫傜敤浜庡崟杩涚▼鍐呯殑浜嬩欢閫氫俊锛?

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventBusCreationExample {
    public static void main(String[] args) {
        // 鏂瑰紡涓€锛氫娇鐢ㄩ粯璁ら厤缃垱寤?
        EventBus bus1 = LocalEvents.newLocalEventBus();
        
        // 鏂瑰紡浜岋細浣跨敤鑷畾涔夐厤缃垱寤?
        EventConfig config = new EventConfig()
                .setMaxListenersPerEvent(500)  // 姣忎釜浜嬩欢鏈€澶?00涓洃鍚櫒
                .setPropagateExceptions(true);  // 浼犳挱寮傚父
        
        EventBus bus2 = LocalEvents.newLocalEventBus(config);
        
        // 鏂瑰紡涓夛細浣跨敤 Builder 妯″紡鍒涘缓
        EventBus bus3 = LocalEvents.builder()
                .maxListenersPerEvent(1000)
                .propagateExceptions(false)
                .build();
        
        System.out.println("鉁?涓夌浜嬩欢鎬荤嚎鍒涘缓鏂瑰紡閮芥垚鍔熶簡锛?);
    }
}
```

#### 2.2 寮傛浜嬩欢鎬荤嚎锛圓syncEventBus锛?

閫傜敤浜庨渶瑕佸紓姝ュ鐞嗕簨浠剁殑鍦烘櫙锛?

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

public class AsyncEventBusExample {
    public static void main(String[] args) {
        // 鍒涘缓寮傛浜嬩欢鎬荤嚎
        EventBus asyncBus = AsyncEvents.newAsyncEventBus();
        
        // 浣跨敤鏂瑰紡鍜屾湰鍦颁簨浠舵€荤嚎涓€鏍?
        asyncBus.subscribe("task_completed", (event, data) -> {
            System.out.println("寮傛澶勭悊浠诲姟瀹屾垚: " + data);
        });
        
        asyncBus.publish("task_completed", "鏁版嵁瀵煎嚭浠诲姟");
        
        System.out.println("鉁?寮傛浜嬩欢鎬荤嚎鍒涘缓鎴愬姛锛?);
    }
}
```

### 3. 璁㈤槄浜嬩欢

璁㈤槄浜嬩欢鏈夊绉嶆柟寮忥紝璁╂垜浠竴涓€瀛︿範锛?

#### 3.1 鍩烘湰璁㈤槄

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEvents;

public class SubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 鏂瑰紡涓€锛氫娇鐢?Lambda 琛ㄨ揪寮?
        eventBus.subscribe("user_login", (event, userData) -> {
            System.out.println("鐢ㄦ埛鐧诲綍: " + userData);
        });
        
        // 鏂瑰紡浜岋細浣跨敤 Consumer锛堥€氳繃 LocalEvents 宸ュ叿绫伙級
        LocalEvents.subscribe(eventBus, "user_login", userData -> {
            System.out.println("鍙︿竴涓洃鍚櫒: " + userData);
        });
        
        // 鏂瑰紡涓夛細浣跨敤瀹屾暣鐨?EventListener 鎺ュ彛
        EventListener<String> listener = (event, data) -> {
            System.out.println("浜嬩欢绫诲瀷: " + event.getEventType());
            System.out.println("浜嬩欢鏁版嵁: " + data);
        };
        eventBus.subscribe("user_login", listener);
        
        // 娴嬭瘯鍙戝竷
        eventBus.publish("user_login", "寮犱笁");
    }
}
```

#### 3.2 甯︿紭鍏堢骇鐨勮闃?

浣犲彲浠ヤ负鐩戝惉鍣ㄨ缃紭鍏堢骇锛屼紭鍏堢骇楂樼殑鐩戝惉鍣ㄤ細鍏堟墽琛岋細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class PrioritySubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 浼樺厛绾ф暟瀛楄秺灏忥紝浼樺厛绾ц秺楂?
        eventBus.subscribe("order_placed", 1, (event, order) -> {
            System.out.println("[浼樺厛绾?1] 楠岃瘉璁㈠崟...");
        });
        
        eventBus.subscribe("order_placed", 2, (event, order) -> {
            System.out.println("[浼樺厛绾?2] 璁板綍璁㈠崟...");
        });
        
        eventBus.subscribe("order_placed", 3, (event, order) -> {
            System.out.println("[浼樺厛绾?3] 鍙戦€侀€氱煡...");
        });
        
        // 鍙戝竷浜嬩欢锛岀湅鐪嬫墽琛岄『搴?
        eventBus.publish("order_placed", "璁㈠崟 1001");
    }
}
```

杩愯缁撴灉锛?
```
[浼樺厛绾?1] 楠岃瘉璁㈠崟...
[浼樺厛绾?2] 璁板綍璁㈠崟...
[浼樺厛绾?3] 鍙戦€侀€氱煡...
```

### 4. 鍙戝竷浜嬩欢

#### 4.1 鍚屾鍙戝竷

鍚屾鍙戝竷鎰忓懗鐫€浜嬩欢浼氬湪褰撳墠绾跨▼涓珛鍗冲鐞嗭細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class SyncPublishExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("message", (event, msg) -> {
            System.out.println("鏀跺埌娑堟伅: " + msg);
        });
        
        // 鍚屾鍙戝竷锛屼細绛夊緟鎵€鏈夌洃鍚櫒澶勭悊瀹屾墠杩斿洖
        System.out.println("寮€濮嬪彂甯冧簨浠?..");
        eventBus.publish("message", "Hello, Event Bus!");
        System.out.println("浜嬩欢鍙戝竷瀹屾垚锛?);
    }
}
```

#### 4.2 寮傛鍙戝竷

寮傛鍙戝竷鎰忓懗鐫€浜嬩欢浼氬湪鍚庡彴绾跨▼涓鐞嗭細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.async.AsyncEvents;

import java.util.concurrent.CompletableFuture;

public class AsyncPublishExample {
    public static void main(String[] args) throws Exception {
        EventBus eventBus = AsyncEvents.newAsyncEventBus();
        
        eventBus.subscribe("long_task", (event, task) -> {
            System.out.println("寮€濮嬪鐞嗕换鍔? " + task);
            try {
                Thread.sleep(1000); // 妯℃嫙鑰楁椂鎿嶄綔
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("浠诲姟澶勭悊瀹屾垚: " + task);
        });
        
        // 寮傛鍙戝竷锛岀珛鍗宠繑鍥?
        System.out.println("寮€濮嬪紓姝ュ彂甯冧簨浠?..");
        CompletableFuture<Void> future = eventBus.publishAsync("long_task", "鏁版嵁瀵煎叆");
        System.out.println("寮傛鍙戝竷瀹屾垚锛岀户缁墽琛屽叾浠栨搷浣?..");
        
        // 绛夊緟寮傛澶勭悊瀹屾垚
        future.get();
        System.out.println("鉁?鎵€鏈夋搷浣滃畬鎴愶紒");
    }
}
```

#### 4.3 甯︿簨浠舵簮鐨勫彂甯?

浣犲彲浠ユ寚瀹氫簨浠剁殑鏉ユ簮锛?

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

public class PublishWithSourceExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        eventBus.subscribe("data_changed", (event, data) -> {
            System.out.println("鏁版嵁鍙樺寲: " + data);
            System.out.println("浜嬩欢鏉ユ簮: " + event.getSource());
        });
        
        // 鍙戝竷鏃舵寚瀹氭潵婧?
        Object source1 = "鐢ㄦ埛鐣岄潰";
        eventBus.publish("data_changed", "鐢ㄦ埛鏇存柊浜嗗鍚?, source1);
        
        Object source2 = "鍚庡彴鏈嶅姟";
        eventBus.publish("data_changed", "绯荤粺鑷姩鍚屾鏁版嵁", source2);
    }
}
```

### 5. 鍙栨秷璁㈤槄

褰撲綘涓嶅啀闇€瑕佺洃鍚煇涓簨浠舵椂锛屽彲浠ュ彇娑堣闃咃細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEvents;

public class UnsubscribeExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 鍒涘缓鐩戝惉鍣?
        EventListener<String> listener1 = (event, data) -> {
            System.out.println("鐩戝惉鍣?1: " + data);
        };
        
        EventListener<String> listener2 = (event, data) -> {
            System.out.println("鐩戝惉鍣?2: " + data);
        };
        
        // 璁㈤槄
        eventBus.subscribe("test_event", listener1);
        eventBus.subscribe("test_event", listener2);
        
        // 绗竴娆″彂甯?
        System.out.println("=== 绗竴娆″彂甯?===");
        eventBus.publish("test_event", "娑堟伅 A");
        
        // 鍙栨秷 listener1 鐨勮闃?
        eventBus.unsubscribe("test_event", listener1);
        
        // 绗簩娆″彂甯?
        System.out.println("\n=== 绗簩娆″彂甯冿紙listener1 宸插彇娑堬級 ===");
        eventBus.publish("test_event", "娑堟伅 B");
        
        // 鍙栨秷璇ヤ簨浠剁被鍨嬬殑鎵€鏈夌洃鍚櫒
        eventBus.unsubscribeAll("test_event");
        
        // 绗笁娆″彂甯?
        System.out.println("\n=== 绗笁娆″彂甯冿紙鎵€鏈夌洃鍚櫒宸插彇娑堬級 ===");
        eventBus.publish("test_event", "娑堟伅 C");
        
        // 妫€鏌ユ槸鍚︽湁鐩戝惉鍣?
        System.out.println("\n鏄惁鏈夌洃鍚櫒: " + eventBus.hasSubscribers("test_event"));
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 浜嬩欢缁熻

EventBus 鎻愪緵浜嗗己澶х殑缁熻鍔熻兘锛屽彲浠ュ府鍔╀綘鐩戞帶浜嬩欢鐨勫鐞嗘儏鍐碉細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventStats;
import ltd.idcu.est.features.event.local.LocalEvents;

public class EventStatsExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 璁㈤槄涓€浜涗簨浠?
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_a", (event, data) -> {});
        eventBus.subscribe("event_b", (event, data) -> {});
        
        // 鍙戝竷涓€浜涗簨浠?
        eventBus.publish("event_a", "data1");
        eventBus.publish("event_a", "data2");
        eventBus.publish("event_b", "data3");
        
        // 鑾峰彇缁熻淇℃伅
        EventStats stats = eventBus.getStats();
        
        System.out.println("=== 浜嬩欢缁熻淇℃伅 ===");
        System.out.println("宸插彂甯冧簨浠舵€绘暟: " + stats.getPublishedCount());
        System.out.println("澶勭悊鎴愬姛浜嬩欢鏁? " + stats.getSuccessCount());
        System.out.println("澶勭悊澶辫触浜嬩欢鏁? " + stats.getFailureCount());
        System.out.println("鎬荤洃鍚櫒鏁? " + eventBus.getTotalSubscriberCount());
        System.out.println("event_a 鐨勭洃鍚櫒鏁? " + eventBus.getSubscriberCount("event_a"));
    }
}
```

### 2. 浜嬩欢閰嶇疆

浣犲彲浠ラ€氳繃 EventConfig 鏉ラ厤缃簨浠舵€荤嚎鐨勮涓猴細

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.concurrent.Executors;

public class EventConfigExample {
    public static void main(String[] args) {
        // 鍒涘缓鑷畾涔夐厤缃?
        EventConfig config = new EventConfig()
                // 绾跨▼姹犲ぇ灏?
                .setThreadPoolSize(4)
                // 鏄惁浣跨敤铏氭嫙绾跨▼锛圝ava 21+锛?
                .setUseVirtualThreads(true)
                // 姣忎釜浜嬩欢鏈€澶氱殑鐩戝惉鍣ㄦ暟閲?
                .setMaxListenersPerEvent(500)
                // 鏄惁浼犳挱寮傚父锛坱rue 琛ㄧず鐩戝惉鍣ㄥ紓甯镐細褰卞搷鍚庣画鐩戝惉鍣級
                .setPropagateExceptions(false)
                // 鐩戝惉鍣ㄨ秴鏃舵椂闂达紙姣锛?
                .setListenerTimeout(10000)
                // 鑷畾涔夌嚎绋嬫睜
                .setExecutorService(Executors.newCachedThreadPool());
        
        // 浣跨敤閰嶇疆鍒涘缓浜嬩欢鎬荤嚎
        EventBus eventBus = LocalEvents.newLocalEventBus(config);
        
        System.out.println("鉁?鑷畾涔夐厤缃殑浜嬩欢鎬荤嚎鍒涘缓鎴愬姛锛?);
        System.out.println("閰嶇疆: " + config);
    }
}
```

### 3. 寮傚父澶勭悊

浣犲彲浠ラ厤缃浣曞鐞嗙洃鍚櫒涓殑寮傚父锛?

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.local.LocalEvents;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        // 閰嶇疆 1锛氫笉浼犳挱寮傚父锛堥粯璁わ級- 涓€涓洃鍚櫒澶辫触涓嶅奖鍝嶅叾浠栫洃鍚櫒
        System.out.println("=== 娴嬭瘯 1锛氫笉浼犳挱寮傚父 ===");
        EventConfig config1 = new EventConfig().setPropagateExceptions(false);
        EventBus bus1 = LocalEvents.newLocalEventBus(config1);
        
        bus1.subscribe("test", (event, data) -> {
            System.out.println("鐩戝惉鍣?1 鎵ц");
            throw new RuntimeException("鐩戝惉鍣?1 鍑洪敊浜嗭紒");
        });
        
        bus1.subscribe("test", (event, data) -> {
            System.out.println("鐩戝惉鍣?2 鎵ц锛堜粛鐒朵細鎵ц锛?);
        });
        
        bus1.publish("test", "鏁版嵁");
        
        // 閰嶇疆 2锛氫紶鎾紓甯?- 涓€涓洃鍚櫒澶辫触浼氶樆姝㈠悗缁洃鍚櫒
        System.out.println("\n=== 娴嬭瘯 2锛氫紶鎾紓甯?===");
        EventConfig config2 = new EventConfig().setPropagateExceptions(true);
        EventBus bus2 = LocalEvents.newLocalEventBus(config2);
        
        bus2.subscribe("test", (event, data) -> {
            System.out.println("鐩戝惉鍣?A 鎵ц");
            throw new RuntimeException("鐩戝惉鍣?A 鍑洪敊浜嗭紒");
        });
        
        bus2.subscribe("test", (event, data) -> {
            System.out.println("鐩戝惉鍣?B 鎵ц锛堜笉浼氭墽琛岋級");
        });
        
        try {
            bus2.publish("test", "鏁版嵁");
        } catch (Exception e) {
            System.out.println("鎹曡幏鍒板紓甯? " + e.getMessage());
        }
    }
}
```

### 4. 鑷畾涔変簨浠?

浣犲彲浠ュ垱寤鸿嚜瀹氫箟鐨勪簨浠剁被鏉ュ皝瑁呮洿澶嶆潅鐨勪簨浠朵俊鎭細

```java
import ltd.idcu.est.features.event.api.Event;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.time.LocalDateTime;

// 鑷畾涔変簨浠舵暟鎹被
class OrderEvent {
    private String orderId;
    private String productName;
    private double amount;
    private LocalDateTime timestamp;
    
    public OrderEvent(String orderId, String productName, double amount) {
        this.orderId = orderId;
        this.productName = productName;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public String getProductName() { return productName; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return "OrderEvent{" +
                "orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}

public class CustomEventExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 璁㈤槄鑷畾涔変簨浠?
        eventBus.subscribe("order_created", (event, orderData) -> {
            System.out.println("鏀跺埌璁㈠崟鍒涘缓浜嬩欢:");
            System.out.println("  璁㈠崟鍙? " + orderData.getOrderId());
            System.out.println("  鍟嗗搧: " + orderData.getProductName());
            System.out.println("  閲戦: " + orderData.getAmount());
            System.out.println("  鏃堕棿: " + orderData.getTimestamp());
        });
        
        // 鍙戝竷鑷畾涔変簨浠?
        OrderEvent order = new OrderEvent("ORD-001", "鏅鸿兘鎵嬫満", 2999.0);
        eventBus.publish("order_created", order);
    }
}
```

### 5. 涓?EST Collection 闆嗘垚

浜嬩欢鎬荤嚎鍙互鍜?EST Collection 瀹岀編缁撳悎锛屽鐞嗘壒閲忎簨浠讹細

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.List;

public class EventCollectionIntegrationExample {
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 璁㈤槄浜嬩欢
        eventBus.subscribe("user_notification", (event, user) -> {
            System.out.println("鍙戦€侀€氱煡缁? " + user);
        });
        
        // 浣跨敤 Collection 鎵归噺鍙戝竷浜嬩欢
        List<String> users = List.of("寮犱笁", "鏉庡洓", "鐜嬩簲", "璧靛叚");
        
        System.out.println("=== 鎵归噺鍙戦€侀€氱煡 ===");
        Seqs.of(users)
                .filter(user -> !user.equals("鐜嬩簲"))  // 杩囨护鎺夌帇浜?
                .forEach(user -> eventBus.publish("user_notification", user));
        
        System.out.println("\n鉁?鎵归噺閫氱煡鍙戦€佸畬鎴愶紒");
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 浜嬩欢鍛藉悕瑙勮寖

濂界殑浜嬩欢鍛藉悕鑳借浠ｇ爜鏇存槗璇伙細

```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄨ繃鍘诲紡锛岃〃绀烘煇浜嬪凡缁忓彂鐢?
"order_placed"       // 璁㈠崟宸插垱寤?
"user_registered"    // 鐢ㄦ埛宸叉敞鍐?
"payment_completed"  // 鏀粯宸插畬鎴?
"email_sent"         // 閭欢宸插彂閫?

// 鉂?涓嶆帹鑽愶細浣跨敤鍛戒护寮?
"place_order"        // 瀹规槗娣锋穯鏄懡浠よ繕鏄簨浠?
"register_user"      // 鍚屼笂
```

### 2. 浜嬩欢鏁版嵁璁捐

浜嬩欢鏁版嵁搴旇鍖呭惈瓒冲鐨勪俊鎭紝浣嗕笉瑕佽繃搴︼細

```java
// 鉁?濂界殑璁捐锛氬寘鍚繀瑕佺殑淇℃伅
class OrderPlacedEvent {
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    // ...
}

// 鉂?涓嶅ソ鐨勮璁★細淇℃伅澶皯
class OrderPlacedEvent {
    private String orderId;  // 鍙湁璁㈠崟 ID锛岀洃鍚櫒杩樺緱鍘绘煡璇?
}

// 鉂?涓嶅ソ鐨勮璁★細淇℃伅澶
class OrderPlacedEvent {
    private String orderId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerAddress;
    // ... 澶浜嗭紒
}
```

### 3. 鐩戝惉鍣ㄨ璁″師鍒?

```java
// 鉁?濂界殑鐩戝惉鍣細鑱岃矗鍗曚竴
eventBus.subscribe("order_placed", (event, order) -> {
    // 鍙礋璐ｅ彂閫佺‘璁ら偖浠?
    emailService.sendOrderConfirmation(order.getCustomerEmail(), order);
});

eventBus.subscribe("order_placed", (event, order) -> {
    // 鍙礋璐ｆ洿鏂板簱瀛?
    inventoryService.updateStock(order.getItems());
});

// 鉂?涓嶅ソ鐨勭洃鍚櫒锛氳亴璐ｅお澶?
eventBus.subscribe("order_placed", (event, order) -> {
    // 涓€涓洃鍚櫒鍋氫簡澶浜?
    emailService.sendOrderConfirmation(...);
    inventoryService.updateStock(...);
    accountingService.recordTransaction(...);
    notificationService.sendPushNotification(...);
});
```

### 4. 閿欒澶勭悊绛栫暐

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandlingBestPractice {
    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingBestPractice.class);
    
    public static void main(String[] args) {
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 鉁?鍦ㄧ洃鍚櫒鍐呴儴澶勭悊寮傚父
        eventBus.subscribe("order_placed", (event, order) -> {
            try {
                // 灏濊瘯鍙戦€侀偖浠?
                emailService.sendOrderConfirmation(order);
            } catch (Exception e) {
                // 璁板綍鏃ュ織锛屼絾涓嶈褰卞搷鍏朵粬鐩戝惉鍣?
                logger.error("鍙戦€佽鍗曠‘璁ら偖浠跺け璐?, e);
                // 鍙互鑰冭檻閲嶈瘯鎴栭檷绾у鐞?
                retryService.scheduleRetry(() -> emailService.sendOrderConfirmation(order));
            }
        });
    }
    
    // 妯℃嫙鐨勬湇鍔?
    private static class emailService {
        static void sendOrderConfirmation(Object order) {
            // 鍙戦€侀偖浠?
        }
    }
    
    private static class retryService {
        static void scheduleRetry(Runnable task) {
            // 璋冨害閲嶈瘯
        }
    }
}
```

### 5. 鎬ц兘浼樺寲寤鸿

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventConfig;
import ltd.idcu.est.features.event.async.AsyncEvents;

public class PerformanceOptimization {
    public static void main(String[] args) {
        // 鉁?瀵逛簬鑰楁椂鎿嶄綔锛屼娇鐢ㄥ紓姝ヤ簨浠舵€荤嚎
        EventBus asyncBus = AsyncEvents.newAsyncEventBus();
        
        asyncBus.subscribe("report_generated", (event, report) -> {
            // 鑰楁椂鐨?PDF 鐢熸垚鎿嶄綔
            generatePdfReport(report);
        });
        
        // 寮傛鍙戝竷锛屼笉闃诲涓荤嚎绋?
        asyncBus.publishAsync("report_generated", salesReport);
        
        // 鉁?鍚堢悊閰嶇疆绾跨▼姹?
        EventConfig config = new EventConfig()
                .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 2)
                .setUseVirtualThreads(true);  // Java 21+ 浣跨敤铏氭嫙绾跨▼
    }
    
    private static void generatePdfReport(Object report) {
        // 鐢熸垚 PDF
    }
    
    private static Object salesReport = new Object();
}
```

### 6. 瀹屾暣绀轰緥锛氱數鍟嗚鍗曠郴缁?

璁╂垜浠敤浜嬩欢鎬荤嚎鏋勫缓涓€涓畝鍖栫殑鐢靛晢璁㈠崟绯荤粺锛?

```java
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.local.LocalEvents;

import java.util.ArrayList;
import java.util.List;

// 璁㈠崟绫?
class Order {
    private String id;
    private String customerId;
    private List<String> items;
    private double totalAmount;
    
    public Order(String id, String customerId, List<String> items, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
    
    // Getters
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public List<String> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
}

// 璁㈠崟鏈嶅姟
class OrderService {
    private EventBus eventBus;
    
    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }
    
    public void createOrder(Order order) {
        System.out.println("馃摝 鍒涘缓璁㈠崟: " + order.getId());
        
        // 鍙戝竷璁㈠崟鍒涘缓浜嬩欢
        eventBus.publish("order_created", order);
    }
}

// 閭欢鏈嶅姟
class EmailService {
    public EmailService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("馃摟 鍙戦€佽鍗曠‘璁ら偖浠剁粰瀹㈡埛: " + order.getCustomerId());
        });
    }
}

// 搴撳瓨鏈嶅姟
class InventoryService {
    public InventoryService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("馃摝 鏇存柊搴撳瓨: " + order.getItems());
        });
    }
}

// 浼氳鏈嶅姟
class AccountingService {
    public AccountingService(EventBus eventBus) {
        eventBus.subscribe("order_created", (event, order) -> {
            System.out.println("馃挵 璁板綍浜ゆ槗: 閲戦 " + order.getTotalAmount());
        });
    }
}

// 涓荤▼搴?
public class ECommerceExample {
    public static void main(String[] args) {
        // 鍒涘缓浜嬩欢鎬荤嚎
        EventBus eventBus = LocalEvents.newLocalEventBus();
        
        // 鍒濆鍖栧悇涓湇鍔?
        OrderService orderService = new OrderService(eventBus);
        new EmailService(eventBus);
        new InventoryService(eventBus);
        new AccountingService(eventBus);
        
        // 鍒涘缓璁㈠崟
        System.out.println("=== 鐢靛晢璁㈠崟绯荤粺绀轰緥 ===\n");
        
        List<String> items = new ArrayList<>();
        items.add("绗旇鏈數鑴?);
        items.add("鏃犵嚎榧犳爣");
        
        Order order = new Order("ORD-2024-001", "CUST-001", items, 5999.0);
        orderService.createOrder(order);
        
        System.out.println("\n鉁?璁㈠崟澶勭悊瀹屾垚锛?);
    }
}
```

杩愯缁撴灉锛?
```
=== 鐢靛晢璁㈠崟绯荤粺绀轰緥 ===

馃摝 鍒涘缓璁㈠崟: ORD-2024-001
馃摟 鍙戦€佽鍗曠‘璁ら偖浠剁粰瀹㈡埛: CUST-001
馃摝 鏇存柊搴撳瓨: [绗旇鏈數鑴? 鏃犵嚎榧犳爣]
馃挵 璁板綍浜ゆ槗: 閲戦 5999.0

鉁?璁㈠崟澶勭悊瀹屾垚锛?
```

---

## 馃幆 鎬荤粨

鎭枩浣狅紒浣犲凡缁忓畬鏁村涔犱簡 EST Event 浜嬩欢鎬荤嚎绯荤粺锛?

璁╂垜浠洖椤句竴涓嬮噸鐐癸細

1. **鏍稿績姒傚康**锛氫簨浠剁被鍨嬨€佷簨浠舵暟鎹€佺洃鍚櫒銆佷簨浠舵€荤嚎
2. **鍩烘湰鎿嶄綔**锛氬垱寤轰簨浠舵€荤嚎銆佽闃呬簨浠躲€佸彂甯冧簨浠躲€佸彇娑堣闃?
3. **楂樼骇鍔熻兘**锛氫簨浠剁粺璁°€佽嚜瀹氫箟閰嶇疆銆佸紓甯稿鐞嗐€佸紓姝ュ鐞?
4. **鏈€浣冲疄璺?*锛氫簨浠跺懡鍚嶃€佹暟鎹璁°€佺洃鍚櫒鑱岃矗銆侀敊璇鐞?

浜嬩欢鎬荤嚎鏄В鑰︾郴缁熺粍浠剁殑寮哄ぇ宸ュ叿锛屽ソ濂藉埄鐢ㄥ畠锛屼綘鐨勪唬鐮佷細鏇村姞鐏垫椿鍜屽彲缁存姢锛?

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST Logging 鏃ュ織绯荤粺锛屼笉瑙佷笉鏁ｏ紒馃帀
