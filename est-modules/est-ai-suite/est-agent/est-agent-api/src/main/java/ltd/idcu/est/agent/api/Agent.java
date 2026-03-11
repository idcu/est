package ltd.idcu.est.agent.api;

import java.util.List;

public interface Agent {
    
    AgentResponse execute(String task);
    
    AgentResponse execute(AgentRequest request);
    
    void addSkill(Skill skill);
    
    void removeSkill(String skillName);
    
    List<Skill> getSkills();
    
    void setMemory(Memory memory);
    
    Memory getMemory();
    
    void setSystemPrompt(String systemPrompt);
    
    String getSystemPrompt();
    
    void reset();
}
