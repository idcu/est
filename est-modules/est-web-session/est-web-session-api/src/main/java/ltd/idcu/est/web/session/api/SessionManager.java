package ltd.idcu.est.web.session.api;

import java.util.List;

public interface SessionManager {

    Session createSession();

    Session getSession(String sessionId);

    Session getSession(String sessionId, boolean create);

    void destroySession(String sessionId);

    void destroySession(Session session);

    boolean hasSession(String sessionId);

    List<Session> getAllSessions();

    int getActiveSessionCount();

    void setMaxInactiveInterval(int seconds);

    int getMaxInactiveInterval();

    void setSessionTimeout(int minutes);

    int getSessionTimeout();

    void cleanExpiredSessions();

    String generateSessionId();

    boolean isValidSessionId(String sessionId);

    interface SessionStore {
        void save(Session session);
        Session load(String sessionId);
        void delete(String sessionId);
        boolean exists(String sessionId);
        List<Session> loadAll();
        void cleanExpired();
        int count();
    }

    interface SessionConfig {
        String getSessionCookieName();
        void setSessionCookieName(String name);
        int getSessionTimeout();
        void setSessionTimeout(int minutes);
        boolean isCookieSecure();
        void setCookieSecure(boolean secure);
        boolean isCookieHttpOnly();
        void setCookieHttpOnly(boolean httpOnly);
        String getCookiePath();
        void setCookiePath(String path);
        String getCookieDomain();
        void setCookieDomain(String domain);
    }
}
