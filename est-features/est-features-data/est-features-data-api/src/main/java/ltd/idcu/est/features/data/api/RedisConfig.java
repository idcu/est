package ltd.idcu.est.features.data.api;

public class RedisConfig {
    
    private String host = "localhost";
    private int port = 6379;
    private String password;
    private int database = 0;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;
    private int maxPoolSize = 10;
    private int minIdle = 2;
    private long maxWaitTime = 30000;
    private String keyPrefix = "";
    
    public RedisConfig() {
    }
    
    public String getHost() {
        return host;
    }
    
    public RedisConfig setHost(String host) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty");
        }
        this.host = host;
        return this;
    }
    
    public int getPort() {
        return port;
    }
    
    public RedisConfig setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 0 and 65535");
        }
        this.port = port;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public RedisConfig setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public int getDatabase() {
        return database;
    }
    
    public RedisConfig setDatabase(int database) {
        if (database < 0) {
            throw new IllegalArgumentException("Database must be >= 0");
        }
        this.database = database;
        return this;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public RedisConfig setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout < 0) {
            throw new IllegalArgumentException("Connection timeout must be >= 0");
        }
        this.connectionTimeout = connectionTimeout;
        return this;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public RedisConfig setReadTimeout(int readTimeout) {
        if (readTimeout < 0) {
            throw new IllegalArgumentException("Read timeout must be >= 0");
        }
        this.readTimeout = readTimeout;
        return this;
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    
    public RedisConfig setMaxPoolSize(int maxPoolSize) {
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("Max pool size must be >= 1");
        }
        this.maxPoolSize = maxPoolSize;
        return this;
    }
    
    public int getMinIdle() {
        return minIdle;
    }
    
    public RedisConfig setMinIdle(int minIdle) {
        if (minIdle < 0) {
            throw new IllegalArgumentException("Min idle must be >= 0");
        }
        this.minIdle = minIdle;
        return this;
    }
    
    public long getMaxWaitTime() {
        return maxWaitTime;
    }
    
    public RedisConfig setMaxWaitTime(long maxWaitTime) {
        if (maxWaitTime < 0) {
            throw new IllegalArgumentException("Max wait time must be >= 0");
        }
        this.maxWaitTime = maxWaitTime;
        return this;
    }
    
    public String getKeyPrefix() {
        return keyPrefix;
    }
    
    public RedisConfig setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix != null ? keyPrefix : "";
        return this;
    }
    
    public static RedisConfig defaultConfig() {
        return new RedisConfig();
    }
    
    public static RedisConfig of(String host, int port) {
        return new RedisConfig().setHost(host).setPort(port);
    }
    
    public static RedisConfig of(String host, int port, String password, int database) {
        return new RedisConfig()
                .setHost(host)
                .setPort(port)
                .setPassword(password)
                .setDatabase(database);
    }
    
    @Override
    public String toString() {
        return "RedisConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database=" + database +
                ", maxPoolSize=" + maxPoolSize +
                ", keyPrefix='" + keyPrefix + '\'' +
                '}';
    }
    
    public static RedisConfigBuilder builder() {
        return new RedisConfigBuilder();
    }
    
    public static class RedisConfigBuilder {
        private String host = "localhost";
        private int port = 6379;
        private String password;
        private int database = 0;
        private int connectionTimeout = 5000;
        private int readTimeout = 5000;
        private String keyPrefix = "";
        
        public RedisConfigBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public RedisConfigBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public RedisConfigBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public RedisConfigBuilder database(int database) {
            this.database = database;
            return this;
        }
        
        public RedisConfigBuilder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }
        
        public RedisConfigBuilder readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }
        
        public RedisConfigBuilder keyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
            return this;
        }
        
        public RedisConfig build() {
            return new RedisConfig()
                    .setHost(host)
                    .setPort(port)
                    .setPassword(password)
                    .setDatabase(database)
                    .setConnectionTimeout(connectionTimeout)
                    .setReadTimeout(readTimeout)
                    .setKeyPrefix(keyPrefix);
        }
    }
}
