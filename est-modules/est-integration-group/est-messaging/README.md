# EST Messaging - 娑堟伅绯荤粺

## 馃摎 鐩綍

- [蹇€熷叆闂╙(#蹇€熷叆闂?
- [鍩虹绡嘳(#鍩虹绡?
- [杩涢樁绡嘳(#杩涢樁绡?
- [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 馃殌 蹇€熷叆闂?

### 浠€涔堟槸娑堟伅绯荤粺锛?

鎯宠薄涓€涓嬶紝浣犲湪涓€涓ぇ鍨嬪叕鍙稿伐浣滐細
- 閿€鍞儴鏈夋柊璁㈠崟锛岄渶瑕侀€氱煡浠撳簱鍙戣揣
- 瀹㈡湇閮ㄦ湁瀹㈡埛鍙嶉锛岄渶瑕侀€氱煡鎶€鏈儴澶勭悊
- 涓嶅悓閮ㄩ棬涔嬮棿闇€瑕侀珮鏁堟矡閫?

**娑堟伅绯荤粺**灏卞儚鍏徃鐨?閭斂绯荤粺"锛屽畠鎻愪緵锛?
- 娑堟伅闃熷垪锛圦ueue锛夛細涓€瀵逛竴閫氫俊锛屽儚瀵勪俊
- 娑堟伅涓婚锛圱opic锛夛細涓€瀵瑰閫氫俊锛屽儚骞挎挱
- 澶氱娑堟伅涓棿浠舵敮鎸侊紙Kafka銆丷edis銆丄MQP銆丄ctiveMQ 绛夛級

璁╃郴缁熶箣闂撮珮鏁堥€氫俊锛?

### 绗竴涓緥瀛?

璁╂垜浠敤 3 鍒嗛挓鍐欎竴涓畝鍗曠殑娑堟伅绋嬪簭锛?

棣栧厛锛屽湪浣犵殑 `pom.xml` 涓坊鍔犱緷璧栵細

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-messaging-api</artifactId>
    <version>2.1.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-messaging-local</artifactId>
    <version>2.1.0</version>
</dependency>
```

鐒跺悗鍒涘缓涓€涓畝鍗曠殑 Java 绫伙細

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

public class MessagingFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 娑堟伅绯荤粺绀轰緥 ===\n");
        
        // 鍒涘缓鏈湴娑堟伅闃熷垪
        LocalMessageQueue queue = LocalMessages.createQueue("order-queue");
        
        // 鍒涘缓娑堣垂鑰?
        MessageConsumer consumer = queue.createConsumer();
        consumer.subscribe(message -> {
            System.out.println("鏀跺埌娑堟伅: " + message.getBody());
        });
        
        // 鍒涘缓鐢熶骇鑰呭苟鍙戦€佹秷鎭?
        MessageProducer producer = queue.createProducer();
        producer.send("鏂拌鍗? 璁㈠崟鍙?1001");
        producer.send("鏂拌鍗? 璁㈠崟鍙?1002");
        
        System.out.println("\n鉁?娑堟伅绀轰緥瀹屾垚锛?);
    }
}
```

杩愯杩欎釜绋嬪簭锛屼綘浼氱湅鍒版秷璐硅€呮敹鍒版秷鎭紒

馃帀 鎭枩浣狅紒浣犲凡缁忓浼氫簡浣跨敤娑堟伅绯荤粺锛?

---

## 馃摉 鍩虹绡?

### 1. 鏍稿績姒傚康

| 姒傚康 | 璇存槑 | 鐢熸椿绫绘瘮 |
|------|------|----------|
| **娑堟伅锛圡essage锛?* | 闇€瑕佷紶閫掔殑鏁版嵁 | 淇′欢 |
| **闃熷垪锛圦ueue锛?* | 涓€瀵逛竴娑堟伅閫氶亾 | 鐐瑰鐐瑰揩閫?|
| **涓婚锛圱opic锛?* | 涓€瀵瑰娑堟伅閫氶亾 | 骞挎挱鐢靛彴 |
| **鐢熶骇鑰咃紙Producer锛?* | 鍙戦€佹秷鎭殑浜?| 瀵勪俊浜?|
| **娑堣垂鑰咃紙Consumer锛?* | 鎺ユ敹娑堟伅鐨勪汉 | 鏀朵俊浜?|

### 2. 鏈湴娑堟伅闃熷垪

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

public class LocalQueueExample {
    public static void main(String[] args) {
        // 鍒涘缓闃熷垪
        LocalMessageQueue queue = LocalMessages.createQueue("task-queue");
        
        // 娑堣垂鑰?
        MessageConsumer consumer = queue.createConsumer();
        consumer.subscribe(msg -> {
            System.out.println("澶勭悊浠诲姟: " + msg.getBody());
        });
        
        // 鐢熶骇鑰呭彂閫佹秷鎭?
        MessageProducer producer = queue.createProducer();
        producer.send("澶囦唤鏁版嵁搴?);
        producer.send("娓呯悊鏃ュ織鏂囦欢");
        producer.send("鍙戦€佹棩鎶?);
    }
}
```

### 3. 鏈湴娑堟伅涓婚

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageTopic;

public class LocalTopicExample {
    public static void main(String[] args) {
        // 鍒涘缓涓婚
        LocalMessageTopic topic = LocalMessages.createTopic("news-topic");
        
        // 娑堣垂鑰?1
        MessageConsumer consumer1 = topic.createConsumer();
        consumer1.subscribe(msg -> {
            System.out.println("[璁㈤槄鑰?1] 鏀跺埌鏂伴椈: " + msg.getBody());
        });
        
        // 娑堣垂鑰?2
        MessageConsumer consumer2 = topic.createConsumer();
        consumer2.subscribe(msg -> {
            System.out.println("[璁㈤槄鑰?2] 鏀跺埌鏂伴椈: " + msg.getBody());
        });
        
        // 鐢熶骇鑰呭彂甯冩秷鎭紙鎵€鏈夎闃呰€呴兘鑳芥敹鍒帮級
        MessageProducer producer = topic.createProducer();
        producer.send("閲嶅ぇ鏂伴椈: EST 2.0 鍙戝竷浜嗭紒");
    }
}
```

---

## 馃敡 杩涢樁绡?

### 1. 澶氱娑堟伅涓棿浠舵敮鎸?

EST Messaging 鏀寔澶氱娑堟伅涓棿浠讹細

```java
// Kafka
import ltd.idcu.est.features.messaging.kafka.KafkaMessages;

// Redis
import ltd.idcu.est.features.messaging.redis.RedisMessages;

// AMQP (RabbitMQ)
import ltd.idcu.est.features.messaging.amqp.AmqpMessages;

// ActiveMQ
import ltd.idcu.est.features.messaging.activemq.ActiveMqMessages;

// WebSocket
import ltd.idcu.est.features.messaging.websocket.WebSocketMessages;

// RocketMQ
import ltd.idcu.est.features.messaging.rocketmq.RocketMqMessages;

// MQTT
import ltd.idcu.est.features.messaging.mqtt.MqttMessages;

// NATS
import ltd.idcu.est.features.messaging.nats.NatsMessages;

// Pulsar
import ltd.idcu.est.features.messaging.pulsar.PulsarMessages;

// ZeroMQ
import ltd.idcu.est.features.messaging.zeromq.ZeroMqMessages;
```

### 2. 涓?EST Collection 闆嗘垚

```java
import ltd.idcu.est.collection.api.Seqs;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

import java.util.List;

public class MessagingCollectionIntegrationExample {
    public static void main(String[] args) {
        LocalMessageQueue queue = LocalMessages.createQueue("batch-queue");
        MessageProducer producer = queue.createProducer();
        
        List<String> messages = List.of("娑堟伅 1", "娑堟伅 2", "娑堟伅 3", "娑堟伅 4", "娑堟伅 5");
        
        // 浣跨敤 Collection 鎵归噺鍙戦€佹秷鎭?
        Seqs.of(messages)
                .forEach(msg -> {
                    producer.send(msg);
                    System.out.println("宸插彂閫? " + msg);
                });
    }
}
```

---

## 馃挕 鏈€浣冲疄璺?

### 1. 娑堟伅纭鏈哄埗

```java
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

public class MessageAckExample {
    public static void main(String[] args) {
        LocalMessageQueue queue = LocalMessages.createQueue("reliable-queue");
        
        MessageConsumer consumer = queue.createConsumer();
        consumer.subscribe(msg -> {
            try {
                System.out.println("澶勭悊娑堟伅: " + msg.getBody());
                // 澶勭悊鎴愬姛锛岀‘璁ゆ秷鎭?
                // msg.ack();
            } catch (Exception e) {
                System.out.println("澶勭悊澶辫触: " + e.getMessage());
                // 澶勭悊澶辫触锛屾嫆缁濇秷鎭?
                // msg.nack();
            }
        });
    }
}
```

---

## 馃幆 鎬荤粨

娑堟伅绯荤粺灏卞儚绋嬪簭鐨?閭斂绯荤粺"锛岃绯荤粺涔嬮棿楂樻晥銆佸彲闈犲湴閫氫俊锛?

涓嬩竴绔狅紝鎴戜滑灏嗗涔?EST AI 浜哄伐鏅鸿兘鍔╂墜锛侌煄?
