package ltd.idcu.est.features.data.api;

public interface TransactionManager {
    
    Transaction begin();
    
    boolean isActive();
    
    void commit();
    
    void rollback();
    
    void setAutoCommit(boolean autoCommit);
    
    boolean isAutoCommit();
    
    int getTransactionIsolation();
    
    void setTransactionIsolation(int level);
}
