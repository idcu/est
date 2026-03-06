package ltd.idcu.est.web;

import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.web.api.HttpMethod;
import ltd.idcu.est.web.api.Route;
import ltd.idcu.est.web.api.RouteHandler;
import ltd.idcu.est.web.api.Router;

import java.util.List;
import java.util.Map;

public class OptimizedRouterTest {

    @Test
    public void testCreateRouter() {
        Router router = new OptimizedRouter();
        Assertions.assertNotNull(router);
        Assertions.assertEquals(0, router.size());
    }

    @Test
    public void testAddGetRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/test", "testHandler");

        Assertions.assertEquals(1, router.size());

        Route route = router.match("/test", HttpMethod.GET);
        Assertions.assertNotNull(route);
        Assertions.assertEquals("testHandler", route.getHandler());
    }

    @Test
    public void testAddPostRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.post("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.POST);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testAddPutRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.put("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.PUT);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testAddDeleteRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.delete("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.DELETE);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testAddPatchRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.patch("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.PATCH);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testAddHeadRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.head("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.HEAD);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testAddOptionsRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.options("/test", "testHandler");

        Route route = router.match("/test", HttpMethod.OPTIONS);
        Assertions.assertNotNull(route);
    }

    @Test
    public void testRouteWithRouteHandler() {
        OptimizedRouter router = new OptimizedRouter();
        boolean[] handlerCalled = {false};

        RouteHandler handler = (req, res) -> {
            handlerCalled[0] = true;
        };

        router.get("/test", handler);

        Route route = router.match("/test", HttpMethod.GET);
        Assertions.assertNotNull(route);
        Assertions.assertNotNull(route.getRouteHandler());
    }

    @Test
    public void testRouteMatching() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/users", "usersHandler");
        router.get("/users/{id}", "userHandler");

        Route route1 = router.match("/users", HttpMethod.GET);
        Assertions.assertNotNull(route1);
        Assertions.assertEquals("usersHandler", route1.getHandler());

        Route route2 = router.match("/users/123", HttpMethod.GET);
        Assertions.assertNotNull(route2);
        Assertions.assertEquals("userHandler", route2.getHandler());
    }

    @Test
    public void testPathVariables() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/users/{id}/posts/{postId}", "postHandler");

        Route route = router.match("/users/123/posts/456", HttpMethod.GET);
        Assertions.assertNotNull(route);
        Assertions.assertTrue(route.hasPathVariables());

        Map<String, String> variables = route.extractPathVariables("/users/123/posts/456");
        Assertions.assertEquals("123", variables.get("id"));
        Assertions.assertEquals("456", variables.get("postId"));
    }

    @Test
    public void testRouteGroups() {
        OptimizedRouter router = new OptimizedRouter();

        router.group("/api", (r, r2) -> {
            r.get("/users", "apiUsersHandler");
            r.get("/posts", "apiPostsHandler");
        });

        Assertions.assertEquals(2, router.size());
        Assertions.assertNotNull(router.match("/api/users", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/api/posts", HttpMethod.GET));
    }

    @Test
    public void testNamedRoutes() {
        OptimizedRouter router = new OptimizedRouter();
        router.name("home").get("/", "homeHandler");

        Route route = router.getRouteByName("home");
        Assertions.assertNotNull(route);
        Assertions.assertEquals("/", route.getPath());
    }

    @Test
    public void testGetRoutes() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/route1", "handler1");
        router.get("/route2", "handler2");

        List<Route> routes = router.getRoutes();
        Assertions.assertEquals(2, routes.size());
    }

    @Test
    public void testGetRoutesByMethod() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/get", "getHandler");
        router.post("/post", "postHandler");
        router.get("/get2", "getHandler2");

        List<Route> getRoutes = router.getRoutesByMethod(HttpMethod.GET);
        Assertions.assertEquals(2, getRoutes.size());

        List<Route> postRoutes = router.getRoutesByMethod(HttpMethod.POST);
        Assertions.assertEquals(1, postRoutes.size());
    }

    @Test
    public void testGetRoutesByPath() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/test", "getHandler");
        router.post("/test", "postHandler");

        List<Route> routes = router.getRoutesByPath("/test");
        Assertions.assertEquals(2, routes.size());
    }

    @Test
    public void testHasRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/test", "handler");

        Assertions.assertTrue(router.hasRoute("/test", HttpMethod.GET));
        Assertions.assertFalse(router.hasRoute("/test", HttpMethod.POST));
        Assertions.assertFalse(router.hasRoute("/nonexistent", HttpMethod.GET));
    }

    @Test
    public void testRemoveRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/test", "handler");

        Assertions.assertEquals(1, router.size());

        router.removeRoute("/test", HttpMethod.GET);

        Assertions.assertEquals(0, router.size());
        Assertions.assertNull(router.match("/test", HttpMethod.GET));
    }

    @Test
    public void testClearRoutes() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/route1", "handler1");
        router.get("/route2", "handler2");

        Assertions.assertEquals(2, router.size());

        router.clear();

        Assertions.assertEquals(0, router.size());
        Assertions.assertTrue(router.isEmpty());
    }

    @Test
    public void testAddRouteDirectly() {
        OptimizedRouter router = new OptimizedRouter();
        Route route = new DefaultRoute("/direct", HttpMethod.GET, "directHandler");

        router.addRoute(route);

        Assertions.assertEquals(1, router.size());
        Assertions.assertNotNull(router.match("/direct", HttpMethod.GET));
    }

    @Test
    public void testWildcardRoute() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/files/*", "fileHandler");

        Assertions.assertNotNull(router.match("/files/doc.txt", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/files/images/photo.jpg", HttpMethod.GET));
    }

    @Test
    public void testRouteWithTrailingSlash() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/test", "handler");

        Assertions.assertNotNull(router.match("/test", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/test/", HttpMethod.GET));
    }

    @Test
    public void testMultiLevelRoutes() {
        OptimizedRouter router = new OptimizedRouter();
        router.get("/", "rootHandler");
        router.get("/a", "aHandler");
        router.get("/a/b", "abHandler");
        router.get("/a/b/c", "abcHandler");

        Assertions.assertNotNull(router.match("/", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/a", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/a/b", HttpMethod.GET));
        Assertions.assertNotNull(router.match("/a/b/c", HttpMethod.GET));
    }
}
