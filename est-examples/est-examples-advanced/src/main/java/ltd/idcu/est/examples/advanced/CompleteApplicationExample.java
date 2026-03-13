package ltd.idcu.est.examples.advanced;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.api.EventListener;
import ltd.idcu.est.event.local.LocalEventBus;

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
        System.out.println("EST Complete Application Example - E-commerce Order System (Core Features)");
        System.out.println("=".repeat(70));
        System.out.println("\nThis example demonstrates how to use EST Framework's core enterprise features:");
        System.out.println("  - Dependency injection container for service management");
        System.out.println("  - Cache system for performance improvement");
        System.out.println("  - Event bus for component decoupling");
        System.out.println("  - Logging system for application behavior tracking");
        System.out.println();
        
        initializeApplication();
        demonstrateUsage();
    }
    
    public static void run() {
        main(new String[]{});
    }
    
    private static void initializeApplication() {
        logger.info("Initializing application...");
        
        System.out.println("\n--- Step 1: Register services to container ---");
        
        UserService userService = new UserService(userCache, eventBus);
        OrderService orderService = new OrderService(orderDatabase, eventBus, userCache);
        NotificationService notificationService = new NotificationService();
        AnalyticsService analyticsService = new AnalyticsService();
        
        container.registerSingleton(UserService.class, userService);
        container.registerSingleton(OrderService.class, orderService);
        
        System.out.println("   [OK] UserService registered");
        System.out.println("   [OK] OrderService registered");
        
        System.out.println("\n--- Step 2: Register event listeners ---");
        
        eventBus.subscribe("user.registered", notificationService::onUserRegistered);
        eventBus.subscribe("user.registered", analyticsService::onUserRegistered);
        eventBus.subscribe("order.created", notificationService::onOrderCreated);
        eventBus.subscribe("order.created", analyticsService::onOrderCreated);
        
        System.out.println("   [OK] User registration listeners registered");
        System.out.println("   [OK] Order creation listeners registered");
        
        System.out.println("\n--- Step 3: Initialize sample data ---");
        
        userService.createUser("ZhangSan", "zhangsan@example.com");
        userService.createUser("LiSi", "lisi@example.com");
        
        System.out.println("   [OK] Sample users created");
        
        logger.info("Application initialization complete!");
    }
    
    private static void demonstrateUsage() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("Starting application feature demonstration");
        System.out.println("=".repeat(70));
        
        UserService userService = container.get(UserService.class);
        OrderService orderService = container.get(OrderService.class);
        
        System.out.println("\n--- Scenario 1: Create new user ---");
        User newUser = userService.createUser("WangWu", "wangwu@example.com");
        System.out.println("   New user created: " + newUser.getName() + " (" + newUser.getEmail() + ")");
        
        System.out.println("\n--- Scenario 2: Query user ---");
        User user = userService.getUserById(newUser.getId());
        if (user != null) {
            System.out.println("   User found: " + user.getName());
        }
        
        System.out.println("\n--- Scenario 3: Get all users ---");
        List<User> allUsers = userService.getAllUsers();
        System.out.println("   Total users: " + allUsers.size());
        for (User u : allUsers) {
            System.out.println("     - " + u.getName());
        }
        
        System.out.println("\n--- Scenario 4: Create order ---");
        Order order = orderService.createOrder(newUser.getId(), "EST Framework Tutorial", 99);
        System.out.println("   Order created successfully: " + order.getId());
        System.out.println("   Product: " + order.getProduct());
        System.out.println("   Amount: " + order.getAmount());
        
        System.out.println("\n--- Scenario 5: Query order ---");
        Order queriedOrder = orderService.getOrderById(order.getId());
        if (queriedOrder != null) {
            System.out.println("   Order found: " + queriedOrder.getProduct());
        }
        
        System.out.println("\n--- Scenario 6: Get all orders ---");
        List<Order> allOrders = orderService.getAllOrders();
        System.out.println("   Total orders: " + allOrders.size());
        
        System.out.println("\n--- Scenario 7: Display statistics ---");
        System.out.println("   Total users: " + userService.getUserCount());
        System.out.println("   Total orders: " + orderService.getOrderCount());
        System.out.println("   Cache size: " + userCache.size());
        
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(70));
        System.out.println("[OK] All feature demonstrations completed!");
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
        logger.info("Creating new user: {} ({})", name, id);
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
        logger.info("Creating new order: {} - User: {} - Product: {} - Amount: {}", 
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
        logger.info("[Notification Service] Sending welcome email to {} at {}", user.getName(), user.getEmail());
    }
    
    public void onOrderCreated(Order order) {
        logger.info("[Notification Service] Sending order confirmation to user {}, order ID: {}", 
            order.getUserId(), order.getId());
    }
}

class AnalyticsService {
    private static final Logger logger = ConsoleLogs.getLogger(AnalyticsService.class);
    private int userCount = 0;
    private int orderCount = 0;
    
    public void onUserRegistered(User user) {
        userCount++;
        logger.info("[Analytics Service] Recording user registration event, total users: {}", userCount);
    }
    
    public void onOrderCreated(Order order) {
        orderCount++;
        logger.info("[Analytics Service] Recording order creation event, total orders: {}", orderCount);
    }
}
