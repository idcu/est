package ltd.idcu.est.utils.format.json;

import java.util.HashMap;
import java.util.Map;

public class SimpleModule implements JsonModule {
    
    private final String name;
    private final Map<Class<?>, JsonSerializer<?>> serializers;
    private final Map<Class<?>, JsonDeserializer<?>> deserializers;
    
    public SimpleModule(String name) {
        this.name = name;
        this.serializers = new HashMap<>();
        this.deserializers = new HashMap<>();
    }
    
    @Override
    public String getModuleName() {
        return name;
    }
    
    public <T> SimpleModule addSerializer(Class<T> type, JsonSerializer<T> serializer) {
        serializers.put(type, serializer);
        return this;
    }
    
    public <T> SimpleModule addDeserializer(Class<T> type, JsonDeserializer<T> deserializer) {
        deserializers.put(type, deserializer);
        return this;
    }
    
    @Override
    public void setupModule(SetupContext context) {
        for (Map.Entry<Class<?>, JsonSerializer<?>> entry : serializers.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<Object> type = (Class<Object>) entry.getKey();
            @SuppressWarnings("unchecked")
            JsonSerializer<Object> serializer = (JsonSerializer<Object>) entry.getValue();
            context.addSerializer(type, serializer);
        }
        for (Map.Entry<Class<?>, JsonDeserializer<?>> entry : deserializers.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<Object> type = (Class<Object>) entry.getKey();
            @SuppressWarnings("unchecked")
            JsonDeserializer<Object> deserializer = (JsonDeserializer<Object>) entry.getValue();
            context.addDeserializer(type, deserializer);
        }
    }
}
