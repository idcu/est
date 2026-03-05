package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectNode extends JsonNode {

    private final Map<String, JsonNode> children;

    public ObjectNode() {
        this.children = new LinkedHashMap<>();
    }

    public ObjectNode(Map<String, JsonNode> children) {
        this.children = new LinkedHashMap<>(children);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.OBJECT;
    }

    @Override
    public String asText() {
        return "";
    }

    @Override
    public int asInt(int defaultValue) {
        return defaultValue;
    }

    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }

    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        return defaultValue;
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        return defaultValue;
    }

    @Override
    public JsonNode get(String fieldName) {
        return children.get(fieldName);
    }

    @Override
    public boolean has(String fieldName) {
        return children.containsKey(fieldName);
    }

    @Override
    public int size() {
        return children.size();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public Iterator<String> fieldNames() {
        return children.keySet().iterator();
    }

    @Override
    public Iterator<JsonNode> elements() {
        return children.values().iterator();
    }

    @Override
    public Iterator<Map.Entry<String, JsonNode>> fields() {
        return children.entrySet().iterator();
    }

    public ObjectNode set(String fieldName, JsonNode value) {
        if (value == null) {
            value = NullNode.getInstance();
        }
        children.put(fieldName, value);
        return this;
    }

    public ObjectNode set(String fieldName, String value) {
        return set(fieldName, value != null ? new TextNode(value) : NullNode.getInstance());
    }

    public ObjectNode set(String fieldName, int value) {
        return set(fieldName, new IntNode(value));
    }

    public ObjectNode set(String fieldName, long value) {
        return set(fieldName, new LongNode(value));
    }

    public ObjectNode set(String fieldName, double value) {
        return set(fieldName, new DoubleNode(value));
    }

    public ObjectNode set(String fieldName, boolean value) {
        return set(fieldName, BooleanNode.valueOf(value));
    }

    public ObjectNode set(String fieldName, BigInteger value) {
        return set(fieldName, value != null ? new BigIntegerNode(value) : NullNode.getInstance());
    }

    public ObjectNode set(String fieldName, BigDecimal value) {
        return set(fieldName, value != null ? new DecimalNode(value) : NullNode.getInstance());
    }

    public ObjectNode setNull(String fieldName) {
        return set(fieldName, NullNode.getInstance());
    }

    public ObjectNode remove(String fieldName) {
        children.remove(fieldName);
        return this;
    }

    public ObjectNode removeAll() {
        children.clear();
        return this;
    }

    public Map<String, JsonNode> getChildren() {
        return new LinkedHashMap<>(children);
    }
}
