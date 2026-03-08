package ltd.idcu.est.event.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EventConfig {
    
    private int threadPoolSize = Runtime.getRuntime().availableProcessors();
    private boolean useVirtualThreads = true;
    private int maxListenersPerEvent = 1000;
    private boolean propagateExceptions = false;
    private long listenerTimeout = 30000;
    private ExecutorService executorService;
    
    public EventConfig() {
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public EventConfig setThreadPoolSize(int threadPoolSize) {
        if (threadPoolSize <= 0) {
            throw new IllegalArgumentException("Thread pool size must be positive");
        }
        this.threadPoolSize = threadPoolSize;
        return this;
    }
    
    public boolean isUseVirtualThreads() {
        return useVirtualThreads;
    }
    
    public EventConfig setUseVirtualThreads(boolean useVirtualThreads) {
        this.useVirtualThreads = useVirtualThreads;
        return this;
    }
    
    public int getMaxListenersPerEvent() {
        return maxListenersPerEvent;
    }
    
    public EventConfig setMaxListenersPerEvent(int maxListenersPerEvent) {
        if (maxListenersPerEvent <= 0) {
            throw new IllegalArgumentException("Max listeners per event must be positive");
        }
        this.maxListenersPerEvent = maxListenersPerEvent;
        return this;
    }
    
    public boolean isPropagateExceptions() {
        return propagateExceptions;
    }
    
    public EventConfig setPropagateExceptions(boolean propagateExceptions) {
        this.propagateExceptions = propagateExceptions;
        return this;
    }
    
    public long getListenerTimeout() {
        return listenerTimeout;
    }
    
    public EventConfig setListenerTimeout(long listenerTimeout) {
        if (listenerTimeout < 0) {
            throw new IllegalArgumentException("Listener timeout must be >= 0");
        }
        this.listenerTimeout = listenerTimeout;
        return this;
    }
    
    public ExecutorService getExecutorService() {
        return executorService;
    }
    
    public EventConfig setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
    
    public ExecutorService createExecutorService() {
        if (executorService != null) {
            return executorService;
        }
        
        if (useVirtualThreads) {
            return Executors.newVirtualThreadPerTaskExecutor();
        }
        
        return Executors.newFixedThreadPool(threadPoolSize, new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(0);
            
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "event-bus-" + counter.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        });
    }
    
    public static EventConfig defaultConfig() {
        return new EventConfig();
    }
    
    public static EventConfig virtualThreads() {
        return new EventConfig().setUseVirtualThreads(true);
    }
    
    public static EventConfig fixedThreads(int size) {
        return new EventConfig()
                .setUseVirtualThreads(false)
                .setThreadPoolSize(size);
    }
    
    @Override
    public String toString() {
        return "EventConfig{" +
                "threadPoolSize=" + threadPoolSize +
                ", useVirtualThreads=" + useVirtualThreads +
                ", maxListenersPerEvent=" + maxListenersPerEvent +
                ", propagateExceptions=" + propagateExceptions +
                ", listenerTimeout=" + listenerTimeout +
                '}';
    }
}
