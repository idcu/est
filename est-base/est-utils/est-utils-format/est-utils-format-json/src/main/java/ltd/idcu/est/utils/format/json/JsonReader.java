package ltd.idcu.est.utils.format.json;

import java.io.Closeable;
import java.io.IOException;

public interface JsonReader extends Closeable {
    
    JsonToken peek() throws IOException;
    
    JsonToken nextToken() throws IOException;
    
    String getCurrentName() throws IOException;
    
    String getString() throws IOException;
    
    int getInt() throws IOException;
    
    long getLong() throws IOException;
    
    double getDouble() throws IOException;
    
    boolean getBoolean() throws IOException;
    
    boolean isNull() throws IOException;
    
    void skipValue() throws IOException;
}
