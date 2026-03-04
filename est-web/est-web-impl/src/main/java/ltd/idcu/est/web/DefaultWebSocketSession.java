package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultWebSocketSession implements WebSocketSession {

    private final String id;
    private final Request request;
    private final Map<String, Object> attributes;

    public DefaultWebSocketSession(Object webSocket, Request request) {
        this.id = UUID.randomUUID().toString();
        this.request = request;
        this.attributes = new HashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void sendText(String message) throws IOException {
        throw new IOException("WebSocket not implemented");
    }

    @Override
    public void sendBinary(byte[] data) throws IOException {
        throw new IOException("WebSocket not implemented");
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void close(int code, String reason) throws IOException {
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
