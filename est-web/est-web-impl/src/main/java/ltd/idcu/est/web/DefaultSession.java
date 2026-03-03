package ltd.idcu.est.web;

import ltd.idcu.est.web.api.Session;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSession implements Session {

    private final String id;
    private final Map<String, Object> attributes;
    private final Instant creationTime;
    private Instant lastAccessedTime;
    private int maxInactiveInterval;
    private volatile boolean valid;
    private volatile boolean isNew;

    public DefaultSession(String id) {
        this.id = id;
        this.attributes = new ConcurrentHashMap<>();
        this.creationTime = Instant.now();
        this.lastAccessedTime = creationTime;
        this.maxInactiveInterval = 1800;
        this.valid = true;
        this.isNew = true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getAttribute(String name) {
        checkValid();
        return attributes.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name, Class<T> type) {
        checkValid();
        Object value = attributes.get(name);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        checkValid();
        if (value != null) {
            attributes.put(name, value);
        } else {
            attributes.remove(name);
        }
    }

    @Override
    public void removeAttribute(String name) {
        checkValid();
        attributes.remove(name);
    }

    @Override
    public Set<String> getAttributeNames() {
        checkValid();
        return new HashSet<>(attributes.keySet());
    }

    @Override
    public void invalidate() {
        valid = false;
        attributes.clear();
    }

    @Override
    public boolean isValid() {
        if (!valid) {
            return false;
        }
        if (maxInactiveInterval > 0) {
            Instant now = Instant.now();
            long elapsedSeconds = now.getEpochSecond() - lastAccessedTime.getEpochSecond();
            if (elapsedSeconds > maxInactiveInterval) {
                valid = false;
                return false;
            }
        }
        return true;
    }

    @Override
    public Instant getCreationTime() {
        checkValid();
        return creationTime;
    }

    @Override
    public Instant getLastAccessedTime() {
        checkValid();
        return lastAccessedTime;
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
    public boolean isNew() {
        checkValid();
        return isNew;
    }

    public void access() {
        this.lastAccessedTime = Instant.now();
        this.isNew = false;
    }

    private void checkValid() {
        if (!isValid()) {
            throw new IllegalStateException("Session is invalid");
        }
    }

    Map<String, Object> getAttributesMap() {
        return attributes;
    }
}
