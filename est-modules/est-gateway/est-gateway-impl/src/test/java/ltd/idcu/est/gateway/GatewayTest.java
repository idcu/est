package ltd.idcu.est.gateway;

import ltd.idcu.est.gateway.api.*;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatewayTest {

    @Test
    public void testDefaultRouteCreation() {
        Route route = new DefaultRoute("/api/users/**", "user-service");
        
        Assertions.assertNotNull(route);
        Assertions.assertEquals("/api/users/**", route.getPath());
        Assertions.assertEquals("user-service", route.getServiceId());
    }

    @Test
    public void testRouteWithPathRewrite() {
        Route route = new DefaultRoute("/api/users/**", "user-service", "/$1");
        
        Assertions.assertNotNull(route);
        Assertions.assertEquals("/api/users/**", route.getPath());
        Assertions.assertEquals("user-service", route.getServiceId());
        Assertions.assertEquals("/$1", route.getPathRewrite());
    }

    @Test
    public void testRouteMatching() {
        Route route = new DefaultRoute("/api/users/**", "user-service");
        
        Assertions.assertTrue(route.matches("/api/users"));
        Assertions.assertTrue(route.matches("/api/users/123"));
        Assertions.assertTrue(route.matches("/api/users/profile"));
        Assertions.assertFalse(route.matches("/api/orders"));
        Assertions.assertFalse(route.matches("/other"));
    }

    @Test
    public void testRouteMatchingWithExactPath() {
        Route route = new DefaultRoute("/health", "health-service");
        
        Assertions.assertTrue(route.matches("/health"));
        Assertions.assertFalse(route.matches("/health/check"));
        Assertions.assertFalse(route.matches("/healthcheck"));
    }

    @Test
    public void testPathRewrite() {
        Route route = new DefaultRoute("/api/users/**", "user-service", "/$1");
        
        String rewritten = route.rewritePath("/api/users/123");
        Assertions.assertEquals("/123", rewritten);
        
        rewritten = route.rewritePath("/api/users/profile");
        Assertions.assertEquals("/profile", rewritten);
    }

    @Test
    public void testPathRewriteWithoutPattern() {
        Route route = new DefaultRoute("/api/users", "user-service");
        
        String rewritten = route.rewritePath("/api/users");
        Assertions.assertEquals("/api/users", rewritten);
    }

    @Test
    public void testDefaultGatewayRouterCreation() {
        GatewayRouter router = new DefaultGatewayRouter();
        
        Assertions.assertNotNull(router);
        Assertions.assertTrue(router.getRoutes().isEmpty());
    }

    @Test
    public void testAddRouteToRouter() {
        GatewayRouter router = new DefaultGatewayRouter();
        Route route = new DefaultRoute("/api/users/**", "user-service");
        
        router.addRoute(route);
        
        List<Route> routes = router.getRoutes();
        Assertions.assertEquals(1, routes.size());
        Assertions.assertEquals("/api/users/**", routes.get(0).getPath());
    }

    @Test
    public void testAddRouteWithPathAndServiceId() {
        GatewayRouter router = new DefaultGatewayRouter();
        
        router.addRoute("/api/orders/**", "order-service");
        
        List<Route> routes = router.getRoutes();
        Assertions.assertEquals(1, routes.size());
        Assertions.assertEquals("/api/orders/**", routes.get(0).getPath());
        Assertions.assertEquals("order-service", routes.get(0).getServiceId());
    }

    @Test
    public void testAddRouteWithPathRewrite() {
        GatewayRouter router = new DefaultGatewayRouter();
        
        router.addRoute("/api/products/**", "product-service", "/$1");
        
        List<Route> routes = router.getRoutes();
        Assertions.assertEquals(1, routes.size());
        Assertions.assertEquals("/api/products/**", routes.get(0).getPath());
        Assertions.assertEquals("product-service", routes.get(0).getServiceId());
        Assertions.assertEquals("/$1", routes.get(0).getPathRewrite());
    }

    @Test
    public void testRouteMatchingInRouter() {
        GatewayRouter router = new DefaultGatewayRouter();
        router.addRoute("/api/users/**", "user-service");
        router.addRoute("/api/orders/**", "order-service");
        router.addRoute("/health", "health-service");
        
        Route matched = router.match("/api/users/123");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("user-service", matched.getServiceId());
        
        matched = router.match("/api/orders/456");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("order-service", matched.getServiceId());
        
        matched = router.match("/health");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("health-service", matched.getServiceId());
        
        matched = router.match("/api/other");
        Assertions.assertNull(matched);
    }

    @Test
    public void testLongestPathMatching() {
        GatewayRouter router = new DefaultGatewayRouter();
        router.addRoute("/api/**", "general-service");
        router.addRoute("/api/users/**", "user-service");
        router.addRoute("/api/users/123", "specific-user-service");
        
        Route matched = router.match("/api/users/123");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("specific-user-service", matched.getServiceId());
        
        matched = router.match("/api/users/456");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("user-service", matched.getServiceId());
        
        matched = router.match("/api/other");
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("general-service", matched.getServiceId());
    }

    @Test
    public void testRemoveRoute() {
        GatewayRouter router = new DefaultGatewayRouter();
        router.addRoute("/api/users/**", "user-service");
        router.addRoute("/api/orders/**", "order-service");
        
        Assertions.assertEquals(2, router.getRoutes().size());
        
        router.removeRoute("/api/users/**");
        
        Assertions.assertEquals(1, router.getRoutes().size());
        Assertions.assertEquals("/api/orders/**", router.getRoutes().get(0).getPath());
    }

    @Test
    public void testClearRoutes() {
        GatewayRouter router = new DefaultGatewayRouter();
        router.addRoute("/api/users/**", "user-service");
        router.addRoute("/api/orders/**", "order-service");
        
        Assertions.assertEquals(2, router.getRoutes().size());
        
        router.clear();
        
        Assertions.assertTrue(router.getRoutes().isEmpty());
    }

    @Test
    public void testDefaultApiGatewayCreation() {
        ApiGateway gateway = new DefaultApiGateway();
        
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(gateway.getRouter());
    }

    @Test
    public void testRegisterService() {
        DefaultApiGateway gateway = new DefaultApiGateway();
        
        gateway.registerService("user-service", "http://localhost:8081");
        gateway.registerService("order-service", "http://localhost:8082");
    }

    @Test
    public void testDefaultGatewayContext() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        DefaultGatewayContext context = new DefaultGatewayContext(
            "/api/users", "GET", headers, new byte[0]);
        
        Assertions.assertEquals("/api/users", context.getRequestPath());
        Assertions.assertEquals("GET", context.getRequestMethod());
        Assertions.assertEquals("application/json", context.getRequestHeaders().get("Content-Type"));
        Assertions.assertNotNull(context.getRequestBody());
    }

    @Test
    public void testGatewayContextWithMatchedRoute() {
        Map<String, String> headers = new HashMap<>();
        DefaultGatewayContext context = new DefaultGatewayContext(
            "/api/users", "GET", headers, new byte[0]);
        
        Route route = new DefaultRoute("/api/users", "user-service");
        context.setMatchedRoute(route);
        
        Assertions.assertEquals(route, context.getMatchedRoute());
    }

    @Test
    public void testGatewayContextWithTargetUrl() {
        Map<String, String> headers = new HashMap<>();
        DefaultGatewayContext context = new DefaultGatewayContext(
            "/api/users", "GET", headers, new byte[0]);
        
        context.setTargetUrl("http://localhost:8081/users");
        
        Assertions.assertEquals("http://localhost:8081/users", context.getTargetUrl());
    }

    @Test
    public void testGatewayContextWithResponse() {
        Map<String, String> headers = new HashMap<>();
        DefaultGatewayContext context = new DefaultGatewayContext(
            "/api/users", "GET", headers, new byte[0]);
        
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", "application/json");
        
        context.setResponseStatus(200);
        context.setResponseHeaders(responseHeaders);
        context.setResponseBody("{\"id\":1}".getBytes());
        
        Assertions.assertEquals(200, context.getResponseStatus());
        Assertions.assertEquals("application/json", context.getResponseHeaders().get("Content-Type"));
        Assertions.assertNotNull(context.getResponseBody());
    }

    @Test
    public void testGatewayFluentApi() {
        Gateway gateway = Gateway.create()
            .withLogging()
            .withCors()
            .route("/api/users/**", "user-service", "/$1")
            .route("/api/orders/**", "order-service", "/$1")
            .registerService("user-service", "http://localhost:8081")
            .registerService("order-service", "http://localhost:8082");
        
        Assertions.assertNotNull(gateway);
        
        ApiGateway apiGateway = gateway;
        Assertions.assertNotNull(apiGateway.getRouter());
        Assertions.assertEquals(2, apiGateway.getRouter().getRoutes().size());
    }

    @Test
    public void testHttpClientCreation() {
        HttpClient client = new HttpClient();
        Assertions.assertNotNull(client);
    }

    @Test
    public void testMiddlewareOrdering() {
        DefaultApiGateway gateway = new DefaultApiGateway();
        
        GatewayMiddleware middleware1 = new GatewayMiddleware() {
            @Override
            public String getName() { return "middleware1"; }
            
            @Override
            public int getOrder() { return 100; }
            
            @Override
            public java.util.function.Function<GatewayContext, GatewayContext> process() {
                return ctx -> ctx;
            }
        };
        
        GatewayMiddleware middleware2 = new GatewayMiddleware() {
            @Override
            public String getName() { return "middleware2"; }
            
            @Override
            public int getOrder() { return 50; }
            
            @Override
            public java.util.function.Function<GatewayContext, GatewayContext> process() {
                return ctx -> ctx;
            }
        };
        
        gateway.addMiddleware(middleware1);
        gateway.addMiddleware(middleware2);
        gateway.removeMiddleware("middleware1");
    }
}
