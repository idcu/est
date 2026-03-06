package ltd.idcu.est.utils.format.json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class JsonSchema {
    
    private final JsonNode schemaNode;
    private final Map<String, JsonNode> definitions;
    
    public JsonSchema(String schemaJson) {
        this(JsonUtils.parseTree(schemaJson));
    }
    
    public JsonSchema(JsonNode schemaNode) {
        this.schemaNode = schemaNode;
        this.definitions = new HashMap<>();
        if (schemaNode.has("$defs")) {
            JsonNode defs = schemaNode.get("$defs");
            if (defs.isObject()) {
                for (java.util.Iterator<Map.Entry<String, JsonNode>> it = defs.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    definitions.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if (schemaNode.has("definitions")) {
            JsonNode defs = schemaNode.get("definitions");
            if (defs.isObject()) {
                for (java.util.Iterator<Map.Entry<String, JsonNode>> it = defs.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    definitions.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
    
    public boolean validate(String json) {
        return validate(JsonUtils.parseTree(json));
    }
    
    public boolean validate(JsonNode instance) {
        try {
            validate(instance, schemaNode);
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }
    
    public List<String> validateAndGetErrors(String json) {
        return validateAndGetErrors(JsonUtils.parseTree(json));
    }
    
    public List<String> validateAndGetErrors(JsonNode instance) {
        List<String> errors = new ArrayList<>();
        validate(instance, schemaNode, "", errors);
        return errors;
    }
    
    private void validate(JsonNode instance, JsonNode schema) throws ValidationException {
        validate(instance, schema, "", new ArrayList<>());
    }
    
    private void validate(JsonNode instance, JsonNode schema, String path, List<String> errors) {
        if (schema.has("$ref")) {
            String ref = schema.get("$ref").asText();
            if (ref.startsWith("#/$defs/")) {
                String defName = ref.substring("#/$defs/".length());
                JsonNode defSchema = definitions.get(defName);
                if (defSchema != null) {
                    validate(instance, defSchema, path, errors);
                    return;
                }
            } else if (ref.startsWith("#/definitions/")) {
                String defName = ref.substring("#/definitions/".length());
                JsonNode defSchema = definitions.get(defName);
                if (defSchema != null) {
                    validate(instance, defSchema, path, errors);
                    return;
                }
            }
            errors.add(path + ": Unsupported reference: " + ref);
            return;
        }
        
        if (schema.has("type")) {
            JsonNode typeNode = schema.get("type");
            if (typeNode.isArray()) {
                boolean match = false;
                for (java.util.Iterator<JsonNode> it = typeNode.elements(); it.hasNext(); ) {
                    JsonNode t = it.next();
                    if (checkType(instance, t.asText())) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    errors.add(path + ": Type mismatch");
                }
            } else {
                if (!checkType(instance, typeNode.asText())) {
                    errors.add(path + ": Type mismatch, expected " + typeNode.asText());
                }
            }
        }
        
        if (schema.has("enum")) {
            JsonNode enumNode = schema.get("enum");
            if (enumNode.isArray()) {
                boolean match = false;
                for (java.util.Iterator<JsonNode> it = enumNode.elements(); it.hasNext(); ) {
                    JsonNode enumValue = it.next();
                    if (nodeEquals(instance, enumValue)) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    errors.add(path + ": Value not in enum");
                }
            }
        }
        
        if (schema.has("const")) {
            JsonNode constNode = schema.get("const");
            if (!nodeEquals(instance, constNode)) {
                errors.add(path + ": Value does not match const");
            }
        }
        
        if (instance.isObject()) {
            validateObject(instance, schema, path, errors);
        }
        
        if (instance.isArray()) {
            validateArray(instance, schema, path, errors);
        }
        
        if (instance.isNumber()) {
            validateNumber(instance, schema, path, errors);
        }
        
        if (instance.isTextual()) {
            validateString(instance, schema, path, errors);
        }
    }
    
    private boolean checkType(JsonNode node, String type) {
        switch (type) {
            case "object":
                return node.isObject();
            case "array":
                return node.isArray();
            case "string":
                return node.isTextual();
            case "number":
                return node.isNumber();
            case "integer":
                return node.isNumber() && !node.asText().contains(".") && !node.asText().toLowerCase().contains("e");
            case "boolean":
                return node.isBoolean();
            case "null":
                return node.isNull();
            default:
                return false;
        }
    }
    
    private void validateObject(JsonNode instance, JsonNode schema, String path, List<String> errors) {
        if (schema.has("properties")) {
            JsonNode properties = schema.get("properties");
            if (properties.isObject()) {
                for (java.util.Iterator<Map.Entry<String, JsonNode>> it = properties.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    String propName = entry.getKey();
                    JsonNode propSchema = entry.getValue();
                    if (instance.has(propName)) {
                        validate(instance.get(propName), propSchema, path + "/" + propName, errors);
                    }
                }
            }
        }
        
        if (schema.has("required")) {
            JsonNode required = schema.get("required");
            if (required.isArray()) {
                for (java.util.Iterator<JsonNode> it = required.elements(); it.hasNext(); ) {
                    String requiredProp = it.next().asText();
                    if (!instance.has(requiredProp)) {
                        errors.add(path + ": Missing required property '" + requiredProp + "'");
                    }
                }
            }
        }
        
        if (schema.has("minProperties")) {
            int min = schema.get("minProperties").asInt();
            if (instance.size() < min) {
                errors.add(path + ": Too few properties, minimum " + min);
            }
        }
        
        if (schema.has("maxProperties")) {
            int max = schema.get("maxProperties").asInt();
            if (instance.size() > max) {
                errors.add(path + ": Too many properties, maximum " + max);
            }
        }
    }
    
    private void validateArray(JsonNode instance, JsonNode schema, String path, List<String> errors) {
        if (schema.has("items")) {
            JsonNode itemsSchema = schema.get("items");
            for (int i = 0; i < instance.size(); i++) {
                validate(instance.get(i), itemsSchema, path + "[" + i + "]", errors);
            }
        }
        
        if (schema.has("minItems")) {
            int min = schema.get("minItems").asInt();
            if (instance.size() < min) {
                errors.add(path + ": Too few items, minimum " + min);
            }
        }
        
        if (schema.has("maxItems")) {
            int max = schema.get("maxItems").asInt();
            if (instance.size() > max) {
                errors.add(path + ": Too many items, maximum " + max);
            }
        }
        
        if (schema.has("uniqueItems")) {
            boolean unique = schema.get("uniqueItems").asBoolean();
            if (unique) {
                List<String> seen = new ArrayList<>();
                for (int i = 0; i < instance.size(); i++) {
                    String str = instance.get(i).toJson();
                    if (seen.contains(str)) {
                        errors.add(path + ": Duplicate item at index " + i);
                        break;
                    }
                    seen.add(str);
                }
            }
        }
    }
    
    private void validateNumber(JsonNode instance, JsonNode schema, String path, List<String> errors) {
        BigDecimal value = instance.asBigDecimal();
        
        if (schema.has("minimum")) {
            BigDecimal min = schema.get("minimum").asBigDecimal();
            boolean exclusive = schema.has("exclusiveMinimum") && schema.get("exclusiveMinimum").asBoolean();
            if (exclusive ? value.compareTo(min) <= 0 : value.compareTo(min) < 0) {
                errors.add(path + ": Number too small");
            }
        }
        
        if (schema.has("maximum")) {
            BigDecimal max = schema.get("maximum").asBigDecimal();
            boolean exclusive = schema.has("exclusiveMaximum") && schema.get("exclusiveMaximum").asBoolean();
            if (exclusive ? value.compareTo(max) >= 0 : value.compareTo(max) > 0) {
                errors.add(path + ": Number too large");
            }
        }
        
        if (schema.has("multipleOf")) {
            BigDecimal multipleOf = schema.get("multipleOf").asBigDecimal();
            BigDecimal[] result = value.divideAndRemainder(multipleOf);
            if (result[1].compareTo(BigDecimal.ZERO) != 0) {
                errors.add(path + ": Not a multiple of " + multipleOf);
            }
        }
    }
    
    private void validateString(JsonNode instance, JsonNode schema, String path, List<String> errors) {
        String value = instance.asText();
        
        if (schema.has("minLength")) {
            int min = schema.get("minLength").asInt();
            if (value.length() < min) {
                errors.add(path + ": String too short, minimum " + min + " characters");
            }
        }
        
        if (schema.has("maxLength")) {
            int max = schema.get("maxLength").asInt();
            if (value.length() > max) {
                errors.add(path + ": String too long, maximum " + max + " characters");
            }
        }
        
        if (schema.has("pattern")) {
            String pattern = schema.get("pattern").asText();
            try {
                Pattern p = Pattern.compile(pattern);
                if (!p.matcher(value).find()) {
                    errors.add(path + ": String does not match pattern");
                }
            } catch (Exception e) {
                errors.add(path + ": Invalid pattern: " + pattern);
            }
        }
    }
    
    private boolean nodeEquals(JsonNode a, JsonNode b) {
        if (a == b) return true;
        if (a == null || b == null) return false;
        if (a.getNodeType() != b.getNodeType()) return false;
        
        switch (a.getNodeType()) {
            case OBJECT:
                if (a.size() != b.size()) return false;
                for (java.util.Iterator<Map.Entry<String, JsonNode>> it = a.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    JsonNode bVal = b.get(entry.getKey());
                    if (!nodeEquals(entry.getValue(), bVal)) return false;
                }
                return true;
            case ARRAY:
                if (a.size() != b.size()) return false;
                for (int i = 0; i < a.size(); i++) {
                    if (!nodeEquals(a.get(i), b.get(i))) return false;
                }
                return true;
            default:
                return a.asText().equals(b.asText());
        }
    }
    
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
