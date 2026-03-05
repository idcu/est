package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class IntNode extends JsonNode {

    private final int value;

    public IntNode(int value) {
        this.value = value;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NUMBER;
    }

    @Override
    public String asText() {
        return Integer.toString(value);
    }

    @Override
    public int asInt(int defaultValue) {
        return value;
    }

    @Override
    public long asLong(long defaultValue) {
        return value;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value;
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return value != 0;
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        return BigDecimal.valueOf(value);
    }

    public int getValue() {
        return value;
    }
}
