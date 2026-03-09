package ltd.idcu.est.examples.kotlin

import ltd.idcu.est.kotlin.dsl.estApplication
import ltd.idcu.est.kotlin.coroutines.estCoroutineScope
import ltd.idcu.est.kotlin.coroutines.estAsync
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() {
    println("=".repeat(80))
    println("  EST Framework Kotlin 示例")
    println("=".repeat(80))
    
    example1_DslApplication()
    println()
    example2_Coroutines()
    println()
    example3_WebApplication()
    println()
    println("=".repeat(80))
    println("  所有示例运行完成！")
    println("=".repeat(80))
}

fun example1_DslApplication() {
    println("\n【示例 1】Kotlin DSL 创建应用")
    println("-".repeat(80))
    
    val app = estApplication("MyKotlinApp", "1.0.0") {
        description = "这是一个使用 Kotlin DSL 创建的 EST 应用"
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
    
    println("应用名称: ${app.name}")
    println("应用版本: ${app.version}")
    println("应用描述: ${app.description}")
    println("作者: ${app.author}")
    
    app.webConfig?.let { webConfig ->
        println("Web 服务端口: ${webConfig.port}")
        println("Web 服务主机: ${webConfig.host}")
        println("路由配置:")
        webConfig.buildRoutes().forEach { route ->
            println("  ${route.method} ${route.path}")
        }
    }
    
    println()
    println("运行应用...")
    app.run()
}

fun example2_Coroutines() {
    println("\n【示例 2】Kotlin 协程集成")
    println("-".repeat(80))
    
    runBlocking {
        println("使用 estCoroutineScope 启动协程...")
        
        val job = estCoroutineScope {
            println("协程 1 开始执行")
            delay(500)
            println("协程 1 执行完成")
        }
        
        val result1 = estAsync {
            delay(300)
            "协程 2 的结果"
        }
        
        val result2 = estAsync {
            delay(400)
            42
        }
        
        println("等待协程完成...")
        job.join()
        
        println("协程 2 结果: ${result1.await()}")
        println("协程 3 结果: ${result2.await()}")
        
        println()
        println("所有协程执行完成！")
    }
}

fun example3_WebApplication() {
    println("\n【示例 3】Web 应用完整示例")
    println("-".repeat(80))
    
    val webApp = estApplication("KotlinWebApp", "2.0.0") {
        description = "完整的 Kotlin Web 应用示例"
        author = "EST Team"
        
        web {
            port = 9090
            
            router {
                get("/") { req, res ->
                    res.send("""
                        <html>
                        <head><title>EST Kotlin Web App</title></head>
                        <body>
                            <h1>欢迎使用 EST Framework Kotlin!</h1>
                            <ul>
                                <li><a href="/hello">Hello</a></li>
                                <li><a href="/api/users">用户列表</a></li>
                                <li><a href="/api/status">状态</a></li>
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
    
    println("Web 应用配置:")
    println("  名称: ${webApp.name}")
    println("  版本: ${webApp.version}")
    
    webApp.webConfig?.let { webConfig ->
        println("  端口: ${webConfig.port}")
        println("  路由数量: ${webConfig.buildRoutes().size}")
        println("  路由列表:")
        webConfig.buildRoutes().forEachIndexed { index, route ->
            println("    ${index + 1}. ${route.method} ${route.path}")
        }
    }
    
    println()
    println("Web 应用已配置完成！")
}
