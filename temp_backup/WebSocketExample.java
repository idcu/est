package ltd.idcu.est.examples.web;

import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;
import ltd.idcu.est.web.api.WebSocketHandler;
import ltd.idcu.est.web.api.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketExample {

    public static void main(String[] args) {
        WebApplication app = new DefaultWebApplication();
        app.port(8080);

        final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

        app.websocket("/chat", new WebSocketHandler() {
            @Override
            public void onOpen(WebSocketSession session) {
                System.out.println("New connection: " + session.getId());
                sessions.put(session.getId(), session);
                try {
                    session.sendText("Welcome to the chat! Your ID: " + session.getId());
                    broadcast("User " + session.getId() + " joined the chat");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocketSession session, String message) {
                System.out.println("Received from " + session.getId() + ": " + message);
                broadcast("[" + session.getId() + "]: " + message);
            }

            @Override
            public void onMessage(WebSocketSession session, byte[] data) {
                System.out.println("Received binary data from " + session.getId());
            }

            @Override
            public void onClose(WebSocketSession session, int code, String reason) {
                System.out.println("Connection closed: " + session.getId() + ", code: " + code + ", reason: " + reason);
                sessions.remove(session.getId());
                broadcast("User " + session.getId() + " left the chat");
            }

            @Override
            public void onError(WebSocketSession session, Throwable error) {
                System.err.println("Error in connection " + session.getId() + ": " + error.getMessage());
                error.printStackTrace();
            }

            private void broadcast(String message) {
                for (WebSocketSession s : sessions.values()) {
                    try {
                        if (s.isOpen()) {
                            s.sendText(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        app.start();
        System.out.println("WebSocket server started at ws://localhost:8081/chat");
        System.out.println("HTTP server started at http://localhost:8080");
    }
}
