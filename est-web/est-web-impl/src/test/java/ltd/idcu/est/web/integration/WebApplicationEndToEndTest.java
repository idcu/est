package ltd.idcu.est.web.integration;

import ltd.idcu.est.test.annotation.AfterAll;
import ltd.idcu.est.test.annotation.BeforeAll;
import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.http.HttpClient;
import ltd.idcu.est.test.http.HttpResponse;
import ltd.idcu.est.test.http.HttpTests;
import ltd.idcu.est.test.http.WebTestServer;
import ltd.idcu.est.web.DefaultWebApplication;
import ltd.idcu.est.web.api.WebApplication;

import java.util.Map;

import static ltd.idcu.est.test.Assertions.*;

public class WebApplicationEndToEndTest {

    private static WebTestServer server;
    private static HttpClient client;

    @BeforeAll
    static void beforeAll() throws Exception {
        DefaultWebApplication app = new DefaultWebApplication("Test Application", "1.0.0");
        app.get("/", (req, res) -> res.send("Hello, World!"));
        
        app.get("/api/greeting", (req, res) -> res.json(Map.of(
            "message", "你好，欢迎使用 EST 框架！",
            "version", "1.3.0"
        )));
        
        app.get("/user/:id", (req, res) -> {
            String userId = req.param("id");
            res.send("用户 ID: " + userId);
        });
        
        app.post("/login", (req, res) -> {
            String username = req.formParam("username");
            String password = req.formParam("password");
            res.json(Map.of(
                "success", "admin".equals(username) && "secret".equals(password),
                "user", username
            ));
        });
        
        server = HttpTests.server(app, 18080);
        
        server.start();
        client = server.getClient();
    }

    @AfterAll
    static void afterAll() throws Exception {
        if (server != null) {
            server.close();
        }
    }

    @Test
    void testRootEndpoint() throws Exception {
        HttpResponse response = client.get("/");
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("Hello, World!", response.getBody());
    }

    @Test
    void testApiGreetingEndpoint() throws Exception {
        HttpResponse response = client.get("/api/greeting");
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getContentType());
        assertTrue(response.getContentType().contains("application/json"));
        assertTrue(response.getBody().contains("你好，欢迎使用 EST 框架！"));
        assertTrue(response.getBody().contains("1.3.0"));
    }

    @Test
    void testUserEndpointWithParameter() throws Exception {
        HttpResponse response = client.get("/user/12345");
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("用户 ID: 12345", response.getBody());
    }

    @Test
    void testLoginEndpointWithValidCredentials() throws Exception {
        HttpResponse response = client.post("/login", Map.of(
            "username", "admin",
            "password", "secret"
        ));
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("\"success\":true"));
        assertTrue(response.getBody().contains("\"user\":\"admin\""));
    }

    @Test
    void testLoginEndpointWithInvalidCredentials() throws Exception {
        HttpResponse response = client.post("/login", Map.of(
            "username", "wrong",
            "password", "password"
        ));
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().contains("\"success\":false"));
    }

    @Test
    void testNotFoundEndpoint() throws Exception {
        HttpResponse response = client.get("/this-does-not-exist");
        assertTrue(response.isClientError());
        assertEquals(404, response.getStatusCode());
    }
}
