package ltd.idcu.est.patterns.impl.behavioral;

import ltd.idcu.est.patterns.api.behavioral.Observer;
import ltd.idcu.est.patterns.api.behavioral.Subject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultSubject<T> implements Subject<T> {
    
    private final ConcurrentHashMap<String, Observer<T>> observers = new ConcurrentHashMap<>();
    
    @Override
    public void attach(Observer<T> observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null");
        }
        String id = observer.getId() != null ? observer.getId() : UUID.randomUUID().toString();
        observers.put(id, observer);
    }
    
    @Override
    public void detach(Observer<T> observer) {
        if (observer != null && observer.getId() != null) {
            observers.remove(observer.getId());
        }
    }
    
    @Override
    public void notifyObservers(T data) {
        observers.values().forEach(observer -> observer.update(data));
    }
    
    @Override
    public int getObserverCount() {
        return observers.size();
    }
}
