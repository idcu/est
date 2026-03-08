package ltd.idcu.est.ai.impl.tools;

import ltd.idcu.est.ai.api.FunctionTool;

import java.util.HashMap;
import java.util.Map;

public class CalculatorTool implements FunctionTool {

    @Override
    public String getName() {
        return "calculator";
    }

    @Override
    public String getDescription() {
        return "Perform basic arithmetic calculations: add, subtract, multiply, divide";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "object");
        
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, Object> operation = new HashMap<>();
        operation.put("type", "string");
        operation.put("description", "The operation to perform: add, subtract, multiply, divide");
        operation.put("enum", new String[]{"add", "subtract", "multiply", "divide"});
        properties.put("operation", operation);
        
        Map<String, Object> a = new HashMap<>();
        a.put("type", "number");
        a.put("description", "First number");
        properties.put("a", a);
        
        Map<String, Object> b = new HashMap<>();
        b.put("type", "number");
        b.put("description", "Second number");
        properties.put("b", b);
        
        params.put("properties", properties);
        params.put("required", new String[]{"operation", "a", "b"});
        
        return params;
    }

    @Override
    public Object execute(Map<String, Object> arguments) {
        try {
            String operation = (String) arguments.get("operation");
            Number aNum = (Number) arguments.get("a");
            Number bNum = (Number) arguments.get("b");
            
            double a = aNum.doubleValue();
            double b = bNum.doubleValue();
            double result;
            
            switch (operation.toLowerCase()) {
                case "add":
                    result = a + b;
                    break;
                case "subtract":
                    result = a - b;
                    break;
                case "multiply":
                    result = a * b;
                    break;
                case "divide":
                    if (b == 0) {
                        return Map.of("error", "Cannot divide by zero");
                    }
                    result = a / b;
                    break;
                default:
                    return Map.of("error", "Unknown operation: " + operation);
            }
            
            return Map.of(
                "result", result,
                "expression", String.format("%s %s %s = %s", a, getOperatorSymbol(operation), b, result)
            );
        } catch (Exception e) {
            return Map.of("error", "Calculation error: " + e.getMessage());
        }
    }
    
    private String getOperatorSymbol(String operation) {
        switch (operation.toLowerCase()) {
            case "add": return "+";
            case "subtract": return "-";
            case "multiply": return "*";
            case "divide": return "/";
            default: return "?";
        }
    }
}
