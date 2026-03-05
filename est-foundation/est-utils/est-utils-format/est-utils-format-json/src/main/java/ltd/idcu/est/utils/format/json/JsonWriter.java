package ltd.idcu.est.utils.format.json;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface JsonWriter extends Closeable {
    
    JsonWriter writeStartObject() throws IOException;
    
    JsonWriter writeEndObject() throws IOException;
    
    JsonWriter writeStartArray() throws IOException;
    
    JsonWriter writeEndArray() throws IOException;
    
    JsonWriter writeName(String name) throws IOException;
    
    JsonWriter writeString(String value) throws IOException;
    
    JsonWriter writeInt(int value) throws IOException;
    
    JsonWriter writeLong(long value) throws IOException;
    
    JsonWriter writeDouble(double value) throws IOException;
    
    JsonWriter writeBigDecimal(BigDecimal value) throws IOException;
    
    JsonWriter writeBigInteger(BigInteger value) throws IOException;
    
    JsonWriter writeBoolean(boolean value) throws IOException;
    
    JsonWriter writeNull() throws IOException;
    
    JsonWriter writeRawValue(String value) throws IOException;
    
    JsonWriter flush() throws IOException;
}
