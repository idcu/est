package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.api.Collections;
import ltd.idcu.est.utils.format.json.JsonUtils;

import java.util.function.Supplier;
import java.util.stream.Stream;

final class DefaultCollectionProvider implements Collections.CollectionProvider {

    @Override
    public <T> Collection<T> fromIterable(Iterable<T> iterable) {
        return CollectionFactory.fromIterable(iterable);
    }

    @Override
    public <T> Collection<T> fromStream(Stream<T> stream) {
        return CollectionFactory.fromStream(stream);
    }

    @Override
    public <T> Collection<T> singleton(T element) {
        return CollectionFactory.singleton(element);
    }

    @Override
    public Collection<String> fromJson(String json) {
        return CollectionFactory.fromJson(json);
    }

    @Override
    public Collection<Object> fromYaml(String yaml) {
        return CollectionFactory.fromYaml(yaml);
    }

    @Override
    public Collection<Object> fromXml(String xml) {
        return CollectionFactory.fromXml(xml);
    }

    @Override
    public Collection<Integer> range(int start, int end, int step) {
        return CollectionFactory.range(start, end, step);
    }

    @Override
    public Collection<Long> range(long start, long end, long step) {
        return CollectionFactory.range(start, end, step);
    }

    @Override
    public <T> Collection<T> generate(int count, Supplier<T> supplier) {
        return CollectionFactory.generate(count, supplier);
    }

    @Override
    public <T> Collection<T> repeat(T element, int times) {
        return CollectionFactory.repeat(element, times);
    }
}
