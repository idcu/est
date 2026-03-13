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
        System.out.println("EST Framework - Module Integration Comprehensive Example");
        System.out.println("=".repeat(80));
        System.out.println("\nThis example demonstrates how EST Framework modules work together:");
        System.out.println("  [X] Dependency Injection Container - manages all services");
        System.out.println("  [X] Data Access - stores users and orders");
        System.out.println("  [X] Cache System - accelerates hot data access");
        System.out.println("  [X] Event Bus - decoupled communication between components");
        System.out.println("  [X] Logging System - records all operations");
        System.out.println("  [X] Scheduler System - executes tasks periodically");
        System.out.println("  [X] Monitor System - monitors application status");
        System.out.println("  [X] Message System - processes orders asynchronously");
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("Architecture Diagram:");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("    +---------------------------------------------------+");
        System.out.println("    |         Web Request / User Operation             |");
        System.out.println("    +-------------------+-------------------------------+");
        System.out.println("                        |");
        System.out.println("                        |");
        System.out.println("    +---------------------------------------------------+");
        System.out.println("    |      Dependency Injection Container             |");
        System.out.println("    |  Manages lifecycle of all services            |");
        System.out.println("    +---------------+-----------------------------------+");
        System.out.println("                    |");
        System.out.println("         +----------+----------+");
        System.out.println("         |          |          |");
        System.out.println("         |          |          |");
        System.out.println("    +----v----+ +----v----+ +----v----+");
        System.out.println("    | Data    | |  Cache  | |  Event  |");
        System.out.println("    |Repo     | |  Cache  | | EventBus|");
        System.out.println("    +----+----+ +----+----+ +----+----+");
        System.out.println("         |          |          |");
        System.out.println("         +----------+----------+");
        System.out.println("                    |");
        System.out.println("         +----------+----------+");
        System.out.println("         |          |          |");
        System.out.println("         |          |          |");
        System.out.println("    +----v----+ +----v----+ +----v----+");
        System.out.println("    |Scheduler| | Monitor | |Messaging|");
        System.out.println("    |Scheduler| | Monitor | |Messaging|");
        System.out.println("    +---------+ +---------+ +---------+");
        System.out.println("                    |");
        System.out.println("                    |");
        System.out.println("    +---------------------------------------------------+");
        System.out.println("    |          Logging System                         |");
        System.out.println("    |     Records all operations and events          |");
        System.out.println("    +---------------------------------------------------+");
        System.out.println();
        
        System.out.println("=".repeat(80));
        System.out.println("Starting demonstration...");
        System.out.println("=".repeat(80));
        
        Container container = new DefaultContainer();
        initializeServices(container);
        
        demonstrateUserOperations(container);
        demonstrateOrderOperations(container);
        demonstrateScheduledTasks(container);
        demonstrateMonitoring(container);
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("[X] All module integration demonstration complete!");
        System.out.println("=".repeat(80));
        System.out.println("\nSummary:");
        System.out.println("  1. Container manages all services with easy DI");
        System.out.println("  2. Data access handles persistence, cache handles acceleration");
        System.out.println("  3. Event bus decouples components, message system handles async");
        System.out.println("  4. Scheduler handles periodic tasks, monitor ensures stability");
        System.out.println("  5. Logging system records everything for easy troubleshooting");
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
            logger.info("Creating user: {}", name);
            User user = new User(name, email);
            userRepo.save(user);
            userCache.put(user.getId(), user);
            eventBus.publish("user.created", user);
            logger.info("User created successfully, ID: {}", user.getId());
            return user;
        }
        
        public User getUserById(Long id) {
            logger.debug("Querying user: {}", id);
            Optional<User> cached = userCache.get(id);
            if (cached.isPresent()) {
                logger.info("Cache hit");
                return cached.get();
            }
            logger.info("Cache miss, querying database...");
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
            logger.info("Creating order: user={}, product={}, amount={}", userId, product, amount);
            Order order = new Order(userId, product, amount);
            orderRepo.save(order);
            eventBus.publish("order.created", order);
            messageProducer.send("order-queue", "Order #" + order.getId() + " pending");
            logger.info("Order created successfully, ID: {}", order.getId());
            return order;
        }
        
        public List<Order> getAllOrders() {
            return orderRepo.findAll();
        }
    }
    
    public static class NotificationService {
        public void onUserCreated(User user) {
            logger.info("[Notification] Sending welcome email to {}", user.getEmail());
        }
        
        public void onOrderCreated(Order order) {
            logger.info("[Notification] Sending order confirmation to user {}", order.getUserId());
        }
    }
    
    public static class StatisticsService {
        private int userCount = 0;
        private int orderCount = 0;
        
        public void onUserCreated(User user) {
            userCount++;
            logger.info("[Statistics] User count: {}", userCount);
        }
        
        public void onOrderCreated(Order order) {
            orderCount++;
            logger.info("[Statistics] Order count: {}", orderCount);
        }
        
        public int getUserCount() { return userCount; }
        public int getOrderCount() { return orderCount; }
    }
    
    private static void initializeServices(Container container) {
        System.out.println("\n--- Step 1: Initialize all services ---");
        
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
            logger.info("[Order Processing] Received message: {}", msg.getBody());
        });
        
        container.registerSingleton(UserService.class, userService);
        container.registerSingleton(OrderService.class, orderService);
        container.registerSingleton(StatisticsService.class, statisticsService);
        container.registerSingleton(EventBus.class, eventBus);
        
        logger.info("All services initialized successfully");
    }
    
    private static void demonstrateUserOperations(Container container) {
        System.out.println("\n--- Step 2: Demonstrate user operations ---");
        
        UserService userService = container.get(UserService.class);
        
        System.out.println("\n2.1 Create users (triggers events)");
        User user1 = userService.createUser("ZhangSan", "zhangsan@example.com");
        User user2 = userService.createUser("LiSi", "lisi@example.com");
        
        System.out.println("\n2.2 Query user (first time from database)");
        User queriedUser1 = userService.getUserById(user1.getId());
        System.out.println("   Query result: " + queriedUser1.getName());
        
        System.out.println("\n2.3 Query user again (cache hit)");
        User cachedUser1 = userService.getUserById(user1.getId());
        System.out.println("   Query result: " + cachedUser1.getName());
        
        System.out.println("\n2.4 Query all users");
        List<User> allUsers = userService.getAllUsers();
        System.out.println("   Total users: " + allUsers.size());
    }
    
    private static void demonstrateOrderOperations(Container container) {
        System.out.println("\n--- Step 3: Demonstrate order operations ---");
        
        UserService userService = container.get(UserService.class);
        OrderService orderService = container.get(OrderService.class);
        StatisticsService statsService = container.get(StatisticsService.class);
        
        List<User> users = userService.getAllUsers();
        
        System.out.println("\n3.1 Create orders (triggers events + message queue)");
        Order order1 = orderService.createOrder(users.get(0).getId(), "iPhone 15", 5999.0);
        Order order2 = orderService.createOrder(users.get(1).getId(), "MacBook Pro", 12999.0);
        
        System.out.println("\n3.2 Query all orders");
        List<Order> allOrders = orderService.getAllOrders();
        System.out.println("   Total orders: " + allOrders.size());
        
        System.out.println("\n3.3 View statistics");
        System.out.println("   Total users: " + statsService.getUserCount());
        System.out.println("   Total orders: " + statsService.getOrderCount());
    }
    
    private static void demonstrateScheduledTasks(Container container) throws InterruptedException {
        System.out.println("\n--- Step 4: Demonstrate scheduled tasks ---");
        
        StatisticsService statsService = container.get(StatisticsService.class);
        
        System.out.println("\n4.1 Create scheduler");
        Scheduler scheduler = FixedSchedulers.create();
        
        Task statsTask = FixedSchedulers.wrap(() -> {
            logger.info("[Scheduled Task] Current stats - users: {}, orders: {}", 
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
        
        System.out.println("   Scheduler started, observing for 7 seconds...");
        Thread.sleep(7000);
        
        scheduler.stop();
        System.out.println("   Scheduler stopped");
    }
    
    private static void demonstrateMonitoring(Container container) {
        System.out.println("\n--- Step 5: Demonstrate monitoring ---");
        
        JvmMonitor monitor = JvmMonitor.getInstance();
        
        System.out.println("\n5.1 JVM health check");
        HealthCheckResult health = monitor.checkHealth();
        System.out.println("   Health status: " + health.getHealthStatus());
        System.out.println("   Status message: " + health.getMessage());
        
        System.out.println("\n5.2 JVM uptime");
        System.out.println("   JVM uptime: " + monitor.getUptime() + "ms");
        
        Map<String, Object> metrics = monitor.getAllMetrics();
        System.out.println("\n5.3 JVM metrics (partial):");
        int count = 0;
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            if (count++ < 5) {
                System.out.println("   " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("   ... more metrics omitted");
    }
}
