package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Observer;

import java.util.UUID;

public abstract class AbstractObserver<T> implements Observer<T> {
    
    private final String id;
    
    protected AbstractObserver() {
        this.id = UUID.randomUUID().toString();
    }
    
    protected AbstractObserver(String id) {
        this.id = id != null ? id : UUID.randomUUID().toString();
    }
    
    @Override
    public String getId() {
        return id;
    }
}
