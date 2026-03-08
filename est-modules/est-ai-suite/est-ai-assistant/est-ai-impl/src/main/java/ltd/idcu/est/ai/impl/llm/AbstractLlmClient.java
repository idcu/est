package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.ai.impl.DefaultFunctionRegistry;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;
import ltd.idcu.est.utils.format.json.JsonNode;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
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
        return executeWithRetry(() -> doChat(messages, options));
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
                if (attempt < maxRetries && isRetryable(e)) {
                    log("Retry attempt " + attempt + " after error: " + e.getMessage());
                    sleep(retryDelayMs * attempt);
                } else {
                    break;
                }
            }
        }

        log("All retry attempts failed");
        throw wrapException(lastException);
    }

    protected boolean isRetryable(Exception e) {
        if (e instanceof LlmException) {
            return ((LlmException) e).isRetryable();
        }
        return true;
    }

    protected LlmException wrapException(Exception e) {
        if (e instanceof LlmException) {
            return (LlmException) e;
        }
        return new LlmException(LlmException.ErrorType.UNKNOWN_ERROR, e.getMessage(), e, getName());
    }

    protected LlmException createHttpException(int statusCode, String responseBody) {
        LlmException.ErrorType errorType = LlmException.determineErrorType(statusCode);
        String message = "HTTP " + statusCode + ": " + extractErrorMessage(responseBody);
        return new LlmException(errorType, message, getName(), statusCode);
    }

    protected String extractErrorMessage(String responseBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(responseBody);
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                if (errorNode.has("message")) {
                    return errorNode.get("message").asText();
                }
                return errorNode.asText();
            }
        } catch (Exception e) {
            log("Failed to extract error message: " + e.getMessage());
        }
        return responseBody;
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

    protected void processSseStream(String responseBody, StreamCallback callback) {
        StringBuilder fullContent = new StringBuilder();
        String finishReason = null;
        int tokenIndex = 0;

        try (BufferedReader reader = new BufferedReader(new StringReader(responseBody))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (callback.shouldCancel()) {
                    break;
                }

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("data: ")) {
                    String data = line.substring(6).trim();
                    if ("[DONE]".equals(data)) {
                        break;
                    }

                    try {
                        JsonNode rootNode = JsonUtils.parseTree(data);
                        if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
                            JsonNode choicesArray = rootNode.get("choices");
                            if (choicesArray.size() > 0) {
                                JsonNode firstChoice = choicesArray.get(0);
                                
                                if (firstChoice.has("finish_reason") && !firstChoice.get("finish_reason").isNull()) {
                                    finishReason = firstChoice.get("finish_reason").asText();
                                }
                                
                                if (firstChoice.has("delta")) {
                                    JsonNode deltaNode = firstChoice.get("delta");
                                    if (deltaNode.has("content") && !deltaNode.get("content").isNull()) {
                                        String token = deltaNode.get("content").asText();
                                        fullContent.append(token);
                                        callback.onToken(token, tokenIndex++);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log("Failed to parse SSE data: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            callback.onError(new LlmException(LlmException.ErrorType.PARSE_ERROR, "Failed to process stream", e, getName()));
            return;
        }

        if (finishReason != null) {
            callback.onFinishReason(finishReason);
        }
        callback.onComplete(LlmResponse.success(fullContent.toString()));
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
            ObjectNode messageNode = new ObjectNode();
            messageNode.set("role", message.getRole());
            messageNode.set("content", message.getContent());
            messagesArray.add(messageNode);
        }
        requestNode.set("messages", messagesArray);
        
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

    protected LlmResponse parseChatResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
                JsonNode choicesArray = rootNode.get("choices");
                if (choicesArray.size() > 0) {
                    JsonNode firstChoice = choicesArray.get(0);
                    if (firstChoice.has("message")) {
                        JsonNode messageNode = firstChoice.get("message");
                        if (messageNode.has("content")) {
                            String content = messageNode.get("content").asText();
                            return LlmResponse.success(content);
                        }
                    } else if (firstChoice.has("delta")) {
                        JsonNode deltaNode = firstChoice.get("delta");
                        if (deltaNode.has("content")) {
                            String content = deltaNode.get("content").asText();
                            return LlmResponse.success(content);
                        }
                    }
                }
            }
            
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                return LlmResponse.error("API Error: " + errorMessage);
            }
            
            return LlmResponse.error("Failed to parse response: invalid format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
    }
}
