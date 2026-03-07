package ltd.idcu.est.core.api.transaction;

public interface TransactionStatus {
    boolean isNewTransaction();
    boolean hasSavepoint();
    void setRollbackOnly();
    boolean isRollbackOnly();
    boolean isCompleted();
}
