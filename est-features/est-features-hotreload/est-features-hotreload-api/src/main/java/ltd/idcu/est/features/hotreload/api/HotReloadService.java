package ltd.idcu.est.features.hotreload.api;

import java.nio.file.Path;
import java.util.List;

public interface HotReloadService {
    void start();
    
    void stop();
    
    boolean isRunning();
    
    void addWatchPath(Path path);
    
    void removeWatchPath(Path path);
    
    List<Path> getWatchPaths();
    
    void addListener(HotReloadListener listener);
    
    void removeListener(HotReloadListener listener);
    
    void setPollInterval(long millis);
    
    long getPollInterval();
    
    void triggerReload();
}
