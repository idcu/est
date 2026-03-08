package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.llm.MockLlmClient;
import ltd.idcu.est.ai.impl.tools.CalculatorTool;
import ltd.idcu.est.ai.impl.tools.StringManipulationTool;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.Map;

public class FunctionCallingIntegrationTest {

    @Test
    public void testCalculatorTool() {
        FunctionTool calculator = new CalculatorTool();
        
        assertEquals("calculator", calculator.getName());
        assertNotNull(calculator.getDescription());
        assertNotNull(calculator.getParameters());
        
        Map<String, Object> args = Map.of(
            "operation", "add",
            "a", 5,
            "b", 3
        );
        Object result = calculator.execute(args);
        assertNotNull(result);
        assertTrue(result instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertTrue(resultMap.containsKey("result"));
        assertEquals(8.0, resultMap.get("result"));
    }
    
    @Test
    public void testCalculatorDivideByZero() {
        FunctionTool calculator = new CalculatorTool();
        
        Map<String, Object> args = Map.of(
            "operation", "divide",
            "a", 10,
            "b", 0
        );
        Object result = calculator.execute(args);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertTrue(resultMap.containsKey("error"));
    }
    
    @Test
    public void testStringManipulationTool() {
        FunctionTool stringTool = new StringManipulationTool();
        
        assertEquals("string_manipulation", stringTool.getName());
        assertNotNull(stringTool.getDescription());
        assertNotNull(stringTool.getParameters());
        
        Map<String, Object> args = Map.of(
            "operation", "uppercase",
            "text", "hello world"
        );
        Object result = stringTool.execute(args);
        assertNotNull(result);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertTrue(resultMap.containsKey("result"));
        assertEquals("HELLO WORLD", resultMap.get("result"));
    }
    
    @Test
    public void testFunctionRegistry() {
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        assertTrue(registry.listTools().isEmpty());
        
        FunctionTool calc = new CalculatorTool();
        FunctionTool stringTool = new StringManipulationTool();
        
        registry.register(calc);
        registry.register(stringTool);
        
        assertEquals(2, registry.listTools().size());
        assertEquals(calc, registry.getTool("calculator"));
        assertEquals(stringTool, registry.getTool("string_manipulation"));
        
        registry.unregister("calculator");
        assertEquals(1, registry.listTools().size());
        assertNull(registry.getTool("calculator"));
    }
    
    @Test
    public void testLlmClientFunctionRegistryIntegration() {
        MockLlmClient client = new MockLlmClient();
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        assertNull(client.getFunctionRegistry());
        
        client.setFunctionRegistry(registry);
        assertEquals(registry, client.getFunctionRegistry());
        
        FunctionTool calc = new CalculatorTool();
        registry.register(calc);
        
        assertEquals(1, client.getFunctionRegistry().listTools().size());
        assertEquals(calc, client.getFunctionRegistry().getTool("calculator"));
    }
    
    @Test
    public void testWordCount() {
        FunctionTool stringTool = new StringManipulationTool();
        
        Map<String, Object> args = Map.of(
            "operation", "count_words",
            "text", "Hello world from EST AI"
        );
        Object result = stringTool.execute(args);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals(5, resultMap.get("result"));
    }
    
    @Test
    public void testStringReverse() {
        FunctionTool stringTool = new StringManipulationTool();
        
        Map<String, Object> args = Map.of(
            "operation", "reverse",
            "text", "EST"
        );
        Object result = stringTool.execute(args);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertEquals("TSE", resultMap.get("result"));
    }
}
