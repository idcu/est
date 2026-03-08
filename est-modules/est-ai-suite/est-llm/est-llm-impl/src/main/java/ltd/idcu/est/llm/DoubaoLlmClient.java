package ltd.idcu.est.llm;

import ltd.idcu.est.llm.api.LlmMessage;
import ltd.idcu.est.llm.api.LlmOptions;
import ltd.idcu.est.llm.api.LlmResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DoubaoLlmClient extends AbstractLlmClient {

    public DoubaoLlmClient() {
        super();
        this.endpoint = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
        this.model = "ep-20241203143730-6jvq9";
    }

    public DoubaoLlmClient(String apiKey) {
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
    @SuppressWarnings("unchecked")
    protected <T> T handleError(Exception e) {
        return (T) LlmResponse.error("Fatal error: " + e.getMessage());
    }

    @Override
    public String getName() {
        return "Doubao (豆包)";
    }
}
