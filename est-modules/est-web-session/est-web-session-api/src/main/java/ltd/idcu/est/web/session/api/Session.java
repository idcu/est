package ltd.idcu.est.web.session.api;

import java.util.Map;

public interface Session {

    String getId();

    long getCreationTime();

    long getLastAccessedTime();

    void setLastAccessedTime(long lastAccessedTime);

    Object getAttribute(String name);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    Map<String, Object> getAttributes();

    void invalidate();

    boolean isInvalid();

    long getMaxInactiveInterval();

    void setMaxInactiveInterval(long interval);

    boolean isNew();
}
