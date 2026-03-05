package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.collection.api.Collections;
import ltd.idcu.est.collection.api.IntCollection;
import ltd.idcu.est.collection.api.LazyCollection;
import ltd.idcu.est.collection.api.MutableCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public <T> LazyCollection<T> lazyFromIterable(Iterable<T> iterable) {
        return new DefaultLazyCollection<>(iterable);
    }

    @Override
    public Collection<Object> fromJson(String json) {
        return CollectionFactory.fromJson(json);
    }

    @Override
    public <T> Collection<T> fromJson(String json, Class<T> elementType) {
        return CollectionFactory.fromJson(json, elementType);
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

    @Override
    public <T> MutableCollection<T> mutableFromIterable(Iterable<T> iterable) {
        if (iterable == null) {
            return new DefaultCollection<>(true);
        }
        if (iterable instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<T> collection = (Collection<T>) iterable;
            return collection.mutable();
        }
        return new DefaultCollection<>(iterable, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> MutableCollection<T> mutableOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return new DefaultCollection<>(true);
        }
        return new DefaultCollection<>(Arrays.asList(elements), true);
    }

    @Override
    public <T> Collection<T> immutableFromIterable(Iterable<T> iterable) {
        if (iterable == null) {
            return new DefaultCollection<>(false);
        }
        if (iterable instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<T> collection = (Collection<T>) iterable;
            return collection.immutable();
        }
        return new DefaultCollection<>(iterable, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Collection<T> immutableOf(T... elements) {
        if (elements == null || elements.length == 0) {
            return Collection.empty();
        }
        return new DefaultCollection<>(Arrays.asList(elements), false);
    }

    @Override
    public IntCollection intOf(int... elements) {
        if (elements == null || elements.length == 0) {
            return new DefaultIntCollection();
        }
        return new DefaultIntCollection(elements);
    }

    @Override
    public IntCollection intFromArray(int[] array) {
        if (array == null || array.length == 0) {
            return new DefaultIntCollection();
        }
        return new DefaultIntCollection(array);
    }

    @Override
    public IntCollection intRange(int start, int end, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("Step cannot be zero");
        }
        List<Integer> list = new ArrayList<>();
        if (step > 0) {
            for (int i = start; i < end; i += step) {
                list.add(i);
            }
        } else {
            for (int i = start; i > end; i += step) {
                list.add(i);
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return new DefaultIntCollection(result);
    }
}
