package ltd.idcu.est.examples.features;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.MessageQueue;
import ltd.idcu.est.messaging.api.MessageTopic;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.local.LocalMessages;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SimpleMessagingExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(SimpleMessagingExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== EST Messaging System Example ===");
        
        queueExample();
        topicExample();
        
        System.out.println("\n[X] All examples complete!");
    }
    
    private static void queueExample() throws InterruptedException {
        System.out.println("\n--- Message Queue ---");
        
        MessageQueue queue = LocalMessages.createQueue("my-queue");
        System.out.println("  Queue name: " + queue.getName());
        
        CountDownLatch latch = new CountDownLatch(3);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("my-queue", msg -> {
            System.out.println("  Received message: " + msg.getBody());
            latch.countDown();
        });
        
        MessageProducer producer = LocalMessages.newProducer();
        producer.send("my-queue", "Message 1");
        producer.send("my-queue", "Message 2");
        producer.send("my-queue", "Message 3");
        
        latch.await(2, TimeUnit.SECONDS);
        
        consumer.close();
        producer.close();
        
        logger.info("Message queue example complete");
    }
    
    private static void topicExample() throws InterruptedException {
        System.out.println("\n--- Message Topic ---");
        
        MessageTopic topic = LocalMessages.createTopic("my-topic");
        System.out.println("  Topic name: " + topic.getName());
        
        CountDownLatch latch1 = new CountDownLatch(2);
        CountDownLatch latch2 = new CountDownLatch(2);
        
        topic.subscribe(msg -> {
            System.out.println("  Subscriber 1 received: " + msg.getBody());
            latch1.countDown();
        });
        
        topic.subscribe(msg -> {
            System.out.println("  Subscriber 2 received: " + msg.getBody());
            latch2.countDown();
        });
        
        topic.publish(DefaultMessage.of(null, "my-topic", "Broadcast message A"));
        topic.publish(DefaultMessage.of(null, "my-topic", "Broadcast message B"));
        
        latch1.await(2, TimeUnit.SECONDS);
        latch2.await(2, TimeUnit.SECONDS);
        
        logger.info("Message topic example complete");
    }
}
