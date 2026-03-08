package ltd.idcu.est.ai.impl.rag;

import ltd.idcu.est.ai.api.rag.*;
import ltd.idcu.est.ai.api.LlmClient;
import ltd.idcu.est.ai.api.LlmMessage;
import ltd.idcu.est.ai.api.LlmResponse;

import java.util.ArrayList;
import java.util.List;

public class DefaultRagPipeline implements RagPipeline {
    
    private static final int DEFAULT_TOP_K = 5;
    
    private RagRetriever retriever;
    private LlmClient llmClient;
    private String systemPrompt;
    
    public DefaultRagPipeline(RagRetriever retriever, LlmClient llmClient) {
        this(retriever, llmClient, buildDefaultSystemPrompt());
    }
    
    public DefaultRagPipeline(RagRetriever retriever, LlmClient llmClient, String systemPrompt) {
        this.retriever = retriever;
        this.llmClient = llmClient;
        this.systemPrompt = systemPrompt;
    }
    
    @Override
    public RagResponse query(String query) {
        return query(query, DEFAULT_TOP_K);
    }
    
    @Override
    public RagResponse query(String query, int topK) {
        try {
            List<DocumentChunk> contextChunks = retriever.retrieve(query, topK);
            
            String context = buildContext(contextChunks);
            String prompt = buildPrompt(query, context);
            
            List<LlmMessage> messages = new ArrayList<>();
            messages.add(new LlmMessage("system", systemPrompt));
            messages.add(new LlmMessage("user", prompt));
            
            LlmResponse llmResponse = llmClient.chat(messages);
            
            if (llmResponse.isSuccess()) {
                return DefaultRagResponse.success(llmResponse.getContent(), contextChunks, query);
            } else {
                return DefaultRagResponse.error(llmResponse.getErrorMessage(), query);
            }
        } catch (Exception e) {
            return DefaultRagResponse.error(e.getMessage(), query);
        }
    }
    
    private String buildContext(List<DocumentChunk> chunks) {
        StringBuilder context = new StringBuilder();
        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            context.append("--- 上下�?").append(i + 1).append(" ---\n");
            context.append(chunk.getContent()).append("\n\n");
        }
        return context.toString();
    }
    
    private String buildPrompt(String query, String context) {
        return """
            请根据以下上下文信息回答用户的问题�?            
            上下文信息：
            %s
            
            用户问题�?s
            
            请基于上下文信息给出准确的回答。如果上下文中没有相关信息，请诚实说明�?            """.formatted(context, query);
    }
    
    private static String buildDefaultSystemPrompt() {
        return "你是一个有用的AI助手，擅长基于提供的上下文信息回答问题。请保持回答准确、简洁�?;
    }
    
    @Override
    public void addDocument(Document document) {
        retriever.addDocument(document);
    }
    
    @Override
    public void addDocuments(List<Document> documents) {
        for (Document document : documents) {
            retriever.addDocument(document);
        }
    }
    
    @Override
    public void clear() {
        retriever.clear();
    }
    
    @Override
    public RagRetriever getRetriever() {
        return retriever;
    }
    
    @Override
    public void setRetriever(RagRetriever retriever) {
        this.retriever = retriever;
    }
    
    public void setLlmClient(LlmClient llmClient) {
        this.llmClient = llmClient;
    }
    
    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }
}
