package ltd.idcu.est.agent.impl;

import ltd.idcu.est.agent.api.*;
import ltd.idcu.est.agent.skill.CalculatorSkill;
import ltd.idcu.est.agent.skill.WebSearchSkill;
import ltd.idcu.est.test.annotation.Test;
import static ltd.idcu.est.test.Assertions.*;

import java.util.List;

public class DefaultAgentTest {
    
    @Test
    public void testCreateAgent() {
        Agent agent = new DefaultAgent();
        assertNotNull(agent);
    }
    
    @Test
    public void testCreateAgentWithMemory() {
        Memory memory = new InMemoryMemory();
        Agent agent = new DefaultAgent(memory);
        assertNotNull(agent);
        assertEquals(memory, agent.getMemory());
    }
    
    @Test
    public void testSetAndGetMemory() {
        DefaultAgent agent = new DefaultAgent();
        
        Memory memory1 = new InMemoryMemory();
        agent.setMemory(memory1);
        assertEquals(memory1, agent.getMemory());
        
        Memory memory2 = new InMemoryMemory();
        agent.setMemory(memory2);
        assertEquals(memory2, agent.getMemory());
    }
    
    @Test
    public void testRegisterSkill() {
        Agent agent = new DefaultAgent();
        
        Skill skill = new CalculatorSkill();
        agent.registerSkill(skill);
        
        List<Skill> skills = agent.getAllSkills();
        assertEquals(1, skills.size());
        assertEquals("calculator", skills.get(0).getName());
    }
    
    @Test
    public void testRegisterMultipleSkills() {
        Agent agent = new DefaultAgent();
        
        agent.registerSkill(new CalculatorSkill());
        agent.registerSkill(new WebSearchSkill());
        
        List<Skill> skills = agent.getAllSkills();
        assertEquals(2, skills.size());
    }
    
    @Test
    public void testGetSkillByName() {
        Agent agent = new DefaultAgent();
        
        Skill calculator = new CalculatorSkill();
        agent.registerSkill(calculator);
        
        Skill retrieved = agent.getSkill("calculator");
        assertNotNull(retrieved);
        assertEquals("calculator", retrieved.getName());
    }
    
    @Test
    public void testGetSkillNotFound() {
        Agent agent = new DefaultAgent();
        
        Skill skill = agent.getSkill("nonexistent");
        assertNull(skill);
    }
    
    @Test
    public void testUnregisterSkill() {
        Agent agent = new DefaultAgent();
        
        agent.registerSkill(new CalculatorSkill());
        assertEquals(1, agent.getAllSkills().size());
        
        agent.unregisterSkill("calculator");
        assertEquals(0, agent.getAllSkills().size());
    }
    
    @Test
    public void testGetAllSkillsEmpty() {
        Agent agent = new DefaultAgent();
        List<Skill> skills = agent.getAllSkills();
        assertNotNull(skills);
        assertTrue(skills.isEmpty());
    }
    
    @Test
    public void testProcessRequest() {
        Agent agent = new DefaultAgent();
        agent.registerSkill(new CalculatorSkill());
        
        AgentRequest request = new AgentRequest();
        request.setQuery("Calculate 2 + 2");
        request.setMaxSteps(5);
        
        AgentResponse response = agent.process(request);
        assertNotNull(response);
        assertNotNull(response.getFinalAnswer());
    }
    
    @Test
    public void testAgentResponseHasSteps() {
        Agent agent = new DefaultAgent();
        
        AgentRequest request = new AgentRequest();
        request.setQuery("Test query");
        
        AgentResponse response = agent.process(request);
        assertNotNull(response.getSteps());
    }
    
    @Test
    public void testSkillExecution() {
        Agent agent = new DefaultAgent();
        
        Skill testSkill = new Skill() {
            @Override
            public String getName() {
                return "test_skill";
            }
            
            @Override
            public String getDescription() {
                return "Test skill";
            }
            
            @Override
            public void initialize(SkillContext context) {
            }
            
            @Override
            public SkillResult execute(SkillInput input, SkillContext context) {
                return SkillResult.success("Test result");
            }
            
            @Override
            public void cleanup(SkillContext context) {
            }
        };
        
        agent.registerSkill(testSkill);
        
        Skill retrieved = agent.getSkill("test_skill");
        SkillResult result = retrieved.execute(new SkillInput(), null);
        
        assertTrue(result.isSuccess());
        assertEquals("Test result", result.getData());
    }
}
