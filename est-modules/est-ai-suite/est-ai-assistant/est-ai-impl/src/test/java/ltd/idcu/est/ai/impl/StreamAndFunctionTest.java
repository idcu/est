package ltd.idcu.est.ai.impl;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.llm.MockLlmClient;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StreamAndFunctionTest {
    
    @Test
    public void testStreamCallbackInterface() {
        List<String> receivedTokens = new ArrayList<>();
        final LlmResponse[] finalResponse = {null};
        final Throwable[] error = {null};
        
        StreamCallback callback = new StreamCallback() {
            @Override
            public void onToken(String token) {
                receivedTokens.add(token);
            }
            
            @Override
            public void onComplete(LlmResponse response) {
                finalResponse[0] = response;
            }
            
            @Override
            public void onError(Throwable e) {
                error[0] = e;
            }
        };
        
        assertNotNull(callback);
    }
    
    @Test
    public void testMockStreamResponse() throws InterruptedException {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Hello, world!");
        
        List<String> receivedTokens = new ArrayList<>();
        final LlmResponse[] finalResponse = {null};
        CountDownLatch latch = new CountDownLatch(1);
        
        client.chatStream(
            List.of(new LlmMessage("user", "test")),
            new StreamCallback() {
                @Override
                public void onToken(String token) {
                    receivedTokens.add(token);
                }
                
                @Override
                public void onComplete(LlmResponse response) {
                    finalResponse[0] = response;
                    latch.countDown();
                }
                
                @Override
                public void onError(Throwable e) {
                    latch.countDown();
                }
            }
        );
        
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(finalResponse[0]);
        assertTrue(finalResponse[0].isSuccess());
        assertEquals("Hello, world!", finalResponse[0].getContent());
    }
    
    @Test
    public void testCustomStreamTokens() throws InterruptedException {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Test content");
        client.setStreamedTokens(List.of("Hello", " ", "world", "!"));
        client.setStreamDelayMs(10);
        
        List<String> receivedTokens = new ArrayList<>();
        final LlmResponse[] finalResponse = {null};
        CountDownLatch latch = new CountDownLatch(1);
        
        client.chatStream(
            List.of(new LlmMessage("user", "test")),
            new StreamCallback() {
                @Override
                public void onToken(String token) {
                    receivedTokens.add(token);
                }
                
                @Override
                public void onComplete(LlmResponse response) {
                    finalResponse[0] = response;
                    latch.countDown();
                }
                
                @Override
                public void onError(Throwable e) {
                    latch.countDown();
                }
            }
        );
        
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertEquals(4, receivedTokens.size());
        assertEquals("Hello", receivedTokens.get(0));
        assertEquals(" ", receivedTokens.get(1));
        assertEquals("world", receivedTokens.get(2));
        assertEquals("!", receivedTokens.get(3));
    }
    
    @Test
    public void testFunctionToolInterface() {
        FunctionTool tool = new FunctionTool() {
            @Override
            public String getName() {
                return "test-tool";
            }
            
            @Override
            public String getDescription() {
                return "A test function tool";
            }
            
            @Override
            public Map<String, Object> getParameters() {
                Map<String, Object> params = new HashMap<>();
                params.put("type", "object");
                return params;
            }
            
            @Override
            public Object execute(Map<String, Object> arguments) {
                return "result";
            }
        };
        
        assertEquals("test-tool", tool.getName());
        assertEquals("A test function tool", tool.getDescription());
        assertNotNull(tool.getParameters());
        assertEquals("result", tool.execute(new HashMap<>()));
    }
    
    @Test
    public void testDefaultFunctionRegistry() {
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        FunctionTool tool1 = new FunctionTool() {
            @Override
            public String getName() { return "tool1"; }
            @Override
            public String getDescription() { return "desc1"; }
            @Override
            public Map<String, Object> getParameters() { return new HashMap<>(); }
            @Override
            public Object execute(Map<String, Object> args) { return "result1"; }
        };
        
        FunctionTool tool2 = new FunctionTool() {
            @Override
            public String getName() { return "tool2"; }
            @Override
            public String getDescription() { return "desc2"; }
            @Override
            public Map<String, Object> getParameters() { return new HashMap<>(); }
            @Override
            public Object execute(Map<String, Object> args) { return "result2"; }
        };
        
        registry.register(tool1);
        registry.register(tool2);
        
        assertEquals(2, registry.listTools().size());
        assertEquals(tool1, registry.getTool("tool1"));
        assertEquals(tool2, registry.getTool("tool2"));
        
        registry.unregister("tool1");
        assertEquals(1, registry.listTools().size());
        assertNull(registry.getTool("tool1"));
        assertNotNull(registry.getTool("tool2"));
    }
    
    @Test
    public void testLlmClientFunctionRegistryIntegration() {
        MockLlmClient client = new MockLlmClient();
        FunctionRegistry registry = new DefaultFunctionRegistry();
        
        assertNull(client.getFunctionRegistry());
        
        client.setFunctionRegistry(registry);
        assertEquals(registry, client.getFunctionRegistry());
        
        FunctionTool tool = new FunctionTool() {
            @Override
            public String getName() { return "test-tool"; }
            @Override
            public String getDescription() { return "test-desc"; }
            @Override
            public Map<String, Object> getParameters() { return new HashMap<>(); }
            @Override
            public Object execute(Map<String, Object> args) { return "test-result"; }
        };
        
        registry.register(tool);
        assertEquals(1, client.getFunctionRegistry().listTools().size());
        assertEquals(tool, client.getFunctionRegistry().getTool("test-tool"));
    }
    
    @Test
    public void testStreamWithOptions() throws InterruptedException {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Test with options");
        client.setStreamDelayMs(10);
        
        LlmOptions options = new LlmOptions();
        options.setTemperature(0.5);
        options.setMaxTokens(100);
        
        List<String> receivedTokens = new ArrayList<>();
        final LlmResponse[] finalResponse = {null};
        CountDownLatch latch = new CountDownLatch(1);
        
        client.chatStream(
            List.of(new LlmMessage("user", "test")),
            options,
            new StreamCallback() {
                @Override
                public void onToken(String token) {
                    receivedTokens.add(token);
                }
                
                @Override
                public void onComplete(LlmResponse response) {
                    finalResponse[0] = response;
                    latch.countDown();
                }
                
                @Override
                public void onError(Throwable e) {
                    latch.countDown();
                }
            }
        );
        
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        assertNotNull(finalResponse[0]);
        assertTrue(finalResponse[0].isSuccess());
    }
}
