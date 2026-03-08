package ltd.idcu.est.hotreload.impl;

import ltd.idcu.est.hotreload.api.HotReloadListener;
import ltd.idcu.est.hotreload.api.HotReloadService;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultHotReloadService implements HotReloadService {

    private final List<Path> watchPaths = new CopyOnWriteArrayList<>();
    private final List<HotReloadListener> listeners = new CopyOnWriteArrayList<>();
    private final Map<Path, Long> lastModifiedTimes = new HashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile Thread watchThread;
    private volatile WatchService watchService;
    private long pollInterval = 1000;

    public DefaultHotReloadService() {
    }

    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            try {
                watchService = FileSystems.getDefault().newWatchService();
                watchThread = new Thread(this::watchLoop, "est-hotreload-watcher");
                watchThread.setDaemon(true);
                watchThread.start();
            } catch (IOException e) {
                running.set(false);
                throw new RuntimeException("Failed to start hot reload service", e);
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            try {
                if (watchService != null) {
                    watchService.close();
                }
                if (watchThread != null) {
                    watchThread.join(2000);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to stop hot reload service", e);
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void addWatchPath(Path path) {
        if (!watchPaths.contains(path)) {
            watchPaths.add(path);
            if (running.get()) {
                registerPath(path);
            }
        }
    }

    @Override
    public void removeWatchPath(Path path) {
        watchPaths.remove(path);
    }

    @Override
    public List<Path> getWatchPaths() {
        return new ArrayList<>(watchPaths);
    }

    @Override
    public void addListener(HotReloadListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HotReloadListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void setPollInterval(long millis) {
        this.pollInterval = millis;
    }

    @Override
    public long getPollInterval() {
        return pollInterval;
    }

    @Override
    public void triggerReload() {
        notifyListeners();
    }

    private void watchLoop() {
        for (Path path : watchPaths) {
            registerPath(path);
        }

        while (running.get()) {
            try {
                WatchKey key = watchService.poll(pollInterval, TimeUnit.MILLISECONDS);
                if (key != null) {
                    processWatchKey(key);
                }
                checkModifiedTimes();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                if (running.get()) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void registerPath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
                scanDirectory(path);
            }
        } catch (IOException e) {
            System.err.println("Failed to register path: " + path + ", " + e.getMessage());
        }
    }

    private void scanDirectory(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (Files.isRegularFile(path)) {
                    lastModifiedTimes.put(path, Files.getLastModifiedTime(path).toMillis());
                } else if (Files.isDirectory(path)) {
                    registerPath(path);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to scan directory: " + dir + ", " + e.getMessage());
        }
    }

    private void processWatchKey(WatchKey key) {
        boolean changed = false;
        for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }

            @SuppressWarnings("unchecked")
            WatchEvent<Path> ev = (WatchEvent<Path>) event;
            Path dir = (Path) key.watchable();
            Path filename = ev.context();
            Path fullPath = dir.resolve(filename);

            if (Files.isDirectory(fullPath)) {
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    registerPath(fullPath);
                }
                changed = true;
            } else if (Files.isRegularFile(fullPath)) {
                changed = true;
            }
        }

        key.reset();

        if (changed) {
            triggerReload();
        }
    }

    private void checkModifiedTimes() {
        boolean changed = false;
        for (Map.Entry<Path, Long> entry : lastModifiedTimes.entrySet()) {
            Path path = entry.getKey();
            try {
                if (Files.exists(path)) {
                    long current = Files.getLastModifiedTime(path).toMillis();
                    if (current > entry.getValue()) {
                        lastModifiedTimes.put(path, current);
                        changed = true;
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to check modified time: " + path + ", " + e.getMessage());
            }
        }

        if (changed) {
            triggerReload();
        }
    }

    private void notifyListeners() {
        for (HotReloadListener listener : listeners) {
            try {
                listener.onReload();
            } catch (Exception e) {
                System.err.println("Hot reload listener failed: " + e.getMessage());
            }
        }
    }
}
