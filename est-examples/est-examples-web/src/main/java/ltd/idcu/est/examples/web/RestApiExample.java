package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RestApiExample {
    
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    private static final Map<String, Product> products = new ConcurrentHashMap<>();
    private static final Map<String, Order> orders = new ConcurrentHashMap<>();
    
    static {
        users.put("1", new User("1", "张三", "zhangsan@example.com", "admin"));
        users.put("2", new User("2", "李四", "lisi@example.com", "user"));
        users.put("3", new User("3", "王五", "wangwu@example.com", "user"));
        
        products.put("101", new Product("101", "笔记本电脑", 5999.00, 100));
        products.put("102", new Product("102", "无线鼠标", 199.00, 500));
        products.put("103", new Product("103", "机械键盘", 399.00, 300));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("REST API 完整示例 - 电商后台管理系统");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("电商后台 API", "1.0.0");
        
        app.enableCors();
        
        app.routes(router -> {
            router.group("/api/v1", (r, group) -> {
                
                r.group("/users", (userRouter, userGroup) -> {
                    userRouter.get("", RestApiExample::listUsers);
                    userRouter.get("/:id", RestApiExample::getUser);
                    userRouter.post("", RestApiExample::createUser);
                    userRouter.put("/:id", RestApiExample::updateUser);
                    userRouter.delete("/:id", RestApiExample::deleteUser);
                });
                
                r.group("/products", (productRouter, productGroup) -> {
                    productRouter.get("", RestApiExample::listProducts);
                    productRouter.get("/:id", RestApiExample::getProduct);
                    productRouter.post("", RestApiExample::createProduct);
                    productRouter.put("/:id", RestApiExample::updateProduct);
                    productRouter.delete("/:id", RestApiExample::deleteProduct);
                });
                
                r.group("/orders", (orderRouter, orderGroup) -> {
                    orderRouter.get("", RestApiExample::listOrders);
                    orderRouter.get("/:id", RestApiExample::getOrder);
                    orderRouter.post("", RestApiExample::createOrder);
                    orderRouter.put("/:id/status", RestApiExample::updateOrderStatus);
                });
                
                r.get("/stats", RestApiExample::getStats);
            });
        });
        
        app.onStartup(() -> {
            System.out.println("\n✓ REST API 服务器启动成功！");
            System.out.println("\n可用的 API 端点：");
            System.out.println("\n【用户管理】");
            System.out.println("  GET    /api/v1/users          - 获取所有用户");
            System.out.println("  GET    /api/v1/users/:id      - 获取单个用户");
            System.out.println("  POST   /api/v1/users          - 创建用户");
            System.out.println("  PUT    /api/v1/users/:id      - 更新用户");
            System.out.println("  DELETE /api/v1/users/:id      - 删除用户");
            System.out.println("\n【商品管理】");
            System.out.println("  GET    /api/v1/products       - 获取所有商品");
            System.out.println("  GET    /api/v1/products/:id   - 获取单个商品");
            System.out.println("  POST   /api/v1/products       - 创建商品");
            System.out.println("  PUT    /api/v1/products/:id   - 更新商品");
            System.out.println("  DELETE /api/v1/products/:id   - 删除商品");
            System.out.println("\n【订单管理】");
            System.out.println("  GET    /api/v1/orders         - 获取所有订单");
            System.out.println("  GET    /api/v1/orders/:id     - 获取单个订单");
            System.out.println("  POST   /api/v1/orders         - 创建订单");
            System.out.println("  PUT    /api/v1/orders/:id/status - 更新订单状态");
            System.out.println("\n【统计数据】");
            System.out.println("  GET    /api/v1/stats          - 获取系统统计数据");
            System.out.println("\n按 Ctrl+C 停止服务器");
            System.out.println("=".repeat(80));
        });
        
        app.run(8080);
    }
    
    public static void main(String[] args) {
        run();
    }
    
    private static void listUsers(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", users.values(),
            "total", users.size()
        ));
    }
    
    private static void getUser(Request req, Response res) {
        String id = req.param("id");
        User user = users.get(id);
        if (user != null) {
            res.json(Map.of("success", true, "data", user));
        } else {
            res.status(404).json(Map.of("success", false, "message", "用户不存在"));
        }
    }
    
    private static void createUser(Request req, Response res) {
        String name = req.formParam("name");
        String email = req.formParam("email");
        String role = req.formParam("role", "user");
        
        if (name == null || email == null) {
            res.status(400).json(Map.of("success", false, "message", "姓名和邮箱不能为空"));
            return;
        }
        
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, role);
        users.put(id, user);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "用户创建成功",
            "data", user
        ));
    }
    
    private static void updateUser(Request req, Response res) {
        String id = req.param("id");
        User user = users.get(id);
        
        if (user == null) {
            res.status(404).json(Map.of("success", false, "message", "用户不存在"));
            return;
        }
        
        String name = req.formParam("name");
        String email = req.formParam("email");
        String role = req.formParam("role");
        
        if (name != null) user.name = name;
        if (email != null) user.email = email;
        if (role != null) user.role = role;
        
        res.json(Map.of(
            "success", true,
            "message", "用户更新成功",
            "data", user
        ));
    }
    
    private static void deleteUser(Request req, Response res) {
        String id = req.param("id");
        if (users.remove(id) != null) {
            res.json(Map.of("success", true, "message", "用户删除成功", "deletedId", id));
        } else {
            res.status(404).json(Map.of("success", false, "message", "用户不存在"));
        }
    }
    
    private static void listProducts(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", products.values(),
            "total", products.size()
        ));
    }
    
    private static void getProduct(Request req, Response res) {
        String id = req.param("id");
        Product product = products.get(id);
        if (product != null) {
            res.json(Map.of("success", true, "data", product));
        } else {
            res.status(404).json(Map.of("success", false, "message", "商品不存在"));
        }
    }
    
    private static void createProduct(Request req, Response res) {
        String name = req.formParam("name");
        String priceStr = req.formParam("price");
        String stockStr = req.formParam("stock");
        
        if (name == null || priceStr == null) {
            res.status(400).json(Map.of("success", false, "message", "商品名称和价格不能为空"));
            return;
        }
        
        double price = Double.parseDouble(priceStr);
        int stock = stockStr != null ? Integer.parseInt(stockStr) : 0;
        
        String id = UUID.randomUUID().toString();
        Product product = new Product(id, name, price, stock);
        products.put(id, product);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "商品创建成功",
            "data", product
        ));
    }
    
    private static void updateProduct(Request req, Response res) {
        String id = req.param("id");
        Product product = products.get(id);
        
        if (product == null) {
            res.status(404).json(Map.of("success", false, "message", "商品不存在"));
            return;
        }
        
        String name = req.formParam("name");
        String priceStr = req.formParam("price");
        String stockStr = req.formParam("stock");
        
        if (name != null) product.name = name;
        if (priceStr != null) product.price = Double.parseDouble(priceStr);
        if (stockStr != null) product.stock = Integer.parseInt(stockStr);
        
        res.json(Map.of(
            "success", true,
            "message", "商品更新成功",
            "data", product
        ));
    }
    
    private static void deleteProduct(Request req, Response res) {
        String id = req.param("id");
        if (products.remove(id) != null) {
            res.json(Map.of("success", true, "message", "商品删除成功", "deletedId", id));
        } else {
            res.status(404).json(Map.of("success", false, "message", "商品不存在"));
        }
    }
    
    private static void listOrders(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", orders.values(),
            "total", orders.size()
        ));
    }
    
    private static void getOrder(Request req, Response res) {
        String id = req.param("id");
        Order order = orders.get(id);
        if (order != null) {
            res.json(Map.of("success", true, "data", order));
        } else {
            res.status(404).json(Map.of("success", false, "message", "订单不存在"));
        }
    }
    
    private static void createOrder(Request req, Response res) {
        String userId = req.formParam("userId");
        String productId = req.formParam("productId");
        String quantityStr = req.formParam("quantity");
        
        if (userId == null || productId == null) {
            res.status(400).json(Map.of("success", false, "message", "用户ID和商品ID不能为空"));
            return;
        }
        
        User user = users.get(userId);
        Product product = products.get(productId);
        
        if (user == null) {
            res.status(404).json(Map.of("success", false, "message", "用户不存在"));
            return;
        }
        
        if (product == null) {
            res.status(404).json(Map.of("success", false, "message", "商品不存在"));
            return;
        }
        
        int quantity = quantityStr != null ? Integer.parseInt(quantityStr) : 1;
        
        String id = UUID.randomUUID().toString();
        Order order = new Order(id, userId, productId, quantity, product.price * quantity, "pending");
        orders.put(id, order);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "订单创建成功",
            "data", order
        ));
    }
    
    private static void updateOrderStatus(Request req, Response res) {
        String id = req.param("id");
        Order order = orders.get(id);
        
        if (order == null) {
            res.status(404).json(Map.of("success", false, "message", "订单不存在"));
            return;
        }
        
        String status = req.formParam("status");
        if (status != null) {
            order.status = status;
        }
        
        res.json(Map.of(
            "success", true,
            "message", "订单状态更新成功",
            "data", order
        ));
    }
    
    private static void getStats(Request req, Response res) {
        double totalRevenue = orders.values().stream()
            .filter(o -> "completed".equals(o.status))
            .mapToDouble(o -> o.totalAmount)
            .sum();
        
        res.json(Map.of(
            "success", true,
            "data", Map.of(
                "users", users.size(),
                "products", products.size(),
                "orders", orders.size(),
                "totalRevenue", totalRevenue
            )
        ));
    }
    
    static class User {
        public String id;
        public String name;
        public String email;
        public String role;
        
        public User(String id, String name, String email, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }
    
    static class Product {
        public String id;
        public String name;
        public double price;
        public int stock;
        
        public Product(String id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }
    
    static class Order {
        public String id;
        public String userId;
        public String productId;
        public int quantity;
        public double totalAmount;
        public String status;
        public long createdAt;
        
        public Order(String id, String userId, String productId, int quantity, double totalAmount, String status) {
            this.id = id;
            this.userId = userId;
            this.productId = productId;
            this.quantity = quantity;
            this.totalAmount = totalAmount;
            this.status = status;
            this.createdAt = System.currentTimeMillis();
        }
    }
}

