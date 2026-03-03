package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.*;

public final class JdbcData {
    
    private JdbcData() {
    }
    
    public static ConnectionPool newConnectionPool(DataConfig config) {
        return new DefaultConnectionPool(config);
    }
    
    public static TransactionManager newTransactionManager(ConnectionPool connectionPool) {
        return new JdbcTransactionManager(connectionPool);
    }
    
    public static Orm newOrm(ConnectionPool connectionPool) {
        return new JdbcOrm(connectionPool);
    }
    
    public static <T, ID> JdbcRepository<T, ID> newRepository(
            ConnectionPool connectionPool, 
            Class<T> entityClass,
            EntityMapper<T> mapper,
            String tableName,
            String idColumnName) {
        return new JdbcRepository<>(connectionPool, entityClass, mapper, tableName, idColumnName);
    }
    
    public static DataConfig.DataConfigBuilder configBuilder() {
        return new DataConfig.DataConfigBuilder();
    }
}
