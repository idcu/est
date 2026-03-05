package ltd.idcu.est.examples.features;

import ltd.idcu.est.features.messaging.api.DefaultMessage;
import ltd.idcu.est.features.messaging.api.Message;
import ltd.idcu.est.features.messaging.api.MessageConsumer;
import ltd.idcu.est.features.messaging.api.MessageProducer;
import ltd.idcu.est.features.messaging.api.MessageQueue;
import ltd.idcu.est.features.messaging.api.MessageTopic;
import ltd.idcu.est.features.messaging.local.LocalMessages;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CompleteMessagingExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteMessagingExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(70));
        System.out.println("EST 消息系统模块 - 完整示例");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例将展示 EST 消息系统模块的各种功能：");
        System.out.println("  - 消息队列（Queue，点对点）");
        System.out.println("  - 消息主题（Topic，发布/订阅）");
        System.out.println("  - 多种实现方式（本地、MQTT、AMQP）");
        System.out.println("  - 实际应用场景（异步处理、解耦）");
        System.out.println("  - 与事件总线的区别");
        System.out.println();
        
        System.out.println("=".repeat(70));
        System.out.println("第一部分：理解消息系统的作用");
        System.out.println("=".repeat(70));
        System.out.println("\n【为什么需要消息系统？】");
        System.out.println("  - 异步处理：不用等结果，先返回");
        System.out.println("  - 系统解耦：模块之间不直接依赖");
        System.out.println("  - 流量削峰：缓冲突发流量");
        System.out.println("  - 可靠性：消息不会丢失\n");
        
        System.out.println("【消息系统 vs 事件总线】");
        System.out.println("   消息系统：");
        System.out.println("     - 跨进程、跨服务通信");
        System.out.println("     - 消息持久化，不丢失");
        System.out.println("     - 适合分布式系统");
        System.out.println();
        System.out.println("   事件总线：");
        System.out.println("     - 同一个进程内通信");
        System.out.println("     - 简单高效");
        System.out.println("     - 适合单体应用\n");
        
        queueExample();
        topicExample();
        practicalScenariosExample();
        differentImplementationsExample();
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有消息系统示例完成！");
        System.out.println("=".repeat(70));
    }
    
    private static void queueExample() throws InterruptedException {
        System.out.println("\n--- 方式一：消息队列（Queue，点对点）---");
        System.out.println("\n【什么是消息队列？】");
        System.out.println("  - 一条消息只能被一个消费者消费");
        System.out.println("  - 就像排队买饭，一份饭只能给一个人");
        System.out.println("  - 适合：任务分发、异步处理\n");
        
        System.out.println("【队列的特点】");
        System.out.println("   ✅ 消息有序：先到先得");
        System.out.println("   ✅ 消息不重复：每条只消费一次");
        System.out.println("   ✅ 可靠性：消息不会丢失\n");
        
        System.out.println("步骤 1: 创建消息队列");
        MessageQueue orderQueue = LocalMessages.createQueue("order-queue");
        System.out.println("   队列名称: " + orderQueue.getName());
        
        System.out.println("\n步骤 2: 创建消费者");
        CountDownLatch latch = new CountDownLatch(3);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("order-queue", message -> {
            System.out.println("   🛒 消费者收到订单: " + message.getBody());
            System.out.println("      正在处理订单...");
            latch.countDown();
        });
        System.out.println("   消费者已订阅队列");
        
        System.out.println("\n步骤 3: 创建生产者，发送消息");
        MessageProducer producer = LocalMessages.newProducer();
        producer.send("order-queue", "订单 #1001: iPhone 15 x 2");
        producer.send("order-queue", "订单 #1002: MacBook Pro x 1");
        producer.send("order-queue", "订单 #1003: AirPods Pro x 3");
        System.out.println("   生产者已发送 3 条消息");
        
        System.out.println("\n步骤 4: 等待消费者处理");
        boolean allReceived = latch.await(2, TimeUnit.SECONDS);
        System.out.println("   所有消息都处理完成: " + allReceived);
        
        System.out.println("\n步骤 5: 关闭资源");
        consumer.close();
        producer.close();
        
        System.out.println("\n💡 提示：多个消费者的情况");
        System.out.println("   如果有多个消费者订阅同一个队列：");
        System.out.println("   - 消息会轮询分给各个消费者");
        System.out.println("   - 可以提高处理速度");
        System.out.println("   - 适合：订单处理、邮件发送等\n");
        
        logger.info("消息队列示例完成");
    }
    
    private static void topicExample() throws InterruptedException {
        System.out.println("\n--- 方式二：消息主题（Topic，发布/订阅）---");
        System.out.println("\n【什么是消息主题？】");
        System.out.println("  - 一条消息会被所有订阅者收到");
        System.out.println("  - 就像广播，所有人都能听到");
        System.out.println("  - 适合：通知、广播、实时数据\n");
        
        System.out.println("【主题的特点】");
        System.out.println("   ✅ 一对多：一个消息多个订阅者");
        System.out.println("   ✅ 实时性：订阅者实时收到消息");
        System.out.println("   ✅ 解耦：发布者不知道订阅者\n");
        
        System.out.println("步骤 1: 创建消息主题");
        MessageTopic newsTopic = LocalMessages.createTopic("news-topic");
        System.out.println("   主题名称: " + newsTopic.getName());
        
        System.out.println("\n步骤 2: 创建多个订阅者");
        CountDownLatch subscriber1Latch = new CountDownLatch(2);
        CountDownLatch subscriber2Latch = new CountDownLatch(2);
        CountDownLatch subscriber3Latch = new CountDownLatch(2);
        
        newsTopic.subscribe(message -> {
            System.out.println("   📱 订阅者 1 (手机) 收到新闻: " + message.getBody());
            subscriber1Latch.countDown();
        });
        
        newsTopic.subscribe(message -> {
            System.out.println("   💻 订阅者 2 (电脑) 收到新闻: " + message.getBody());
            subscriber2Latch.countDown();
        });
        
        newsTopic.subscribe(message -> {
            System.out.println("   📺 订阅者 3 (平板) 收到新闻: " + message.getBody());
            subscriber3Latch.countDown();
        });
        System.out.println("   3 个订阅者已订阅主题");
        
        System.out.println("\n步骤 3: 发布消息");
        newsTopic.publish(DefaultMessage.of(null, "news-topic", "突发新闻：EST 框架 1.3 发布！"));
        newsTopic.publish(DefaultMessage.of(null, "news-topic", "科技新闻：AI 技术新突破！"));
        System.out.println("   已发布 2 条新闻");
        
        System.out.println("\n步骤 4: 等待所有订阅者收到");
        boolean sub1 = subscriber1Latch.await(2, TimeUnit.SECONDS);
        boolean sub2 = subscriber2Latch.await(2, TimeUnit.SECONDS);
        boolean sub3 = subscriber3Latch.await(2, TimeUnit.SECONDS);
        System.out.println("   订阅者 1 收到所有: " + sub1);
        System.out.println("   订阅者 2 收到所有: " + sub2);
        System.out.println("   订阅者 3 收到所有: " + sub3);
        
        System.out.println("\n💡 提示：队列 vs 主题");
        System.out.println("   用队列的场景：");
        System.out.println("     - 订单处理（一个订单只处理一次）");
        System.out.println("     - 发送邮件（一封邮件只发一次）");
        System.out.println("     - 数据备份（一个备份任务只执行一次）");
        System.out.println();
        System.out.println("   用主题的场景：");
        System.out.println("     - 新闻广播（所有人都看）");
        System.out.println("     - 实时数据（股票、天气）");
        System.out.println("     - 系统通知（所有人都收到）\n");
        
        logger.info("消息主题示例完成");
    }
    
    private static void practicalScenariosExample() {
        System.out.println("\n--- 方式三：实际应用场景 ---");
        
        System.out.println("\n【场景 1：订单异步处理】");
        System.out.println("   问题：用户下单后，要做很多事：");
        System.out.println("     - 扣减库存");
        System.out.println("     - 发送确认邮件");
        System.out.println("     - 通知仓库");
        System.out.println("     - 更新统计数据");
        System.out.println("   如果都同步做，用户会等很久！");
        System.out.println();
        System.out.println("   解决方案：用消息队列异步处理");
        System.out.println("     1. 用户下单 → 快速返回 '下单成功'");
        System.out.println("     2. 把订单消息发到队列");
        System.out.println("     3. 后台消费者慢慢处理：扣库存、发邮件等");
        System.out.println("   用户体验：快！");
        System.out.println("   系统稳定性：即使后台挂了，消息还在队列里\n");
        
        System.out.println("【场景 2：系统解耦】");
        System.out.println("   问题：订单服务要调用很多服务：");
        System.out.println("     - 库存服务");
        System.out.println("     - 邮件服务");
        System.out.println("     - 统计服务");
        System.out.println("   如果库存服务挂了，订单服务也用不了！");
        System.out.println();
        System.out.println("   解决方案：用消息系统解耦");
        System.out.println("     订单服务只负责发消息到队列");
        System.out.println("     各个服务自己从队列拿消息处理");
        System.out.println("   好处：");
        System.out.println("     - 一个服务挂了不影响其他");
        System.out.println("     - 可以独立扩展各个服务");
        System.out.println("     - 代码更简洁\n");
        
        System.out.println("【场景 3：流量削峰】");
        System.out.println("   问题：秒杀活动，每秒 1 万请求");
        System.out.println("   数据库每秒只能处理 1000 请求");
        System.out.println("   直接打数据库会崩！");
        System.out.println();
        System.out.println("   解决方案：用消息队列缓冲");
        System.out.println("     1. 请求先放到队列里（缓冲）");
        System.out.println("     2. 消费者按数据库能处理的速度慢慢消费");
        System.out.println("     3. 保护数据库不被冲垮");
        System.out.println("   就像水库，洪水来时存起来，慢慢放\n");
        
        System.out.println("【场景 4：日志收集】");
        System.out.println("   需求：多个服务的日志要汇总");
        System.out.println();
        System.out.println("   解决方案：用消息主题");
        System.out.println("     1. 每个服务把日志发到主题");
        System.out.println("     2. 日志收集服务订阅主题");
        System.out.println("     3. 把所有日志存到一起");
        System.out.println("   好处：");
        System.out.println("     - 服务不需要知道日志收集服务的存在");
        System.out.println("     - 可以随时添加新的日志处理服务\n");
        
        System.out.println("【场景 5：实时通知】");
        System.out.println("   需求：用户下单后，要通知多个地方：");
        System.out.println("     - 用户手机 APP 推送");
        System.out.println("     - 商家后台通知");
        System.out.println("     - 客服系统通知");
        System.out.println();
        System.out.println("   解决方案：用消息主题");
        System.out.println("     1. 订单服务发布 '订单创建' 消息");
        System.out.println("     2. 各个系统订阅这个主题");
        System.out.println("     3. 各自处理自己的通知逻辑");
        System.out.println("   好处：");
        System.out.println("     - 订单服务不需要管有多少个通知系统");
        System.out.println("     - 添加新的通知系统不用改订单服务\n");
        
        logger.info("实际应用场景示例完成");
    }
    
    private static void differentImplementationsExample() {
        System.out.println("\n--- 方式四：不同的实现方式 ---");
        
        System.out.println("\n【实现方式 1：本地消息（Local）】");
        System.out.println("   特点：");
        System.out.println("     - 同一个 JVM 进程内");
        System.out.println("     - 最快，最简单");
        System.out.println("     - 重启后消息丢失");
        System.out.println();
        System.out.println("   适用场景：");
        System.out.println("     - 单体应用");
        System.out.println("     - 开发测试");
        System.out.println("     - 消息不一定要持久化\n");
        
        System.out.println("【实现方式 2：MQTT】");
        System.out.println("   特点：");
        System.out.println("     - 轻量级消息协议");
        System.out.println("     - 适合物联网（IoT）");
        System.out.println("     - 低带宽、不稳定网络");
        System.out.println();
        System.out.println("   适用场景：");
        System.out.println("     - 智能设备（传感器、摄像头）");
        System.out.println("     - 移动应用");
        System.out.println("     - 网络不稳定的环境\n");
        
        System.out.println("【实现方式 3：AMQP（RabbitMQ）】");
        System.out.println("   特点：");
        System.out.println("     - 企业级消息队列");
        System.out.println("     - 功能强大、可靠");
        System.out.println("     - 消息持久化、不丢失");
        System.out.println();
        System.out.println("   适用场景：");
        System.out.println("     - 生产环境");
        System.out.println("     - 高可靠性要求");
        System.out.println("     - 微服务架构\n");
        
        System.out.println("【如何选择？】");
        System.out.println("   🧪 开发测试 → 用 Local（简单快速）");
        System.out.println("   🔌 物联网设备 → 用 MQTT（轻量级）");
        System.out.println("   🏢 生产环境 → 用 AMQP（可靠稳定）\n");
        
        System.out.println("💡 EST 的好处");
        System.out.println("   同样的代码，换个配置就能切换实现！");
        System.out.println("   开发用 Local，生产用 RabbitMQ");
        System.out.println("   不需要改代码！\n");
        
        logger.info("不同实现方式示例完成");
    }
}
