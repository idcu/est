package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayNode extends JsonNode {

    private final List<JsonNode> children;

    public ArrayNode() {
        this.children = new ArrayList<>();
    }

    public ArrayNode(List<JsonNode> children) {
        this.children = new ArrayList<>(children);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ARRAY;
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
    public JsonNode get(int index) {
        if (index >= 0 && index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    @Override
    public boolean has(int index) {
        return index >= 0 && index < children.size();
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
    public Iterator<JsonNode> elements() {
        return children.iterator();
    }

    public ArrayNode add(JsonNode value) {
        if (value == null) {
            value = NullNode.getInstance();
        }
        children.add(value);
        return this;
    }

    public ArrayNode add(String value) {
        return add(value != null ? new TextNode(value) : NullNode.getInstance());
    }

    public ArrayNode add(int value) {
        return add(new IntNode(value));
    }

    public ArrayNode add(long value) {
        return add(new LongNode(value));
    }

    public ArrayNode add(double value) {
        return add(new DoubleNode(value));
    }

    public ArrayNode add(boolean value) {
        return add(BooleanNode.valueOf(value));
    }

    public ArrayNode add(BigInteger value) {
        return add(value != null ? new BigIntegerNode(value) : NullNode.getInstance());
    }

    public ArrayNode add(BigDecimal value) {
        return add(value != null ? new DecimalNode(value) : NullNode.getInstance());
    }

    public ArrayNode addNull() {
        return add(NullNode.getInstance());
    }

    public ArrayNode set(int index, JsonNode value) {
        if (value == null) {
            value = NullNode.getInstance();
        }
        if (index >= 0 && index < children.size()) {
            children.set(index, value);
        } else if (index == children.size()) {
            children.add(value);
        }
        return this;
    }

    public ArrayNode remove(int index) {
        if (index >= 0 && index < children.size()) {
            children.remove(index);
        }
        return this;
    }

    public ArrayNode removeAll() {
        children.clear();
        return this;
    }

    public List<JsonNode> getChildren() {
        return new ArrayList<>(children);
    }
}
