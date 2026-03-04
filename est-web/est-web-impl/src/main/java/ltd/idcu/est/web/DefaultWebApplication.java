package ltd.idcu.est.web;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.lifecycle.LifecycleListener;
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.api.Controller;
import ltd.idcu.est.web.api.RouteHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DefaultWebApplication implements WebApplication {

    private final String name;
    private final String version;
    private WebServer server;
    private Config config;
    private Router router;
    private final List<Middleware> middlewares;
    private final List<LifecycleListener> lifecycleListeners;
    private final Map<Class<? extends Exception>, Consumer<Exception>> exceptionHandlers;
    private Consumer<Exception> globalErrorHandler;
    private Runnable startupCallback;
    private Runnable shutdownCallback;
    private volatile boolean running = false;
    private View.ViewResolver viewResolver;
    private final Map<String, WebSocketHandler> webSocketHandlers;
    private final List<WebSocketEndpoint> webSocketEndpoints;

    public DefaultWebApplication() {
        this("EST Web Application", "1.0.0");
    }

    public DefaultWebApplication(String name, String version) {
        this.name = name;
        this.version = version;
        this.router = new DefaultRouter();
        this.middlewares = new ArrayList<>();
        this.lifecycleListeners = new ArrayList<>();
        this.exceptionHandlers = new HashMap<>();
        this.webSocketHandlers = new HashMap<>();
        this.webSocketEndpoints = new ArrayList<>();
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
        return server;
    }

    @Override
    public void setServer(WebServer server) {
        this.server = server;
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
    }

    @Override
    public List<Middleware> getMiddlewares() {
        return new ArrayList<>(middlewares);
    }

    @Override
    public void addMiddleware(Middleware middleware) {
        middlewares.add(middleware);
        if (server != null) {
            server.addMiddleware(middleware);
        }
    }

    @Override
    public void removeMiddleware(String name) {
        middlewares.removeIf(m -> m.getName().equals(name));
        if (server != null) {
            server.removeMiddleware(name);
        }
    }

    @Override
    public void use(Middleware... middlewares) {
        for (Middleware middleware : middlewares) {
            addMiddleware(middleware);
        }
    }

    @Override
    public void use(String path, Middleware middleware) {
        addMiddleware(new Middleware() {
            @Override
            public String getName() {
                return middleware.getName();
            }

            @Override
            public int getPriority() {
                return middleware.getPriority();
            }

            @Override
            public boolean before(Request request, Response response) {
                if (request.getPath().startsWith(path)) {
                    return middleware.before(request, response);
                }
                return true;
            }

            @Override
            public void after(Request request, Response response) {
                if (request.getPath().startsWith(path)) {
                    middleware.after(request, response);
                }
            }

            @Override
            public void onError(Request request, Response response, Exception e) {
                if (request.getPath().startsWith(path)) {
                    middleware.onError(request, response, e);
                }
            }

            @Override
            public boolean shouldApply(Request request) {
                return request.getPath().startsWith(path);
            }
        });
    }

    @Override
    public void routes(Consumer<Router> routerConfig) {
        routerConfig.accept(router);
    }

    @Override
    public void controller(String path, Class<? extends Controller> controllerClass) {
        router.get(path, "controller:" + controllerClass.getName());
    }

    @Override
    public void controller(String path, Controller controller) {
        router.get(path, "controller:" + controller.getClass().getName());
    }

    @Override
    public void restController(String path, Class<? extends RestController> controllerClass) {
        router.get(path, "restController:" + controllerClass.getName());
    }

    @Override
    public void restController(String path, RestController controller) {
        router.get(path, "restController:" + controller.getClass().getName());
    }

    public DefaultWebApplication get(String path, RouteHandler handler) {
        router.get(path, handler);
        return this;
    }

    public DefaultWebApplication post(String path, RouteHandler handler) {
        router.post(path, handler);
        return this;
    }

    public DefaultWebApplication put(String path, RouteHandler handler) {
        router.put(path, handler);
        return this;
    }

    public DefaultWebApplication delete(String path, RouteHandler handler) {
        router.delete(path, handler);
        return this;
    }

    public DefaultWebApplication patch(String path, RouteHandler handler) {
        router.patch(path, handler);
        return this;
    }

    @Override
    public void staticFiles(String path, String location) {
        if (server != null) {
            server.staticFiles(path, location);
        }
    }

    @Override
    public void enableCors() {
        enableCors(new DefaultCorsMiddleware());
    }

    @Override
    public void enableCors(CorsMiddleware corsMiddleware) {
        addMiddleware(corsMiddleware);
    }

    @Override
    public void enableCors(Map<String, Object> options) {
        DefaultCorsMiddleware corsMiddleware = new DefaultCorsMiddleware();
        if (options.containsKey("origins")) {
            @SuppressWarnings("unchecked")
            List<String> origins = (List<String>) options.get("origins");
            corsMiddleware.setAllowedOrigins(origins);
        }
        if (options.containsKey("methods")) {
            @SuppressWarnings("unchecked")
            List<String> methods = (List<String>) options.get("methods");
            corsMiddleware.setAllowedMethods(methods);
        }
        if (options.containsKey("headers")) {
            @SuppressWarnings("unchecked")
            List<String> headers = (List<String>) options.get("headers");
            corsMiddleware.setAllowedHeaders(headers);
        }
        if (options.containsKey("credentials")) {
            corsMiddleware.setAllowCredentials((Boolean) options.get("credentials"));
        }
        if (options.containsKey("maxAge")) {
            corsMiddleware.setMaxAge(((Number) options.get("maxAge")).longValue());
        }
        enableCors(corsMiddleware);
    }

    @Override
    public void exceptionHandler(Class<? extends Exception> exceptionClass, Consumer<Exception> handler) {
        exceptionHandlers.put(exceptionClass, handler);
    }

    @Override
    public void errorHandler(Consumer<Exception> handler) {
        this.globalErrorHandler = handler;
    }

    @Override
    public void before(Consumer<Request> filter) {
        before("/*", filter);
    }

    @Override
    public void after(Consumer<Response> filter) {
        after("/*", filter);
    }

    @Override
    public void before(String path, Consumer<Request> filter) {
        addMiddleware(new Middleware() {
            @Override
            public String getName() {
                return "before-filter-" + path;
            }

            @Override
            public int getPriority() {
                return 100;
            }

            @Override
            public boolean before(Request request, Response response) {
                filter.accept(request);
                return true;
            }

            @Override
            public boolean shouldApply(Request request) {
                return request.getPath().startsWith(path);
            }
        });
    }

    @Override
    public void after(String path, Consumer<Response> filter) {
        addMiddleware(new Middleware() {
            @Override
            public String getName() {
                return "after-filter-" + path;
            }

            @Override
            public int getPriority() {
                return 900;
            }

            @Override
            public void after(Request request, Response response) {
                filter.accept(response);
            }

            @Override
            public boolean shouldApply(Request request) {
                return request.getPath().startsWith(path);
            }
        });
    }

    @Override
    public void onStartup(Runnable callback) {
        this.startupCallback = callback;
    }

    @Override
    public void onShutdown(Runnable callback) {
        this.shutdownCallback = callback;
    }

    @Override
    public void run() {
        run(8080);
    }

    @Override
    public void run(int port) {
        run("0.0.0.0", port);
    }

    @Override
    public void run(String host, int port) {
        if (server == null) {
            server = new HttpServerImpl();
        }
        
        server.setHost(host);
        server.setPort(port);
        server.setRouter(router);
        
        if (viewResolver != null) {
            server.setViewResolver(viewResolver);
        }
        
        for (Middleware middleware : middlewares) {
            server.addMiddleware(middleware);
        }
        
        for (Map.Entry<String, WebSocketHandler> entry : webSocketHandlers.entrySet()) {
            server.websocket(entry.getKey(), entry.getValue());
        }
        for (WebSocketEndpoint endpoint : webSocketEndpoints) {
            server.websocket(endpoint);
        }
        
        server.errorHandler((request, response, e) -> {
            Consumer<Exception> handler = findExceptionHandler(e.getClass());
            if (handler != null) {
                handler.accept(e);
            } else if (globalErrorHandler != null) {
                globalErrorHandler.accept(e);
            } else {
                response.sendError(500, e.getMessage());
            }
        });
        
        server.initialize();
        server.start();
        running = true;
        
        if (startupCallback != null) {
            startupCallback.run();
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }

    @Override
    public void shutdown() {
        if (server != null && running) {
            server.stop();
            running = false;
            
            if (shutdownCallback != null) {
                shutdownCallback.run();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void addListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    @Override
    public void removeListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    @Override
    public void start() {
        run();
    }

    @Override
    public void stop() {
        shutdown();
    }

    @SuppressWarnings("unchecked")
    private Consumer<Exception> findExceptionHandler(Class<? extends Exception> exceptionClass) {
        Class<?> current = exceptionClass;
        while (current != null && Exception.class.isAssignableFrom(current)) {
            Consumer<Exception> handler = exceptionHandlers.get(current);
            if (handler != null) {
                return handler;
            }
            current = current.getSuperclass();
        }
        return null;
    }

    public static WebApplication create() {
        return new DefaultWebApplication();
    }

    public static WebApplication create(String name, String version) {
        return new DefaultWebApplication(name, version);
    }

    @Override
    public View.ViewResolver getViewResolver() {
        return viewResolver;
    }

    @Override
    public void setViewResolver(View.ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    @Override
    public void setViewEngine(View.ViewEngine viewEngine) {
        this.viewResolver = new DefaultViewResolver(viewEngine);
    }

    @Override
    public void setViewEngine(View.ViewEngine viewEngine, String templatePath) {
        this.viewResolver = new DefaultViewResolver(viewEngine, templatePath);
    }

    @Override
    public void setViewEngine(View.ViewEngine viewEngine, String templatePath, String templatePrefix, String templateSuffix) {
        this.viewResolver = new DefaultViewResolver(viewEngine, templatePath, templatePrefix, templateSuffix);
    }

    @Override
    public View createView(String viewName) {
        if (viewResolver != null) {
            return viewResolver.resolve(viewName);
        }
        DefaultView view = new DefaultView(viewName);
        view.setViewEngine(new StringTemplateEngine());
        return view;
    }

    @Override
    public View createView(String viewName, Map<String, Object> model) {
        View view = createView(viewName);
        view.setModel(model);
        return view;
    }

    @Override
    public void websocket(String path, WebSocketHandler handler) {
        if (server != null) {
            server.websocket(path, handler);
        } else {
            webSocketHandlers.put(path, handler);
        }
    }

    @Override
    public void websocket(WebSocketEndpoint endpoint) {
        if (server != null) {
            server.websocket(endpoint);
        } else {
            webSocketEndpoints.add(endpoint);
        }
    }
}
