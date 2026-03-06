package ltd.idcu.est.core.tx.api;

public interface TransactionStatus {
    boolean isNewTransaction();
    boolean hasSavepoint();
    void setRollbackOnly();
    boolean isRollbackOnly();
    boolean isCompleted();
}
