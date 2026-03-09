package ltd.idcu.est.data.api;

import java.util.Optional;

public interface Repository<T, ID> {

    T save(T entity);

    Iterable<T> saveAll(Iterable<T> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAllById(Iterable<ID> ids);

    void deleteAll(Iterable<T> entities);

    void deleteAll();
}
