package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoggingMiddlewareTest {

    @Test
    public void testCreateLoggingMiddleware() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        Assertions.assertNotNull(middleware);
    }

    @Test
    public void testMiddlewareName() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        Assertions.assertEquals("logging", middleware.getName());
    }

    @Test
    public void testMiddlewarePriority() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        Assertions.assertEquals(200, middleware.getPriority());
    }

    @Test
    public void testIsGlobal() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        Assertions.assertTrue(middleware.isGlobal());
    }

    @Test
    public void testBeforeMethod() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testAfterMethod() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();

        middleware.after(request, response);
    }

    @Test
    public void testOnErrorMethod() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        Exception exception = new Exception("Test error");

        middleware.onError(request, response, exception);
    }

    @Test
    public void testBeforeWithDifferentMethods() {
        LoggingMiddleware middleware = new LoggingMiddleware();

        HttpMethod[] methods = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH};

        for (HttpMethod method : methods) {
            MockRequest request = new MockRequest(method, "/test");
            MockResponse response = new MockResponse();
            boolean result = middleware.before(request, response);
            Assertions.assertTrue(result);
        }
    }

    @Test
    public void testBeforeWithDifferentPaths() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockResponse response = new MockResponse();

        String[] paths = {"/", "/home", "/api/users", "/api/users/123", "/about"};

        for (String path : paths) {
            MockRequest request = new MockRequest(HttpMethod.GET, path);
            boolean result = middleware.before(request, response);
            Assertions.assertTrue(result);
        }
    }

    @Test
    public void testAfterWithDifferentStatusCodes() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");

        int[] statusCodes = {200, 201, 400, 401, 403, 404, 500};

        for (int statusCode : statusCodes) {
            MockResponse response = new MockResponse();
            response.setStatus(statusCode);
            middleware.after(request, response);
        }
    }

    @Test
    public void testBeforeWithRemoteAddress() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        request.setRemoteAddress("192.168.1.100");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testOnErrorWithDifferentExceptions() {
        LoggingMiddleware middleware = new LoggingMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();

        Exception[] exceptions = {
                new NullPointerException("Null pointer"),
                new IllegalArgumentException("Invalid argument"),
                new RuntimeException("Runtime error")
        };

        for (Exception exception : exceptions) {
            middleware.onError(request, response, exception);
        }
    }
}
