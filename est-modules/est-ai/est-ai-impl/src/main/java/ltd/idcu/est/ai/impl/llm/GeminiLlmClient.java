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
import java.util.List;

public class GeminiLlmClient extends AbstractLlmClient {

    public GeminiLlmClient() {
        super();
        this.endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-pro-exp:generateContent";
        this.model = "gemini-2.0-pro-exp";
    }

    public GeminiLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildGeminiRequestBody(messages, options, false);
            String urlWithKey = endpoint + "?key=" + apiKey;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseGeminiResponse(response.body());
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
            String streamEndpoint = endpoint.replace(":generateContent", ":streamGenerateContent");
            String requestBody = buildGeminiRequestBody(messages, options, true);
            String urlWithKey = streamEndpoint + "?key=" + apiKey + "&alt=sse";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlWithKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            processGeminiSseStream(response.body(), callback);
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

    private String buildGeminiRequestBody(List<LlmMessage> messages, LlmOptions options, boolean stream) {
        ObjectNode requestNode = new ObjectNode();
        ArrayNode contentsArray = new ArrayNode();
        
        for (LlmMessage message : messages) {
            ObjectNode contentNode = new ObjectNode();
            
            String role;
            if ("user".equals(message.getRole())) {
                role = "user";
            } else if ("assistant".equals(message.getRole())) {
                role = "model";
            } else {
                continue;
            }
            
            contentNode.set("role", role);
            
            ArrayNode partsArray = new ArrayNode();
            ObjectNode partNode = new ObjectNode();
            partNode.set("text", message.getContent());
            partsArray.add(partNode);
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
        
        if (generationConfig.size() > 0) {
            requestNode.set("generationConfig", generationConfig);
        }
        
        return JsonUtils.toJson(requestNode);
    }

    private LlmResponse parseGeminiResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("candidates") && rootNode.get("candidates").isArray()) {
                JsonNode candidatesArray = rootNode.get("candidates");
                if (candidatesArray.size() > 0) {
                    JsonNode firstCandidate = candidatesArray.get(0);
                    if (firstCandidate.has("content")) {
                        JsonNode contentNode = firstCandidate.get("content");
                        if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                            JsonNode partsArray = contentNode.get("parts");
                            StringBuilder fullContent = new StringBuilder();
                            for (int i = 0; i < partsArray.size(); i++) {
                                JsonNode partNode = partsArray.get(i);
                                if (partNode.has("text")) {
                                    fullContent.append(partNode.get("text").asText());
                                }
                            }
                            return LlmResponse.success(fullContent.toString());
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

    private void processGeminiSseStream(String responseBody, StreamCallback callback) {
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
                        
                        if (rootNode.has("candidates") && rootNode.get("candidates").isArray()) {
                            JsonNode candidatesArray = rootNode.get("candidates");
                            if (candidatesArray.size() > 0) {
                                JsonNode firstCandidate = candidatesArray.get(0);
                                
                                if (firstCandidate.has("finishReason")) {
                                    finishReason = firstCandidate.get("finishReason").asText();
                                }
                                
                                if (firstCandidate.has("content")) {
                                    JsonNode contentNode = firstCandidate.get("content");
                                    if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                                        JsonNode partsArray = contentNode.get("parts");
                                        for (int i = 0; i < partsArray.size(); i++) {
                                            JsonNode partNode = partsArray.get(i);
                                            if (partNode.has("text")) {
                                                String token = partNode.get("text").asText();
                                                fullContent.append(token);
                                                callback.onToken(token, tokenIndex++);
                                            }
                                        }
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
    public String getName() {
        return "Google Gemini";
    }
}
