package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.WebSocketSession;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultWebSocketSession implements WebSocketSession {
    private final String id;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final AtomicBoolean open;
    private final Map<String, Object> attributes;

    public DefaultWebSocketSession(String id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        this.open = new AtomicBoolean(true);
        this.attributes = new ConcurrentHashMap<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void sendText(String message) throws IOException {
        sendFrame(0x1, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void sendBinary(byte[] message) throws IOException {
        sendFrame(0x2, message);
    }

    @Override
    public void close() throws IOException {
        close(1000, "Normal closure");
    }

    @Override
    public void close(int code, String reason) throws IOException {
        if (open.compareAndSet(true, false)) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                baos.write((byte) (code >> 8));
                baos.write((byte) (code & 0xFF));
                if (reason != null && !reason.isEmpty()) {
                    baos.write(reason.getBytes(StandardCharsets.UTF_8));
                }
                sendFrame(0x8, baos.toByteArray());
            } finally {
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        }
    }

    @Override
    public boolean isOpen() {
        return open.get();
    }

    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    void sendFrame(int opcode, byte[] payload) throws IOException {
        ByteArrayOutputStream frame = new ByteArrayOutputStream();
        frame.write(0x80 | opcode);

        int length = payload.length;
        if (length <= 125) {
            frame.write(length);
        } else if (length <= 65535) {
            frame.write(126);
            frame.write((byte) (length >> 8));
            frame.write((byte) (length & 0xFF));
        } else {
            frame.write(127);
            for (int i = 7; i >= 0; i--) {
                frame.write((byte) ((length >> (i * 8)) & 0xFF));
            }
        }

        frame.write(payload);
        outputStream.write(frame.toByteArray());
        outputStream.flush();
    }

    DataInputStream getInputStream() {
        return inputStream;
    }

    static String generateAcceptKey(String key) {
        try {
            String combined = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(combined.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
