package ltd.idcu.est.kotlin.examples

import ltd.idcu.est.kotlin.dsl.*
import ltd.idcu.est.kotlin.coroutines.*
import ltd.idcu.est.kotlin.extensions.*
import kotlinx.coroutines.delay

fun main() {
    println("=== EST Kotlin DSL Example ===\n")
    
    example1_EstApplication()
    println()
    
    example2_Coroutines()
    println()
    
    example3_Extensions()
    println()
    
    example4_CombinedUsage()
}

fun example1_EstApplication() {
    println("Example 1: EST Application DSL")
    println("-" . repeat(40))
    
    val app = estApplication("MyKotlinApp", "1.0.0") {
        description = "A Kotlin-powered EST application"
        author = "EST Team"
        
        web {
            port = 8080
            host = "localhost"
            
            router {
                get("/") { req, res ->
                    res.send("Welcome to EST Kotlin!")
                }
                
                get("/hello") { req, res ->
                    res.send("Hello from Kotlin DSL!")
                }
                
                get("/api/user") { req, res ->
                    res.json("""{"name":"John","age":30}""")
                }
                
                post("/api/user") { req, res ->
                    res.status(201)
                    res.send("User created")
                }
            }
        }
    }
    
    println("Application created: ${app.name} v${app.version}")
    println("Description: ${app.description}")
    println("Author: ${app.author}")
    
    app.run()
}

fun example2_Coroutines() {
    println("Example 2: Coroutines Integration")
    println("-" . repeat(40))
    
    estCoroutineScope {
        println("Starting coroutine example...")
        
        val deferred1 = estAsync {
            delay(100)
            "Result from coroutine 1"
        }
        
        val deferred2 = estAsync {
            delay(150)
            "Result from coroutine 2"
        }
        
        val result1 = deferred1.await()
        val result2 = deferred2.await()
        
        println("Result 1: $result1")
        println("Result 2: $result2")
    }
    
    println("Coroutine example completed")
}

fun example3_Extensions() {
    println("Example 3: Kotlin Extensions")
    println("-" . repeat(40))
    
    val nullString: String? = null
    println("orEmpty: '${nullString.orEmpty()}'")
    
    val emptyString = ""
    println("nullIfEmpty: ${emptyString.nullIfEmpty()}")
    
    val camelCase = "helloWorldExample"
    println("toSnakeCase: ${camelCase.toSnakeCase()}")
    
    val snakeCase = "hello_world_example"
    println("toCamelCase: ${snakeCase.toCamelCase()}")
    
    val longString = "This is a very long string that needs to be truncated"
    println("truncate: '${longString.truncate(30)}'")
    
    val (result, time) = measureTimeMillis {
        Thread.sleep(50)
        "Operation completed"
    }
    println("measureTimeMillis: $result took ${time}ms")
    
    retry(3, 100) {
        println("Retry attempt...")
    }
}

fun example4_CombinedUsage() {
    println("Example 4: Combined Usage")
    println("-" . repeat(40))
    
    val app = estApplication("CombinedApp", "2.0.0") {
        description = "Combined Kotlin features"
        author = "EST Team"
        
        web {
            port = 9090
            router {
                get("/api/data") { req, res ->
                    estCoroutineScope {
                        val data = estAsync {
                            delay(100)
                            mapOf(
                                "status" to "success",
                                "data" to listOf(1, 2, 3)
                            )
                        }
                        res.json(data.await().toString())
                    }
                }
            }
        }
    }
    
    println("Combined application ready: ${app.name}")
}
