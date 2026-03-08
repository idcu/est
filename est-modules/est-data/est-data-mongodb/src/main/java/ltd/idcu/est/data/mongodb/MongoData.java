package ltd.idcu.est.data.mongodb;

import ltd.idcu.est.data.api.MongoClient;
import ltd.idcu.est.data.api.MongoConfig;
import ltd.idcu.est.data.api.Orm;

public final class MongoData {
    
    private MongoData() {
    }
    
    public static MongoClient newClient() {
        return new DefaultMongoClient();
    }
    
    public static MongoClient newClient(String host, int port, String database) {
        return new DefaultMongoClient(host, port, database);
    }
    
    public static MongoClient newClient(MongoConfig config) {
        return new DefaultMongoClient(config);
    }
    
    public static Orm newOrm(MongoClient client) {
        return new MongoOrm(client);
    }
    
    public static Orm newOrm(MongoClient client, String database) {
        return new MongoOrm(client, database);
    }
    
    public static <T, ID> MongoRepository.MongoRepositoryBuilder<T, ID> repositoryBuilder() {
        return MongoRepository.builder();
    }
    
    public static MongoConfig.MongoConfigBuilder configBuilder() {
        return new MongoConfig.MongoConfigBuilder();
    }
}
