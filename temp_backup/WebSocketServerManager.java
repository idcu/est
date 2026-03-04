package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.WebSocketEndpoint;
import ltd.idcu.est.web.api.WebSocketHandler;
import ltd.idcu.est.web.api.WebSocketSession;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class WebSocketServerManager {

    private final Map<String, WebSocketHandler> handlers;
    private WebSocketServer webSocketServer;
    private int port;
    private String host;
    private volatile boolean running = false;

    public WebSocketServerManager() {
        this.handlers = new HashMap<>();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void addHandler(String path, WebSocketHandler handler) {
        handlers.put(path, handler);
    }

    public void addEndpoint(WebSocketEndpoint endpoint) {
        handlers.put(endpoint.getPath(), endpoint.getHandler());
    }

    public void initialize() {
        InetSocketAddress address = new InetSocketAddress(host, port + 1);
        webSocketServer = new WebSocketServer(address) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                String path = handshake.getResourceDescriptor();
                WebSocketHandler handler = handlers.get(path);
                if (handler != null) {
                    Request request = createMockRequest(path);
                    WebSocketSession session = new DefaultWebSocketSession(conn, request);
                    conn.setAttachment(session);
                    handler.onOpen(session);
                } else {
                    conn.close(1000, "Endpoint not found");
                }
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                WebSocketSession session = conn.getAttachment();
                if (session != null) {
                    String path = session.getRequest().getPath();
                    WebSocketHandler handler = handlers.get(path);
                    if (handler != null) {
                        handler.onClose(session, code, reason);
                    }
                }
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                WebSocketSession session = conn.getAttachment();
                if (session != null) {
                    String path = session.getRequest().getPath();
                    WebSocketHandler handler = handlers.get(path);
                    if (handler != null) {
                        handler.onMessage(session, message);
                    }
                }
            }

            @Override
            public void onMessage(WebSocket conn, byte[] message) {
                WebSocketSession session = conn.getAttachment();
                if (session != null) {
                    String path = session.getRequest().getPath();
                    WebSocketHandler handler = handlers.get(path);
                    if (handler != null) {
                        handler.onMessage(session, message);
                    }
                }
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                if (conn != null) {
                    WebSocketSession session = conn.getAttachment();
                    if (session != null) {
                        String path = session.getRequest().getPath();
                        WebSocketHandler handler = handlers.get(path);
                        if (handler != null) {
                            handler.onError(session, ex);
                        }
                    }
                }
            }

            @Override
            public void onStart() {
                running = true;
            }
        };
    }

    public void start() {
        if (webSocketServer != null && !running) {
            webSocketServer.start();
        }
    }

    public void stop() {
        if (webSocketServer != null && running) {
            try {
                webSocketServer.stop();
                running = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    private Request createMockRequest(String path) {
        return new Request() {
            @Override
            public String getPath() {
                return path;
            }

            @Override
            public ltd.idcu.est.web.api.HttpMethod getMethod() {
                return ltd.idcu.est.web.api.HttpMethod.GET;
            }

            @Override
            public Map<String, String> getHeaders() {
                return new HashMap<>();
            }

            @Override
            public String getHeader(String name) {
                return null;
            }

            @Override
            public Map<String, String> getParameters() {
                return new HashMap<>();
            }

            @Override
            public String getParameter(String name) {
                return null;
            }

            @Override
            public Map<String, String> getCookies() {
                return new HashMap<>();
            }

            @Override
            public String getCookie(String name) {
                return null;
            }

            @Override
            public ltd.idcu.est.web.api.Session getSession() {
                return null;
            }

            @Override
            public ltd.idcu.est.web.api.Session getSession(boolean create) {
                return null;
            }

            @Override
            public String getBody() {
                return null;
            }

            @Override
            public byte[] getBodyAsBytes() {
                return new byte[0];
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public Map<String, String> getPathVariables() {
                return new HashMap<>();
            }

            @Override
            public String getPathVariable(String name) {
                return null;
            }

            @Override
            public void addPathVariables(Map<String, String> variables) {
            }
        };
    }
}
