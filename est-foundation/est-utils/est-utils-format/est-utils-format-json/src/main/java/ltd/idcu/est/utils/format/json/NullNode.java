package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NullNode extends JsonNode {

    static final NullNode INSTANCE = new NullNode();

    private NullNode() {
    }

    public static NullNode getInstance() {
        return INSTANCE;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.NULL;
    }

    @Override
    public String asText() {
        return "null";
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
}
