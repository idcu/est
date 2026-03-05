package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerNode extends JsonNode {

    private final BigInteger value;

    public BigIntegerNode(BigInteger value) {
        this.value = value;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NUMBER;
    }

    @Override
    public String asText() {
        return value != null ? value.toString() : "0";
    }

    @Override
    public int asInt(int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.intValue();
    }

    @Override
    public long asLong(long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.longValue();
    }

    @Override
    public double asDouble(double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.doubleValue();
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return value.signum() != 0;
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        return value != null ? new BigDecimal(value) : defaultValue;
    }

    public BigInteger getValue() {
        return value;
    }
}
