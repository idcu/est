package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdbcTransactionManager implements TransactionManager {
    
    private final ConnectionPool connectionPool;
    private final ThreadLocal<Connection> currentConnection;
    private final ThreadLocal<AtomicBoolean> active;
    private final ThreadLocal<Integer> depth;
    private final ThreadLocal<Boolean> rollbackOnly;
    private final ThreadLocal<Boolean> autoCommit;
    private final ThreadLocal<Integer> isolationLevel;
    
    public JdbcTransactionManager(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.currentConnection = new ThreadLocal<>();
        this.active = new ThreadLocal<>();
        this.depth = new ThreadLocal<>();
        this.rollbackOnly = new ThreadLocal<>();
        this.autoCommit = ThreadLocal.withInitial(() -> true);
        this.isolationLevel = new ThreadLocal<>();
    }
    
    @Override
    public Transaction begin() {
        if (active.get() == null) {
            active.set(new AtomicBoolean(false));
        }
        
        if (depth.get() == null) {
            depth.set(0);
        }
        
        int currentDepth = depth.get();
        
        if (currentDepth == 0) {
            try {
                Connection conn = connectionPool.getConnection();
                conn.setAutoCommit(false);
                currentConnection.set(conn);
                active.get().set(true);
                rollbackOnly.set(false);
                depth.set(1);
            } catch (SQLException e) {
                throw new DataException("Failed to begin transaction", e);
            }
        } else {
            depth.set(currentDepth + 1);
        }
        
        return new JdbcTransaction(this);
    }
    
    @Override
    public boolean isActive() {
        AtomicBoolean flag = active.get();
        return flag != null && flag.get();
    }
    
    @Override
    public void commit() {
        if (!isActive()) {
            throw new DataException("No active transaction");
        }
        
        if (Boolean.TRUE.equals(rollbackOnly.get())) {
            rollback();
            throw new DataException("Transaction marked for rollback only");
        }
        
        int currentDepth = depth.get() - 1;
        depth.set(currentDepth);
        
        if (currentDepth == 0) {
            try {
                Connection conn = currentConnection.get();
                if (conn != null) {
                    conn.commit();
                    conn.setAutoCommit(true);
                    connectionPool.releaseConnection(conn);
                }
            } catch (SQLException e) {
                throw new DataException("Failed to commit transaction", e);
            } finally {
                active.get().set(false);
                currentConnection.remove();
            }
        }
    }
    
    @Override
    public void rollback() {
        if (!isActive()) {
            return;
        }
        
        int currentDepth = depth.get() - 1;
        depth.set(currentDepth);
        
        if (currentDepth == 0) {
            try {
                Connection conn = currentConnection.get();
                if (conn != null) {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    connectionPool.releaseConnection(conn);
                }
            } catch (SQLException e) {
                throw new DataException("Failed to rollback transaction", e);
            } finally {
                active.get().set(false);
                currentConnection.remove();
                rollbackOnly.set(false);
            }
        } else {
            rollbackOnly.set(true);
        }
    }
    
    @Override
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit.set(autoCommit);
    }
    
    @Override
    public boolean isAutoCommit() {
        return Boolean.TRUE.equals(autoCommit.get());
    }
    
    @Override
    public int getTransactionIsolation() {
        Integer level = isolationLevel.get();
        return level != null ? level : Connection.TRANSACTION_READ_COMMITTED;
    }
    
    @Override
    public void setTransactionIsolation(int level) {
        isolationLevel.set(level);
    }
    
    Connection getCurrentConnection() {
        return currentConnection.get();
    }
    
    void setRollbackOnly() {
        if (isActive()) {
            rollbackOnly.set(true);
        }
    }
    
    boolean isRollbackOnly() {
        return Boolean.TRUE.equals(rollbackOnly.get());
    }
    
    private static class JdbcTransaction implements Transaction {
        private final JdbcTransactionManager manager;
        private boolean closed;
        
        JdbcTransaction(JdbcTransactionManager manager) {
            this.manager = manager;
            this.closed = false;
        }
        
        @Override
        public void commit() {
            if (closed) {
                throw new DataException("Transaction already closed");
            }
            manager.commit();
            closed = true;
        }
        
        @Override
        public void rollback() {
            if (closed) {
                return;
            }
            manager.rollback();
            closed = true;
        }
        
        @Override
        public boolean isActive() {
            return !closed && manager.isActive();
        }
        
        @Override
        public void setRollbackOnly() {
            manager.setRollbackOnly();
        }
        
        @Override
        public boolean isRollbackOnly() {
            return manager.isRollbackOnly();
        }
        
        @Override
        public void close() {
            if (!closed && manager.isActive()) {
                rollback();
            }
        }
    }
}
