package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Aggregate;
import ltd.idcu.est.patterns.api.behavioral.Iterator;

public class DefaultIterator<T> implements Iterator<T> {
    
    private final Aggregate<T> aggregate;
    private int index = 0;
    
    public DefaultIterator(Aggregate<T> aggregate) {
        this.aggregate = aggregate;
    }
    
    @Override
    public boolean hasNext() {
        return index < aggregate.size();
    }
    
    @Override
    public T next() {
        if (hasNext()) {
            return aggregate.get(index++);
        }
        return null;
    }
    
    @Override
    public void reset() {
        index = 0;
    }
}
