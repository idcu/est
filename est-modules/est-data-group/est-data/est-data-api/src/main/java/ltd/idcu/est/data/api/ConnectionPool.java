package ltd.idcu.est.data.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionPool {

    Connection getConnection() throws SQLException;

    Connection getConnection(long timeoutMillis) throws SQLException;

    void releaseConnection(Connection connection);

    void close();

    int getActiveConnections();

    int getIdleConnections();

    int getMinPoolSize();

    int getMaxPoolSize();

    long getConnectionTimeout();

    long getMaxLifetime();

    long getMaxIdleTime();

    boolean isTestWhileIdle();

    long getValidationInterval();

    String getValidationQuery();

    ConnectionPoolStats getStats();
}
