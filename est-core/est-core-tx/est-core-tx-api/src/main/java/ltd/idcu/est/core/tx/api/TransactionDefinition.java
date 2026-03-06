package ltd.idcu.est.core.tx.api;

import ltd.idcu.est.core.api.annotation.Transactional;

public interface TransactionDefinition {
    Transactional.Propagation getPropagationBehavior();
    Transactional.Isolation getIsolationLevel();
    int getTimeout();
    boolean isReadOnly();
    String getName();
}
