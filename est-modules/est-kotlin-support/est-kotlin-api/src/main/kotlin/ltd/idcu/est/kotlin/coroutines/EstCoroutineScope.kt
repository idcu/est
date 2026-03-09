package ltd.idcu.est.kotlin.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class EstCoroutineScope(
    name: String = "EST-Coroutine",
    threadCount: Int = Runtime.getRuntime().availableProcessors()
) : CoroutineScope {
    
    private val dispatcher = Executors.newFixedThreadPool(threadCount) { r ->
        Thread(r, "$name-${r.hashCode()}").apply { isDaemon = true }
    }.asCoroutineDispatcher()
    
    private val job = SupervisorJob()
    
    override val coroutineContext: CoroutineContext
        get() = dispatcher + job + CoroutineName(name)
    
    fun shutdown() {
        job.cancel()
        dispatcher.close()
    }
    
    suspend fun <T> withTimeout(timeMillis: Long, block: suspend CoroutineScope.() -> T): T {
        return withTimeout(timeMillis, block)
    }
    
    suspend fun <T> withTimeoutOrNull(timeMillis: Long, block: suspend CoroutineScope.() -> T): T? {
        return withTimeoutOrNull(timeMillis, block)
    }
    
    fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return async { block() }
    }
    
    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return launch { block() }
    }
}

object GlobalEstScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()
}

suspend fun <T> EstCoroutineScope.runWithContext(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context, block)
}

suspend fun <T> retry(
    times: Int = 3,
    delayMillis: Long = 1000,
    predicate: (Throwable) -> Boolean = { true },
    block: suspend () -> T
): T {
    var lastException: Throwable? = null
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Throwable) {
            if (!predicate(e)) throw e
            lastException = e
            if (attempt < times - 1) {
                delay(delayMillis)
            }
        }
    }
    throw lastException!!
}

suspend fun <T> withRetry(
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000,
    multiplier: Double = 2.0,
    block: suspend () -> T
): T {
    var delayMs = initialDelayMs
    var lastException: Throwable? = null
    
    repeat(maxRetries) { attempt ->
        try {
            return block()
        } catch (e: Throwable) {
            lastException = e
            if (attempt < maxRetries - 1) {
                delay(delayMs)
                delayMs = (delayMs * multiplier).toLong()
            }
        }
    }
    throw lastException!!
}
