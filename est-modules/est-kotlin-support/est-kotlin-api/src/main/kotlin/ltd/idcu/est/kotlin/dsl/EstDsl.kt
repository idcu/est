package ltd.idcu.est.kotlin.dsl

import ltd.idcu.est.core.api.Module
import ltd.idcu.est.core.api.Application

@DslMarker
annotation class EstDslMarker

@EstDslMarker
interface EstDsl

fun estApplication(name: String, version: String, init: EstApplicationDsl.() -> Unit): EstApplication {
    val dsl = EstApplicationDsl(name, version)
    dsl.init()
    return dsl.build()
}

@EstDsl
class EstApplicationDsl(val name: String, val version: String) {
    var description: String = ""
    var author: String = ""
    private val modules = mutableListOf<Module>()
    private var webConfig: WebDsl? = null
    
    fun module(module: Module) {
        modules.add(module)
    }
    
    fun web(init: WebDsl.() -> Unit) {
        val dsl = WebDsl()
        dsl.init()
        webConfig = dsl
    }
    
    fun build(): EstApplication {
        return EstApplication(name, version, description, author, modules, webConfig)
    }
}

@EstDsl
class WebDsl {
    var port: Int = 8080
    var host: String = "0.0.0.0"
    private val routes = mutableListOf<RouteDsl>()
    
    fun router(init: RouterDsl.() -> Unit) {
        val dsl = RouterDsl()
        dsl.init()
        routes.addAll(dsl.routes)
    }
    
    fun buildRoutes(): List<RouteDsl> = routes.toList()
}

@EstDsl
class RouterDsl {
    val routes = mutableListOf<RouteDsl>()
    
    fun get(path: String, handler: (EstRequest, EstResponse) -> Unit) {
        routes.add(RouteDsl("GET", path, handler))
    }
    
    fun post(path: String, handler: (EstRequest, EstResponse) -> Unit) {
        routes.add(RouteDsl("POST", path, handler))
    }
    
    fun put(path: String, handler: (EstRequest, EstResponse) -> Unit) {
        routes.add(RouteDsl("PUT", path, handler))
    }
    
    fun delete(path: String, handler: (EstRequest, EstResponse) -> Unit) {
        routes.add(RouteDsl("DELETE", path, handler))
    }
}

data class RouteDsl(
    val method: String,
    val path: String,
    val handler: (EstRequest, EstResponse) -> Unit
)

data class EstRequest(
    val method: String,
    val path: String,
    val headers: Map<String, List<String>> = emptyMap(),
    val queryParams: Map<String, List<String>> = emptyMap(),
    val body: String? = null
)

data class EstResponse(
    var status: Int = 200,
    val headers: MutableMap<String, String> = mutableMapOf(),
    var body: String? = null
) {
    fun send(content: String) {
        body = content
    }
    
    fun json(content: String) {
        headers["Content-Type"] = "application/json"
        body = content
    }
    
    fun status(code: Int) {
        status = code
    }
    
    fun header(name: String, value: String) {
        headers[name] = value
    }
}

data class EstApplication(
    val name: String,
    val version: String,
    val description: String,
    val author: String,
    val modules: List<Module>,
    val webConfig: WebDsl?
) {
    fun run() {
        println("Starting EST Application: $name v$version")
        println("Description: $description")
        println("Author: $author")
        webConfig?.let {
            println("Web server starting on ${it.host}:${it.port}")
            it.buildRoutes().forEach { route ->
                println("  ${route.method} ${route.path}")
            }
        }
        println("Application started successfully!")
    }
}
