package ltd.idcu.est.ai.impl.embedding;

import ltd.idcu.est.utils.format.json.JsonUtils;
import ltd.idcu.est.utils.format.json.JsonNode;
import ltd.idcu.est.utils.format.json.ObjectNode;
import ltd.idcu.est.utils.format.json.ArrayNode;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CohereEmbeddingModel extends AbstractEmbeddingModel {
    
    public static final String DEFAULT_MODEL = "embed-v3.0";
    public static final String DEFAULT_ENDPOINT = "https://api.cohere.ai/v1/embed";
    private String inputType = "search_document";
    private String embeddingType = "float";
    
    public CohereEmbeddingModel() {
        super();
        this.model = DEFAULT_MODEL;
        this.endpoint = DEFAULT_ENDPOINT;
    }
    
    public CohereEmbeddingModel(String apiKey) {
        super(apiKey);
        this.model = DEFAULT_MODEL;
        this.endpoint = DEFAULT_ENDPOINT;
    }
    
    @Override
    public String getName() {
        return "cohere";
    }
    
    @Override
    public int getDimension() {
        switch (model) {
            case "embed-english-v3.0":
            case "embed-multilingual-v3.0":
            case "embed-v3.0":
                return 1024;
            case "embed-english-light-v3.0":
            case "embed-multilingual-light-v3.0":
                return 384;
            default:
                return 1024;
        }
    }
    
    @Override
    protected float[][] doEmbedBatch(String[] texts) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("Cohere API key is not set");
        }
        
        try {
            ObjectNode requestBody = JsonUtils.createObjectNode();
            requestBody.put("model", model);
            requestBody.put("input_type", inputType);
            requestBody.put("embedding_types", JsonUtils.createArrayNode().add(embeddingType));
            
            ArrayNode textsArray = JsonUtils.createArrayNode();
            for (String text : texts) {
                textsArray.add(text);
            }
            requestBody.set("texts", textsArray);
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JsonUtils.toJson(requestBody)))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                throw new RuntimeException("Cohere API error: " + response.statusCode() + " - " + response.body());
            }
            
            JsonNode responseJson = JsonUtils.parse(response.body());
            JsonNode embeddingsNode = responseJson.path("embeddings").path(embeddingType);
            
            List<float[]> embeddings = new ArrayList<>();
            if (embeddingsNode.isArray()) {
                for (JsonNode embeddingNode : embeddingsNode) {
                    if (embeddingNode.isArray()) {
                        float[] embedding = new float[embeddingNode.size()];
                        for (int i = 0; i < embeddingNode.size(); i++) {
                            embedding[i] = embeddingNode.get(i).floatValue();
                        }
                        embeddings.add(embedding);
                    }
                }
            }
            
            return embeddings.toArray(new float[0][]);
        } catch (Exception e) {
            throw wrapException(e);
        }
    }
    
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    
    public void setEmbeddingType(String embeddingType) {
        this.embeddingType = embeddingType;
    }
}
