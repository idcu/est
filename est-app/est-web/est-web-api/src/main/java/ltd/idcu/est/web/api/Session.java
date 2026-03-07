package ltd.idcu.est.web.api;

import java.time.Instant;
import java.util.Set;

public interface Session {

    String getId();

    Object getAttribute(String name);

    <T> T getAttribute(String name, Class<T> type);

    void setAttribute(String name, Object value);

    void removeAttribute(String name);

    Set<String> getAttributeNames();

    void invalidate();

    boolean isValid();

    Instant getCreationTime();

    Instant getLastAccessedTime();

    void setMaxInactiveInterval(int seconds);

    int getMaxInactiveInterval();

    boolean isNew();

    default <T> T getAttributeOrDefault(String name, T defaultValue) {
        T value = getAttribute(name, (Class<T>) defaultValue.getClass());
        return value != null ? value : defaultValue;
    }
}
