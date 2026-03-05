package ltd.idcu.est.collection.api;

import java.util.function.*;

public interface LazyCollection<T> extends Collection<T> {

    LazyCollection<T> lazy();

    Collection<T> eager();

    boolean isLazy();

    static <T> LazyCollection<T> of(T... elements) {
        return Collections.lazyOf(elements);
    }

    static <T> LazyCollection<T> from(Iterable<T> iterable) {
        return Collections.lazyFrom(iterable);
    }
}
