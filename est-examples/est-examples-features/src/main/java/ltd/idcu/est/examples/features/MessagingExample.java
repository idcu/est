package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessageQueue;
import ltd.idcu.est.features.messaging.api.MessageTopic;
import ltd.idcu.est.features.messaging.local.LocalMessages;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MessagingExample {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n=== Messaging Examples ===");
        
        queueExample();
        topicExample();
        convenienceMethodsExample();
    }
    
    private static void queueExample() throws InterruptedException {
        System.out.println("\n--- Message Queue Example ---");
        
        MessageQueue queue = LocalMessages.createQueue("my-queue");
        System.out.println("Created queue: " + queue.getName());
        
        CountDownLatch latch = new CountDownLatch(3);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("my-queue", message -> {
            System.out.println("Received from queue: " + message.getBody());
            latch.countDown();
        });
        System.out.println("Consumer subscribed to queue");
        
        MessageProducer producer = LocalMessages.newProducer();
        producer.send("my-queue", "Hello, Queue!");
        producer.send("my-queue", "Second message");
        producer.send("my-queue", "Third message");
        System.out.println("Sent 3 messages to queue");
        
        boolean received = latch.await(2, TimeUnit.SECONDS);
        System.out.println("All messages received: " + received);
        
        consumer.close();
        producer.close();
    }
    
    private static void topicExample() throws InterruptedException {
        System.out.println("\n--- Message Topic Example ---");
        
        MessageTopic topic = LocalMessages.createTopic("my-topic");
        System.out.println("Created topic: " + topic.getName());
        
        CountDownLatch subscriber1Latch = new CountDownLatch(2);
        CountDownLatch subscriber2Latch = new CountDownLatch(2);
        
        topic.subscribe(message -> {
            System.out.println("Subscriber 1 received: " + message.getBody());
            subscriber1Latch.countDown();
        });
        
        topic.subscribe(message -> {
            System.out.println("Subscriber 2 received: " + message.getBody());
            subscriber2Latch.countDown();
        });
        System.out.println("2 subscribers subscribed to topic");
        
        topic.publish(DefaultMessage.of(null, "my-topic", "Hello, Topic!"));
        topic.publish(DefaultMessage.of(null, "my-topic", "Broadcast message"));
        System.out.println("Published 2 messages to topic");
        
        boolean sub1Received = subscriber1Latch.await(2, TimeUnit.SECONDS);
        boolean sub2Received = subscriber2Latch.await(2, TimeUnit.SECONDS);
        System.out.println("Subscriber 1 received all: " + sub1Received);
        System.out.println("Subscriber 2 received all: " + sub2Received);
    }
    
    private static void convenienceMethodsExample() throws InterruptedException {
        System.out.println("\n--- Convenience Methods Example ---");
        
        CountDownLatch latch = new CountDownLatch(1);
        
        LocalMessages.subscribe("notifications", message -> {
            System.out.println("Received notification: " + message.getBody());
            latch.countDown();
        });
        
        LocalMessages.publish("notifications", "Important update!");
        System.out.println("Published notification");
        
        latch.await(2, TimeUnit.SECONDS);
    }
}
