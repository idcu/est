package ltd.idcu.est.features.ai.impl.llm;

import ltd.idcu.est.features.ai.api.LlmMessage;
import ltd.idcu.est.features.ai.api.LlmOptions;
import ltd.idcu.est.features.ai.api.LlmResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class QwenLlmClient extends AbstractLlmClient {

    public QwenLlmClient() {
        super();
        this.endpoint = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";
        this.model = "qwen-max";
    }

    public QwenLlmClient(String apiKey) {
        this();
        this.apiKey = apiKey;
    }

    @Override
    protected LlmResponse doChat(List<LlmMessage> messages, LlmOptions options) {
        try {
            String requestBody = buildRequestBody(messages, options);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                return LlmResponse.error("API Error: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return LlmResponse.error("Error calling API: " + e.getMessage());
        }
    }

    private String buildRequestBody(List<LlmMessage> messages, LlmOptions options) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"model\": \"").append(model).append("\",\n");
        sb.append("  \"messages\": [\n");
        
        for (int i = 0; i < messages.size(); i++) {
            LlmMessage msg = messages.get(i);
            sb.append("    {\n");
            sb.append("      \"role\": \"").append(msg.getRole()).append("\",\n");
            sb.append("      \"content\": \"").append(escapeJson(msg.getContent())).append("\"\n");
            sb.append("    }");
            if (i < messages.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        
        sb.append("  ],\n");
        sb.append("  \"temperature\": ").append(options.getTemperature() != null ? options.getTemperature() : 0.7).append(",\n");
        sb.append("  \"max_tokens\": ").append(options.getMaxTokens() != null ? options.getMaxTokens() : 2000).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private LlmResponse parseResponse(String jsonBody) {
        try {
            int contentStart = jsonBody.indexOf("\"content\":\"") + 11;
            int contentEnd = jsonBody.indexOf("\"", contentStart);
            if (contentStart > 10 && contentEnd > contentStart) {
                String content = jsonBody.substring(contentStart, contentEnd);
                return LlmResponse.success(unescapeJson(content));
            }
        } catch (Exception e) {
            return LlmResponse.error("Failed to parse response: " + e.getMessage());
        }
        return LlmResponse.error("Failed to parse response");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T handleError(Exception e) {
        return (T) LlmResponse.error("Fatal error: " + e.getMessage());
    }

    @Override
    public String getName() {
        return "Qwen (通义千问)";
    }
}
