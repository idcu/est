package ltd.idcu.est.rag.impl;

import ltd.idcu.est.rag.api.Embedding;
import ltd.idcu.est.rag.api.EmbeddingModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SimpleEmbeddingModel implements EmbeddingModel {
    
    private final int dimension = 1536;
    private final Random random = new Random(42);
    private final String modelName = "simple-embedding";
    
    @Override
    public Embedding embed(String text) {
        Embedding embedding = new Embedding();
        embedding.setId(UUID.randomUUID().toString());
        embedding.setVector(generateRandomVector());
        return embedding;
    }
    
    @Override
    public List<Embedding> embed(List<String> texts) {
        List<Embedding> embeddings = new ArrayList<>();
        for (String text : texts) {
            embeddings.add(embed(text));
        }
        return embeddings;
    }
    
    @Override
    public float[] embedToVector(String text) {
        return generateRandomVector();
    }
    
    @Override
    public List<float[]> embedToVectors(List<String> texts) {
        List<float[]> vectors = new ArrayList<>();
        for (String text : texts) {
            vectors.add(embedToVector(text));
        }
        return vectors;
    }
    
    @Override
    public int getDimension() {
        return dimension;
    }
    
    @Override
    public String getModelName() {
        return modelName;
    }
    
    private float[] generateRandomVector() {
        float[] vector = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = (float) random.nextGaussian();
        }
        float norm = 0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = (float) Math.sqrt(norm);
        for (int i = 0; i < dimension; i++) {
            vector[i] /= norm;
        }
        return vector;
    }
}
