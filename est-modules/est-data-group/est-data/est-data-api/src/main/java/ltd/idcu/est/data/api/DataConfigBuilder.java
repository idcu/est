package ltd.idcu.est.data.api;

public class DataConfigBuilder {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int minPoolSize = 1;
    private int maxPoolSize = 10;
    private long connectionTimeout = 30000;
    private long validationInterval = 30000;
    private boolean testOnBorrow = true;
    private long maxIdleTime = 600000;
    private boolean autoCommit = true;
    private int transactionIsolation = -1;
    private long maxLifetime = 1800000;
    private boolean testWhileIdle = true;
    private String validationQuery;

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

    public DataConfigBuilder validationInterval(long validationInterval) {
        this.validationInterval = validationInterval;
        return this;
    }

    public DataConfigBuilder testOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        return this;
    }

    public DataConfigBuilder maxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public DataConfigBuilder autoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    public DataConfigBuilder transactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
        return this;
    }

    public DataConfigBuilder maxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
        return this;
    }

    public DataConfigBuilder testWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        return this;
    }

    public DataConfigBuilder validationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
        return this;
    }

    public DataConfig build() {
        return new DefaultDataConfig(this);
    }

    private static class DefaultDataConfig implements DataConfig {
        private final String url;
        private final String username;
        private final String password;
        private final String driverClassName;
        private int minPoolSize;
        private int maxPoolSize;
        private long connectionTimeout;
        private long validationInterval;
        private boolean testOnBorrow;
        private long maxIdleTime;
        private boolean autoCommit;
        private int transactionIsolation;
        private long maxLifetime;
        private boolean testWhileIdle;
        private String validationQuery;

        private DefaultDataConfig(DataConfigBuilder builder) {
            this.url = builder.url;
            this.username = builder.username;
            this.password = builder.password;
            this.driverClassName = builder.driverClassName;
            this.minPoolSize = builder.minPoolSize;
            this.maxPoolSize = builder.maxPoolSize;
            this.connectionTimeout = builder.connectionTimeout;
            this.validationInterval = builder.validationInterval;
            this.testOnBorrow = builder.testOnBorrow;
            this.maxIdleTime = builder.maxIdleTime;
            this.autoCommit = builder.autoCommit;
            this.transactionIsolation = builder.transactionIsolation;
            this.maxLifetime = builder.maxLifetime;
            this.testWhileIdle = builder.testWhileIdle;
            this.validationQuery = builder.validationQuery;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getDriverClassName() {
            return driverClassName;
        }

        @Override
        public int getMinPoolSize() {
            return minPoolSize;
        }

        @Override
        public void setMinPoolSize(int minPoolSize) {
            this.minPoolSize = minPoolSize;
        }

        @Override
        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        @Override
        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        @Override
        public long getConnectionTimeout() {
            return connectionTimeout;
        }

        @Override
        public void setConnectionTimeout(long timeoutMillis) {
            this.connectionTimeout = timeoutMillis;
        }

        @Override
        public long getValidationInterval() {
            return validationInterval;
        }

        @Override
        public boolean isTestOnBorrow() {
            return testOnBorrow;
        }

        @Override
        public long getMaxIdleTime() {
            return maxIdleTime;
        }

        @Override
        public boolean isAutoCommit() {
            return autoCommit;
        }

        @Override
        public int getTransactionIsolation() {
            return transactionIsolation;
        }

        @Override
        public long getMaxLifetime() {
            return maxLifetime;
        }

        @Override
        public void setMaxLifetime(long maxLifetimeMillis) {
            this.maxLifetime = maxLifetimeMillis;
        }

        @Override
        public void setMaxIdleTime(long maxIdleTimeMillis) {
            this.maxIdleTime = maxIdleTimeMillis;
        }

        @Override
        public boolean isTestWhileIdle() {
            return testWhileIdle;
        }

        @Override
        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        @Override
        public void setValidationInterval(long validationIntervalMillis) {
            this.validationInterval = validationIntervalMillis;
        }

        @Override
        public String getValidationQuery() {
            return validationQuery;
        }

        @Override
        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }
    }
}
