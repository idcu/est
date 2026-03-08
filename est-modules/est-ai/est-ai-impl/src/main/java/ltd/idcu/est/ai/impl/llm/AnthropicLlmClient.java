package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AnthropicLlmClient extends AbstractLlmClient {

    public AnthropicLlmClient() {
        super();
        this.endpoint = "https://api.anthropic.com/v1/messages";
        this.model = "claude-3-opus-20240229";
    }

    public AnthropicLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildAnthropicRequestBody(messages, options, false);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseAnthropicResponse(response.body());
            } else {
                throw createHttpException(response.statusCode(), response.body());
            }
        } catch (IOException e) {
            throw new LlmException(LlmException.ErrorType.NETWORK_ERROR, "Network error", e, getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LlmException(LlmException.ErrorType.TIMEOUT_ERROR, "Request interrupted", e, getName());
        }
    }

    @Override
    public void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback) {
        try {
            String requestBody = buildAnthropicRequestBody(messages, options, true);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            processAnthropicSseStream(response.body(), callback);
                        } else {
                            callback.onError(createHttpException(response.statusCode(), response.body()));
                        }
                    })
                    .exceptionally(throwable -> {
                        callback.onError(new LlmException(LlmException.ErrorType.NETWORK_ERROR, "Stream error", throwable, getName()));
                        return null;
                    });
        } catch (Exception e) {
            callback.onError(new LlmException(LlmException.ErrorType.UNKNOWN_ERROR, "Stream setup error", e, getName()));
        }
    }

    private String buildAnthropicRequestBody(List<LlmMessage> messages, LlmOptions options, boolean stream) {
        ObjectNode requestNode = new ObjectNode();
        requestNode.set("model", model);
        
        List<LlmMessage> anthropicMessages = new ArrayList<>();
        String systemPrompt = null;
        
        for (LlmMessage message : messages) {
            if ("system".equals(message.getRole())) {
                systemPrompt = message.getContent();
            } else {
                anthropicMessages.add(message);
            }
        }
        
        if (systemPrompt != null) {
            requestNode.set("system", systemPrompt);
        }
        
        ArrayNode messagesArray = new ArrayNode();
        for (LlmMessage message : anthropicMessages) {
            ObjectNode messageNode = new ObjectNode();
            messageNode.set("role", message.getRole());
            messageNode.set("content", message.getContent());
            messagesArray.add(messageNode);
        }
        requestNode.set("messages", messagesArray);
        
        if (options.getMaxTokens() != null) {
            requestNode.set("max_tokens", options.getMaxTokens());
        } else {
            requestNode.set("max_tokens", 4096);
        }
        
        if (options.getTemperature() != null) {
            requestNode.set("temperature", options.getTemperature());
        }
        
        if (options.getTopP() != null) {
            requestNode.set("top_p", options.getTopP());
        }
        
        if (options.getTopK() != null) {
            requestNode.set("top_k", options.getTopK());
        }
        
        if (stream) {
            requestNode.set("stream", true);
        }
        
        return JsonUtils.toJson(requestNode);
    }

    private LlmResponse parseAnthropicResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("content") && rootNode.get("content").isArray()) {
                JsonNode contentArray = rootNode.get("content");
                StringBuilder fullContent = new StringBuilder();
                
                for (int i = 0; i < contentArray.size(); i++) {
                    JsonNode contentItem = contentArray.get(i);
                    if (contentItem.has("type") && "text".equals(contentItem.get("type").asText())) {
                        if (contentItem.has("text")) {
                            fullContent.append(contentItem.get("text").asText());
                        }
                    }
                }
                
                return LlmResponse.success(fullContent.toString());
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

    private void processAnthropicSseStream(String responseBody, StreamCallback callback) {
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
                        String eventType = rootNode.has("type") ? rootNode.get("type").asText() : "";
                        
                        if ("message_stop".equals(eventType)) {
                            break;
                        } else if ("content_block_delta".equals(eventType)) {
                            if (rootNode.has("delta")) {
                                JsonNode deltaNode = rootNode.get("delta");
                                if (deltaNode.has("text")) {
                                    String token = deltaNode.get("text").asText();
                                    fullContent.append(token);
                                    callback.onToken(token, tokenIndex++);
                                }
                            }
                        } else if ("message_delta".equals(eventType)) {
                            if (rootNode.has("delta")) {
                                JsonNode deltaNode = rootNode.get("delta");
                                if (deltaNode.has("stop_reason")) {
                                    finishReason = deltaNode.get("stop_reason").asText();
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
    public String getName() {
        return "Anthropic Claude";
    }
}
