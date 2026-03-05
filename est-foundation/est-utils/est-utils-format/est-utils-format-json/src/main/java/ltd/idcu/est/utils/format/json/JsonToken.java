package ltd.idcu.est.utils.format.json;

public enum JsonToken {
    START_OBJECT,
    END_OBJECT,
    START_ARRAY,
    END_ARRAY,
    FIELD_NAME,
    VALUE_STRING,
    VALUE_NUMBER,
    VALUE_TRUE,
    VALUE_FALSE,
    VALUE_NULL,
    END_OF_INPUT
}
