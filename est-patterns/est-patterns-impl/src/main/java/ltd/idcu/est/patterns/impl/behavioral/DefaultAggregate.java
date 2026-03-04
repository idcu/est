package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Aggregate;
import ltd.idcu.est.patterns.api.behavioral.Iterator;

import java.util.ArrayList;
import java.util.List;

public class DefaultAggregate<T> implements Aggregate<T> {
    
    private final List<T> items = new ArrayList<>();
    
    public void add(T item) {
        items.add(item);
    }
    
    @Override
    public Iterator<T> createIterator() {
        return new DefaultIterator<>(this);
    }
    
    @Override
    public int size() {
        return items.size();
    }
    
    @Override
    public T get(int index) {
        return items.get(index);
    }
}
