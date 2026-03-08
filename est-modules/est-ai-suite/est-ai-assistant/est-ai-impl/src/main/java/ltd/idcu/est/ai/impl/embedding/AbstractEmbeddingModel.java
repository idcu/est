package ltd.idcu.est.ai.impl.embedding;

import ltd.idcu.est.ai.api.vector.EmbeddingModel;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class AbstractEmbeddingModel implements EmbeddingModel {
    
    protected String apiKey;
    protected String endpoint;
    protected String model;
    protected final HttpClient httpClient;
    protected int maxRetries = 3;
    protected long retryDelayMs = 1000;
    
    protected AbstractEmbeddingModel() {
        this.httpClient = HttpClient.newHttpClient();
    }
    
    protected AbstractEmbeddingModel(String apiKey) {
        this();
        this.apiKey = apiKey;
    }
    
    @Override
    public float[] embed(String text) {
        float[][] embeddings = embedBatch(new String[]{text});
        return embeddings.length > 0 ? embeddings[0] : new float[0];
    }
    
    @Override
    public float[][] embedBatch(String[] texts) {
        return executeWithRetry(() -> doEmbedBatch(texts));
    }
    
    protected abstract float[][] doEmbedBatch(String[] texts);
    
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
                } else {
                    break;
                }
            }
        }
        
        log("All retry attempts failed");
        throw wrapException(lastException);
    }
    
    protected RuntimeException wrapException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Embedding error: " + e.getMessage(), e);
    }
    
    protected void log(String message) {
        System.out.println("[Embedding " + getName() + "] " + message);
    }
    
    protected void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
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
}
