package ltd.idcu.est.web;

import ltd.idcu.est.features.monitor.api.Metrics;
import ltd.idcu.est.web.api.Middleware;
import ltd.idcu.est.web.api.Request;
import ltd.idcu.est.web.api.Response;

public class PerformanceMonitorMiddleware implements Middleware {

    private final Metrics metrics;
    private final String name;
    private final int priority;

    public PerformanceMonitorMiddleware(Metrics metrics) {
        this(metrics, "performance-monitor", 50);
    }

    public PerformanceMonitorMiddleware(Metrics metrics, String name, int priority) {
        this.metrics = metrics;
        this.name = name;
        this.priority = priority;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean before(Request request, Response response) {
        metrics.incrementCounter("http.requests.total");
        metrics.incrementCounter("http.requests.active");
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void after(Request request, Response response) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            String path = request.getPath();
            String method = request.getMethod().getMethod();
            
            metrics.recordTimer("http.requests.duration", duration);
            metrics.recordTimer("http.requests." + method + "." + path + ".duration", duration);
            
            metrics.decrementCounter("http.requests.active");
            
            int status = response.getStatus();
            metrics.incrementCounter("http.responses." + status);
        }
    }

    @Override
    public void onError(Request request, Response response, Exception exception) {
        metrics.incrementCounter("http.errors.total");
        metrics.decrementCounter("http.requests.active");
    }

    @Override
    public boolean isGlobal() {
        return true;
    }
}
