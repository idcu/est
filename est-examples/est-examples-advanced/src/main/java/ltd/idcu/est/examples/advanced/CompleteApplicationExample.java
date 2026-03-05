package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.features.logging.api.Logger;
import ltd.idcu.est.features.logging.console.ConsoleLogs;
import ltd.idcu.est.features.cache.api.Cache;
import ltd.idcu.est.features.cache.memory.MemoryCache;
import ltd.idcu.est.features.event.api.EventBus;
import ltd.idcu.est.features.event.api.EventListener;
import ltd.idcu.est.features.event.local.LocalEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CompleteApplicationExample {
    
    private static final Logger logger = ConsoleLogs.getLogger(CompleteApplicationExample.class);
    private static final Container container = new DefaultContainer();
    private static final EventBus eventBus = new LocalEventBus();
    private static final Cache<String, User> userCache = new MemoryCache<>();
    private static final Map<String, Order> orderDatabase = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("EST 完整应用示例 - 电商订单系统（核心功能）");
        System.out.println("=".repeat(70));
        System.out.println("\n本示例展示了如何使用 EST 框架的核心企业级功能：");
        System.out.println("  - 依赖注入容器管理服务");
        System.out.println("  - 缓存系统提升性能");
        System.out.println("  - 事件总线实现组件解耦");
        System.out.println("  - 日志系统记录应用行为");
        System.out.println();
        
        initializeApplication();
        demonstrateUsage();
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    private static void initializeApplication() {
        logger.info("正在初始化应用...");
        
        System.out.println("\n--- 步骤 1: 注册服务到容器 ---");
        
        UserService userService = new UserService(userCache, eventBus);
        OrderService orderService = new OrderService(orderDatabase, eventBus, userCache);
        NotificationService notificationService = new NotificationService();
        AnalyticsService analyticsService = new AnalyticsService();
        
        container.registerSingleton(UserService.class, userService);
        container.registerSingleton(OrderService.class, orderService);
        
        System.out.println("   ✓ UserService 已注册");
        System.out.println("   ✓ OrderService 已注册");
        
        System.out.println("\n--- 步骤 2: 注册事件监听器 ---");
        
        eventBus.subscribe("user.registered", notificationService::onUserRegistered);
        eventBus.subscribe("user.registered", analyticsService::onUserRegistered);
        eventBus.subscribe("order.created", notificationService::onOrderCreated);
        eventBus.subscribe("order.created", analyticsService::onOrderCreated);
        
        System.out.println("   ✓ 用户注册监听器已注册");
        System.out.println("   ✓ 订单创建监听器已注册");
        
        System.out.println("\n--- 步骤 3: 初始化示例数据 ---");
        
        userService.createUser("张三", "zhangsan@example.com");
        userService.createUser("李四", "lisi@example.com");
        
        System.out.println("   ✓ 示例用户已创建");
        
        logger.info("应用初始化完成！");
    }
    
    private static void demonstrateUsage() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("开始演示应用功能");
        System.out.println("=".repeat(70));
        
        UserService userService = container.get(UserService.class);
        OrderService orderService = container.get(OrderService.class);
        
        System.out.println("\n--- 场景 1: 创建新用户 ---");
        User newUser = userService.createUser("王五", "wangwu@example.com");
        System.out.println("   新用户创建成功: " + newUser.getName() + " (" + newUser.getEmail() + ")");
        
        System.out.println("\n--- 场景 2: 查询用户 ---");
        User user = userService.getUserById(newUser.getId());
        if (user != null) {
            System.out.println("   查询到用户: " + user.getName());
        }
        
        System.out.println("\n--- 场景 3: 获取所有用户 ---");
        List<User> allUsers = userService.getAllUsers();
        System.out.println("   所有用户数量: " + allUsers.size());
        for (User u : allUsers) {
            System.out.println("     - " + u.getName());
        }
        
        System.out.println("\n--- 场景 4: 创建订单 ---");
        Order order = orderService.createOrder(newUser.getId(), "EST 框架教程", 99);
        System.out.println("   订单创建成功: " + order.getId());
        System.out.println("   商品: " + order.getProduct());
        System.out.println("   金额: " + order.getAmount());
        
        System.out.println("\n--- 场景 5: 查询订单 ---");
        Order queriedOrder = orderService.getOrderById(order.getId());
        if (queriedOrder != null) {
            System.out.println("   查询到订单: " + queriedOrder.getProduct());
        }
        
        System.out.println("\n--- 场景 6: 获取所有订单 ---");
        List<Order> allOrders = orderService.getAllOrders();
        System.out.println("   所有订单数量: " + allOrders.size());
        
        System.out.println("\n--- 场景 7: 显示统计信息 ---");
        System.out.println("   总用户数: " + userService.getUserCount());
        System.out.println("   总订单数: " + orderService.getOrderCount());
        System.out.println("   缓存大小: " + userCache.size());
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("✓ 所有功能演示完成！");
        System.out.println("=".repeat(70));
    }
}

class User {
    private final String id;
    private final String name;
    private final String email;
    private final long createdAt;
    
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public long getCreatedAt() { return createdAt; }
}

class Order {
    private final String id;
    private final String userId;
    private final String product;
    private final int amount;
    private final String status;
    private final long createdAt;
    
    public Order(String id, String userId, String product, int amount) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.amount = amount;
        this.status = "PENDING";
        this.createdAt = System.currentTimeMillis();
    }
    
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getProduct() { return product; }
    public int getAmount() { return amount; }
    public String getStatus() { return status; }
    public long getCreatedAt() { return createdAt; }
}

class UserService {
    private final Cache<String, User> userCache;
    private final EventBus eventBus;
    private static final Logger logger = ConsoleLogs.getLogger(UserService.class);
    
    public UserService(Cache<String, User> userCache, EventBus eventBus) {
        this.userCache = userCache;
        this.eventBus = eventBus;
    }
    
    public User createUser(String name, String email) {
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email);
        userCache.put(id, user);
        logger.info("创建新用户: {} ({})", name, id);
        eventBus.publish("user.registered", user);
        return user;
    }
    
    public User getUserById(String id) {
        return userCache.get(id).orElse(null);
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        return users;
    }
    
    public int getUserCount() {
        return userCache.size();
    }
}

class OrderService {
    private final Map<String, Order> orderDatabase;
    private final EventBus eventBus;
    private final Cache<String, User> userCache;
    private static final Logger logger = ConsoleLogs.getLogger(OrderService.class);
    
    public OrderService(Map<String, Order> orderDatabase, EventBus eventBus, Cache<String, User> userCache) {
        this.orderDatabase = orderDatabase;
        this.eventBus = eventBus;
        this.userCache = userCache;
    }
    
    public Order createOrder(String userId, String product, int amount) {
        String id = UUID.randomUUID().toString();
        Order order = new Order(id, userId, product, amount);
        orderDatabase.put(id, order);
        
        User user = userCache.get(userId).orElse(null);
        logger.info("创建新订单: {} - 用户: {} - 商品: {} - 金额: {}", 
            id, user != null ? user.getName() : userId, product, amount);
        
        eventBus.publish("order.created", order);
        return order;
    }
    
    public Order getOrderById(String id) {
        return orderDatabase.get(id);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orderDatabase.values());
    }
    
    public int getOrderCount() {
        return orderDatabase.size();
    }
}

class NotificationService {
    private static final Logger logger = ConsoleLogs.getLogger(NotificationService.class);
    
    public void onUserRegistered(User user) {
        logger.info("[通知服务] 向 {} 发送欢迎邮件到 {}", user.getName(), user.getEmail());
    }
    
    public void onOrderCreated(Order order) {
        logger.info("[通知服务] 向用户 {} 发送订单确认通知，订单号: {}", 
            order.getUserId(), order.getId());
    }
}

class AnalyticsService {
    private static final Logger logger = ConsoleLogs.getLogger(AnalyticsService.class);
    private int userCount = 0;
    private int orderCount = 0;
    
    public void onUserRegistered(User user) {
        userCount++;
        logger.info("[分析服务] 记录用户注册事件，累计用户: {}", userCount);
    }
    
    public void onOrderCreated(Order order) {
        orderCount++;
        logger.info("[分析服务] 记录订单创建事件，累计订单: {}", orderCount);
    }
}
