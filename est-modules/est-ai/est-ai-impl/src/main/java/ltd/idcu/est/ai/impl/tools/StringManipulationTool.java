package ltd.idcu.est.ai.impl.tools;

import ltd.idcu.est.ai.api.FunctionTool;

import java.util.HashMap;
import java.util.Map;

public class StringManipulationTool implements FunctionTool {

    @Override
    public String getName() {
        return "string_manipulation";
    }

    @Override
    public String getDescription() {
        return "Perform various string manipulations: uppercase, lowercase, reverse, count characters, count words";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "object");
        
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> operation = new HashMap<>();
        operation.put("type", "string");
        operation.put("description", "The operation to perform");
        operation.put("enum", new String[]{"uppercase", "lowercase", "reverse", "count_chars", "count_words"});
        properties.put("operation", operation);
        
        Map<String, Object> text = new HashMap<>();
        text.put("type", "string");
        text.put("description", "The input text to process");
        properties.put("text", text);
        
        params.put("properties", properties);
        params.put("required", new String[]{"operation", "text"});
        
        return params;
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        try {
            String operation = (String) arguments.get("operation");
            String text = (String) arguments.get("text");
            
            if (text == null) {
                return Map.of("error", "Text cannot be null");
            }
            
            Object result;
            switch (operation.toLowerCase()) {
                case "uppercase":
                    result = text.toUpperCase();
                    break;
                case "lowercase":
                    result = text.toLowerCase();
                    break;
                case "reverse":
                    result = new StringBuilder(text).reverse().toString();
                    break;
                case "count_chars":
                    result = text.length();
                    break;
                case "count_words":
                    result = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;
                    break;
                default:
                    return Map.of("error", "Unknown operation: " + operation);
            }
            
            return Map.of(
                "operation", operation,
                "result", result,
                "original_text", text
            );
        } catch (Exception e) {
            return Map.of("error", "String manipulation error: " + e.getMessage());
        }
    }
}
