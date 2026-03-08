package ltd.idcu.est.llm;

import ltd.idcu.est.llm.api.*;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;
import ltd.idcu.est.utils.format.json.JsonNode;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractLlmClient implements LlmClient {

    protected String apiKey;
    protected String endpoint;
    protected String model;
    protected final HttpClient httpClient;
    protected int maxRetries = 3;
    protected long retryDelayMs = 1000;
    protected FunctionRegistry functionRegistry;
    protected boolean autoExecuteTools = true;
    protected int maxToolIterations = 5;

    protected AbstractLlmClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.functionRegistry = new DefaultFunctionRegistry();
    }

    protected AbstractLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    public String generate(String prompt) {
        return generate(prompt, new LlmOptions());
    }

    @Override
    public String generate(String prompt, LlmOptions options) {
        LlmMessage userMessage = new LlmMessage("user", prompt);
        LlmResponse response = chat(List.of(userMessage), options);
        return response.getContent();
    }

    @Override
    public LlmResponse chat(List<LlmMessage> messages) {
        return chat(messages, new LlmOptions());
    }

    @Override
    public LlmResponse chat(List<LlmMessage> messages, LlmOptions options) {
        return executeWithRetry(() -> chatWithToolExecution(messages, options));
    }

    protected LlmResponse chatWithToolExecution(List<LlmMessage> messages, LlmOptions options) {
        List<LlmMessage> conversationHistory = new ArrayList<>(messages);
        int iteration = 0;

        while (iteration < maxToolIterations) {
            LlmResponse response = doChat(conversationHistory, options);

            if (!response.isSuccess()) {
                return response;
            }

            if (!response.hasToolCalls() || !autoExecuteTools) {
                return response;
            }

            log("LLM requested tool calls, executing...");

            List<LlmMessage.ToolCall> toolCalls = response.getToolCalls();
            LlmMessage assistantMessage = new LlmMessage("assistant", response.getContent());
            assistantMessage.setToolCalls(toolCalls);
            conversationHistory.add(assistantMessage);

            for (LlmMessage.ToolCall toolCall : toolCalls) {
                LlmMessage toolResultMessage = executeToolCall(toolCall);
                conversationHistory.add(toolResultMessage);
            }

            iteration++;
        }

        log("Max tool iterations reached, returning final response");
        return doChat(conversationHistory, options);
    }

    protected LlmMessage executeToolCall(LlmMessage.ToolCall toolCall) {
        String toolName = toolCall.getFunction().getName();
        String toolCallId = toolCall.getId();
        String argumentsJson = toolCall.getFunction().getArguments();

        log("Executing tool: " + toolName + " with arguments: " + argumentsJson);

        FunctionTool tool = functionRegistry.getTool(toolName);
        if (tool == null) {
            String errorMsg = "Tool not found: " + toolName;
            log(errorMsg);
            return LlmMessage.tool(toolCallId, toolName, JsonUtils.toJson(Map.of("error", errorMsg)));
        }

        try {
            Map<String, Object> arguments;
            if (argumentsJson != null && !argumentsJson.isEmpty()) {
                arguments = JsonUtils.fromJson(argumentsJson, Map.class);
            } else {
                arguments = new HashMap<>();
            }

            Object result = tool.execute(arguments);
            String resultJson = JsonUtils.toJson(result);
            log("Tool execution result: " + resultJson);
            return LlmMessage.tool(toolCallId, toolName, resultJson);
        } catch (Exception e) {
            log("Tool execution failed: " + e.getMessage());
            return LlmMessage.tool(toolCallId, toolName, JsonUtils.toJson(Map.of("error", e.getMessage())));
        }
    }

    protected abstract LlmResponse doChat(List<LlmMessage> messages, LlmOptions options);

    protected <T> T executeWithRetry(Supplier<T> action) {
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            try {
                return action.get();
            } catch (Exception e) {
                lastException = e;
                attempt++;
                if (attempt < maxRetries) {
                    log("Retry attempt " + attempt + " after error: " + e.getMessage());
                    sleep(retryDelayMs * attempt);
                }
            }
        }

        log("All retry attempts failed");
        return handleError(lastException);
    }

    protected void log(String message) {
        System.out.println("[LLM " + getName() + "] " + message);
    }

    protected void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract <T> T handleError(Exception e);

    @Override
    public void chatStream(List<LlmMessage> messages, StreamCallback callback) {
        chatStream(messages, new LlmOptions(), callback);
    }

    @Override
    public void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback) {
        try {
            LlmResponse response = chat(messages, options);
            callback.onComplete(response);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void setFunctionRegistry(FunctionRegistry registry) {
        this.functionRegistry = registry;
    }

    @Override
    public FunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    public void setAutoExecuteTools(boolean autoExecuteTools) {
        this.autoExecuteTools = autoExecuteTools;
    }

    public void setMaxToolIterations(int maxToolIterations) {
        this.maxToolIterations = maxToolIterations;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }

    protected String buildChatRequestBody(List<LlmMessage> messages, LlmOptions options) {
        return buildChatRequestBody(messages, options, false);
    }

    protected String buildChatRequestBody(List<LlmMessage> messages, LlmOptions options, boolean stream) {
        ObjectNode requestNode = new ObjectNode();
        requestNode.set("model", model);
        
        ArrayNode messagesArray = new ArrayNode();
        for (LlmMessage message : messages) {
            ObjectNode messageNode = buildMessageNode(message);
            messagesArray.add(messageNode);
        }
        requestNode.set("messages", messagesArray);
        
        if (functionRegistry != null && !functionRegistry.listTools().isEmpty()) {
            ArrayNode toolsArray = new ArrayNode();
            for (FunctionTool tool : functionRegistry.listTools()) {
                ObjectNode toolNode = new ObjectNode();
                toolNode.set("type", "function");
                
                ObjectNode functionNode = new ObjectNode();
                functionNode.set("name", tool.getName());
                functionNode.set("description", tool.getDescription());
                functionNode.set("parameters", tool.getParameters());
                toolNode.set("function", functionNode);
                
                toolsArray.add(toolNode);
            }
            requestNode.set("tools", toolsArray);
        }
        
        if (options.getTemperature() != null) {
            requestNode.set("temperature", options.getTemperature());
        }
        if (options.getMaxTokens() != null) {
            requestNode.set("max_tokens", options.getMaxTokens());
        }
        if (options.getTopP() != null) {
            requestNode.set("top_p", options.getTopP());
        }
        if (options.getTopK() != null) {
            requestNode.set("top_k", options.getTopK());
        }
        if (options.getPresencePenalty() != null) {
            requestNode.set("presence_penalty", options.getPresencePenalty());
        }
        if (options.getFrequencyPenalty() != null) {
            requestNode.set("frequency_penalty", options.getFrequencyPenalty());
        }
        if (options.getStop() != null && !options.getStop().isEmpty()) {
            ArrayNode stopArray = new ArrayNode();
            for (String stop : options.getStop()) {
                stopArray.add(stop);
            }
            requestNode.set("stop", stopArray);
        }
        if (stream) {
            requestNode.set("stream", true);
        }
        
        return JsonUtils.toJson(requestNode);
    }

    protected ObjectNode buildMessageNode(LlmMessage message) {
        ObjectNode messageNode = new ObjectNode();
        messageNode.set("role", message.getRole());
        
        if (message.getContent() != null) {
            messageNode.set("content", message.getContent());
        }
        
        if ("tool".equals(message.getRole())) {
            if (message.getToolCallId() != null) {
                messageNode.set("tool_call_id", message.getToolCallId());
            }
        }
        
        if (message.getToolCalls() != null && !message.getToolCalls().isEmpty()) {
            ArrayNode toolCallsArray = new ArrayNode();
            for (LlmMessage.ToolCall toolCall : message.getToolCalls()) {
                ObjectNode toolCallNode = new ObjectNode();
                toolCallNode.set("id", toolCall.getId());
                toolCallNode.set("type", toolCall.getType());
                
                ObjectNode functionNode = new ObjectNode();
                functionNode.set("name", toolCall.getFunction().getName());
                functionNode.set("arguments", toolCall.getFunction().getArguments());
                toolCallNode.set("function", functionNode);
                
                toolCallsArray.add(toolCallNode);
            }
            messageNode.set("tool_calls", toolCallsArray);
        }
        
        return messageNode;
    }

    protected LlmResponse parseChatResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                return LlmResponse.error("API Error: " + errorMessage);
            }
            
            if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
                JsonNode choicesArray = rootNode.get("choices");
                if (choicesArray.size() > 0) {
                    JsonNode firstChoice = choicesArray.get(0);
                    
                    List<LlmMessage.ToolCall> toolCalls = null;
                    String content = null;
                    String finishReason = null;
                    
                    if (firstChoice.has("message")) {
                        JsonNode messageNode = firstChoice.get("message");
                        
                        if (messageNode.has("content") && !messageNode.get("content").isNull()) {
                            content = messageNode.get("content").asText();
                        }
                        
                        if (messageNode.has("tool_calls")) {
                            JsonNode toolCallsNode = messageNode.get("tool_calls");
                            if (toolCallsNode.isArray()) {
                                toolCalls = parseToolCalls(toolCallsNode);
                            }
                        }
                    } else if (firstChoice.has("delta")) {
                        JsonNode deltaNode = firstChoice.get("delta");
                        if (deltaNode.has("content") && !deltaNode.get("content").isNull()) {
                            content = deltaNode.get("content").asText();
                        }
                        
                        if (deltaNode.has("tool_calls")) {
                            JsonNode toolCallsNode = deltaNode.get("tool_calls");
                            if (toolCallsNode.isArray()) {
                                toolCalls = parseToolCalls(toolCallsNode);
                            }
                        }
                    }
                    
                    if (firstChoice.has("finish_reason")) {
                        finishReason = firstChoice.get("finish_reason").asText();
                    }
                    
                    LlmResponse response;
                    if (content != null) {
                        response = LlmResponse.success(content);
                    } else {
                        response = LlmResponse.success("");
                    }
                    
                    if (toolCalls != null && !toolCalls.isEmpty()) {
                        response.setToolCalls(toolCalls);
                    }
                    
                    if (!response.getChoices().isEmpty()) {
                        LlmResponse.LlmChoice choice = response.getChoices().get(0);
                        choice.setFinishReason(finishReason);
                        choice.setToolCalls(toolCalls);
                    }
                    
                    return response;
                }
            }
            
            return LlmResponse.error("Failed to parse response: invalid format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
    }

    protected List<LlmMessage.ToolCall> parseToolCalls(JsonNode toolCallsNode) {
        List<LlmMessage.ToolCall> toolCalls = new ArrayList<>();
        for (int i = 0; i < toolCallsNode.size(); i++) {
            JsonNode toolCallNode = toolCallsNode.get(i);
            String id = toolCallNode.has("id") ? toolCallNode.get("id").asText() : null;
            String type = toolCallNode.has("type") ? toolCallNode.get("type").asText() : "function";
            
            if (toolCallNode.has("function")) {
                JsonNode functionNode = toolCallNode.get("function");
                String name = functionNode.has("name") ? functionNode.get("name").asText() : null;
                String arguments = functionNode.has("arguments") ? functionNode.get("arguments").asText() : null;
                
                LlmMessage.ToolCall toolCall = new LlmMessage.ToolCall();
                toolCall.setId(id);
                toolCall.setType(type);
                
                LlmMessage.Function function = new LlmMessage.Function();
                function.setName(name);
                function.setArguments(arguments);
                toolCall.setFunction(function);
                
                toolCalls.add(toolCall);
            }
        }
        return toolCalls;
    }

    protected String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    protected String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}
