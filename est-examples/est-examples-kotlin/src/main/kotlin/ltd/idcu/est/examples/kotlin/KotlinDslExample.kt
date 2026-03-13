package ltd.idcu.est.examples.kotlin

import ltd.idcu.est.kotlin.dsl.estApplication
import ltd.idcu.est.kotlin.coroutines.estCoroutineScope
import ltd.idcu.est.kotlin.coroutines.estAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    println("=".repeat(80))
    println("  EST Framework Kotlin Example")
    println("=".repeat(80))
    
    example1_DslApplication()
    println()
    example2_Coroutines()
    println()
    example3_WebApplication()
    println()
    println("=".repeat(80))
    println("  All examples completed!")
    println("=".repeat(80))
}

fun example1_DslApplication() {
    println("\n[Example 1] Kotlin DSL Create Application")
    println("-".repeat(80))
    
    val app = estApplication("MyKotlinApp", "1.0.0") {
        description = "This is an EST application created with Kotlin DSL"
        author = "EST Team"
        
        web {
            port = 8080
            host = "0.0.0.0"
            
            router {
                get("/hello") { req, res ->
                    res.send("Hello, Kotlin!")
                }
                
                get("/api/users") { req, res ->
                    res.json("""{"users": ["Alice", "Bob", "Charlie"]}""")
                }
                
                post("/api/users") { req, res ->
                    res.status(201)
                    res.send("User created successfully")
                }
            }
        }
    }
    
    println("Application name: ${app.name}")
    println("Application version: ${app.version}")
    println("Application description: ${app.description}")
    println("Author: ${app.author}")
    
    app.webConfig?.let { webConfig ->
        println("Web service port: ${webConfig.port}")
        println("Web service host: ${webConfig.host}")
        println("Route configuration:")
        webConfig.buildRoutes().forEach { route ->
            println("  ${route.method} ${route.path}")
        }
    }
    
    println()
    println("Running application...")
    app.run()
}

fun example2_Coroutines() {
    println("\n[Example 2] Kotlin Coroutines Integration")
    println("-".repeat(80))
    
    runBlocking {
        println("Starting coroutines with estCoroutineScope...")
        
        val job = estCoroutineScope {
            println("Coroutine 1 started")
            delay(500)
            println("Coroutine 1 completed")
        }
        
        val result1 = estAsync {
            delay(300)
            "Coroutine 2 result"
        }
        
        val result2 = estAsync {
            delay(400)
            42
        }
        
        println("Waiting for coroutines to complete...")
        job.join()
        
        println("Coroutine 2 result: ${result1.await()}")
        println("Coroutine 3 result: ${result2.await()}")
        
        println()
        println("All coroutines completed!")
    }
}

fun example3_WebApplication() {
    println("\n[Example 3] Web Application Complete Example")
    println("-".repeat(80))
    
    val webApp = estApplication("KotlinWebApp", "2.0.0") {
        description = "Complete Kotlin Web Application Example"
        author = "EST Team"
        
        web {
            port = 9090
            
            router {
                get("/") { req, res ->
                    res.send("""
                        <html>
                        <head><title>EST Kotlin Web App</title></head>
                        <body>
                            <h1>Welcome to EST Framework Kotlin!</h1>
                            <ul>
                                <li><a href="/hello">Hello</a></li>
                                <li><a href="/api/users">User List</a></li>
                                <li><a href="/api/status">Status</a></li>
                            </ul>
                        </body>
                        </html>
                    """.trimIndent())
                }
                
                get("/api/status") { req, res ->
                    res.header("Content-Type", "application/json")
                    res.send("""
                        {
                            "status": "running",
                            "version": "2.0.0",
                            "framework": "EST",
                            "language": "Kotlin"
                        }
                    """.trimIndent())
                }
                
                put("/api/users/{id}") { req, res ->
                    val userId = req.path.substringAfterLast("/")
                    res.send("User $userId updated")
                }
                
                delete("/api/users/{id}") { req, res ->
                    val userId = req.path.substringAfterLast("/")
                    res.status(204)
                    res.send("")
                }
            }
        }
    }
    
    println("Web Application Configuration:")
    println("  Name: ${webApp.name}")
    println("  Version: ${webApp.version}")
    
    webApp.webConfig?.let { webConfig ->
        println("  Port: ${webConfig.port}")
        println("  Route count: ${webConfig.buildRoutes().size}")
        println("  Route list:")
        webConfig.buildRoutes().forEachIndexed { index, route ->
            println("    ${index + 1}. ${route.method} ${route.path}")
        }
    }
    
    println()
    println("Web application configured!")
}
