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

public class ZhipuAiLlmClient extends AbstractLlmClient {

    public ZhipuAiLlmClient() {
        super();
        this.endpoint = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
        this.model = "glm-4";
    }

    public ZhipuAiLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildChatRequestBody(messages, options, false);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseChatResponse(response.body());
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
            String requestBody = buildChatRequestBody(messages, options, true);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofLines())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            StringBuilder fullContent = new StringBuilder();
                            response.body().forEach(line -> {
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    if (!data.equals("[DONE]")) {
                                        try {
                                            String token = parseStreamToken(data);
                                            if (token != null) {
                                                fullContent.append(token);
                                                callback.onToken(token);
                                            }
                                        } catch (Exception e) {
                                        }
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

    private String parseStreamToken(String data) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(data);
            if (rootNode.has("choices") && rootNode.get("choices").isArray()) {
                JsonNode choicesArray = rootNode.get("choices");
                if (choicesArray.size() > 0) {
                    JsonNode firstChoice = choicesArray.get(0);
                    if (firstChoice.has("delta")) {
                        JsonNode deltaNode = firstChoice.get("delta");
                        if (deltaNode.has("content")) {
                            return deltaNode.get("content").asText();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T handleError(Exception e) {
        return (T) LlmResponse.error("Fatal error: " + e.getMessage());
    }

    @Override
    public String getName() {
        return "Zhipu AI";
    }
}
