package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class CacheBenchmark {

    private Cache<String, String> cache;

    @Setup(Level.Iteration)
    public void setup() {
        cache = new MemoryCache<>();
        for (int i = 0; i < 100; i++) {
            cache.put("key" + i, "value" + i);
        }
    }

    @Benchmark
    public String cache_get() {
        return cache.get("key50").orElse(null);
    }

    @Benchmark
    public void cache_put() {
        cache.put("newKey", "newValue");
    }

    @Benchmark
    public boolean cache_contains() {
        return cache.containsKey("key50");
    }
}
