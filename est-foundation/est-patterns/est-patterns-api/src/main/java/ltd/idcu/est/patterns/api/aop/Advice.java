package ltd.idcu.est.patterns.api.aop;

public interface Advice {
    
    int getOrder();
    
    default int getPriority() {
        return getOrder();
    }
}
