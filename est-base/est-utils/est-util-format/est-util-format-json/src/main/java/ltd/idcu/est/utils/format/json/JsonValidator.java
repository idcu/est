package ltd.idcu.est.utils.format.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonValidator {
    
    private final List<String> errors = new ArrayList<>();
    
    public JsonValidator() {
    }
    
    public boolean validate(String json) {
        errors.clear();
        try {
            Object parsed = JsonUtils.parse(json);
            return validateValue(parsed, "");
        } catch (Exception e) {
            errors.add("Invalid JSON: " + e.getMessage());
            return false;
        }
    }
    
    private boolean validateValue(Object value, String path) {
        if (value == null) {
            return true;
        }
        
        if (value instanceof Map) {
            return validateObject((Map<?, ?>) value, path);
        } else if (value instanceof List) {
            return validateArray((List<?>) value, path);
        } else if (value instanceof String || 
                   value instanceof Number || 
                   value instanceof Boolean) {
            return true;
        }
        
        errors.add("Unsupported type at " + path + ": " + value.getClass());
        return false;
    }
    
    private boolean validateObject(Map<?, ?> map, String path) {
        boolean valid = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey() != null ? entry.getKey().toString() : "null";
            String newPath = path.isEmpty() ? key : path + "." + key;
            
            if (entry.getKey() == null) {
                errors.add("Null key at " + newPath);
                valid = false;
            }
            
            if (!validateValue(entry.getValue(), newPath)) {
                valid = false;
            }
        }
        return valid;
    }
    
    private boolean validateArray(List<?> list, String path) {
        boolean valid = true;
        for (int i = 0; i < list.size(); i++) {
            String newPath = path + "[" + i + "]";
            if (!validateValue(list.get(i), newPath)) {
                valid = false;
            }
        }
        return valid;
    }
    
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
    
    public void clear() {
        errors.clear();
    }
    
    public static boolean isValidJson(String json) {
        return new JsonValidator().validate(json);
    }
}
