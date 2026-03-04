package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Session;
import ltd.idcu.est.web.api.SessionManager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSessionManager implements SessionManager {

    private final Map<String, Session> sessions;
    private final SessionManager.SessionStore sessionStore;
    private final SessionManager.SessionConfig sessionConfig;
    private final SecureRandom secureRandom;
    private int maxInactiveInterval = 1800;

    public DefaultSessionManager() {
        this.sessions = new ConcurrentHashMap<>();
        this.sessionStore = new InMemorySessionStore();
        this.sessionConfig = new DefaultSessionConfig();
        this.secureRandom = new SecureRandom();
    }

    @Override
    public Session createSession() {
        String sessionId = generateSessionId();
        DefaultSession session = new DefaultSession(sessionId);
        session.setMaxInactiveInterval(maxInactiveInterval);
        sessions.put(sessionId, session);
        sessionStore.save(session);
        return session;
    }

    @Override
    public Session getSession(String sessionId) {
        return getSession(sessionId, false);
    }

    @Override
    public Session getSession(String sessionId, boolean create) {
        if (sessionId == null || sessionId.isEmpty()) {
            return create ? createSession() : null;
        }
        Session session = sessions.get(sessionId);
        if (session == null) {
            session = sessionStore.load(sessionId);
            if (session != null) {
                sessions.put(sessionId, session);
            }
        }
        if (session != null && session.isValid()) {
            if (session instanceof DefaultSession) {
                ((DefaultSession) session).access();
            }
            return session;
        }
        if (session != null) {
            sessions.remove(sessionId);
            sessionStore.delete(sessionId);
        }
        return create ? createSession() : null;
    }

    @Override
    public void destroySession(String sessionId) {
        Session session = sessions.remove(sessionId);
        if (session != null) {
            session.invalidate();
            sessionStore.delete(sessionId);
        }
    }

    @Override
    public void destroySession(Session session) {
        if (session != null) {
            destroySession(session.getId());
        }
    }

    @Override
    public boolean hasSession(String sessionId) {
        return sessions.containsKey(sessionId) || sessionStore.exists(sessionId);
    }

    @Override
    public List<Session> getAllSessions() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public int getActiveSessionCount() {
        return sessions.size();
    }

    @Override
    public void setMaxInactiveInterval(int seconds) {
        this.maxInactiveInterval = seconds;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    @Override
    public void setSessionTimeout(int minutes) {
        setMaxInactiveInterval(minutes * 60);
    }

    @Override
    public int getSessionTimeout() {
        return getMaxInactiveInterval() / 60;
    }

    @Override
    public void cleanExpiredSessions() {
        List<String> expiredIds = new ArrayList<>();
        for (Map.Entry<String, Session> entry : sessions.entrySet()) {
            if (!entry.getValue().isValid()) {
                expiredIds.add(entry.getKey());
            }
        }
        for (String id : expiredIds) {
            destroySession(id);
        }
        sessionStore.cleanExpired();
    }

    @Override
    public String generateSessionId() {
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public boolean isValidSessionId(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return false;
        }
        try {
            Base64.getUrlDecoder().decode(sessionId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public SessionManager.SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    private static class InMemorySessionStore implements SessionStore {
        private final Map<String, Session> store = new ConcurrentHashMap<>();

        @Override
        public void save(Session session) {
            store.put(session.getId(), session);
        }

        @Override
        public Session load(String sessionId) {
            return store.get(sessionId);
        }

        @Override
        public void delete(String sessionId) {
            store.remove(sessionId);
        }

        @Override
        public boolean exists(String sessionId) {
            return store.containsKey(sessionId);
        }

        @Override
        public List<Session> loadAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void cleanExpired() {
            List<String> expiredIds = new ArrayList<>();
            for (Map.Entry<String, Session> entry : store.entrySet()) {
                if (!entry.getValue().isValid()) {
                    expiredIds.add(entry.getKey());
                }
            }
            for (String id : expiredIds) {
                store.remove(id);
            }
        }

        @Override
        public int count() {
            return store.size();
        }
    }

    private static class DefaultSessionConfig implements SessionConfig {
        private String sessionCookieName = "JSESSIONID";
        private int sessionTimeout = 30;
        private boolean cookieSecure = false;
        private boolean cookieHttpOnly = true;
        private String cookiePath = "/";
        private String cookieDomain = null;

        @Override
        public String getSessionCookieName() {
            return sessionCookieName;
        }

        @Override
        public void setSessionCookieName(String name) {
            this.sessionCookieName = name;
        }

        @Override
        public int getSessionTimeout() {
            return sessionTimeout;
        }

        @Override
        public void setSessionTimeout(int minutes) {
            this.sessionTimeout = minutes;
        }

        @Override
        public boolean isCookieSecure() {
            return cookieSecure;
        }

        @Override
        public void setCookieSecure(boolean secure) {
            this.cookieSecure = secure;
        }

        @Override
        public boolean isCookieHttpOnly() {
            return cookieHttpOnly;
        }

        @Override
        public void setCookieHttpOnly(boolean httpOnly) {
            this.cookieHttpOnly = httpOnly;
        }

        @Override
        public String getCookiePath() {
            return cookiePath;
        }

        @Override
        public void setCookiePath(String path) {
            this.cookiePath = path;
        }

        @Override
        public String getCookieDomain() {
            return cookieDomain;
        }

        @Override
        public void setCookieDomain(String domain) {
            this.cookieDomain = domain;
        }
    }
}
