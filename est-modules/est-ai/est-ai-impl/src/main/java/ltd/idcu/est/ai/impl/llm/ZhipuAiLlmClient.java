package ltd.idcu.est.ai.impl.llm;

import ltd.idcu.est.ai.api.*;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            processSseStream(response.body(), callback);
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

    @Override
    public String getName() {
        return "Zhipu AI";
    }
}
