package ltd.idcu.est.core.tx.impl;

import ltd.idcu.est.core.tx.api.PlatformTransactionManager;
import ltd.idcu.est.core.tx.api.TransactionDefinition;
import ltd.idcu.est.core.tx.api.TransactionStatus;
import ltd.idcu.est.core.tx.api.annotation.Transactional;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class TransactionTest {

    static class MockDataSource implements DataSource {
        Connection connection;
        boolean committed = false;
        boolean rolledBack = false;
        boolean autoCommit = true;

        public MockDataSource() {
            this.connection = new MockConnection(this);
        }

        @Override
        public Connection getConnection() throws SQLException {
            return connection;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return connection;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException { return null; }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {}

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {}

        @Override
        public int getLoginTimeout() throws SQLException { return 0; }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException { return null; }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException { return null; }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException { return false; }
    }

    static class MockConnection implements Connection {
        private final MockDataSource dataSource;

        public MockConnection(MockDataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void setAutoCommit(boolean autoCommit) throws SQLException {
            dataSource.autoCommit = autoCommit;
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return dataSource.autoCommit;
        }

        @Override
        public void commit() throws SQLException {
            dataSource.committed = true;
        }

        @Override
        public void rollback() throws SQLException {
            dataSource.rolledBack = true;
        }

        @Override
        public void close() throws SQLException {}

        @Override
        public boolean isClosed() throws SQLException { return false; }

        @Override
        public java.sql.DatabaseMetaData getMetaData() throws SQLException { return null; }

        @Override
        public void setReadOnly(boolean readOnly) throws SQLException {}

        @Override
        public boolean isReadOnly() throws SQLException { return false; }

        @Override
        public void setCatalog(String catalog) throws SQLException {}

        @Override
        public String getCatalog() throws SQLException { return null; }

        @Override
        public void setTransactionIsolation(int level) throws SQLException {}

        @Override
        public int getTransactionIsolation() throws SQLException { return 0; }

        @Override
        public java.sql.SQLWarning getWarnings() throws SQLException { return null; }

        @Override
        public void clearWarnings() throws SQLException {}

        @Override
        public java.sql.Statement createStatement() throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql) throws SQLException { return null; }

        @Override
        public java.sql.CallableStatement prepareCall(String sql) throws SQLException { return null; }

        @Override
        public String nativeSQL(String sql) throws SQLException { return null; }

        @Override
        public void setHoldability(int holdability) throws SQLException {}

        @Override
        public int getHoldability() throws SQLException { return 0; }

        @Override
        public java.sql.Savepoint setSavepoint() throws SQLException { return null; }

        @Override
        public java.sql.Savepoint setSavepoint(String name) throws SQLException { return null; }

        @Override
        public void rollback(java.sql.Savepoint savepoint) throws SQLException {}

        @Override
        public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {}

        @Override
        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return null; }

        @Override
        public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException { return null; }

        @Override
        public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return null; }

        @Override
        public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return null; }

        @Override
        public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException { return null; }

        @Override
        public java.util.Map<String, Class<?>> getTypeMap() throws SQLException { return null; }

        @Override
        public void setTypeMap(java.util.Map<String, Class<?>> map) throws SQLException {}

        @Override
        public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException {}

        @Override
        public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException {}

        @Override
        public String getClientInfo(String name) throws SQLException { return null; }

        @Override
        public java.util.Properties getClientInfo() throws SQLException { return null; }

        @Override
        public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException { return null; }

        @Override
        public java.sql.Struct createStruct(String typeName, Object[] attributes) throws SQLException { return null; }

        @Override
        public void setSchema(String schema) throws SQLException {}

        @Override
        public String getSchema() throws SQLException { return null; }

        @Override
        public void abort(java.util.concurrent.Executor executor) throws SQLException {}

        @Override
        public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) throws SQLException {}

        @Override
        public int getNetworkTimeout() throws SQLException { return 0; }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException { return null; }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException { return false; }

        @Override
        public boolean isValid(int timeout) throws SQLException { return true; }

        @Override
        public java.sql.NClob createNClob() throws SQLException { return null; }

        @Override
        public java.sql.SQLXML createSQLXML() throws SQLException { return null; }

        @Override
        public java.sql.Clob createClob() throws SQLException { return null; }

        @Override
        public java.sql.Blob createBlob() throws SQLException { return null; }
    }

    @Test
    public void testTransactionCommit() {
        MockDataSource dataSource = new MockDataSource();
        PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(definition);
        
        Assertions.assertTrue(status.isNewTransaction());
        Assertions.assertFalse(dataSource.autoCommit);
        
        txManager.commit(status);
        
        Assertions.assertTrue(dataSource.committed);
        Assertions.assertFalse(dataSource.rolledBack);
        Assertions.assertTrue(status.isCompleted());
    }

    @Test
    public void testTransactionRollback() {
        MockDataSource dataSource = new MockDataSource();
        PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(definition);
        
        txManager.rollback(status);
        
        Assertions.assertFalse(dataSource.committed);
        Assertions.assertTrue(dataSource.rolledBack);
        Assertions.assertTrue(status.isCompleted());
    }

    @Test
    public void testSetRollbackOnly() {
        MockDataSource dataSource = new MockDataSource();
        PlatformTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = txManager.getTransaction(definition);
        
        status.setRollbackOnly();
        Assertions.assertTrue(status.isRollbackOnly());
        
        txManager.commit(status);
        
        Assertions.assertFalse(dataSource.committed);
        Assertions.assertTrue(dataSource.rolledBack);
    }

    @Test
    public void testTransactionDefinitionFromAnnotation() {
        @Transactional(
            propagation = Transactional.Propagation.REQUIRES_NEW,
            isolation = Transactional.Isolation.READ_COMMITTED,
            timeout = 30,
            readOnly = true
        )
        class TestClass {}

        Transactional annotation = TestClass.class.getAnnotation(Transactional.class);
        DefaultTransactionDefinition definition = DefaultTransactionDefinition.from(annotation);
        
        Assertions.assertEquals(Transactional.Propagation.REQUIRES_NEW, definition.getPropagationBehavior());
        Assertions.assertEquals(Transactional.Isolation.READ_COMMITTED, definition.getIsolationLevel());
        Assertions.assertEquals(30, definition.getTimeout());
        Assertions.assertTrue(definition.isReadOnly());
    }
}
