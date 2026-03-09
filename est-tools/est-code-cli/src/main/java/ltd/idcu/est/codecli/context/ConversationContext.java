package ltd.idcu.est.codecli.context;

import java.util.ArrayList;
import java.util.List;

public class ConversationContext {

    private final List<ConversationMessage> messages;
    private final int maxHistorySize;

    public ConversationContext() {
        this(20);
    }

    public ConversationContext(int maxHistorySize) {
        this.messages = new ArrayList<>();
        this.maxHistorySize = maxHistorySize;
    }

    public void addMessage(String role, String content) {
        messages.add(new ConversationMessage(role, content));
        trimHistory();
    }

    public void addUserMessage(String content) {
        addMessage("user", content);
    }

    public void addAssistantMessage(String content) {
        addMessage("assistant", content);
    }

    public void addSystemMessage(String content) {
        addMessage("system", content);
    }

    public List<ConversationMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public String getContextAsString() {
        StringBuilder sb = new StringBuilder();
        for (ConversationMessage message : messages) {
            sb.append(message.getRole()).append(": ").append(message.getContent()).append("\n");
        }
        return sb.toString();
    }

    public String buildPrompt(String currentMessage) {
        StringBuilder sb = new StringBuilder();
        
        for (ConversationMessage message : messages) {
            sb.append(message.getRole()).append(": ").append(message.getContent()).append("\n\n");
        }
        
        sb.append("user: ").append(currentMessage);
        return sb.toString();
    }

    public void clear() {
        messages.clear();
    }

    public int size() {
        return messages.size();
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public ConversationMessage getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    private void trimHistory() {
        while (messages.size() > maxHistorySize) {
            messages.remove(0);
        }
    }

    public static class ConversationMessage {
        private final String role;
        private final String content;
        private final long timestamp;

        public ConversationMessage(String role, String content) {
            this.role = role;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
