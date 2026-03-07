package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.WebSocketHandler;
import ltd.idcu.est.gateway.api.WebSocketSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebSocketHandlerAdapter implements Runnable {
    private final DefaultWebSocketSession session;
    private final WebSocketHandler handler;
    private final Map<String, List<String>> headers;

    public WebSocketHandlerAdapter(DefaultWebSocketSession session, WebSocketHandler handler, Map<String, List<String>> headers) {
        this.session = session;
        this.handler = handler;
        this.headers = headers;
    }

    @Override
    public void run() {
        try {
            handler.onOpen(session, headers);
            while (session.isOpen()) {
                readFrame();
            }
        } catch (IOException e) {
            if (session.isOpen()) {
                handler.onError(session, e);
            }
        } finally {
            try {
                if (session.isOpen()) {
                    session.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void readFrame() throws IOException {
        DataInputStream in = session.getInputStream();
        
        int b1 = in.readUnsignedByte();
        int opcode = b1 & 0x0F;
        boolean fin = (b1 & 0x80) != 0;

        int b2 = in.readUnsignedByte();
        boolean masked = (b2 & 0x80) != 0;
        int length = b2 & 0x7F;

        if (length == 126) {
            length = in.readUnsignedShort();
        } else if (length == 127) {
            length = (int) in.readLong();
        }

        byte[] mask = new byte[4];
        if (masked) {
            in.readFully(mask);
        }

        byte[] payload = new byte[length];
        in.readFully(payload);

        if (masked) {
            for (int i = 0; i < payload.length; i++) {
                payload[i] ^= mask[i % 4];
            }
        }

        switch (opcode) {
            case 0x1:
                handler.onMessage(session, new String(payload, StandardCharsets.UTF_8));
                break;
            case 0x2:
                handler.onBinaryMessage(session, payload);
                break;
            case 0x8:
                int code = 1000;
                String reason = "";
                if (payload.length >= 2) {
                    code = ((payload[0] & 0xFF) << 8) | (payload[1] & 0xFF);
                    if (payload.length > 2) {
                        reason = new String(payload, 2, payload.length - 2, StandardCharsets.UTF_8);
                    }
                }
                handler.onClose(session, code, reason);
                session.close(code, reason);
                break;
            case 0x9:
                session.sendFrame(0xA, payload);
                break;
            case 0xA:
                break;
        }
    }
}
