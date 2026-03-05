package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Handler;

public abstract class AbstractHandler<T> implements Handler<T> {
    
    protected Handler<T> next;
    
    @Override
    public void setNext(Handler<T> next) {
        this.next = next;
    }
    
    @Override
    public void handle(T request) {
        if (canHandle(request)) {
            doHandle(request);
        } else if (next != null) {
            next.handle(request);
        }
    }
    
    protected abstract boolean canHandle(T request);
    
    protected abstract void doHandle(T request);
}
