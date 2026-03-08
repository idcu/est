# EST Integration Group 集成模块组 - 小白从入门到精通

## 目录
1. [什么是 EST Integration Group？](#什么是-est-integration-group)
2. [快速入门：5分钟上手](#快速入门5分钟上手)
3. [基础篇](#基础篇)
4. [进阶篇](#进阶篇)
5. [最佳实践](#最佳实践)

---

## 什么是 EST Integration Group？

### 用大白话理解

EST Integration Group 就像是一个"连接器工具箱"。想象一下你的应用需要和外部系统通信：

**传统方式**：每个消息队列都要写不同的代码，邮件、短信、对象存储都要自己实现... 很麻烦！

**EST Integration Group 方式**：给你一套统一的集成工具，里面有：
- 📨 **消息抽象** - 支持 Kafka、RabbitMQ、Redis、MQTT 等
- 📧 **第三方集成** - 邮件、短信、OSS 对象存储

### 核心特点

- 🎯 **简单易用** - 统一的 API，不用关心底层实现
- ⚡ **高性能** - 优化的消息处理和集成实现
- 🔧 **灵活扩展** - 可以自定义消息和集成实现
- 🎨 **功能完整** - 消息队列、邮件、短信、OSS 一应俱全

---

## 快速入门：5分钟上手

### 第一步：引入依赖

在你的 Maven pom.xml 中添加：

```xml
<dependencies>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-messaging</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>ltd.idcu</groupId>
        <artifactId>est-integration</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

### 第二步：你的第一个集成应用

```java
import ltd.idcu.est.messaging.MessageProducer;
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.memory.MemoryMessaging;
import ltd.idcu.est.integration.email.EmailSender;
import ltd.idcu.est.integration.email.Email;

public class FirstIntegrationApp {
    public static void main(String[] args) {
        System.out.println("=== EST Integration Group 第一个示例 ===\n");
        
        MessageProducer producer = MemoryMessaging.createProducer();
        MessageConsumer consumer = MemoryMessaging.createConsumer();
        
        consumer.subscribe("my-topic", message -> {
            System.out.println("收到消息: " + message.getBody());
        });
        
        producer.send("my-topic", Message.of("Hello, Messaging!"));
        System.out.println("消息已发送");
        
        EmailSender emailSender = EmailSender.create();
        Email email = Email.builder()
            .to("user@example.com")
            .subject("测试邮件")
            .body("这是一封测试邮件")
            .build();
        emailSender.send(email);
        System.out.println("邮件已发送");
    }
}
```

---

## 基础篇

### 1. est-messaging 消息抽象

详细文档请参考：[est-messaging README](./est-messaging/README.md)

#### 发送消息

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

#### 消费消息

```java
import ltd.idcu.est.messaging.MessageConsumer;
import ltd.idcu.est.messaging.Message;

MessageConsumer consumer = KafkaMessaging.createConsumer("localhost:9092", "my-group");

consumer.subscribe("my-topic", message -> {
    System.out.println("收到消息: " + message.getBody());
    return MessageAck.ACK;
});
```

#### 多种消息系统

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

### 2. est-integration 第三方集成

#### 发送邮件

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
    .subject("测试邮件")
    .body("<h1>Hello!</h1><p>这是一封 HTML 邮件</p>")
    .html(true)
    .attachment(EmailAttachment.of("file.pdf", Paths.get("file.pdf")))
    .build();

emailSender.send(email);
```

#### 发送短信

```java
import ltd.idcu.est.integration.sms.SmsSender;
import ltd.idcu.est.integration.sms.Sms;
import ltd.idcu.est.integration.sms.aliyun.AliyunSmsSender;
import ltd.idcu.est.integration.sms.tencent.TencentSmsSender;

SmsSender smsSender = AliyunSmsSender.builder()
    .accessKeyId("your-access-key-id")
    .accessKeySecret("your-access-key-secret")
    .signName("我的应用")
    .templateCode("SMS_123456789")
    .build();

Sms sms = Sms.builder()
    .phoneNumber("13800138000")
    .templateParam(Map.of("code", "123456"))
    .build();

smsSender.send(sms);
```

#### 对象存储 OSS

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
System.out.println("文件内容: " + object.getContentAsString());

ossClient.delete("files/test.txt");
```

---

## 进阶篇

### 1. 自定义消息序列化

```java
import ltd.idcu.est.messaging.Message;
import ltd.idcu.est.messaging.serializer.MessageSerializer;
import ltd.idcu.est.messaging.serializer.JsonMessageSerializer;

public class CustomMessageSerializer implements MessageSerializer {
    
    @Override
    public byte[] serialize(Message message) {
        // 自定义序列化逻辑
        return JsonMessageSerializer.INSTANCE.serialize(message);
    }
    
    @Override
    public Message deserialize(byte[] data) {
        // 自定义反序列化逻辑
        return JsonMessageSerializer.INSTANCE.deserialize(data);
    }
}

MessageProducer producer = KafkaMessaging.createProducer("localhost:9092");
producer.setSerializer(new CustomMessageSerializer());
```

### 2. 邮件模板

```java
import ltd.idcu.est.integration.email.EmailSender;
import ltd.idcu.est.integration.email.Email;
import ltd.idcu.est.integration.email.EmailTemplate;
import ltd.idcu.est.integration.email.EmailTemplateEngine;

EmailTemplateEngine templateEngine = EmailTemplateEngine.create();

EmailTemplate template = templateEngine.load("welcome-email.html");

Email email = Email.builder()
    .to("user@example.com")
    .subject("欢迎加入")
    .body(template.render(Map.of(
        "name", "张三",
        "date", LocalDate.now().toString()
    )))
    .html(true)
    .build();

emailSender.send(email);
```

### 3. 消息重试和死信队列

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

## 最佳实践

### 1. 消息设计

```java
// ✅ 推荐：消息体简洁，包含必要信息
Message message = Message.builder()
    .body(Map.of(
        "orderId", "ORDER001",
        "amount", 99.99
    ))
    .header("type", "order-created")
    .build();

// ❌ 不推荐：消息体过大
Message badMessage = Message.builder()
    .body(largeObject) // 太大的对象
    .build();
```

### 2. 邮件发送

```java
// ✅ 推荐：使用异步发送
CompletableFuture.runAsync(() -> {
    emailSender.send(email);
});

// ❌ 不推荐：同步发送阻塞主线程
emailSender.send(email); // 可能阻塞
```

### 3. 错误处理

```java
// ✅ 推荐：捕获异常，记录日志
try {
    smsSender.send(sms);
} catch (Exception e) {
    logger.error("短信发送失败", e);
    fallbackSmsSender.send(sms);
}

// ❌ 不推荐：不处理异常
smsSender.send(sms); // 异常可能导致程序崩溃
```

---

## 模块结构

```
est-integration-group/
├── est-messaging/    # 消息抽象（Kafka、RabbitMQ、Redis、MQTT）
└── est-integration/  # 第三方集成（Email、SMS、OSS）
```

---

## 相关资源

- [est-messaging README](./est-messaging/README.md) - 消息详细文档
- [示例代码](../../est-examples/est-examples-advanced/) - 高级示例
- [EST Foundation](../est-foundation/README.md) - 基础设施模块
- [EST Core](../../est-core/README.md) - 核心模块

---

**文档版本**: 2.0  
**最后更新**: 2026-03-08
