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
        System.out.println("=== EST AI Vector Store 示例 ===\n");
        
        AiAssistant assistant = Ai.create();
        VectorStore vectorStore = assistant.getVectorStore();
        
        vectorStore.connect(new HashMap<>());
        
        System.out.println("1. 创建集合...");
        String collectionName = "knowledge-base";
        int dimension = 128;
        vectorStore.createCollection(collectionName, dimension);
        System.out.println("✅ 集合 '" + collectionName + "' 创建成功，维度: " + dimension + "\n");
        
        System.out.println("2. 插入示例向量...");
        Random random = new Random(42);
        
        String[] docs = {
            "EST框架是一个零依赖的Java框架",
            "EST AI模块支持多种LLM提供商",
            "EST提供向量数据库功能",
            "EST支持RAG（检索增强生成）",
            "EST具有优秀的性能表现"
        };
        
        for (int i = 0; i < docs.length; i++) {
            float[] vector = generateRandomVector(dimension, random);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("content", docs[i]);
            metadata.put("category", "documentation");
            
            Vector vec = new DefaultVector("doc-" + i, vector, metadata);
            vectorStore.upsert(collectionName, vec);
            System.out.println("  插入文档 " + i + ": " + docs[i]);
        }
        System.out.println("✅ 共插入 " + vectorStore.count(collectionName) + " 个向量\n");
        
        System.out.println("3. 执行相似性搜索...");
        float[] queryVector = generateRandomVector(dimension, random);
        int topK = 3;
        
        List<Vector> results = vectorStore.search(collectionName, queryVector, topK);
        System.out.println("🔍 搜索结果 (Top " + topK + "):");
        for (int i = 0; i < results.size(); i++) {
            Vector result = results.get(i);
            String content = (String) result.getMetadata().get("content");
            System.out.printf("  %d. [得分: %.4f] %s%n", 
                i + 1, result.getScore(), content);
        }
        System.out.println();
        
        System.out.println("4. 获取单个向量...");
        Vector retrieved = vectorStore.get(collectionName, "doc-0");
        if (retrieved != null) {
            System.out.println("✅ 获取成功: " + retrieved.getMetadata().get("content"));
        }
        System.out.println();
        
        System.out.println("5. 删除向量...");
        vectorStore.delete(collectionName, "doc-4");
        System.out.println("✅ 删除后剩余: " + vectorStore.count(collectionName) + " 个向量\n");
        
        System.out.println("6. 清空集合...");
        vectorStore.clear(collectionName);
        System.out.println("✅ 清空后剩余: " + vectorStore.count(collectionName) + " 个向量\n");
        
        System.out.println("7. 列出所有集合...");
        List<String> collections = vectorStore.listCollections();
        System.out.println("📋 集合列表:");
        for (String col : collections) {
            System.out.println("  - " + col);
        }
        System.out.println();
        
        System.out.println("8. 删除集合...");
        vectorStore.deleteCollection(collectionName);
        System.out.println("✅ 集合 '" + collectionName + "' 已删除\n");
        
        vectorStore.disconnect();
        System.out.println("🎉 示例完成！");
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
