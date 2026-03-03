package ltd.idcu.est.features.data.api;

public interface Transaction extends AutoCloseable {
    
    void commit();
    
    void rollback();
    
    boolean isActive();
    
    void setRollbackOnly();
    
    boolean isRollbackOnly();
    
    @Override
    void close();
}
