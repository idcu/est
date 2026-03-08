package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.api.CacheConfig;
import ltd.idcu.est.cache.memory.MemoryCache;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 5, time = 2)
@Fork(1)
@State(Scope.Benchmark)
public class CacheStrategyBenchmark {

    private static final int CACHE_SIZE = 1000;
    private static final int KEY_SPACE = 10000;
    
    private Cache<String, String> lruCache;
    private Cache<String, String> tinyLfuCache;
    private Random random;
    private String[] keys;

    @Setup(Level.Iteration)
    public void setup() {
        random = new Random(42);
        keys = new String[KEY_SPACE];
        for (int i = 0; i < KEY_SPACE; i++) {
            keys[i] = "key" + i;
        }
        
        CacheConfig lruConfig = CacheConfig.of(CACHE_SIZE).setEvictionPolicy("LRU");
        lruCache = new MemoryCache<>(lruConfig);
        
        CacheConfig tinyLfuConfig = CacheConfig.of(CACHE_SIZE).setEvictionPolicy("TINYLFU");
        tinyLfuCache = new MemoryCache<>(tinyLfuConfig);
        
        for (int i = 0; i < CACHE_SIZE; i++) {
            lruCache.put("key" + i, "value" + i);
            tinyLfuCache.put("key" + i, "value" + i);
        }
    }

    @Benchmark
    public String lru_get_random() {
        int index = random.nextInt(KEY_SPACE);
        return lruCache.get(keys[index]).orElse(null);
    }

    @Benchmark
    public String tinyLfu_get_random() {
        int index = random.nextInt(KEY_SPACE);
        return tinyLfuCache.get(keys[index]).orElse(null);
    }

    @Benchmark
    public void lru_put() {
        int index = random.nextInt(KEY_SPACE);
        lruCache.put(keys[index], "value" + index);
    }

    @Benchmark
    public void tinyLfu_put() {
        int index = random.nextInt(KEY_SPACE);
        tinyLfuCache.put(keys[index], "value" + index);
    }
}
