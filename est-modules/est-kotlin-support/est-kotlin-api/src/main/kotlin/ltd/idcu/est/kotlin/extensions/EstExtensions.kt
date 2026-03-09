package ltd.idcu.est.kotlin.extensions

import ltd.idcu.est.core.api.Module
import ltd.idcu.est.core.api.Application
import java.util.*

fun String?.orEmpty(): String = this ?: ""

fun String?.orDefault(default: String): String = this ?: default

fun <T> T?.orDefault(default: T): T = this ?: default

fun <T> T?.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) {
        this.block()
    }
    return this
}

fun <T> T?.letIf(condition: Boolean, block: (T) -> Unit): T? {
    if (condition && this != null) {
        block(this)
    }
    return this
}

fun String.toOptional(): Optional<String> = Optional.ofNullable(this)

fun <T> T.toOptional(): Optional<T> = Optional.ofNullable(this)

fun String.isNotNullOrEmpty(): Boolean = this != null && this.isNotEmpty()

fun String?.nullIfEmpty(): String? = if (this.isNullOrEmpty()) null else this

fun <T> List<T>?.nullIfEmpty(): List<T>? = if (this.isNullOrEmpty()) null else this

fun <K, V> Map<K, V>?.nullIfEmpty(): Map<K, V>? = if (this.isNullOrEmpty()) null else this

fun String.capitalizeFirst(): String {
    if (this.isEmpty()) return this
    return this.substring(0, 1).uppercase() + this.substring(1)
}

fun String.decapitalizeFirst(): String {
    if (this.isEmpty()) return this
    return this.substring(0, 1).lowercase() + this.substring(1)
}

fun String.toSnakeCase(): String {
    return this.replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
}

fun String.toCamelCase(): String {
    return this.split('_').joinToString("") { it.capitalizeFirst() }.decapitalizeFirst()
}

fun String.truncate(maxLength: Int, suffix: String = "..."): String {
    if (this.length <= maxLength) return this
    return this.take(maxLength - suffix.length) + suffix
}

fun <T> MutableList<T>.removeIf(predicate: (T) -> Boolean): Boolean {
    var removed = false
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        if (predicate(iterator.next())) {
            iterator.remove()
            removed = true
        }
    }
    return removed
}

fun <T> List<T>.distinctByKey(keySelector: (T) -> Any): List<T> {
    val seen = mutableSetOf<Any>()
    return this.filter { seen.add(keySelector(it)) }
}

fun <T, R> List<T>.mapToSet(transform: (T) -> R): Set<R> {
    return this.mapTo(mutableSetOf(), transform)
}

fun <K, V> Map<K, V>.getOrDefault(key: K, defaultValue: () -> V): V {
    return this[key] ?: defaultValue()
}

fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, mappingFunction: (K) -> V): V {
    return this.getOrPut(key) { mappingFunction(key) }
}

inline fun <T> measureTimeMillis(block: () -> T): Pair<T, Long> {
    val start = System.currentTimeMillis()
    val result = block()
    val end = System.currentTimeMillis()
    return result to (end - start)
}

inline fun <T> measureTimeNanos(block: () -> T): Pair<T, Long> {
    val start = System.nanoTime()
    val result = block()
    val end = System.nanoTime()
    return result to (end - start)
}

inline fun retry(
    times: Int,
    delayMillis: Long = 0,
    block: () -> Unit
) {
    var lastException: Exception? = null
    repeat(times) { attempt ->
        try {
            block()
            return
        } catch (e: Exception) {
            lastException = e
            if (attempt < times - 1 && delayMillis > 0) {
                Thread.sleep(delayMillis)
            }
        }
    }
    throw lastException ?: RuntimeException("Retry failed")
}

inline fun <T> retryOrDefault(
    times: Int,
    defaultValue: T,
    delayMillis: Long = 0,
    block: () -> T
): T {
    repeat(times) { attempt ->
        try {
            return block()
        } catch (e: Exception) {
            if (attempt < times - 1 && delayMillis > 0) {
                Thread.sleep(delayMillis)
            }
        }
    }
    return defaultValue
}
