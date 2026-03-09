package ltd.idcu.est.kotlin.extensions

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.Instant

fun String?.nullIfBlank(): String? = if (isNullOrBlank()) null else this

fun String?.orEmpty(): String = this ?: ""

fun String.truncate(maxLength: Int, suffix: String = "..."): String {
    if (length <= maxLength) return this
    return take(maxLength - suffix.length) + suffix
}

fun <T> List<T>?.nullIfEmpty(): List<T>? = if (isNullOrEmpty()) null else this

fun <T> List<T>?.orEmpty(): List<T> = this ?: emptyList()

fun <T> MutableList<T>.removeIf(predicate: (T) -> Boolean): Boolean {
    val iterator = iterator()
    var removed = false
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
            removed = true
        }
    }
    return removed
}

fun <K, V> Map<K, V>?.nullIfEmpty(): Map<K, V>? = if (isNullOrEmpty()) null else this

fun <K, V> Map<K, V>?.orEmpty(): Map<K, V> = this ?: emptyMap()

fun <T> T?.ifNull(defaultValue: () -> T): T = this ?: defaultValue()

fun <T> T?.ifNullOrEmpty(defaultValue: () -> T): T where T : CharSequence =
    if (isNullOrEmpty()) defaultValue() else this

fun <T> T?.takeIf(condition: Boolean): T? = if (condition) this else null

fun <T> T?.takeUnless(condition: Boolean): T? = if (!condition) this else null

infix fun <T> T?.coalesce(other: T): T = this ?: other

fun <T, R> T?.letIf(condition: Boolean, block: (T) -> R): R? =
    if (condition && this != null) block(this) else null

fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) block()
    return this
}

fun <T> Result<T>.onSuccessLog(block: (T) -> Unit): Result<T> {
    onSuccess(block)
    return this
}

fun <T> Result<T>.onFailureLog(block: (Throwable) -> Unit): Result<T> {
    onFailure(block)
    return this
}

fun <T> Result<T>.getOrNullLog(): T? {
    onFailure { println("Error: ${it.message}") }
    return getOrNull()
}

fun Duration.toHumanReadable(): String {
    val seconds = seconds
    return when {
        seconds < 60 -> "${seconds}s"
        seconds < 3600 -> "${seconds / 60}m ${seconds % 60}s"
        else -> "${seconds / 3600}h ${(seconds % 3600) / 60}m"
    }
}

fun Instant.elapsed(): Duration = Duration.between(this, Instant.now())

fun Instant.isOlderThan(duration: Duration): Boolean = elapsed() > duration

fun Instant.isNewerThan(duration: Duration): Boolean = elapsed() < duration

fun <T> timer(block: () -> T): Pair<T, Duration> {
    val start = Instant.now()
    val result = block()
    val end = Instant.now()
    return result to Duration.between(start, end)
}

suspend fun <T> timerSuspend(block: suspend () -> T): Pair<T, Duration> {
    val start = Instant.now()
    val result = block()
    val end = Instant.now()
    return result to Duration.between(start, end)
}

fun <T> Flow<T>.throttle(periodMillis: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime >= periodMillis) {
            emit(value)
            lastEmissionTime = currentTime
        }
    }
}

fun <T> Flow<T>.debounce(timeoutMillis: Long): Flow<T> = flow {
    var lastValue: T? = null
    var job: kotlinx.coroutines.Job? = null
    
    collect { value ->
        lastValue = value
        job?.cancel()
        job = kotlinx.coroutines.GlobalScope.launch {
            delay(timeoutMillis)
            lastValue?.let { emit(it) }
        }
    }
}

fun <T> List<T>.paginate(page: Int, pageSize: Int): List<T> {
    val fromIndex = page * pageSize
    if (fromIndex >= size) return emptyList()
    val toIndex = minOf(fromIndex + pageSize, size)
    return subList(fromIndex, toIndex)
}

fun <T, K> List<T>.groupByIntoList(keySelector: (T) -> K): Map<K, List<T>> =
    groupBy(keySelector)

fun <T, K, V> List<T>.associateByNonNull(keySelector: (T) -> K?, valueTransform: (T) -> V): Map<K, V> =
    mapNotNull { t -> keySelector(t)?.let { it to valueTransform(t) } }
        .toMap()

fun String.toCamelCase(): String {
    return split('_', '-', ' ')
        .mapIndexed { index, s ->
            if (index == 0) s.lowercase()
            else s.lowercase().replaceFirstChar { it.uppercaseChar() }
        }
        .joinToString("")
}

fun String.toSnakeCase(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1_$2")
        .lowercase()
        .replace(' ', '_')
        .replace('-', '_')
}

fun String.toKebabCase(): String {
    return replace(Regex("([a-z])([A-Z])"), "$1-$2")
        .lowercase()
        .replace(' ', '-')
        .replace('_', '-')
}
