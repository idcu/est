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

public class ErnieLlmClient extends AbstractLlmClient {

    public ErnieLlmClient() {
        super();
        this.endpoint = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";
        this.model = "ernie-4.0";
    }

    public ErnieLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildChatRequestBody(messages, options, false);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "?access_token=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseErnieResponse(response.body());
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
            String requestBody = buildChatRequestBody(messages, options, true);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint + "?access_token=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            processErnieStream(response.body(), callback);
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

    protected String buildChatRequestBody(List<LlmMessage> messages, LlmOptions options) {
        return buildChatRequestBody(messages, options, false);
    }

    protected String buildChatRequestBody(List<LlmMessage> messages, LlmOptions options, boolean stream) {
        ObjectNode requestNode = new ObjectNode();
        
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
        if (stream) {
            requestNode.set("stream", true);
        }
        
        return JsonUtils.toJson(requestNode);
    }

    private LlmResponse parseErnieResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            if (rootNode.has("result")) {
                String content = rootNode.get("result").asText();
                return LlmResponse.success(content);
            }
            if (rootNode.has("error_msg")) {
                String errorMsg = rootNode.get("error_msg").asText();
                return LlmResponse.error("API Error: " + errorMsg);
            }
            return LlmResponse.error("Failed to parse response: invalid format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
    }

    private void processErnieStream(String responseBody, StreamCallback callback) {
        StringBuilder fullContent = new StringBuilder();
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

                try {
                    JsonNode rootNode = JsonUtils.parseTree(line);
                    if (rootNode.has("result")) {
                        String token = rootNode.get("result").asText();
                        fullContent.append(token);
                        callback.onToken(token, tokenIndex++);
                    }
                } catch (Exception e) {
                    log("Failed to parse Ernie stream data: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            callback.onError(new LlmException(LlmException.ErrorType.PARSE_ERROR, "Failed to process stream", e, getName()));
            return;
        }

        callback.onComplete(LlmResponse.success(fullContent.toString()));
    }

    @Override
    public String getName() {
        return "Ernie (文心一言)";
    }
}
