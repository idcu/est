package ltd.idcu.est.web.api;

import ltd.idcu.est.core.api.Module;

import java.util.List;
import java.util.function.BiConsumer;

public interface WebServer extends Module {

    int getPort();

    void setPort(int port);

    String getHost();

    void setHost(String host);

    String getContextPath();

    void setContextPath(String contextPath);

    Router getRouter();

    void setRouter(Router router);

    List<Middleware> getMiddlewares();

    void addMiddleware(Middleware middleware);

    void removeMiddleware(String name);

    void clearMiddlewares();

    View.ViewResolver getViewResolver();

    void setViewResolver(View.ViewResolver resolver);

    void registerController(String path, Controller controller);

    void registerController(Class<? extends Controller> controllerClass);

    void registerRestController(String path, RestController controller);

    void registerRestController(Class<? extends RestController> controllerClass);

    void staticFiles(String path, String location);

    void staticFiles(String path, String location, String... extensions);

    void errorHandler(BiConsumer<Request, Response, Exception> handler);

    void before(String path, BiConsumer<Request, Response> filter);

    void after(String path, BiConsumer<Request, Response> filter);

    default boolean isStarted() {
        return isRunning();
    }

    default String getUrl() {
        String host = getHost();
        int port = getPort();
        String contextPath = getContextPath();
        return String.format("http://%s:%d%s", host, port, contextPath);
    }

    default String getSecureUrl() {
        String host = getHost();
        int port = getPort();
        String contextPath = getContextPath();
        return String.format("https://%s:%d%s", host, port, contextPath);
    }
}
