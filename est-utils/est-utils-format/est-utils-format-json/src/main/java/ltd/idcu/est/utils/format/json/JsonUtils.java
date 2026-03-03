package ltd.idcu.est.utils.format.json;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public final class JsonUtils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("^\"(?:[^\"\\\\]|\\\\.)*\"$");
    private static final Pattern JSON_NUMBER_PATTERN = Pattern.compile("^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$");
    private static final Pattern JSON_BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
    private static final Pattern JSON_NULL_PATTERN = Pattern.compile("^null$");

    private JsonUtils() {
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        return toJsonValue(obj);
    }

    private static String toJsonValue(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof String) {
            return escapeString((String) obj);
        }
        if (obj instanceof Number) {
            return obj.toString();
        }
        if (obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Character) {
            return escapeString(obj.toString());
        }
        if (obj instanceof Map) {
            return toJsonMap((Map<?, ?>) obj);
        }
        if (obj instanceof Collection) {
            return toJsonArray((Collection<?>) obj);
        }
        if (obj.getClass().isArray()) {
            return toJsonArrayFromArray(obj);
        }
        return escapeString(obj.toString());
    }

    private static String toJsonMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(escapeString(String.valueOf(entry.getKey())));
            sb.append(':');
            sb.append(toJsonValue(entry.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }

    private static String toJsonArray(Collection<?> collection) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(toJsonValue(item));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String toJsonArrayFromArray(Object array) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int length = java.lang.reflect.Array.getLength(array);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(toJsonValue(java.lang.reflect.Array.get(array, i)));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String escapeString(String str) {
        if (str == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\b' -> sb.append("\\b");
                case '\f' -> sb.append("\\f");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static String toPrettyJson(Object obj) {
        return toPrettyJson(obj, 2);
    }

    public static String toPrettyJson(Object obj, int indent) {
        String json = toJson(obj);
        return prettyPrint(json, indent);
    }

    private static String prettyPrint(String json, int indent) {
        if (json == null || json.isEmpty()) {
            return json;
        }
        StringBuilder result = new StringBuilder();
        String indentStr = " ".repeat(indent);
        int level = 0;
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escaped) {
                escaped = false;
                result.append(c);
                continue;
            }

            if (c == '\\' && inString) {
                escaped = true;
                result.append(c);
                continue;
            }

            if (c == '"') {
                inString = !inString;
                result.append(c);
                continue;
            }

            if (inString) {
                result.append(c);
                continue;
            }

            switch (c) {
                case '{', '[' -> {
                    result.append(c);
                    result.append('\n');
                    level++;
                    result.append(indentStr.repeat(level));
                }
                case '}', ']' -> {
                    result.append('\n');
                    level--;
                    result.append(indentStr.repeat(level));
                    result.append(c);
                }
                case ',' -> {
                    result.append(c);
                    result.append('\n');
                    result.append(indentStr.repeat(level));
                }
                case ':' -> {
                    result.append(c);
                    result.append(' ');
                }
                case ' ', '\t', '\n', '\r' -> {
                }
                default -> result.append(c);
            }
        }
        return result.toString();
    }

    public static Object parse(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        JsonParser parser = new JsonParser(json.trim());
        return parser.parse();
    }

    public static Map<String, Object> parseObject(String json) {
        Object result = parse(json);
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) result;
            return map;
        }
        return null;
    }

    public static List<Object> parseArray(String json) {
        Object result = parse(json);
        if (result instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) result;
            return list;
        }
        return null;
    }

    public static String getString(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        return value != null ? String.valueOf(value) : null;
    }

    public static String getString(Map<String, Object> map, String key, String defaultValue) {
        String value = getString(map, key);
        return value != null ? value : defaultValue;
    }

    public static Integer getInteger(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static int getInt(Map<String, Object> map, String key, int defaultValue) {
        Integer value = getInteger(map, key);
        return value != null ? value : defaultValue;
    }

    public static Long getLong(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static long getLong(Map<String, Object> map, String key, long defaultValue) {
        Long value = getLong(map, key);
        return value != null ? value : defaultValue;
    }

    public static Double getDouble(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static double getDouble(Map<String, Object> map, String key, double defaultValue) {
        Double value = getDouble(map, key);
        return value != null ? value : defaultValue;
    }

    public static Boolean getBoolean(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }

    public static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        Boolean value = getBoolean(map, key);
        return value != null ? value : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof Map) {
            return (Map<String, Object>) value;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getList(Map<String, Object> map, String key) {
        if (map == null || key == null) {
            return null;
        }
        Object value = map.get(key);
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return null;
    }

    public static boolean isValidJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return false;
        }
        try {
            parse(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isJsonObject(String json) {
        if (json == null) {
            return false;
        }
        String trimmed = json.trim();
        return trimmed.startsWith("{") && trimmed.endsWith("}");
    }

    public static boolean isJsonArray(String json) {
        if (json == null) {
            return false;
        }
        String trimmed = json.trim();
        return trimmed.startsWith("[") && trimmed.endsWith("]");
    }

    public static String compact(String json) {
        if (json == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean inString = false;
        boolean escaped = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escaped) {
                escaped = false;
                result.append(c);
                continue;
            }

            if (c == '\\' && inString) {
                escaped = true;
                result.append(c);
                continue;
            }

            if (c == '"') {
                inString = !inString;
                result.append(c);
                continue;
            }

            if (inString) {
                result.append(c);
                continue;
            }

            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                continue;
            }

            result.append(c);
        }
        return result.toString();
    }

    private static class JsonParser {
        private final String json;
        private int pos;

        JsonParser(String json) {
            this.json = json;
            this.pos = 0;
        }

        Object parse() {
            skipWhitespace();
            if (pos >= json.length()) {
                return null;
            }
            char c = json.charAt(pos);
            if (c == '{') {
                return parseObject();
            } else if (c == '[') {
                return parseArray();
            } else if (c == '"') {
                return parseString();
            } else if (c == '-' || Character.isDigit(c)) {
                return parseNumber();
            } else if (json.startsWith("true", pos)) {
                pos += 4;
                return true;
            } else if (json.startsWith("false", pos)) {
                pos += 5;
                return false;
            } else if (json.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw new RuntimeException("Unexpected character at position " + pos + ": " + c);
        }

        private Map<String, Object> parseObject() {
            Map<String, Object> map = new LinkedHashMap<>();
            pos++;
            skipWhitespace();

            if (pos < json.length() && json.charAt(pos) == '}') {
                pos++;
                return map;
            }

            while (pos < json.length()) {
                skipWhitespace();
                String key = parseString();
                skipWhitespace();
                if (pos >= json.length() || json.charAt(pos) != ':') {
                    throw new RuntimeException("Expected ':' at position " + pos);
                }
                pos++;
                skipWhitespace();
                Object value = parse();
                map.put(key, value);
                skipWhitespace();

                if (pos >= json.length()) {
                    break;
                }

                char c = json.charAt(pos);
                if (c == '}') {
                    pos++;
                    break;
                } else if (c == ',') {
                    pos++;
                } else {
                    throw new RuntimeException("Expected ',' or '}' at position " + pos);
                }
            }
            return map;
        }

        private List<Object> parseArray() {
            List<Object> list = new ArrayList<>();
            pos++;
            skipWhitespace();

            if (pos < json.length() && json.charAt(pos) == ']') {
                pos++;
                return list;
            }

            while (pos < json.length()) {
                skipWhitespace();
                Object value = parse();
                list.add(value);
                skipWhitespace();

                if (pos >= json.length()) {
                    break;
                }

                char c = json.charAt(pos);
                if (c == ']') {
                    pos++;
                    break;
                } else if (c == ',') {
                    pos++;
                } else {
                    throw new RuntimeException("Expected ',' or ']' at position " + pos);
                }
            }
            return list;
        }

        private String parseString() {
            if (json.charAt(pos) != '"') {
                throw new RuntimeException("Expected '\"' at position " + pos);
            }
            pos++;
            StringBuilder sb = new StringBuilder();

            while (pos < json.length()) {
                char c = json.charAt(pos);
                if (c == '"') {
                    pos++;
                    return sb.toString();
                } else if (c == '\\') {
                    pos++;
                    if (pos >= json.length()) {
                        throw new RuntimeException("Unexpected end of string");
                    }
                    char escaped = json.charAt(pos);
                    switch (escaped) {
                        case '"' -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/' -> sb.append('/');
                        case 'b' -> sb.append('\b');
                        case 'f' -> sb.append('\f');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 't' -> sb.append('\t');
                        case 'u' -> {
                            if (pos + 4 >= json.length()) {
                                throw new RuntimeException("Invalid unicode escape");
                            }
                            String hex = json.substring(pos + 1, pos + 5);
                            sb.append((char) Integer.parseInt(hex, 16));
                            pos += 4;
                        }
                        default -> throw new RuntimeException("Invalid escape character: " + escaped);
                    }
                    pos++;
                } else {
                    sb.append(c);
                    pos++;
                }
            }
            throw new RuntimeException("Unterminated string");
        }

        private Number parseNumber() {
            int start = pos;
            if (json.charAt(pos) == '-') {
                pos++;
            }
            while (pos < json.length() && (Character.isDigit(json.charAt(pos)) || json.charAt(pos) == '.' ||
                    json.charAt(pos) == 'e' || json.charAt(pos) == 'E' ||
                    json.charAt(pos) == '+' || json.charAt(pos) == '-')) {
                pos++;
            }
            String numStr = json.substring(start, pos);
            if (numStr.contains(".") || numStr.contains("e") || numStr.contains("E")) {
                return Double.parseDouble(numStr);
            }
            long longValue = Long.parseLong(numStr);
            if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                return (int) longValue;
            }
            return longValue;
        }

        private void skipWhitespace() {
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
        }
    }
}
