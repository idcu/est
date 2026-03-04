package ltd.idcu.est.web.api;

import java.io.IOException;
import java.util.Map;

public interface WebSocketSession {

    String getId();

    boolean isOpen();

    void sendText(String message) throws IOException;

    void sendBinary(byte[] data) throws IOException;

    void close() throws IOException;

    void close(int code, String reason) throws IOException;

    Map<String, String> getAttributes();

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    Request getRequest();
}
