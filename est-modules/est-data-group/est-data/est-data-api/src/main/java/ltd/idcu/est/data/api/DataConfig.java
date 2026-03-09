package ltd.idcu.est.data.api;

public interface DataConfig {
    String getUrl();
    String getUsername();
    String getPassword();
    String getDriverClassName();
    int getMinPoolSize();
    int getMaxPoolSize();
    void setMinPoolSize(int minPoolSize);
    void setMaxPoolSize(int maxPoolSize);
    long getConnectionTimeout();
    void setConnectionTimeout(long timeoutMillis);
    long getValidationInterval();
    boolean isTestOnBorrow();
    long getMaxIdleTime();
    boolean isAutoCommit();
    int getTransactionIsolation();
    
    long getMaxLifetime();
    void setMaxLifetime(long maxLifetimeMillis);
    void setMaxIdleTime(long maxIdleTimeMillis);
    boolean isTestWhileIdle();
    void setTestWhileIdle(boolean testWhileIdle);
    void setValidationInterval(long validationIntervalMillis);
    String getValidationQuery();
    void setValidationQuery(String validationQuery);
    
    static DataConfigBuilder builder() {
        return new DataConfigBuilder();
    }
}
