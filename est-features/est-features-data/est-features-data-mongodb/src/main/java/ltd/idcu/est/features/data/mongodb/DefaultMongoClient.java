package ltd.idcu.est.features.data.mongodb;

import ltd.idcu.est.features.data.api.DataException;
import ltd.idcu.est.features.data.api.MongoClient;
import ltd.idcu.est.features.data.api.MongoConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMongoClient implements MongoClient {
    
    private final Map<String, Map<String, Object>> collections;
    private final String host;
    private final int port;
    private final String database;
    private boolean connected;
    
    public DefaultMongoClient() {
        this(MongoConfig.defaultConfig());
    }
    
    public DefaultMongoClient(String host, int port, String database) {
        this(new MongoConfig().setHost(host).setPort(port).setDatabase(database));
    }
    
    public DefaultMongoClient(MongoConfig config) {
        this.host = config.getHost();
        this.port = config.getPort();
        this.database = config.getDatabase();
        this.collections = new ConcurrentHashMap<>();
        this.connected = false;
    }
    
    @Override
    public void connect() {
        if (connected) {
            return;
        }
        connected = true;
    }
    
    private void ensureConnected() {
        if (!connected) {
            connect();
        }
    }
    
    @Override
    public void disconnect() {
        close();
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void createCollection(String collectionName) {
        ensureConnected();
        if (collectionName == null || collectionName.isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        collections.computeIfAbsent(collectionName, k -> new ConcurrentHashMap<>());
    }
    
    @Override
    public void dropCollection(String collectionName) {
        ensureConnected();
        if (collectionName != null) {
            collections.remove(collectionName);
        }
    }
    
    @Override
    public boolean collectionExists(String collectionName) {
        ensureConnected();
        return collectionName != null && collections.containsKey(collectionName);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> String insertOne(String collectionName, T document) {
        ensureConnected();
        if (collectionName == null || collectionName.isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        
        Map<String, Object> collection = collections.computeIfAbsent(collectionName, k -> new ConcurrentHashMap<>());
        String id = UUID.randomUUID().toString();
        
        Map<String, Object> docMap;
        if (document instanceof Map) {
            docMap = new HashMap<>((Map<String, Object>) document);
        } else {
            docMap = objectToMap(document);
        }
        docMap.put("_id", id);
        
        collection.put(id, docMap);
        return id;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<String> insertMany(String collectionName, List<T> documents) {
        ensureConnected();
        List<String> ids = new ArrayList<>();
        if (documents != null) {
            for (T document : documents) {
                ids.add(insertOne(collectionName, document));
            }
        }
        return ids;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOne(String collectionName, String id, Class<T> clazz) {
        ensureConnected();
        if (collectionName == null || id == null) {
            return null;
        }
        
        Map<String, Object> collection = collections.get(collectionName);
        if (collection == null) {
            return null;
        }
        
        Object doc = collection.get(id);
        if (doc == null) {
            return null;
        }
        
        if (clazz.isInstance(doc)) {
            return (T) doc;
        }
        
        if (doc instanceof Map) {
            return mapToObject((Map<String, Object>) doc, clazz);
        }
        
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(String collectionName, Class<T> clazz) {
        ensureConnected();
        List<T> result = new ArrayList<>();
        Map<String, Object> collection = collections.get(collectionName);
        if (collection != null) {
            for (Object doc : collection.values()) {
                if (clazz.isInstance(doc)) {
                    result.add((T) doc);
                } else if (doc instanceof Map) {
                    result.add(mapToObject((Map<String, Object>) doc, clazz));
                }
            }
        }
        return result;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(String collectionName, String fieldName, Object value, Class<T> clazz) {
        ensureConnected();
        List<T> result = new ArrayList<>();
        Map<String, Object> collection = collections.get(collectionName);
        if (collection != null && fieldName != null) {
            for (Object doc : collection.values()) {
                Map<String, Object> docMap;
                if (doc instanceof Map) {
                    docMap = (Map<String, Object>) doc;
                } else {
                    docMap = objectToMap(doc);
                }
                
                Object fieldValue = docMap.get(fieldName);
                if (Objects.equals(fieldValue, value)) {
                    if (clazz.isInstance(doc)) {
                        result.add((T) doc);
                    } else {
                        result.add(mapToObject(docMap, clazz));
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T updateOne(String collectionName, String id, T document) {
        ensureConnected();
        if (collectionName == null || id == null || document == null) {
            return null;
        }
        
        Map<String, Object> collection = collections.get(collectionName);
        if (collection == null || !collection.containsKey(id)) {
            return null;
        }
        
        Map<String, Object> docMap;
        if (document instanceof Map) {
            docMap = new HashMap<>((Map<String, Object>) document);
        } else {
            docMap = objectToMap(document);
        }
        docMap.put("_id", id);
        
        collection.put(id, docMap);
        return document;
    }
    
    @Override
    public boolean deleteOne(String collectionName, String id) {
        ensureConnected();
        if (collectionName == null || id == null) {
            return false;
        }
        Map<String, Object> collection = collections.get(collectionName);
        if (collection == null) {
            return false;
        }
        return collection.remove(id) != null;
    }
    
    @Override
    public long deleteMany(String collectionName, Map<String, Object> filter) {
        ensureConnected();
        long count = 0;
        Map<String, Object> collection = collections.get(collectionName);
        if (collection != null && filter != null) {
            List<String> toDelete = new ArrayList<>();
            for (Map.Entry<String, Object> entry : collection.entrySet()) {
                Map<String, Object> docMap;
                if (entry.getValue() instanceof Map) {
                    docMap = (Map<String, Object>) entry.getValue();
                } else {
                    docMap = objectToMap(entry.getValue());
                }
                
                boolean match = true;
                for (Map.Entry<String, Object> filterEntry : filter.entrySet()) {
                    if (!Objects.equals(docMap.get(filterEntry.getKey()), filterEntry.getValue())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    toDelete.add(entry.getKey());
                }
            }
            for (String id : toDelete) {
                collection.remove(id);
                count++;
            }
        }
        return count;
    }
    
    @Override
    public long count(String collectionName) {
        ensureConnected();
        Map<String, Object> collection = collections.get(collectionName);
        return collection != null ? collection.size() : 0;
    }
    
    @Override
    public long count(String collectionName, Map<String, Object> filter) {
        ensureConnected();
        long count = 0;
        Map<String, Object> collection = collections.get(collectionName);
        if (collection != null && filter != null) {
            for (Object doc : collection.values()) {
                Map<String, Object> docMap;
                if (doc instanceof Map) {
                    docMap = (Map<String, Object>) doc;
                } else {
                    docMap = objectToMap(doc);
                }
                
                boolean match = true;
                for (Map.Entry<String, Object> filterEntry : filter.entrySet()) {
                    if (!Objects.equals(docMap.get(filterEntry.getKey()), filterEntry.getValue())) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    count++;
                }
            }
        }
        return count;
    }
    
    @Override
    public <T> List<T> findWithLimit(String collectionName, int limit, Class<T> clazz) {
        ensureConnected();
        List<T> all = findAll(collectionName, clazz);
        if (limit <= 0 || all.size() <= limit) {
            return all;
        }
        return all.subList(0, limit);
    }
    
    @Override
    public <T> List<T> findWithSkipAndLimit(String collectionName, int skip, int limit, Class<T> clazz) {
        ensureConnected();
        List<T> all = findAll(collectionName, clazz);
        if (skip < 0) skip = 0;
        if (skip >= all.size()) {
            return new ArrayList<>();
        }
        int endIndex = Math.min(skip + limit, all.size());
        return all.subList(skip, endIndex);
    }
    
    private Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            throw new DataException("Failed to convert object to map", e);
        }
        return map;
    }
    
    @SuppressWarnings("unchecked")
    private <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = map.get(field.getName());
                if (value != null) {
                    field.set(obj, value);
                }
            }
            return obj;
        } catch (Exception e) {
            throw new DataException("Failed to convert map to object", e);
        }
    }
    
    @Override
    public void close() {
        connected = false;
        collections.clear();
    }
}
