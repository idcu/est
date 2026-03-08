package ltd.idcu.est.ai.impl.embedding;

import ltd.idcu.est.ai.api.vector.EmbeddingModel;
import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ZhipuAiEmbeddingModel extends AbstractEmbeddingModel implements EmbeddingModel {
    
    public ZhipuAiEmbeddingModel() {
        super();
        this.endpoint = "https://open.bigmodel.cn/api/paas/v4/embeddings";
        this.model = "embedding-3";
    }
    
    public ZhipuAiEmbeddingModel(String apiKey) {
        this();
        this.apiKey = apiKey;
    }
    
    @Override
    public String getName() {
        return "Zhipu AI Embeddings";
    }
    
    @Override
    public int getDimension() {
        if ("embedding-3".equals(model)) {
            return 1024;
        } else if ("embedding-2".equals(model)) {
            return 1024;
        } else if ("embedding-v2".equals(model)) {
            return 1024;
        }
        return 1024;
    }
    
    @Override
    protected float[][] doEmbedBatch(String[] texts) {
        try {
            String requestBody = buildRequestBody(texts);
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
                throw new RuntimeException("HTTP " + response.statusCode() + ": " + response.body());
            }
        } catch (IOException e) {
            throw new RuntimeException("Network error", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrupted", e);
        }
    }
    
    private String buildRequestBody(String[] texts) {
        ObjectNode requestNode = new ObjectNode();
        requestNode.set("model", model);
        
        ArrayNode inputArray = new ArrayNode();
        for (String text : texts) {
            inputArray.add(text);
        }
        requestNode.set("input", inputArray);
        
        return JsonUtils.toJson(requestNode);
    }
    
    private float[][] parseResponse(String jsonBody) {
        try {
            JsonNode rootNode = JsonUtils.parseTree(jsonBody);
            
            if (rootNode.has("data") && rootNode.get("data").isArray()) {
                JsonNode dataArray = rootNode.get("data");
                List<float[]> embeddings = new ArrayList<>();
                
                for (int i = 0; i < dataArray.size(); i++) {
                    JsonNode itemNode = dataArray.get(i);
                    if (itemNode.has("embedding") && itemNode.get("embedding").isArray()) {
                        JsonNode embeddingArray = itemNode.get("embedding");
                        float[] embedding = new float[embeddingArray.size()];
                        for (int j = 0; j < embeddingArray.size(); j++) {
                            embedding[j] = (float) embeddingArray.get(j).asDouble();
                        }
                        embeddings.add(embedding);
                    }
                }
                
                return embeddings.toArray(new float[0][]);
            }
            
            throw new RuntimeException("Invalid response format");
        } catch (Exception e) {
            log("Failed to parse response: " + e.getMessage());
            throw new RuntimeException("Failed to parse response", e);
        }
    }
}
