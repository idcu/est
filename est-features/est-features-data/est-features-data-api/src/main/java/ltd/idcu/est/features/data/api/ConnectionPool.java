package ltd.idcu.est.features.data.api;

import java.sql.Connection;

public interface ConnectionPool {
    
    Connection getConnection();
    
    Connection getConnection(long timeoutMillis);
    
    void releaseConnection(Connection connection);
    
    void close();
    
    int getPoolSize();
    
    int getActiveConnections();
    
    int getIdleConnections();
    
    int getMaxPoolSize();
    
    void setMaxPoolSize(int maxPoolSize);
    
    int getMinPoolSize();
    
    void setMinPoolSize(int minPoolSize);
    
    long getConnectionTimeout();
    
    void setConnectionTimeout(long timeoutMillis);
    
    boolean isClosed();
    
    void validateConnections();
    
    ConnectionPoolStats getStats();
}
