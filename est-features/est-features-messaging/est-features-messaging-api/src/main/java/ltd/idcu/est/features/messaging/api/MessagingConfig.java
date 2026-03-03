package ltd.idcu.est.features.messaging.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagingConfig {
    
    private String host = "localhost";
    private int port = 5672;
    private String username = "guest";
    private String password = "guest";
    private String virtualHost = "/";
    private int connectionTimeout = 30000;
    private int threadPoolSize = Runtime.getRuntime().availableProcessors();
    private boolean useVirtualThreads = true;
    private int maxQueueSize = 10000;
    private long defaultMessageTtl = 0;
    private int prefetchCount = 1;
    private boolean autoReconnect = true;
    private int reconnectInterval = 5000;
    private ExecutorService executorService;
    
    public MessagingConfig() {
    }
    
    public String getHost() {
        return host;
    }
    
    public MessagingConfig setHost(String host) {
        this.host = host;
        return this;
    }
    
    public int getPort() {
        return port;
    }
    
    public MessagingConfig setPort(int port) {
        this.port = port;
        return this;
    }
    
    public String getUsername() {
        return username;
    }
    
    public MessagingConfig setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public MessagingConfig setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public String getVirtualHost() {
        return virtualHost;
    }
    
    public MessagingConfig setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
        return this;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public MessagingConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }
    
    public int getThreadPoolSize() {
        return threadPoolSize;
    }
    
    public MessagingConfig setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }
    
    public boolean isUseVirtualThreads() {
        return useVirtualThreads;
    }
    
    public MessagingConfig setUseVirtualThreads(boolean useVirtualThreads) {
        this.useVirtualThreads = useVirtualThreads;
        return this;
    }
    
    public int getMaxQueueSize() {
        return maxQueueSize;
    }
    
    public MessagingConfig setMaxQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        return this;
    }
    
    public long getDefaultMessageTtl() {
        return defaultMessageTtl;
    }
    
    public MessagingConfig setDefaultMessageTtl(long defaultMessageTtl) {
        this.defaultMessageTtl = defaultMessageTtl;
        return this;
    }
    
    public int getPrefetchCount() {
        return prefetchCount;
    }
    
    public MessagingConfig setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
        return this;
    }
    
    public boolean isAutoReconnect() {
        return autoReconnect;
    }
    
    public MessagingConfig setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }
    
    public int getReconnectInterval() {
        return reconnectInterval;
    }
    
    public MessagingConfig setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
        return this;
    }
    
    public ExecutorService getExecutorService() {
        return executorService;
    }
    
    public MessagingConfig setExecutorService(ExecutorService executorService) {
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
                Thread t = new Thread(r, "messaging-" + counter.incrementAndGet());
                t.setDaemon(true);
                return t;
            }
        });
    }
    
    public static MessagingConfig defaultConfig() {
        return new MessagingConfig();
    }
    
    public static MessagingConfig local() {
        return new MessagingConfig();
    }
    
    public static MessagingConfig amqp(String host, int port) {
        return new MessagingConfig()
                .setHost(host)
                .setPort(port);
    }
    
    public static MessagingConfig mqtt(String host, int port) {
        return new MessagingConfig()
                .setHost(host)
                .setPort(port);
    }
    
    @Override
    public String toString() {
        return "MessagingConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", virtualHost='" + virtualHost + '\'' +
                ", connectionTimeout=" + connectionTimeout +
                ", threadPoolSize=" + threadPoolSize +
                ", useVirtualThreads=" + useVirtualThreads +
                ", maxQueueSize=" + maxQueueSize +
                ", autoReconnect=" + autoReconnect +
                '}';
    }
}
