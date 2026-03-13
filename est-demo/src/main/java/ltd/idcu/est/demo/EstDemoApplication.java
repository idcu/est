package ltd.idcu.est.demo;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.logging.file.FileLogs;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.cache.file.FileCache;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEventBus;
import ltd.idcu.est.event.async.AsyncEventBus;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.api.Task;
import ltd.idcu.est.scheduler.fixed.FixedRateScheduler;
import ltd.idcu.est.scheduler.fixed.FixedRateSchedulers;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.system.SystemMonitor;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryRepository;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class EstDemoApplication {

    private static final Logger consoleLogger = ConsoleLogs.getLogger(EstDemoApplication.class);
    private static final Logger fileLogger = FileLogs.getLogger("est-demo.log", new File("est-demo.log"));
    
    private static final MemoryCache<String, Object> memoryCache = new MemoryCache<>();
    private static final FileCache fileCache = new FileCache("est-demo-cache");
    
    private static final EventBus localEventBus = new LocalEventBus();
    private static final EventBus asyncEventBus = new AsyncEventBus();
    
    private static final Scheduler scheduler = FixedRateSchedulers.create();
    
    private static final JvmMonitor jvmMonitor = JvmMonitor.getInstance();
    private static final SystemMonitor systemMonitor = SystemMonitor.getInstance();
    
    private static final Repository<User, String> userRepository = MemoryRepository.<User, String>builder()
        .idExtractor(User::getId)
        .build();
    
    private static final Map<String, Todo> todos = new ConcurrentHashMap<>();
    private static final Map<String, Product> products = new ConcurrentHashMap<>();
    private static final List<LogEntry> logEntries = Collections.synchronizedList(new ArrayList<>());
    
    private static final AtomicInteger requestCount = new AtomicInteger(0);
    private static final AtomicLong totalRequestTime = new AtomicLong(0);

    static {
        userRepository.save(new User("1", "Alice", "alice@example.com", "admin"));
        userRepository.save(new User("2", "Bob", "bob@example.com", "user"));
        userRepository.save(new User("3", "Charlie", "charlie@example.com", "user"));
        
        todos.put("1", new Todo("1", "学习EST框架", "了解EST框架的核心功能", false, "high"));
        todos.put("2", new Todo("2", "创建第一个Web应用", "使用EST创建一个简单的Web应用", true, "medium"));
        todos.put("3", new Todo("3", "探索更多功能", "了解EST的其他功能模块", false, "low"));
        
        products.put("1", new Product("1", "EST Framework Pro", "企业级框架授权", 999.0, 100));
        products.put("2", new Product("2", "EST AI Suite", "AI工具套件", 499.0, 50));
        products.put("3", new Product("3", "EST Training", "培训课程", 199.0, 200));
        
        localEventBus.subscribe("user.created", (event, data) -> {
            if (data instanceof UserCreatedEvent userEvent) {
                consoleLogger.info("收到用户创建事件: {}", userEvent.getUsername());
                fileLogger.info("用户 {} 创建成功", userEvent.getUsername());
                addLogEntry("USER_CREATED", "User created: " + userEvent.getUsername());
            }
        });
        
        localEventBus.subscribe("product.event", (event, data) -> {
            if (data instanceof ProductEvent productEvent) {
                consoleLogger.info("收到产品事件: {} - {}", productEvent.getAction(), productEvent.getProductName());
                addLogEntry("PRODUCT_" + productEvent.getAction().toUpperCase(), "Product " + productEvent.getAction() + ": " + productEvent.getProductName());
            }
        });
        
        asyncEventBus.subscribe("task.event", (event, data) -> {
            if (data instanceof TaskEvent taskEvent) {
                consoleLogger.info("异步处理任务: {}", taskEvent.getTaskName());
                addLogEntry("TASK_STARTED", "Task started: " + taskEvent.getTaskName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                consoleLogger.info("任务 {} 处理完成", taskEvent.getTaskName());
                addLogEntry("TASK_COMPLETED", "Task completed: " + taskEvent.getTaskName());
            }
        });
        
        Task logTask = FixedRateSchedulers.wrap(() -> {
            consoleLogger.debug("定时任务执行 - 当前请求数: {}", requestCount.get());
        });
        scheduler.scheduleAtFixedRate(logTask, 10, 30, TimeUnit.SECONDS);
        
        memoryCache.put("system:startup", System.currentTimeMillis());
        addLogEntry("SYSTEM_STARTUP", "EST Demo Application started");
    }
    
    private static void addLogEntry(String type, String message) {
        LogEntry entry = new LogEntry(UUID.randomUUID().toString(), type, message, System.currentTimeMillis());
        logEntries.add(entry);
        if (logEntries.size() > 100) {
            logEntries.remove(0);
        }
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Framework Demo Application");
        System.out.println("  Enhanced with more features!");
        System.out.println("========================================");
        System.out.println();

        WebApplication app = Web.create("EST Demo", "2.1.0");

        app.use(new Middleware() {
            @Override
            public String getName() {
                return "request-logger";
            }

            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public boolean before(Request req, Response res) {
                requestCount.incrementAndGet();
                long startTime = System.currentTimeMillis();
                req.setAttribute("startTime", startTime);
                consoleLogger.info("Request: {} {}", req.getMethod(), req.getPath());
                return true;
            }

            @Override
            public void after(Request req, Response res) {
                Long startTime = (Long) req.getAttribute("startTime");
                if (startTime != null) {
                    long duration = System.currentTimeMillis() - startTime;
                    totalRequestTime.addAndGet(duration);
                    consoleLogger.info("Response: {} {} - {}ms", req.getMethod(), req.getPath(), duration);
                }
            }

            @Override
            public boolean isGlobal() {
                return true;
            }
        });

        app.routes(router -> {
            router.get("/", EstDemoApplication::homePage);
            
            router.group("/api", (apiRouter, apiGroup) -> {
                apiRouter.group("/users", (userRouter, userGroup) -> {
                    userRouter.get("", EstDemoApplication::listUsers);
                    userRouter.get("/:id", EstDemoApplication::getUser);
                    userRouter.post("", EstDemoApplication::createUser);
                    userRouter.put("/:id", EstDemoApplication::updateUser);
                    userRouter.delete("/:id", EstDemoApplication::deleteUser);
                });
                
                apiRouter.group("/todos", (todoRouter, todoGroup) -> {
                    todoRouter.get("", EstDemoApplication::listTodos);
                    todoRouter.get("/:id", EstDemoApplication::getTodo);
                    todoRouter.post("", EstDemoApplication::createTodo);
                    todoRouter.put("/:id", EstDemoApplication::updateTodo);
                    todoRouter.delete("/:id", EstDemoApplication::deleteTodo);
                    todoRouter.patch("/:id/complete", EstDemoApplication::toggleComplete);
                });
                
                apiRouter.group("/products", (productRouter, productGroup) -> {
                    productRouter.get("", EstDemoApplication::listProducts);
                    productRouter.get("/:id", EstDemoApplication::getProduct);
                    productRouter.post("", EstDemoApplication::createProduct);
                    productRouter.put("/:id", EstDemoApplication::updateProduct);
                    productRouter.delete("/:id", EstDemoApplication::deleteProduct);
                });
                
                apiRouter.group("/cache", (cacheRouter, cacheGroup) -> {
                    cacheRouter.get("/memory/:key", EstDemoApplication::getMemoryCache);
                    cacheRouter.put("/memory/:key", EstDemoApplication::setMemoryCache);
                    cacheRouter.delete("/memory/:key", EstDemoApplication::deleteMemoryCache);
                    cacheRouter.get("/memory", EstDemoApplication::listMemoryCache);
                    cacheRouter.get("/file/:key", EstDemoApplication::getFileCache);
                    cacheRouter.put("/file/:key", EstDemoApplication::setFileCache);
                });
                
                apiRouter.group("/events", (eventRouter, eventGroup) -> {
                    eventRouter.post("/local", EstDemoApplication::publishLocalEvent);
                    eventRouter.post("/async", EstDemoApplication::publishAsyncEvent);
                });
                
                apiRouter.group("/monitor", (monitorRouter, monitorGroup) -> {
                    monitorRouter.get("/jvm", EstDemoApplication::getJvmMetrics);
                    monitorRouter.get("/system", EstDemoApplication::getSystemMetrics);
                    monitorRouter.get("/stats", EstDemoApplication::getAppStats);
                });
                
                apiRouter.group("/logs", (logsRouter, logsGroup) -> {
                    logsRouter.get("", EstDemoApplication::listLogs);
                    logsRouter.delete("", EstDemoApplication::clearLogs);
                });
                
                apiRouter.get("/hello", EstDemoApplication::helloHandler);
                apiRouter.post("/echo", EstDemoApplication::echoHandler);
            });
        });

        app.enableCors();

        System.out.println("Server starting on port 8080...");
        System.out.println("Available endpoints:");
        System.out.println("  - GET  /                          - Home page with UI");
        System.out.println("  - GET  /api/hello                 - Hello with name parameter");
        System.out.println("  - POST /api/echo                  - Echo request body");
        System.out.println();
        System.out.println("  Users API:");
        System.out.println("  - GET  /api/users                 - List all users");
        System.out.println("  - GET  /api/users/:id             - Get user by ID");
        System.out.println("  - POST /api/users                 - Create user");
        System.out.println("  - PUT  /api/users/:id             - Update user");
        System.out.println("  - DELETE /api/users/:id           - Delete user");
        System.out.println();
        System.out.println("  Todos API:");
        System.out.println("  - GET  /api/todos                 - List all todos");
        System.out.println("  - GET  /api/todos/:id             - Get todo by ID");
        System.out.println("  - POST /api/todos                 - Create todo");
        System.out.println("  - PUT  /api/todos/:id             - Update todo");
        System.out.println("  - DELETE /api/todos/:id           - Delete todo");
        System.out.println("  - PATCH /api/todos/:id/complete   - Toggle todo complete");
        System.out.println();
        System.out.println("  Products API:");
        System.out.println("  - GET  /api/products              - List all products");
        System.out.println("  - GET  /api/products/:id          - Get product by ID");
        System.out.println("  - POST /api/products              - Create product");
        System.out.println("  - PUT  /api/products/:id          - Update product");
        System.out.println("  - DELETE /api/products/:id        - Delete product");
        System.out.println();
        System.out.println("  Cache API:");
        System.out.println("  - GET  /api/cache/memory          - List memory cache");
        System.out.println("  - GET  /api/cache/memory/:key     - Get memory cache");
        System.out.println("  - PUT  /api/cache/memory/:key     - Set memory cache");
        System.out.println("  - DELETE /api/cache/memory/:key    - Delete memory cache");
        System.out.println("  - GET  /api/cache/file/:key       - Get file cache");
        System.out.println("  - PUT  /api/cache/file/:key       - Set file cache");
        System.out.println();
        System.out.println("  Events API:");
        System.out.println("  - POST /api/events/local          - Publish local event");
        System.out.println("  - POST /api/events/async          - Publish async event");
        System.out.println();
        System.out.println("  Monitor API:");
        System.out.println("  - GET  /api/monitor/jvm           - Get JVM metrics");
        System.out.println("  - GET  /api/monitor/system        - Get system metrics");
        System.out.println("  - GET  /api/monitor/stats         - Get app stats");
        System.out.println();
        System.out.println("  Logs API:");
        System.out.println("  - GET  /api/logs                  - List recent logs");
        System.out.println("  - DELETE /api/logs                - Clear all logs");
        System.out.println();
        System.out.println("Open your browser and visit: http://localhost:8080");
        System.out.println("Press Ctrl+C to stop the server.");
        System.out.println();

        app.run(8080);
    }

    private static void homePage(Request req, Response res) {
        res.html("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>EST Framework Demo</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }
                    .container { max-width: 1400px; margin: 0 auto; }
                    .header { background: white; padding: 30px; border-radius: 10px; margin-bottom: 20px; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
                    .header h1 { color: #333; margin-bottom: 10px; }
                    .header p { color: #666; }
                    .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(400px, 1fr)); gap: 20px; }
                    .card { background: white; padding: 25px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
                    .card h2 { color: #333; margin-bottom: 15px; font-size: 1.3em; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
                    .stats { display: flex; gap: 10px; margin-bottom: 20px; flex-wrap: wrap; }
                    .stat { flex: 1; min-width: 120px; text-align: center; padding: 15px; background: #f8f9fa; border-radius: 8px; }
                    .stat-number { font-size: 24px; font-weight: bold; color: #667eea; }
                    .stat-label { color: #666; font-size: 12px; margin-top: 5px; }
                    .form-group { margin-bottom: 15px; }
                    .form-group label { display: block; margin-bottom: 5px; color: #555; font-weight: 500; font-size: 14px; }
                    .form-group input, .form-group select { width: 100%; padding: 10px; border: 2px solid #e0e0e0; border-radius: 5px; font-size: 14px; transition: border-color 0.3s; }
                    .form-group input:focus, .form-group select:focus { outline: none; border-color: #667eea; }
                    .btn { padding: 8px 16px; border: none; border-radius: 5px; cursor: pointer; font-size: 13px; font-weight: 500; transition: all 0.3s; }
                    .btn-primary { background: #667eea; color: white; }
                    .btn-primary:hover { background: #5568d3; }
                    .btn-success { background: #4CAF50; color: white; }
                    .btn-success:hover { background: #45a049; }
                    .btn-danger { background: #ff4444; color: white; }
                    .btn-danger:hover { background: #cc0000; }
                    .btn-warning { background: #ff9800; color: white; }
                    .btn-warning:hover { background: #e68a00; }
                    .btn-group { display: flex; gap: 8px; flex-wrap: wrap; }
                    .list { list-style: none; }
                    .list-item { padding: 12px; border: 1px solid #e0e0e0; border-radius: 5px; margin-bottom: 10px; display: flex; justify-content: space-between; align-items: center; }
                    .list-item.completed { opacity: 0.6; }
                    .list-item.completed .item-text { text-decoration: line-through; color: #999; }
                    .item-content { flex: 1; }
                    .item-text { font-weight: 500; font-size: 14px; }
                    .item-desc { color: #666; font-size: 12px; margin-top: 3px; }
                    .priority { padding: 2px 6px; border-radius: 3px; font-size: 11px; margin-right: 8px; }
                    .priority-high { background: #ff4444; color: white; }
                    .priority-medium { background: #ffaa00; color: white; }
                    .priority-low { background: #4CAF50; color: white; }
                    .metric-box { background: #f8f9fa; padding: 12px; border-radius: 8px; margin-bottom: 10px; }
                    .metric-key { color: #666; font-size: 12px; }
                    .metric-value { color: #333; font-weight: 600; font-size: 14px; margin-top: 4px; word-break: break-all; }
                    .tabs { display: flex; gap: 5px; margin-bottom: 20px; border-bottom: 2px solid #e0e0e0; flex-wrap: wrap; }
                    .tab { padding: 8px 16px; cursor: pointer; border: none; background: transparent; color: #666; font-weight: 500; border-bottom: 2px solid transparent; margin-bottom: -2px; font-size: 13px; }
                    .tab.active { color: #667eea; border-bottom-color: #667eea; }
                    .tab-content { display: none; }
                    .tab-content.active { display: block; }
                    .log-item { padding: 10px; border-left: 4px solid #667eea; background: #f8f9fa; margin-bottom: 8px; border-radius: 0 5px 5px 0; }
                    .log-type { font-weight: bold; font-size: 12px; color: #667eea; }
                    .log-message { font-size: 13px; margin-top: 4px; }
                    .log-time { font-size: 11px; color: #999; margin-top: 4px; }
                    .price-tag { font-weight: bold; color: #4CAF50; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚀 EST Framework Demo</h1>
                        <p>Enhanced with logging, caching, events, scheduling, monitoring, products, and logs!</p>
                    </div>
                    
                    <div class="grid">
                        <div class="card" style="grid-column: 1 / -1;">
                            <h2>📊 Application Stats</h2>
                            <div class="stats">
                                <div class="stat">
                                    <div class="stat-number" id="requestCount">0</div>
                                    <div class="stat-label">Requests</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="userCount">0</div>
                                    <div class="stat-label">Users</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="todoCount">0</div>
                                    <div class="stat-label">Todos</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="productCount">0</div>
                                    <div class="stat-label">Products</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="cacheCount">0</div>
                                    <div class="stat-label">Cache Items</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="logCount">0</div>
                                    <div class="stat-label">Log Entries</div>
                                </div>
                                <div class="stat">
                                    <div class="stat-number" id="avgTime">0</div>
                                    <div class="stat-label">Avg Time (ms)</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="card">
                        <h2>👥 Users</h2>
                        <div class="form-group">
                            <label>Name</label>
                            <input type="text" id="userName" placeholder="Enter name">
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" id="userEmail" placeholder="Enter email">
                        </div>
                        <div class="form-group">
                            <label>Role</label>
                            <select id="userRole">
                                <option value="user">User</option>
                                <option value="admin">Admin</option>
                            </select>
                        </div>
                        <button class="btn btn-primary" onclick="addUser()">Add User</button>
                        <ul class="list" id="userList" style="margin-top: 20px;"></ul>
                    </div>
                    
                    <div class="card">
                        <h2>📝 Todos</h2>
                        <div class="form-group">
                            <label>Title</label>
                            <input type="text" id="todoTitle" placeholder="Enter title">
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <input type="text" id="todoDesc" placeholder="Enter description">
                        </div>
                        <div class="form-group">
                            <label>Priority</label>
                            <select id="todoPriority">
                                <option value="low">Low</option>
                                <option value="medium" selected>Medium</option>
                                <option value="high">High</option>
                            </select>
                        </div>
                        <button class="btn btn-primary" onclick="addTodo()">Add Todo</button>
                        <ul class="list" id="todoList" style="margin-top: 20px;"></ul>
                    </div>
                    
                    <div class="card">
                        <h2>📦 Products</h2>
                        <div class="form-group">
                            <label>Name</label>
                            <input type="text" id="productName" placeholder="Enter product name">
                        </div>
                        <div class="form-group">
                            <label>Description</label>
                            <input type="text" id="productDesc" placeholder="Enter description">
                        </div>
                        <div class="form-group">
                            <label>Price</label>
                            <input type="number" id="productPrice" placeholder="Enter price" step="0.01">
                        </div>
                        <div class="form-group">
                            <label>Stock</label>
                            <input type="number" id="productStock" placeholder="Enter stock" value="0">
                        </div>
                        <button class="btn btn-primary" onclick="addProduct()">Add Product</button>
                        <ul class="list" id="productList" style="margin-top: 20px;"></ul>
                    </div>
                    
                    <div class="card">
                        <h2>💾 Cache</h2>
                        <div class="form-group">
                            <label>Key</label>
                            <input type="text" id="cacheKey" placeholder="Enter key">
                        </div>
                        <div class="form-group">
                            <label>Value</label>
                            <input type="text" id="cacheValue" placeholder="Enter value">
                        </div>
                        <div class="btn-group">
                            <button class="btn btn-primary" onclick="setCache()">Set</button>
                            <button class="btn btn-success" onclick="getCache()">Get</button>
                            <button class="btn btn-danger" onclick="deleteCache()">Delete</button>
                        </div>
                        <div id="cacheResult" style="margin-top: 15px; padding: 10px; background: #f8f9fa; border-radius: 5px;"></div>
                    </div>
                    
                    <div class="card">
                        <h2>📡 Events</h2>
                        <div class="form-group">
                            <label>Event Name</label>
                            <input type="text" id="eventName" placeholder="Enter event name" value="test-event">
                        </div>
                        <div class="btn-group">
                            <button class="btn btn-primary" onclick="publishLocalEvent()">Publish Local</button>
                            <button class="btn btn-success" onclick="publishAsyncEvent()">Publish Async</button>
                        </div>
                        <div id="eventResult" style="margin-top: 15px; padding: 10px; background: #f8f9fa; border-radius: 5px;"></div>
                    </div>
                    
                    <div class="card">
                        <h2>📈 Monitoring</h2>
                        <div class="tabs">
                            <button class="tab active" onclick="switchTab('jvm')">JVM</button>
                            <button class="tab" onclick="switchTab('system')">System</button>
                            <button class="tab" onclick="switchTab('stats')">App Stats</button>
                        </div>
                        <div id="jvmTab" class="tab-content active"></div>
                        <div id="systemTab" class="tab-content"></div>
                        <div id="statsTab" class="tab-content"></div>
                    </div>
                    
                    <div class="card" style="grid-column: 1 / -1;">
                        <h2>📋 Logs</h2>
                        <div class="btn-group" style="margin-bottom: 15px;">
                            <button class="btn btn-primary" onclick="loadLogs('')">All</button>
                            <button class="btn btn-primary" onclick="loadLogs('USER_CREATED')">User Events</button>
                            <button class="btn btn-primary" onclick="loadLogs('PRODUCT')">Product Events</button>
                            <button class="btn btn-primary" onclick="loadLogs('TASK')">Tasks</button>
                            <button class="btn btn-danger" onclick="clearLogs()">Clear All</button>
                        </div>
                        <div id="logsList"></div>
                    </div>
                </div>
            </div>
            
            <script>
                let users = [];
                let todos = [];
                let products = [];
                let stats = {};
                
                async function loadAll() {
                    await Promise.all([
                        loadUsers(),
                        loadTodos(),
                        loadProducts(),
                        loadStats(),
                        loadLogs('')
                    ]);
                }
                
                async function loadUsers() {
                    const res = await fetch('/api/users');
                    const data = await res.json();
                    users = data.data || [];
                    renderUsers();
                    updateStats();
                }
                
                async function loadTodos() {
                    const res = await fetch('/api/todos');
                    const data = await res.json();
                    todos = data.data || [];
                    renderTodos();
                    updateStats();
                }
                
                async function loadProducts() {
                    const res = await fetch('/api/products');
                    const data = await res.json();
                    products = data.data || [];
                    renderProducts();
                    updateStats();
                }
                
                async function loadLogs(type) {
                    const url = type ? '/api/logs?type=' + encodeURIComponent(type) : '/api/logs';
                    const res = await fetch(url);
                    const data = await res.json();
                    renderLogs(data.data || []);
                    updateStats();
                }
                
                async function loadStats() {
                    const res = await fetch('/api/monitor/stats');
                    stats = await res.json();
                    updateStats();
                }
                
                async function loadJvmMetrics() {
                    const res = await fetch('/api/monitor/jvm');
                    const data = await res.json();
                    renderMetrics('jvmTab', data);
                }
                
                async function loadSystemMetrics() {
                    const res = await fetch('/api/monitor/system');
                    const data = await res.json();
                    renderMetrics('systemTab', data);
                }
                
                async function loadAppStats() {
                    const res = await fetch('/api/monitor/stats');
                    const data = await res.json();
                    renderMetrics('statsTab', data);
                }
                
                function renderMetrics(tabId, data) {
                    const container = document.getElementById(tabId);
                    container.innerHTML = Object.entries(data).map(([key, value]) => \\`
                        <div class="metric-box">
                            <div class="metric-key">\\${key}</div>
                            <div class="metric-value">\\${typeof value === 'object' ? JSON.stringify(value, null, 2) : value}</div>
                        </div>
                    \\`).join('');
                }
                
                function renderLogs(logs) {
                    const container = document.getElementById('logsList');
                    if (!logs || logs.length === 0) {
                        container.innerHTML = '<div style="padding: 20px; text-align: center; color: #999;">No logs found</div>';
                        return;
                    }
                    container.innerHTML = logs.slice().reverse().map(log => \\`
                        <div class="log-item">
                            <div class="log-type">\\${escapeHtml(log.type)}</div>
                            <div class="log-message">\\${escapeHtml(log.message)}</div>
                            <div class="log-time">\\${new Date(log.timestamp).toLocaleString()}</div>
                        </div>
                    \\`).join('');
                }
                
                function switchTab(tab) {
                    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
                    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
                    event.target.classList.add('active');
                    document.getElementById(tab + 'Tab').classList.add('active');
                    
                    if (tab === 'jvm') loadJvmMetrics();
                    if (tab === 'system') loadSystemMetrics();
                    if (tab === 'stats') loadAppStats();
                }
                
                function updateStats() {
                    document.getElementById('requestCount').textContent = stats.requestCount || 0;
                    document.getElementById('userCount').textContent = users.length;
                    document.getElementById('todoCount').textContent = todos.length;
                    document.getElementById('productCount').textContent = products.length;
                    document.getElementById('cacheCount').textContent = stats.cacheSize || 0;
                    document.getElementById('logCount').textContent = stats.logCount || 0;
                    document.getElementById('avgTime').textContent = stats.avgRequestTimeMs || 0;
                }
                
                async function addUser() {
                    const name = document.getElementById('userName').value;
                    const email = document.getElementById('userEmail').value;
                    const role = document.getElementById('userRole').value;
                    
                    if (!name || !email) return;
                    
                    const formData = new FormData();
                    formData.append('name', name);
                    formData.append('email', email);
                    formData.append('role', role);
                    
                    await fetch('/api/users', { method: 'POST', body: formData });
                    document.getElementById('userName').value = '';
                    document.getElementById('userEmail').value = '';
                    loadUsers();
                }
                
                async function deleteUser(id) {
                    await fetch('/api/users/' + id, { method: 'DELETE' });
                    loadUsers();
                }
                
                function renderUsers() {
                    const list = document.getElementById('userList');
                    list.innerHTML = users.map(user => \\`
                        <li class="list-item">
                            <div class="item-content">
                                <div class="item-text">\\${escapeHtml(user.name)}</div>
                                <div class="item-desc">\\${escapeHtml(user.email)} - \\${user.role}</div>
                            </div>
                            <button class="btn btn-danger" onclick="deleteUser('\\\\${user.id}')">Delete</button>
                        </li>
                    \\`).join('');
                }
                
                async function addTodo() {
                    const title = document.getElementById('todoTitle').value;
                    const desc = document.getElementById('todoDesc').value;
                    const priority = document.getElementById('todoPriority').value;
                    
                    if (!title) return;
                    
                    const formData = new FormData();
                    formData.append('title', title);
                    formData.append('description', desc);
                    formData.append('priority', priority);
                    
                    await fetch('/api/todos', { method: 'POST', body: formData });
                    document.getElementById('todoTitle').value = '';
                    document.getElementById('todoDesc').value = '';
                    loadTodos();
                }
                
                async function toggleTodo(id) {
                    await fetch('/api/todos/' + id + '/complete', { method: 'PATCH' });
                    loadTodos();
                }
                
                async function deleteTodo(id) {
                    await fetch('/api/todos/' + id, { method: 'DELETE' });
                    loadTodos();
                }
                
                function renderTodos() {
                    const list = document.getElementById('todoList');
                    list.innerHTML = todos.map(todo => \\`
                        <li class="list-item \\${todo.completed ? 'completed' : ''}">
                            <input type="checkbox" \\${todo.completed ? 'checked' : ''} 
                                onchange="toggleTodo('\\\\${todo.id}')" style="margin-right: 10px;">
                            <div class="item-content">
                                <div class="item-text">\\${escapeHtml(todo.title)}</div>
                                <div class="item-desc">\\${escapeHtml(todo.description || '')}</div>
                            </div>
                            <span class="priority priority-\\${todo.priority}">\\${todo.priority}</span>
                            <button class="btn btn-danger" onclick="deleteTodo('\\\\${todo.id}')">Delete</button>
                        </li>
                    \\`).join('');
                }
                
                async function addProduct() {
                    const name = document.getElementById('productName').value;
                    const desc = document.getElementById('productDesc').value;
                    const price = document.getElementById('productPrice').value;
                    const stock = document.getElementById('productStock').value;
                    
                    if (!name || !price) return;
                    
                    const formData = new FormData();
                    formData.append('name', name);
                    formData.append('description', desc);
                    formData.append('price', price);
                    formData.append('stock', stock);
                    
                    await fetch('/api/products', { method: 'POST', body: formData });
                    document.getElementById('productName').value = '';
                    document.getElementById('productDesc').value = '';
                    document.getElementById('productPrice').value = '';
                    loadProducts();
                }
                
                async function deleteProduct(id) {
                    await fetch('/api/products/' + id, { method: 'DELETE' });
                    loadProducts();
                }
                
                function renderProducts() {
                    const list = document.getElementById('productList');
                    list.innerHTML = products.map(product => \\`
                        <li class="list-item">
                            <div class="item-content">
                                <div class="item-text">\\${escapeHtml(product.name)}</div>
                                <div class="item-desc">\\${escapeHtml(product.description || '')}</div>
                                <div class="item-desc">
                                    <span class="price-tag">$\\${product.price.toFixed(2)}</span>
                                    <span style="margin-left: 15px;">Stock: \\${product.stock}</span>
                                </div>
                            </div>
                            <button class="btn btn-danger" onclick="deleteProduct('\\\\${product.id}')">Delete</button>
                        </li>
                    \\`).join('');
                }
                
                async function setCache() {
                    const key = document.getElementById('cacheKey').value;
                    const value = document.getElementById('cacheValue').value;
                    if (!key || !value) return;
                    
                    const formData = new FormData();
                    formData.append('value', value);
                    await fetch('/api/cache/memory/' + key, { method: 'PUT', body: formData });
                    document.getElementById('cacheResult').textContent = 'Cache set: ' + key + ' = ' + value;
                    loadStats();
                }
                
                async function getCache() {
                    const key = document.getElementById('cacheKey').value;
                    if (!key) return;
                    
                    const res = await fetch('/api/cache/memory/' + key);
                    const data = await res.json();
                    document.getElementById('cacheResult').textContent = data.value ? (key + ' = ' + data.value) : 'Key not found';
                }
                
                async function deleteCache() {
                    const key = document.getElementById('cacheKey').value;
                    if (!key) return;
                    
                    await fetch('/api/cache/memory/' + key, { method: 'DELETE' });
                    document.getElementById('cacheResult').textContent = 'Cache deleted: ' + key;
                    loadStats();
                }
                
                async function publishLocalEvent() {
                    const name = document.getElementById('eventName').value;
                    const formData = new FormData();
                    formData.append('name', name);
                    await fetch('/api/events/local', { method: 'POST', body: formData });
                    document.getElementById('eventResult').textContent = 'Local event published: ' + name;
                    loadLogs('');
                }
                
                async function publishAsyncEvent() {
                    const name = document.getElementById('eventName').value;
                    const formData = new FormData();
                    formData.append('name', name);
                    await fetch('/api/events/async', { method: 'POST', body: formData });
                    document.getElementById('eventResult').textContent = 'Async event published: ' + name;
                    loadLogs('');
                }
                
                async function clearLogs() {
                    await fetch('/api/logs', { method: 'DELETE' });
                    loadLogs('');
                }
                
                function escapeHtml(text) {
                    const div = document.createElement('div');
                    div.textContent = text || '';
                    return div.innerHTML;
                }
                
                loadAll();
                setInterval(() => { loadStats(); loadLogs(''); }, 5000);
            </script>
            </body>
            </html>
        """);
    }

    private static void helloHandler(Request req, Response res) {
        String name = req.getParameterOrDefault("name", "Guest");
        res.text("Hello, " + name + "!");
    }

    private static void listUsers(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", userRepository.findAll()
        ));
    }

    private static void getUser(Request req, Response res) {
        String id = req.getPathVariable("id");
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            res.json(Map.of("success", true, "data", user));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "User not found"));
        }
    }

    private static void createUser(Request req, Response res) {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String role = req.getParameterOrDefault("role", "user");

        if (name == null || name.isBlank() || email == null || email.isBlank()) {
            res.setStatus(400);
            res.json(Map.of("success", false, "message", "Name and email are required"));
            return;
        }

        String id = UUID.randomUUID().toString();
        User user = new User(id, name, email, role);
        userRepository.save(user);

        localEventBus.publish("user.created", new UserCreatedEvent(name, email));

        res.setStatus(201);
        res.json(Map.of(
            "success", true,
            "message", "User created successfully",
            "data", user
        ));
    }

    private static void updateUser(Request req, Response res) {
        String id = req.getPathVariable("id");
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "User not found"));
            return;
        }

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String role = req.getParameter("role");

        if (name != null) user.name = name;
        if (email != null) user.email = email;
        if (role != null) user.role = role;

        userRepository.save(user);

        res.json(Map.of(
            "success", true,
            "message", "User updated successfully",
            "data", user
        ));
    }

    private static void deleteUser(Request req, Response res) {
        String id = req.getPathVariable("id");
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            res.json(Map.of("success", true, "message", "User deleted successfully"));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "User not found"));
        }
    }

    private static void listTodos(Request req, Response res) {
        String filter = req.getParameter("filter");
        
        var filtered = todos.values().stream();
        if ("active".equals(filter)) {
            filtered = filtered.filter(t -> !t.completed);
        } else if ("completed".equals(filter)) {
            filtered = filtered.filter(t -> t.completed);
        }
        
        res.json(Map.of(
            "success", true,
            "data", filtered.collect(Collectors.toList())
        ));
    }

    private static void getTodo(Request req, Response res) {
        String id = req.getPathVariable("id");
        Todo todo = todos.get(id);
        if (todo != null) {
            res.json(Map.of("success", true, "data", todo));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Todo not found"));
        }
    }

    private static void createTodo(Request req, Response res) {
        String title = req.getParameter("title");
        String description = req.getParameterOrDefault("description", "");
        String priority = req.getParameterOrDefault("priority", "medium");

        if (title == null || title.isBlank()) {
            res.setStatus(400);
            res.json(Map.of("success", false, "message", "Title is required"));
            return;
        }

        String id = UUID.randomUUID().toString();
        Todo todo = new Todo(id, title, description, false, priority);
        todos.put(id, todo);

        res.setStatus(201);
        res.json(Map.of(
            "success", true,
            "message", "Todo created successfully",
            "data", todo
        ));
    }

    private static void updateTodo(Request req, Response res) {
        String id = req.getPathVariable("id");
        Todo todo = todos.get(id);

        if (todo == null) {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Todo not found"));
            return;
        }

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String priority = req.getParameter("priority");

        if (title != null) todo.title = title;
        if (description != null) todo.description = description;
        if (priority != null) todo.priority = priority;

        res.json(Map.of(
            "success", true,
            "message", "Todo updated successfully",
            "data", todo
        ));
    }

    private static void deleteTodo(Request req, Response res) {
        String id = req.getPathVariable("id");
        if (todos.remove(id) != null) {
            res.json(Map.of("success", true, "message", "Todo deleted successfully"));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Todo not found"));
        }
    }

    private static void toggleComplete(Request req, Response res) {
        String id = req.getPathVariable("id");
        Todo todo = todos.get(id);

        if (todo == null) {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Todo not found"));
            return;
        }

        todo.completed = !todo.completed;
        res.json(Map.of(
            "success", true,
            "message", "Todo status updated",
            "data", todo
        ));
    }

    private static void getMemoryCache(Request req, Response res) {
        String key = req.getPathVariable("key");
        Object value = memoryCache.get(key);
        res.json(Map.of(
            "success", true,
            "key", key,
            "value", value
        ));
    }

    private static void setMemoryCache(Request req, Response res) {
        String key = req.getPathVariable("key");
        String value = req.getParameter("value");
        if (value != null) {
            memoryCache.put(key, value);
        }
        res.json(Map.of(
            "success", true,
            "message", "Cache set successfully",
            "key", key,
            "value", value
        ));
    }

    private static void deleteMemoryCache(Request req, Response res) {
        String key = req.getPathVariable("key");
        memoryCache.remove(key);
        res.json(Map.of(
            "success", true,
            "message", "Cache deleted successfully"
        ));
    }

    private static void listMemoryCache(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", memoryCache.keySet()
        ));
    }

    private static void getFileCache(Request req, Response res) {
        String key = req.getPathVariable("key");
        Object value = fileCache.get(key);
        res.json(Map.of(
            "success", true,
            "key", key,
            "value", value
        ));
    }

    private static void setFileCache(Request req, Response res) {
        String key = req.getPathVariable("key");
        String value = req.getParameter("value");
        if (value != null) {
            fileCache.put(key, value);
        }
        res.json(Map.of(
            "success", true,
            "message", "File cache set successfully",
            "key", key,
            "value", value
        ));
    }

    private static void publishLocalEvent(Request req, Response res) {
        String name = req.getParameterOrDefault("name", "test-event");
        localEventBus.publish("user.created", new UserCreatedEvent(name, name + "@example.com"));
        res.json(Map.of(
            "success", true,
            "message", "Local event published"
        ));
    }

    private static void publishAsyncEvent(Request req, Response res) {
        String name = req.getParameterOrDefault("name", "async-task");
        asyncEventBus.publish("task.event", new TaskEvent(name));
        res.json(Map.of(
            "success", true,
            "message", "Async event published"
        ));
    }

    private static void getJvmMetrics(Request req, Response res) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("heapMemoryUsage", jvmMonitor.getMetric("heapMemoryUsage"));
        metrics.put("nonHeapMemoryUsage", jvmMonitor.getMetric("nonHeapMemoryUsage"));
        metrics.put("threadCount", jvmMonitor.getMetric("threadCount"));
        metrics.put("peakThreadCount", jvmMonitor.getMetric("peakThreadCount"));
        metrics.put("systemLoadAverage", jvmMonitor.getMetric("systemLoadAverage"));
        metrics.put("uptime", jvmMonitor.getMetric("uptime"));
        res.json(metrics);
    }

    private static void getSystemMetrics(Request req, Response res) {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("availableProcessors", systemMonitor.getMetric("availableProcessors"));
        metrics.put("freeMemory", systemMonitor.getMetric("freeMemory"));
        metrics.put("totalMemory", systemMonitor.getMetric("totalMemory"));
        metrics.put("maxMemory", systemMonitor.getMetric("maxMemory"));
        metrics.put("osName", systemMonitor.getMetric("osName"));
        metrics.put("osVersion", systemMonitor.getMetric("osVersion"));
        metrics.put("osArch", systemMonitor.getMetric("osArch"));
        res.json(metrics);
    }

    private static void getAppStats(Request req, Response res) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("requestCount", requestCount.get());
        stats.put("userCount", userRepository.count());
        stats.put("todoCount", todos.size());
        stats.put("productCount", products.size());
        stats.put("cacheSize", memoryCache.size());
        stats.put("logCount", logEntries.size());
        stats.put("uptime", System.currentTimeMillis() - (Long) memoryCache.get("system:startup").orElse(0L));
        long avgTime = requestCount.get() > 0 ? totalRequestTime.get() / requestCount.get() : 0;
        stats.put("avgRequestTimeMs", avgTime);
        res.json(stats);
    }
    
    private static void echoHandler(Request req, Response res) {
        String body = req.getBody();
        Map<String, Object> result = new HashMap<>();
        result.put("method", req.getMethod());
        result.put("path", req.getPath());
        result.put("body", body);
        result.put("timestamp", System.currentTimeMillis());
        res.json(result);
    }
    
    private static void listProducts(Request req, Response res) {
        res.json(Map.of(
            "success", true,
            "data", new ArrayList<>(products.values())
        ));
    }
    
    private static void getProduct(Request req, Response res) {
        String id = req.getPathVariable("id");
        Product product = products.get(id);
        if (product != null) {
            res.json(Map.of("success", true, "data", product));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Product not found"));
        }
    }
    
    private static void createProduct(Request req, Response res) {
        String name = req.getParameter("name");
        String description = req.getParameterOrDefault("description", "");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameterOrDefault("stock", "0");
        
        if (name == null || name.isBlank() || priceStr == null) {
            res.setStatus(400);
            res.json(Map.of("success", false, "message", "Name and price are required"));
            return;
        }
        
        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            
            String id = UUID.randomUUID().toString();
            Product product = new Product(id, name, description, price, stock);
            products.put(id, product);
            
            localEventBus.publish("product.event", new ProductEvent("created", name));
            
            res.setStatus(201);
            res.json(Map.of(
                "success", true,
                "message", "Product created successfully",
                "data", product
            ));
        } catch (NumberFormatException e) {
            res.setStatus(400);
            res.json(Map.of("success", false, "message", "Invalid number format"));
        }
    }
    
    private static void updateProduct(Request req, Response res) {
        String id = req.getPathVariable("id");
        Product product = products.get(id);
        
        if (product == null) {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Product not found"));
            return;
        }
        
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        
        if (name != null) product.name = name;
        if (description != null) product.description = description;
        if (priceStr != null) {
            try {
                product.price = Double.parseDouble(priceStr);
            } catch (NumberFormatException ignored) {}
        }
        if (stockStr != null) {
            try {
                product.stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException ignored) {}
        }
        
        localEventBus.publish("product.event", new ProductEvent("updated", product.name));
        
        res.json(Map.of(
            "success", true,
            "message", "Product updated successfully",
            "data", product
        ));
    }
    
    private static void deleteProduct(Request req, Response res) {
        String id = req.getPathVariable("id");
        Product product = products.remove(id);
        if (product != null) {
            localEventBus.publish("product.event", new ProductEvent("deleted", product.name));
            res.json(Map.of("success", true, "message", "Product deleted successfully"));
        } else {
            res.setStatus(404);
            res.json(Map.of("success", false, "message", "Product not found"));
        }
    }
    
    private static void listLogs(Request req, Response res) {
        String type = req.getParameter("type");
        List<LogEntry> filteredLogs = logEntries;
        if (type != null && !type.isBlank()) {
            filteredLogs = logEntries.stream()
                .filter(log -> log.type.equalsIgnoreCase(type) || log.type.startsWith(type))
                .collect(Collectors.toList());
        }
        res.json(Map.of(
            "success", true,
            "data", filteredLogs
        ));
    }
    
    private static void clearLogs(Request req, Response res) {
        logEntries.clear();
        addLogEntry("LOGS_CLEARED", "All logs cleared");
        res.json(Map.of("success", true, "message", "Logs cleared successfully"));
    }

    static class User {
        public String id;
        public String name;
        public String email;
        public String role;
        public long createdAt;

        public User(String id, String name, String email, String role) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.createdAt = System.currentTimeMillis();
        }

        public String getId() {
            return id;
        }
    }

    static class Todo {
        public String id;
        public String title;
        public String description;
        public boolean completed;
        public String priority;
        public long createdAt;

        public Todo(String id, String title, String description, boolean completed, String priority) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.completed = completed;
            this.priority = priority;
            this.createdAt = System.currentTimeMillis();
        }
    }
    
    static class Product {
        public String id;
        public String name;
        public String description;
        public double price;
        public int stock;
        public long createdAt;

        public Product(String id, String name, String description, double price, int stock) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stock = stock;
            this.createdAt = System.currentTimeMillis();
        }
    }
    
    static class LogEntry {
        public String id;
        public String type;
        public String message;
        public long timestamp;

        public LogEntry(String id, String type, String message, long timestamp) {
            this.id = id;
            this.type = type;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    static class UserCreatedEvent {
        private final String username;
        private final String email;

        public UserCreatedEvent(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() { return username; }
        public String getEmail() { return email; }
    }
    
    static class ProductEvent {
        private final String action;
        private final String productName;

        public ProductEvent(String action, String productName) {
            this.action = action;
            this.productName = productName;
        }

        public String getAction() { return action; }
        public String getProductName() { return productName; }
    }

    static class TaskEvent {
        private final String taskName;

        public TaskEvent(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskName() { return taskName; }
    }
}
