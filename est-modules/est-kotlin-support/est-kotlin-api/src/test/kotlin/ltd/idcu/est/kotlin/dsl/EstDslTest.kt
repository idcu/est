package ltd.idcu.est.kotlin.dsl

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EstDslTest {

    @Test
    fun testEstApplicationBasicCreation() {
        val app = estApplication("TestApp", "1.0.0") {
            description = "Test application"
            author = "Test Author"
        }
        
        assertEquals("TestApp", app.name)
        assertEquals("1.0.0", app.version)
        assertEquals("Test application", app.description)
        assertEquals("Test Author", app.author)
        assertNotNull(app.modules)
        assertTrue(app.modules.isEmpty())
    }

    @Test
    fun testEstApplicationWithWebConfig() {
        val app = estApplication("WebApp", "2.0.0") {
            web {
                port = 9090
                host = "localhost"
            }
        }
        
        assertEquals("WebApp", app.name)
        assertEquals("2.0.0", app.version)
        assertNotNull(app.webConfig)
        assertEquals(9090, app.webConfig?.port)
        assertEquals("localhost", app.webConfig?.host)
    }

    @Test
    fun testRouterDsl() {
        val app = estApplication("RouterApp", "1.0.0") {
            web {
                router {
                    get("/api/users") { req, res ->
                        res.send("Users list")
                    }
                    post("/api/users") { req, res ->
                        res.status(201).send("User created")
                    }
                    put("/api/users/{id}") { req, res ->
                        res.send("User updated")
                    }
                    delete("/api/users/{id}") { req, res ->
                        res.status(204)
                    }
                }
            }
        }
        
        assertNotNull(app.webConfig)
        val routes = app.webConfig?.buildRoutes()
        assertNotNull(routes)
        assertEquals(4, routes?.size)
        
        val getRoute = routes?.find { it.method == "GET" && it.path == "/api/users" }
        assertNotNull(getRoute)
        
        val postRoute = routes?.find { it.method == "POST" && it.path == "/api/users" }
        assertNotNull(postRoute)
    }

    @Test
    fun testEstResponse() {
        val response = EstResponse()
        
        response.send("Hello World")
        assertEquals("Hello World", response.body)
        assertEquals(200, response.status)
        
        response.json("{\"key\":\"value\"}")
        assertEquals("{\"key\":\"value\"}", response.body)
        assertEquals("application/json", response.headers["Content-Type"])
        
        response.status(404)
        assertEquals(404, response.status)
        
        response.header("X-Custom", "value")
        assertEquals("value", response.headers["X-Custom"])
    }

    @Test
    fun testEstRequest() {
        val request = EstRequest(
            method = "GET",
            path = "/api/test",
            headers = mapOf("Accept" to listOf("application/json")),
            queryParams = mapOf("page" to listOf("1")),
            body = "test body"
        )
        
        assertEquals("GET", request.method)
        assertEquals("/api/test", request.path)
        assertTrue(request.headers.containsKey("Accept"))
        assertTrue(request.queryParams.containsKey("page"))
        assertEquals("test body", request.body)
    }

    @Test
    fun testApplicationRun() {
        val app = estApplication("RunTestApp", "1.0.0") {
            description = "Test app for run"
            author = "Test"
            web {
                port = 8080
                router {
                    get("/test") { _, _ -> }
                }
            }
        }
        
        app.run()
    }

    @Test
    fun testWebDslMultipleRouters() {
        val app = estApplication("MultiRouterApp", "1.0.0") {
            web {
                router {
                    get("/api/v1/users") { _, _ -> }
                }
                router {
                    get("/api/v2/users") { _, _ -> }
                }
            }
        }
        
        val routes = app.webConfig?.buildRoutes()
        assertEquals(2, routes?.size)
    }

    @Test
    fun testRouteDslHandlerInvocation() {
        var handlerCalled = false
        
        val app = estApplication("HandlerTest", "1.0.0") {
            web {
                router {
                    get("/test") { _, _ ->
                        handlerCalled = true
                    }
                }
            }
        }
        
        val routes = app.webConfig?.buildRoutes()
        val route = routes?.firstOrNull()
        
        assertNotNull(route)
        
        val req = EstRequest("GET", "/test")
        val res = EstResponse()
        route?.handler?.invoke(req, res)
        
        assertTrue(handlerCalled)
    }
}
