package ltd.idcu.est.web.api;

@FunctionalInterface
public interface RouteHandler {
    void handle(Request request, Response response) throws Exception;
}
