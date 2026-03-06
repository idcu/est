package ltd.idcu.est.core.tx.impl;

import ltd.idcu.est.core.tx.api.TransactionStatus;

public class DefaultTransactionStatus implements TransactionStatus {
    private final boolean newTransaction;
    private boolean rollbackOnly = false;
    private boolean completed = false;
    private Object transaction;

    public DefaultTransactionStatus(Object transaction, boolean newTransaction) {
        this.transaction = transaction;
        this.newTransaction = newTransaction;
    }

    public Object getTransaction() {
        return transaction;
    }

    @Override
    public boolean isNewTransaction() {
        return newTransaction;
    }

    @Override
    public boolean hasSavepoint() {
        return false;
    }

    @Override
    public void setRollbackOnly() {
        this.rollbackOnly = true;
    }

    @Override
    public boolean isRollbackOnly() {
        return rollbackOnly;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted() {
        this.completed = true;
    }
}
