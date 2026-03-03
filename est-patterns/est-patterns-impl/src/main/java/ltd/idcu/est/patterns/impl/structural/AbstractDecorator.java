package ltd.idcu.est.patterns.impl.structural;

import ltd.idcu.est.patterns.api.structural.Decorator;

public abstract class AbstractDecorator<T> implements Decorator<T> {
    
    protected T decorated;
    
    protected AbstractDecorator() {
    }
    
    protected AbstractDecorator(T decorated) {
        this.decorated = decorated;
    }
    
    @Override
    public T getDecorated() {
        return decorated;
    }
    
    @Override
    public void setDecorated(T decorated) {
        this.decorated = decorated;
    }
}
