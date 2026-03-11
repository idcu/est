package ltd.idcu.est.data.jdbc;

import ltd.idcu.est.data.api.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultConnectionPool implements ConnectionPool {
    
    private final DataConfig config;
    private final BlockingQueue<PooledConnection> idleConnections;
    private final Set<PooledConnection> activeConnections;
    private final AtomicBoolean closed;
    private final AtomicInteger totalConnections;
    private final AtomicLong totalBorrowed;
    private final AtomicLong totalReturned;
    private final AtomicLong totalCreated;
    private final AtomicLong totalDestroyed;
    private final AtomicLong totalWaitTime;
    private final ScheduledExecutorService validationExecutor;
    
    public DefaultConnectionPool(DataConfig config) {
        this.config = config;
        this.idleConnections = new LinkedBlockingQueue<>();
        this.activeConnections = ConcurrentHashMap.newKeySet();
        this.closed = new AtomicBoolean(false);
        this.totalConnections = new AtomicInteger(0);
        this.totalBorrowed = new AtomicLong(0);
        this.totalReturned = new AtomicLong(0);
        this.totalCreated = new AtomicLong(0);
        this.totalDestroyed = new AtomicLong(0);
        this.totalWaitTime = new AtomicLong(0);
        
        initializeDriver();
        initializePool();
        
        if (config.isTestWhileIdle()) {
            this.validationExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "connection-pool-validator");
                t.setDaemon(true);
                return t;
            });
            this.validationExecutor.scheduleAtFixedRate(
                    this::validateIdleConnections,
                    config.getValidationInterval(),
                    config.getValidationInterval(),
                    TimeUnit.MILLISECONDS
            );
        } else {
            this.validationExecutor = null;
        }
    }
    
    private void initializeDriver() {
        try {
            String driverClass = config.getDriverClassName();
            if (driverClass != null && !driverClass.isEmpty()) {
                Class.forName(driverClass);
            }
        } catch (ClassNotFoundException e) {
            throw new DataException("Driver not found: " + config.getDriverClassName(), e);
        }
    }
    
    private void initializePool() {
        for (int i = 0; i < config.getMinPoolSize(); i++) {
            try {
                PooledConnection conn = createConnection();
                idleConnections.offer(conn);
            } catch (SQLException e) {
                throw new DataException("Failed to initialize connection pool", e);
            }
        }
    }
    
    @Override
    public Connection getConnection() {
        return getConnection(config.getConnectionTimeout());
    }
    
    @Override
    public Connection getConnection(long timeoutMillis) {
        if (closed.get()) {
            throw new DataException("Connection pool is closed");
        }
        
        long startTime = System.currentTimeMillis();
        
        while (true) {
            PooledConnection conn = idleConnections.poll();
            
            if (conn != null) {
                if (isConnectionValid(conn)) {
                    activeConnections.add(conn);
                    totalBorrowed.incrementAndGet();
                    totalWaitTime.addAndGet(System.currentTimeMillis() - startTime);
                    return wrapConnection(conn);
                } else {
                    destroyConnection(conn);
                }
            }
            
            if (totalConnections.get() < config.getMaxPoolSize()) {
                try {
                    conn = createConnection();
                    activeConnections.add(conn);
                    totalBorrowed.incrementAndGet();
                    totalWaitTime.addAndGet(System.currentTimeMillis() - startTime);
                    return wrapConnection(conn);
                } catch (SQLException e) {
                    throw new DataException("Failed to create connection", e);
                }
            }
            
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed >= timeoutMillis) {
                throw new DataException("Connection pool timeout");
            }
            
            try {
                Thread.sleep(Math.min(100, timeoutMillis - elapsed));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DataException("Interrupted while waiting for connection", e);
            }
        }
    }
    
    private Connection wrapConnection(PooledConnection conn) {
        return new ConnectionWrapper(conn, this);
    }
    
    void releaseConnection(PooledConnection conn) {
        if (closed.get()) {
            destroyConnection(conn);
            return;
        }
        
        activeConnections.remove(conn);
        totalReturned.incrementAndGet();
        
        if (isConnectionValid(conn)) {
            idleConnections.offer(conn);
        } else {
            destroyConnection(conn);
        }
    }
    
    @Override
    public void releaseConnection(Connection connection) {
        if (connection instanceof ConnectionWrapper wrapper) {
            releaseConnection(wrapper.getWrappedConnection());
        }
    }
    
    private PooledConnection createConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
        
        conn.setAutoCommit(config.isAutoCommit());
        conn.setTransactionIsolation(config.getTransactionIsolation());
        
        totalConnections.incrementAndGet();
        totalCreated.incrementAndGet();
        
        return new PooledConnection(conn, System.currentTimeMillis());
    }
    
    private void destroyConnection(PooledConnection conn) {
        try {
            conn.getPhysicalConnection().close();
        } catch (SQLException ignored) {
        }
        totalConnections.decrementAndGet();
        totalDestroyed.incrementAndGet();
    }
    
    private boolean isConnectionValid(PooledConnection conn) {
        try {
            if (conn.getPhysicalConnection().isClosed()) {
                return false;
            }
            if (config.isTestOnBorrow()) {
                return conn.getPhysicalConnection().isValid(1);
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    private void validateIdleConnections() {
        List<PooledConnection> toRemove = new ArrayList<>();
        for (PooledConnection conn : idleConnections) {
            if (!isConnectionValid(conn)) {
                toRemove.add(conn);
            }
        }
        for (PooledConnection conn : toRemove) {
            idleConnections.remove(conn);
            destroyConnection(conn);
        }
    }
    
    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            if (validationExecutor != null) {
                validationExecutor.shutdown();
            }
            
            for (PooledConnection conn : activeConnections) {
                destroyConnection(conn);
            }
            activeConnections.clear();
            
            for (PooledConnection conn : idleConnections) {
                destroyConnection(conn);
            }
            idleConnections.clear();
        }
    }
    
    @Override
    public int getActiveConnections() {
        return activeConnections.size();
    }
    
    @Override
    public int getIdleConnections() {
        return idleConnections.size();
    }
    
    @Override
    public int getMinPoolSize() {
        return config.getMinPoolSize();
    }
    
    @Override
    public int getMaxPoolSize() {
        return config.getMaxPoolSize();
    }
    
    @Override
    public long getConnectionTimeout() {
        return config.getConnectionTimeout();
    }
    
    @Override
    public long getMaxLifetime() {
        return config.getMaxLifetime();
    }
    
    @Override
    public long getMaxIdleTime() {
        return config.getMaxIdleTime();
    }
    
    @Override
    public boolean isTestWhileIdle() {
        return config.isTestWhileIdle();
    }
    
    @Override
    public long getValidationInterval() {
        return config.getValidationInterval();
    }
    
    @Override
    public String getValidationQuery() {
        return config.getValidationQuery();
    }
    
    @Override
    public ConnectionPoolStats getStats() {
        ConnectionPoolStats stats = new ConnectionPoolStats();
        stats.setTotalConnections(totalConnections.get());
        stats.setActiveConnections(activeConnections.size());
        stats.setIdleConnections(idleConnections.size());
        stats.setTotalBorrowed(totalBorrowed.get());
        stats.setTotalReturned(totalReturned.get());
        stats.setTotalCreated(totalCreated.get());
        stats.setTotalDestroyed(totalDestroyed.get());
        stats.setTotalWaitTime(totalWaitTime.get());
        stats.setBorrowCount(totalBorrowed.get());
        return stats;
    }
    
    static class PooledConnection {
        private final Connection physicalConnection;
        private final long createdAt;
        private volatile long lastUsedAt;
        
        PooledConnection(Connection physicalConnection, long createdAt) {
            this.physicalConnection = physicalConnection;
            this.createdAt = createdAt;
            this.lastUsedAt = createdAt;
        }
        
        Connection getPhysicalConnection() {
            return physicalConnection;
        }
        
        long getCreatedAt() {
            return createdAt;
        }
        
        long getLastUsedAt() {
            return lastUsedAt;
        }
        
        void updateLastUsedAt() {
            this.lastUsedAt = System.currentTimeMillis();
        }
    }
    
    static class ConnectionWrapper extends ConnectionWrapperBase {
        private final PooledConnection pooledConnection;
        private final DefaultConnectionPool pool;
        private boolean closed;
        
        ConnectionWrapper(PooledConnection pooledConnection, DefaultConnectionPool pool) {
            super(pooledConnection.getPhysicalConnection());
            this.pooledConnection = pooledConnection;
            this.pool = pool;
            this.closed = false;
        }
        
        PooledConnection getWrappedConnection() {
            return pooledConnection;
        }
        
        @Override
        public void close() throws SQLException {
            if (!closed) {
                closed = true;
                pooledConnection.updateLastUsedAt();
                pool.releaseConnection(pooledConnection);
            }
        }
        
        @Override
        public boolean isClosed() throws SQLException {
            return closed || super.isClosed();
        }
    }
    
    static abstract class ConnectionWrapperBase extends ConnectionDelegate {
        private final Connection delegate;
        
        ConnectionWrapperBase(Connection delegate) {
            super(delegate);
            this.delegate = delegate;
        }
        
        @Override
        protected Connection getDelegate() {
            return delegate;
        }
    }
    
    static abstract class ConnectionDelegate implements Connection {
        private final Connection delegate;
        
        ConnectionDelegate(Connection delegate) {
            this.delegate = delegate;
        }
        
        protected abstract Connection getDelegate();
        
        @Override
        public Statement createStatement() throws SQLException {
            return getDelegate().createStatement();
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return getDelegate().prepareStatement(sql);
        }
        
        @Override
        public CallableStatement prepareCall(String sql) throws SQLException {
            return getDelegate().prepareCall(sql);
        }
        
        @Override
        public String nativeSQL(String sql) throws SQLException {
            return getDelegate().nativeSQL(sql);
        }
        
        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            getDelegate().setAutoCommit(autoCommit);
        }
        
        @Override
        public boolean getAutoCommit() throws SQLException {
            return getDelegate().getAutoCommit();
        }
        
        @Override
        public void commit() throws SQLException {
            getDelegate().commit();
        }
        
        @Override
        public void rollback() throws SQLException {
            getDelegate().rollback();
        }
        
        @Override
        public boolean isClosed() throws SQLException {
            return getDelegate().isClosed();
        }
        
        @Override
        public DatabaseMetaData getMetaData() throws SQLException {
            return getDelegate().getMetaData();
        }
        
        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {
            getDelegate().setReadOnly(readOnly);
        }
        
        @Override
        public boolean isReadOnly() throws SQLException {
            return getDelegate().isReadOnly();
        }
        
        @Override
        public void setCatalog(String catalog) throws SQLException {
            getDelegate().setCatalog(catalog);
        }
        
        @Override
        public String getCatalog() throws SQLException {
            return getDelegate().getCatalog();
        }
        
        @Override
        public void setTransactionIsolation(int level) throws SQLException {
            getDelegate().setTransactionIsolation(level);
        }
        
        @Override
        public int getTransactionIsolation() throws SQLException {
            return getDelegate().getTransactionIsolation();
        }
        
        @Override
        public SQLWarning getWarnings() throws SQLException {
            return getDelegate().getWarnings();
        }
        
        @Override
        public void clearWarnings() throws SQLException {
            getDelegate().clearWarnings();
        }
        
        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
            return getDelegate().createStatement(resultSetType, resultSetConcurrency);
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency);
        }
        
        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
            return getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency);
        }
        
        @Override
        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return getDelegate().getTypeMap();
        }
        
        @Override
        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            getDelegate().setTypeMap(map);
        }
        
        @Override
        public void setHoldability(int holdability) throws SQLException {
            getDelegate().setHoldability(holdability);
        }
        
        @Override
        public int getHoldability() throws SQLException {
            return getDelegate().getHoldability();
        }
        
        @Override
        public Savepoint setSavepoint() throws SQLException {
            return getDelegate().setSavepoint();
        }
        
        @Override
        public Savepoint setSavepoint(String name) throws SQLException {
            return getDelegate().setSavepoint(name);
        }
        
        @Override
        public void rollback(Savepoint savepoint) throws SQLException {
            getDelegate().rollback(savepoint);
        }
        
        @Override
        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            getDelegate().releaseSavepoint(savepoint);
        }
        
        @Override
        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return getDelegate().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return getDelegate().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        
        @Override
        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            return getDelegate().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
            return getDelegate().prepareStatement(sql, autoGeneratedKeys);
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
            return getDelegate().prepareStatement(sql, columnIndexes);
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
            return getDelegate().prepareStatement(sql, columnNames);
        }
        
        @Override
        public Clob createClob() throws SQLException {
            return getDelegate().createClob();
        }
        
        @Override
        public Blob createBlob() throws SQLException {
            return getDelegate().createBlob();
        }
        
        @Override
        public NClob createNClob() throws SQLException {
            return getDelegate().createNClob();
        }
        
        @Override
        public SQLXML createSQLXML() throws SQLException {
            return getDelegate().createSQLXML();
        }
        
        @Override
        public boolean isValid(int timeout) throws SQLException {
            return getDelegate().isValid(timeout);
        }
        
        @Override
        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            getDelegate().setClientInfo(name, value);
        }
        
        @Override
        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            getDelegate().setClientInfo(properties);
        }
        
        @Override
        public String getClientInfo(String name) throws SQLException {
            return getDelegate().getClientInfo(name);
        }
        
        @Override
        public Properties getClientInfo() throws SQLException {
            return getDelegate().getClientInfo();
        }
        
        @Override
        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            return getDelegate().createArrayOf(typeName, elements);
        }
        
        @Override
        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            return getDelegate().createStruct(typeName, attributes);
        }
        
        @Override
        public void setSchema(String schema) throws SQLException {
            getDelegate().setSchema(schema);
        }
        
        @Override
        public String getSchema() throws SQLException {
            return getDelegate().getSchema();
        }
        
        @Override
        public void abort(Executor executor) throws SQLException {
            getDelegate().abort(executor);
        }
        
        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            getDelegate().setNetworkTimeout(executor, milliseconds);
        }
        
        @Override
        public int getNetworkTimeout() throws SQLException {
            return getDelegate().getNetworkTimeout();
        }
        
        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return getDelegate().unwrap(iface);
        }
        
        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return getDelegate().isWrapperFor(iface);
        }
    }
}
