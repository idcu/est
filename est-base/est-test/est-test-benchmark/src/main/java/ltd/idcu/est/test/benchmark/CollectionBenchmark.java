package ltd.idcu.est.test.benchmark;

import ltd.idcu.est.collection.api.Seq;
import ltd.idcu.est.collection.impl.Seqs;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class CollectionBenchmark {

    private Seq<Integer> estCollection;
    private List<Integer> javaList;

    @Setup(Level.Iteration)
    public void setup() {
        estCollection = Seqs.from(new ArrayList<>());
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
    public Seq<Integer> estCollection_map() {
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
    public List<Integer> javaStream_map() {
        return javaList.stream()
            .map(i -> i * 2)
            .collect(Collectors.toList());
    }

    @Benchmark
    public Seq<Integer> estCollection_filter() {
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
    public List<Integer> javaStream_filter() {
        return javaList.stream()
            .filter(i -> i % 2 == 0)
            .collect(Collectors.toList());
    }

    @Benchmark
    public Seq<Integer> estCollection_chain() {
        return estCollection
            .filter(i -> i % 2 == 0)
            .map(i -> i * 2)
            .sorted();
    }

    @Benchmark
    public List<Integer> javaList_chain() {
        List<Integer> filtered = new ArrayList<>();
        for (Integer i : javaList) {
            if (i % 2 == 0) {
                filtered.add(i * 2);
            }
        }
        filtered.sort(Integer::compareTo);
        return filtered;
    }

    @Benchmark
    public List<Integer> javaStream_chain() {
        return javaList.stream()
            .filter(i -> i % 2 == 0)
            .map(i -> i * 2)
            .sorted()
            .collect(Collectors.toList());
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

    @Benchmark
    public Integer javaStream_reduce() {
        return javaList.stream()
            .reduce(0, Integer::sum);
    }

    @Benchmark
    public Seq<Integer> estCollection_distinct() {
        return estCollection.distinct();
    }

    @Benchmark
    public Seq<Integer> estCollection_take() {
        return estCollection.take(100);
    }

    @Benchmark
    public Seq<Integer> estCollection_drop() {
        return estCollection.drop(100);
    }

    @Benchmark
    public Seq<Integer> estCollection_groupBy() {
        return estCollection.map(i -> i % 10);
    }

    @Benchmark
    public String estCollection_joinToString() {
        return estCollection.joinToString(",");
    }

    @Benchmark
    public Seq<Integer> estCollection_partition_map() {
        var partitioned = estCollection.partition(i -> i % 2 == 0);
        return partitioned.getKey().map(i -> i * 2);
    }

    @Benchmark
    public Seq<Integer> estCollection_zipWithIndex() {
        return estCollection.zipWithIndex().map(p -> p.getKey() + p.getValue());
    }
}
