package ltd.idcu.est.monitor.api;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

public class ThreadHealthCheck implements HealthCheck {
    private final int maxThreadCount;

    public ThreadHealthCheck() {
        this(1000);
    }

    public ThreadHealthCheck(int maxThreadCount) {
        this.maxThreadCount = maxThreadCount;
    }

    @Override
    public HealthStatus check() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        int threadCount = threadBean.getThreadCount();
        int peakThreadCount = threadBean.getPeakThreadCount();
        int daemonThreadCount = threadBean.getDaemonThreadCount();

        Map<String, Object> details = new HashMap<>();
        details.put("threadCount", threadCount);
        details.put("peakThreadCount", peakThreadCount);
        details.put("daemonThreadCount", daemonThreadCount);

        if (threadCount > maxThreadCount) {
            return HealthStatus.unhealthy("Thread count exceeds " + maxThreadCount + ": " + threadCount, details);
        } else if (threadCount > maxThreadCount * 0.8) {
            return HealthStatus.degraded("Thread count is high: " + threadCount, details);
        } else {
            return HealthStatus.healthy("Thread count is normal: " + threadCount, details);
        }
    }

    @Override
    public HealthStatus getStatus() {
        return check();
    }

    @Override
    public String getName() {
        return "threads";
    }

    @Override
    public String getDescription() {
        return "JVM thread count health check";
    }
}
