package ltd.idcu.est.codegen.db.database;

import ltd.idcu.est.codegen.db.reader.DatabaseMetadataReader;
import ltd.idcu.est.codegen.db.reader.MySQLMetadataReader;
import ltd.idcu.est.codegen.db.reader.PostgreSQLMetadataReader;

public enum DatabaseType {
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver", MySQLMetadataReader.class),
    POSTGRESQL("postgresql", "org.postgresql.Driver", PostgreSQLMetadataReader.class),
    ORACLE("oracle", "oracle.jdbc.OracleDriver", null),
    SQLSERVER("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", null);

    private final String name;
    private final String defaultDriver;
    private final Class<? extends DatabaseMetadataReader> readerClass;

    DatabaseType(String name, String defaultDriver, Class<? extends DatabaseMetadataReader> readerClass) {
        this.name = name;
        this.defaultDriver = defaultDriver;
        this.readerClass = readerClass;
    }

    public String getName() {
        return name;
    }

    public String getDefaultDriver() {
        return defaultDriver;
    }

    public Class<? extends DatabaseMetadataReader> getReaderClass() {
        return readerClass;
    }

    public static DatabaseType fromName(String name) {
        for (DatabaseType type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown database type: " + name);
    }

    public static DatabaseType fromJdbcUrl(String url) {
        if (url == null) {
            return MYSQL;
        }
        if (url.startsWith("jdbc:mysql:")) {
            return MYSQL;
        } else if (url.startsWith("jdbc:postgresql:")) {
            return POSTGRESQL;
        } else if (url.startsWith("jdbc:oracle:")) {
            return ORACLE;
        } else if (url.startsWith("jdbc:sqlserver:")) {
            return SQLSERVER;
        }
        return MYSQL;
    }

    public DatabaseMetadataReader createReader() {
        if (readerClass == null) {
            throw new UnsupportedOperationException("Database type " + name + " is not supported yet");
        }
        try {
            return readerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create reader for " + name, e);
        }
    }
}
