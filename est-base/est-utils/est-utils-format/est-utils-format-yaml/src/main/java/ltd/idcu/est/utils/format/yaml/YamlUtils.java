package ltd.idcu.est.utils.format.yaml;

import java.util.*;

public final class YamlUtils {

    private static final String INDENT = "  ";

    private YamlUtils() {
    }

    public static String toYaml(Object obj) {
        if (obj == null) {
            return "null\n";
        }
        StringBuilder sb = new StringBuilder();
        toYamlValue(obj, sb, 0);
        return sb.toString();
    }

    private static void toYamlValue(Object obj, StringBuilder sb, int level) {
        if (obj == null) {
            sb.append("null\n");
            return;
        }

        if (obj instanceof Map) {
            toYamlMap((Map<?, ?>) obj, sb, level);
        } else if (obj instanceof Collection) {
            toYamlList((Collection<?>) obj, sb, level);
        } else if (obj.getClass().isArray()) {
            toYamlArray(obj, sb, level);
        } else if (obj instanceof String) {
            String str = (String) obj;
            if (str.contains("\n") || str.contains(":") || str.contains("#") ||
                    str.startsWith("'") || str.startsWith("\"") ||
                    str.startsWith("- ") || str.startsWith("[") || str.startsWith("{")) {
                sb.append(quoteString(str)).append("\n");
            } else if (str.isEmpty() || str.equals("null") || str.equals("true") || str.equals("false") ||
                    looksLikeNumber(str)) {
                sb.append(quoteString(str)).append("\n");
            } else {
                sb.append(str).append("\n");
            }
        } else if (obj instanceof Number || obj instanceof Boolean) {
            sb.append(obj).append("\n");
        } else {
            sb.append(quoteString(obj.toString())).append("\n");
        }
    }

    private static void toYamlMap(Map<?, ?> map, StringBuilder sb, int level) {
        if (map.isEmpty()) {
            sb.append("{}\n");
            return;
        }

        String indentStr = INDENT.repeat(level);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();

            sb.append(indentStr);

            if (key.contains(":") || key.contains("#") || key.contains("\n") ||
                    key.startsWith("'") || key.startsWith("\"")) {
                sb.append(quoteString(key));
            } else {
                sb.append(key);
            }

            sb.append(":");

            if (value == null) {
                sb.append(" null\n");
            } else if (value instanceof Map) {
                Map<?, ?> childMap = (Map<?, ?>) value;
                if (childMap.isEmpty()) {
                    sb.append(" {}\n");
                } else {
                    sb.append("\n");
                    toYamlMap(childMap, sb, level + 1);
                }
            } else if (value instanceof Collection) {
                Collection<?> childList = (Collection<?>) value;
                if (childList.isEmpty()) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    toYamlList(childList, sb, level + 1);
                }
            } else if (value.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(value);
                if (length == 0) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    toYamlArray(value, sb, level + 1);
                }
            } else if (isSimpleValue(value)) {
                sb.append(" ");
                toYamlValue(value, sb, 0);
            } else {
                sb.append("\n");
                toYamlValue(value, sb, level + 1);
            }
        }
    }

    private static void toYamlList(Collection<?> list, StringBuilder sb, int level) {
        if (list.isEmpty()) {
            sb.append("[]\n");
            return;
        }

        String indentStr = INDENT.repeat(level);
        for (Object item : list) {
            sb.append(indentStr).append("-");

            if (item == null) {
                sb.append(" null\n");
            } else if (item instanceof Map) {
                Map<?, ?> childMap = (Map<?, ?>) item;
                if (childMap.isEmpty()) {
                    sb.append(" {}\n");
                } else {
                    sb.append("\n");
                    toYamlMap(childMap, sb, level + 1);
                }
            } else if (item instanceof Collection) {
                Collection<?> childList = (Collection<?>) item;
                if (childList.isEmpty()) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    toYamlList(childList, sb, level + 1);
                }
            } else if (item.getClass().isArray()) {
                int length = java.lang.reflect.Array.getLength(item);
                if (length == 0) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    toYamlArray(item, sb, level + 1);
                }
            } else if (isSimpleValue(item)) {
                sb.append(" ");
                toYamlValue(item, sb, 0);
            } else {
                sb.append("\n");
                toYamlValue(item, sb, level + 1);
            }
        }
    }

    private static void toYamlArray(Object array, StringBuilder sb, int level) {
        int length = java.lang.reflect.Array.getLength(array);
        if (length == 0) {
            sb.append("[]\n");
            return;
        }

        String indentStr = INDENT.repeat(level);
        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(array, i);
            sb.append(indentStr).append("-");

            if (item == null) {
                sb.append(" null\n");
            } else if (item instanceof Map) {
                Map<?, ?> childMap = (Map<?, ?>) item;
                if (childMap.isEmpty()) {
                    sb.append(" {}\n");
                } else {
                    sb.append("\n");
                    toYamlMap(childMap, sb, level + 1);
                }
            } else if (item instanceof Collection) {
                Collection<?> childList = (Collection<?>) item;
                if (childList.isEmpty()) {
                    sb.append(" []\n");
                } else {
                    sb.append("\n");
                    toYamlList(childList, sb, level + 1);
                }
            } else if (isSimpleValue(item)) {
                sb.append(" ");
                toYamlValue(item, sb, 0);
            } else {
                sb.append("\n");
                toYamlValue(item, sb, level + 1);
            }
        }
    }

    private static boolean isSimpleValue(Object value) {
        if (value == null) {
            return true;
        }
        return value instanceof String || value instanceof Number || value instanceof Boolean;
    }

    private static String quoteString(String str) {
        if (str.contains("'")) {
            return "\"" + str.replace("\"", "\\\"") + "\"";
        }
        return "'" + str + "'";
    }

    private static boolean looksLikeNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Map<String, Object> parse(String yaml) {
        if (yaml == null || yaml.trim().isEmpty()) {
            return new LinkedHashMap<>();
        }
        YamlParser parser = new YamlParser(yaml);
        return parser.parse();
    }

    public static List<Map<String, Object>> parseDocuments(String yaml) {
        if (yaml == null || yaml.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> documents = new ArrayList<>();
        String[] docs = yaml.split("\n---\\s*\n");
        for (String doc : docs) {
            if (!doc.trim().isEmpty()) {
                documents.add(parse(doc));
            }
        }
        return documents;
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

    private static class YamlParser {
        private final String[] lines;
        private int currentLine;
        private final int totalLines;

        YamlParser(String yaml) {
            this.lines = yaml.split("\n");
            this.totalLines = this.lines.length;
            this.currentLine = 0;
        }

        Map<String, Object> parse() {
            Map<String, Object> result = new LinkedHashMap<>();
            while (currentLine < totalLines) {
                String line = lines[currentLine];
                if (isBlankOrComment(line)) {
                    currentLine++;
                    continue;
                }
                if (line.trim().equals("---") || line.trim().equals("...")) {
                    currentLine++;
                    continue;
                }
                parseValue(result, getIndent(line));
            }
            return result;
        }

        private void parseValue(Map<String, Object> parent, int baseIndent) {
            while (currentLine < totalLines) {
                String line = lines[currentLine];
                if (isBlankOrComment(line)) {
                    currentLine++;
                    continue;
                }

                int indent = getIndent(line);
                if (indent < baseIndent) {
                    break;
                }

                String trimmed = line.trim();

                if (trimmed.startsWith("- ")) {
                    parseListItem(parent, baseIndent);
                } else if (trimmed.contains(":")) {
                    parseMapEntry(parent, baseIndent);
                } else {
                    currentLine++;
                }
            }
        }

        private void parseMapEntry(Map<String, Object> parent, int baseIndent) {
            String line = lines[currentLine];
            String trimmed = line.trim();

            int colonIndex = findColon(trimmed);
            if (colonIndex == -1) {
                currentLine++;
                return;
            }

            String key = parseKey(trimmed.substring(0, colonIndex));
            String valueStr = trimmed.substring(colonIndex + 1).trim();

            currentLine++;

            Object value;
            if (valueStr.isEmpty()) {
                if (currentLine < totalLines) {
                    String nextLine = lines[currentLine];
                    if (!isBlankOrComment(nextLine)) {
                        int nextIndent = getIndent(nextLine);
                        if (nextIndent > baseIndent) {
                            if (nextLine.trim().startsWith("- ")) {
                                List<Object> list = new ArrayList<>();
                                parseListItems(list, nextIndent);
                                value = list;
                            } else {
                                Map<String, Object> nestedMap = new LinkedHashMap<>();
                                parseValue(nestedMap, nextIndent);
                                value = nestedMap;
                            }
                        } else {
                            value = null;
                        }
                    } else {
                        value = null;
                    }
                } else {
                    value = null;
                }
            } else {
                value = parseScalar(valueStr);
            }

            parent.put(key, value);
        }

        private void parseListItem(Map<String, Object> parent, int baseIndent) {
            List<Object> list = new ArrayList<>();
            parseListItems(list, baseIndent);
            if (!list.isEmpty()) {
                String firstLine = lines[currentLine - 1];
                if (firstLine != null) {
                    parent.put("_list", list);
                }
            }
        }

        private void parseListItems(List<Object> list, int baseIndent) {
            while (currentLine < totalLines) {
                String line = lines[currentLine];
                if (isBlankOrComment(line)) {
                    currentLine++;
                    continue;
                }

                int indent = getIndent(line);
                if (indent < baseIndent) {
                    break;
                }

                String trimmed = line.trim();
                if (!trimmed.startsWith("-")) {
                    break;
                }

                if (indent > baseIndent) {
                    break;
                }

                String itemStr = trimmed.substring(1).trim();
                currentLine++;

                Object item;
                if (itemStr.isEmpty()) {
                    if (currentLine < totalLines) {
                        String nextLine = lines[currentLine];
                        if (!isBlankOrComment(nextLine)) {
                            int nextIndent = getIndent(nextLine);
                            if (nextIndent > indent) {
                                if (nextLine.trim().startsWith("- ")) {
                                    List<Object> nestedList = new ArrayList<>();
                                    parseListItems(nestedList, nextIndent);
                                    item = nestedList;
                                } else {
                                    Map<String, Object> nestedMap = new LinkedHashMap<>();
                                    parseValue(nestedMap, nextIndent);
                                    item = nestedMap;
                                }
                            } else {
                                item = null;
                            }
                        } else {
                            item = null;
                        }
                    } else {
                        item = null;
                    }
                } else if (itemStr.contains(":") && !itemStr.startsWith("'") && !itemStr.startsWith("\"")) {
                    int colonIndex = findColon(itemStr);
                    if (colonIndex != -1) {
                        Map<String, Object> inlineMap = new LinkedHashMap<>();
                        String key = parseKey(itemStr.substring(0, colonIndex));
                        String val = itemStr.substring(colonIndex + 1).trim();
                        inlineMap.put(key, parseScalar(val));

                        if (currentLine < totalLines) {
                            String nextLine = lines[currentLine];
                            if (!isBlankOrComment(nextLine)) {
                                int nextIndent = getIndent(nextLine);
                                if (nextIndent > indent) {
                                    parseValue(inlineMap, nextIndent);
                                }
                            }
                        }
                        item = inlineMap;
                    } else {
                        item = parseScalar(itemStr);
                    }
                } else {
                    item = parseScalar(itemStr);
                }

                list.add(item);
            }
        }

        private Object parseScalar(String value) {
            if (value == null || value.isEmpty() || value.equals("~") || value.equals("null")) {
                return null;
            }
            if (value.equals("true")) {
                return true;
            }
            if (value.equals("false")) {
                return false;
            }
            if (value.equals(".nan")) {
                return Double.NaN;
            }
            if (value.equals(".inf") || value.equals("+.inf")) {
                return Double.POSITIVE_INFINITY;
            }
            if (value.equals("-.inf")) {
                return Double.NEGATIVE_INFINITY;
            }

            if ((value.startsWith("'") && value.endsWith("'")) ||
                    (value.startsWith("\"") && value.endsWith("\""))) {
                return unquoteString(value);
            }

            try {
                if (value.contains(".") || value.contains("e") || value.contains("E")) {
                    return Double.parseDouble(value);
                }
                long longValue = Long.parseLong(value);
                if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                    return (int) longValue;
                }
                return longValue;
            } catch (NumberFormatException e) {
                return value;
            }
        }

        private String parseKey(String key) {
            key = key.trim();
            if ((key.startsWith("'") && key.endsWith("'")) ||
                    (key.startsWith("\"") && key.endsWith("\""))) {
                return unquoteString(key);
            }
            return key;
        }

        private String unquoteString(String str) {
            if (str.length() < 2) {
                return str;
            }
            char quote = str.charAt(0);
            if (str.charAt(str.length() - 1) != quote) {
                return str;
            }
            String content = str.substring(1, str.length() - 1);
            if (quote == '"') {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < content.length(); i++) {
                    char c = content.charAt(i);
                    if (c == '\\' && i + 1 < content.length()) {
                        char next = content.charAt(i + 1);
                        switch (next) {
                            case 'n' -> sb.append('\n');
                            case 't' -> sb.append('\t');
                            case 'r' -> sb.append('\r');
                            case '\\' -> sb.append('\\');
                            case '"' -> sb.append('"');
                            default -> {
                                sb.append(c);
                                sb.append(next);
                            }
                        }
                        i++;
                    } else {
                        sb.append(c);
                    }
                }
                return sb.toString();
            }
            return content.replace("''", "'");
        }

        private int findColon(String str) {
            boolean inSingleQuote = false;
            boolean inDoubleQuote = false;
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '\'' && !inDoubleQuote) {
                    inSingleQuote = !inSingleQuote;
                } else if (c == '"' && !inSingleQuote) {
                    inDoubleQuote = !inDoubleQuote;
                } else if (c == ':' && !inSingleQuote && !inDoubleQuote) {
                    return i;
                }
            }
            return -1;
        }

        private int getIndent(String line) {
            int indent = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ' ') {
                    indent++;
                } else if (line.charAt(i) == '\t') {
                    indent += 2;
                } else {
                    break;
                }
            }
            return indent;
        }

        private boolean isBlankOrComment(String line) {
            String trimmed = line.trim();
            return trimmed.isEmpty() || trimmed.startsWith("#");
        }
    }
}
