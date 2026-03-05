package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DoubleNode extends JsonNode {

    private final double value;

    public DoubleNode(double value) {
        this.value = value;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NUMBER;
    }

    @Override
    public String asText() {
        return Double.toString(value);
    }

    @Override
    public int asInt(int defaultValue) {
        return (int) value;
    }

    @Override
    public long asLong(long defaultValue) {
        return (long) value;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value;
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return value != 0.0;
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        return BigDecimal.valueOf(value).toBigInteger();
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        return BigDecimal.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}
