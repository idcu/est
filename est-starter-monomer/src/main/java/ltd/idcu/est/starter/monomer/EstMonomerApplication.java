package ltd.idcu.est.starter.monomer;

import ltd.idcu.est.web.Web;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;
import ltd.idcu.est.logging.api.Logger;
import ltd.idcu.est.logging.console.ConsoleLogs;
import ltd.idcu.est.logging.file.FileLogs;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import ltd.idcu.est.event.api.EventBus;
import ltd.idcu.est.event.local.LocalEventBus;
import ltd.idcu.est.scheduler.api.Scheduler;
import ltd.idcu.est.scheduler.fixed.FixedScheduler;
import ltd.idcu.est.monitor.api.Monitor;
import ltd.idcu.est.monitor.jvm.JvmMonitor;
import ltd.idcu.est.monitor.system.SystemMonitor;
import ltd.idcu.est.data.api.Repository;
import ltd.idcu.est.data.memory.MemoryRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EstMonomerApplication {

    private static final Logger consoleLogger = ConsoleLogs.getLogger(EstMonomerApplication.class);
    private static final Logger fileLogger = FileLogs.getLogger("est-monomer.log");
    
    private static final Cache<String, Object> memoryCache = new MemoryCache<>();
    
    private static final EventBus localEventBus = new LocalEventBus();
    
    private static final Scheduler scheduler = new FixedScheduler();
    
    private static final Monitor jvmMonitor = new JvmMonitor();
    private static final Monitor systemMonitor = new SystemMonitor();
    
    private static final AtomicInteger requestCount = new AtomicInteger(0);
    private static final AtomicLong totalRequestTime = new AtomicLong(0);

    static {
        localEventBus.subscribe(SystemEvent.class, event -> {
            consoleLogger.info("收到系统事件: {} - {}", event.getType(), event.getMessage());
            fileLogger.info("系统事件 [{}]: {}", event.getType(), event.getMessage());
        });
        
        scheduler.scheduleAtFixedRate(() -> {
            consoleLogger.debug("定时任务执行 - 当前请求数: {}", requestCount.get());
        }, 10, 30, TimeUnit.SECONDS);
        
        memoryCache.put("system:startup", System.currentTimeMillis());
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  EST Monomer Application");
        System.out.println("  单体应用启动器");
        System.out.println("  Version: 2.3.0-SNAPSHOT");
        System.out.println("========================================");
        System.out.println();

        WebApplication app = Web.create("EST Monomer", "2.3.0-SNAPSHOT");

        app.use((req, res, next) -> {
            requestCount.incrementAndGet();
            long startTime = System.currentTimeMillis();
            consoleLogger.info("Request: {} {}", req.getMethod(), req.getPath());
            next.handle();
            long duration = System.currentTimeMillis() - startTime;
            totalRequestTime.addAndGet(duration);
            consoleLogger.info("Response: {} {} - {}ms", req.getMethod(), req.getPath(), duration);
        });

        app.routes(router -> {
            router.get("/", EstMonomerApplication::homePage);
            
            router.group("/api", (apiRouter, apiGroup) -> {
                apiRouter.group("/system", (systemRouter, systemGroup) -> {
                    systemRouter.get("/health", EstMonomerApplication::healthCheck);
                    systemRouter.get("/info", EstMonomerApplication::systemInfo);
                });
                
                apiRouter.group("/cache", (cacheRouter, cacheGroup) -> {
                    cacheRouter.get("/memory/:key", EstMonomerApplication::getMemoryCache);
                    cacheRouter.put("/memory/:key", EstMonomerApplication::setMemoryCache);
                    cacheRouter.delete("/memory/:key", EstMonomerApplication::deleteMemoryCache);
                    cacheRouter.get("/memory", EstMonomerApplication::listMemoryCache);
                });
                
                apiRouter.group("/monitor", (monitorRouter, monitorGroup) -> {
                    monitorRouter.get("/jvm", EstMonomerApplication::getJvmMetrics);
                    monitorRouter.get("/system", EstMonomerApplication::getSystemMetrics);
                    monitorRouter.get("/stats", EstMonomerApplication::getAppStats);
                });
                
                apiRouter.get("/hello", EstMonomerApplication::helloHandler);
            });
        });

        app.enableCors();

        System.out.println("Server starting on port 8080...");
        System.out.println("Available endpoints:");
        System.out.println("  - GET  /                          - 首页");
        System.out.println("  - GET  /api/hello                 - 问候接口");
        System.out.println("  - GET  /api/system/health         - 健康检查");
        System.out.println("  - GET  /api/system/info           - 系统信息");
        System.out.println("  - GET  /api/monitor/jvm           - JVM指标");
        System.out.println("  - GET  /api/monitor/system        - 系统指标");
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
                <title>EST Monomer - 单体应用启动器</title>
                <meta charset="UTF-8">
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { font-family: 'Segoe UI', Arial, sans-serif; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; padding: 20px; }
                    .container { max-width: 1200px; margin: 0 auto; }
                    .header { background: white; padding: 40px; border-radius: 10px; margin-bottom: 20px; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
                    .header h1 { color: #333; margin-bottom: 10px; font-size: 2.5em; }
                    .header p { color: #666; font-size: 1.1em; }
                    .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(350px, 1fr)); gap: 20px; }
                    .card { background: white; padding: 25px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
                    .card h2 { color: #333; margin-bottom: 15px; font-size: 1.3em; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
                    .card ul { list-style: none; padding-left: 0; }
                    .card li { padding: 10px 0; border-bottom: 1px solid #eee; }
                    .card li:last-child { border-bottom: none; }
                    .card a { color: #667eea; text-decoration: none; }
                    .card a:hover { text-decoration: underline; }
                    .stat-box { background: #f8f9fa; padding: 20px; border-radius: 8px; text-align: center; }
                    .stat-number { font-size: 36px; font-weight: bold; color: #667eea; }
                    .stat-label { color: #666; margin-top: 5px; }
                    .feature-icon { font-size: 2em; margin-right: 10px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚀 EST Monomer</h1>
                        <p>EST框架单体应用启动器 - 快速开始您的项目</p>
                    </div>
                    
                    <div class="grid">
                        <div class="card">
                            <h2>📊 应用统计</h2>
                            <div class="stat-box">
                                <div class="stat-number" id="requestCount">0</div>
                                <div class="stat-label">请求次数</div>
                            </div>
                        </div>
                        
                        <div class="card">
                            <h2>✨ 核心功能</h2>
                            <ul>
                                <li><span class="feature-icon">🌐</span> Web服务 (HTTP/REST)</li>
                                <li><span class="feature-icon">📝</span> 日志系统 (控制台/文件)</li>
                                <li><span class="feature-icon">💾</span> 缓存系统 (内存)</li>
                                <li><span class="feature-icon">📡</span> 事件总线</li>
                                <li><span class="feature-icon">⏰</span> 任务调度</li>
                                <li><span class="feature-icon">📈</span> 系统监控</li>
                            </ul>
                        </div>
                        
                        <div class="card">
                            <h2>🔗 快速链接</h2>
                            <ul>
                                <li><a href="/api/system/health">健康检查</a></li>
                                <li><a href="/api/system/info">系统信息</a></li>
                                <li><a href="/api/monitor/jvm">JVM指标</a></li>
                                <li><a href="/api/monitor/system">系统指标</a></li>
                                <li><a href="/api/monitor/stats">应用统计</a></li>
                            </ul>
                        </div>
                        
                        <div class="card">
                            <h2>📚 下一步</h2>
                            <ul>
                                <li>查看 <a href="https://github.com/idcu/est" target="_blank">EST框架文档</a></li>
                                <li>了解 <a href="https://github.com/idcu/est/tree/main/est-app/est-admin" target="_blank">est-admin 管理后台</a></li>
                                <li>探索 <a href="https://github.com/idcu/est/tree/main/est-modules" target="_blank">EST模块</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <script>
                    async function loadStats() {
                        const res = await fetch('/api/monitor/stats');
                        const data = await res.json();
                        document.getElementById('requestCount').textContent = data.requestCount || 0;
                    }
                    
                    loadStats();
                    setInterval(loadStats, 3000);
                </script>
            </body>
            </html>
        """);
    }

    private static void helloHandler(Request req, Response res) {
        String name = req.getParameterOrDefault("name", "Guest");
        res.text("Hello, " + name + "! Welcome to EST Monomer!");
    }

    private static void healthCheck(Request req, Response res) {
        res.json(Map.of(
            "status", "UP",
            "timestamp", System.currentTimeMillis(),
            "version", "2.3.0-SNAPSHOT"
        ));
    }

    private static void systemInfo(Request req, Response res) {
        res.json(Map.of(
            "app", Map.of(
                "name", "EST Monomer",
                "version", "2.3.0-SNAPSHOT"
            ),
            "java", Map.of(
                "version", System.getProperty("java.version"),
                "vendor", System.getProperty("java.vendor")
            ),
            "os", Map.of(
                "name", System.getProperty("os.name"),
                "version", System.getProperty("os.version"),
                "arch", System.getProperty("os.arch")
            )
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

    private static void getJvmMetrics(Request req, Response res) {
        Map<String, Object> metrics = new HashMap<>();
        for (String metric : Arrays.asList("heapMemoryUsage", "nonHeapMemoryUsage", "threadCount", "uptime", "gcDetails")) {
            try {
                metrics.put(metric, jvmMonitor.getMetric(metric));
            } catch (Exception e) {
                metrics.put(metric, "N/A");
            }
        }
        res.json(metrics);
    }

    private static void getSystemMetrics(Request req, Response res) {
        Map<String, Object> metrics = new HashMap<>();
        for (String metric : Arrays.asList("availableProcessors", "systemLoadAverage", "totalMemory", "freeMemory")) {
            try {
                metrics.put(metric, systemMonitor.getMetric(metric));
            } catch (Exception e) {
                metrics.put(metric, "N/A");
            }
        }
        res.json(metrics);
    }

    private static void getAppStats(Request req, Response res) {
        long avgTime = requestCount.get() > 0 ? totalRequestTime.get() / requestCount.get() : 0;
        res.json(Map.of(
            "requestCount", requestCount.get(),
            "totalRequestTimeMs", totalRequestTime.get(),
            "avgRequestTimeMs", avgTime,
            "cacheSize", memoryCache.keySet().size(),
            "startupTime", memoryCache.get("system:startup")
        ));
    }

    static class SystemEvent {
        private final String type;
        private final String message;

        public SystemEvent(String type, String message) {
            this.type = type;
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public String getMessage() {
            return message;
        }
    }
}
