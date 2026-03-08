package ltd.idcu.est.ai.api;

import ltd.idcu.est.ai.api.config.AiConfig;
import ltd.idcu.est.ai.api.config.ConfigLoader;
import ltd.idcu.est.ai.api.skill.SkillRegistry;
import ltd.idcu.est.ai.api.storage.PromptTemplateRepository;
import ltd.idcu.est.ai.api.storage.SkillRepository;
import ltd.idcu.est.ai.api.storage.StorageProvider;
import ltd.idcu.est.ai.api.vector.VectorStore;
import ltd.idcu.est.ai.api.vector.EmbeddingModel;

import java.util.List;
import java.util.Map;

public interface AiAssistant {
    
    PromptTemplateRegistry getTemplateRegistry();
    
    CodeGenerator getCodeGenerator();
    
    ProjectScaffold getProjectScaffold();
    
    LlmClient getLlmClient();
    
    void setLlmClient(LlmClient llmClient);
    
    RequirementParser getRequirementParser();
    
    ArchitectureDesigner getArchitectureDesigner();
    
    TestAndDeployManager getTestAndDeployManager();
    
    AiConfig getConfig();
    
    void setConfig(AiConfig config);
    
    void reloadConfig();
    
    void reloadConfig(ConfigLoader loader);
    
    String getQuickReference(String topic);
    
    String getBestPractice(String category);
    
    String getTutorial(String topic);
    
    String generatePrompt(String templateName, Map<String, String> variables);
    
    String suggestCode(String requirement);
    
    String explainCode(String code);
    
    String optimizeCode(String code);
    
    String chat(String message);
    
    String chat(List<LlmMessage> messages);
    
    SkillRegistry getSkillRegistry();
    
    StorageProvider getStorageProvider();
    
    void setStorageProvider(StorageProvider storageProvider);
    
    SkillRepository getSkillRepository();
    
    PromptTemplateRepository getPromptTemplateRepository();
    
    VectorStore getVectorStore();
    
    void setVectorStore(VectorStore vectorStore);
    
    EmbeddingModel getEmbeddingModel();
    
    void setEmbeddingModel(EmbeddingModel embeddingModel);
}
