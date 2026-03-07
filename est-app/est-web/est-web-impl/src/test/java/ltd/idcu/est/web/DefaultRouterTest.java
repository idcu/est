package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Route;
import ltd.idcu.est.web.api.Router;

import java.util.List;

public class DefaultRouterTest {

    @Test
    public void testRouterCreation() {
        Router router = new DefaultRouter();
        Assertions.assertNotNull(router);
        Assertions.assertEquals(0, router.size());
    }

    @Test
    public void testAddGetRoute() {
        Router router = new DefaultRouter();
        router.get("/test", (req, res) -> { res.ok(); res.text("OK"); });
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.GET));
    }

    @Test
    public void testAddPostRoute() {
        Router router = new DefaultRouter();
        router.post("/test", (req, res) -> { res.ok(); res.text("Created"); });
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.POST));
        Assertions.assertFalse(router.hasRoute("/test", HttpMethod.GET));
    }

    @Test
    public void testAddPutRoute() {
        Router router = new DefaultRouter();
        router.put("/test", (req, res) -> { res.ok(); res.text("Updated"); });
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.PUT));
    }

    @Test
    public void testAddDeleteRoute() {
        Router router = new DefaultRouter();
        router.delete("/test", (req, res) -> { res.ok(); res.text("Deleted"); });
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.DELETE));
    }

    @Test
    public void testAddPatchRoute() {
        Router router = new DefaultRouter();
        router.patch("/test", (req, res) -> { res.ok(); res.text("Patched"); });
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.PATCH));
    }

    @Test
    public void testAddHeadRoute() {
        Router router = new DefaultRouter();
        router.head("/test", (req, res) -> res.ok());
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.HEAD));
    }

    @Test
    public void testAddOptionsRoute() {
        Router router = new DefaultRouter();
        router.options("/test", (req, res) -> res.ok());
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.OPTIONS));
    }

    @Test
    public void testMultipleRoutes() {
        Router router = new DefaultRouter();
        router.get("/home", (req, res) -> { res.ok(); res.text("Home"); });
        router.post("/api/users", (req, res) -> { res.ok(); res.text("Created"); });
        router.get("/api/users/:id", (req, res) -> { res.ok(); res.text("User"); });
        
        Assertions.assertEquals(3, router.size());
    }

    @Test
    public void testMatchRoute() {
        Router router = new DefaultRouter();
        router.get("/test", (req, res) -> { res.ok(); res.text("OK"); });
        
        Route matched = router.match("/test", HttpMethod.GET);
        Assertions.assertNotNull(matched);
        Assertions.assertEquals("/test", matched.getPath());
        Assertions.assertEquals(HttpMethod.GET, matched.getMethod());
    }

    @Test
    public void testMatchNonExistentRoute() {
        Router router = new DefaultRouter();
        router.get("/test", (req, res) -> { res.ok(); res.text("OK"); });
        
        Route matched = router.match("/nonexistent", HttpMethod.GET);
        Assertions.assertNull(matched);
    }

    @Test
    public void testGetRoutes() {
        Router router = new DefaultRouter();
        router.get("/route1", (req, res) -> { res.ok(); res.text("1"); });
        router.get("/route2", (req, res) -> { res.ok(); res.text("2"); });
        
        List<Route> routes = router.getRoutes();
        Assertions.assertEquals(2, routes.size());
    }

    @Test
    public void testGetRoutesByMethod() {
        Router router = new DefaultRouter();
        router.get("/get1", (req, res) -> res.ok());
        router.get("/get2", (req, res) -> res.ok());
        router.post("/post1", (req, res) -> res.ok());
        
        List<Route> getRoutes = router.getRoutesByMethod(HttpMethod.GET);
        Assertions.assertEquals(2, getRoutes.size());
        
        List<Route> postRoutes = router.getRoutesByMethod(HttpMethod.POST);
        Assertions.assertEquals(1, postRoutes.size());
    }

    @Test
    public void testRemoveRoute() {
        Router router = new DefaultRouter();
        router.get("/test", (req, res) -> { res.ok(); res.text("OK"); });
        
        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.GET));
        
        router.removeRoute("/test", HttpMethod.GET);
        
        Assertions.assertFalse(router.hasRoute("/test", HttpMethod.GET));
        Assertions.assertEquals(0, router.size());
    }

    @Test
    public void testClearRoutes() {
        Router router = new DefaultRouter();
        router.get("/route1", (req, res) -> res.ok());
        router.post("/route2", (req, res) -> res.ok());
        
        Assertions.assertEquals(2, router.size());
        
        router.clear();
        
        Assertions.assertEquals(0, router.size());
    }

    @Test
    public void testNamedRoute() {
        DefaultRouter router = new DefaultRouter();
        router.name("home").get("/home", (req, res) -> { res.ok(); res.text("Home"); });
        
        Route namedRoute = router.getRouteByName("home");
        Assertions.assertNotNull(namedRoute);
        Assertions.assertEquals("/home", namedRoute.getPath());
    }

    @Test
    public void testPrefix() {
        DefaultRouter router = new DefaultRouter();
        router.prefix("/api").get("/users", (req, res) -> { res.ok(); res.text("Users"); });
        
        Assertions.assertTrue(router.hasRoute("/api/users", HttpMethod.GET));
    }

    @Test
    public void testGroup() {
        Router router = new DefaultRouter();
        router.group("/api", (r, _r) -> {
            r.get("/users", (req, res) -> { res.ok(); res.text("Users"); });
            r.post("/users", (req, res) -> { res.ok(); res.text("Created"); });
        });
        
        Assertions.assertTrue(router.hasRoute("/api/users", HttpMethod.GET));
        Assertions.assertTrue(router.hasRoute("/api/users", HttpMethod.POST));
    }

    @Test
    public void testAddRouteDirectly() {
        Router router = new DefaultRouter();
        Route route = new DefaultRoute("/direct", HttpMethod.GET, (req, res) -> { res.ok(); res.text("Direct"); });
        router.addRoute(route);
        
        Assertions.assertEquals(1, router.size());
        Assertions.assertTrue(router.hasRoute("/direct", HttpMethod.GET));
    }

    @Test
    public void testGetRoutesByPath() {
        Router router = new DefaultRouter();
        router.get("/path", (req, res) -> res.ok());
        router.post("/path", (req, res) -> res.ok());
        
        List<Route> routes = router.getRoutesByPath("/path");
        Assertions.assertEquals(2, routes.size());
    }
}
