# Messaging 消息系统 API

消息系统提供多种消息中间件支持，包括本地消息、Kafka、Redis、RabbitMQ、ActiveMQ等，支持队列和主题两种模式。

## 核心接口

```java
public interface Message {
    String getId();
    Object getBody();
    Map<String, Object> getHeaders();
    long getTimestamp();
}

public interface MessageQueue {
    MessageProducer createProducer();
    MessageConsumer createConsumer();
    String getName();
    MessageStats stats();
}

public interface MessageTopic {
    MessageProducer createProducer();
    MessageConsumer createConsumer();
    String getName();
    MessageStats stats();
}

public interface MessageProducer {
    void send(Object body);
    void send(Object body, Map<String, Object> headers);
}

public interface MessageConsumer {
    void subscribe(Consumer<Message> handler);
    void unsubscribe();
}
```

## 本地消息

### 本地队列 (LocalMessageQueue)

```java
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageQueue;

// 创建本地队列
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
```

### 本地主题 (LocalMessageTopic)

```java
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.messaging.local.LocalMessageTopic;

// 创建本地主题
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
producer.send("重大新闻: EST 1.3 发布了！");
```

## Kafka 消息

```java
import ltd.idcu.est.features.messaging.kafka.KafkaMessages;
import ltd.idcu.est.features.messaging.kafka.KafkaConnection;

// 配置 Kafka 连接
KafkaConnection config = KafkaConnection.builder()
    .bootstrapServers("localhost:9092")
    .groupId("my-group")
    .build();

// 创建 Kafka 队列
var queue = KafkaMessages.createQueue("kafka-topic", config);

// 消费消息
MessageConsumer consumer = queue.createConsumer();
consumer.subscribe(msg -> {
    System.out.println("Kafka 收到: " + msg.getBody());
});

// 发送消息
MessageProducer producer = queue.createProducer();
producer.send("Hello from Kafka!");
```

## Redis 消息

```java
import ltd.idcu.est.features.messaging.redis.RedisMessages;
import ltd.idcu.est.features.messaging.redis.RedisConnection;

// 配置 Redis 连接
RedisConnection config = RedisConnection.builder()
    .host("localhost")
    .port(6379)
    .build();

// 创建 Redis 队列
var queue = RedisMessages.createQueue("redis-queue", config);

// 消费消息
MessageConsumer consumer = queue.createConsumer();
consumer.subscribe(msg -> {
    System.out.println("Redis 收到: " + msg.getBody());
});

// 发送消息
MessageProducer producer = queue.createProducer();
producer.send("Hello from Redis!");
```

## AMQP (RabbitMQ) 消息

```java
import ltd.idcu.est.features.messaging.amqp.AmqpMessages;
import ltd.idcu.est.features.messaging.amqp.AmqpConnection;

// 配置 AMQP 连接
AmqpConnection config = AmqpConnection.builder()
    .host("localhost")
    .port(5672)
    .username("guest")
    .password("guest")
    .build();

// 创建 AMQP 队列
var queue = AmqpMessages.createQueue("amqp-queue", config);

// 消费消息
MessageConsumer consumer = queue.createConsumer();
consumer.subscribe(msg -> {
    System.out.println("AMQP 收到: " + msg.getBody());
});

// 发送消息
MessageProducer producer = queue.createProducer();
producer.send("Hello from RabbitMQ!");
```

## 其他消息中间件支持

```java
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

// STOMP
import ltd.idcu.est.features.messaging.stomp.StompMessages;
```

## 消息统计

```java
MessageStats stats = queue.stats();
System.out.println("Sent: " + stats.getSentCount());
System.out.println("Received: " + stats.getReceivedCount());
System.out.println("Failed: " + stats.getFailedCount());
```

## 消息确认

```java
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
```
