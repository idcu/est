package ltd.idcu.est.core.impl.transaction;

import ltd.idcu.est.core.api.annotation.Transactional;
import ltd.idcu.est.core.api.transaction.PlatformTransactionManager;
import ltd.idcu.est.core.api.transaction.TransactionDefinition;
import ltd.idcu.est.core.api.transaction.TransactionStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceTransactionManager implements PlatformTransactionManager {
    private final DataSource dataSource;
    private final ThreadLocal<TransactionContext> transactionHolder = new ThreadLocal<>();

    public DataSourceTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) {
        TransactionContext existingContext = transactionHolder.get();
        
        if (existingContext != null && isExistingTransaction(existingContext)) {
            return handleExistingTransaction(definition, existingContext);
        }
        
        return startNewTransaction(definition);
    }

    private boolean isExistingTransaction(TransactionContext context) {
        return context != null && context.isActive();
    }

    private TransactionStatus handleExistingTransaction(TransactionDefinition definition, 
                                                         TransactionContext existingContext) {
        switch (definition.getPropagationBehavior()) {
            case REQUIRED:
            case SUPPORTS:
                return new DefaultTransactionStatus(existingContext.getConnection(), false);
            case REQUIRES_NEW:
                suspend(existingContext);
                return startNewTransaction(definition);
            case NOT_SUPPORTED:
                suspend(existingContext);
                return new DefaultTransactionStatus(null, false);
            case MANDATORY:
                return new DefaultTransactionStatus(existingContext.getConnection(), false);
            case NEVER:
                throw new IllegalStateException("Existing transaction found for transaction marked with propagation 'never'");
            case NESTED:
                return new DefaultTransactionStatus(existingContext.getConnection(), false);
            default:
                throw new UnsupportedOperationException("Unknown propagation behavior: " + definition.getPropagationBehavior());
        }
    }

    private TransactionStatus startNewTransaction(TransactionDefinition definition) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            
            if (definition.getIsolationLevel() != Transactional.Isolation.DEFAULT) {
                connection.setTransactionIsolation(convertToJdbcIsolationLevel(definition.getIsolationLevel()));
            }
            
            if (definition.isReadOnly()) {
                connection.setReadOnly(true);
            }

            TransactionContext context = new TransactionContext(connection);
            transactionHolder.set(context);
            
            return new DefaultTransactionStatus(connection, true);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start transaction", e);
        }
    }

    private int convertToJdbcIsolationLevel(Transactional.Isolation isolation) {
        switch (isolation) {
            case READ_UNCOMMITTED:
                return Connection.TRANSACTION_READ_UNCOMMITTED;
            case READ_COMMITTED:
                return Connection.TRANSACTION_READ_COMMITTED;
            case REPEATABLE_READ:
                return Connection.TRANSACTION_REPEATABLE_READ;
            case SERIALIZABLE:
                return Connection.TRANSACTION_SERIALIZABLE;
            default:
                throw new UnsupportedOperationException("Unknown isolation level: " + isolation);
        }
    }

    private void suspend(TransactionContext context) {
    }

    @Override
    public void commit(TransactionStatus status) {
        DefaultTransactionStatus txStatus = (DefaultTransactionStatus) status;
        
        if (txStatus.isCompleted()) {
            throw new IllegalStateException("Transaction is already completed");
        }
        
        if (txStatus.isRollbackOnly()) {
            rollback(status);
            return;
        }
        
        if (txStatus.isNewTransaction()) {
            try {
                Connection connection = (Connection) txStatus.getTransaction();
                connection.commit();
                connection.close();
                clearTransaction();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to commit transaction", e);
            }
        }
        
        txStatus.setCompleted();
    }

    @Override
    public void rollback(TransactionStatus status) {
        DefaultTransactionStatus txStatus = (DefaultTransactionStatus) status;
        
        if (txStatus.isCompleted()) {
            throw new IllegalStateException("Transaction is already completed");
        }
        
        if (txStatus.isNewTransaction()) {
            try {
                Connection connection = (Connection) txStatus.getTransaction();
                connection.rollback();
                connection.close();
                clearTransaction();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to rollback transaction", e);
            }
        }
        
        txStatus.setCompleted();
    }

    private void clearTransaction() {
        transactionHolder.remove();
    }

    private static class TransactionContext {
        private final Connection connection;
        private boolean active = true;

        TransactionContext(Connection connection) {
            this.connection = connection;
        }

        Connection getConnection() {
            return connection;
        }

        boolean isActive() {
            return active;
        }
    }
}
