package ltd.idcu.est.features.data.jdbc;

import ltd.idcu.est.features.data.api.DataException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DialectFactory {
    
    private static final Map<String, Dialect> dialectCache = new HashMap<>();
    
    static {
        dialectCache.put("MySQL", new MySQLDialect());
        dialectCache.put("PostgreSQL", new PostgreSQLDialect());
        dialectCache.put("Oracle", new OracleDialect());
        dialectCache.put("SQLServer", new SQLServerDialect());
        dialectCache.put("Microsoft SQL Server", new SQLServerDialect());
        dialectCache.put("SQLite", new SQLiteDialect());
    }
    
    public static Dialect getDialect(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return getDialect(connection);
        } catch (SQLException e) {
            throw new DataException("Failed to determine database dialect", e);
        }
    }
    
    public static Dialect getDialect(Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String productName = metaData.getDatabaseProductName();
            return getDialectByName(productName);
        } catch (SQLException e) {
            throw new DataException("Failed to determine database dialect", e);
        }
    }
    
    public static Dialect getDialectByName(String databaseName) {
        for (Map.Entry<String, Dialect> entry : dialectCache.entrySet()) {
            if (databaseName.toLowerCase().contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }
        return new MySQLDialect();
    }
    
    public static void registerDialect(String name, Dialect dialect) {
        dialectCache.put(name, dialect);
    }
}
