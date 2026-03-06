package ltd.idcu.est.core.api.transaction;

import ltd.idcu.est.core.api.annotation.Transactional;

@Deprecated
public interface TransactionDefinition {
    Transactional.Propagation getPropagationBehavior();
    Transactional.Isolation getIsolationLevel();
    int getTimeout();
    boolean isReadOnly();
    String getName();
}
