package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.core.api.Container;
import ltd.idcu.est.core.impl.DefaultContainer;
import ltd.idcu.est.cache.api.Cache;
import ltd.idcu.est.cache.memory.MemoryCache;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.OPS_PER_SECOND)
@Warmup(iterations = 5, time = 2)
@Measurement(iterations = 10, time = 2)
@Fork(1)
@State(Scope.Benchmark)
public class PerformanceBenchmark {

    @State(Scope.Benchmark)
    public static class ContainerState {
        Container container;
        List<Class<?>> serviceClasses;
        Random random;

        @Setup(Level.Iteration)
        public void setup() {
            container = new DefaultContainer();
            serviceClasses = new ArrayList<>();
            random = new Random(42);

            for (int i = 0; i < 50; i++) {
                final int index = i;
                Class<?> serviceClass = createServiceClass("Service" + i);
                serviceClasses.add(serviceClass);
                container.register(serviceClass, serviceClass);
            }
        }

        private Class<?> createServiceClass(String name) {
            return new Object() {}.getClass();
        }
    }

    @State(Scope.Benchmark)
    public static class CacheState {
        Cache<Integer, String> cache;
        List<Integer> keys;
        Random random;

        @Setup(Level.Iteration)
        public void setup() {
            cache = new MemoryCache<>();
            keys = new ArrayList<>();
            random = new Random(42);

            for (int i = 0; i < 10000; i++) {
                cache.put(i, "value" + i);
                keys.add(i);
            }
        }
    }

    @State(Scope.Benchmark)
    public static class CollectionState {
        List<Integer> arrayList;
        List<Integer> linkedList;
        Set<Integer> hashSet;
        Map<Integer, String> hashMap;
        Map<Integer, String> concurrentHashMap;
        Random random;

        @Setup(Level.Iteration)
        public void setup() {
            random = new Random(42);

            arrayList = new ArrayList<>();
            linkedList = new LinkedList<>();
            hashSet = new HashSet<>();
            hashMap = new HashMap<>();
            concurrentHashMap = new java.util.concurrent.ConcurrentHashMap<>();

            for (int i = 0; i < 10000; i++) {
                arrayList.add(i);
                linkedList.add(i);
                hashSet.add(i);
                hashMap.put(i, "value" + i);
                concurrentHashMap.put(i, "value" + i);
            }
        }
    }

    @Benchmark
    public Object container_get_random(ContainerState state) {
        int index = state.random.nextInt(state.serviceClasses.size());
        return state.container.get(state.serviceClasses.get(index));
    }

    @Benchmark
    public Object container_register_and_get(ContainerState state) {
        Class<?> tempClass = new Object() {}.getClass();
        state.container.register(tempClass, tempClass);
        return state.container.get(tempClass);
    }

    @Benchmark
    public String cache_get_random(CacheState state) {
        int key = state.keys.get(state.random.nextInt(state.keys.size()));
        return state.cache.get(key).orElse(null);
    }

    @Benchmark
    public void cache_put_and_get(CacheState state) {
        int key = state.random.nextInt(20000);
        state.cache.put(key, "newValue" + key);
        state.cache.get(key);
    }

    @Benchmark
    public int arrayList_iteration(CollectionState state) {
        int sum = 0;
        for (int i : state.arrayList) {
            sum += i;
        }
        return sum;
    }

    @Benchmark
    public int arrayList_stream_sum(CollectionState state) {
        return state.arrayList.stream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public int hashSet_contains(CollectionState state) {
        int count = 0;
        for (int i = 0; i < 1000; i++) {
            if (state.hashSet.contains(state.random.nextInt(20000))) {
                count++;
            }
        }
        return count;
    }

    @Benchmark
    public String hashMap_get(CollectionState state) {
        return state.hashMap.get(state.random.nextInt(10000));
    }

    @Benchmark
    public String concurrentHashMap_get(CollectionState state) {
        return state.concurrentHashMap.get(state.random.nextInt(10000));
    }

    @Benchmark
    public int int_stream_operations() {
        return IntStream.range(0, 10000)
                .filter(i -> i % 2 == 0)
                .map(i -> i * 2)
                .sum();
    }
}
