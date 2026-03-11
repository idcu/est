package ltd.idcu.est.rag.impl;

import ltd.idcu.est.llm.api.LlmClient;
import ltd.idcu.est.llm.api.LlmMessage;
import ltd.idcu.est.rag.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultRagEngine implements RagEngine {
    
    private VectorStore vectorStore;
    private TextSplitter textSplitter;
    private EmbeddingModel embeddingModel;
    private LlmClient llmClient;
    
    public DefaultRagEngine() {
        this.vectorStore = new InMemoryVectorStore();
        this.textSplitter = new FixedSizeTextSplitter();
        this.embeddingModel = new SimpleEmbeddingModel();
        this.vectorStore.setEmbeddingModel(this.embeddingModel);
    }
    
    public DefaultRagEngine(VectorStore vectorStore, TextSplitter textSplitter, 
                            EmbeddingModel embeddingModel, LlmClient llmClient) {
        this.vectorStore = vectorStore;
        this.textSplitter = textSplitter;
        this.embeddingModel = embeddingModel;
        this.llmClient = llmClient;
        this.vectorStore.setEmbeddingModel(this.embeddingModel);
    }
    
    @Override
    public void addDocument(Document document) {
        List<DocumentChunk> chunks = textSplitter.split(document);
        
        for (DocumentChunk chunk : chunks) {
            float[] vector = embeddingModel.embedToVector(chunk.getContent());
            Embedding embedding = new Embedding();
            embedding.setId(UUID.randomUUID().toString());
            embedding.setVector(vector);
            embedding.setChunkId(chunk.getId());
            embedding.setDocumentId(document.getId());
            
            vectorStore.addEmbedding(embedding);
            
            if (vectorStore instanceof InMemoryVectorStore) {
                ((InMemoryVectorStore) vectorStore).addChunkContent(chunk.getId(), chunk.getContent());
            }
        }
    }
    
    @Override
    public void addDocuments(List<Document> documents) {
        for (Document document : documents) {
            addDocument(document);
        }
    }
    
    @Override
    public List<SearchResult> retrieve(String query, int topK) {
        return vectorStore.search(query, topK);
    }
    
    @Override
    public String generate(String query, List<SearchResult> contexts) {
        if (llmClient == null) {
            return buildMockResponse(query, contexts);
        }
        
        StringBuilder contextBuilder = new StringBuilder();
        for (int i = 0; i < contexts.size(); i++) {
            contextBuilder.append("[").append(i + 1).append("] ");
            contextBuilder.append(contexts.get(i).getContent()).append("\n\n");
        }
        
        String prompt = buildPrompt(query, contextBuilder.toString());
        
        List<LlmMessage> messages = new ArrayList<>();
        messages.add(LlmMessage.system("你是一个专业的问答助手。请基于提供的上下文信息回答用户的问题。"));
        messages.add(LlmMessage.user(prompt));
        
        return llmClient.chat(messages).getContent();
    }
    
    @Override
    public String retrieveAndGenerate(String query, int topK) {
        List<SearchResult> contexts = retrieve(query, topK);
        return generate(query, contexts);
    }
    
    @Override
    public void setVectorStore(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.vectorStore.setEmbeddingModel(this.embeddingModel);
    }
    
    @Override
    public void setTextSplitter(TextSplitter textSplitter) {
        this.textSplitter = textSplitter;
    }
    
    @Override
    public void setEmbeddingModel(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        this.vectorStore.setEmbeddingModel(this.embeddingModel);
    }
    
    public void setLlmClient(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
    
    @Override
    public VectorStore getVectorStore() {
        return vectorStore;
    }
    
    @Override
    public TextSplitter getTextSplitter() {
        return textSplitter;
    }
    
    @Override
    public EmbeddingModel getEmbeddingModel() {
        return embeddingModel;
    }
    
    private String buildPrompt(String query, String context) {
        return "请基于以下上下文信息回答问题：\n\n" +
               "上下文信息：\n" + context + "\n" +
               "问题：" + query + "\n\n" +
               "请根据上下文信息回答问题。如果上下文中没有相关信息，请说明。";
    }
    
    private String buildMockResponse(String query, List<SearchResult> contexts) {
        StringBuilder response = new StringBuilder();
        response.append("基于 ").append(contexts.size()).append(" 个相关文档片段回答：\n\n");
        
        for (int i = 0; i < Math.min(3, contexts.size()); i++) {
            response.append("相关片段 ").append(i + 1).append(" (得分: ")
                   .append(String.format("%.2f", contexts.get(i).getScore())).append("):\n");
            response.append(contexts.get(i).getContent()).append("\n\n");
        }
        
        response.append("问题：").append(query).append("\n");
        response.append("（这是模拟响应。配置 LlmClient 后可获得真实 AI 回答）");
        
        return response.toString();
    }
}
