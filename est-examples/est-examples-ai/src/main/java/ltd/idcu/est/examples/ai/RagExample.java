package ltd.idcu.est.examples.ai;

import ltd.idcu.est.ai.api.rag.*;
import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.api.vector.EmbeddingModel;
import ltd.idcu.est.ai.impl.llm.LlmClientFactory;
import ltd.idcu.est.ai.impl.embedding.EmbeddingModelFactory;
import ltd.idcu.est.ai.impl.rag.*;

import java.util.List;

public class RagExample {
    
    public static void main(String[] args) {
        System.out.println("=== EST AI RAG (检索增强生�? 示例 ===\n");
        
        System.out.println("1. 初始化组�?..");
        
        DocumentChunker chunker = new DefaultDocumentChunker(500, 100);
        System.out.println("   �?文档分块器已创建 (chunkSize: 500, overlap: 100)");
        
        EmbeddingModel embeddingModel = createSimpleEmbeddingModel();
        System.out.println("   �?Embedding模型已创�?);
        
        RagRetriever retriever = new DefaultRagRetriever(embeddingModel, chunker);
        System.out.println("   �?RAG检索器已创�?);
        
        LlmClient llmClient = createSimpleLlmClient();
        System.out.println("   �?LLM客户端已创建\n");
        
        System.out.println("2. 创建RAG管道...");
        RagPipeline ragPipeline = new DefaultRagPipeline(retriever, llmClient);
        System.out.println("   �?RAG管道已创建\n");
        
        System.out.println("3. 添加示例文档...");
        addSampleDocuments(ragPipeline);
        System.out.println("   �?示例文档已添加\n");
        
        System.out.println("4. 测试RAG查询...");
        String[] queries = {
            "EST框架的特点是什么？",
            "EST AI支持哪些功能�?,
            "如何使用EST的向量数据库�?
        };
        
        for (String query : queries) {
            System.out.println("\n   📝 查询: " + query);
            System.out.println("   ".repeat(40));
            
            RagResponse response = ragPipeline.query(query, 3);
            
            if (response.isSuccess()) {
                System.out.println("   🎯 回答:");
                System.out.println(wrapText(response.getAnswer(), 60, "      "));
                
                if (!response.getContextChunks().isEmpty()) {
                    System.out.println("\n   📚 参考上下文:");
                    for (int i = 0; i < response.getContextChunks().size(); i++) {
                        DocumentChunk chunk = response.getContextChunks().get(i);
                        System.out.printf("      [%d] %s...%n", 
                            i + 1, 
                            chunk.getContent().substring(0, Math.min(50, chunk.getContent().length())));
                    }
                }
            } else {
                System.out.println("   �?错误: " + response.getErrorMessage());
            }
            System.out.println();
        }
        
        System.out.println("5. 清理资源...");
        ragPipeline.clear();
        System.out.println("   �?已清理\n");
        
        System.out.println("🎉 RAG示例完成�?);
        System.out.println("\n提示:");
        System.out.println("  - 在实际使用中，请配置真实的API密钥");
        System.out.println("  - EmbeddingModel使用: " + embeddingModel.getName());
        System.out.println("  - LLM使用: " + llmClient.getName());
        System.out.println("  - 查看更多示例: est-examples-ai模块");
    }
    
    private static EmbeddingModel createSimpleEmbeddingModel() {
        return new EmbeddingModel() {
            @Override
            public String getName() {
                return "Simple Embedding (Demo)";
            }
            
            @Override
            public float[] embed(String text) {
                float[] embedding = new float[128];
                for (int i = 0; i < 128; i++) {
                    embedding[i] = (float) (Math.sin(text.hashCode() * (i + 1)) * 0.1);
                }
                return normalize(embedding);
            }
            
            @Override
            public float[][] embedBatch(String[] texts) {
                float[][] embeddings = new float[texts.length][];
                for (int i = 0; i < texts.length; i++) {
                    embeddings[i] = embed(texts[i]);
                }
                return embeddings;
            }
            
            @Override
            public int getDimension() {
                return 128;
            }
            
            private float[] normalize(float[] vec) {
                double norm = 0;
                for (float v : vec) norm += v * v;
                norm = Math.sqrt(norm);
                if (norm > 0) {
                    for (int i = 0; i < vec.length; i++) {
                        vec[i] /= norm;
                    }
                }
                return vec;
            }
        };
    }
    
    private static LlmClient createSimpleLlmClient() {
        return new LlmClient() {
            @Override
            public String generate(String prompt) {
                return generate(prompt, null);
            }
            
            @Override
            public String generate(String prompt, Object options) {
                return "这是一个演示回答。在实际使用中，请配置真实的LLM API密钥。\n" +
                       "您的查询是关�? " + prompt.substring(0, Math.min(50, prompt.length())) + "...";
            }
            
            @Override
            public Object chat(List messages) {
                return null;
            }
            
            @Override
            public Object chat(List messages, Object options) {
                return null;
            }
            
            @Override
            public void chatStream(List messages, Object callback) {}
            
            @Override
            public void chatStream(List messages, Object options, Object callback) {}
            
            @Override
            public void setFunctionRegistry(Object registry) {}
            
            @Override
            public Object getFunctionRegistry() { return null; }
            
            @Override
            public String getName() { return "Simple LLM (Demo)"; }
            
            @Override
            public String getModel() { return "demo-model"; }
            
            @Override
            public void setModel(String model) {}
            
            @Override
            public void setApiKey(String apiKey) {}
            
            @Override
            public void setEndpoint(String endpoint) {}
            
            @Override
            public boolean isAvailable() { return true; }
        };
    }
    
    private static void addSampleDocuments(RagPipeline pipeline) {
        String[] docs = {
            """
            EST框架是一个零依赖的Java框架，设计理念是简洁、高效、易于学习�?            它的核心特点包括：零依赖设计，仅使用Java标准库；模块化架构，
            功能按模块划分；AI友好，专为AI代码生成优化；云原生支持�?            支持GraalVM原生镜像；渐进式学习，学习曲线平缓�?            """,
            """
            EST AI模块提供了丰富的AI功能，包括：多LLM提供商支持，
            支持OpenAI、智谱AI、通义千问、文心一言、Anthropic Claude�?            Google Gemini、Mistral AI等；向量数据库，支持内存存储和扩展；
            文档分块，智能文本分割；RAG管道，检索增强生成；
            技能系统，插件式功能扩展；MCP协议支持，与Claude Desktop等集成�?            """,
            """
            EST的向量数据库使用非常简单。首先获取VectorStore实例�?            然后创建集合，设置维度。接着插入向量数据，每个向量包含ID�?            值数组和元数据。搜索时使用查询向量，返回最相似的Top K结果�?            支持余弦相似度计算，线程安全的内存实现适合开发和测试�?            """,
            """
            EST框架的性能表现优秀。启动时间仅需几十毫秒，内存占用低�?            包体积小。零依赖设计确保了无依赖冲突，部署简单�?            支持GraalVM原生镜像编译，进一步提升启动速度和降低内存占用�?            适合Serverless、微服务、边缘计算等场景�?            """
        };
        
        for (int i = 0; i < docs.length; i++) {
            Document doc = new DefaultDocument(docs[i], "sample-doc-" + i);
            pipeline.addDocument(doc);
            System.out.println("   - 添加文档 " + (i + 1) + ": " + 
                docs[i].substring(0, Math.min(30, docs[i].length())) + "...");
        }
    }
    
    private static String wrapText(String text, int maxWidth, String indent) {
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder(indent);
        
        for (String word : words) {
            if (line.length() + word.length() > maxWidth && line.length() > indent.length()) {
                result.append(line).append("\n");
                line = new StringBuilder(indent);
            }
            if (line.length() > indent.length()) {
                line.append(" ");
            }
            line.append(word);
        }
        
        if (line.length() > indent.length()) {
            result.append(line);
        }
        
        return result.toString();
    }
}
