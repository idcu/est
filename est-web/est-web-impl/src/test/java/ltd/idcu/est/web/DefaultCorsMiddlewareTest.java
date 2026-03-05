package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.HttpStatus;

import java.util.Arrays;
import java.util.List;

public class DefaultCorsMiddlewareTest {

    @Test
    public void testCreateCorsMiddleware() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertNotNull(middleware);
    }

    @Test
    public void testMiddlewareName() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertEquals("cors", middleware.getName());
    }

    @Test
    public void testMiddlewarePriority() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertEquals(50, middleware.getPriority());
    }

    @Test
    public void testIsGlobal() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertTrue(middleware.isGlobal());
    }

    @Test
    public void testDefaultAllowedOrigins() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> origins = middleware.getAllowedOrigins();
        Assertions.assertNotNull(origins);
        Assertions.assertEquals(1, origins.size());
        Assertions.assertEquals("*", origins.get(0));
    }

    @Test
    public void testIsOriginAllowedWithWildcard() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertTrue(middleware.isOriginAllowed("http://example.com"));
        Assertions.assertTrue(middleware.isOriginAllowed("https://test.com"));
        Assertions.assertTrue(middleware.isOriginAllowed("null"));
    }

    @Test
    public void testSetAllowedOrigins() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> origins = Arrays.asList("http://example.com", "https://test.com");
        middleware.setAllowedOrigins(origins);
        
        List<String> result = middleware.getAllowedOrigins();
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("http://example.com"));
        Assertions.assertTrue(result.contains("https://test.com"));
    }

    @Test
    public void testIsOriginAllowedWithSpecificOrigins() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedOrigins(Arrays.asList("http://example.com"));
        
        Assertions.assertTrue(middleware.isOriginAllowed("http://example.com"));
        Assertions.assertFalse(middleware.isOriginAllowed("https://test.com"));
    }

    @Test
    public void testDefaultAllowedMethods() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> methods = middleware.getAllowedMethods();
        Assertions.assertNotNull(methods);
        Assertions.assertEquals(6, methods.size());
        Assertions.assertTrue(methods.contains("GET"));
        Assertions.assertTrue(methods.contains("POST"));
        Assertions.assertTrue(methods.contains("PUT"));
        Assertions.assertTrue(methods.contains("DELETE"));
        Assertions.assertTrue(methods.contains("PATCH"));
        Assertions.assertTrue(methods.contains("OPTIONS"));
    }

    @Test
    public void testSetAllowedMethods() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> methods = Arrays.asList("GET", "POST");
        middleware.setAllowedMethods(methods);
        
        List<String> result = middleware.getAllowedMethods();
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("GET"));
        Assertions.assertTrue(result.contains("POST"));
    }

    @Test
    public void testSetAndGetAllowedHeaders() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> headers = Arrays.asList("Content-Type", "Authorization");
        middleware.setAllowedHeaders(headers);
        
        List<String> result = middleware.getAllowedHeaders();
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("Content-Type"));
        Assertions.assertTrue(result.contains("Authorization"));
    }

    @Test
    public void testSetAndGetExposedHeaders() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        List<String> headers = Arrays.asList("X-Custom-Header", "X-Another-Header");
        middleware.setExposedHeaders(headers);
        
        List<String> result = middleware.getExposedHeaders();
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("X-Custom-Header"));
        Assertions.assertTrue(result.contains("X-Another-Header"));
    }

    @Test
    public void testSetAndGetAllowCredentials() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertFalse(middleware.isAllowCredentials());
        
        middleware.setAllowCredentials(true);
        Assertions.assertTrue(middleware.isAllowCredentials());
        
        middleware.setAllowCredentials(false);
        Assertions.assertFalse(middleware.isAllowCredentials());
    }

    @Test
    public void testSetAndGetMaxAge() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        Assertions.assertEquals(3600L, middleware.getMaxAge());
        
        middleware.setMaxAge(1800);
        Assertions.assertEquals(1800L, middleware.getMaxAge());
    }

    @Test
    public void testOptionsRequestSetsCorsHeaders() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        MockRequest request = new MockRequest(HttpMethod.OPTIONS, "/test");
        MockResponse response = new MockResponse();
        
        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertNotNull(response.getHeader("Access-Control-Allow-Origin"));
        Assertions.assertNotNull(response.getHeader("Access-Control-Allow-Methods"));
    }

    @Test
    public void testOptionsRequestWithMaxAge() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setMaxAge(7200);
        MockRequest request = new MockRequest(HttpMethod.OPTIONS, "/test");
        MockResponse response = new MockResponse();
        
        middleware.before(request, response);
        Assertions.assertEquals("7200", response.getHeader("Access-Control-Max-Age"));
    }

    @Test
    public void testOptionsRequestWithCredentials() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowCredentials(true);
        MockRequest request = new MockRequest(HttpMethod.OPTIONS, "/test");
        MockResponse response = new MockResponse();
        
        middleware.before(request, response);
        Assertions.assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
    }

    @Test
    public void testOptionsRequestWithExposedHeaders() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setExposedHeaders(Arrays.asList("X-Custom", "X-Test"));
        MockRequest request = new MockRequest(HttpMethod.OPTIONS, "/test");
        MockResponse response = new MockResponse();
        
        middleware.before(request, response);
        Assertions.assertEquals("X-Custom,X-Test", response.getHeader("Access-Control-Expose-Headers"));
    }

    @Test
    public void testDisallowedOriginReturnsForbidden() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedOrigins(Arrays.asList("http://allowed.com"));
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        request.setHeader("Origin", "http://disallowed.com");
        MockResponse response = new MockResponse();
        
        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatus());
    }

    @Test
    public void testGetRequestWithAllowedOrigin() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedOrigins(Arrays.asList("http://allowed.com"));
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        request.setHeader("Origin", "http://allowed.com");
        MockResponse response = new MockResponse();
        
        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testAfterMethod() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/test");
        MockResponse response = new MockResponse();
        
        middleware.after(request, response);
    }

    @Test
    public void testSetAllowedOriginsToNull() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedOrigins(null);
        List<String> result = middleware.getAllowedOrigins();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testSetAllowedMethodsToNull() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedMethods(null);
        List<String> result = middleware.getAllowedMethods();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testSetAllowedHeadersToNull() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setAllowedHeaders(null);
        List<String> result = middleware.getAllowedHeaders();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testSetExposedHeadersToNull() {
        DefaultCorsMiddleware middleware = new DefaultCorsMiddleware();
        middleware.setExposedHeaders(null);
        List<String> result = middleware.getExposedHeaders();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}
