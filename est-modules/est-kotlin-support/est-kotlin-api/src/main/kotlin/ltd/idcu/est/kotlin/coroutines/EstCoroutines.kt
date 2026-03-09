package ltd.idcu.est.kotlin.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

object EstDispatchers {
    val Default: CoroutineDispatcher = Dispatchers.Default
    val IO: CoroutineDispatcher = Dispatchers.IO
    val Main: CoroutineDispatcher = Dispatchers.Main
    val Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    
    private val estExecutor: ExecutorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() * 2
    )
    
    val Est: CoroutineDispatcher = estExecutor.asCoroutineDispatcher()
}

fun estCoroutineScope(
    context: CoroutineContext = EstDispatchers.Est,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return CoroutineScope(context).launch(block = block)
}

suspend fun <T> estAsync(
    context: CoroutineContext = EstDispatchers.Est,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return CoroutineScope(context).async(block = block)
}

fun <T> estFuture(
    context: CoroutineContext = EstDispatchers.Est,
    block: suspend CoroutineScope.() -> T
): CompletableFuture<T> {
    return CoroutineScope(context).future(block = block)
}

suspend fun <T> withEstContext(
    context: CoroutineContext = EstDispatchers.Est,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(context, block)
}

suspend fun delayEst(
    timeoutMillis: Long,
    block: suspend () -> Unit
) {
    withTimeout(timeoutMillis) {
        block()
    }
}

suspend fun <T> delayEstOrDefault(
    timeoutMillis: Long,
    defaultValue: T,
    block: suspend () -> T
): T {
    return withTimeoutOrNull(timeoutMillis) {
        block()
    } ?: defaultValue
}

class EstCoroutineScope(
    override val coroutineContext: CoroutineContext = EstDispatchers.Est
) : CoroutineScope {
    
    fun shutdown() {
        coroutineContext.cancel()
    }
}

fun estCoroutineScope(
    context: CoroutineContext = EstDispatchers.Est
): EstCoroutineScope {
    return EstCoroutineScope(context)
}
