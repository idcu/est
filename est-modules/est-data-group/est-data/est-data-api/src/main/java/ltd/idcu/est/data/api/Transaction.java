package ltd.idcu.est.data.api;

public interface Transaction extends AutoCloseable {
    void commit();
    void rollback();
    boolean isActive();
    void setRollbackOnly();
    boolean isRollbackOnly();
    void close();
}
