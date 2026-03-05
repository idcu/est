package ltd.idcu.est.utils.format.json;

import java.util.Iterator;
import java.util.Map;

public class JsonMergePatch {
    
    public static JsonNode apply(JsonNode target, JsonNode patch) {
        if (patch.isNull()) {
            return NullNode.getInstance();
        }
        
        if (!target.isObject() || !patch.isObject()) {
            return deepCopy(patch);
        }
        
        ObjectNode result = new ObjectNode();
        
        ObjectNode targetObject = (ObjectNode) target;
        ObjectNode patchObject = (ObjectNode) patch;
        
        Iterator<Map.Entry<String, JsonNode>> targetFields = targetObject.fields();
        while (targetFields.hasNext()) {
            Map.Entry<String, JsonNode> entry = targetFields.next();
            if (!patchObject.has(entry.getKey())) {
                result.set(entry.getKey(), deepCopy(entry.getValue()));
            }
        }
        
        Iterator<Map.Entry<String, JsonNode>> patchFields = patchObject.fields();
        while (patchFields.hasNext()) {
            Map.Entry<String, JsonNode> entry = patchFields.next();
            String fieldName = entry.getKey();
            JsonNode patchValue = entry.getValue();
            
            if (patchValue.isNull()) {
                continue;
            }
            
            JsonNode targetValue = targetObject.get(fieldName);
            if (targetValue != null) {
                result.set(fieldName, apply(targetValue, patchValue));
            } else {
                result.set(fieldName, deepCopy(patchValue));
            }
        }
        
        return result;
    }
    
    public static JsonNode apply(String targetJson, String patchJson) {
        JsonNode target = JsonUtils.parseTree(targetJson);
        JsonNode patch = JsonUtils.parseTree(patchJson);
        return apply(target, patch);
    }
    
    public static String applyToString(String targetJson, String patchJson) {
        JsonNode result = apply(targetJson, patchJson);
        return JsonUtils.toJson(result);
    }
    
    public static JsonNode createDiff(JsonNode source, JsonNode target) {
        if (source.equals(target)) {
            return NullNode.getInstance();
        }
        
        if (!source.isObject() || !target.isObject()) {
            return deepCopy(target);
        }
        
        ObjectNode result = new ObjectNode();
        ObjectNode sourceObject = (ObjectNode) source;
        ObjectNode targetObject = (ObjectNode) target;
        
        Iterator<String> sourceFieldNames = sourceObject.fieldNames();
        while (sourceFieldNames.hasNext()) {
            String fieldName = sourceFieldNames.next();
            if (!targetObject.has(fieldName)) {
                result.setNull(fieldName);
            }
        }
        
        Iterator<Map.Entry<String, JsonNode>> targetFields = targetObject.fields();
        while (targetFields.hasNext()) {
            Map.Entry<String, JsonNode> entry = targetFields.next();
            String fieldName = entry.getKey();
            JsonNode targetValue = entry.getValue();
            JsonNode sourceValue = sourceObject.get(fieldName);
            
            if (sourceValue == null) {
                result.set(fieldName, deepCopy(targetValue));
            } else {
                JsonNode diff = createDiff(sourceValue, targetValue);
                if (!diff.isNull()) {
                    result.set(fieldName, diff);
                }
            }
        }
        
        return result;
    }
    
    public static JsonNode createDiff(String sourceJson, String targetJson) {
        JsonNode source = JsonUtils.parseTree(sourceJson);
        JsonNode target = JsonUtils.parseTree(targetJson);
        return createDiff(source, target);
    }
    
    public static String createDiffToString(String sourceJson, String targetJson) {
        JsonNode diff = createDiff(sourceJson, targetJson);
        return JsonUtils.toJson(diff);
    }
    
    private static JsonNode deepCopy(JsonNode node) {
        if (node == null || node.isNull()) {
            return NullNode.getInstance();
        }
        
        switch (node.getNodeType()) {
            case OBJECT:
                ObjectNode objectNode = new ObjectNode();
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    objectNode.set(entry.getKey(), deepCopy(entry.getValue()));
                }
                return objectNode;
                
            case ARRAY:
                ArrayNode arrayNode = new ArrayNode();
                Iterator<JsonNode> elements = node.elements();
                while (elements.hasNext()) {
                    arrayNode.add(deepCopy(elements.next()));
                }
                return arrayNode;
                
            case STRING:
                return new TextNode(node.asText());
                
            case NUMBER:
                String numText = node.asText();
                if (numText.contains(".") || numText.toLowerCase().contains("e")) {
                    return new DoubleNode(node.asDouble());
                }
                try {
                    long longValue = Long.parseLong(numText);
                    if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                        return new IntNode((int) longValue);
                    }
                    return new LongNode(longValue);
                } catch (NumberFormatException e) {
                    return new DoubleNode(node.asDouble());
                }
                
            case BOOLEAN:
                return BooleanNode.valueOf(node.asBoolean());
                
            case NULL:
            default:
                return NullNode.getInstance();
        }
    }
}
