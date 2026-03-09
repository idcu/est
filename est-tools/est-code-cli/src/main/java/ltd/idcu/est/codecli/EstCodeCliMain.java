package ltd.idcu.est.codecli;

import ltd.idcu.est.ai.api.AiAssistant;
import ltd.idcu.est.ai.impl.DefaultAiAssistant;
import ltd.idcu.est.codecli.config.CliConfig;

public class EstCodeCliMain {
    
    public static void main(String[] args) {
        CliConfig config = CliConfig.load();
        
        AiAssistant aiAssistant = createAiAssistant(config);
        
        CliInteractionHandler handler = new CliInteractionHandler(
            aiAssistant, 
            config.getWorkDir(), 
            config.getNickname()
        );
        
        handler.start();
    }
    
    private static AiAssistant createAiAssistant(CliConfig config) {
        DefaultAiAssistant assistant = new DefaultAiAssistant();
        return assistant;
    }
}
