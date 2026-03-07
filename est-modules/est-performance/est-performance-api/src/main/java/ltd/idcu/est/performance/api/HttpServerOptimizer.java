package ltd.idcu.est.performance.api;

import java.util.Map;

public interface HttpServerOptimizer {
    int getBacklog();

    int getThreadPoolSize();

    boolean isUseVirtualThreads();

    int getConnectionTimeout();

    int getRequestTimeout();

    int getMaxRequestSize();

    boolean isEnableCompression();

    boolean isEnableCaching();

    int getCacheMaxAge();

    Map<String, Object> toMap();
}
