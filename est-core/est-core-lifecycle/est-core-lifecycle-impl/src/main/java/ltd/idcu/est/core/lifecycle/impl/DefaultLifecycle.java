package ltd.idcu.est.core.lifecycle.impl;

import ltd.idcu.est.core.lifecycle.api.Lifecycle;
import ltd.idcu.est.core.lifecycle.api.LifecycleListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultLifecycle implements Lifecycle {
    
    private final List<LifecycleListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean running = false;
    
    @Override
    public void start() {
        if (running) {
            return;
        }
        running = true;
        notifyStart();
    }
    
    @Override
    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        notifyStop();
    }
    
    @Override
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void addListener(LifecycleListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    @Override
    public void removeListener(LifecycleListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
    
    private void notifyStart() {
        for (LifecycleListener listener : listeners) {
            try {
                listener.onStart();
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                    .uncaughtException(Thread.currentThread(), e);
            }
        }
    }
    
    private void notifyStop() {
        for (LifecycleListener listener : listeners) {
            try {
                listener.onStop();
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                    .uncaughtException(Thread.currentThread(), e);
            }
        }
    }
}
