package ltd.idcu.est.codecli.agent;

import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.impl.DefaultAgent;
import ltd.idcu.est.agent.impl.InMemoryMemory;
import ltd.idcu.est.agent.skill.CalculatorSkill;
import ltd.idcu.est.agent.skill.WebSearchSkill;
import ltd.idcu.est.codecli.agent.skill.CodeGenerationSkill;
import ltd.idcu.est.codecli.agent.skill.CodeExplanationSkill;
import ltd.idcu.est.codecli.agent.skill.CodeOptimizationSkill;
import ltd.idcu.est.codecli.agent.skill.BugFixSkill;
import ltd.idcu.est.codecli.agent.skill.DocumentationSkill;

public class AgentManager {
    
    private static AgentManager instance;
    private Agent agent;
    private Memory memory;
    
    private AgentManager() {
        initialize();
    }
    
    public static synchronized AgentManager getInstance() {
        if (instance == null) {
            instance = new AgentManager();
        }
        return instance;
    }
    
    private void initialize() {
        memory = new InMemoryMemory();
        agent = new DefaultAgent(memory);
        
        agent.registerSkill(new WebSearchSkill());
        agent.registerSkill(new CalculatorSkill());
        agent.registerSkill(new CodeGenerationSkill());
        agent.registerSkill(new CodeExplanationSkill());
        agent.registerSkill(new CodeOptimizationSkill());
        agent.registerSkill(new BugFixSkill());
        agent.registerSkill(new DocumentationSkill());
    }
    
    public Agent getAgent() {
        return agent;
    }
    
    public Memory getMemory() {
        return memory;
    }
    
    public String processQuery(String query) {
        return processQuery(query, 10);
    }
    
    public String processQuery(String query, int maxSteps) {
        AgentRequest request = new AgentRequest();
        request.setQuery(query);
        request.setMaxSteps(maxSteps);
        
        AgentResponse response = agent.process(request);
        return response.getFinalAnswer();
    }
    
    public AgentResponse processRequest(AgentRequest request) {
        return agent.process(request);
    }
    
    public void registerSkill(Skill skill) {
        agent.registerSkill(skill);
    }
    
    public void clearMemory() {
        memory.clear();
    }
}
