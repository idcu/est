package ltd.idcu.est.utils.format.json;

import org.objectweb.asm.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsmJsonSerializerGenerator implements JsonSerializerGenerator {
    
    private static final Map<Class<?>, JsonSerializer<?>> serializerCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, JsonDeserializer<?>> deserializerCache = new ConcurrentHashMap<>();
    private static int classCounter = 0;
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> JsonSerializer<T> generateSerializer(Class<T> clazz) {
        return (JsonSerializer<T>) serializerCache.computeIfAbsent(clazz, 
            c -> {
                try {
                    return new ReflectiveJsonSerializerGenerator.ReflectiveJsonSerializer<>(c);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate serializer for " + c.getName(), e);
                }
            });
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> JsonDeserializer<T> generateDeserializer(Class<T> clazz) {
        return (JsonDeserializer<T>) deserializerCache.computeIfAbsent(clazz, 
            c -> {
                try {
                    return new ReflectiveJsonSerializerGenerator.ReflectiveJsonDeserializer<>(c);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate deserializer for " + c.getName(), e);
                }
            });
    }
}
