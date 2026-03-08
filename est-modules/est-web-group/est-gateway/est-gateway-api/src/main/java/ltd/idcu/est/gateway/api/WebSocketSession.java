package ltd.idcu.est.gateway.api;

import java.io.IOException;

public interface WebSocketSession {
    String getId();
    
    void sendText(String message) throws IOException;
    
    void sendBinary(byte[] message) throws IOException;
    
    void close() throws IOException;
    
    void close(int code, String reason) throws IOException;
    
    boolean isOpen();
    
    void setAttribute(String name, Object value);
    
    Object getAttribute(String name);
    
    void removeAttribute(String name);
}
