package ltd.idcu.est.features.ai.impl.llm;

import ltd.idcu.est.features.ai.api.LlmClient;
import ltd.idcu.est.features.ai.api.LlmMessage;
import ltd.idcu.est.features.ai.api.LlmOptions;
import ltd.idcu.est.features.ai.api.LlmResponse;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractLlmClient implements LlmClient {

    protected String apiKey;
    protected String endpoint;
    protected String model;
    protected final HttpClient httpClient;
    protected int maxRetries = 3;
    protected long retryDelayMs = 1000;

    protected AbstractLlmClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    protected AbstractLlmClient(String apiKey) {
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
        return executeWithRetry(() -> doChat(messages, options));
    }

    protected abstract LlmResponse doChat(List<LlmMessage> messages, LlmOptions options);

    protected <T> T executeWithRetry(Supplier<T> action) {
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            try {
                return action.get();
            } catch (Exception e) {
                lastException = e;
                attempt++;
                if (attempt < maxRetries) {
                    log("Retry attempt " + attempt + " after error: " + e.getMessage());
                    sleep(retryDelayMs * attempt);
                }
            }
        }

        log("All retry attempts failed");
        return handleError(lastException);
    }

    protected void log(String message) {
        System.out.println("[LLM " + getName() + "] " + message);
    }

    protected void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected abstract <T> T handleError(Exception e);

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

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setRetryDelayMs(long retryDelayMs) {
        this.retryDelayMs = retryDelayMs;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isEmpty();
    }

    protected String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    protected String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                   .replace("\\\\", "\\")
                   .replace("\\n", "\n")
                   .replace("\\r", "\r")
                   .replace("\\t", "\t");
    }
}
