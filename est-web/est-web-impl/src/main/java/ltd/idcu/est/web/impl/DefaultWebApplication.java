package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.*;
import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.lifecycle.Lifecycle;
import ltd.idcu.est.core.api.lifecycle.LifecycleListener;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultWebApplication implements WebApplication {

    private String name;
    private String version;
    private WebServer webServer;
    private Router router;
    private SessionManager sessionManager;
    private StaticFileHandler staticFileHandler;
    private Config config;
    private java.util.List<LifecycleListener> listeners = new java.util.ArrayList<>();

    public DefaultWebApplication() {
        this("EST Application", "1.0.0");
    }

    public DefaultWebApplication(String name, String version) {
        this.name = name;
        this.version = version;
        this.webServer = new DefaultWebServer();
        this.router = new DefaultRouter();
        this.sessionManager = new DefaultSessionManager();
        this.staticFileHandler = new DefaultStaticFileHandler();
        ((DefaultWebServer) webServer).setRouter(router);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public WebServer getServer() {
        return webServer;
    }

    @Override
    public void setServer(WebServer server) {
        this.webServer = server;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Router getRouter() {
        return router;
    }

    @Override
    public void setRouter(Router router) {
        this.router = router;
        if (webServer != null) {
            webServer.setRouter(router);
        }
    }

    @Override
    public List<Middleware> getMiddlewares() {
        return webServer.getMiddlewares();
    }

    @Override
    public void addMiddleware(Middleware middleware) {
        webServer.addMiddleware(middleware);
    }

    @Override
    public void removeMiddleware(String name) {
        webServer.removeMiddleware(name);
    }

    @Override
    public void use(Middleware... middlewares) {
        for (Middleware middleware : middlewares) {
            webServer.addMiddleware(middleware);
        }
    }

    @Override
    public void use(String path, Middleware middleware) {
        // 实现路径中间件逻辑
    }

    @Override
    public void routes(Consumer<Router> routerConfig) {
        routerConfig.accept(router);
    }

    @Override
    public void controller(String path, Class<? extends Controller> controllerClass) {
        webServer.registerController(controllerClass);
    }

    @Override
    public void controller(String path, Controller controller) {
        webServer.registerController(path, controller);
    }

    @Override
    public void restController(String path, Class<? extends RestController> controllerClass) {
        webServer.registerRestController(controllerClass);
    }

    @Override
    public void restController(String path, RestController controller) {
        webServer.registerRestController(path, controller);
    }

    @Override
    public void staticFiles(String path, String location) {
        webServer.staticFiles(path, location);
    }

    @Override
    public void enableCors() {
        addMiddleware(new DefaultCorsMiddleware());
    }

    @Override
    public void enableCors(CorsMiddleware corsMiddleware) {
        addMiddleware(corsMiddleware);
    }

    @Override
    public void enableCors(Map<String, Object> options) {
        DefaultCorsMiddleware corsMiddleware = new DefaultCorsMiddleware();
        
        // 处理配置选项
        if (options.containsKey("allowedOrigins")) {
            corsMiddleware.setAllowedOrigins((List<String>) options.get("allowedOrigins"));
        }
        
        if (options.containsKey("allowedMethods")) {
            corsMiddleware.setAllowedMethods((List<String>) options.get("allowedMethods"));
        }
        
        if (options.containsKey("allowedHeaders")) {
            corsMiddleware.setAllowedHeaders((List<String>) options.get("allowedHeaders"));
        }
        
        if (options.containsKey("exposedHeaders")) {
            corsMiddleware.setExposedHeaders((List<String>) options.get("exposedHeaders"));
        }
        
        if (options.containsKey("allowCredentials")) {
            corsMiddleware.setAllowCredentials((boolean) options.get("allowCredentials"));
        }
        
        if (options.containsKey("maxAge")) {
            corsMiddleware.setMaxAge((long) options.get("maxAge"));
        }
        
        addMiddleware(corsMiddleware);
    }

    @Override
    public void exceptionHandler(Class<? extends Exception> exceptionClass, Consumer<Exception> handler) {
        // 实现异常处理逻辑
    }

    @Override
    public void errorHandler(Consumer<Exception> handler) {
        // 实现错误处理逻辑
    }

    @Override
    public void before(Consumer<Request> filter) {
        // 实现前置过滤器逻辑
    }

    @Override
    public void after(Consumer<Response> filter) {
        // 实现后置过滤器逻辑
    }

    @Override
    public void before(String path, Consumer<Request> filter) {
        // 实现路径前置过滤器逻辑
    }

    @Override
    public void after(String path, Consumer<Response> filter) {
        // 实现路径后置过滤器逻辑
    }

    @Override
    public void onStartup(Runnable callback) {
        // 实现启动回调逻辑
    }

    @Override
    public void onShutdown(Runnable callback) {
        // 实现关闭回调逻辑
    }

    @Override
    public void run() {
        start();
    }

    @Override
    public void run(int port) {
        webServer.setPort(port);
        start();
    }

    @Override
    public void run(String host, int port) {
        webServer.setHost(host);
        webServer.setPort(port);
        start();
    }

    @Override
    public void shutdown() {
        stop();
    }

    @Override
    public void start() {
        webServer.start();
    }

    @Override
    public void stop() {
        webServer.stop();
    }

    @Override
    public boolean isRunning() {
        return webServer.isRunning();
    }

    @Override
    public void addListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        listeners.remove(listener);
    }
}
