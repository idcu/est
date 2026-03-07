package ltd.idcu.est.web.api;

@FunctionalInterface
public interface ErrorHandler {
    void handle(Request request, Response response, Exception exception);
}