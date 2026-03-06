package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Seq;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Seqs {

    private Seqs() {
    }

    public static <T> Seq<T> empty() {
        return DefaultSeq.empty();
    }

    @SafeVarargs
    public static <T> Seq<T> of(T... elements) {
        return DefaultSeq.of(elements);
    }

    public static <T> Seq<T> from(Iterable<T> iterable) {
        return DefaultSeq.from(iterable);
    }

    public static <T> Seq<T> from(Stream<T> stream) {
        return DefaultSeq.from(stream);
    }

    public static Seq<Integer> range(int start, int end) {
        return range(start, end, 1);
    }

    public static Seq<Integer> range(int start, int end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step cannot be zero");
        }
        if (step > 0 && start >= end) {
            return empty();
        }
        if (step < 0 && start <= end) {
            return empty();
        }
        return from(IntStream.iterate(start, n -> n + step)
            .takeWhile(n -> step > 0 ? n < end : n > end)
            .boxed());
    }

    public static <T> Seq<T> generate(int count, Supplier<T> supplier) {
        if (count <= 0) {
            return empty();
        }
        return from(Stream.generate(supplier).limit(count));
    }

    public static <T> Seq<T> repeat(T element, int times) {
        if (times <= 0) {
            return empty();
        }
        return from(Stream.generate(() -> element).limit(times));
    }

    public static <T> Seq<T> fromList(List<T> list) {
        return from(list);
    }
}
