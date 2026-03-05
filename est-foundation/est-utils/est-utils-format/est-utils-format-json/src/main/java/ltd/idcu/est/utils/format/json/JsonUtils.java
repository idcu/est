package ltd.idcu.est.utils.format.json;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class JsonUtils {

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Pattern JSON_STRING_PATTERN = Pattern.compile("^\"(?:[^\"\\\\]|\\\\.)*\"$");
    private static final Pattern JSON_NUMBER_PATTERN = Pattern.compile("^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$");
    private static final Pattern JSON_BOOLEAN_PATTERN = Pattern.compile("^(true|false)$");
    private static final Pattern JSON_NULL_PATTERN = Pattern.compile("^null$");

    private static final Map<Class<?>, List<FieldInfo>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ThreadLocal<DateTimeFormatter> DATE_TIME_FORMATTER = 
        ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private static final ThreadLocal<DateTimeFormatter> DATE_FORMATTER = 
        ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_LOCAL_DATE);
    private static final ThreadLocal<DateTimeFormatter> TIME_FORMATTER = 
        ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_LOCAL_TIME);

    private static final Map<Class<?>, JsonSerializer<?>> SERIALIZERS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, JsonDeserializer<?>> DESERIALIZERS = new ConcurrentHashMap<>();
    private static final java.util.List<JsonModule> REGISTERED_MODULES = new java.util.concurrent.CopyOnWriteArrayList<>();

    private static final ThreadLocal<Set<Integer>> SERIALIZATION_STACK = 
        ThreadLocal.withInitial(HashSet::new);
    
    private static final ThreadLocal<StringBuilder> STRING_BUILDER_CACHE = 
        ThreadLocal.withInitial(() -> new StringBuilder(256));
    
    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

    private JsonUtils() {
    }
    
    public static JsonReader createReader(String json) {
        return new DefaultJsonReader(json);
    }
    
    public static JsonReader createReader(Reader reader) {
        return new DefaultJsonReader(reader);
    }
    
    public static JsonReader createReader(char[] chars) {
        return new DefaultJsonReader(chars, 0, chars.length);
    }
    
    public static JsonReader createReader(char[] chars, int offset, int length) {
        return new DefaultJsonReader(chars, offset, length);
    }
    
    public static Object parse(char[] chars) {
        if (chars == null || chars.length == 0) {
            return null;
        }
        return parse(chars, 0, chars.length);
    }
    
    public static Object parse(char[] chars, int offset, int length) {
        if (chars == null || length <= 0) {
            return null;
        }
        JsonTreeParser parser = new JsonTreeParser(chars, offset, length);
        return treeToValue(parser.parse());
    }
    
    public static JsonNode parseTree(char[] chars) {
        if (chars == null || chars.length == 0) {
            return NullNode.getInstance();
        }
        return parseTree(chars, 0, chars.length);
    }
    
    public static JsonNode parseTree(char[] chars, int offset, int length) {
        if (chars == null || length <= 0) {
            return NullNode.getInstance();
        }
        JsonTreeParser parser = new JsonTreeParser(chars, offset, length);
        return parser.parse();
    }
    
    public static JsonWriter createWriter(Writer writer) {
        return new DefaultJsonWriter(writer);
    }
    
    public static JsonWriter createWriter(Writer writer, boolean prettyPrint) {
        DefaultJsonWriter jsonWriter = new DefaultJsonWriter(writer);
        if (prettyPrint) {
            jsonWriter.setIndentEnabled(true);
        }
        return jsonWriter;
    }
    
    public static JsonWriter createWriter(Writer writer, String indent) {
        DefaultJsonWriter jsonWriter = new DefaultJsonWriter(writer);
        jsonWriter.setIndent(indent);
        return jsonWriter;
    }

    public static <T> void registerSerializer(Class<T> type, JsonSerializer<T> serializer) {
        SERIALIZERS.put(type, serializer);
    }

    public static <T> void registerDeserializer(Class<T> type, JsonDeserializer<T> deserializer) {
        DESERIALIZERS.put(type, deserializer);
    }

    public static void clearSerializers() {
        SERIALIZERS.clear();
    }

    public static void clearDeserializers() {
        DESERIALIZERS.clear();
    }
    
    public static void registerModule(JsonModule module) {
        if (!REGISTERED_MODULES.contains(module)) {
            REGISTERED_MODULES.add(module);
            module.setupModule(new JsonModule.SetupContext() {
                @Override
                public <T> void addSerializer(Class<T> type, JsonSerializer<T> serializer) {
                    registerSerializer(type, serializer);
                }
                
                @Override
                public <T> void addDeserializer(Class<T> type, JsonDeserializer<T> deserializer) {
                    registerDeserializer(type, deserializer);
                }
            });
        }
    }
    
    public static java.util.List<JsonModule> getRegisteredModules() {
        return java.util.Collections.unmodifiableList(REGISTERED_MODULES);
    }
    
    public static void clearModules() {
        REGISTERED_MODULES.clear();
    }

    public static void setDateTimeFormatter(DateTimeFormatter formatter) {
        DATE_TIME_FORMATTER.set(formatter);
    }

    public static void setDateFormatter(DateTimeFormatter formatter) {
        DATE_FORMATTER.set(formatter);
    }

    public static void setTimeFormatter(DateTimeFormatter formatter) {
        TIME_FORMATTER.set(formatter);
    }

    private static final ThreadLocal<Class<?>> CURRENT_VIEW = new ThreadLocal<>();
    
    public static void setView(Class<?> view) {
        CURRENT_VIEW.set(view);
    }
    
    public static void clearView() {
        CURRENT_VIEW.remove();
    }
    
    private static Class<?> getCurrentView() {
        return CURRENT_VIEW.get();
    }
    
    private static class ClassInfo {
        final Class<?> clazz;
        final List<FieldInfo> fields;
        final java.lang.reflect.Method anyGetter;
        final java.lang.reflect.Method anySetter;
        final java.lang.reflect.Method jsonValueMethod;
        final java.lang.reflect.Constructor<?> jsonCreatorConstructor;
        final java.lang.reflect.Method jsonCreatorMethod;
        
        ClassInfo(Class<?> clazz) {
            this.clazz = clazz;
            this.fields = getSerializableFields(clazz);
            this.anyGetter = findAnyGetter(clazz);
            this.anySetter = findAnySetter(clazz);
            this.jsonValueMethod = findJsonValueMethod(clazz);
            this.jsonCreatorConstructor = findJsonCreatorConstructor(clazz);
            this.jsonCreatorMethod = findJsonCreatorMethod(clazz);
        }
        
        private java.lang.reflect.Method findAnyGetter(Class<?> clazz) {
            for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(JsonAnyGetter.class) && 
                    method.getParameterCount() == 0) {
                    method.setAccessible(true);
                    return method;
                }
            }
            return null;
        }
        
        private java.lang.reflect.Method findAnySetter(Class<?> clazz) {
            for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(JsonAnySetter.class) && 
                    method.getParameterCount() == 2) {
                    method.setAccessible(true);
                    return method;
                }
            }
            return null;
        }
        
        private java.lang.reflect.Method findJsonValueMethod(Class<?> clazz) {
            for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(JsonValue.class) && 
                    method.getParameterCount() == 0) {
                    method.setAccessible(true);
                    return method;
                }
            }
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(JsonValue.class)) {
                    return null;
                }
            }
            return null;
        }
        
        private java.lang.reflect.Constructor<?> findJsonCreatorConstructor(Class<?> clazz) {
            for (java.lang.reflect.Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                if (constructor.isAnnotationPresent(JsonCreator.class)) {
                    constructor.setAccessible(true);
                    return constructor;
                }
            }
            return null;
        }
        
        private java.lang.reflect.Method findJsonCreatorMethod(Class<?> clazz) {
            for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(JsonCreator.class) && 
                    java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    method.setAccessible(true);
                    return method;
                }
            }
            return null;
        }
    }
    
    private static final Map<Class<?>, ClassInfo> CLASS_INFO_CACHE = new ConcurrentHashMap<>();
    
    private static ClassInfo getClassInfo(Class<?> clazz) {
        return CLASS_INFO_CACHE.computeIfAbsent(clazz, ClassInfo::new);
    }
    
    private static class FieldInfo {
        final String name;
        final String jsonName;
        final Field field;
        final JsonInclude.Include include;
        final String formatPattern;
        final Object defaultValue;
        final Class<?>[] views;
        final boolean rawValue;
        final boolean jsonValueField;

        FieldInfo(Field field, Class<?> declaringClass) {
            this.field = field;
            this.field.setAccessible(true);
            
            JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
            this.jsonName = (jsonProperty != null && !jsonProperty.value().isEmpty()) 
                ? jsonProperty.value() 
                : field.getName();
            this.name = field.getName();
            
            JsonInclude jsonInclude = field.getAnnotation(JsonInclude.class);
            if (jsonInclude == null) {
                jsonInclude = declaringClass.getAnnotation(JsonInclude.class);
            }
            this.include = (jsonInclude != null) ? jsonInclude.value() : JsonInclude.Include.ALWAYS;
            
            JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
            this.formatPattern = (jsonFormat != null) ? jsonFormat.pattern() : null;
            
            JsonView jsonView = field.getAnnotation(JsonView.class);
            this.views = (jsonView != null) ? jsonView.value() : null;
            
            JsonRawValue jsonRawValue = field.getAnnotation(JsonRawValue.class);
            this.rawValue = (jsonRawValue != null) && jsonRawValue.value();
            
            JsonValue jsonValue = field.getAnnotation(JsonValue.class);
            this.jsonValueField = (jsonValue != null) && jsonValue.value();
            
            this.defaultValue = getDefaultValue(field.getType());
        }
        
        boolean shouldIncludeForView() {
            if (views == null || views.length == 0) {
                return true;
            }
            Class<?> currentView = getCurrentView();
            if (currentView == null) {
                return true;
            }
            for (Class<?> view : views) {
                if (view.isAssignableFrom(currentView)) {
                    return true;
                }
            }
            return false;
        }

        private Object getDefaultValue(Class<?> type) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\u0000';
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0.0f;
            if (type == double.class) return 0.0d;
            return null;
        }

        boolean shouldInclude(Object value) {
            switch (include) {
                case ALWAYS:
                    return true;
                case NON_NULL:
                    return value != null;
                case NON_EMPTY:
                    return value != null && !isEmpty(value);
                case NON_DEFAULT:
                    return value != null && !Objects.equals(value, defaultValue);
                default:
                    return true;
            }
        }

        private boolean isEmpty(Object value) {
            if (value instanceof String) return ((String) value).isEmpty();
            if (value instanceof java.util.Collection) return ((java.util.Collection<?>) value).isEmpty();
            if (value instanceof Map) return ((Map<?, ?>) value).isEmpty();
            if (value.getClass().isArray()) return java.lang.reflect.Array.getLength(value) == 0;
            return false;
        }
    }

    private static List<FieldInfo> getSerializableFields(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, c -> {
            List<FieldInfo> fields = new ArrayList<>();
            Class<?> current = c;
            while (current != null && current != Object.class) {
                for (Field field : current.getDeclaredFields()) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isStatic(modifiers) 
                        && !Modifier.isTransient(modifiers)
                        && !field.isAnnotationPresent(JsonIgnore.class)) {
                        fields.add(new FieldInfo(field, c));
                    }
                }
                current = current.getSuperclass();
            }
            
            JsonPropertyOrder propertyOrder = c.getAnnotation(JsonPropertyOrder.class);
            if (propertyOrder != null) {
                String[] order = propertyOrder.value();
                if (order != null && order.length > 0) {
                    Map<String, FieldInfo> fieldMap = new HashMap<>();
                    for (FieldInfo field : fields) {
                        fieldMap.put(field.jsonName, field);
                    }
                    List<FieldInfo> orderedFields = new ArrayList<>();
                    for (String name : order) {
                        FieldInfo field = fieldMap.get(name);
                        if (field != null) {
                            orderedFields.add(field);
                            fieldMap.remove(name);
                        }
                    }
                    orderedFields.addAll(fieldMap.values());
                    fields = orderedFields;
                } else if (propertyOrder.alphabetic()) {
                    fields.sort(Comparator.comparing(f -> f.jsonName));
                }
            } else {
                fields.sort(Comparator.comparing(f -> f.jsonName));
            }
            
            return fields;
        });
    }

    private static boolean isJavaBean(Object obj) {
        if (obj == null) return false;
        Class<?> clazz = obj.getClass();
        return !clazz.isPrimitive() 
            && !clazz.isArray() 
            && !clazz.isEnum()
            && !Map.class.isAssignableFrom(clazz)
            && !Collection.class.isAssignableFrom(clazz)
            && !Iterable.class.isAssignableFrom(clazz)
            && !Number.class.isAssignableFrom(clazz)
            && !String.class.equals(clazz)
            && !Boolean.class.equals(clazz)
            && !Character.class.equals(clazz)
            && !LocalDateTime.class.equals(clazz)
            && !LocalDate.class.equals(clazz)
            && !LocalTime.class.equals(clazz)
            && !Date.class.equals(clazz);
    }

    private static String toJsonObject(Object obj) {
        ClassInfo classInfo = getClassInfo(obj.getClass());
        
        if (classInfo.jsonValueMethod != null) {
            try {
                Object value = classInfo.jsonValueMethod.invoke(obj);
                return toJsonValue(value);
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke @JsonValue method", e);
            }
        }
        
        for (FieldInfo fieldInfo : classInfo.fields) {
            if (fieldInfo.jsonValueField) {
                try {
                    Object value = fieldInfo.field.get(obj);
                    return toJsonValue(value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access @JsonValue field", e);
                }
            }
        }
        
        StringBuilder sb = new StringBuilder(256);
        sb.append('{');
        boolean first = true;
        
        JsonTypeInfo typeInfo = obj.getClass().getAnnotation(JsonTypeInfo.class);
        if (typeInfo != null && typeInfo.use() != JsonTypeInfo.Id.NONE) {
            String typeProperty = typeInfo.property();
            String typeValue = getTypeValue(obj.getClass(), typeInfo);
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(escapeString(typeProperty));
            sb.append(':');
            sb.append(escapeString(typeValue));
        }
        
        for (FieldInfo fieldInfo : classInfo.fields) {
            try {
                if (!fieldInfo.shouldIncludeForView()) {
                    continue;
                }
                Object value = fieldInfo.field.get(obj);
                if (fieldInfo.shouldInclude(value)) {
                    if (!first) {
                        sb.append(',');
                    }
                    first = false;
                    sb.append(escapeString(fieldInfo.jsonName));
                    sb.append(':');
                    if (fieldInfo.rawValue && value instanceof String) {
                        sb.append((String) value);
                    } else {
                        sb.append(toJsonValueWithFormat(value, fieldInfo.formatPattern));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + fieldInfo.name, e);
            }
        }
        
        if (classInfo.anyGetter != null) {
            try {
                Object anyValue = classInfo.anyGetter.invoke(obj);
                if (anyValue instanceof Map) {
                    Map<?, ?> anyMap = (Map<?, ?>) anyValue;
                    for (Map.Entry<?, ?> entry : anyMap.entrySet()) {
                    if (!first) {
                            sb.append(',');
                        }
                        first = false;
                        sb.append(escapeString(String.valueOf(entry.getKey())));
                        sb.append(':');
                        sb.append(toJsonValue(entry.getValue()));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke @JsonAnyGetter method", e);
            }
        }
        
        sb.append('}');
        return sb.toString();
    }
    
    private static String getTypeValue(Class<?> clazz, JsonTypeInfo typeInfo) {
        switch (typeInfo.use()) {
            case CLASS:
                return clazz.getName();
            case MINIMAL_CLASS:
                return clazz.getSimpleName();
            case NAME:
                JsonTypeName typeName = clazz.getAnnotation(JsonTypeName.class);
                if (typeName != null && !typeName.value().isEmpty()) {
                    return typeName.value();
                }
                return clazz.getSimpleName();
            default:
                return clazz.getName();
        }
    }

    private static String toJsonValueWithFormat(Object obj, String formatPattern) {
        if (obj == null) {
            return "null";
        }
        if (formatPattern != null && !formatPattern.isEmpty()) {
            DateTimeFormatter formatter = FORMATTER_CACHE.computeIfAbsent(formatPattern, 
                DateTimeFormatter::ofPattern);
            if (obj instanceof LocalDateTime) {
                return escapeString(formatter.format((LocalDateTime) obj));
            }
            if (obj instanceof LocalDate) {
                return escapeString(formatter.format((LocalDate) obj));
            }
            if (obj instanceof LocalTime) {
                return escapeString(formatter.format((LocalTime) obj));
            }
        }
        return toJsonValue(obj);
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        Set<Integer> stack = SERIALIZATION_STACK.get();
        try {
            return toJsonValue(obj);
        } finally {
            stack.clear();
            clearView();
        }
    }
    
    public static String toJson(Object obj, Class<?> view) {
        setView(view);
        return toJson(obj);
    }

    @SuppressWarnings("unchecked")
    private static String toJsonValue(Object obj) {
        if (obj == null) {
            return "null";
        }

        JsonSerializer<Object> serializer = (JsonSerializer<Object>) SERIALIZERS.get(obj.getClass());
        if (serializer != null) {
            return serializer.serialize(obj);
        }

        if (obj instanceof JsonNode) {
            return toJsonNode((JsonNode) obj);
        }

        int identityHash = System.identityHashCode(obj);
        Set<Integer> stack = SERIALIZATION_STACK.get();
        if (stack.contains(identityHash)) {
            return "null";
        }
        stack.add(identityHash);
        try {
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
            if (obj instanceof LocalDateTime) {
                return escapeString(DATE_TIME_FORMATTER.get().format((LocalDateTime) obj));
            }
            if (obj instanceof LocalDate) {
                return escapeString(DATE_FORMATTER.get().format((LocalDate) obj));
            }
            if (obj instanceof LocalTime) {
                return escapeString(TIME_FORMATTER.get().format((LocalTime) obj));
            }
            if (obj instanceof Date) {
                return escapeString(obj.toString());
            }
            if (obj instanceof Map) {
                return toJsonMap((Map<?, ?>) obj);
            }
            if (obj instanceof java.util.Collection) {
                return toJsonArray((java.util.Collection<?>) obj);
            }
            if (obj instanceof Iterable) {
                return toJsonArrayFromIterable((Iterable<?>) obj);
            }
            if (obj.getClass().isArray()) {
                return toJsonArrayFromArray(obj);
            }
            if (isJavaBean(obj)) {
                return toJsonObject(obj);
            }
            return escapeString(obj.toString());
        } finally {
            stack.remove(identityHash);
        }
    }
    
    private static String toJsonNode(JsonNode node) {
        if (node == null || node.isNull()) {
            return "null";
        }
        switch (node.getNodeType()) {
            case OBJECT:
                return toJsonObjectNode((ObjectNode) node);
            case ARRAY:
                return toJsonArrayNode((ArrayNode) node);
            case STRING:
                return escapeString(node.asText());
            case NUMBER:
            case BOOLEAN:
                return node.asText();
            case NULL:
            default:
                return "null";
        }
    }
    
    private static String toJsonObjectNode(ObjectNode node) {
        StringBuilder sb = new StringBuilder(256);
        sb.append('{');
        boolean first = true;
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(escapeString(field.getKey()));
            sb.append(':');
            sb.append(toJsonNode(field.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }
    
    private static String toJsonArrayNode(ArrayNode node) {
        StringBuilder sb = new StringBuilder(256);
        sb.append('[');
        boolean first = true;
        Iterator<JsonNode> elements = node.elements();
        while (elements.hasNext()) {
            JsonNode element = elements.next();
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append(toJsonNode(element));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String toJsonMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder(256);
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
    
    private static String toJsonArray(java.util.Collection<?> collection) {
        StringBuilder sb = new StringBuilder(256);
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
    
    private static String toJsonArrayFromIterable(Iterable<?> iterable) {
        StringBuilder sb = new StringBuilder(256);
        sb.append('[');
        boolean first = true;
        for (Object item : iterable) {
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
        StringBuilder sb = new StringBuilder(256);
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
        StringBuilder sb = STRING_BUILDER_CACHE.get();
        sb.setLength(0);
        sb.ensureCapacity(str.length() + 20);
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
                        sb.append("\\u");
                        String hex = Integer.toHexString(c);
                        for (int j = hex.length(); j < 4; j++) {
                            sb.append('0');
                        }
                        sb.append(hex);
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
    
    public static JsonNode parseTree(String json) {
        if (json == null || json.trim().isEmpty()) {
            return NullNode.getInstance();
        }
        JsonTreeParser parser = new JsonTreeParser(json.trim());
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

    public static <T> T parseObject(String json, Class<T> clazz) {
        Object result = parse(json);
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) result;
            return mapToObject(map, clazz);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> parseArray(String json, Class<T> elementClass) {
        Object result = parse(json);
        if (result instanceof List) {
            List<Object> list = (List<Object>) result;
            List<T> typedList = new ArrayList<>();
            for (Object item : list) {
                typedList.add(convertValue(item, elementClass));
            }
            return typedList;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String json, TypeReference<T> typeRef) {
        Object result = parse(json);
        java.lang.reflect.Type type = typeRef.getType();
        if (type instanceof Class<?>) {
            return (T) convertValue(result, (Class<T>) type);
        }
        if (type instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType paramType = (java.lang.reflect.ParameterizedType) type;
            java.lang.reflect.Type rawType = paramType.getRawType();
            if (rawType == List.class && result instanceof List) {
                java.lang.reflect.Type[] typeArgs = paramType.getActualTypeArguments();
                if (typeArgs.length == 1 && typeArgs[0] instanceof Class<?>) {
                    List<Object> list = (List<Object>) result;
                    List<Object> typedList = new ArrayList<>();
                    Class<?> elementClass = (Class<?>) typeArgs[0];
                    for (Object item : list) {
                        typedList.add(convertValue(item, elementClass));
                    }
                    return (T) typedList;
                }
            }
        }
        return (T) result;
    }

    @SuppressWarnings("unchecked")
    private static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            Class<?> actualClass = resolveActualClass(map, clazz);
            ClassInfo classInfo = getClassInfo(actualClass);
            
            T obj;
            
            if (classInfo.jsonCreatorConstructor != null) {
                java.lang.reflect.Parameter[] params = classInfo.jsonCreatorConstructor.getParameters();
                Object[] args = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    java.lang.reflect.Parameter param = params[i];
                    JsonProperty jsonProperty = param.getAnnotation(JsonProperty.class);
                    String paramName = (jsonProperty != null) ? jsonProperty.value() : param.getName();
                    Object value = map.get(paramName);
                    if (value != null) {
                        args[i] = convertValue(value, param.getType());
                    } else {
                        args[i] = null;
                    }
                }
                obj = (T) classInfo.jsonCreatorConstructor.newInstance(args);
            } else if (classInfo.jsonCreatorMethod != null) {
                java.lang.reflect.Parameter[] params = classInfo.jsonCreatorMethod.getParameters();
                Object[] args = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    java.lang.reflect.Parameter param = params[i];
                    JsonProperty jsonProperty = param.getAnnotation(JsonProperty.class);
                    String paramName = (jsonProperty != null) ? jsonProperty.value() : param.getName();
                    Object value = map.get(paramName);
                    if (value != null) {
                        args[i] = convertValue(value, param.getType());
                    } else {
                        args[i] = null;
                    }
                }
                obj = (T) classInfo.jsonCreatorMethod.invoke(null, args);
            } else {
                Constructor<?> constructor = actualClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                obj = (T) constructor.newInstance();
            }
            
            Set<String> processedKeys = new HashSet<>();
            
            for (FieldInfo fieldInfo : classInfo.fields) {
                Object value = map.get(fieldInfo.jsonName);
                if (value == null) {
                    value = map.get(fieldInfo.name);
                }
                if (value != null) {
                    Object convertedValue = convertValueWithFormat(value, fieldInfo.field.getType(), fieldInfo.formatPattern);
                    fieldInfo.field.set(obj, convertedValue);
                    processedKeys.add(fieldInfo.jsonName);
                    processedKeys.add(fieldInfo.name);
                }
            }
            
            if (classInfo.anySetter != null) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (!processedKeys.contains(entry.getKey())) {
                        classInfo.anySetter.invoke(obj, entry.getKey(), entry.getValue());
                    }
                }
            }
            
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize to " + clazz.getName(), e);
        }
    }
    
    private static Class<?> resolveActualClass(Map<String, Object> map, Class<?> baseClass) {
        JsonTypeInfo typeInfo = baseClass.getAnnotation(JsonTypeInfo.class);
        if (typeInfo == null || typeInfo.use() == JsonTypeInfo.Id.NONE) {
            return baseClass;
        }
        
        String typeProperty = typeInfo.property();
        Object typeValueObj = map.get(typeProperty);
        if (typeValueObj == null) {
            return baseClass;
        }
        
        String typeValue = String.valueOf(typeValueObj);
        
        JsonSubTypes subTypes = baseClass.getAnnotation(JsonSubTypes.class);
        if (subTypes != null) {
            for (JsonSubTypes.Type subType : subTypes.value()) {
                String subTypeName = subType.name();
                if (subTypeName.isEmpty()) {
                    JsonTypeName jsonTypeName = subType.value().getAnnotation(JsonTypeName.class);
                    if (jsonTypeName != null) {
                        subTypeName = jsonTypeName.value();
                    }
                    if (subTypeName.isEmpty()) {
                        subTypeName = subType.value().getSimpleName();
                    }
                }
                
                switch (typeInfo.use()) {
                    case CLASS:
                        if (subType.value().getName().equals(typeValue)) {
                            return subType.value();
                        }
                        break;
                    case MINIMAL_CLASS:
                        if (subType.value().getSimpleName().equals(typeValue)) {
                            return subType.value();
                        }
                        break;
                    case NAME:
                        if (subTypeName.equals(typeValue)) {
                            return subType.value();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        
        try {
            if (typeInfo.use() == JsonTypeInfo.Id.CLASS) {
                return Class.forName(typeValue);
            }
        } catch (ClassNotFoundException e) {
        }
        
        return baseClass;
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertValueWithFormat(Object value, Class<T> targetType, String formatPattern) {
        if (value == null) {
            return null;
        }
        if (formatPattern != null && !formatPattern.isEmpty()) {
            DateTimeFormatter formatter = FORMATTER_CACHE.computeIfAbsent(formatPattern, 
                DateTimeFormatter::ofPattern);
            if (targetType == LocalDateTime.class && value instanceof String) {
                return (T) LocalDateTime.parse((String) value, formatter);
            }
            if (targetType == LocalDate.class && value instanceof String) {
                return (T) LocalDate.parse((String) value, formatter);
            }
            if (targetType == LocalTime.class && value instanceof String) {
                return (T) LocalTime.parse((String) value, formatter);
            }
        }
        return convertValue(value, targetType);
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertValue(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }
        JsonDeserializer<T> deserializer = (JsonDeserializer<T>) DESERIALIZERS.get(targetType);
        if (deserializer != null) {
            return deserializer.deserialize(value);
        }
        if (targetType.isInstance(value)) {
            return (T) value;
        }
        if (targetType == String.class) {
            return (T) String.valueOf(value);
        }
        if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return (T) Integer.valueOf(((Number) value).intValue());
            }
            if (value instanceof String) {
                return (T) Integer.valueOf((String) value);
            }
        }
        if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return (T) Long.valueOf(((Number) value).longValue());
            }
            if (value instanceof String) {
                return (T) Long.valueOf((String) value);
            }
        }
        if (targetType == Double.class || targetType == double.class) {
            if (value instanceof Number) {
                return (T) Double.valueOf(((Number) value).doubleValue());
            }
            if (value instanceof String) {
                return (T) Double.valueOf((String) value);
            }
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return (T) value;
            }
            if (value instanceof String) {
                return (T) Boolean.valueOf((String) value);
            }
        }
        if (targetType == LocalDateTime.class && value instanceof String) {
            return (T) LocalDateTime.parse((String) value, DATE_TIME_FORMATTER.get());
        }
        if (targetType == LocalDate.class && value instanceof String) {
            return (T) LocalDate.parse((String) value, DATE_FORMATTER.get());
        }
        if (targetType == LocalTime.class && value instanceof String) {
            return (T) LocalTime.parse((String) value, TIME_FORMATTER.get());
        }
        if (Map.class.isAssignableFrom(targetType) && value instanceof Map) {
            return (T) value;
        }
        if (List.class.isAssignableFrom(targetType) && value instanceof List) {
            return (T) value;
        }
        if (value instanceof Map) {
            return mapToObject((Map<String, Object>) value, targetType);
        }
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to " + targetType);
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
    
    private static Object treeToValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        switch (node.getNodeType()) {
            case OBJECT:
                Map<String, Object> map = new LinkedHashMap<>();
                for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    map.put(entry.getKey(), treeToValue(entry.getValue()));
                }
                return map;
            case ARRAY:
                List<Object> list = new ArrayList<>();
                for (Iterator<JsonNode> it = node.elements(); it.hasNext(); ) {
                    list.add(treeToValue(it.next()));
                }
                return list;
            case STRING:
                return node.asText();
            case NUMBER:
                String numText = node.asText();
                if (numText.contains(".") || numText.toLowerCase().contains("e")) {
                    return node.asDouble();
                }
                try {
                    long longValue = Long.parseLong(numText);
                    if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                        return (int) longValue;
                    }
                    return longValue;
                } catch (NumberFormatException e) {
                    return node.asDouble();
                }
            case BOOLEAN:
                return node.asBoolean();
            case NULL:
            default:
                return null;
        }
    }
    
    private static class JsonTreeParser {
        private final char[] chars;
        private final int offset;
        private final int length;
        private int pos;

        JsonTreeParser(String json) {
            this.chars = json.toCharArray();
            this.offset = 0;
            this.length = chars.length;
            this.pos = 0;
        }
        
        JsonTreeParser(char[] chars, int offset, int length) {
            this.chars = chars;
            this.offset = offset;
            this.length = length;
            this.pos = offset;
        }

        JsonNode parse() {
            skipWhitespace();
            if (pos >= offset + length) {
                return NullNode.getInstance();
            }
            char c = chars[pos];
            if (c == '{') {
                return parseObject();
            } else if (c == '[') {
                return parseArray();
            } else if (c == '"') {
                return parseString();
            } else if (c == '-' || Character.isDigit(c)) {
                return parseNumber();
            } else if (pos + 3 < offset + length && chars[pos + 1] == 'r' && chars[pos + 2] == 'u' && chars[pos + 3] == 'e') {
                pos += 4;
                return BooleanNode.valueOf(true);
            } else if (pos + 4 < offset + length && chars[pos + 1] == 'a' && chars[pos + 2] == 'l' && chars[pos + 3] == 's' && chars[pos + 4] == 'e') {
                pos += 5;
                return BooleanNode.valueOf(false);
            } else if (pos + 3 < offset + length && chars[pos + 1] == 'u' && chars[pos + 2] == 'l' && chars[pos + 3] == 'l') {
                pos += 4;
                return NullNode.getInstance();
            }
            throw new RuntimeException("Unexpected character at position " + pos + ": " + c);
        }

        private ObjectNode parseObject() {
            ObjectNode node = new ObjectNode();
            pos++;
            skipWhitespace();

            if (pos < offset + length && chars[pos] == '}') {
                pos++;
                return node;
            }

            while (pos < offset + length) {
                skipWhitespace();
                String key = parseStringValue();
                skipWhitespace();
                if (pos >= offset + length || chars[pos] != ':') {
                    throw new RuntimeException("Expected ':' at position " + pos);
                }
                pos++;
                skipWhitespace();
                JsonNode value = parse();
                node.set(key, value);
                skipWhitespace();

                if (pos >= offset + length) {
                    break;
                }

                char c = chars[pos];
                if (c == '}') {
                    pos++;
                    break;
                } else if (c == ',') {
                    pos++;
                } else {
                    throw new RuntimeException("Expected ',' or '}' at position " + pos);
                }
            }
            return node;
        }

        private ArrayNode parseArray() {
            ArrayNode node = new ArrayNode();
            pos++;
            skipWhitespace();

            if (pos < offset + length && chars[pos] == ']') {
                pos++;
                return node;
            }

            while (pos < offset + length) {
                skipWhitespace();
                JsonNode value = parse();
                node.add(value);
                skipWhitespace();

                if (pos >= offset + length) {
                    break;
                }

                char c = chars[pos];
                if (c == ']') {
                    pos++;
                    break;
                } else if (c == ',') {
                    pos++;
                } else {
                    throw new RuntimeException("Expected ',' or ']' at position " + pos);
                }
            }
            return node;
        }

        private TextNode parseString() {
            return new TextNode(parseStringValue());
        }
        
        private String parseStringValue() {
            if (chars[pos] != '"') {
                throw new RuntimeException("Expected '\"' at position " + pos);
            }
            pos++;
            int start = pos;
            boolean hasEscape = false;
            while (pos < offset + length) {
                char c = chars[pos];
                if (c == '"') {
                    break;
                }
                if (c == '\\') {
                    hasEscape = true;
                }
                pos++;
            }
            int end = pos;
            pos++;
            if (!hasEscape) {
                return new String(chars, start, end - start);
            }
            StringBuilder sb = new StringBuilder(end - start);
            int i = start;
            while (i < end) {
                char c = chars[i];
                if (c == '\\') {
                    i++;
                    if (i >= end) {
                        throw new RuntimeException("Unexpected end of string");
                    }
                    char escaped = chars[i];
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
                            if (i + 4 >= end) {
                                throw new RuntimeException("Invalid unicode escape");
                            }
                            String hex = new String(chars, i + 1, 4);
                            sb.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        }
                        default -> throw new RuntimeException("Invalid escape character: " + escaped);
                    }
                } else {
                    sb.append(c);
                }
                i++;
            }
            return sb.toString();
        }

        private JsonNode parseNumber() {
            int start = pos;
            boolean isDecimal = false;
            if (chars[pos] == '-') {
                pos++;
            }
            while (pos < offset + length && (Character.isDigit(chars[pos]) || chars[pos] == '.' ||
                    chars[pos] == 'e' || chars[pos] == 'E' ||
                    chars[pos] == '+' || chars[pos] == '-')) {
                if (chars[pos] == '.' || chars[pos] == 'e' || chars[pos] == 'E') {
                    isDecimal = true;
                }
                pos++;
            }
            String numStr = new String(chars, start, pos - start);
            if (isDecimal) {
                return new DoubleNode(Double.parseDouble(numStr));
            }
            try {
                long longValue = Long.parseLong(numStr);
                if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                    return new IntNode((int) longValue);
                }
                return new LongNode(longValue);
            } catch (NumberFormatException e) {
                return new DoubleNode(Double.parseDouble(numStr));
            }
        }

        private void skipWhitespace() {
            while (pos < offset + length && Character.isWhitespace(chars[pos])) {
                pos++;
            }
        }
    }
    
    public static JsonNode readPath(JsonNode root, String path) {
        return JsonPath.compile(path).read(root);
    }
    
    public static void setPath(JsonNode root, String path, JsonNode value) {
        JsonPath.compile(path).set(root, value);
    }
    
    public static void deletePath(JsonNode root, String path) {
        JsonPath.compile(path).delete(root);
    }
    
    public static JsonNode readPath(String json, String path) {
        JsonNode root = parseTree(json);
        return readPath(root, path);
    }
    
    public static String setPath(String json, String path, JsonNode value) {
        JsonNode root = parseTree(json);
        setPath(root, path, value);
        return toJson(root);
    }
    
    public static String deletePath(String json, String path) {
        JsonNode root = parseTree(json);
        deletePath(root, path);
        return toJson(root);
    }
    
    public static JsonNode applyMergePatch(JsonNode target, JsonNode patch) {
        return JsonMergePatch.apply(target, patch);
    }
    
    public static JsonNode applyMergePatch(String targetJson, String patchJson) {
        return JsonMergePatch.apply(targetJson, patchJson);
    }
    
    public static String applyMergePatchToString(String targetJson, String patchJson) {
        return JsonMergePatch.applyToString(targetJson, patchJson);
    }
    
    public static JsonNode createMergePatch(JsonNode source, JsonNode target) {
        return JsonMergePatch.createDiff(source, target);
    }
    
    public static JsonNode createMergePatch(String sourceJson, String targetJson) {
        return JsonMergePatch.createDiff(sourceJson, targetJson);
    }
    
    public static String createMergePatchToString(String sourceJson, String targetJson) {
        return JsonMergePatch.createDiffToString(sourceJson, targetJson);
    }
}
