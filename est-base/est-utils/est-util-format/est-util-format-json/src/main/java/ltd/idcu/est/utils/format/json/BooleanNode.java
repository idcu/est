package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BooleanNode extends JsonNode {

    static final BooleanNode TRUE = new BooleanNode(true);
    static final BooleanNode FALSE = new BooleanNode(false);

    private final boolean value;

    private BooleanNode(boolean value) {
        this.value = value;
    }

    public static BooleanNode valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.BOOLEAN;
    }

    @Override
    public String asText() {
        return Boolean.toString(value);
    }

    @Override
    public int asInt(int defaultValue) {
        return value ? 1 : 0;
    }

    @Override
    public long asLong(long defaultValue) {
        return value ? 1L : 0L;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value ? 1.0 : 0.0;
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        return value;
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        return value ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        return value ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    public boolean getValue() {
        return value;
    }
}
