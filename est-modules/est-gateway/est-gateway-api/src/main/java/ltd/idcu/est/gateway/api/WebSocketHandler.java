package ltd.idcu.est.gateway.api;

import java.util.List;
import java.util.Map;

public interface WebSocketHandler {
    void onOpen(WebSocketSession session, Map<String, List<String>> headers);
    
    void onMessage(WebSocketSession session, String message);
    
    void onBinaryMessage(WebSocketSession session, byte[] message);
    
    void onClose(WebSocketSession session, int code, String reason);
    
    void onError(WebSocketSession session, Throwable error);
}
