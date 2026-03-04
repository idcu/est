package ltd.idcu.est.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ltd.idcu.est.web.api.*;
import ltd.idcu.est.web.api.Controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class HttpServerImpl implements WebServer {

    private final String name;
    private final String version;
    private HttpServer server;
    private int port = 8080;
    private String host = "0.0.0.0";
    private String contextPath = "";
    private Router router;
    private final List<Middleware> middlewares;
    private View.ViewResolver viewResolver;
    private final DefaultSessionManager sessionManager;
    private final List<StaticFileHandler> staticFileHandlers;
    private ErrorHandler errorHandler;
    private volatile boolean running = false;

    public HttpServerImpl() {
        this.name = "EST HTTP Server";
        this.version = "1.0.0";
        this.router = new DefaultRouter();
        this.middlewares = new ArrayList<>();
        this.sessionManager = new DefaultSessionManager();
        this.staticFileHandlers = new ArrayList<>();
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
    public void initialize() {
        try {
            server = HttpServer.create(new InetSocketAddress(host, port), 0);
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            server.createContext("/", new RequestHandler());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize HTTP server", e);
        }
    }

    @Override
    public void start() {
        if (server == null) {
            initialize();
        }
        server.start();
        sessionManager.startCleanupTask();
        running = true;
    }

    @Override
    public void stop() {
        if (server != null) {
            sessionManager.stopCleanupTask();
            server.stop(0);
            running = false;
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

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
        this.contextPath = contextPath != null ? contextPath : "";
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
        middlewares.sort(Comparator.comparingInt(Middleware::getPriority));
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
        router.route(path, HttpMethod.GET, "controller:" + controller.getClass().getName());
    }

    @Override
    public void registerController(Class<? extends Controller> controllerClass) {
        try {
            Controller controller = controllerClass.getDeclaredConstructor().newInstance();
            registerController("/", controller);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register controller", e);
        }
    }

    @Override
    public void registerRestController(String path, RestController controller) {
        router.route(path, HttpMethod.GET, "restController:" + controller.getClass().getName());
    }

    @Override
    public void registerRestController(Class<? extends RestController> controllerClass) {
        try {
            RestController controller = controllerClass.getDeclaredConstructor().newInstance();
            registerRestController("/", controller);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register rest controller", e);
        }
    }

    @Override
    public void staticFiles(String path, String location) {
        DefaultStaticFileHandler handler = new DefaultStaticFileHandler(path, location);
        staticFileHandlers.add(handler);
    }

    @Override
    public void staticFiles(String path, String location, String... extensions) {
        DefaultStaticFileHandler handler = new DefaultStaticFileHandler(path, location);
        handler.setAllowedExtensions(List.of(extensions));
        staticFileHandlers.add(handler);
    }

    @Override
    public void errorHandler(ErrorHandler handler) {
        this.errorHandler = handler;
    }

    @Override
    public void before(String path, BiConsumer<Request, Response> filter) {
        addMiddleware(new Middleware() {
            @Override
            public String getName() {
                return "before-" + path;
            }

            @Override
            public int getPriority() {
                return 100;
            }

            @Override
            public boolean before(Request request, Response response) {
                if (request.getPath().startsWith(path)) {
                    filter.accept(request, response);
                }
                return true;
            }

            @Override
            public boolean shouldApply(Request request) {
                return request.getPath().startsWith(path);
            }
        });
    }

    @Override
    public void after(String path, BiConsumer<Request, Response> filter) {
        addMiddleware(new Middleware() {
            @Override
            public String getName() {
                return "after-" + path;
            }

            @Override
            public int getPriority() {
                return 900;
            }

            @Override
            public void after(Request request, Response response) {
                if (request.getPath().startsWith(path)) {
                    filter.accept(request, response);
                }
            }

            @Override
            public boolean shouldApply(Request request) {
                return request.getPath().startsWith(path);
            }
        });
    }

    public DefaultSessionManager getSessionManager() {
        return sessionManager;
    }

    private class RequestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            DefaultRequest request = new DefaultRequest(exchange);
            DefaultResponse response = new DefaultResponse(exchange);
            request.setSessionManager(sessionManager);

            try {
                String path = request.getPath();
                
                for (StaticFileHandler handler : staticFileHandlers) {
                    if (path.startsWith(handler.getPathPrefix())) {
                        handler.serve(path, request, response);
                        response.commit();
                        return;
                    }
                }

                Route route = router.match(path, request.getMethod());
                if (route == null) {
                    response.sendError(404, "Not Found");
                    response.commit();
                    return;
                }

                if (route.hasPathVariables()) {
                    Map<String, String> variables = route.extractPathVariables(path);
                    request.addPathVariables(variables);
                }

                boolean proceed = true;
                for (Middleware middleware : middlewares) {
                    if (middleware.shouldApply(request) || middleware.isGlobal()) {
                        proceed = middleware.before(request, response);
                        if (!proceed) {
                            break;
                        }
                    }
                }

                if (proceed) {
                    handleRoute(route, request, response);
                }

                for (int i = middlewares.size() - 1; i >= 0; i--) {
                    Middleware middleware = middlewares.get(i);
                    if (middleware.shouldApply(request) || middleware.isGlobal()) {
                        middleware.after(request, response);
                    }
                }

                setSessionCookie(request, response);

                response.commit();
            } catch (Exception e) {
                handleError(request, response, e);
                response.commit();
            }
        }

        private void setSessionCookie(DefaultRequest request, DefaultResponse response) {
            ltd.idcu.est.web.api.Session session = request.getSession(false);
            if (session != null && session.isValid()) {
                String sessionId = session.getId();
                String existingSessionId = request.getCookie("JSESSIONID");
                if (existingSessionId == null || !existingSessionId.equals(sessionId)) {
                    response.setCookie("JSESSIONID", sessionId, sessionManager.getMaxInactiveInterval(), 
                                      "/", null, request.isSecure(), true);
                }
            }
        }

        private void handleRoute(Route route, DefaultRequest request, DefaultResponse response) throws Exception {
            RouteHandler routeHandler = route.getRouteHandler();
            if (routeHandler != null) {
                routeHandler.handle(request, response);
                return;
            }

            String handler = route.getHandler();
            if (handler == null || handler.isEmpty()) {
                response.sendError(500, "No handler defined for route");
                return;
            }

            response.json(Map.of(
                "path", request.getPath(),
                "method", request.getMethod().getMethod(),
                "handler", handler,
                "params", request.getParameters()
            ));
        }

        private void handleError(DefaultRequest request, DefaultResponse response, Exception e) {
            if (errorHandler != null) {
                errorHandler.handle(request, response, e);
            } else {
                response.sendError(500, "Internal Server Error: " + e.getMessage());
            }
            
            for (Middleware middleware : middlewares) {
                if (middleware.shouldApply(request) || middleware.isGlobal()) {
                    middleware.onError(request, response, e);
                }
            }
        }
    }
}
