package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.data.api.Entity;
import ltd.idcu.est.data.api.Id;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryData;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEventBus;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.scheduler.api.ScheduleConfig;
import ltd.idcu.est.scheduler.api.ScheduleType;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.fixed.FixedSchedulers;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.api.HealthCheckResult;
import ltd.idcu.est.messaging.api.MessageQueue;
import ltd.idcu.est.messaging.api.MessageConsumer;
import ltd.idcu.est.messaging.api.MessageProducer;
import ltd.idcu.est.messaging.local.LocalMessages;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ModuleIntegrationExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(ModuleIntegrationExample.class);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(80));
        System.out.println("EST 框架 - 模块联动综合示例");
        System.out.println("=".repeat(80));
        System.out.println("\n本示例展�?EST 框架各个模块如何协同工作�?);
        System.out.println("  📦 依赖注入容器 - 管理所有服�?);
        System.out.println("  💾 数据访问 - 存储用户和订�?);
        System.out.println("  �?缓存系统 - 加速热点数据访�?);
        System.out.println("  📡 事件总线 - 组件间解耦通信");
        System.out.println("  📝 日志系统 - 记录所有操�?);
        System.out.println("  �?调度系统 - 定时执行任务");
        System.out.println("  🔍 监控系统 - 监控应用状�?);
        System.out.println("  📨 消息系统 - 异步处理订单");
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("架构图：");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("    ┌─────────────────────────────────────────────────────────�?);
        System.out.println("    �?             Web 请求 / 用户操作                   �?);
        System.out.println("    └────────────────────┬────────────────────────────────�?);
        System.out.println("                         �?);
        System.out.println("                         �?);
        System.out.println("    ┌─────────────────────────────────────────────────────────�?);
        System.out.println("    �?        依赖注入容器 (Container)               �?);
        System.out.println("    �? 管理所有服务的生命周期                    �?);
        System.out.println("    └────────────────┬────────────────────────────────────�?);
        System.out.println("                     �?);
        System.out.println("         ┌───────────┼───────────�?);
        System.out.println("         �?          �?          �?);
        System.out.println("         �?          �?          �?);
        System.out.println("    ┌─────────�?┌─────────�?┌─────────�?);
        System.out.println("    �?数据访问 �?�?缓存系统 �?�?事件总线 �?);
        System.out.println("    │Repository�?�? Cache  �?│EventBus �?);
        System.out.println("    └────┬────�?└────┬────�?└────┬────�?);
        System.out.println("         �?          �?          �?);
        System.out.println("         └───────────┼───────────�?);
        System.out.println("                     �?);
        System.out.println("         ┌───────────┼───────────�?);
        System.out.println("         �?          �?          �?);
        System.out.println("         �?          �?          �?);
        System.out.println("    ┌─────────�?┌─────────�?┌─────────�?);
        System.out.println("    �?调度系统 �?�?监控系统 �?�?消息系统 �?);
        System.out.println("    │Scheduler�?�?Monitor �?�?Messaging�?);
        System.out.println("    └─────────�?└─────────�?└─────────�?);
        System.out.println("                     �?);
        System.out.println("                     �?);
        System.out.println("    ┌─────────────────────────────────────────────────────────�?);
        System.out.println("    �?          日志系统 (Logging)                    �?);
        System.out.println("    �?     记录所有操作和事件                      �?);
        System.out.println("    └─────────────────────────────────────────────────────────�?);
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("开始演�?..");
        System.out.println("=".repeat(80));
        
        Container container = new DefaultContainer();
        initializeServices(container);
        
        demonstrateUserOperations(container);
        demonstrateOrderOperations(container);
        demonstrateScheduledTasks(container);
        demonstrateMonitoring(container);
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("�?所有模块联动演示完成！");
        System.out.println("=".repeat(80));
        System.out.println("\n总结�?);
        System.out.println("  1. 容器管理所有服务，方便依赖注入");
        System.out.println("  2. 数据访问负责持久化，缓存负责加�?);
        System.out.println("  3. 事件总线解耦组件，消息系统异步处理");
        System.out.println("  4. 调度系统定时任务，监控系统保驾护�?);
        System.out.println("  5. 日志系统记录一切，问题排查不愁");
    }
    
    @Entity(tableName = "users")
    public static class User {
        @Id
        private Long id;
        private String name;
        private String email;
        
        public User() {}
        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    @Entity(tableName = "orders")
    public static class Order {
        @Id
        private Long id;
        private Long userId;
        private String product;
        private double amount;
        private String status;
        
        public Order() {}
        public Order(Long userId, String product, double amount) {
            this.userId = userId;
            this.product = product;
            this.amount = amount;
            this.status = "PENDING";
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class UserService {
        private final Repository<User, Long> userRepo;
        private final Cache<Long, User> userCache;
        private final EventBus eventBus;
        
        public UserService(Repository<User, Long> userRepo, 
                          Cache<Long, User> userCache,
                          EventBus eventBus) {
            this.userRepo = userRepo;
            this.userCache = userCache;
            this.eventBus = eventBus;
        }
        
        public User createUser(String name, String email) {
            logger.info("创建用户: {}", name);
            User user = new User(name, email);
            userRepo.save(user);
            userCache.put(user.getId(), user);
            eventBus.publish("user.created", user);
            logger.info("用户创建成功，ID: {}", user.getId());
            return user;
        }
        
        public User getUserById(Long id) {
            logger.debug("查询用户: {}", id);
            Optional<User> cached = userCache.get(id);
            if (cached.isPresent()) {
                logger.info("缓存命中�?);
                return cached.get();
            }
            logger.info("缓存未命中，查询数据�?..");
            Optional<User> user = userRepo.findById(id);
            user.ifPresent(u -> userCache.put(u.getId(), u));
            return user.orElse(null);
        }
        
        public List<User> getAllUsers() {
            return userRepo.findAll();
        }
    }
    
    public static class OrderService {
        private final Repository<Order, Long> orderRepo;
        private final EventBus eventBus;
        private final MessageProducer messageProducer;
        
        public OrderService(Repository<Order, Long> orderRepo,
                           EventBus eventBus,
                           MessageProducer messageProducer) {
            this.orderRepo = orderRepo;
            this.eventBus = eventBus;
            this.messageProducer = messageProducer;
        }
        
        public Order createOrder(Long userId, String product, double amount) {
            logger.info("创建订单: 用户={}, 商品={}, 金额={}", userId, product, amount);
            Order order = new Order(userId, product, amount);
            orderRepo.save(order);
            eventBus.publish("order.created", order);
            messageProducer.send("order-queue", "订单 #" + order.getId() + " 待处�?);
            logger.info("订单创建成功，ID: {}", order.getId());
            return order;
        }
        
        public List<Order> getAllOrders() {
            return orderRepo.findAll();
        }
    }
    
    public static class NotificationService {
        public void onUserCreated(User user) {
            logger.info("[通知服务] �?{} 发送欢迎邮�?, user.getEmail());
        }
        
        public void onOrderCreated(Order order) {
            logger.info("[通知服务] 向用�?{} 发送订单确�?, order.getUserId());
        }
    }
    
    public static class StatisticsService {
        private int userCount = 0;
        private int orderCount = 0;
        
        public void onUserCreated(User user) {
            userCount++;
            logger.info("[统计服务] 用户�? {}", userCount);
        }
        
        public void onOrderCreated(Order order) {
            orderCount++;
            logger.info("[统计服务] 订单�? {}", orderCount);
        }
        
        public int getUserCount() { return userCount; }
        public int getOrderCount() { return orderCount; }
    }
    
    private static void initializeServices(Container container) {
        System.out.println("\n--- 步骤 1: 初始化所有服�?---");
        
        Repository<User, Long> userRepo = MemoryData.newRepository();
        Repository<Order, Long> orderRepo = MemoryData.newRepository();
        Cache<Long, User> userCache = new MemoryCache<>();
        EventBus eventBus = new LocalEventBus();
        MessageQueue orderQueue = LocalMessages.createQueue("order-queue");
        MessageProducer messageProducer = LocalMessages.newProducer();
        
        UserService userService = new UserService(userRepo, userCache, eventBus);
        OrderService orderService = new OrderService(orderRepo, eventBus, messageProducer);
        NotificationService notificationService = new NotificationService();
        StatisticsService statisticsService = new StatisticsService();
        
        eventBus.subscribe("user.created", notificationService::onUserCreated);
        eventBus.subscribe("user.created", statisticsService::onUserCreated);
        eventBus.subscribe("order.created", notificationService::onOrderCreated);
        eventBus.subscribe("order.created", statisticsService::onOrderCreated);
        
        MessageConsumer consumer = LocalMessages.newConsumer();
        consumer.subscribe("order-queue", msg -> {
            logger.info("[订单处理] 收到消息: {}", msg.getBody());
        });
        
        container.registerSingleton(UserService.class, userService);
        container.registerSingleton(OrderService.class, orderService);
        container.registerSingleton(StatisticsService.class, statisticsService);
        container.registerSingleton(EventBus.class, eventBus);
        
        logger.info("所有服务初始化完成�?);
    }
    
    private static void demonstrateUserOperations(Container container) {
        System.out.println("\n--- 步骤 2: 演示用户操作 ---");
        
        UserService userService = container.get(UserService.class);
        
        System.out.println("\n2.1 创建用户（触发事件）");
        User user1 = userService.createUser("张三", "zhangsan@example.com");
        User user2 = userService.createUser("李四", "lisi@example.com");
        
        System.out.println("\n2.2 查询用户（第一次查数据库）");
        User queriedUser1 = userService.getUserById(user1.getId());
        System.out.println("   查询�? " + queriedUser1.getName());
        
        System.out.println("\n2.3 再次查询用户（命中缓存）");
        User cachedUser1 = userService.getUserById(user1.getId());
        System.out.println("   查询�? " + cachedUser1.getName());
        
        System.out.println("\n2.4 查询所有用�?);
        List<User> allUsers = userService.getAllUsers();
        System.out.println("   所有用户数: " + allUsers.size());
    }
    
    private static void demonstrateOrderOperations(Container container) {
        System.out.println("\n--- 步骤 3: 演示订单操作 ---");
        
        UserService userService = container.get(UserService.class);
        OrderService orderService = container.get(OrderService.class);
        StatisticsService statsService = container.get(StatisticsService.class);
        
        List<User> users = userService.getAllUsers();
        
        System.out.println("\n3.1 创建订单（触发事�?+ 消息队列�?);
        Order order1 = orderService.createOrder(users.get(0).getId(), "iPhone 15", 5999.0);
        Order order2 = orderService.createOrder(users.get(1).getId(), "MacBook Pro", 12999.0);
        
        System.out.println("\n3.2 查询所有订�?);
        List<Order> allOrders = orderService.getAllOrders();
        System.out.println("   所有订单数: " + allOrders.size());
        
        System.out.println("\n3.3 查看统计数据");
        System.out.println("   总用户数: " + statsService.getUserCount());
        System.out.println("   总订单数: " + statsService.getOrderCount());
    }
    
    private static void demonstrateScheduledTasks(Container container) throws InterruptedException {
        System.out.println("\n--- 步骤 4: 演示调度任务 ---");
        
        StatisticsService statsService = container.get(StatisticsService.class);
        
        System.out.println("\n4.1 创建调度�?);
        Scheduler scheduler = FixedSchedulers.create();
        
        Task statsTask = FixedSchedulers.wrap(() -> {
            logger.info("[定时任务] 当前统计 - 用户: {}, 订单: {}", 
                statsService.getUserCount(), statsService.getOrderCount());
        });
        
        ScheduleConfig config = ScheduleConfig.builder()
                .type(ScheduleType.FIXED_RATE)
                .initialDelay(1)
                .period(3)
                .timeUnit(TimeUnit.SECONDS)
                .build();
        
        scheduler.schedule(statsTask, config);
        scheduler.start();
        
        System.out.println("   调度器已启动，观�?7 �?..");
        Thread.sleep(7000);
        
        scheduler.stop();
        System.out.println("   调度器已停止");
    }
    
    private static void demonstrateMonitoring(Container container) {
        System.out.println("\n--- 步骤 5: 演示监控 ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        System.out.println("\n5.1 JVM 健康检�?);
        HealthCheckResult health = monitor.checkHealth();
        System.out.println("   健康状�? " + health.getHealthStatus());
        System.out.println("   状态消�? " + health.getMessage());
        
        System.out.println("\n5.2 JVM 运行时间");
        System.out.println("   JVM 已运�? " + monitor.getUptime() + "ms");
        
        Map<String, Object> metrics = monitor.getAllMetrics();
        System.out.println("\n5.3 JVM 指标（部分）:");
        int count = 0;
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            if (count++ < 5) {
                System.out.println("   " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("   ... 更多指标省略");
    }
}
