package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.vector.Vector;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.impl.Ai;
import ltd.idcu.est.ai.impl.vector.DefaultVector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VectorStoreExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI Vector Store Example ===\n");
        
        AiAssistant assistant = Ai.create();
        VectorStore vectorStore = assistant.getVectorStore();
        
        vectorStore.connect(new HashMap<>());
        
        System.out.println("1. Creating collection...");
        String collectionName = "knowledge-base";
        int dimension = 128;
        vectorStore.createCollection(collectionName, dimension);
        System.out.println("[X] Collection '" + collectionName + "' created, dimension: " + dimension + "\n");
        
        System.out.println("2. Inserting sample vectors...");
        Random random = new Random(42);
        
        String[] docs = {
            "EST Framework is a zero-dependency Java framework",
            "EST AI module supports multiple LLM providers",
            "EST provides vector database functionality",
            "EST supports RAG (Retrieval-Augmented Generation)",
            "EST has excellent performance characteristics"
        };
        
        for (int i = 0; i < docs.length; i++) {
            float[] vector = generateRandomVector(dimension, random);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("content", docs[i]);
            metadata.put("category", "documentation");
            
            Vector vec = new DefaultVector("doc-" + i, vector, metadata);
            vectorStore.upsert(collectionName, vec);
            System.out.println("  Inserted document " + i + ": " + docs[i]);
        }
        System.out.println("[X] Total inserted: " + vectorStore.count(collectionName) + " vectors\n");
        
        System.out.println("3. Performing similarity search...");
        float[] queryVector = generateRandomVector(dimension, random);
        int topK = 3;
        
        List<Vector> results = vectorStore.search(collectionName, queryVector, topK);
        System.out.println("[Search] Results (Top " + topK + "):");
        for (int i = 0; i < results.size(); i++) {
            Vector result = results.get(i);
            String content = (String) result.getMetadata().get("content");
            System.out.printf("  %d. [Score: %.4f] %s%n", 
                i + 1, result.getScore(), content);
        }
        System.out.println();
        
        System.out.println("4. Getting single vector...");
        Vector retrieved = vectorStore.get(collectionName, "doc-0");
        if (retrieved != null) {
            System.out.println("[X] Retrieved: " + retrieved.getMetadata().get("content"));
        }
        System.out.println();
        
        System.out.println("5. Deleting vector...");
        vectorStore.delete(collectionName, "doc-4");
        System.out.println("[X] Remaining after delete: " + vectorStore.count(collectionName) + " vectors\n");
        
        System.out.println("6. Clearing collection...");
        vectorStore.clear(collectionName);
        System.out.println("[X] Remaining after clear: " + vectorStore.count(collectionName) + " vectors\n");
        
        System.out.println("7. Listing all collections...");
        List<String> collections = vectorStore.listCollections();
        System.out.println("[List] Collection list:");
        for (String col : collections) {
            System.out.println("  - " + col);
        }
        System.out.println();
        
        System.out.println("8. Deleting collection...");
        vectorStore.deleteCollection(collectionName);
        System.out.println("[X] Collection '" + collectionName + "' deleted\n");
        
        vectorStore.disconnect();
        System.out.println("[X] Example complete!");
    }
    
    private static float[] generateRandomVector(int dimension, Random random) {
        float[] vector = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = (float) (random.nextGaussian() * 0.1);
        }
        return normalize(vector);
    }
    
    private static float[] normalize(float[] vector) {
        double norm = 0;
        for (float v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);
        if (norm > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= norm;
            }
        }
        return vector;
    }
}
