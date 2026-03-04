package ltd.idcu.est.web.api;

public interface WebSocketEndpoint {

    String getPath();

    WebSocketHandler getHandler();
}
