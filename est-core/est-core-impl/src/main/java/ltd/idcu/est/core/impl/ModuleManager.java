package ltd.idcu.est.core.impl;

import ltd.idcu.est.core.api.Module;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ModuleManager {
    
    private final Map<String, Module> modules = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;
    
    public void registerModule(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module cannot be null");
        }
        modules.put(module.getName(), module);
    }
    
    public void unregisterModule(String name) {
        if (name != null) {
            Module module = modules.remove(name);
            if (module != null && module.isRunning()) {
                module.stop();
            }
        }
    }
    
    public Optional<Module> getModule(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(modules.get(name));
    }
    
    public Collection<Module> getAllModules() {
        return modules.values();
    }
    
    public void initializeAll() {
        if (initialized) {
            return;
        }
        for (Module module : modules.values()) {
            module.initialize();
        }
        initialized = true;
    }
    
    public void startAll() {
        for (Module module : modules.values()) {
            if (!module.isRunning()) {
                module.start();
            }
        }
    }
    
    public void stopAll() {
        for (Module module : modules.values()) {
            if (module.isRunning()) {
                module.stop();
            }
        }
    }
    
    public boolean contains(String name) {
        return name != null && modules.containsKey(name);
    }
    
    public int size() {
        return modules.size();
    }
    
    public void clear() {
        stopAll();
        modules.clear();
        initialized = false;
    }
}
