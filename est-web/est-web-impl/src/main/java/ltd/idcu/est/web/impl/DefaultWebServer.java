package ltd.idcu.est.web.impl;

import ltd.idcu.est.core.api.Module;
import ltd.idcu.est.web.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DefaultWebServer implements WebServer {

    private int port = 8080;
    private String host = "localhost";
    private String contextPath = "/";
    private Router router;
    private List<Middleware> middlewares = new ArrayList<>();
    private View.ViewResolver viewResolver;

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public Router getRouter() {
        if (router == null) {
            router = new DefaultRouter();
        }
        return router;
    }

    @Override
    public void setRouter(Router router) {
        this.router = router;
    }

    @Override
    public List<Middleware> getMiddlewares() {
        return middlewares;
    }

    @Override
    public void addMiddleware(Middleware middleware) {
        middlewares.add(middleware);
    }

    @Override
    public void removeMiddleware(String name) {
        middlewares.removeIf(m -> m.getName().equals(name));
    }

    @Override
    public void clearMiddlewares() {
        middlewares.clear();
    }

    @Override
    public View.ViewResolver getViewResolver() {
        return viewResolver;
    }

    @Override
    public void setViewResolver(View.ViewResolver resolver) {
        this.viewResolver = resolver;
    }

    @Override
    public void registerController(String path, Controller controller) {
        // 实现控制器注册逻辑
    }

    @Override
    public void registerController(Class<? extends Controller> controllerClass) {
        // 实现控制器注册逻辑
    }

    @Override
    public void registerRestController(String path, RestController controller) {
        // 实现REST控制器注册逻辑
    }

    @Override
    public void registerRestController(Class<? extends RestController> controllerClass) {
        // 实现REST控制器注册逻辑
    }

    @Override
    public void staticFiles(String path, String location) {
        // 实现静态文件配置逻辑
    }

    @Override
    public void staticFiles(String path, String location, String... extensions) {
        // 实现静态文件配置逻辑
    }

    @Override
    public void errorHandler(ErrorHandler handler) {
        // 实现错误处理逻辑
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void before(String path, BiConsumer<Request, Response> filter) {
        // 实现前置过滤器逻辑
    }

    @Override
    public void after(String path, BiConsumer<Request, Response> filter) {
        // 实现后置过滤器逻辑
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void start() {
        // 实现服务器启动逻辑
    }

    @Override
    public void stop() {
        // 实现服务器停止逻辑
    }

    @Override
    public String getName() {
        return "web-server";
    }

    @Override
    public void initialize() {
        // 实现初始化逻辑
    }
}
