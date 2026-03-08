# EST Integration Group 闆嗘垚妯″潡缁?- 灏忕櫧浠庡叆闂ㄥ埌绮鹃€?
## 鐩綍
1. [浠€涔堟槸 EST Integration Group锛焆(#浠€涔堟槸-est-integration-group)
2. [蹇€熷叆闂細5鍒嗛挓涓婃墜](#蹇€熷叆闂?鍒嗛挓涓婃墜)
3. [鍩虹绡嘳(#鍩虹绡?
4. [杩涢樁绡嘳(#杩涢樁绡?
5. [鏈€浣冲疄璺礭(#鏈€浣冲疄璺?

---

## 浠€涔堟槸 EST Integration Group锛?
### 鐢ㄥぇ鐧借瘽鐞嗚В

EST Integration Group 灏卞儚鏄竴涓?杩炴帴鍣ㄥ伐鍏风"銆傛兂璞′竴涓嬩綘鐨勫簲鐢ㄩ渶瑕佸拰澶栭儴绯荤粺閫氫俊锛?
**浼犵粺鏂瑰紡**锛氭瘡涓秷鎭槦鍒楅兘瑕佸啓涓嶅悓鐨勪唬鐮侊紝閭欢銆佺煭淇°€佸璞″瓨鍌ㄩ兘瑕佽嚜宸卞疄鐜?.. 寰堥夯鐑︼紒

**EST Integration Group 鏂瑰紡**锛氱粰浣犱竴濂楃粺涓€鐨勯泦鎴愬伐鍏凤紝閲岄潰鏈夛細
- 馃摠 **娑堟伅鎶借薄** - 鏀寔 Kafka銆丷abbitMQ銆丷edis銆丮QTT 绛?- 馃摟 **绗笁鏂归泦鎴?* - 閭欢銆佺煭淇°€丱SS 瀵硅薄瀛樺偍

### 鏍稿績鐗圭偣

- 馃幆 **绠€鍗曟槗鐢?* - 缁熶竴鐨?API锛屼笉鐢ㄥ叧蹇冨簳灞傚疄鐜?- 鈿?**楂樻€ц兘** - 浼樺寲鐨勬秷鎭鐞嗗拰闆嗘垚瀹炵幇
- 馃敡 **鐏垫椿鎵╁睍** - 鍙互鑷畾涔夋秷鎭拰闆嗘垚瀹炵幇
- 馃帹 **鍔熻兘瀹屾暣** - 娑堟伅闃熷垪銆侀偖浠躲€佺煭淇°€丱SS 涓€搴斾勘鍏?
---

## 蹇€熷叆闂細5鍒嗛挓涓婃墜

### 绗竴姝ワ細寮曞叆渚濊禆

鍦ㄤ綘鐨?Maven pom.xml 涓坊鍔狅細

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-messaging</artifactId>
        <version>2.1.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-integration</artifactId>
        <version>2.1.0</version>
    </dependency>
</dependencies>
```

### 绗簩姝ワ細浣犵殑绗竴涓泦鎴愬簲鐢?
```java
import ltd.idcu.est.messaging.MessageProducer;
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.memory.MemoryMessaging;
import ltd.idcu.est.integration.email.EmailSender;
import ltd.idcu.est.integration.email.Email;

public class FirstIntegrationApp {
    public static void main(String[] args) {
        System.out.println("=== EST Integration Group 绗竴涓ず渚?===\n");
        
        MessageProducer producer = MemoryMessaging.createProducer();
        MessageConsumer consumer = MemoryMessaging.createConsumer();
        
        consumer.subscribe("my-topic", message -> {
            System.out.println("鏀跺埌娑堟伅: " + message.getBody());
        });
        
        producer.send("my-topic", Message.of("Hello, Messaging!"));
        System.out.println("娑堟伅宸插彂閫?);
        
        EmailSender emailSender = EmailSender.create();
        Email email = Email.builder()
            .to("user@example.com")
            .subject("娴嬭瘯閭欢")
            .body("杩欐槸涓€灏佹祴璇曢偖浠?)
            .build();
        emailSender.send(email);
        System.out.println("閭欢宸插彂閫?);
    }
}
```

---

## 鍩虹绡?
### 1. est-messaging 娑堟伅鎶借薄

璇︾粏鏂囨。璇峰弬鑰冿細[est-messaging README](./est-messaging/README.md)

#### 鍙戦€佹秷鎭?
```java
import ltd.idcu.est.messaging.MessageProducer;
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.kafka.KafkaMessaging;
import ltd.idcu.est.messaging.rabbitmq.RabbitMQMessaging;
import ltd.idcu.est.messaging.redis.RedisMessaging;

MessageProducer producer = KafkaMessaging.createProducer("localhost:9092");

Message message = Message.builder()
    .body("Hello, Kafka!")
    .header("key", "value")
    .build();

producer.send("my-topic", message);
```

#### 娑堣垂娑堟伅

```java
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.Message;

MessageConsumer consumer = KafkaMessaging.createConsumer("localhost:9092", "my-group");

consumer.subscribe("my-topic", message -> {
    System.out.println("鏀跺埌娑堟伅: " + message.getBody());
    return MessageAck.ACK;
});
```

#### 澶氱娑堟伅绯荤粺

```java
import ltd.idcu.est.messaging.MessageProducer;
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.kafka.KafkaMessaging;
import ltd.idcu.est.messaging.rabbitmq.RabbitMQMessaging;
import ltd.idcu.est.messaging.redis.RedisMessaging;
import ltd.idcu.est.messaging.mqtt.MqttMessaging;

MessageProducer kafkaProducer = KafkaMessaging.createProducer("localhost:9092");
MessageProducer rabbitProducer = RabbitMQMessaging.createProducer("localhost:5672");
MessageProducer redisProducer = RedisMessaging.createProducer("localhost:6379");
MessageProducer mqttProducer = MqttMessaging.createProducer("tcp://localhost:1883");
```

### 2. est-integration 绗笁鏂归泦鎴?
#### 鍙戦€侀偖浠?
```java
import ltd.idcu.est.integration.email.EmailSender;
import ltd.idcu.est.integration.email.Email;
import ltd.idcu.est.integration.email.EmailAttachment;
import ltd.idcu.est.integration.email.smtp.SmtpEmailSender;

EmailSender emailSender = SmtpEmailSender.builder()
    .host("smtp.example.com")
    .port(587)
    .username("user@example.com")
    .password("password")
    .build();

Email email = Email.builder()
    .from("sender@example.com")
    .to("recipient@example.com")
    .cc("cc@example.com")
    .bcc("bcc@example.com")
    .subject("娴嬭瘯閭欢")
    .body("<h1>Hello!</h1><p>杩欐槸涓€灏?HTML 閭欢</p>")
    .html(true)
    .attachment(EmailAttachment.of("file.pdf", Paths.get("file.pdf")))
    .build();

emailSender.send(email);
```

#### 鍙戦€佺煭淇?
```java
import ltd.idcu.est.integration.sms.SmsSender;
import ltd.idcu.est.integration.sms.Sms;
import ltd.idcu.est.integration.sms.aliyun.AliyunSmsSender;
import ltd.idcu.est.integration.sms.tencent.TencentSmsSender;

SmsSender smsSender = AliyunSmsSender.builder()
    .accessKeyId("your-access-key-id")
    .accessKeySecret("your-access-key-secret")
    .signName("鎴戠殑搴旂敤")
    .templateCode("SMS_123456789")
    .build();

Sms sms = Sms.builder()
    .phoneNumber("13800138000")
    .templateParam(Map.of("code", "123456"))
    .build();

smsSender.send(sms);
```

#### 瀵硅薄瀛樺偍 OSS

```java
import ltd.idcu.est.integration.oss.OssClient;
import ltd.idcu.est.integration.oss.OssObject;
import ltd.idcu.est.integration.oss.aliyun.AliyunOssClient;
import ltd.idcu.est.integration.oss.tencent.TencentCosClient;
import ltd.idcu.est.integration.oss.minio.MinioOssClient;

OssClient ossClient = AliyunOssClient.builder()
    .endpoint("oss-cn-hangzhou.aliyuncs.com")
    .accessKeyId("your-access-key-id")
    .accessKeySecret("your-access-key-secret")
    .bucketName("my-bucket")
    .build();

ossClient.upload("files/test.txt", Paths.get("test.txt"));

OssObject object = ossClient.download("files/test.txt");
System.out.println("鏂囦欢鍐呭: " + object.getContentAsString());

ossClient.delete("files/test.txt");
```

---

## 杩涢樁绡?
### 1. 鑷畾涔夋秷鎭簭鍒楀寲

```java
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.serializer.MessageSerializer;
import ltd.idcu.est.messaging.serializer.JsonMessageSerializer;

public class CustomMessageSerializer implements MessageSerializer {
    
    @Override
    public byte[] serialize(Message message) {
        // 鑷畾涔夊簭鍒楀寲閫昏緫
        return JsonMessageSerializer.INSTANCE.serialize(message);
    }
    
    @Override
    public Message deserialize(byte[] data) {
        // 鑷畾涔夊弽搴忓垪鍖栭€昏緫
        return JsonMessageSerializer.INSTANCE.deserialize(data);
    }
}

MessageProducer producer = KafkaMessaging.createProducer("localhost:9092");
producer.setSerializer(new CustomMessageSerializer());
```

### 2. 閭欢妯℃澘

```java
import ltd.idcu.est.integration.email.EmailSender;
import ltd.idcu.est.integration.email.Email;
import ltd.idcu.est.integration.email.EmailTemplate;
import ltd.idcu.est.integration.email.EmailTemplateEngine;

EmailTemplateEngine templateEngine = EmailTemplateEngine.create();

EmailTemplate template = templateEngine.load("welcome-email.html");

Email email = Email.builder()
    .to("user@example.com")
    .subject("娆㈣繋鍔犲叆")
    .body(template.render(Map.of(
        "name", "寮犱笁",
        "date", LocalDate.now().toString()
    )))
    .html(true)
    .build();

emailSender.send(email);
```

### 3. 娑堟伅閲嶈瘯鍜屾淇￠槦鍒?
```java
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.MessageAck;
import ltd.idcu.est.messaging.retry.RetryPolicy;
import ltd.idcu.est.messaging.deadletter.DeadLetterQueue;

RetryPolicy retryPolicy = RetryPolicy.builder()
    .maxRetries(3)
    .backoff(1000)
    .build();

DeadLetterQueue dlq = DeadLetterQueue.create("my-dlq");

MessageConsumer consumer = KafkaMessaging.createConsumer("localhost:9092", "my-group");
consumer.setRetryPolicy(retryPolicy);
consumer.setDeadLetterQueue(dlq);

consumer.subscribe("my-topic", message -> {
    try {
        processMessage(message);
        return MessageAck.ACK;
    } catch (Exception e) {
        return MessageAck.NACK;
    }
});
```

---

## 鏈€浣冲疄璺?
### 1. 娑堟伅璁捐

```java
// 鉁?鎺ㄨ崘锛氭秷鎭綋绠€娲侊紝鍖呭惈蹇呰淇℃伅
Message message = Message.builder()
    .body(Map.of(
        "orderId", "ORDER001",
        "amount", 99.99
    ))
    .header("type", "order-created")
    .build();

// 鉂?涓嶆帹鑽愶細娑堟伅浣撹繃澶?Message badMessage = Message.builder()
    .body(largeObject) // 澶ぇ鐨勫璞?    .build();
```

### 2. 閭欢鍙戦€?
```java
// 鉁?鎺ㄨ崘锛氫娇鐢ㄥ紓姝ュ彂閫?CompletableFuture.runAsync(() -> {
    emailSender.send(email);
});

// 鉂?涓嶆帹鑽愶細鍚屾鍙戦€侀樆濉炰富绾跨▼
emailSender.send(email); // 鍙兘闃诲
```

### 3. 閿欒澶勭悊

```java
// 鉁?鎺ㄨ崘锛氭崟鑾峰紓甯革紝璁板綍鏃ュ織
try {
    smsSender.send(sms);
} catch (Exception e) {
    logger.error("鐭俊鍙戦€佸け璐?, e);
    fallbackSmsSender.send(sms);
}

// 鉂?涓嶆帹鑽愶細涓嶅鐞嗗紓甯?smsSender.send(sms); // 寮傚父鍙兘瀵艰嚧绋嬪簭宕╂簝
```

---

## 妯″潡缁撴瀯

```
est-integration-group/
鈹溾攢鈹€ est-messaging/    # 娑堟伅鎶借薄锛圞afka銆丷abbitMQ銆丷edis銆丮QTT锛?鈹斺攢鈹€ est-integration/  # 绗笁鏂归泦鎴愶紙Email銆丼MS銆丱SS锛?```

---

## 鐩稿叧璧勬簮

- [est-messaging README](./est-messaging/README.md) - 娑堟伅璇︾粏鏂囨。
- [绀轰緥浠ｇ爜](../../est-examples/est-examples-advanced/) - 楂樼骇绀轰緥
- [EST Foundation](../est-foundation/README.md) - 鍩虹璁炬柦妯″潡
- [EST Core](../../est-core/README.md) - 鏍稿績妯″潡

---

**鏂囨。鐗堟湰**: 2.0  
**鏈€鍚庢洿鏂?*: 2026-03-08
