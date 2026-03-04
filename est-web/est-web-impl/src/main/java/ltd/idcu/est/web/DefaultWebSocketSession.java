package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.WebSocketSession;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultWebSocketSession implements WebSocketSession {

    private final String id;
    private final WebSocket webSocket;
    private final Request request;
    private final Map<String, Object> attributes;

    public DefaultWebSocketSession(WebSocket webSocket, Request request) {
        this.id = UUID.randomUUID().toString();
        this.webSocket = webSocket;
        this.request = request;
        this.attributes = new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOpen() {
        return webSocket != null && webSocket.isOpen();
    }

    @Override
    public void sendText(String message) throws IOException {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(message);
        } else {
            throw new IOException("WebSocket session is not open");
        }
    }

    @Override
    public void sendBinary(byte[] data) throws IOException {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(ByteBuffer.wrap(data));
        } else {
            throw new IOException("WebSocket session is not open");
        }
    }

    @Override
    public void close() throws IOException {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close();
        }
    }

    @Override
    public void close(int code, String reason) throws IOException {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.close(code, reason);
        }
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> stringAttributes = new HashMap<>();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (entry.getValue() instanceof String) {
                stringAttributes.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return stringAttributes;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public Request getRequest() {
        return request;
    }
}
