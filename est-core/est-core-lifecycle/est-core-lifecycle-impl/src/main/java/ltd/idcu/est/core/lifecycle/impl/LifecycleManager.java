package ltd.idcu.est.core.lifecycle.impl;

import ltd.idcu.est.core.lifecycle.api.Lifecycle;
import ltd.idcu.est.core.lifecycle.api.LifecycleListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifecycleManager {
    
    private final Map<String, Lifecycle> lifecycles = new ConcurrentHashMap<>();
    private final List<LifecycleListener> globalListeners = new CopyOnWriteArrayList<>();
    
    public void register(String name, Lifecycle lifecycle) {
        if (name == null || lifecycle == null) {
            throw new IllegalArgumentException("Name and lifecycle cannot be null");
        }
        lifecycles.put(name, lifecycle);
    }
    
    public void unregister(String name) {
        if (name != null) {
            lifecycles.remove(name);
        }
    }
    
    public Lifecycle get(String name) {
        if (name == null) {
            return null;
        }
        return lifecycles.get(name);
    }
    
    public void startAll() {
        for (Lifecycle lifecycle : lifecycles.values()) {
            if (!lifecycle.isRunning()) {
                lifecycle.start();
            }
        }
    }
    
    public void stopAll() {
        for (Lifecycle lifecycle : lifecycles.values()) {
            if (lifecycle.isRunning()) {
                lifecycle.stop();
            }
        }
    }
    
    public void start(String name) {
        Lifecycle lifecycle = get(name);
        if (lifecycle != null && !lifecycle.isRunning()) {
            lifecycle.start();
        }
    }
    
    public void stop(String name) {
        Lifecycle lifecycle = get(name);
        if (lifecycle != null && lifecycle.isRunning()) {
            lifecycle.stop();
        }
    }
    
    public void addGlobalListener(LifecycleListener listener) {
        if (listener != null) {
            globalListeners.add(listener);
            for (Lifecycle lifecycle : lifecycles.values()) {
                lifecycle.addListener(listener);
            }
        }
    }
    
    public void removeGlobalListener(LifecycleListener listener) {
        if (listener != null) {
            globalListeners.remove(listener);
            for (Lifecycle lifecycle : lifecycles.values()) {
                lifecycle.removeListener(listener);
            }
        }
    }
    
    public boolean isRunning(String name) {
        Lifecycle lifecycle = get(name);
        return lifecycle != null && lifecycle.isRunning();
    }
    
    public int size() {
        return lifecycles.size();
    }
    
    public void clear() {
        stopAll();
        lifecycles.clear();
        globalListeners.clear();
    }
}
