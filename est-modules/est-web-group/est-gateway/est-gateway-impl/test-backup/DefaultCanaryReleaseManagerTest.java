package ltd.idcu.est.gateway.impl;

import ltd.idcu.est.gateway.api.CanaryReleaseConfig;
import ltd.idcu.est.gateway.api.CanaryReleaseManager;
import ltd.idcu.est.gateway.api.GatewayContext;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.*;

public class DefaultCanaryReleaseManagerTest {

    @Test
    public void testAddAndGetConfig() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig(
            "user-service", "v1", "v2", 10);
        manager.addConfig(config);
        
        CanaryReleaseConfig retrieved = manager.getConfig("user-service");
        Assertions.assertNotNull(retrieved);
        Assertions.assertEquals("user-service", retrieved.getServiceId());
        Assertions.assertEquals("v1", retrieved.getPrimaryVersion());
        Assertions.assertEquals("v2", retrieved.getCanaryVersion());
        Assertions.assertEquals(10, retrieved.getCanaryPercentage());
    }

    @Test
    public void testRemoveConfig() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        manager.addConfig(new CanaryReleaseConfig("user-service", "v1", "v2", 10));
        Assertions.assertNotNull(manager.getConfig("user-service"));
        
        manager.removeConfig("user-service");
        Assertions.assertNull(manager.getConfig("user-service"));
    }

    @Test
    public void testGetAllConfigs() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        manager.addConfig(new CanaryReleaseConfig("service1", "v1", "v2", 10));
        manager.addConfig(new CanaryReleaseConfig("service2", "v1", "v2", 20));
        
        Map<String, CanaryReleaseConfig> allConfigs = manager.getAllConfigs();
        Assertions.assertEquals(2, allConfigs.size());
        Assertions.assertTrue(allConfigs.containsKey("service1"));
        Assertions.assertTrue(allConfigs.containsKey("service2"));
    }

    @Test
    public void testSelectTargetVersion() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        manager.addConfig(new CanaryReleaseConfig("user-service", "v1", "v2", 0));
        GatewayContext context = createMockContext(new HashMap<>());
        
        String version = manager.selectTargetVersion("user-service", context);
        Assertions.assertEquals("v1", version);
    }

    @Test
    public void testHeaderMatcher() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig("user-service", "v1", "v2", 0);
        Map<String, List<String>> headerMatchers = new HashMap<>();
        headerMatchers.put("X-Canary", Collections.singletonList("true"));
        config.setHeaderMatchers(headerMatchers);
        manager.addConfig(config);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Canary", "true");
        GatewayContext context = createMockContext(headers);
        
        boolean shouldRoute = manager.shouldRouteToCanary("user-service", context);
        Assertions.assertTrue(shouldRoute);
    }

    @Test
    public void testCookieMatcher() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig("user-service", "v1", "v2", 0);
        config.setCookieMatchers(Collections.singletonList("canary_user"));
        manager.addConfig(config);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "canary_user=123; other=value");
        GatewayContext context = createMockContext(headers);
        
        boolean shouldRoute = manager.shouldRouteToCanary("user-service", context);
        Assertions.assertTrue(shouldRoute);
    }

    @Test
    public void testIpMatcherExact() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig("user-service", "v1", "v2", 0);
        config.setIpMatchers(Collections.singletonList("192.168.1.100"));
        manager.addConfig(config);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", "192.168.1.100");
        GatewayContext context = createMockContext(headers);
        
        boolean shouldRoute = manager.shouldRouteToCanary("user-service", context);
        Assertions.assertTrue(shouldRoute);
    }

    @Test
    public void testIpMatcherWildcard() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig("user-service", "v1", "v2", 0);
        config.setIpMatchers(Collections.singletonList("192.168.1.*"));
        manager.addConfig(config);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", "192.168.1.50");
        GatewayContext context = createMockContext(headers);
        
        boolean shouldRoute = manager.shouldRouteToCanary("user-service", context);
        Assertions.assertTrue(shouldRoute);
    }

    @Test
    public void testIpMatcherCidr() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        CanaryReleaseConfig config = new CanaryReleaseConfig("user-service", "v1", "v2", 0);
        config.setIpMatchers(Collections.singletonList("192.168.1.0/24"));
        manager.addConfig(config);
        
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", "192.168.1.100");
        GatewayContext context = createMockContext(headers);
        
        boolean shouldRoute = manager.shouldRouteToCanary("user-service", context);
        Assertions.assertTrue(shouldRoute);
    }

    @Test
    public void testNoConfigReturnsNull() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        GatewayContext context = createMockContext(new HashMap<>());
        
        String version = manager.selectTargetVersion("non-existent-service", context);
        Assertions.assertNull(version);
    }

    @Test
    public void testInvalidPercentage() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CanaryReleaseConfig("service", "v1", "v2", 150);
        });
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CanaryReleaseConfig("service", "v1", "v2", -10);
        });
    }

    @Test
    public void testStickySession() {
        CanaryReleaseManager manager = new DefaultCanaryReleaseManager();
        
        manager.addConfig(new CanaryReleaseConfig("user-service", "v1", "v2", 50));
        
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "JSESSIONID=test-session-123");
        GatewayContext context = createMockContext(headers);
        
        String firstVersion = manager.selectTargetVersion("user-service", context);
        String secondVersion = manager.selectTargetVersion("user-service", context);
        
        Assertions.assertEquals(firstVersion, secondVersion);
    }

    private GatewayContext createMockContext(Map<String, String> headers) {
        return new GatewayContext() {
            @Override
            public String getRequestPath() { return "/test"; }
            
            @Override
            public String getRequestMethod() { return "GET"; }
            
            @Override
            public Map<String, String> getRequestHeaders() { return headers; }
            
            @Override
            public byte[] getRequestBody() { return new byte[0]; }
            
            @Override
            public void setResponseStatus(int statusCode) {}
            
            @Override
            public int getResponseStatusCode() { return 200; }
            
            @Override
            public void setResponseHeaders(Map<String, String> headers) {}
            
            @Override
            public Map<String, String> getResponseHeaders() { return new HashMap<>(); }
            
            @Override
            public void setResponseBody(byte[] body) {}
            
            @Override
            public byte[] getResponseBody() { return new byte[0]; }
            
            @Override
            public void setAttribute(String name, Object value) {}
            
            @Override
            public Object getAttribute(String name) { return null; }
        };
    }
}
