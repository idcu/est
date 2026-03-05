package ltd.idcu.est.utils.format.json;

public interface JsonSerializer<T> {
    String serialize(T value);
}
