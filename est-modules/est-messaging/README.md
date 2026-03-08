# EST Messaging - 消息系统

## 📚 目录

- [快速入门](#快速入门)
- [基础篇](#基础篇)
- [进阶篇](#进阶篇)
- [最佳实践](#最佳实践)

---

## 🚀 快速入门

### 什么是消息系统？

想象一下，你在一个大型公司工作：
- 销售部有新订单，需要通知仓库发货
- 客服部有客户反馈，需要通知技术部处理
- 不同部门之间需要高效沟通

**消息系统**就像公司的"邮政系统"，它提供：
- 消息队列（Queue）：一对一通信，像寄信
- 消息主题（Topic）：一对多通信，像广播
- 多种消息中间件支持（Kafka、Redis、AMQP、ActiveMQ 等）

让系统之间高效通信！

### 第一个例子

让我们用 3 分钟写一个简单的消息程序！

首先，在你的 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-messaging-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>ltd.idcu.est</groupId>
    <artifactId>est-features-messaging-local</artifactId>
    <version>2.0.0</version>
</dependency>
```

然后创建一个简单的 Java 类：

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

public class MessagingFirstExample {
    public static void main(String[] args) {
        System.out.println("=== 消息系统示例 ===\n");
        
        // 创建本地消息队列
        LocalMessageQueue queue = LocalMessages.createQueue("order-queue");
        
        // 创建消费者
        MessageConsumer consumer = queue.createConsumer();
        consumer.subscribe(message -> {
            System.out.println("收到消息: " + message.getBody());
        });
        
        // 创建生产者并发送消息
        MessageProducer producer = queue.createProducer();
        producer.send("新订单: 订单号 1001");
        producer.send("新订单: 订单号 1002");
        
        System.out.println("\n✅ 消息示例完成！");
    }
}
```

运行这个程序，你会看到消费者收到消息！

🎉 恭喜你！你已经学会了使用消息系统！

---

## 📖 基础篇

### 1. 核心概念

| 概念 | 说明 | 生活类比 |
|------|------|----------|
| **消息（Message）** | 需要传递的数据 | 信件 |
| **队列（Queue）** | 一对一消息通道 | 点对点快递 |
| **主题（Topic）** | 一对多消息通道 | 广播电台 |
| **生产者（Producer）** | 发送消息的人 | 寄信人 |
| **消费者（Consumer）** | 接收消息的人 | 收信人 |

### 2. 本地消息队列

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

public class LocalQueueExample {
    public static void main(String[] args) {
        // 创建队列
        LocalMessageQueue queue = LocalMessages.createQueue("task-queue");
        
        // 消费者
        MessageConsumer consumer = queue.createConsumer();
        consumer.subscribe(msg -> {
            System.out.println("处理任务: " + msg.getBody());
        });
        
        // 生产者发送消息
        MessageProducer producer = queue.createProducer();
        producer.send("备份数据库");
        producer.send("清理日志文件");
        producer.send("发送日报");
    }
}
```

### 3. 本地消息主题

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageTopic;

public class LocalTopicExample {
    public static void main(String[] args) {
        // 创建主题
        LocalMessageTopic topic = LocalMessages.createTopic("news-topic");
        
        // 消费者 1
        MessageConsumer consumer1 = topic.createConsumer();
        consumer1.subscribe(msg -> {
            System.out.println("[订阅者 1] 收到新闻: " + msg.getBody());
        });
        
        // 消费者 2
        MessageConsumer consumer2 = topic.createConsumer();
        consumer2.subscribe(msg -> {
            System.out.println("[订阅者 2] 收到新闻: " + msg.getBody());
        });
        
        // 生产者发布消息（所有订阅者都能收到）
        MessageProducer producer = topic.createProducer();
        producer.send("重大新闻: EST 2.0 发布了！");
    }
}
```

---

## 🔧 进阶篇

### 1. 多种消息中间件支持

EST Messaging 支持多种消息中间件：

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

### 2. 与 EST Collection 集成

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
        
        List<String> messages = List.of("消息 1", "消息 2", "消息 3", "消息 4", "消息 5");
        
        // 使用 Collection 批量发送消息
        Seqs.of(messages)
                .forEach(msg -> {
                    producer.send(msg);
                    System.out.println("已发送: " + msg);
                });
    }
}
```

---

## 💡 最佳实践

### 1. 消息确认机制

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
                System.out.println("处理消息: " + msg.getBody());
                // 处理成功，确认消息
                // msg.ack();
            } catch (Exception e) {
                System.out.println("处理失败: " + e.getMessage());
                // 处理失败，拒绝消息
                // msg.nack();
            }
        });
    }
}
```

---

## 🎯 总结

消息系统就像程序的"邮政系统"，让系统之间高效、可靠地通信！

下一章，我们将学习 EST AI 人工智能助手！🎉
