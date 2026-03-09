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
import java.util.List;

public class GeminiLlmClient extends AbstractLlmClient {

    public GeminiLlmClient() {
        super();
        this.endpoint = "https://generativelanguage.googleapis.com/v1beta/models";
        this.model = "gemini-1.5-pro";
    }

    public GeminiLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildGeminiChatRequestBody(messages, options);
            String url = endpoint + "/" + model + ":generateContent?key=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseGeminiChatResponse(response.body());
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
            String requestBody = buildGeminiChatRequestBody(messages, options);
            String url = endpoint + "/" + model + ":streamGenerateContent?key=" + apiKey;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            StringBuilder fullContent = new StringBuilder();
                            response.body().forEach(line -> {
                                try {
                                    String token = parseGeminiStreamToken(line);
                                    if (token != null) {
                                        fullContent.append(token);
                                        callback.onToken(token);
                                    }
                                } catch (Exception e) {
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

    private String buildGeminiChatRequestBody(List<LlmMessage> messages, LlmOptions options) {
        ObjectNode requestNode = new ObjectNode();
        
        ArrayNode contentsArray = new ArrayNode();
        
        for (LlmMessage message : messages) {
            if ("system".equals(message.getRole())) {
                continue;
            }
            
            ObjectNode contentNode = new ObjectNode();
            String role = "user".equals(message.getRole()) ? "user" : "model";
            contentNode.set("role", role);
            
            ArrayNode partsArray = new ArrayNode();
            ObjectNode textPart = new ObjectNode();
            textPart.set("text", message.getContent());
            partsArray.add(textPart);
            contentNode.set("parts", partsArray);
            
            contentsArray.add(contentNode);
        }
        requestNode.set("contents", contentsArray);
        
        ObjectNode generationConfig = new ObjectNode();
        if (options.getTemperature() != null) {
            generationConfig.set("temperature", options.getTemperature());
        }
        if (options.getTopP() != null) {
            generationConfig.set("topP", options.getTopP());
        }
        if (options.getTopK() != null) {
            generationConfig.set("topK", options.getTopK());
        }
        if (options.getMaxTokens() != null) {
            generationConfig.set("maxOutputTokens", options.getMaxTokens());
        }
        if (options.getStop() != null && !options.getStop().isEmpty()) {
            ArrayNode stopArray = new ArrayNode();
            for (String stop : options.getStop()) {
                stopArray.add(stop);
            }
            generationConfig.set("stopSequences", stopArray);
        }
        
        if (!generationConfig.isEmpty()) {
            requestNode.set("generationConfig", generationConfig);
        }
        
        return JsonUtils.toJson(requestNode);
    }

    private LlmResponse parseGeminiChatResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("error")) {
                JsonNode errorNode = rootNode.get("error");
                String errorMessage = errorNode.has("message") ? errorNode.get("message").asText() : "Unknown error";
                return LlmResponse.error("API Error: " + errorMessage);
            }
            
            if (rootNode.has("candidates") && rootNode.get("candidates").isArray()) {
                JsonNode candidatesArray = rootNode.get("candidates");
                if (candidatesArray.size() > 0) {
                    JsonNode firstCandidate = candidatesArray.get(0);
                    
                    if (firstCandidate.has("content")) {
                        JsonNode contentNode = firstCandidate.get("content");
                        if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                            JsonNode partsArray = contentNode.get("parts");
                            StringBuilder contentBuilder = new StringBuilder();
                            
                            for (int i = 0; i < partsArray.size(); i++) {
                                JsonNode part = partsArray.get(i);
                                if (part.has("text")) {
                                    contentBuilder.append(part.get("text").asText());
                                }
                            }
                            
                            return LlmResponse.success(contentBuilder.toString());
                        }
                    }
                }
            }
            
            return LlmResponse.error("Failed to parse response: invalid format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
    }

    private String parseGeminiStreamToken(String line) {
        try {
            if (line.trim().isEmpty() || !line.trim().startsWith("{")) {
                return null;
            }
            
            JsonNode rootNode = JsonUtils.parseTree(line);
            if (rootNode.has("candidates") && rootNode.get("candidates").isArray()) {
                JsonNode candidatesArray = rootNode.get("candidates");
                if (candidatesArray.size() > 0) {
                    JsonNode firstCandidate = candidatesArray.get(0);
                    if (firstCandidate.has("content")) {
                        JsonNode contentNode = firstCandidate.get("content");
                        if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                            JsonNode partsArray = contentNode.get("parts");
                            if (partsArray.size() > 0) {
                                JsonNode firstPart = partsArray.get(0);
                                if (firstPart.has("text")) {
                                    return firstPart.get("text").asText();
                                }
                            }
                        }
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
        return "Gemini";
    }
}
