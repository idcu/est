package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.Session;
import ltd.idcu.est.web.api.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultSessionManager implements SessionManager {

    private SessionStore sessionStore;
    private int maxInactiveInterval = 1800; // 30分钟
    private SessionConfig sessionConfig;

    public DefaultSessionManager() {
        this.sessionStore = new InMemorySessionStore();
        this.sessionConfig = new DefaultSessionConfig();
    }

    @Override
    public Session createSession() {
        String sessionId = generateSessionId();
        Session session = new DefaultSession(sessionId);
        session.setMaxInactiveInterval(maxInactiveInterval);
        sessionStore.save(session);
        return session;
    }

    @Override
    public Session getSession(String sessionId) {
        return getSession(sessionId, false);
    }

    @Override
    public Session getSession(String sessionId, boolean create) {
        Session session = sessionStore.load(sessionId);
        if (session == null && create) {
            return createSession();
        }
        return session;
    }

    @Override
    public void destroySession(String sessionId) {
        sessionStore.delete(sessionId);
    }

    @Override
    public void destroySession(Session session) {
        if (session != null) {
            sessionStore.delete(session.getId());
        }
    }

    @Override
    public boolean hasSession(String sessionId) {
        return sessionStore.exists(sessionId);
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionStore.loadAll();
    }

    @Override
    public int getActiveSessionCount() {
        return sessionStore.count();
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
        this.maxInactiveInterval = minutes * 60;
    }

    @Override
    public int getSessionTimeout() {
        return maxInactiveInterval / 60;
    }

    @Override
    public void cleanExpiredSessions() {
        sessionStore.cleanExpired();
    }

    @Override
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isValidSessionId(String sessionId) {
        return sessionId != null && sessionId.length() > 0;
    }

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public SessionConfig getSessionConfig() {
        return sessionConfig;
    }

    public void setSessionConfig(SessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
    }

    private static class InMemorySessionStore implements SessionStore {

        private Map<String, Session> sessions = new HashMap<>();

        @Override
        public void save(Session session) {
            sessions.put(session.getId(), session);
        }

        @Override
        public Session load(String sessionId) {
            Session session = sessions.get(sessionId);
            if (session != null && !session.isValid()) {
                delete(sessionId);
                return null;
            }
            return session;
        }

        @Override
        public void delete(String sessionId) {
            sessions.remove(sessionId);
        }

        @Override
        public boolean exists(String sessionId) {
            Session session = sessions.get(sessionId);
            if (session != null && !session.isValid()) {
                delete(sessionId);
                return false;
            }
            return sessions.containsKey(sessionId);
        }

        @Override
        public List<Session> loadAll() {
            cleanExpired();
            return new ArrayList<>(sessions.values());
        }

        @Override
        public void cleanExpired() {
            List<String> expiredSessionIds = new ArrayList<>();
            for (Session session : sessions.values()) {
                if (!session.isValid()) {
                    expiredSessionIds.add(session.getId());
                }
            }
            for (String sessionId : expiredSessionIds) {
                sessions.remove(sessionId);
            }
        }

        @Override
        public int count() {
            cleanExpired();
            return sessions.size();
        }
    }

    private static class DefaultSessionConfig implements SessionConfig {

        private String sessionCookieName = "EST_SESSION";
        private int sessionTimeout = 30; // 30分钟
        private boolean cookieSecure = false;
        private boolean cookieHttpOnly = true;
        private String cookiePath = "/";
        private String cookieDomain = "";

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
