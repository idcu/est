package ltd.idcu.est.core.tx.impl;

import ltd.idcu.est.core.tx.api.TransactionDefinition;
import ltd.idcu.est.core.tx.api.annotation.Transactional;

public class DefaultTransactionDefinition implements TransactionDefinition {
    private final Transactional.Propagation propagation;
    private final Transactional.Isolation isolation;
    private final int timeout;
    private final boolean readOnly;
    private final String name;

    public DefaultTransactionDefinition() {
        this(Transactional.Propagation.REQUIRED, Transactional.Isolation.DEFAULT, -1, false, "");
    }

    public DefaultTransactionDefinition(Transactional.Propagation propagation, Transactional.Isolation isolation, 
                                        int timeout, boolean readOnly, String name) {
        this.propagation = propagation;
        this.isolation = isolation;
        this.timeout = timeout;
        this.readOnly = readOnly;
        this.name = name;
    }

    public static DefaultTransactionDefinition from(Transactional transactional) {
        return new DefaultTransactionDefinition(
                transactional.propagation(),
                transactional.isolation(),
                transactional.timeout(),
                transactional.readOnly(),
                ""
        );
    }

    @Override
    public Transactional.Propagation getPropagationBehavior() {
        return propagation;
    }

    @Override
    public Transactional.Isolation getIsolationLevel() {
        return isolation;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public String getName() {
        return name;
    }
}
