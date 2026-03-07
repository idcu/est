package ltd.idcu.est.features.ai.impl.llm;

import ltd.idcu.est.features.ai.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ZhipuAiLlmClient implements LlmClient {

    private String apiKey;
    private String endpoint = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private String model = "glm-4";
    private final HttpClient httpClient;

    public ZhipuAiLlmClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public ZhipuAiLlmClient(String apiKey) {
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

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }

    @Override
    public String getName() {
        return "Zhipu AI";
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }
}
