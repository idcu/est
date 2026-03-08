package ltd.idcu.est.web.router.api;

@FunctionalInterface
public interface RouteHandler {
    void handle(Object request, Object response) throws Exception;
}
