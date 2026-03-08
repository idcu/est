package ltd.idcu.est.ai.test.performance;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.api.vector.Vector;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.impl.Ai;
import ltd.idcu.est.ai.impl.vector.DefaultVector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VectorStoreBenchmark {
    
    private static final int DIMENSION = 128;
    private static final int NUM_VECTORS = 1000;
    private static final Random random = new Random(42);
    
    public static void main(String[] args) {
        System.out.println("=== EST AI Vector Store 性能基准测试 ===\n");
        
        BenchmarkRunner runner = new BenchmarkRunner(5, 50);
        
        AiAssistant assistant = Ai.create();
        VectorStore vectorStore = assistant.getVectorStore();
        vectorStore.connect(new HashMap<>());
        
        String collectionName = "benchmark-collection";
        
        System.out.println("准备测试数据...");
        Vector[] testVectors = new Vector[NUM_VECTORS];
        float[][] queryVectors = new float[100][];
        for (int i = 0; i < NUM_VECTORS; i++) {
            float[] vec = generateRandomVector(DIMENSION);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("index", i);
            testVectors[i] = new DefaultVector("vec-" + i, vec, metadata);
            if (i < 100) {
                queryVectors[i] = generateRandomVector(DIMENSION);
            }
        }
        System.out.println("✅ 测试数据准备完成\n");
        
        vectorStore.createCollection(collectionName, DIMENSION);
        
        runner.run("VectorStore.upsert (single)", () -> {
            Vector vec = new DefaultVector("test-single", generateRandomVector(DIMENSION), new HashMap<>());
            vectorStore.upsert(collectionName, vec);
        });
        
        vectorStore.clear(collectionName);
        
        runner.run("VectorStore.upsert (batch " + NUM_VECTORS + ")", () -> {
            for (int i = 0; i < NUM_VECTORS; i++) {
                vectorStore.upsert(collectionName, testVectors[i]);
            }
        });
        
        runner.run("VectorStore.get", () -> {
            vectorStore.get(collectionName, "vec-500");
        });
        
        runner.run("VectorStore.search (top 5)", () -> {
            vectorStore.search(collectionName, queryVectors[0], 5);
        });
        
        runner.run("VectorStore.search (top 50)", () -> {
            vectorStore.search(collectionName, queryVectors[0], 50);
        });
        
        runner.run("VectorStore.count", () -> {
            vectorStore.count(collectionName);
        });
        
        runner.run("VectorStore.delete (single)", () -> {
            vectorStore.delete(collectionName, "vec-999");
        });
        
        vectorStore.upsert(collectionName, testVectors[999]);
        
        vectorStore.clear(collectionName);
        runner.run("VectorStore.clear", () -> {
            for (int i = 0; i < NUM_VECTORS; i++) {
                vectorStore.upsert(collectionName, testVectors[i]);
            }
            vectorStore.clear(collectionName);
        });
        
        vectorStore.deleteCollection(collectionName);
        vectorStore.disconnect();
        
        runner.printSummary();
        
        System.out.println("\n🎉 基准测试完成！");
    }
    
    private static float[] generateRandomVector(int dimension) {
        float[] vector = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            vector[i] = (float) random.nextGaussian() * 0.1;
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
