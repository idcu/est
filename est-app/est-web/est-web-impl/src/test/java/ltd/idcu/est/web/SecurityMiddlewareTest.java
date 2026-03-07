package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.HttpStatus;

import java.util.Base64;

public class SecurityMiddlewareTest {

    @Test
    public void testCreateSecurityMiddleware() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        Assertions.assertNotNull(middleware);
    }

    @Test
    public void testMiddlewareName() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        Assertions.assertEquals("security", middleware.getName());
    }

    @Test
    public void testMiddlewarePriority() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        Assertions.assertEquals(300, middleware.getPriority());
    }

    @Test
    public void testIsGlobal() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        Assertions.assertTrue(middleware.isGlobal());
    }

    @Test
    public void testDefaultExcludedPaths() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/public");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testNoAuthHeaderReturnsUnauthorized() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testInvalidAuthHeaderReturnsUnauthorized() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        request.setHeader("Authorization", "Invalid token");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testValidCredentialsAllowsAccess() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.addUser("testuser", "testpass");

        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        String credentials = Base64.getEncoder().encodeToString("testuser:testpass".getBytes());
        request.setHeader("Authorization", "Basic " + credentials);
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testInvalidCredentialsDeniesAccess() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.addUser("testuser", "testpass");

        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        String credentials = Base64.getEncoder().encodeToString("testuser:wrongpass".getBytes());
        request.setHeader("Authorization", "Basic " + credentials);
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testNonExistentUserDeniesAccess() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.addUser("testuser", "testpass");

        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        String credentials = Base64.getEncoder().encodeToString("nonexistent:testpass".getBytes());
        request.setHeader("Authorization", "Basic " + credentials);
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testAddExcludedPath() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.addExcludedPath("/custom");

        MockRequest request = new MockRequest(HttpMethod.GET, "/custom");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testSetRealm() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.setRealm("My Realm");

        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        MockResponse response = new MockResponse();

        middleware.before(request, response);
        String wwwAuthenticate = response.getHeader("WWW-Authenticate");
        Assertions.assertNotNull(wwwAuthenticate);
        Assertions.assertTrue(wwwAuthenticate.contains("My Realm"));
    }

    @Test
    public void testMalformedCredentials() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        request.setHeader("Authorization", "Basic malformed");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testCredentialsWithoutColon() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        String credentials = Base64.getEncoder().encodeToString("nocolon".getBytes());
        request.setHeader("Authorization", "Basic " + credentials);
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertFalse(result);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void testExcludedPathWithSubpaths() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/static/css/style.css");
        MockResponse response = new MockResponse();

        boolean result = middleware.before(request, response);
        Assertions.assertTrue(result);
    }

    @Test
    public void testOnError() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        MockRequest request = new MockRequest(HttpMethod.GET, "/protected");
        MockResponse response = new MockResponse();
        Exception exception = new Exception("Test error");

        middleware.onError(request, response, exception);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
        Assertions.assertNotNull(response.getHeader("WWW-Authenticate"));
    }

    @Test
    public void testMultipleUsers() {
        SecurityMiddleware middleware = new SecurityMiddleware();
        middleware.addUser("user1", "pass1");
        middleware.addUser("user2", "pass2");

        MockRequest request1 = new MockRequest(HttpMethod.GET, "/protected");
        String credentials1 = Base64.getEncoder().encodeToString("user1:pass1".getBytes());
        request1.setHeader("Authorization", "Basic " + credentials1);
        MockResponse response1 = new MockResponse();
        boolean result1 = middleware.before(request1, response1);
        Assertions.assertTrue(result1);

        MockRequest request2 = new MockRequest(HttpMethod.GET, "/protected");
        String credentials2 = Base64.getEncoder().encodeToString("user2:pass2".getBytes());
        request2.setHeader("Authorization", "Basic " + credentials2);
        MockResponse response2 = new MockResponse();
        boolean result2 = middleware.before(request2, response2);
        Assertions.assertTrue(result2);
    }
}
