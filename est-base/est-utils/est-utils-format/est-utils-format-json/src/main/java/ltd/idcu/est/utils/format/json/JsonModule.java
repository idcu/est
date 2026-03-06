package ltd.idcu.est.utils.format.json;

public interface JsonModule {
    
    String getModuleName();
    
    default void setupModule(SetupContext context) {
    }
    
    interface SetupContext {
        <T> void addSerializer(Class<T> type, JsonSerializer<T> serializer);
        <T> void addDeserializer(Class<T> type, JsonDeserializer<T> deserializer);
    }
}
