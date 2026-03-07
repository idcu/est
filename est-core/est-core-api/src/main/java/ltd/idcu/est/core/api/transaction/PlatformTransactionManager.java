package ltd.idcu.est.core.api.transaction;

import ltd.idcu.est.core.api.annotation.Transactional;

public interface PlatformTransactionManager {
    TransactionStatus getTransaction(TransactionDefinition definition);
    void commit(TransactionStatus status);
    void rollback(TransactionStatus status);
}
