package ltd.idcu.est.utils.format.json;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonPath {
    
    private static final Pattern PATH_PATTERN = Pattern.compile("\\.([^.\\[\\]]+)|\\[([0-9]+)\\]|\\['([^']+)'\\]");
    
    private final String path;
    private final List<PathSegment> segments;
    
    private JsonPath(String path, List<PathSegment> segments) {
        this.path = path;
        this.segments = segments;
    }
    
    public static JsonPath compile(String path) {
        List<PathSegment> segments = parsePath(path);
        return new JsonPath(path, segments);
    }
    
    public JsonNode read(JsonNode root) {
        JsonNode current = root;
        for (PathSegment segment : segments) {
            if (current == null) {
                return null;
            }
            current = segment.apply(current);
        }
        return current;
    }
    
    public void set(JsonNode root, JsonNode value) {
        JsonNode parent = root;
        PathSegment lastSegment = null;
        
        for (int i = 0; i < segments.size() - 1; i++) {
            if (parent == null) {
                return;
            }
            parent = segments.get(i).apply(parent);
        }
        
        if (parent != null && !segments.isEmpty()) {
            lastSegment = segments.get(segments.size() - 1);
            lastSegment.set(parent, value);
        }
    }
    
    public void delete(JsonNode root) {
        JsonNode parent = root;
        PathSegment lastSegment = null;
        
        for (int i = 0; i < segments.size() - 1; i++) {
            if (parent == null) {
                return;
            }
            parent = segments.get(i).apply(parent);
        }
        
        if (parent != null && !segments.isEmpty()) {
            lastSegment = segments.get(segments.size() - 1);
            lastSegment.delete(parent);
        }
    }
    
    private static List<PathSegment> parsePath(String path) {
        List<PathSegment> segments = new ArrayList<>();
        
        if (path.startsWith("$")) {
            path = path.substring(1);
        }
        
        Matcher matcher = PATH_PATTERN.matcher(path);
        int lastEnd = 0;
        
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String between = path.substring(lastEnd, matcher.start()).trim();
                if (!between.isEmpty()) {
                    if (between.startsWith(".")) {
                        segments.add(new PropertySegment(between.substring(1)));
                    }
                }
            }
            
            if (matcher.group(1) != null) {
                segments.add(new PropertySegment(matcher.group(1)));
            } else if (matcher.group(2) != null) {
                segments.add(new IndexSegment(Integer.parseInt(matcher.group(2))));
            } else if (matcher.group(3) != null) {
                segments.add(new PropertySegment(matcher.group(3)));
            }
            
            lastEnd = matcher.end();
        }
        
        if (lastEnd < path.length()) {
            String remaining = path.substring(lastEnd).trim();
            if (!remaining.isEmpty() && remaining.startsWith(".")) {
                segments.add(new PropertySegment(remaining.substring(1)));
            }
        }
        
        return segments;
    }
    
    private interface PathSegment {
        JsonNode apply(JsonNode node);
        void set(JsonNode parent, JsonNode value);
        void delete(JsonNode parent);
    }
    
    private static class PropertySegment implements PathSegment {
        private final String property;
        
        PropertySegment(String property) {
            this.property = property;
        }
        
        @Override
        public JsonNode apply(JsonNode node) {
            if (node instanceof ObjectNode) {
                return ((ObjectNode) node).get(property);
            }
            return null;
        }
        
        @Override
        public void set(JsonNode parent, JsonNode value) {
            if (parent instanceof ObjectNode) {
                ((ObjectNode) parent).set(property, value);
            }
        }
        
        @Override
        public void delete(JsonNode parent) {
            if (parent instanceof ObjectNode) {
                ((ObjectNode) parent).remove(property);
            }
        }
    }
    
    private static class IndexSegment implements PathSegment {
        private final int index;
        
        IndexSegment(int index) {
            this.index = index;
        }
        
        @Override
        public JsonNode apply(JsonNode node) {
            if (node instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) node;
                if (index >= 0 && index < arrayNode.size()) {
                    return arrayNode.get(index);
                }
            }
            return null;
        }
        
        @Override
        public void set(JsonNode parent, JsonNode value) {
            if (parent instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) parent;
                while (arrayNode.size() <= index) {
                    arrayNode.add(NullNode.getInstance());
                }
                arrayNode.set(index, value);
            }
        }
        
        @Override
        public void delete(JsonNode parent) {
            if (parent instanceof ArrayNode) {
                ArrayNode arrayNode = (ArrayNode) parent;
                if (index >= 0 && index < arrayNode.size()) {
                    arrayNode.remove(index);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return path;
    }
}
