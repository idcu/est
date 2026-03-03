package ltd.idcu.est.collection.api;

public interface Collector<T, R> {

    R collect(Iterable<? extends T> elements);
}
