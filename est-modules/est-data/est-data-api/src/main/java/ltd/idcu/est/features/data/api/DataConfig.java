package ltd.idcu.est.features.data.api;

public class DataConfig {
    
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int minPoolSize = 5;
    private int maxPoolSize = 20;
    private long connectionTimeout = 30000;
    private long idleTimeout = 600000;
    private long maxLifetime = 1800000;
    private int transactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED;
    private boolean autoCommit = true;
    private String validationQuery = "SELECT 1";
    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;
    private boolean testWhileIdle = true;
    private long validationInterval = 30000;
    private int maxStatementCacheSize = 100;
    private long maxIdleTime = 30 * 60 * 1000L;
    
    public DataConfig() {
    }
    
    public String getUrl() {
        return url;
    }
    
    public DataConfig setUrl(String url) {
        this.url = url;
        return this;
    }
    
    public String getUsername() {
        return username;
    }
    
    public DataConfig setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public DataConfig setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public DataConfig setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }
    
    public int getMinPoolSize() {
        return minPoolSize;
    }
    
    public DataConfig setMinPoolSize(int minPoolSize) {
        if (minPoolSize < 0) {
            throw new IllegalArgumentException("Min pool size must be >= 0");
        }
        this.minPoolSize = minPoolSize;
        return this;
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    
    public DataConfig setMaxPoolSize(int maxPoolSize) {
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("Max pool size must be >= 1");
        }
        this.maxPoolSize = maxPoolSize;
        return this;
    }
    
    public long getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public DataConfig setConnectionTimeout(long connectionTimeout) {
        if (connectionTimeout < 0) {
            throw new IllegalArgumentException("Connection timeout must be >= 0");
        }
        this.connectionTimeout = connectionTimeout;
        return this;
    }
    
    public long getIdleTimeout() {
        return idleTimeout;
    }
    
    public DataConfig setIdleTimeout(long idleTimeout) {
        if (idleTimeout < 0) {
            throw new IllegalArgumentException("Idle timeout must be >= 0");
        }
        this.idleTimeout = idleTimeout;
        return this;
    }
    
    public long getMaxLifetime() {
        return maxLifetime;
    }
    
    public DataConfig setMaxLifetime(long maxLifetime) {
        if (maxLifetime < 0) {
            throw new IllegalArgumentException("Max lifetime must be >= 0");
        }
        this.maxLifetime = maxLifetime;
        return this;
    }
    
    public int getTransactionIsolation() {
        return transactionIsolation;
    }
    
    public DataConfig setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
        return this;
    }
    
    public boolean isAutoCommit() {
        return autoCommit;
    }
    
    public DataConfig setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }
    
    public String getValidationQuery() {
        return validationQuery;
    }
    
    public DataConfig setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
        return this;
    }
    
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }
    
    public DataConfig setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }
    
    public boolean isTestOnReturn() {
        return testOnReturn;
    }
    
    public DataConfig setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        return this;
    }
    
    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }
    
    public DataConfig setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        return this;
    }
    
    public long getValidationInterval() {
        return validationInterval;
    }
    
    public DataConfig setValidationInterval(long validationInterval) {
        if (validationInterval < 0) {
            throw new IllegalArgumentException("Validation interval must be >= 0");
        }
        this.validationInterval = validationInterval;
        return this;
    }
    
    public int getMaxStatementCacheSize() {
        return maxStatementCacheSize;
    }
    
    public DataConfig setMaxStatementCacheSize(int maxStatementCacheSize) {
        if (maxStatementCacheSize < 0) {
            throw new IllegalArgumentException("Max statement cache size must be >= 0");
        }
        this.maxStatementCacheSize = maxStatementCacheSize;
        return this;
    }
    
    public long getMaxIdleTime() {
        return maxIdleTime;
    }
    
    public DataConfig setMaxIdleTime(long maxIdleTime) {
        if (maxIdleTime < 0) {
            throw new IllegalArgumentException("Max idle time must be >= 0");
        }
        this.maxIdleTime = maxIdleTime;
        return this;
    }
    
    public static DataConfig of(String url, String username, String password) {
        return new DataConfig()
                .setUrl(url)
                .setUsername(username)
                .setPassword(password);
    }
    
    public static DataConfig of(String url, String username, String password, String driverClassName) {
        return new DataConfig()
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverClassName(driverClassName);
    }
    
    @Override
    public String toString() {
        return "DataConfig{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", driverClassName='" + driverClassName + '\'' +
                ", minPoolSize=" + minPoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
    
    public static DataConfigBuilder builder() {
        return new DataConfigBuilder();
    }
    
    public static class DataConfigBuilder {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private int minPoolSize = 5;
        private int maxPoolSize = 20;
        private long connectionTimeout = 30000;
        
        public DataConfigBuilder url(String url) {
            this.url = url;
            return this;
        }
        
        public DataConfigBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public DataConfigBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public DataConfigBuilder driverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
            return this;
        }
        
        public DataConfigBuilder minPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
            return this;
        }
        
        public DataConfigBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }
        
        public DataConfigBuilder connectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }
        
        public DataConfig build() {
            return new DataConfig()
                    .setUrl(url)
                    .setUsername(username)
                    .setPassword(password)
                    .setDriverClassName(driverClassName)
                    .setMinPoolSize(minPoolSize)
                    .setMaxPoolSize(maxPoolSize)
                    .setConnectionTimeout(connectionTimeout);
        }
    }
}
