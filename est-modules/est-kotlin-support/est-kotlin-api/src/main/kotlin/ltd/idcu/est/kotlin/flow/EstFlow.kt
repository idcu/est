package ltd.idcu.est.kotlin.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Duration
import java.util.concurrent.atomic.AtomicInteger

fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { value ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime >= periodMillis) {
            emit(value)
            lastEmissionTime = currentTime
        }
    }
}

fun <T> Flow<T>.throttleLatest(periodMillis: Long): Flow<T> = flow {
    var latestValue: T? = null
    var timerJob: Job? = null
    val mutex = Mutex()
    
    collect { value ->
        mutex.withLock {
            latestValue = value
            if (timerJob == null || timerJob?.isCompleted == true) {
                timerJob = coroutineScope {
                    launch {
                        delay(periodMillis)
                        mutex.withLock {
                            latestValue?.let { emit(it) }
                            latestValue = null
                        }
                    }
                }
            }
        }
    }
}

fun <T> Flow<T>.bufferCount(count: Int, skip: Int = count): Flow<List<T>> = flow {
    val buffer = mutableListOf<T>()
    collect { value ->
        buffer.add(value)
        if (buffer.size >= count) {
            emit(buffer.toList())
            if (skip <= count) {
                buffer.clear()
            } else {
                repeat(skip) { if (buffer.isNotEmpty()) buffer.removeAt(0) }
            }
        }
    }
    if (buffer.isNotEmpty()) {
        emit(buffer)
    }
}

fun <T> Flow<T>.bufferTime(durationMillis: Long): Flow<List<T>> = flow {
    val buffer = mutableListOf<T>()
    var timerJob: Job? = null
    
    onCompletion {
        timerJob?.cancel()
        if (buffer.isNotEmpty()) {
            emit(buffer.toList())
        }
    }
    
    collect { value ->
        buffer.add(value)
        if (timerJob == null || timerJob?.isCompleted == true) {
            timerJob = coroutineScope {
                launch {
                    delay(durationMillis)
                    if (buffer.isNotEmpty()) {
                        emit(buffer.toList())
                        buffer.clear()
                    }
                }
            }
        }
    }
}

fun <T, K> Flow<T>.groupBy(keySelector: (T) -> K): Flow<Pair<K, List<T>>> = flow {
    val groups = mutableMapOf<K, MutableList<T>>()
    collect { value ->
        val key = keySelector(value)
        groups.getOrPut(key) { mutableListOf() }.add(value)
    }
    groups.forEach { (key, list) ->
        emit(key to list)
    }
}

fun <T> Flow<T>.distinctUntilChanged(comparator: (T, T) -> Boolean): Flow<T> = flow {
    var previous: T? = null
    collect { value ->
        val shouldEmit = previous == null || !comparator(previous!!, value)
        if (shouldEmit) {
            emit(value)
            previous = value
        }
    }
}

fun <T> Flow<T>.scan(initial: T, operation: (T, T) -> T): Flow<T> = flow {
    var accumulator = initial
    emit(accumulator)
    collect { value ->
        accumulator = operation(accumulator, value)
        emit(accumulator)
    }
}

fun <T> Flow<T>.window(size: Int, step: Int = 1): Flow<List<T>> = flow {
    val window = mutableListOf<T>()
    var index = 0
    
    collect { value ->
        window.add(value)
        
        if (window.size >= size) {
            emit(window.take(size))
        }
        
        index++
        if (index >= step) {
            repeat(step) { if (window.isNotEmpty()) window.removeAt(0) }
            index = 0
        }
    }
}

fun <T> Flow<T>.retryWhen(
    maxRetries: Int = 3,
    delayMillis: Long = 1000,
    predicate: suspend (Throwable) -> Boolean = { true }
): Flow<T> = flow {
    var retryCount = 0
    while (true) {
        try {
            collect { value ->
                emit(value)
                retryCount = 0
            }
            return@flow
        } catch (e: Throwable) {
            if (retryCount >= maxRetries || !predicate(e)) {
                throw e
            }
            retryCount++
            delay(delayMillis)
        }
    }
}

fun <T> Flow<T>.onErrorResumeNext(fallback: Flow<T>): Flow<T> = catch { emitAll(fallback) }

fun <T> Flow<T>.onErrorReturnItem(item: T): Flow<T> = catch { emit(item) }

fun <T> Flow<T>.timeout(durationMillis: Long, fallback: suspend () -> T): Flow<T> = flow {
    coroutineScope {
        val result = withTimeoutOrNull(durationMillis) {
            toList()
        }
        if (result != null) {
            result.forEach { emit(it) }
        } else {
            emit(fallback())
        }
    }
}

fun <T, R> Flow<T>.flatMapWith(transform: suspend (T) -> Flow<R>): Flow<R> = flatMapConcat(transform)

fun <T, R> Flow<T>.switchMapWith(transform: suspend (T) -> Flow<R>): Flow<R> = flatMapLatest(transform)

fun <T> Flow<T>.share(): SharedFlow<T> = shareIn(GlobalScope, SharingStarted.Eagerly)

fun <T> Flow<T>.replay(amount: Int): SharedFlow<T> = shareIn(
    GlobalScope,
    SharingStarted.Eagerly,
    replay = amount
)

fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
    GlobalScope,
    SharingStarted.Eagerly,
    initialValue
)

fun <T> Flow<T>.count(): Flow<Int> = flow {
    var count = 0
    collect { count++ }
    emit(count)
}

fun <T : Comparable<T>> Flow<T>.min(): Flow<T?> = flow {
    var min: T? = null
    collect { value ->
        if (min == null || value < min!!) {
            min = value
        }
    }
    emit(min)
}

fun <T : Comparable<T>> Flow<T>.max(): Flow<T?> = flow {
    var max: T? = null
    collect { value ->
        if (max == null || value > max!!) {
            max = value
        }
    }
    emit(max)
}

fun <T> Flow<T>.sumBy(selector: (T) -> Int): Flow<Int> = flow {
    var sum = 0
    collect { sum += selector(it) }
    emit(sum)
}

fun <T> Flow<T>.averageBy(selector: (T) -> Double): Flow<Double> = flow {
    var sum = 0.0
    var count = 0
    collect {
        sum += selector(it)
        count++
    }
    emit(if (count > 0) sum / count else 0.0)
}

fun <T> Flow<T>.debounce(durationMillis: Long): Flow<T> = flow {
    var debounceJob: Job? = null
    var lastValue: T? = null
    
    collect { value ->
        lastValue = value
        debounceJob?.cancel()
        debounceJob = coroutineScope {
            launch {
                delay(durationMillis)
                lastValue?.let { emit(it) }
            }
        }
    }
    
    debounceJob?.join()
}

fun <T> Flow<T>.sample(periodMillis: Long): Flow<T> = flow {
    var lastSampleTime = 0L
    collect { value ->
        val now = System.currentTimeMillis()
        if (now - lastSampleTime >= periodMillis) {
            emit(value)
            lastSampleTime = now
        }
    }
}

fun <T, R> Flow<T>.zipWith(other: Flow<R>): Flow<Pair<T, R>> = flow {
    collect { first ->
        other.collect { second ->
            emit(first to second)
        }
    }
}

fun <T, R1, R2, R> Flow<T>.combineWith(
    flow1: Flow<R1>,
    flow2: Flow<R2>,
    transform: (T, R1, R2) -> R
): Flow<R> = combine(this, flow1, flow2, transform)

fun <T> Flow<T>.filterNot(predicate: (T) -> Boolean): Flow<T> = filter { !predicate(it) }

fun <T> Flow<T>.takeUntil(predicate: (T) -> Boolean): Flow<T> = flow {
    collect { value ->
        if (predicate(value)) {
            return@flow
        }
        emit(value)
    }
}

fun <T> Flow<T>.skip(count: Int): Flow<T> = flow {
    var skipped = 0
    collect { value ->
        if (skipped >= count) {
            emit(value)
        } else {
            skipped++
        }
    }
}

fun <T> Flow<T>.elementAt(index: Int): Flow<T?> = flow {
    var currentIndex = 0
    collect { value ->
        if (currentIndex == index) {
            emit(value)
            return@flow
        }
        currentIndex++
    }
    emit(null)
}

fun <T> Flow<T>.firstOrNull(): Flow<T?> = flow {
    var result: T? = null
    collect { value ->
        result = value
        return@flow
    }
    emit(result)
}

fun <T> Flow<T>.lastOrNull(): Flow<T?> = flow {
    var result: T? = null
    collect { result = it }
    emit(result)
}

fun <T> Flow<T>.contains(element: T): Flow<Boolean> = flow {
    var found = false
    collect {
        if (it == element) {
            found = true
            return@flow
        }
    }
    emit(found)
}

fun <T> Flow<T>.all(predicate: (T) -> Boolean): Flow<Boolean> = flow {
    var allMatch = true
    collect {
        if (!predicate(it)) {
            allMatch = false
            return@flow
        }
    }
    emit(allMatch)
}

fun <T> Flow<T>.any(predicate: (T) -> Boolean): Flow<Boolean> = flow {
    var anyMatch = false
    collect {
        if (predicate(it)) {
            anyMatch = true
            return@flow
        }
    }
    emit(anyMatch)
}
