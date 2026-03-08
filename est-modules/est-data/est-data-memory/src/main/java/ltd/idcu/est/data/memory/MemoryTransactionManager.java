package ltd.idcu.est.data.memory;

import ltd.idcu.est.data.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryTransactionManager implements TransactionManager {
    
    private final ThreadLocal<AtomicBoolean> active;
    private final ThreadLocal<AtomicInteger> depth;
    private final ThreadLocal<Boolean> rollbackOnly;
    private final ThreadLocal<Boolean> autoCommit;
    private final ThreadLocal<Integer> isolationLevel;
    private final List<TransactionListener> listeners;
    
    public MemoryTransactionManager() {
        this.active = new ThreadLocal<>();
        this.depth = new ThreadLocal<>();
        this.rollbackOnly = new ThreadLocal<>();
        this.autoCommit = ThreadLocal.withInitial(() -> true);
        this.isolationLevel = ThreadLocal.withInitial(() -> java.sql.Connection.TRANSACTION_READ_COMMITTED);
        this.listeners = new ArrayList<>();
    }
    
    @Override
    public Transaction begin() {
        if (active.get() == null) {
            active.set(new AtomicBoolean(false));
        }
        if (depth.get() == null) {
            depth.set(new AtomicInteger(0));
        }
        
        if (active.get().compareAndSet(false, true)) {
            depth.get().set(1);
            rollbackOnly.set(false);
            notifyBegin();
        } else {
            depth.get().incrementAndGet();
        }
        
        return new MemoryTransaction(this);
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
        
        int currentDepth = depth.get().decrementAndGet();
        if (currentDepth == 0) {
            active.get().set(false);
            rollbackOnly.set(false);
            notifyCommit();
        }
    }
    
    @Override
    public void rollback() {
        if (!isActive()) {
            return;
        }
        
        int currentDepth = depth.get().decrementAndGet();
        if (currentDepth == 0) {
            active.get().set(false);
            rollbackOnly.set(false);
            notifyRollback();
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
        return level != null ? level : java.sql.Connection.TRANSACTION_READ_COMMITTED;
    }
    
    @Override
    public void setTransactionIsolation(int level) {
        isolationLevel.set(level);
    }
    
    void setRollbackOnly() {
        if (isActive()) {
            rollbackOnly.set(true);
        }
    }
    
    boolean isRollbackOnly() {
        return Boolean.TRUE.equals(rollbackOnly.get());
    }
    
    public void addListener(TransactionListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(TransactionListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyBegin() {
        for (TransactionListener listener : listeners) {
            try {
                listener.onBegin();
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyCommit() {
        for (TransactionListener listener : listeners) {
            try {
                listener.onCommit();
            } catch (Exception ignored) {
            }
        }
    }
    
    private void notifyRollback() {
        for (TransactionListener listener : listeners) {
            try {
                listener.onRollback();
            } catch (Exception ignored) {
            }
        }
    }
    
    public interface TransactionListener {
        void onBegin();
        void onCommit();
        void onRollback();
    }
    
    private static class MemoryTransaction implements Transaction {
        private final MemoryTransactionManager manager;
        private boolean closed;
        
        MemoryTransaction(MemoryTransactionManager manager) {
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
