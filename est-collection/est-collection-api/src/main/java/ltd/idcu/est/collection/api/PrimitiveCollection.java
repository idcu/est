package ltd.idcu.est.collection.api;

public interface PrimitiveCollection<T> extends Iterable<T> {

    int size();

    boolean isEmpty();

    boolean isNotEmpty();

    boolean contains(Object element);
}
