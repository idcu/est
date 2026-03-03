package ltd.idcu.est.web.impl;

import ltd.idcu.est.web.api.Session;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultSession implements Session {

    private String id;
    private Map<String, Object> attributes = new HashMap<>();
    private Instant creationTime;
    private Instant lastAccessedTime;
    private int maxInactiveInterval;
    private boolean invalidated;

    public DefaultSession(String id) {
        this.id = id;
        this.creationTime = Instant.now();
        this.lastAccessedTime = creationTime;
        this.maxInactiveInterval = 1800; // 默认30分钟
        this.invalidated = false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getAttribute(String name) {
        if (!isValid()) {
            return null;
        }
        lastAccessedTime = Instant.now();
        return attributes.get(name);
    }

    @Override
    public <T> T getAttribute(String name, Class<T> type) {
        if (!isValid()) {
            return null;
        }
        lastAccessedTime = Instant.now();
        Object value = attributes.get(name);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    @Override
    public void setAttribute(String name, Object value) {
        if (!isValid()) {
            return;
        }
        lastAccessedTime = Instant.now();
        attributes.put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        if (!isValid()) {
            return;
        }
        lastAccessedTime = Instant.now();
        attributes.remove(name);
    }

    @Override
    public Set<String> getAttributeNames() {
        if (!isValid()) {
            return Set.of();
        }
        lastAccessedTime = Instant.now();
        return attributes.keySet();
    }

    @Override
    public void invalidate() {
        invalidated = true;
        attributes.clear();
    }

    @Override
    public boolean isValid() {
        if (invalidated) {
            return false;
        }
        if (maxInactiveInterval <= 0) {
            return true;
        }
        return Instant.now().toEpochMilli() - lastAccessedTime.toEpochMilli() <= maxInactiveInterval * 1000L;
    }

    @Override
    public Instant getCreationTime() {
        return creationTime;
    }

    @Override
    public Instant getLastAccessedTime() {
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
        return lastAccessedTime == creationTime;
    }
}
