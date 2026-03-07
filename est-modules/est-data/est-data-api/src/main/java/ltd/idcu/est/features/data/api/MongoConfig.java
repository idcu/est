package ltd.idcu.est.features.data.api;

public class MongoConfig {
    
    private String host = "localhost";
    private int port = 27017;
    private String username;
    private String password;
    private String database = "est";
    private String connectionString;
    private int connectionTimeout = 5000;
    private int socketTimeout = 60000;
    private int serverSelectionTimeout = 30000;
    private int minPoolSize = 0;
    private int maxPoolSize = 100;
    private int maxWaitTime = 120000;
    
    public MongoConfig() {
    }
    
    public String getHost() {
        return host;
    }
    
    public MongoConfig setHost(String host) {
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Host cannot be null or empty");
        }
        this.host = host;
        return this;
    }
    
    public int getPort() {
        return port;
    }
    
    public MongoConfig setPort(int port) {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be between 0 and 65535");
        }
        this.port = port;
        return this;
    }
    
    public String getUsername() {
        return username;
    }
    
    public MongoConfig setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public MongoConfig setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public String getDatabase() {
        return database;
    }
    
    public MongoConfig setDatabase(String database) {
        if (database == null || database.isEmpty()) {
            throw new IllegalArgumentException("Database cannot be null or empty");
        }
        this.database = database;
        return this;
    }
    
    public String getConnectionString() {
        return connectionString;
    }
    
    public MongoConfig setConnectionString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public MongoConfig setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout < 0) {
            throw new IllegalArgumentException("Connection timeout must be >= 0");
        }
        this.connectionTimeout = connectionTimeout;
        return this;
    }
    
    public int getSocketTimeout() {
        return socketTimeout;
    }
    
    public MongoConfig setSocketTimeout(int socketTimeout) {
        if (socketTimeout < 0) {
            throw new IllegalArgumentException("Socket timeout must be >= 0");
        }
        this.socketTimeout = socketTimeout;
        return this;
    }
    
    public int getServerSelectionTimeout() {
        return serverSelectionTimeout;
    }
    
    public MongoConfig setServerSelectionTimeout(int serverSelectionTimeout) {
        if (serverSelectionTimeout < 0) {
            throw new IllegalArgumentException("Server selection timeout must be >= 0");
        }
        this.serverSelectionTimeout = serverSelectionTimeout;
        return this;
    }
    
    public int getMinPoolSize() {
        return minPoolSize;
    }
    
    public MongoConfig setMinPoolSize(int minPoolSize) {
        if (minPoolSize < 0) {
            throw new IllegalArgumentException("Min pool size must be >= 0");
        }
        this.minPoolSize = minPoolSize;
        return this;
    }
    
    public int getMaxPoolSize() {
        return maxPoolSize;
    }
    
    public MongoConfig setMaxPoolSize(int maxPoolSize) {
        if (maxPoolSize < 1) {
            throw new IllegalArgumentException("Max pool size must be >= 1");
        }
        this.maxPoolSize = maxPoolSize;
        return this;
    }
    
    public int getMaxWaitTime() {
        return maxWaitTime;
    }
    
    public MongoConfig setMaxWaitTime(int maxWaitTime) {
        if (maxWaitTime < 0) {
            throw new IllegalArgumentException("Max wait time must be >= 0");
        }
        this.maxWaitTime = maxWaitTime;
        return this;
    }
    
    public static MongoConfig defaultConfig() {
        return new MongoConfig();
    }
    
    public static MongoConfig of(String host, int port, String database) {
        return new MongoConfig()
                .setHost(host)
                .setPort(port)
                .setDatabase(database);
    }
    
    public static MongoConfig of(String host, int port, String username, String password, String database) {
        return new MongoConfig()
                .setHost(host)
                .setPort(port)
                .setUsername(username)
                .setPassword(password)
                .setDatabase(database);
    }
    
    public static MongoConfig ofConnectionString(String connectionString) {
        return new MongoConfig().setConnectionString(connectionString);
    }
    
    @Override
    public String toString() {
        return "MongoConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", database='" + database + '\'' +
                ", maxPoolSize=" + maxPoolSize +
                '}';
    }
    
    public static MongoConfigBuilder builder() {
        return new MongoConfigBuilder();
    }
    
    public static class MongoConfigBuilder {
        private String host = "localhost";
        private int port = 27017;
        private String username;
        private String password;
        private String database = "est";
        private String connectionString;
        private int connectionTimeout = 5000;
        private int socketTimeout = 60000;
        private int serverSelectionTimeout = 30000;
        private int maxPoolSize = 100;
        
        public MongoConfigBuilder host(String host) {
            this.host = host;
            return this;
        }
        
        public MongoConfigBuilder port(int port) {
            this.port = port;
            return this;
        }
        
        public MongoConfigBuilder username(String username) {
            this.username = username;
            return this;
        }
        
        public MongoConfigBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        public MongoConfigBuilder database(String database) {
            this.database = database;
            return this;
        }
        
        public MongoConfigBuilder connectionString(String connectionString) {
            this.connectionString = connectionString;
            return this;
        }
        
        public MongoConfigBuilder connectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }
        
        public MongoConfigBuilder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }
        
        public MongoConfigBuilder serverSelectionTimeout(int serverSelectionTimeout) {
            this.serverSelectionTimeout = serverSelectionTimeout;
            return this;
        }
        
        public MongoConfigBuilder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }
        
        public MongoConfig build() {
            return new MongoConfig()
                    .setHost(host)
                    .setPort(port)
                    .setUsername(username)
                    .setPassword(password)
                    .setDatabase(database)
                    .setConnectionString(connectionString)
                    .setConnectionTimeout(connectionTimeout)
                    .setSocketTimeout(socketTimeout)
                    .setServerSelectionTimeout(serverSelectionTimeout)
                    .setMaxPoolSize(maxPoolSize);
        }
    }
}
