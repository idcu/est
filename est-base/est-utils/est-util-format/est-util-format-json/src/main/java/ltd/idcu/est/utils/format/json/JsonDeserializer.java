package ltd.idcu.est.utils.format.json;

public interface JsonDeserializer<T> {
    T deserialize(Object jsonValue);
}
