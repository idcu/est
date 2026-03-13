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
        users.put("1", new User("1", "Zhang San", "zhangsan@example.com", "admin"));
        users.put("2", new User("2", "Li Si", "lisi@example.com", "user"));
        users.put("3", new User("3", "Wang Wu", "wangwu@example.com", "user"));
        
        products.put("101", new Product("101", "Laptop", 5999.00, 100));
        products.put("102", new Product("102", "Wireless Mouse", 199.00, 500));
        products.put("103", new Product("103", "Mechanical Keyboard", 399.00, 300));
    }
    
    public static void run() {
        System.out.println("\n".repeat(2));
        System.out.println("=".repeat(80));
        System.out.println("Complete REST API Example - E-commerce Backend Management System");
        System.out.println("=".repeat(80));
        
        WebApplication app = Web.create("E-commerce Backend API", "1.0.0");
        
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
            System.out.println("\n[OK] REST API server started successfully!");
            System.out.println("\nAvailable API Endpoints:");
            System.out.println("\n[User Management]");
            System.out.println("  GET    /api/v1/users          - Get all users");
            System.out.println("  GET    /api/v1/users/:id      - Get single user");
            System.out.println("  POST   /api/v1/users          - Create user");
            System.out.println("  PUT    /api/v1/users/:id      - Update user");
            System.out.println("  DELETE /api/v1/users/:id      - Delete user");
            System.out.println("\n[Product Management]");
            System.out.println("  GET    /api/v1/products       - Get all products");
            System.out.println("  GET    /api/v1/products/:id   - Get single product");
            System.out.println("  POST   /api/v1/products       - Create product");
            System.out.println("  PUT    /api/v1/products/:id   - Update product");
            System.out.println("  DELETE /api/v1/products/:id   - Delete product");
            System.out.println("\n[Order Management]");
            System.out.println("  GET    /api/v1/orders         - Get all orders");
            System.out.println("  GET    /api/v1/orders/:id     - Get single order");
            System.out.println("  POST   /api/v1/orders         - Create order");
            System.out.println("  PUT    /api/v1/orders/:id/status - Update order status");
            System.out.println("\n[Statistics]");
            System.out.println("  GET    /api/v1/stats          - Get system statistics");
            System.out.println("\n[X] Ctrl+C to stop server");
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
            res.status(404).json(Map.of("success", false, "message", "User not found"));
        }
    }
    
    private static void createUser(Request req, Response res) {
        String name = req.formParam("name");
        String email = req.formParam("email");
        String role = req.formParam("role", "user");
        
        if (name == null || email == null) {
            res.status(400).json(Map.of("success", false, "message", "Name and email cannot be empty"));
            return;
        }
        
        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, role);
        users.put(id, user);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "User created successfully",
            "data", user
        ));
    }
    
    private static void updateUser(Request req, Response res) {
        String id = req.param("id");
        User user = users.get(id);
        
        if (user == null) {
            res.status(404).json(Map.of("success", false, "message", "User not found"));
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
            "message", "User updated successfully",
            "data", user
        ));
    }
    
    private static void deleteUser(Request req, Response res) {
        String id = req.param("id");
        if (users.remove(id) != null) {
            res.json(Map.of("success", true, "message", "User deleted successfully", "deletedId", id));
        } else {
            res.status(404).json(Map.of("success", false, "message", "User not found"));
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
            res.status(404).json(Map.of("success", false, "message", "Product not found"));
        }
    }
    
    private static void createProduct(Request req, Response res) {
        String name = req.formParam("name");
        String priceStr = req.formParam("price");
        String stockStr = req.formParam("stock");
        
        if (name == null || priceStr == null) {
            res.status(400).json(Map.of("success", false, "message", "Product name and price cannot be empty"));
            return;
        }
        
        double price = Double.parseDouble(priceStr);
        int stock = stockStr != null ? Integer.parseInt(stockStr) : 0;
        
        String id = UUID.randomUUID().toString();
        Product product = new Product(id, name, price, stock);
        products.put(id, product);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "Product created successfully",
            "data", product
        ));
    }
    
    private static void updateProduct(Request req, Response res) {
        String id = req.param("id");
        Product product = products.get(id);
        
        if (product == null) {
            res.status(404).json(Map.of("success", false, "message", "Product not found"));
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
            "message", "Product updated successfully",
            "data", product
        ));
    }
    
    private static void deleteProduct(Request req, Response res) {
        String id = req.param("id");
        if (products.remove(id) != null) {
            res.json(Map.of("success", true, "message", "Product deleted successfully", "deletedId", id));
        } else {
            res.status(404).json(Map.of("success", false, "message", "Product not found"));
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
            res.status(404).json(Map.of("success", false, "message", "Order not found"));
        }
    }
    
    private static void createOrder(Request req, Response res) {
        String userId = req.formParam("userId");
        String productId = req.formParam("productId");
        String quantityStr = req.formParam("quantity");
        
        if (userId == null || productId == null) {
            res.status(400).json(Map.of("success", false, "message", "User ID and product ID cannot be empty"));
            return;
        }
        
        User user = users.get(userId);
        Product product = products.get(productId);
        
        if (user == null) {
            res.status(404).json(Map.of("success", false, "message", "User not found"));
            return;
        }
        
        if (product == null) {
            res.status(404).json(Map.of("success", false, "message", "Product not found"));
            return;
        }
        
        int quantity = quantityStr != null ? Integer.parseInt(quantityStr) : 1;
        
        String id = UUID.randomUUID().toString();
        Order order = new Order(id, userId, productId, quantity, product.price * quantity, "pending");
        orders.put(id, order);
        
        res.status(201).json(Map.of(
            "success", true,
            "message", "Order created successfully",
            "data", order
        ));
    }
    
    private static void updateOrderStatus(Request req, Response res) {
        String id = req.param("id");
        Order order = orders.get(id);
        
        if (order == null) {
            res.status(404).json(Map.of("success", false, "message", "Order not found"));
            return;
        }
        
        String status = req.formParam("status");
        if (status != null) {
            order.status = status;
        }
        
        res.json(Map.of(
            "success", true,
            "message", "Order status updated successfully",
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
