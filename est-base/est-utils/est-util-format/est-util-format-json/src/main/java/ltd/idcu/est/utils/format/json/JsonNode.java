package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class JsonNode {

    public enum NodeType {
        OBJECT,
        ARRAY,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL
    }

    public abstract NodeType getNodeType();

    public boolean isObject() {
        return getNodeType() == NodeType.OBJECT;
    }

    public boolean isArray() {
        return getNodeType() == NodeType.ARRAY;
    }

    public boolean isTextual() {
        return getNodeType() == NodeType.STRING;
    }

    public boolean isNumber() {
        return getNodeType() == NodeType.NUMBER;
    }

    public boolean isBoolean() {
        return getNodeType() == NodeType.BOOLEAN;
    }

    public boolean isNull() {
        return getNodeType() == NodeType.NULL;
    }

    public abstract String asText();

    public String asText(String defaultValue) {
        if (isNull()) {
            return defaultValue;
        }
        return asText();
    }

    public int asInt() {
        return asInt(0);
    }

    public abstract int asInt(int defaultValue);

    public long asLong() {
        return asLong(0L);
    }

    public abstract long asLong(long defaultValue);

    public double asDouble() {
        return asDouble(0.0);
    }

    public abstract double asDouble(double defaultValue);

    public boolean asBoolean() {
        return asBoolean(false);
    }

    public abstract boolean asBoolean(boolean defaultValue);

    public BigInteger asBigInteger() {
        return asBigInteger(BigInteger.ZERO);
    }

    public abstract BigInteger asBigInteger(BigInteger defaultValue);

    public BigDecimal asBigDecimal() {
        return asBigDecimal(BigDecimal.ZERO);
    }

    public abstract BigDecimal asBigDecimal(BigDecimal defaultValue);

    public JsonNode get(int index) {
        return null;
    }

    public JsonNode get(String fieldName) {
        return null;
    }

    public boolean has(int index) {
        return get(index) != null;
    }

    public boolean has(String fieldName) {
        return get(fieldName) != null;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<String> fieldNames() {
        return java.util.Collections.emptyIterator();
    }

    public Iterator<JsonNode> elements() {
        return java.util.Collections.emptyIterator();
    }

    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return java.util.Collections.emptyIterator();
    }

    public String toJson() {
        return JsonUtils.toJson(this);
    }

    public String toPrettyJson() {
        return JsonUtils.toPrettyJson(this);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
