package ltd.idcu.est.collection.impl;

import ltd.idcu.est.collection.api.Collection;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.yaml.YamlUtils;
import ltd.idcu.est.utils.format.xml.XmlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class CollectionFactory {

    private CollectionFactory() {
    }

    public static <T> Collection<T> fromIterable(Iterable<T> iterable) {
        if (iterable == null) {
            return new DefaultCollection<>();
        }
        if (iterable instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<T> collection = (Collection<T>) iterable;
            return collection;
        }
        return new DefaultCollection<>(iterable);
    }

    public static <T> Collection<T> fromStream(Stream<T> stream) {
        if (stream == null) {
            return new DefaultCollection<>();
        }
        List<T> list = stream.toList();
        return new DefaultCollection<>(list);
    }

    public static <T> Collection<T> singleton(T element) {
        return new SingletonCollection<>(element);
    }

    public static Collection<Object> fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new DefaultCollection<>();
        }
        Object parsed = JsonUtils.parse(json);
        if (parsed instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) parsed;
            return new DefaultCollection<>(list);
        }
        return new DefaultCollection<>();
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> fromJson(String json, Class<T> elementType) {
        if (json == null || json.trim().isEmpty()) {
            return new DefaultCollection<>();
        }
        Object parsed = JsonUtils.parse(json);
        if (parsed instanceof List) {
            List<Object> list = (List<Object>) parsed;
            List<T> result = new ArrayList<>();
            for (Object item : list) {
                result.add(convertToType(item, elementType));
            }
            return new DefaultCollection<>(result);
        }
        return new DefaultCollection<>();
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertToType(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
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
        throw new IllegalArgumentException("Cannot convert " + value.getClass() + " to " + targetType);
    }

    public static Collection<Object> fromYaml(String yaml) {
        if (yaml == null || yaml.trim().isEmpty()) {
            return new DefaultCollection<>();
        }
        List<Map<String, Object>> documents = YamlUtils.parseDocuments(yaml);
        if (documents.isEmpty()) {
            Map<String, Object> parsed = YamlUtils.parse(yaml);
            if (parsed.isEmpty()) {
                return new DefaultCollection<>();
            }
            List<Object> result = new ArrayList<>();
            result.add(parsed);
            return new DefaultCollection<>(result);
        }
        return new DefaultCollection<>(new ArrayList<>(documents));
    }

    public static Collection<Object> fromXml(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            return new DefaultCollection<>();
        }
        XmlUtils.XmlNode root = XmlUtils.parse(xml);
        if (root == null) {
            return new DefaultCollection<>();
        }
        List<Object> result = new ArrayList<>();
        for (XmlUtils.XmlNode child : root.getChildren()) {
            result.add(xmlNodeToObject(child));
        }
        return new DefaultCollection<>(result);
    }

    private static Object xmlNodeToObject(XmlUtils.XmlNode node) {
        if (node.hasTextContent() && !node.hasChildren()) {
            return node.getTextContent();
        }
        Map<String, Object> map = new java.util.LinkedHashMap<>();
        if (node.hasTextContent()) {
            map.put("_text", node.getTextContent());
        }
        for (Map.Entry<String, String> attr : node.getAttributes().entrySet()) {
            map.put("@" + attr.getKey(), attr.getValue());
        }
        Map<String, List<Object>> childrenByName = new java.util.LinkedHashMap<>();
        for (XmlUtils.XmlNode child : node.getChildren()) {
            String childName = child.getName();
            childrenByName.computeIfAbsent(childName, k -> new ArrayList<>())
                    .add(xmlNodeToObject(child));
        }
        for (Map.Entry<String, List<Object>> entry : childrenByName.entrySet()) {
            List<Object> values = entry.getValue();
            if (values.size() == 1) {
                map.put(entry.getKey(), values.get(0));
            } else {
                map.put(entry.getKey(), values);
            }
        }
        return map;
    }

    public static Collection<Integer> range(int start, int end) {
        return range(start, end, 1);
    }

    public static Collection<Integer> range(int start, int end, int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be positive");
        }
        List<Integer> list = new ArrayList<>();
        if (start < end) {
            for (int i = start; i < end; i += step) {
                list.add(i);
            }
        } else if (start > end) {
            for (int i = start; i > end; i -= step) {
                list.add(i);
            }
        }
        return new DefaultCollection<>(list);
    }

    public static Collection<Long> range(long start, long end) {
        return range(start, end, 1L);
    }

    public static Collection<Long> range(long start, long end, long step) {
        if (step <= 0) {
            throw new IllegalArgumentException("Step must be positive");
        }
        List<Long> list = new ArrayList<>();
        if (start < end) {
            for (long i = start; i < end; i += step) {
                list.add(i);
            }
        } else if (start > end) {
            for (long i = start; i > end; i -= step) {
                list.add(i);
            }
        }
        return new DefaultCollection<>(list);
    }

    public static <T> Collection<T> generate(int count, Supplier<T> supplier) {
        if (count <= 0) {
            return new DefaultCollection<>();
        }
        List<T> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(supplier.get());
        }
        return new DefaultCollection<>(list);
    }

    public static <T> Collection<T> repeat(T element, int times) {
        if (times <= 0) {
            return new DefaultCollection<>();
        }
        if (times == 1) {
            return new SingletonCollection<>(element);
        }
        List<T> list = new ArrayList<>(times);
        for (int i = 0; i < times; i++) {
            list.add(element);
        }
        return new DefaultCollection<>(list);
    }
}
