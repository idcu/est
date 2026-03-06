package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TextNode extends JsonNode {

    private final String value;

    public TextNode(String value) {
        this.value = value;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.STRING;
    }

    @Override
    public String asText() {
        return value != null ? value : "";
    }

    @Override
    public int asInt(int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public long asLong(long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public double asDouble(double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public boolean asBoolean(boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    @Override
    public BigInteger asBigInteger(BigInteger defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return new BigInteger(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public BigDecimal asBigDecimal(BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getValue() {
        return value;
    }
}
