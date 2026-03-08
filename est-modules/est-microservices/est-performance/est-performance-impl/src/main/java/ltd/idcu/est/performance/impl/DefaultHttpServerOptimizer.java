package ltd.idcu.est.performance.impl;

import ltd.idcu.est.performance.api.HttpServerOptimizer;

import java.util.LinkedHashMap;
import java.util.Map;


public class DefaultHttpServerOptimizer implements HttpServerOptimizer {
    private int backlog = 0;
    private int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2;
    private boolean useVirtualThreads = true;
    private int connectionTimeout = 30000;
    private int requestTimeout = 30000;
    private int maxRequestSize = 10 * 1024 * 1024;
    private boolean enableCompression = true;
    private boolean enableCaching = true;
    private int cacheMaxAge = 3600;

    public DefaultHttpServerOptimizer() {
    }

    public DefaultHttpServerOptimizer setBacklog(int backlog) {
        this.backlog = backlog;
        return this;
    }

    public DefaultHttpServerOptimizer setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public DefaultHttpServerOptimizer setUseVirtualThreads(boolean useVirtualThreads) {
        this.useVirtualThreads = useVirtualThreads;
        return this;
    }

    public DefaultHttpServerOptimizer setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public DefaultHttpServerOptimizer setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }

    public DefaultHttpServerOptimizer setMaxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
        return this;
    }

    public DefaultHttpServerOptimizer setEnableCompression(boolean enableCompression) {
        this.enableCompression = enableCompression;
        return this;
    }

    public DefaultHttpServerOptimizer setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
        return this;
    }

    public DefaultHttpServerOptimizer setCacheMaxAge(int cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
        return this;
    }

    @Override
    public int getBacklog() {
        return backlog;
    }

    @Override
    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    @Override
    public boolean isUseVirtualThreads() {
        return useVirtualThreads;
    }

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public int getRequestTimeout() {
        return requestTimeout;
    }

    @Override
    public int getMaxRequestSize() {
        return maxRequestSize;
    }

    @Override
    public boolean isEnableCompression() {
        return enableCompression;
    }

    @Override
    public boolean isEnableCaching() {
        return enableCaching;
    }

    @Override
    public int getCacheMaxAge() {
        return cacheMaxAge;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("backlog", backlog);
        config.put("threadPoolSize", threadPoolSize);
        config.put("useVirtualThreads", useVirtualThreads);
        config.put("connectionTimeout", connectionTimeout);
        config.put("requestTimeout", requestTimeout);
        config.put("maxRequestSize", formatBytes(maxRequestSize));
        config.put("enableCompression", enableCompression);
        config.put("enableCaching", enableCaching);
        config.put("cacheMaxAge", cacheMaxAge + "s");
        return config;
    }

    public static DefaultHttpServerOptimizer forProduction() {
        return new DefaultHttpServerOptimizer()
                .setBacklog(100)
                .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 4)
                .setUseVirtualThreads(true)
                .setConnectionTimeout(60000)
                .setRequestTimeout(60000)
                .setMaxRequestSize(50 * 1024 * 1024)
                .setEnableCompression(true)
                .setEnableCaching(true)
                .setCacheMaxAge(7200);
    }

    public static DefaultHttpServerOptimizer forDevelopment() {
        return new DefaultHttpServerOptimizer()
                .setBacklog(0)
                .setThreadPoolSize(Runtime.getRuntime().availableProcessors() * 2)
                .setUseVirtualThreads(true)
                .setConnectionTimeout(30000)
                .setRequestTimeout(30000)
                .setMaxRequestSize(10 * 1024 * 1024)
                .setEnableCompression(false)
                .setEnableCaching(false)
                .setCacheMaxAge(0);
    }

    @Override
    public String toString() {
        return "DefaultHttpServerOptimizer{" +
                "backlog=" + backlog +
                ", threadPoolSize=" + threadPoolSize +
                ", useVirtualThreads=" + useVirtualThreads +
                ", connectionTimeout=" + connectionTimeout + "ms" +
                ", requestTimeout=" + requestTimeout + "ms" +
                ", maxRequestSize=" + formatBytes(maxRequestSize) +
                ", enableCompression=" + enableCompression +
                ", enableCaching=" + enableCaching +
                ", cacheMaxAge=" + cacheMaxAge + "s" +
                '}';
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        else if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        else if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        else return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }
}
