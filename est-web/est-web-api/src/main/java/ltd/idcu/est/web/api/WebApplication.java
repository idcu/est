package ltd.idcu.est.web.api;

import ltd.idcu.est.core.api.Config;
import ltd.idcu.est.core.api.lifecycle.Lifecycle;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface WebApplication extends Lifecycle {

    String getName();

    String getVersion();

    WebServer getServer();

    void setServer(WebServer server);

    Config getConfig();

    void setConfig(Config config);

    Router getRouter();

    void setRouter(Router router);

    List<Middleware> getMiddlewares();

    void addMiddleware(Middleware middleware);

    void removeMiddleware(String name);

    void use(Middleware... middlewares);

    void use(String path, Middleware middleware);

    void routes(Consumer<Router> routerConfig);

    void controller(String path, Class<? extends Controller> controllerClass);

    void controller(String path, Controller controller);

    void restController(String path, Class<? extends RestController> controllerClass);

    void restController(String path, RestController controller);

    void staticFiles(String path, String location);

    void enableCors();

    void enableCors(CorsMiddleware corsMiddleware);

    void enableCors(Map<String, Object> options);

    void exceptionHandler(Class<? extends Exception> exceptionClass, Consumer<Exception> handler);

    void errorHandler(Consumer<Exception> handler);

    void before(Consumer<Request> filter);

    void after(Consumer<Response> filter);

    void before(String path, Consumer<Request> filter);

    void after(String path, Consumer<Response> filter);

    void onStartup(Runnable callback);

    void onShutdown(Runnable callback);

    void run();

    void run(int port);

    void run(String host, int port);

    void shutdown();

    boolean isRunning();

    static WebApplication create() {
        return create("EST Application", "1.0.0");
    }

    static WebApplication create(String name) {
        return create(name, "1.0.0");
    }

    static WebApplication create(String name, String version);
}
