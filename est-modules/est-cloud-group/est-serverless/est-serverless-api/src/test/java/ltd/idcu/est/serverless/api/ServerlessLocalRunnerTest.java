package ltd.idcu.est.serverless.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServerlessLocalRunnerTest {

    private ServerlessLocalRunner runner;

    @BeforeEach
    void setUp() {
        runner = new ServerlessLocalRunner();
    }

    @AfterEach
    void tearDown() {
        runner.shutdown();
    }

    @Test
    void testRegisterAndInvokeFunction() {
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function);
        
        String result = runner.invoke("test-func", "Hello");
        assertEquals("HELLO", result);
        assertTrue(function.initialized);
    }

    @Test
    void testInvokeWithConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("key", "value");
        
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function, config);
        
        assertNotNull(function.config);
        assertEquals("value", function.config.get("key"));
    }

    @Test
    void testInvokeWithRequestContext() {
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function);
        
        Map<String, Object> requestContext = new HashMap<>();
        requestContext.put("customKey", "customValue");
        
        runner.invoke("test-func", "input", requestContext);
        
        assertTrue(function.context.containsKey("customKey"));
        assertEquals("customValue", function.context.get("customKey"));
    }

    @Test
    void testInvokeNonExistentFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            runner.invoke("nonexistent", "input");
        });
    }

    @Test
    void testGlobalContext() {
        runner.setGlobalContext("globalKey", "globalValue");
        
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function);
        
        runner.invoke("test-func", "input");
        
        assertTrue(function.context.containsKey("globalKey"));
        assertEquals("globalValue", function.context.get("globalKey"));
    }

    @Test
    void testContextContainsExecutionInfo() {
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function);
        
        runner.invoke("test-func", "input");
        
        assertTrue(function.context.containsKey("functionName"));
        assertEquals("test-func", function.context.get("functionName"));
        assertTrue(function.context.containsKey("localExecution"));
        assertTrue(function.context.containsKey("executionTime"));
    }

    @Test
    void testMultipleFunctions() {
        TestFunction func1 = new TestFunction();
        TestFunction func2 = new TestFunction();
        
        runner.registerFunction("func1", func1);
        runner.registerFunction("func2", func2);
        
        String result1 = runner.invoke("func1", "hello");
        String result2 = runner.invoke("func2", "world");
        
        assertEquals("HELLO", result1);
        assertEquals("WORLD", result2);
    }

    @Test
    void testShutdown() {
        TestFunction function = new TestFunction();
        runner.registerFunction("test-func", function);
        
        runner.shutdown();
        
        assertTrue(function.destroyed);
    }

    private static class TestFunction implements ServerlessFunction<String, String> {
        boolean initialized = false;
        boolean destroyed = false;
        Map<String, Object> config;
        Map<String, Object> context;

        @Override
        public void initialize(Map<String, Object> config) {
            this.initialized = true;
            this.config = config;
        }

        @Override
        public String handle(String input, Map<String, Object> context) {
            this.context = context;
            return input.toUpperCase();
        }

        @Override
        public void destroy() {
            this.destroyed = true;
        }
    }
}
