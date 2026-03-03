package ltd.idcu.est.patterns.api.behavioral;

public interface Subject<T> {
    
    void attach(Observer<T> observer);
    
    void detach(Observer<T> observer);
    
    void notifyObservers(T data);
    
    int getObserverCount();
}
