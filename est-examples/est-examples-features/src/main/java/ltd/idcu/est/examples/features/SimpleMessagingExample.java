package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.MessageQueue;
import ltd.idcu.est.features.messaging.api.MessageTopic;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleMessagingExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleMessagingExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST 消息系统示例 ===");
        
        queueExample();
        topicExample();
        
        System.out.println("\n✓ 所有示例完成！");
    }
    
    private static void queueExample() throws InterruptedException {
        System.out.println("\n--- 消息队列 (Queue) ---");
        
        MessageQueue queue = LocalMessages.createQueue("my-queue");
        System.out.println("  队列名称: " + queue.getName());
        
        CountDownLatch latch = new CountDownLatch(3);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("my-queue", msg -> {
            System.out.println("  收到消息: " + msg.getBody());
            latch.countDown();
        });
        
        MessageProducer producer = LocalMessages.newProducer();
        producer.send("my-queue", "消息 1");
        producer.send("my-queue", "消息 2");
        producer.send("my-queue", "消息 3");
        
        latch.await(2, TimeUnit.SECONDS);
        
        consumer.close();
        producer.close();
        
        logger.info("消息队列示例完成");
    }
    
    private static void topicExample() throws InterruptedException {
        System.out.println("\n--- 消息主题 (Topic) ---");
        
        MessageTopic topic = LocalMessages.createTopic("my-topic");
        System.out.println("  主题名称: " + topic.getName());
        
        CountDownLatch latch1 = new CountDownLatch(2);
        CountDownLatch latch2 = new CountDownLatch(2);
        
        topic.subscribe(msg -> {
            System.out.println("  订阅者 1 收到: " + msg.getBody());
            latch1.countDown();
        });
        
        topic.subscribe(msg -> {
            System.out.println("  订阅者 2 收到: " + msg.getBody());
            latch2.countDown();
        });
        
        topic.publish(DefaultMessage.of(null, "my-topic", "广播消息 A"));
        topic.publish(DefaultMessage.of(null, "my-topic", "广播消息 B"));
        
        latch1.await(2, TimeUnit.SECONDS);
        latch2.await(2, TimeUnit.SECONDS);
        
        logger.info("消息主题示例完成");
    }
}
