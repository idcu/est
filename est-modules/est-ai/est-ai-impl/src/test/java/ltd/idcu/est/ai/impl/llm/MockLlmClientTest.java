package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.LlmOptions;
import ltd.idcu.est.ai.api.LlmResponse;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class MockLlmClientTest {
    
    @Test
    public void testCreateMockClient() {
        MockLlmClient client = new MockLlmClient();
        assertNotNull(client);
        assertTrue(client.isAvailable());
    }
    
    @Test
    public void testGetName() {
        MockLlmClient client = new MockLlmClient();
        assertEquals("mock", client.getName());
    }
    
    @Test
    public void testSetAndGetApiKey() {
        MockLlmClient client = new MockLlmClient();
        client.setApiKey("test-key");
        assertEquals("test-key", client.getApiKey());
    }
    
    @Test
    public void testSetAndGetModel() {
        MockLlmClient client = new MockLlmClient();
        client.setModel("test-model");
        assertEquals("test-model", client.getModel());
    }
    
    @Test
    public void testSetEndpoint() {
        MockLlmClient client = new MockLlmClient();
        client.setEndpoint("https://test.endpoint");
    }
    
    @Test
    public void testSetAndGetAvailable() {
        MockLlmClient client = new MockLlmClient();
        assertTrue(client.isAvailable());
        
        client.setAvailable(false);
        assertFalse(client.isAvailable());
        
        client.setAvailable(true);
        assertTrue(client.isAvailable());
    }
    
    @Test
    public void testDefaultMockResponse() {
        MockLlmClient client = new MockLlmClient();
        String response = client.generate("test prompt");
        assertEquals("Mock response", response);
    }
    
    @Test
    public void testSetMockResponseString() {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Custom response");
        
        String response = client.generate("test prompt");
        assertEquals("Custom response", response);
    }
    
    @Test
    public void testSetMockResponseObject() {
        MockLlmClient client = new MockLlmClient();
        LlmResponse customResponse = LlmResponse.success("Object response");
        client.setMockResponse(customResponse);
        
        String response = client.generate("test prompt");
        assertEquals("Object response", response);
    }
    
    @Test
    public void testSetErrorResponse() {
        MockLlmClient client = new MockLlmClient();
        client.setErrorResponse("Test error");
        
        LlmResponse response = client.chat(List.of(new LlmMessage("user", "test")));
        assertFalse(response.isSuccess());
        assertEquals("Test error", response.getErrorMessage());
    }
    
    @Test
    public void testGenerateWithOptions() {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Options response");
        
        LlmOptions options = new LlmOptions();
        String response = client.generate("test", options);
        assertEquals("Options response", response);
    }
    
    @Test
    public void testChatWithMessages() {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Chat response");
        
        List<LlmMessage> messages = List.of(
            new LlmMessage("user", "Hello")
        );
        
        LlmResponse response = client.chat(messages);
        assertTrue(response.isSuccess());
        assertEquals("Chat response", response.getContent());
    }
    
    @Test
    public void testChatWithMessagesAndOptions() {
        MockLlmClient client = new MockLlmClient();
        client.setMockResponse("Chat with options");
        
        List<LlmMessage> messages = List.of(
            new LlmMessage("user", "Hello")
        );
        LlmOptions options = new LlmOptions();
        
        LlmResponse response = client.chat(messages, options);
        assertTrue(response.isSuccess());
        assertEquals("Chat with options", response.getContent());
    }
    
    @Test
    public void testReceivedMessages() {
        MockLlmClient client = new MockLlmClient();
        
        List<LlmMessage> messages1 = List.of(
            new LlmMessage("user", "Message 1")
        );
        client.chat(messages1);
        
        List<LlmMessage> messages2 = List.of(
            new LlmMessage("user", "Message 2")
        );
        client.chat(messages2);
        
        List<List<LlmMessage>> received = client.getReceivedMessages();
        assertEquals(2, received.size());
        assertEquals(messages1, received.get(0));
        assertEquals(messages2, received.get(1));
    }
    
    @Test
    public void testClearReceivedMessages() {
        MockLlmClient client = new MockLlmClient();
        
        client.chat(List.of(new LlmMessage("user", "test")));
        assertFalse(client.getReceivedMessages().isEmpty());
        
        client.clearReceivedMessages();
        assertTrue(client.getReceivedMessages().isEmpty());
    }
}
