package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.HttpStatus;

public class PerformanceMonitorMiddlewareTest {

    @Test
    public void testCreatePerformanceMonitorMiddleware() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        Assertions.assertNotNull(middleware);
    }

    @Test
    public void testMiddlewareWithCustomName() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics, "custom-monitor", 100);
        Assertions.assertEquals("custom-monitor", middleware.getName());
        Assertions.assertEquals(100, middleware.getPriority());
    }

    @Test
    public void testDefaultMiddlewareNameAndPriority() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        Assertions.assertEquals("performance-monitor", middleware.getName());
        Assertions.assertEquals(50, middleware.getPriority());
    }

    @Test
    public void testIsGlobal() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        Assertions.assertTrue(middleware.isGlobal());
    }

    @Test
    public void testBeforeIncrementsRequestCounters() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, metrics.getCounter("http.requests.total"));
        Assertions.assertEquals(1, metrics.getCounter("http.requests.active"));
        Assertions.assertNotNull(request.getAttribute("startTime"));
    }

    @Test
    public void testBeforeWithDifferentMethods() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockResponse response = new MockResponse();

        HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH};

        for (HttpMethod method : methods) {
            MockRequest request = new MockRequest(method, "/test");
            middleware.before(request, response);
        }

        Assertions.assertEquals(5, metrics.getCounter("http.requests.total"));
        Assertions.assertEquals(5, metrics.getCounter("http.requests.active"));
    }

    @Test
    public void testAfterRecordsDuration() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        response.setStatus(HttpStatus.OK);

        middleware.before(request, response);
        Long startTime = (Long) request.getAttribute("startTime");
        Assertions.assertNotNull(startTime);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        middleware.after(request, response);
        Assertions.assertEquals(0, metrics.getCounter("http.requests.active"));
        Assertions.assertNotNull(metrics.getMetric("http.requests.duration"));
    }

    @Test
    public void testAfterRecordsStatusCounter() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        response.setStatus(HttpStatus.NOT_FOUND);

        middleware.before(request, response);
        middleware.after(request, response);

        Assertions.assertEquals(1, metrics.getCounter("http.responses.404"));
    }

    @Test
    public void testAfterWithDifferentStatusCodes() {
        int[] statusCodes = {200, 201, 400, 401, 403, 404, 500};

        for (int statusCode : statusCodes) {
            MockMetrics metrics = new MockMetrics();
            PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
            MockRequest request = new MockRequest(HttpMethod.GET, "/test");
            MockResponse response = new MockResponse();
            response.setStatus(statusCode);

            middleware.before(request, response);
            middleware.after(request, response);

            Assertions.assertEquals(1, metrics.getCounter("http.responses." + statusCode));
        }
    }

    @Test
    public void testAfterRecordsPathSpecificDuration() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/api/users");
        MockResponse response = new MockResponse();

        middleware.before(request, response);
        middleware.after(request, response);

        Assertions.assertNotNull(metrics.getMetric("http.requests.GET./api/users.duration"));
    }

    @Test
    public void testAfterWithoutStartTime() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();

        middleware.after(request, response);
        Assertions.assertEquals(0, metrics.getCounter("http.requests.active"));
    }

    @Test
    public void testOnErrorIncrementsErrorCounter() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        Exception exception = new RuntimeException("Test error");

        middleware.before(request, response);
        middleware.onError(request, response, exception);

        Assertions.assertEquals(1, metrics.getCounter("http.errors.total"));
        Assertions.assertEquals(0, metrics.getCounter("http.requests.active"));
    }

    @Test
    public void testOnErrorWithoutBefore() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        Exception exception = new RuntimeException("Test error");

        middleware.onError(request, response, exception);
        Assertions.assertEquals(1, metrics.getCounter("http.errors.total"));
    }

    @Test
    public void testMultipleRequests() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockResponse response = new MockResponse();

        for (int i = 0; i < 10; i++) {
            MockRequest request = new MockRequest(HttpMethod.GET, "/test" + i);
            middleware.before(request, response);
            middleware.after(request, response);
        }

        Assertions.assertEquals(10, metrics.getCounter("http.requests.total"));
        Assertions.assertEquals(0, metrics.getCounter("http.requests.active"));
    }

    @Test
    public void testWithDifferentPaths() {
        MockMetrics metrics = new MockMetrics();
        PerformanceMonitorMiddleware middleware = new PerformanceMonitorMiddleware(metrics);
        MockResponse response = new MockResponse();

        String[] paths = {"/", "/home", "/api", "/api/users", "/about"};

        for (String path : paths) {
            MockRequest request = new MockRequest(HttpMethod.GET, path);
            middleware.before(request, response);
            middleware.after(request, response);
        }

        Assertions.assertEquals(5, metrics.getCounter("http.requests.total"));
    }
}
