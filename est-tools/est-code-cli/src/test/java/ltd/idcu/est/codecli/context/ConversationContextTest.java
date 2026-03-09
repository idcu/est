package ltd.idcu.est.codecli.context;

import ltd.idcu.est.test.annotation.Test;
import ltd.idcu.est.test.annotation.BeforeEach;

import java.util.List;

import static ltd.idcu.est.test.Assertions.*;

public class ConversationContextTest {

    private ConversationContext context;

    @BeforeEach
    void beforeEach() {
        context = new ConversationContext();
    }

    @Test
    void testDefaultMaxHistorySize() {
        assertEquals(0, context.size());
        assertTrue(context.isEmpty());
    }

    @Test
    void testCustomMaxHistorySize() {
        ConversationContext customContext = new ConversationContext(50);
        assertEquals(0, customContext.size());
        assertTrue(customContext.isEmpty());
    }

    @Test
    void testAddMessage() {
        context.addMessage("user", "Hello");
        assertEquals(1, context.size());
        assertFalse(context.isEmpty());
    }

    @Test
    void testAddUserMessage() {
        context.addUserMessage("Hello user");
        assertEquals(1, context.size());
        
        ConversationContext.ConversationMessage message = context.getLastMessage();
        assertNotNull(message);
        assertEquals("user", message.getRole());
        assertEquals("Hello user", message.getContent());
    }

    @Test
    void testAddAssistantMessage() {
        context.addAssistantMessage("Hello assistant");
        assertEquals(1, context.size());
        
        ConversationContext.ConversationMessage message = context.getLastMessage();
        assertNotNull(message);
        assertEquals("assistant", message.getRole());
        assertEquals("Hello assistant", message.getContent());
    }

    @Test
    void testAddSystemMessage() {
        context.addSystemMessage("Hello system");
        assertEquals(1, context.size());
        
        ConversationContext.ConversationMessage message = context.getLastMessage();
        assertNotNull(message);
        assertEquals("system", message.getRole());
        assertEquals("Hello system", message.getContent());
    }

    @Test
    void testGetMessages() {
        context.addUserMessage("Message 1");
        context.addAssistantMessage("Message 2");
        context.addUserMessage("Message 3");
        
        List<ConversationContext.ConversationMessage> messages = context.getMessages();
        assertNotNull(messages);
        assertEquals(3, messages.size());
        assertEquals("user", messages.get(0).getRole());
        assertEquals("Message 1", messages.get(0).getContent());
        assertEquals("assistant", messages.get(1).getRole());
        assertEquals("Message 2", messages.get(1).getContent());
        assertEquals("user", messages.get(2).getRole());
        assertEquals("Message 3", messages.get(2).getContent());
    }

    @Test
    void testGetMessagesEmpty() {
        List<ConversationContext.ConversationMessage> messages = context.getMessages();
        assertNotNull(messages);
        assertTrue(messages.isEmpty());
    }

    @Test
    void testGetContextAsString() {
        context.addUserMessage("Hello");
        context.addAssistantMessage("Hi there");
        
        String contextString = context.getContextAsString();
        assertNotNull(contextString);
        assertTrue(contextString.contains("user: Hello"));
        assertTrue(contextString.contains("assistant: Hi there"));
    }

    @Test
    void testGetContextAsStringEmpty() {
        String contextString = context.getContextAsString();
        assertNotNull(contextString);
        assertTrue(contextString.isEmpty());
    }

    @Test
    void testBuildPrompt() {
        context.addUserMessage("Previous message");
        context.addAssistantMessage("Previous response");
        
        String prompt = context.buildPrompt("Current question");
        assertNotNull(prompt);
        assertTrue(prompt.contains("user: Previous message"));
        assertTrue(prompt.contains("assistant: Previous response"));
        assertTrue(prompt.contains("user: Current question"));
    }

    @Test
    void testBuildPromptEmpty() {
        String prompt = context.buildPrompt("Current question");
        assertNotNull(prompt);
        assertEquals("user: Current question", prompt);
    }

    @Test
    void testClear() {
        context.addUserMessage("Message 1");
        context.addAssistantMessage("Message 2");
        context.clear();
        
        assertEquals(0, context.size());
        assertTrue(context.isEmpty());
        assertNull(context.getLastMessage());
    }

    @Test
    void testClearEmpty() {
        context.clear();
        assertEquals(0, context.size());
        assertTrue(context.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, context.size());
        context.addUserMessage("Message 1");
        assertEquals(1, context.size());
        context.addAssistantMessage("Message 2");
        assertEquals(2, context.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(context.isEmpty());
        context.addUserMessage("Message");
        assertFalse(context.isEmpty());
        context.clear();
        assertTrue(context.isEmpty());
    }

    @Test
    void testGetLastMessage() {
        context.addUserMessage("Message 1");
        context.addAssistantMessage("Message 2");
        
        ConversationContext.ConversationMessage lastMessage = context.getLastMessage();
        assertNotNull(lastMessage);
        assertEquals("assistant", lastMessage.getRole());
        assertEquals("Message 2", lastMessage.getContent());
    }

    @Test
    void testGetLastMessageEmpty() {
        assertNull(context.getLastMessage());
    }

    @Test
    void testTrimHistory() {
        ConversationContext smallContext = new ConversationContext(3);
        smallContext.addUserMessage("Message 1");
        smallContext.addAssistantMessage("Message 2");
        smallContext.addUserMessage("Message 3");
        assertEquals(3, smallContext.size());
        
        smallContext.addAssistantMessage("Message 4");
        assertEquals(3, smallContext.size());
        
        List<ConversationContext.ConversationMessage> messages = smallContext.getMessages();
        assertEquals("assistant", messages.get(0).getRole());
        assertEquals("Message 2", messages.get(0).getContent());
        assertEquals("user", messages.get(1).getRole());
        assertEquals("Message 3", messages.get(1).getContent());
        assertEquals("assistant", messages.get(2).getRole());
        assertEquals("Message 4", messages.get(2).getContent());
    }

    @Test
    void testConversationMessageProperties() {
        context.addUserMessage("Test content");
        ConversationContext.ConversationMessage message = context.getLastMessage();
        
        assertNotNull(message);
        assertEquals("user", message.getRole());
        assertEquals("Test content", message.getContent());
        assertTrue(message.getTimestamp() > 0);
    }

    @Test
    void testMultipleMessages() {
        for (int i = 1; i <= 10; i++) {
            context.addUserMessage("Message " + i);
        }
        assertEquals(10, context.size());
    }

    @Test
    void testMessagesAreImmutable() {
        context.addUserMessage("Original message");
        List<ConversationContext.ConversationMessage> messages1 = context.getMessages();
        
        context.addAssistantMessage("New message");
        List<ConversationContext.ConversationMessage> messages2 = context.getMessages();
        
        assertEquals(1, messages1.size());
        assertEquals(2, messages2.size());
    }
}
