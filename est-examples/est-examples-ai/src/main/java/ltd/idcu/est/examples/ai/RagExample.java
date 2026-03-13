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
        System.out.println("=== EST AI RAG (Retrieval-Augmented Generation) Example ===\n");
        
        System.out.println("1. Initializing components...");
        
        DocumentChunker chunker = new DefaultDocumentChunker(500, 100);
        System.out.println("   [X] Document chunker created (chunkSize: 500, overlap: 100)");
        
        EmbeddingModel embeddingModel = createSimpleEmbeddingModel();
        System.out.println("   [X] Embedding model created");
        
        RagRetriever retriever = new DefaultRagRetriever(embeddingModel, chunker);
        System.out.println("   [X] RAG retriever created");
        
        LlmClient llmClient = createSimpleLlmClient();
        System.out.println("   [X] LLM client created\n");
        
        System.out.println("2. Creating RAG pipeline...");
        RagPipeline ragPipeline = new DefaultRagPipeline(retriever, llmClient);
        System.out.println("   [X] RAG pipeline created\n");
        
        System.out.println("3. Adding sample documents...");
        addSampleDocuments(ragPipeline);
        System.out.println("   [X] Sample documents added\n");
        
        System.out.println("4. Testing RAG queries...");
        String[] queries = {
            "What are the characteristics of EST Framework?",
            "What features does EST AI support?",
            "How to use EST's vector database?"
        };
        
        for (String query : queries) {
            System.out.println("\n   [X] Query: " + query);
            System.out.println("   ".repeat(40));
            
            RagResponse response = ragPipeline.query(query, 3);
            
            if (response.isSuccess()) {
                System.out.println("   [X] Answer:");
                System.out.println(wrapText(response.getAnswer(), 60, "      "));
                
                if (!response.getContextChunks().isEmpty()) {
                    System.out.println("\n   [X] Reference context:");
                    for (int i = 0; i < response.getContextChunks().size(); i++) {
                        DocumentChunk chunk = response.getContextChunks().get(i);
                        System.out.printf("      [%d] %s...%n", 
                            i + 1, 
                            chunk.getContent().substring(0, Math.min(50, chunk.getContent().length())));
                    }
                }
            } else {
                System.out.println("   [ ] Error: " + response.getErrorMessage());
            }
            System.out.println();
        }
        
        System.out.println("5. Cleaning up resources...");
        ragPipeline.clear();
        System.out.println("   [X] Cleaned up\n");
        
        System.out.println("[X] RAG example complete!");
        System.out.println("\nTips:");
        System.out.println("  - In actual use, please configure real API keys");
        System.out.println("  - EmbeddingModel using: " + embeddingModel.getName());
        System.out.println("  - LLM using: " + llmClient.getName());
        System.out.println("  - See more examples: est-examples-ai module");
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
                return "This is a demo answer. In actual use, please configure real LLM API keys.\n" +
                       "Your query is about: " + prompt.substring(0, Math.min(50, prompt.length())) + "...";
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
            EST Framework is a zero-dependency Java framework designed with simplicity, efficiency, and ease of learning in mind.
            Its core features include: zero-dependency design using only Java standard library; modular architecture
            with features divided by modules; AI-friendly, optimized for AI code generation; cloud-native support
            with GraalVM native image; progressive learning with gentle learning curve.
            """,
            """
            EST AI module provides rich AI features including: multi-LLM provider support
            for OpenAI, Zhipu AI, Qwen, Ernie, Anthropic Claude, Google Gemini, Mistral AI, etc.;
            vector database with in-memory storage and extensibility; document chunking with intelligent text splitting;
            RAG pipeline for retrieval-augmented generation; skill system with plugin-based feature extension;
            MCP protocol support for integration with Claude Desktop, etc.
            """,
            """
            EST's vector database is very simple to use. First get the VectorStore instance,
            then create a collection and set the dimension. Then insert vector data, each vector containing ID,
            value array and metadata. When searching, use a query vector and return the most similar Top K results.
            Supports cosine similarity calculation, thread-safe in-memory implementation suitable for development and testing.
            """,
            """
            EST Framework has excellent performance. Startup time only takes tens of milliseconds, low memory footprint,
            small package size. Zero-dependency design ensures no dependency conflicts, simple deployment.
            Supports GraalVM native image compilation to further improve startup speed and reduce memory footprint.
            Suitable for Serverless, microservices, edge computing and other scenarios.
            """
        };
        
        for (int i = 0; i < docs.length; i++) {
            Document doc = new DefaultDocument(docs[i], "sample-doc-" + i);
            pipeline.addDocument(doc);
            System.out.println("   - Added document " + (i + 1) + ": " + 
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
