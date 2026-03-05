package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.impl.CollectionFactory;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class CollectionBenchmark {

    private Collection<Integer> estCollection;
    private List<Integer> javaList;

    @Setup(Level.Iteration)
    public void setup() {
        estCollection = CollectionFactory.fromIterable(new ArrayList<>());
        javaList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            estCollection = estCollection.plus(i);
            javaList.add(i);
        }
    }

    @Benchmark
    public int estCollection_size() {
        return estCollection.size();
    }

    @Benchmark
    public int javaList_size() {
        return javaList.size();
    }

    @Benchmark
    public Collection<Integer> estCollection_map() {
        return estCollection.map(i -> i * 2);
    }

    @Benchmark
    public List<Integer> javaList_map() {
        List<Integer> result = new ArrayList<>();
        for (Integer i : javaList) {
            result.add(i * 2);
        }
        return result;
    }

    @Benchmark
    public Collection<Integer> estCollection_filter() {
        return estCollection.filter(i -> i % 2 == 0);
    }

    @Benchmark
    public List<Integer> javaList_filter() {
        List<Integer> result = new ArrayList<>();
        for (Integer i : javaList) {
            if (i % 2 == 0) {
                result.add(i);
            }
        }
        return result;
    }

    @Benchmark
    public Integer estCollection_reduce() {
        return estCollection.reduce(0, Integer::sum);
    }

    @Benchmark
    public Integer javaList_reduce() {
        int sum = 0;
        for (Integer i : javaList) {
            sum += i;
        }
        return sum;
    }
}
