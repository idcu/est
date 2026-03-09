package ltd.idcu.est.llm;

import ltd.idcu.est.llm.api.LlmMessage;
import ltd.idcu.est.llm.api.LlmOptions;
import ltd.idcu.est.llm.api.LlmResponse;
import ltd.idcu.est.llm.api.StreamCallback;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;

import java.io.IOException;
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
            String requestBody = buildAnthropicChatRequestBody(messages, options);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseAnthropicChatResponse(response.body());
            } else {
                return LlmResponse.error("API Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return LlmResponse.error("Error calling API: " + e.getMessage());
        }
    }

    @Override
    public void chatStream(List<LlmMessage> messages, LlmOptions options, StreamCallback callback) {
        try {
            String requestBody = buildAnthropicChatRequestBody(messages, options, true);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", apiKey)
                    .header("anthropic-version", "2023-06-01")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            StringBuilder fullContent = new StringBuilder();
                            response.body().forEach(line -> {
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    try {
                                        String token = parseAnthropicStreamToken(data);
                                        if (token != null) {
                                            fullContent.append(token);
                                            callback.onToken(token);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            });
                            callback.onComplete(LlmResponse.success(fullContent.toString()));
                        } else {
                            callback.onError(new RuntimeException("API Error: " + response.statusCode()));
                        }
                    })
                    .exceptionally(throwable -> {
                        callback.onError(throwable);
                        return null;
                    });
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    private String buildAnthropicChatRequestBody(List<LlmMessage> messages, LlmOptions options) {
        return buildAnthropicChatRequestBody(messages, options, false);
    }

    private String buildAnthropicChatRequestBody(List<LlmMessage> messages, LlmOptions options, boolean stream) {
        ObjectNode requestNode = new ObjectNode();
        requestNode.set("model", model);
        
        List<LlmMessage> anthropicMessages = new ArrayList<>();
        String systemMessage = null;
        
        for (LlmMessage message : messages) {
            if ("system".equals(message.getRole())) {
                systemMessage = message.getContent();
            } else {
                anthropicMessages.add(message);
            }
        }
        
        if (systemMessage != null) {
            requestNode.set("system", systemMessage);
        }
        
        ArrayNode messagesArray = new ArrayNode();
        for (LlmMessage message : anthropicMessages) {
            ObjectNode messageNode = new ObjectNode();
            messageNode.set("role", "user".equals(message.getRole()) ? "user" : "assistant");
            messageNode.set("content", message.getContent());
            messagesArray.add(messageNode);
        }
        requestNode.set("messages", messagesArray);
        
        int maxTokens = options.getMaxTokens() != null ? options.getMaxTokens() : 4096;
        requestNode.set("max_tokens", maxTokens);
        
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

    private LlmResponse parseAnthropicChatResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                return LlmResponse.error("API Error: " + errorMessage);
            }
            
            if (rootNode.has("content") && rootNode.get("content").isArray()) {
                JsonNode contentArray = rootNode.get("content");
                StringBuilder contentBuilder = new StringBuilder();
                
                for (int i = 0; i < contentArray.size(); i++) {
                    JsonNode contentItem = contentArray.get(i);
                    if (contentItem.has("type") && "text".equals(contentItem.get("type").asText())) {
                        if (contentItem.has("text")) {
                            contentBuilder.append(contentItem.get("text").asText());
                        }
                    }
                }
                
                return LlmResponse.success(contentBuilder.toString());
            }
            
            return LlmResponse.error("Failed to parse response: invalid format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
    }

    private String parseAnthropicStreamToken(String data) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(data);
            String type = rootNode.has("type") ? rootNode.get("type").asText() : "";
            
            if ("content_block_delta".equals(type)) {
                if (rootNode.has("delta")) {
                    JsonNode deltaNode = rootNode.get("delta");
                    if (deltaNode.has("text")) {
                        return deltaNode.get("text").asText();
                    }
                }
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T handleError(Exception e) {
        return (T) LlmResponse.error("Fatal error: " + e.getMessage());
    }

    @Override
    public String getName() {
        return "Anthropic";
    }
}
