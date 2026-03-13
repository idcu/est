package ltd.idcu.est.examples.features;

import ltd.idcu.est.messaging.api.DefaultMessage;
import ltd.idcu.est.messaging.api.Message;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.api.MessageQueue;
import ltd.idcu.est.messaging.api.MessageTopic;
import ltd.idcu.est.messaging.local.LocalMessages;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CompleteMessagingExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteMessagingExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("EST Messaging Module - Complete Example");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates various features of the EST Messaging module:");
        System.out.println("  - Message Queue (Queue, Point-to-Point)");
        System.out.println("  - Message Topic (Topic, Publish-Subscribe)");
        System.out.println("  - Multiple implementations (Local, MQTT, AMQP)");
        System.out.println("  - Practical scenarios (Async processing, decoupling)");
        System.out.println("  - Difference from Event Bus");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("Part 1: Understanding the Role of Messaging");
        System.out.println("=".repeat(70));
        System.out.println("\n[Why Do We Need a Messaging System?]");
        System.out.println("  - Async processing: Don't wait for result, return first");
        System.out.println("  - System decoupling: Modules don't directly depend on each other");
        System.out.println("  - Traffic peak shaving: Buffer burst traffic");
        System.out.println("  - Reliability: Messages won't be lost\n");
        
        System.out.println("[Messaging System vs Event Bus]");
        System.out.println("   Messaging System:");
        System.out.println("     - Cross-process, cross-service communication");
        System.out.println("     - Message persistence, no loss");
        System.out.println("     - Suitable for distributed systems");
        System.out.println();
        System.out.println("   Event Bus:");
        System.out.println("     - Same process communication");
        System.out.println("     - Simple and efficient");
        System.out.println("     - Suitable for monolithic applications\n");
        
        queueExample();
        topicExample();
        practicalScenariosExample();
        differentImplementationsExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[X] All messaging system examples completed!");
        System.out.println("=".repeat(70));
    }
    
    private static void queueExample() throws InterruptedException {
        System.out.println("\n--- Approach 1: Message Queue (Queue, Point-to-Point) ---");
        System.out.println("\n[What is a Message Queue?]");
        System.out.println("  - A message can only be consumed by one consumer");
        System.out.println("  - Like queuing for food, one portion goes to one person");
        System.out.println("  - Suitable for: Task distribution, async processing\n");
        
        System.out.println("[Queue Features]");
        System.out.println("   [Ordered] Messages ordered: First come first served");
        System.out.println("   [No Duplicates] No duplicate messages: Each consumed only once");
        System.out.println("   [Reliable] Reliability: Messages won't be lost\n");
        
        System.out.println("Step 1: Create message queue");
        MessageQueue orderQueue = LocalMessages.createQueue("order-queue");
        System.out.println("   Queue name: " + orderQueue.getName());
        
        System.out.println("\nStep 2: Create consumer");
        CountDownLatch latch = new CountDownLatch(3);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("order-queue", message -> {
            System.out.println("   [Order] Consumer received order: " + message.getBody());
            System.out.println("      Processing order...");
            latch.countDown();
        });
        System.out.println("   Consumer subscribed to queue");
        
        System.out.println("\nStep 3: Create producer, send messages");
        MessageProducer producer = LocalMessages.newProducer();
        producer.send("order-queue", "Order #1001: iPhone 15 x 2");
        producer.send("order-queue", "Order #1002: MacBook Pro x 1");
        producer.send("order-queue", "Order #1003: AirPods Pro x 3");
        System.out.println("   Producer sent 3 messages");
        
        System.out.println("\nStep 4: Wait for consumer to process");
        boolean allReceived = latch.await(2, TimeUnit.SECONDS);
        System.out.println("   All messages processed: " + allReceived);
        
        System.out.println("\nStep 5: Close resources");
        consumer.close();
        producer.close();
        
        System.out.println("\n[Tip] Multiple consumers scenario");
        System.out.println("   If multiple consumers subscribe to same queue:");
        System.out.println("   - Messages will be round-robin distributed to consumers");
        System.out.println("   - Can improve processing speed");
        System.out.println("   - Suitable for: Order processing, email sending, etc.\n");
        
        logger.info("Message queue example completed");
    }
    
    private static void topicExample() throws InterruptedException {
        System.out.println("\n--- Approach 2: Message Topic (Topic, Publish-Subscribe) ---");
        System.out.println("\n[What is a Message Topic?]");
        System.out.println("  - A message will be received by all subscribers");
        System.out.println("  - Like broadcast, everyone can hear");
        System.out.println("  - Suitable for: Notifications, broadcast, real-time data\n");
        
        System.out.println("[Topic Features]");
        System.out.println("   [One-to-Many] One-to-many: One message multiple subscribers");
        System.out.println("   [Real-time] Real-time: Subscribers receive messages in real-time");
        System.out.println("   [Decoupled] Decoupled: Publisher doesn't know subscribers\n");
        
        System.out.println("Step 1: Create message topic");
        MessageTopic newsTopic = LocalMessages.createTopic("news-topic");
        System.out.println("   Topic name: " + newsTopic.getName());
        
        System.out.println("\nStep 2: Create multiple subscribers");
        CountDownLatch subscriber1Latch = new CountDownLatch(2);
        CountDownLatch subscriber2Latch = new CountDownLatch(2);
        CountDownLatch subscriber3Latch = new CountDownLatch(2);
        
        newsTopic.subscribe(message -> {
            System.out.println("   [Mobile] Subscriber 1 (Mobile) received news: " + message.getBody());
            subscriber1Latch.countDown();
        });
        
        newsTopic.subscribe(message -> {
            System.out.println("   [Desktop] Subscriber 2 (Desktop) received news: " + message.getBody());
            subscriber2Latch.countDown();
        });
        
        newsTopic.subscribe(message -> {
            System.out.println("   [Tablet] Subscriber 3 (Tablet) received news: " + message.getBody());
            subscriber3Latch.countDown();
        });
        System.out.println("   3 subscribers subscribed to topic");
        
        System.out.println("\nStep 3: Publish messages");
        newsTopic.publish(DefaultMessage.of(null, "news-topic", "Breaking News: EST Framework 1.3 Released!"));
        newsTopic.publish(DefaultMessage.of(null, "news-topic", "Tech News: AI Technology Breakthrough!"));
        System.out.println("   Published 2 news items");
        
        System.out.println("\nStep 4: Wait for all subscribers to receive");
        boolean sub1 = subscriber1Latch.await(2, TimeUnit.SECONDS);
        boolean sub2 = subscriber2Latch.await(2, TimeUnit.SECONDS);
        boolean sub3 = subscriber3Latch.await(2, TimeUnit.SECONDS);
        System.out.println("   Subscriber 1 received all: " + sub1);
        System.out.println("   Subscriber 2 received all: " + sub2);
        System.out.println("   Subscriber 3 received all: " + sub3);
        
        System.out.println("\n[Tip] Queue vs Topic");
        System.out.println("   When to use Queue:");
        System.out.println("     - Order processing (one order processed only once)");
        System.out.println("     - Email sending (one email sent only once)");
        System.out.println("     - Data backup (one backup task executed only once)");
        System.out.println();
        System.out.println("   When to use Topic:");
        System.out.println("     - News broadcast (everyone sees it)");
        System.out.println("     - Real-time data (stocks, weather)");
        System.out.println("     - System notifications (everyone receives)\n");
        
        logger.info("Message topic example completed");
    }
    
    private static void practicalScenariosExample() {
        System.out.println("\n--- Approach 3: Practical Application Scenarios ---");
        
        System.out.println("\n[Scenario 1: Async Order Processing]");
        System.out.println("   Problem: After user places order, many things need to be done:");
        System.out.println("     - Deduct inventory");
        System.out.println("     - Send confirmation email");
        System.out.println("     - Notify warehouse");
        System.out.println("     - Update statistics");
        System.out.println("   If all done synchronously, user will wait a long time!");
        System.out.println();
        System.out.println("   Solution: Use message queue for async processing");
        System.out.println("     1. User places order -> Quickly return 'Order placed successfully'");
        System.out.println("     2. Send order message to queue");
        System.out.println("     3. Background consumers process slowly: deduct inventory, send email, etc.");
        System.out.println("   User experience: Fast");
        System.out.println("   System stability: Even if backend goes down, messages are still in queue\n");
        
        System.out.println("[Scenario 2: System Decoupling]");
        System.out.println("   Problem: Order service needs to call many services:");
        System.out.println("     - Inventory service");
        System.out.println("     - Email service");
        System.out.println("     - Statistics service");
        System.out.println("   If inventory service goes down, order service also can't be used!");
        System.out.println();
        System.out.println("   Solution: Use messaging system to decouple");
        System.out.println("     Order service only responsible for sending messages to queue");
        System.out.println("     Each service gets messages from queue to process on their own");
        System.out.println("   Benefits:");
        System.out.println("     - One service going down doesn't affect others");
        System.out.println("     - Can scale each service independently");
        System.out.println("     - Code is cleaner\n");
        
        System.out.println("[Scenario 3: Traffic Peak Shaving]");
        System.out.println("   Problem: Flash sale activity, 10,000 requests per second");
        System.out.println("   Database can only process 1000 requests per second");
        System.out.println("   Directly hitting database will crash it");
        System.out.println();
        System.out.println("   Solution: Use message queue to buffer");
        System.out.println("     1. Requests first go into queue (buffer)");
        System.out.println("     2. Consumers consume slowly at speed database can handle");
        System.out.println("     3. Protect database from being overwhelmed");
        System.out.println("   Like a reservoir, store flood when it comes, release slowly\n");
        
        System.out.println("[Scenario 4: Log Collection]");
        System.out.println("   Requirement: Logs from multiple services need to be aggregated");
        System.out.println();
        System.out.println("   Solution: Use message topic");
        System.out.println("     1. Each service sends logs to topic");
        System.out.println("     2. Log collection service subscribes to topic");
        System.out.println("     3. Store all logs together");
        System.out.println("   Benefits:");
        System.out.println("     - Services don't need to know log collection service exists");
        System.out.println("     - Can add new log processing services anytime\n");
        
        System.out.println("[Scenario 5: Real-time Notifications]");
        System.out.println("   Requirement: After user places order, need to notify multiple places:");
        System.out.println("     - User mobile app push");
        System.out.println("     - Merchant backend notification");
        System.out.println("     - Customer service system notification");
        System.out.println();
        System.out.println("   Solution: Use message topic");
        System.out.println("     1. Order service publishes 'Order Created' message");
        System.out.println("     2. Each system subscribes to this topic");
        System.out.println("     3. Each handles their own notification logic");
        System.out.println("   Benefits:");
        System.out.println("     - Order service doesn't need to care how many notification systems there are");
        System.out.println("     - Adding new notification system doesn't require changing order service\n");
        
        logger.info("Practical scenarios example completed");
    }
    
    private static void differentImplementationsExample() {
        System.out.println("\n--- Approach 4: Different Implementation Methods ---");
        
        System.out.println("\n[Implementation 1: Local Messaging]");
        System.out.println("   Features:");
        System.out.println("     - Same JVM process");
        System.out.println("     - Fastest, simplest");
        System.out.println("     - Messages lost after restart");
        System.out.println();
        System.out.println("   Applicable scenarios:");
        System.out.println("     - Monolithic applications");
        System.out.println("     - Development testing");
        System.out.println("     - Messages don't necessarily need persistence\n");
        
        System.out.println("[Implementation 2: MQTT]");
        System.out.println("   Features:");
        System.out.println("     - Lightweight messaging protocol");
        System.out.println("     - Suitable for IoT (Internet of Things)");
        System.out.println("     - Low bandwidth, unstable network");
        System.out.println();
        System.out.println("   Applicable scenarios:");
        System.out.println("     - Smart devices (sensors, cameras)");
        System.out.println("     - Mobile applications");
        System.out.println("     - Unstable network environments\n");
        
        System.out.println("[Implementation 3: AMQP (RabbitMQ)]");
        System.out.println("   Features:");
        System.out.println("     - Enterprise-grade message queue");
        System.out.println("     - Powerful features, reliable");
        System.out.println("     - Message persistence, no loss");
        System.out.println();
        System.out.println("   Applicable scenarios:");
        System.out.println("     - Production environments");
        System.out.println("     - High reliability requirements");
        System.out.println("     - Microservices architecture\n");
        
        System.out.println("[How to Choose?]");
        System.out.println("   [Dev] Development testing -> Local (simple and fast)");
        System.out.println("   [IoT] IoT devices -> MQTT (lightweight)");
        System.out.println("   [Prod] Production environment -> AMQP (reliable and stable)\n");
        
        System.out.println("[Tip] EST's Advantage");
        System.out.println("   Same code, change config to switch implementations!");
        System.out.println("   Development uses Local, production uses RabbitMQ");
        System.out.println("   No need to change code!\n");
        
        logger.info("Different implementations example completed");
    }
}
