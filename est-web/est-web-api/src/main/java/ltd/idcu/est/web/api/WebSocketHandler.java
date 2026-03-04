package ltd.idcu.est.web.api;

public interface WebSocketHandler {

    void onOpen(WebSocketSession session);

    void onMessage(WebSocketSession session, String message);

    void onMessage(WebSocketSession session, byte[] data);

    void onClose(WebSocketSession session, int code, String reason);

    void onError(WebSocketSession session, Throwable error);
}
