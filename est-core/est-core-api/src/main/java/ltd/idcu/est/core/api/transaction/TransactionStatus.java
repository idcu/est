package ltd.idcu.est.core.api.transaction;

@Deprecated
public interface TransactionStatus {
    boolean isNewTransaction();
    boolean hasSavepoint();
    void setRollbackOnly();
    boolean isRollbackOnly();
    boolean isCompleted();
}
